package app.bean.auth;

/**
 * 
 * 功能描述 : 用户所拥有的访问权限
 * @author : YH
 * @version:
 */
public class AccessControlBean {
	/** 权限代码 */
	private String authCode;
	/** 角色代码 */
	private String roleCode;
	/** 权限名称 */
	private String authName;
	/** 角色名称 */
	private String roleName;
	/** 菜单名称 */
	private String urlName;
	/** 菜单URL */
	private String url;

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
