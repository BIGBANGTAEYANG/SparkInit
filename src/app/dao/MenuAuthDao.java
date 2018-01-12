package app.dao;

import java.util.List;
import java.util.Map;

import spark.db.ObjectDao;
import app.common.Constant;
import app.entity.SysMenuAuth;

/**
 * 
 * 功能描述 : 菜单权限类数据访问 
 * @author : YH
 * @version: 
 * @param <T>
 */
public class MenuAuthDao<T> extends ObjectDao<T> {
	
	
	/**查询所有菜单权限SQL*/
	private final static String ALL_SQL = "SELECT * from SYS_MENU_AUTH";
	/**
	 * 查询所有菜单权限<br>
	 * @return 菜单权限列表
	 */
	public List<?> findAllMenuAuths(){
		return super.findBySqlList(Constant.SOURCEID, ALL_SQL, SysMenuAuth.class, null);
	}
	
	/**查询菜单拥有的功能SQL*/
	private final static String SQL = "SELECT * from SYS_MENU_AUTH where menuid = ?";
	/**
	 * 根据menuid查询菜单拥有的功能<br>
	 * @param menuid
	 * @return 权限列表
	 */
	public List<?> findAuthsByMenucode(long menuid){
		return super.findBySqlList(Constant.SOURCEID, SQL, SysMenuAuth.class, new Object[]{menuid});
	}
	
	private final static String REMOVE_SQL = "delete from SYS_MENU_AUTH where menuid = ?";
	/**
	 * 移除菜单功能
	 * @param menuid 菜单代码
	 * @return
	 */
	public int removeAuthByMenucode(long menuid) {
		return super.update(Constant.SOURCEID, REMOVE_SQL, new Object[]{menuid});
	}
	
	private final static String BATCHSAVE_SQL = "insert into SYS_MENU_AUTH(authcode,menuid) values(?,?)";
	/**
	 * 批量保存
	 * @param objs
	 * @return
	 */
	public boolean batchSave(Object[][] objs) {
		return super.batchUpdate(Constant.SOURCEID, BATCHSAVE_SQL, objs);
	}
	
	private final static String FINDAUTHBYMENUID = "SELECT a.authcode,a.authname,b.menuid FROM SYS_AUTH a LEFT JOIN SYS_MENU_AUTH b ON a.authcode = b.authcode AND b.menuid = ? ORDER BY b.menuid DESC ,a.authcode ASC";
	/**
	 * 查询所有菜单功能，并标注菜单已有的功能
	 * @param menuid
	 * @return
	 */
	public List<Map<String, Object>> findAuthByMenuid(long menuid) {
		return super.findMapListBysql(Constant.SOURCEID, FINDAUTHBYMENUID, new Object[]{menuid});
	}
}
