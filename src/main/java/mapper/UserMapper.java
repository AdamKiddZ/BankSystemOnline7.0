


package mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import pojo.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * Version 2.0 with Shiro
 */
@Repository("userMapper")
public interface UserMapper {
    /**
     * 用户注册方法，返回用户注册的ID
     *
     * @param user
     * @return
     */
    @Insert("insert ignore into t_user(user_name,user_password) values (#{username},#{password})")
    int register(User user);

    /**
     * 用户注册方法，返回用户注册的ID
     *
     * @param user
     * @return
     */
    @Insert("insert into t_user_roles(user_name,role_name) values (#{username},'user')")
    int initUserRoles(User user);

    /**
     * 用户登录
     *
     * @param username
     * @return
     */
    @Select("select user_id as id,user_name as username," +
            "user_flag as flag,user_balance as balance,user_grant as adminGrant," +
            "reg_time as regtime from t_user where user_name=#{username}")
//	@ResultType()
    User login(@Param("username") String username);

    //管理员——————————————————————————

    /**
     * 管理员登录
     *
     * @param username
     * @return
     */
    @Select("select user_id as id,user_name as username," +
            "user_flag as flag,user_balance as balance,user_grant as adminGrant," +
            "reg_time as regtime from t_user where user_name=#{username} and user_grant=1")
    User adminLogin(@Param("username") String username);

    /**
     * 获取所有非管理员账户
     *
     * @return
     */
    @Select("select user_id as id,user_name as username," +
            "user_flag as flag,user_balance as balance,user_grant as adminGrant," +
            "reg_time as regtime from t_user where user_grant=0")
    List<User> getAllUsers();

    /**
     * 冻结某人的账户
     *
     * @param userid
     * @return
     */
    @Update("update t_user set user_flag=1 where user_id =#{id} and user_grant=0")
    int lockAccount(@Param("id") Integer userid);

    /**
     * 解冻某人的账户
     *
     * @param userid
     * @return
     */
    @Update("update t_user set user_flag=0 where user_id =#{id} and user_grant=0")
    int unlockAccount(@Param("id") Integer userid);

    /**
     * 查询余额
     *
     * @param userid
     * @return
     */
    @Select("select user_balance from t_user where user_id=#{id}")
    BigDecimal inquiry(@Param("id") Integer userid);

    /**
     * 取款
     *
     * @param userid
     * @param money
     * @return
     */
    @Update("update t_user u set u.user_balance=u.user_balance-#{money} where u.user_flag=0 and u.user_id=#{id} and u.user_balance>=#{money}")
    int withdraw(@Param("id") Integer userid, @Param("money") BigDecimal money);

    /**
     * 存款
     *
     * @param userid
     * @param money
     * @return
     */
    @Update("update t_user u set u.user_balance=u.user_balance+#{money} where u.user_flag=0 and u.user_id=#{id}")
    int deposit(@Param("id") Integer userid, @Param("money") BigDecimal money);
    //转账方法的声明
//	String transfer(User sender, Integer receiverID, BigDecimal money) throws BankException;
//    //添加操作记录方法的声明
//     void setLog(User user, String logType, BigDecimal amount) throws BankException;

    /**
     * 查询用户信息
     * @param username
     * @return
     */
    @Select("select user_name as username,user_password as password from t_user where user_name=#{username}")
    User getUserByUserName(@Param("username") String username);

    /**
     * 获取用户所有角色
     * @param username
     * @return
     */
    @Select("select role_name from t_user_roles where user_name=#{username}")
    List<String> queryRolesByUserName(@Param("username") String username);

    /**
     * 获取用户所有权限
     * @param rolename
     * @return
     */
    @Select("select permission from t_roles_permissions where role_name=#{rolename}")
    List<String> queryPermissionByRole(@Param("rolename") String rolename);
}