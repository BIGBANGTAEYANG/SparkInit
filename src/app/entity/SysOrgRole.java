package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : 机构角色表 
 * @author : YH
 * @version:
 */
@Table(name="SYS_ORG_ROLE")
public class SysOrgRole {
	/**角色代码*/
	private String rolecode;

	/**用户代码*/
	private String orgcode;

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
}
