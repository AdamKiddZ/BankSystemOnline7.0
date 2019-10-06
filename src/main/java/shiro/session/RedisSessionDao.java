package shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * 从redis中取得session的Dao层
 * 因为Session被存放在Redis中
 */
public class RedisSessionDao extends AbstractSessionDAO {
//    @Autowired
//    private RedisUtil redisUtil;

    @Autowired
    private JedisPool jedisPool;
    /**
     * 常量，Shiro的前缀
     */
    private static final String SHIRO_SESSION_PREFIX = "shiro_session:";

    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    /**
     * 将Session序列化存储到Redis中
     * key就是SHIRO_SESSION_PREFIX+key
     * 采用二进制形式存储
     * 这样就不会产生机器间的差距而出现的编码不统一情况
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
//        将SessionId和Session绑定起来
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        Jedis jedis = jedisPool.getResource();
        byte[] value;
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
//        反序列化
        return (Session) SerializationUtils.deserialize(value);
    }

    //    create和update共用保存session到redis的方法
    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            Jedis jedis = jedisPool.getResource();
            try {
                jedis.setex(key, 600, value);
            } finally {
                jedis.close();
            }
        }
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            //如果会话过期/停止 没必要再更新了
            return;
        }
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        byte[] key = getKey(session.getId().toString());
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取所有活跃的Session
     * 以常量SHIRO_SESSION_PREFIX为前缀
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Jedis jedis = jedisPool.getResource();
        Set<Session> sessions = new HashSet<>();
        try {
            Set<byte[]> keys = jedis.keys((SHIRO_SESSION_PREFIX + "*").getBytes());
            if (CollectionUtils.isEmpty(keys)) {
                return sessions;
            }
            keys.forEach(p -> {
                byte[] value = jedis.get(p);
                sessions.add((Session) SerializationUtils.deserialize(value));
            });

        } finally {
            jedis.close();
        }
        return sessions;
    }
}
