package app.dao;

import java.util.List;

import spark.db.ObjectDao;
import spark.page.easyui.Page;
import app.common.Constant;
import app.entity.SysAuth;


/**
 * 
 * 功能描述 : 权限类数据访问 
 * @author : YH
 * @version: 
 * @param <T>
 */
public class AuthDao<T> extends ObjectDao<T> {

	private final static String FINDPAGE_SQL = "select authcode,authname,des,status,status AS _status,createtime from SYS_AUTH ORDER BY authcode ASC";
	public Page findByPage(Page p) {
		return super.findPageByMysql(Constant.SOURCEID, FINDPAGE_SQL, p, null);
	}
	
	/**查询权限基本信息列表SQL*/
	private final static String FINDALL_SQL = "select * from SYS_AUTH";
	/**
	 * 查询权限基本信息列表
	 * @return
	 */
	public List<?> findAllData() {
		return super.findBySqlList(Constant.SOURCEID, FINDALL_SQL, SysAuth.class, null);
	}
	
	/**查询权限基本信息列表SQL*/
	private final static String FINDVISIBLE_SQL = "select * from SYS_AUTH WHERE status = '1'";
	/**
	 * 查询权限基本信息列表
	 * @return
	 */
	public List<?> findVisibleData() {
		return super.findBySqlList(Constant.SOURCEID, FINDVISIBLE_SQL, SysAuth.class, null);
	}
	
	/**通过权限代码查询权限基本信息SQL*/
	private final static String FIND_SQL = "select * from SYS_AUTH where authcode = ?";
	/**
	 * 通过权限代码查询权限基本信息
	 * @param authid 权限代码
	 * @return
	 */
	public SysAuth findByCode(String authcode) {
		return (SysAuth) super.findEntityBySql(Constant.SOURCEID, FIND_SQL, SysAuth.class, new Object[]{authcode});
	}
	
	/**查询用户所拥有的权限SQL*/
	private final static String SQL = "SELECT SYS_ROLE.rolename AS roleName,SYS_ROLE.rolecode AS roleCode,SYS_AUTH.authname AS authName,SYS_AUTH.authcode AS authCode,SYS_MENU.menuname AS urlName,SYS_MENU.url FROM SYS_USER_ROLE,SYS_ROLE,SYS_ROLE_MENU_AUTH,SYS_AUTH,SYS_MENU WHERE SYS_USER_ROLE.rolecode = SYS_ROLE.rolecode AND SYS_ROLE_MENU_AUTH.rolecode = SYS_ROLE.rolecode AND SYS_ROLE_MENU_AUTH.authcode = SYS_AUTH.authcode AND SYS_ROLE_MENU_AUTH.menuid = SYS_MENU.menuid AND SYS_AUTH.status = 1 AND SYS_USER_ROLE.usercode=?";
	/**查询用户所拥有的权限SQL*/
	private final static String SQL_ADMIN = "SELECT DISTINCT SYS_ROLE.rolecode AS roleCode,  SYS_ROLE.rolename AS roleName,  SYS_AUTH.authname AS authName,  SYS_AUTH.authcode AS authCode,  SYS_MENU.menuname AS urlName,  SYS_MENU.url FROM  SYS_MENU_AUTH,  SYS_AUTH,  SYS_MENU,  SYS_ROLE WHERE   SYS_MENU_AUTH.menuid = SYS_MENU.menuid   AND SYS_MENU_AUTH.authcode = SYS_AUTH.authcode  AND SYS_AUTH.status = 1 AND SYS_ROLE.rolecode = ?";
	
	/**
	 * 根据usercode查询用户所拥有的权限：<br>
	 * 包括：能访问的菜单及页上能使用的功能<br>
	 * @param usercode 用户代码
	 * @param c 表实体
	 * @return 权限列表
	 */
	@SuppressWarnings("rawtypes")
	public List<?> findAuthsByUserCode(String usercode,Class c){
		if (Constant.ROLE_ADMIN.equals(usercode)) {
			return super.findBySqlList(Constant.SOURCEID, SQL_ADMIN, c, new Object[]{usercode});
		} else {
			return super.findBySqlList(Constant.SOURCEID, SQL, c, new Object[]{usercode});
		}
	}
	
	/**修改权限基本信息*/
	private final static String MODIFY_SQL = "update SYS_AUTH set authname = ?,status = ?,createtime = ?,des = ? where authcode = ?";
	/**新增权限基本信息*/
	private final static String INSERT_SQL = "insert into SYS_AUTH (authcode,authname,status,des,createtime) values(?,?,?,?,?)";
	/**删除权限基本信息*/
	private final static String DELETE_SQL = "delete from SYS_AUTH where authcode = ?";
	/**删除权限，同步删除菜单权限关联表数据SQL*/
	private final static String DELETE_MENU_AUTH_SQL = "delete from SYS_MENU_AUTH where authcode = ?";
	/**删除权限，同步删除角色菜单权限关联表数据SQL*/
	private final static String DELETE_ROLE_MENU_AUTH_SQL = "delete from SYS_ROLE_MENU_AUTH where authcode = ?";
	/**
	 * 更新权限基本信息
	 * @param authcode 权限代码
	 * @param authname 权限名称
	 * @param status 状态
	 * @param des 描述
	 * @param createtime 更新时间
	 * @param updateFlag 1-修改     2-新增    3-删除
	 * @return 1-成功 0-失败
	 */
	public int updateAuth(String authcode, String authname, String status, String createtime, String des, int updateFlag) {
		if (updateFlag == 1) {
			// 修改
			return super.update(Constant.SOURCEID, MODIFY_SQL, new Object[]{authname,status,createtime,des,authcode});
		} else if (updateFlag == 2) {
			// 新增
			return super.update(Constant.SOURCEID, INSERT_SQL, new Object[]{authcode,authname,status,des,createtime});
		} else if (updateFlag == 3) {
			// 删除
			super.update(Constant.SOURCEID, DELETE_SQL, new Object[]{authcode});
			// 删除成功后，同步删除菜单权限关联表、角色菜单权限关联表数据
			super.update(Constant.SOURCEID, DELETE_MENU_AUTH_SQL, new Object[]{authcode});
			super.update(Constant.SOURCEID, DELETE_ROLE_MENU_AUTH_SQL, new Object[]{authcode});
			return 1;
		}
		return 0;
	}
}
