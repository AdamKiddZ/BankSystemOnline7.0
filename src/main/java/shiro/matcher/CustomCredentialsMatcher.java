package shiro.matcher;

import com.google.protobuf.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import util.exception.BankException;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义身份验证类
 */
public class CustomCredentialsMatcher extends HashedCredentialsMatcher {

    //private Ehcache passwordRetryCache;
    /**
     * 缓存存放用户登录的尝试次数，所有的Cache实际上都是同一个RedisCache
     */
    private Cache<String, AtomicInteger> passwordRetryCache;

    /**
     * 缓存与用户对应的SessionId
     */
    private Cache<String, Serializable> userSessionCache;

    /**
     * 缓存与用户对应的SessionId
     */
    private Cache<Serializable, Session> sessionCache;

    public CustomCredentialsMatcher(CacheManager cacheManager) {
//        使用EHCache，改为Redis做缓存
//        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
        passwordRetryCache = cacheManager.getCache(null);
        userSessionCache = cacheManager.getCache(null);
        sessionCache = cacheManager.getCache(null);
    }

    /**
     * 用户匹配登录时调用该方法
     * @param authcToken
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {

        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String loginName = token.getUsername();

        AtomicInteger retryCount = passwordRetryCache.get(loginName);
        if (retryCount == null) {
            retryCount = new AtomicInteger();
            passwordRetryCache.put(loginName, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            //throw new ExcessiveAttemptsException();
            System.out.println("输入错误次数已经达到5次");
            return false;
//            throw new BankException("输入错误次数已经达到5次");
        }

        //记得正确登录后把出错次数清空
        boolean flag = super.doCredentialsMatch(authcToken,info);
        if(flag==true){
            passwordRetryCache.remove(loginName);
            //实现对之前登陆的用户踢出
            Session session = SecurityUtils.getSubject().getSession();
            Serializable sessionId = session.getId();
            //从缓存中取出之前该用户对应的sessionId，有的话就删除
            Serializable perSessionId = userSessionCache.get(loginName);
            if (perSessionId != null) {
                sessionCache.remove(perSessionId);
            }
            userSessionCache.put(loginName, sessionId);
            System.err.println("Matcher被调用了……"+sessionId.toString());
        }
        return flag;
    }

    public Cache<String, AtomicInteger> getPasswordRetryCache() {
        return passwordRetryCache;
    }

//    public void setPasswordRetryCache(Cache<String, AtomicInteger> passwordRetryCache) {
//        this.passwordRetryCache = passwordRetryCache;
//    }

    public static void main(String[] args) {
        System.out.println(new Md5Hash("123456","天线宝宝2").toString());
    }
}
