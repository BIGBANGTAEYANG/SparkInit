package app.dao;

import java.util.List;

import spark.db.ObjectDao;
import app.common.Constant;
import app.entity.SysRole;

/**
 * 
 * 功能描述 : 角色类数据访问 
 * @author : YH
 * @version: 
 * @param <T>
 */
public class RoleDao<T> extends ObjectDao<T> {
	
	private final static String ALL_SQL = "SELECT * FROM SYS_ROLE";
	/**
	 * 查询全部角色信息
	 * @return 角色列表
	 */
	public List<?> findAllRoles(){
		return super.findBySqlList(Constant.SOURCEID, ALL_SQL, SysRole.class, null);
	}
	
	/**查询机构下关联的角色列表SQL*/
	private final static String FINDBYORGCODE_SQL = "SELECT a.rolecode,a.rolename,a.des,a.createtime FROM SYS_ROLE a,SYS_ORG_ROLE b WHERE a.rolecode = b.rolecode AND b.orgcode = ? ORDER BY a.rolecode";
	/**
	 * 查询机构下关联的角色列表
	 * @param orgcode 机构代码
	 * @return
	 */
	public List<?> findRolesByOrgcode(String orgcode) {
		return super.findBySqlList(Constant.SOURCEID, FINDBYORGCODE_SQL, SysRole.class, new Object[]{orgcode});
	}
	
	private final static String FINDBYID_SQL = "SELECT * FROM SYS_ROLE where rolecode = ?";
	/**
	 * 通过rolecode查询角色信息
	 * @param rolecode 角色代码
	 * @return
	 */
	public SysRole findById(String rolecode) {
		return (SysRole)super.findEntityBySql(Constant.SOURCEID, FINDBYID_SQL, SysRole.class, new Object[]{rolecode});
	}
	
	private final static String INSERT_SQL = "insert into SYS_ROLE(rolecode,rolename,des,createtime) value (?,?,?,?)";
	/**
	 * 新增角色
	 * @param sr 角色信息
	 * @return
	 */
	public int insertRole(SysRole sr) {
		return super.update(Constant.SOURCEID, INSERT_SQL, new Object[]{sr.getRolecode(),sr.getRolename(),sr.getDes(),sr.getCreatetime()});
	}
	
	private final static String UPDATE_SQL = "update SYS_ROLE set rolename=?, des=?, createtime=? where rolecode = ?";
	/**
	 * 更新角色信息
	 * @param sr 角色信息
	 * @return
	 */
	public int updateRole(SysRole sr) {
		return super.update(Constant.SOURCEID, UPDATE_SQL, new Object[]{sr.getRolename(),sr.getDes(),sr.getCreatetime(),sr.getRolecode()});
	}
	
	/**删除角色*/
	private final static String DELETE_SQL = "delete from SYS_ROLE where rolecode = ?";
	/**删除用户角色关系*/
	private final static String DELETE_USERROLE_SQL = "delete from SYS_USER_ROLE where rolecode = ?";
	/**删除角色菜单权限关联数据SQL*/
	private final static String DELETE_ROLEMENUAUTH_SQL = "delete from SYS_ROLE_MENU_AUTH where rolecode = ?";
	/**删除机构-角色信息数据SQL*/
	private final static String DELETE_ORG_ROLES_SQL = "delete from SYS_ORG_ROLE where rolecode = ?";
	/**
	 * 删除角色信息（同步删除用户角色关系、角色菜单权限关联、角色涉密数据权限数据、机构-角色关联关系数据）
	 * @param rolecode 角色代码
	 * @return
	 */
	public int deleteRole(String rolecode) throws Exception {
		super.update(Constant.SOURCEID, DELETE_ROLEMENUAUTH_SQL, new Object[]{rolecode});
		super.update(Constant.SOURCEID, DELETE_USERROLE_SQL, new Object[]{rolecode});
		super.update(Constant.SOURCEID, DELETE_ORG_ROLES_SQL, new Object[]{rolecode});
		return super.update(Constant.SOURCEID, DELETE_SQL, new Object[]{rolecode});
	}
}
