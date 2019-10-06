package service;

import pojo.Log;
import pojo.User;
import util.exception.BankException;

import java.math.BigDecimal;
import java.util.List;

public interface BusinessService {
    //查询方法的声明
    BigDecimal inquiry(User user) throws BankException;
    //取钱方法的声明
    String withdraw(User user, BigDecimal money) throws BankException;
    //存钱方法的声明
    String deposit(User user, BigDecimal money) throws BankException;
    //转账方法的声明
    String transfer(User sender, int receiverID, BigDecimal money) throws BankException;
    //    //添加操作记录方法的声明
//     void setLog(User user, String logType, BigDecimal amount) throws BankException;
    //获取操作记录方法的声明
    List<Log> getLogByUser(User user) throws BankException;
}
