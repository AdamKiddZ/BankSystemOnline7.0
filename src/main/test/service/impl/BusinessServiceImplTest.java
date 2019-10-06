package service.impl;

import mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pojo.User;
import service.BusinessService;
import service.UserService;
import util.exception.BankException;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/spring-*.xml"
})
public class BusinessServiceImplTest {

    @Autowired
    BusinessService businessService;

    @Autowired
    UserService userService;

    @Test
    public void withdraw() throws BankException {

        businessService.deposit(userService.login("14","123"), BigDecimal.valueOf(100));
    }

    @Test
    public void deposit() {
    }

    @Test
    public void transfer() throws BankException {
        businessService.transfer(userService.login("14","123"),15,BigDecimal.valueOf(100));
    }
}