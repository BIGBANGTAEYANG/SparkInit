package app.dao;

import java.util.List;

import spark.db.ObjectDao;
import spark.page.easyui.Page;
import app.common.Constant;
import app.entity.SysRoleMenuAuth;

/**
 * 
 * 功能描述 : 角色菜单权限类数据访问 
 * @author : YH
 * @version: 
 * @param <T>
 */
public class RoleMenuAuthDao<T> extends ObjectDao<T> {
	
	/**通过角色代码查询角色菜单权限基本信息SQL*/
	private final static String FINDBYROLECODE_SQL = "select * from SYS_ROLE_MENU_AUTH where rolecode = ?";
	/**
	 * 查询角色菜单权限
	 * @param roleid 角色代码
	 * @return
	 */
	public Page findPageByRolecode(String rolecode) {
		return super.findPageByMysql(Constant.SOURCEID, FINDBYROLECODE_SQL, new Page(), new Object[]{rolecode});
	}
	
	/**
	 * 查询角色菜单权限
	 * @param roleid 角色代码
	 * @return
	 */
	public List<?> findListByRolecode(String rolecode) {
		return super.findBySqlList(Constant.SOURCEID, FINDBYROLECODE_SQL, SysRoleMenuAuth.class, new Object[]{rolecode});
	}
	
	/**删除角色菜单权限关联数据SQL*/
	private final static String DELETEROLECODE_SQL = "delete from SYS_ROLE_MENU_AUTH where rolecode=?";
	/**
	 * 删除角色所有权限
	 * @param rolecode 角色代码
	 * @return
	 */
	public boolean deleteRoleAuthByRolecode(String rolecode) {
		super.update(Constant.SOURCEID, DELETEROLECODE_SQL, new Object[]{rolecode});
		return true;
	}
	
	/**批量更新关联数据SQL*/
	private final static String BATCH_SQL = "insert into SYS_ROLE_MENU_AUTH(rolecode,menuid,authcode)values(?,?,?)";
	/**
	 * 更新角色菜单权限
	 * @param objs 更新数据
	 * @param rolecode 角色代码
	 * @return
	 */
	public boolean updateRoleMenuAuth(Object[][] objs, String rolecode) {
		return super.batchUpdate(Constant.SOURCEID, BATCH_SQL, objs);
	}
	
	/**
	 * 移除菜单权限，相应删除角色菜单权限中对应的菜单权限SQL
	 */
	private final static String DELTEMENUAUTH_BATCH_SQL = "delete from SYS_ROLE_MENU_AUTH where authcode = ? and menuid = ?";
	/**
	 * 移除菜单权限，相应删除角色菜单权限中对应的菜单权限
	 * @param objs
	 * @return
	 */
	public boolean deleteMenuAuth(Object[][] objs) {
		return super.batchUpdate(Constant.SOURCEID, DELTEMENUAUTH_BATCH_SQL, objs);
	}
}
