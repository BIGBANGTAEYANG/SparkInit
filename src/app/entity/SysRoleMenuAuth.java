package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : 角色菜单权限表
 * @author : YH
 * @version:
 */
@Table(name="SYS_ROLE_MENU_AUTH")
public class SysRoleMenuAuth {
	/**权限代码*/
	private String authcode;
   
	/**菜单代码*/
	private long menuid;

	/**角色代码*/
	private String rolecode;

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public long getMenuid() {
		return menuid;
	}

	public void setMenuid(long menuid) {
		this.menuid = menuid;
	}

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
}
