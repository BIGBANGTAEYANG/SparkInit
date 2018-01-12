package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : 用户角色表 
 * @author : YH
 * @version:
 */
@Table(name="SYS_USER_ROLE")
public class SysUserRole {
	/**角色代码*/
	private String rolecode;

	/**用户代码*/
	private long userid;

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}
}
