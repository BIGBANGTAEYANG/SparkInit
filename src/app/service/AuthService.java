package app.service;

import java.util.List;
import java.util.Map;

import app.bean.InfoBean;
import app.entity.SysMenuAuth;
import spark.Request;
import spark.page.easyui.Page;

public interface AuthService {

	/**
	 * 菜单功能列表
	 * @param p
	 * @return
	 */
	public Page findAuths(Page p);
	
	/**
	 * 检查当前登录用户所拥有的权限列表
	 * @param req
	 * @param code URL权限参数
	 * @return
	 */
	public List<String> checkFuncitonAuths(Request req, String code);
	
	/**
	 * 获取菜单功能
	 * @param p
	 * @param menuid 菜单代码
	 * @return
	 */
	public Page findMenuAuths(Page p, long menuid);
	
	/**
	 * 更新权限基本信息
	 * @param authcode 权限代码
	 * @param authname 权限名称
	 * @param status 状态
	 * @param des 描述
	 * @param updateFlag 更新状态 1-修改   2-新增
	 * @param _usercode 操作员用户代码
	 * @return
	 */
	public InfoBean updateAuth(String authcode, String authname, String status, String des, String updateFlag, String _usercode);
	
	/**
	 * 删除权限
	 * @param authcode 权限代码
	 * @param _usercode 操作员用户代码
	 * @return
	 */
	public InfoBean deleteAuth(String authcode, String _usercode);
	
	/**
	 * 获取菜单权限
	 * @param menuid 菜单代码
	 * @return
	 */
	public List<SysMenuAuth> findMenuAuth(long menuid);
	
	/**
	 * 更新菜单功能
	 * @param menuid 菜单代码
	 * @param auths 功能代码，以"|"分隔
	 * @param _usercode 操作员用户代码
	 * @return
	 */
	public InfoBean updateMenuAuths(long menuid, String auths, String _usercode);
	
	/**
	 * 取角色拥有的菜单功能
	 * @param rolecode 角色代码
	 * @return
	 */
	public Map<String, Object> findRoleMenuAuth(String rolecode);
	
	/**
	 * 描述：获取菜单取所有功能
	 * @return Map<menuid,List<authcode>>
	 */
	public Map<Long, List<String>> findMenuAuthMap();
	
	/**
	 * 更新角色菜单权限信息
	 * @param param 更新的数据（menuid_authid,menuid_authid,menuid_authid）
	 * @param rolecode 角色代码
	 * @param _usercode 操作员用户代码
	 * @return
	 */
	public InfoBean updateRoleMenuAuth(String param, String rolecode, String _usercode);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 查询所有菜单功能，并标注菜单已有的功能
	 * @param menuid
	 * @return
	 */
	public List<Map<String, Object>> findAuthByMenuid(long menuid);
	
	public Map<String, Object> findRoleMenuAuthTmp(String rolecode);
	
	public boolean isHavePermission(Request req, String url); 
	
}
