package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : 角色表 
 * @author : YH
 * @version:
 */
@Table(name="SYS_ROLE")
public class SysRole {

	/**角色代码，唯一*/
	private String rolecode;

	/**角色名称*/
	private String rolename;
	
	/**创建时间*/
	private String createtime;

    /**备注*/
	private String des;

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
}
