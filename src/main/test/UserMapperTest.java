import mapper.UserMapper;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pojo.User;
import service.UserService;
import util.exception.BankException;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath:spring/spring-*.xml"}
)
public class UserMapperTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="userMapper")
    private UserMapper userMapper;

    @Resource(name="userService")
    private UserService userService;

    @org.junit.Test
    public void register() throws BankException {
        int id=userService.register("test","123");
        System.out.println(id);
    }

    @org.junit.Test
    public void login() {
        User user=userMapper.login("123");
        System.out.println(user);
    }

    @org.junit.Test
    public void get() {
        User user=userMapper.login("天线宝宝");
        System.out.println(user);
    }

    @org.junit.Test
    public void isUserExisted() {
        System.out.println(userMapper.login("18"));
    }

    @org.junit.Test
    public void adminLogin() {
        User admin=userMapper.adminLogin("123");
        System.out.println(admin);
    }

    @org.junit.Test
    public void getAllUsers() {
        List users=userMapper.getAllUsers();
        users.forEach(p->
        {
            System.out.println(p);
        });
    }

    @org.junit.Test
    public void lockAccount() {
        userMapper.lockAccount(16);
    }

    @org.junit.Test
    public void unlockAccount() {
        System.out.println(userMapper.unlockAccount(16));
    }

    @org.junit.Test
    public void inquiry() {
        System.out.println(userMapper.inquiry(14));
    }

    @org.junit.Test
    public void withdraw() {
        System.out.println(userMapper.withdraw(14, BigDecimal.valueOf(100)));
    }

    @org.junit.Test
    public void deposit() {
        System.out.println(userMapper.deposit(14,BigDecimal.valueOf(100)));
    }
}