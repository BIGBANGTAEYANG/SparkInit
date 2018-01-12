package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : 用户表
 * @author : YH
 * @version:
 */
@Table(name="SYS_USER")
public class SysUser {

	/**用户代码，唯一*/
	private long userid;

	/**用户账号*/
	private String usercode;
	
	/**用户名称*/
	private String username;
	
	/**密码*/
	private String userpwd;
	
	/**用户所属机构代码*/
	private String orgCode;
    
	/**备注*/
	private String des;
 
	/**是否有效: 1有效  0无效，默认值为1*/
	private int status;
	
	/**创建时间*/
	private String createtime;
	
	/**最后一次登录时间*/
	private String lastlogintime;

	/**员工编号*/
	private String empid;
	
	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public SysUser() {
    }
	
	public long getUserid() {
		return userid;
	}
	
	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

}