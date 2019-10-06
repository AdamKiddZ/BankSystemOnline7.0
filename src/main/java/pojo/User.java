


package pojo;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
* @author Adam_Zed
* @version 1.4
* 封装了用户数据的用户实体类
*/

@Getter
@Setter
@RequiredArgsConstructor()
@NoArgsConstructor
@ToString
public class User {
	//用户id、用户名、密码、是否冻结、以及账户金额等
	@NonNull
	private int id;

	@NonNull
	private String username;

	@NonNull
	private String password;

	@NonNull
	private Integer flag;

	/**
	 * 带参构造方法不包括balance
	 */
	private BigDecimal balance;

	@NonNull
	private Integer adminGrant;

	@NonNull
	private Timestamp regtime;

//	@OneToMany(mappedBy="user",cascade = CascadeType.ALL)
//	/**
//	 * 一对多。集合属性为懒加载
//	 */
//	private List<Log> logs;

	public String toInfoString(){
		return "用户ID："+id+"\t用户名："+username+"\t状态："+(flag==1?"已冻结":"未冻结");
	}

//	public User toUserExampleWithoutBalance(){
//		return new User(this.id,this.username,this.password,this.flag,this.adminGrant,this.regtime);
//	}
}