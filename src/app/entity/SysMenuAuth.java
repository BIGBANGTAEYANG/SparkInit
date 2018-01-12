package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : 菜单权限表
 * @author : YH
 * @version:
 */
@Table(name="SYS_MENU_AUTH")
public class SysMenuAuth {
	/**权限代码*/
	private String authcode;

	/**菜单代码*/
	private long menuid;

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
}
