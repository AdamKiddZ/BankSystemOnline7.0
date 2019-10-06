


package mapper;


import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import pojo.Log;
import pojo.User;
import util.exception.BankException;

import java.math.BigDecimal;
import java.util.List;

@Repository("logMapper")
public interface LogMapper {
	/**
	 * 获取操作记录
	 * @param userid
	 * @return
	 */
	@Results({
		@Result(column = "log_id",property ="logid"),
		@Result(column = "log_type",property = "logtype"),
		@Result(column = "log_amount",property = "amount"),
		@Result(column = "log_time",property = "logtime"),
		@Result(property="user",
				column="user_id",
				one=@One(select="mapper.UserMapper.getById",fetchType=FetchType.EAGER),javaType = User.class),}
				)
	@Select("select * from t_log where user_id =#{user_id}")
	List<Log> getLogsByUser(@Param("user_id") Integer userid);

	/**
	 * 添加用户操作日志
	 */
	@Insert("insert ignore into t_log(user_id,log_type,log_amount) values(#{uid},#{type},#{amount})")
	int addLog(@Param("uid") int userid,@Param("type") String type,@Param("amount") BigDecimal amount);
}