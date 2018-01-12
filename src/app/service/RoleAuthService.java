package app.service;

import java.util.Map;

import spark.Request;
import app.bean.auth.AccessControlBean;


public interface RoleAuthService {
	
	/**
	 * 重新加载用户所拥有的权限
	 * @param usercode 用户代码
	 * @return 权限列表
	 */
	public String reloadAuth(String usercode);
	
	/**
	 * 从session里获取用户功能权限
	 * @param req 请求
	 * @return 权限列表
	 */
	public Map<String,Map<String,AccessControlBean>> reloadAuthBySession(Request req);
	
}
