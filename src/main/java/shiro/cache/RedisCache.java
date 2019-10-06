package shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;
import util.CustomJedisUtil;

import java.util.Collection;
import java.util.Set;

public class RedisCache<K,V> implements Cache<K,V> {
    private static final String SHIRO_CACHE_PREFIX="shiro_cache:";

    @Autowired
    private CustomJedisUtil customJedisUtil;
    /**
     * 获取key的二进制数组
     * @param k
     * @return
     */
    private byte[] getKey(K k){
        if(k instanceof String){
            return (SHIRO_CACHE_PREFIX+k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    @Override
    public V get(K k) throws CacheException {
        byte[] value=customJedisUtil.get(getKey(k));
        if(value==null){
            return (V)SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key=getKey(k);
        byte[] value=SerializationUtils.serialize(v);
        customJedisUtil.set(key,value);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key=getKey(k);
        byte[] value=customJedisUtil.get(key);
        customJedisUtil.del(key);
        if(value!=null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        /**
         * 默认空实现
         */
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set keys() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }
}
