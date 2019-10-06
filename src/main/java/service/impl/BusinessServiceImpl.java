package service.impl;

import mapper.LogMapper;
import mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pojo.Log;
import pojo.User;
import service.BusinessService;
import util.exception.AccountOverDrawnException;
import util.exception.BankException;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;


@Service("businessService")
public class BusinessServiceImpl implements BusinessService {

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Resource(name = "logMapper")
    private LogMapper logMapper;

    private String getReturnMessage(User user) throws BankException {
        //格式化BigDecimal数据精度
        //#.00 表示两位小数 #.0000四位小数
        DecimalFormat df = new DecimalFormat("0.0000");
        return "账户:" + user.getUsername() + " 当前余额为：" + df.format(inquiry(user)) + "元";
    }

    @Override
    public BigDecimal inquiry(User user) throws BankException {
        return userMapper.inquiry(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false, rollbackFor = BankException.class)
    public String withdraw(User user, BigDecimal money) throws BankException {
        if (user.getFlag() == 1) {
            throw new BankException("当前账户已冻结！");
        }
        //用户账户余额减少（先判断余额够不够）
        if (user.getBalance().compareTo(money) < 0) {
            throw new AccountOverDrawnException("账户余额不足！");
        }
        //返回的信息
        String returnWord = null;
        try {
            if (userMapper.withdraw(user.getId(), money) > 0 && logMapper.addLog(user.getId(), "取款", money) > 0) {
                returnWord = "取款成功！" + getReturnMessage(user);
                user.setBalance(user.getBalance().subtract(money));
            } else {
                throw new BankException("取款失败！");
            }
        } catch (BankException be) {
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            //发生错误则用户余额不变
            throw new BankException("取款失败！");
        }
        return returnWord;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false, rollbackFor = BankException.class)
    public String deposit(User user, BigDecimal money) throws BankException {
        if (user.getFlag() == 1) {
            throw new BankException("当前账户已冻结！");
        }
        //返回的信息
        String returnWord = null;
        try {
            if (userMapper.deposit(user.getId(), money) > 0 && logMapper.addLog(user.getId(), "存款", money) > 0) {
                returnWord = "存款成功！" + getReturnMessage(user);
                user.setBalance(user.getBalance().add(money));
            } else {
                throw new BankException("存款失败");
            }
        } catch (BankException be) {
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BankException("存款失败");
        }
        return returnWord;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, readOnly = false, rollbackFor = BankException.class)
    public String transfer(User sender, int receiverID, BigDecimal money) throws BankException {
        //先判断账户是否已经冻结
        if (sender.getFlag() == 1) {
            throw new BankException("当前账户已冻结！");
        }

        //转账账户余额减少（先判断余额够不够）
        if (sender.getBalance().compareTo(money) < 0) {
            throw new AccountOverDrawnException("账户余额不足！");
        }

        //返回的信息
        String returnWord = null;
        try {
            /**
             * TODO——————————————————————————————
             */
            if (userMapper.withdraw(sender.getId(), money) > 0
                    && logMapper.addLog(sender.getId(), "转账，给：" + receiverID, money) > 0
                    && userMapper.deposit(receiverID, money) > 0
                    && logMapper.addLog(receiverID,"收到转账，从："+sender.getId(),money)>0) {
                returnWord = "转账成功！" + getReturnMessage(sender);
                sender.setBalance(sender.getBalance().subtract(money));
            } else {
                throw new BankException("转账失败");
            }
        } catch (BankException be) {
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BankException("转账失败");
        }
        return returnWord;
    }

    @Override
    public List<Log> getLogByUser(User user) throws BankException {
        return logMapper.getLogsByUser(user.getId());
    }
}
