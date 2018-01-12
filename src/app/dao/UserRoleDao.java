package app.dao;

import java.util.List;

import spark.db.ObjectDao;
import app.common.Constant;
import app.entity.SysUserRole;

/**
 * 
 * 功能描述 : 用户角色类数据访问 
 * @author : YH
 * @version: 
 * @param <T>
 */
public class UserRoleDao<T> extends ObjectDao<T> {
	
	
	private final static String USERCODE_SQL = "SELECT * FROM SYS_USER_ROLE where usercode = ?";
	/**
	 * 根据usercode查询用户的角色信息
	 * @param usercode 用户代码
	 * @return 用户角色列表
	 */
	public List<?> getRolesByUserCode(String usercode){
		return super.findBySqlList(Constant.SOURCEID, USERCODE_SQL, SysUserRole.class, new Object[]{usercode});
	}
	
	private final static String REMOVE_SQL = "delete from SYS_USER_ROLE where usercode = ?";
	/**
	 * 移除usercode所拥有的角色
	 * @param usercode 用户代码
	 * @return
	 */
	public int removeRoleByUsercode(String usercode) {
		return super.update(Constant.SOURCEID, REMOVE_SQL, new Object[]{usercode});
	}
	
	private final static String BATCHSAVE_SQL = "insert into SYS_USER_ROLE(rolecode,usercode) values(?,?)";
	/**
	 * 批量保存
	 * @param objs
	 * @return
	 */
	public boolean batchSave(Object[][] objs) {
		return super.batchUpdate(Constant.SOURCEID, BATCHSAVE_SQL, objs);
	}
	
	private final static String BATCHDELETE_SQL = "delete from SYS_USER_ROLE where rolecode = ? and usercode = ?";
	/**
	 * 批量删除
	 * @param objs
	 * @return
	 */
	public boolean batchDelete(Object[][] objs) {
		return super.batchUpdate(Constant.SOURCEID, BATCHDELETE_SQL, objs);
	}
}
