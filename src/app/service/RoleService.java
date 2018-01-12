package app.service;

import app.bean.InfoBean;
import spark.page.easyui.Page;

public interface RoleService {

	/**
	 * 获取角色列表
	 * @param p
	 * @return
	 */
	public Page findRoles(Page p);
	/**
	 * 更新角色信息
	 * @param rolecode 角色代码
	 * @param rolename 角色名称
	 * @param des 描述
	 * @param opflag "0"-新增  "1"-修改
	 * @param _usercode 操作员用户代码
	 * @return
	 */
	public InfoBean updateRole(String rolecode, String rolename, String des, String opflag, String _usercode);
	/**
	 * 删除角色信息（同步删除用户角色关系、角色菜单权限关联、角色涉密数据权限数据）
	 * @param rolecode 角色代码
	 * @param _usercode 操作员用户代码
	 * @return
	 */
	public InfoBean deleteRole(String rolecode, String _usercode);
	
}
