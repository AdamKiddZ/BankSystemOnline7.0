package util;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CustomJedisUtil {

    @Autowired
    private JedisPool jedisPool;

    public byte[] get(byte[] key){
        Jedis jedis=jedisPool.getResource();
        try{
            return jedis.get(key);
        }finally {
            jedis.close();
        }
    }

    public void set(byte[] key,byte[] value){
        Jedis jedis=jedisPool.getResource();
        try{
            jedis.setex(key,600,value);
        }finally {
            jedis.close();
        }
    }

    public void del(byte[] key){
        Jedis jedis=jedisPool.getResource();
        try{
            jedis.del(key);
        }finally {
            jedis.close();
        }
    }
}
