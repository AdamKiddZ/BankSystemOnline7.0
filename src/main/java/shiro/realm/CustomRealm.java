package shiro.realm;

import mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {
//    Map<String,String> userMap=new HashMap<>(16);
//    {
//        /**
//         * 使用保存的加密之后的密码，MD5加密一次
//         * 加密之前：123456，
//         * 加密之后：e10adc3949ba59abbe56e057f20f883e
//         * 加盐："adam"
//         * 加盐之后：e1407abc55f5dce04126d62097a5058c
//         */
//        userMap.put("adam","e1407abc55f5dce04126d62097a5058c");
//        super.setName("customRealm");
//    }

    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //模拟从数据库或者缓存中获取角色的数据
        Set<String> roles = getRolesByUserName(userName);
        //模拟从数据库或者缓存中获取角色的授权
        Set<String> permission = getPermissionsByRoles(roles);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    private Set<String> getRolesByUserName(String userName) {
        List<String> list = userMapper.queryRolesByUserName(userName);
        Set<String> sets = new HashSet<>(list);
        return sets;
    }

    private Set<String> getPermissionsByRoles(Set<String> roles) {
        Set<String> sets = new HashSet<>();
        roles.forEach(p -> {
            List<String> permissions = userMapper.queryPermissionByRole(p);
            sets.addAll(permissions);
        });
        return sets;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从主体中传过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();
        //通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(userName);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, password, "customRealm");
        /**
         * 返回AuthenticationInfo对象之前
         * 需要将盐值设入进去
         */
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    private String getPasswordByUserName(String userName) {
        User user = userMapper.getUserByUserName(userName);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new Md5Hash("123456", "adam"));
    }
}
