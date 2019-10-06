package service.impl;

import mapper.UserMapper;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pojo.User;
import service.UserService;
import util.MD5Util;
import util.exception.BankException;

import java.util.List;

@Service("userService")
//@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ,readOnly = false,rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("userMapper")
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false, rollbackFor = BankException.class)
    public int register(String username, String password) throws BankException {
        //		对密码进行加密，md5加盐加密，使用用户名作为盐值
        password= new Md5Hash(password, username).toString();
        User user =new User();
        user.setUsername(username);
        user.setPassword(password);
        try {
            userMapper.register(user);
            userMapper.initUserRoles(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user.getId();
    }

    @Override
    public User login(String username, String password) throws BankException {
//        //		对密码进行加密
//        password= MD5Util.getMD5(password);
        return userMapper.login(username);
    }

    /**
     * 看该用户是否存在，尝试登录
     * @param username
     * @return
     * @throws BankException
     */
    @Override
    public boolean isUserExisted(String username) throws BankException {
//        int index=userMapper.getById(userID);
//        return index>0;
        User user=userMapper.login(username);
        return user!=null;
    }

    @Override
    public User adminLogin(String username, String password) throws BankException {
//        //		对密码进行加密
//        password= MD5Util.getMD5(password);
        return userMapper.adminLogin(username);
    }

    @Override
    public List<User> getAllUsers() throws BankException {
        return userMapper.getAllUsers();
    }

    @Override
    public boolean lockAccount(int userid) throws BankException {
        int index=userMapper.lockAccount(userid);
        return index>0;
    }

    @Override
    public boolean unlockAccount(int userid) throws BankException {
        int index=userMapper.unlockAccount(userid);
        return index>0;
    }
}
