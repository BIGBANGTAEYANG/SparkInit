package app.dao;

import spark.db.ObjectDao;
import spark.page.easyui.Page;

import java.util.List;
import java.util.Map;
import app.bean.user.UserBean;
import app.common.Constant;
import app.entity.SysUser;

/**
 * 
 * 功能描述 : 用户类数据访问 
 * @author : YH
 * @version: 
 * @param <T>
 */
public class UserDao<T> extends ObjectDao<T> {
	
	private final static String INSERT_SQL = "insert into SYS_USER(usercode,username,userpwd,orgcode,createtime,des,status,lastlogintime,empid)values(?,?,?,?,?,?,?,?,?)";
	/**
	 * 新增用户
	 * @param user
	 */
	public int save(SysUser user) {
		return super.update(Constant.SOURCEID, INSERT_SQL, new Object[]{user.getUsercode(),user.getUsername(),user.getUserpwd(),user.getOrgCode(),user.getCreatetime(),user.getDes(),user.getStatus(),user.getLastlogintime(),user.getEmpid()});
	}
	
	private final static String MODIFYBYCODE_LOGIN_SQL = "UPDATE SYS_USER SET username = ?, orgcode = ?, lastlogintime = ?, STATUS = ?, des = ?, empid = ?  WHERE usercode = ?";
	private final static String MODIFYBYCODE_SQL = "UPDATE SYS_USER SET username = ?, orgcode = ?, STATUS = ?, des = ?, empid = ?  WHERE usercode = ?";
	/**
	 * 修改用户信息
	 * @param user
	 * @param islogin true:登录  false:非登录
	 * @return
	 */
	public int updateUser(SysUser user, boolean islogin) {
		if (islogin) {
			return super.update(Constant.SOURCEID, MODIFYBYCODE_LOGIN_SQL, new Object[]{user.getUsername(),user.getOrgCode(),user.getLastlogintime(),user.getStatus(),user.getDes(),user.getEmpid(),user.getUsercode()});
		} else {
			return super.update(Constant.SOURCEID, MODIFYBYCODE_SQL, new Object[]{user.getUsername(),user.getOrgCode(),user.getStatus(),user.getDes(),user.getEmpid(),user.getUsercode()});
		}
	}
	
	/**
	 * 修改用户密码SQL
	 */
	private final static String MODIFYUSERPWD_SQL = "UPDATE SYS_USER set userpwd = ? where usercode = ?";
	/**
	 * 用户密码修改
	 * @param usercode 用户代码
	 * @param userpwd 新密码
	 * @return
	 */
	public int updateUserPwd(String usercode, String userpwd) {
		return super.update(Constant.SOURCEID, MODIFYUSERPWD_SQL, new Object[]{userpwd, usercode});
	}
	
	private final static String REMOVE_USERROLE_SQL = "delete from SYS_USER_ROLE where usercode = ?";
	private final static String DELETEBYCODE_SQL = "delete from SYS_USER where usercode = ?";
	/**
	 * 根据usercode删除用户（同步删除用户角色关系、用户临时授权的涉密数据）
	 * @param usercode 用户代码
	 * @return
	 */
	public int deleteUser(String usercode) {
		// 移除用户角色关系
		super.update(Constant.SOURCEID, REMOVE_USERROLE_SQL, new Object[]{usercode});
		// 移除用户
		return super.update(Constant.SOURCEID, DELETEBYCODE_SQL, new Object[]{usercode});
	}
	
	private final static String USERID_SQL = "select * from SYS_USER where userid = ?";
	/**
	 * 根据用户代号查询用户
	 * @param usercode 用户代号
	 * @return
	 */
	public SysUser getUserById(long userid) {
		return (SysUser) super.findEntityBySql(Constant.SOURCEID, USERID_SQL, SysUser.class, new Object[]{userid});
	}
	
	private final static String USERCODE_SQL = "select * from SYS_USER where usercode = ?";
	/**
	 * 根据usercode查询用户
	 * @param usercode 用户昵称
	 * @return
	 */
	public SysUser getUserByCode(String usercode) {
		return (SysUser) super.findEntityBySql(Constant.SOURCEID, USERCODE_SQL, SysUser.class, new Object[]{usercode});
	}
	
	/**关联机构表查询用户信息SQL*/
	private final static String USERCODE_ORG_SQL = "SELECT su.*,so.orgname FROM SYS_USER su, SYS_ORG so WHERE su.orgcode = so.orgcode AND su.usercode = ?";
	/**
	 * 关联机构表查询用户信息
	 * @param usercode 用户账号
	 * @return
	 */
	public UserBean getUserBeanByCode(String usercode) {
		return (UserBean) super.findEntityBySql(Constant.SOURCEID, USERCODE_ORG_SQL, UserBean.class, new Object[]{usercode});
	}
	
	private final static String FINDPAGEUSER_SQL="SELECT t.userid, t.usercode, t.username, t.empid, t.orgcode, d.orgname, c.roles, t.status,t.status AS _status, t.des, t.createtime, t.lastlogintime FROM SYS_USER t "
			+ "LEFT JOIN (SELECT a.usercode, GROUP_CONCAT(b.rolename) roles FROM SYS_USER a "
			+ "LEFT JOIN (SELECT u.usercode,u.rolecode, r.rolename FROM SYS_USER_ROLE u  "
			+ "LEFT JOIN SYS_ROLE r ON u.rolecode=r.rolecode) b ON a.usercode=b.usercode GROUP BY a.usercode) c ON t.usercode=c.usercode "
			+ "LEFT JOIN SYS_ORG d ON t.orgcode=d.orgcode ORDER BY t.userid ASC";
	
	public Page findPageAllDatas(Page p) {
		return super.findPageByMysql(Constant.SOURCEID, FINDPAGEUSER_SQL, p, null);
	} 
	
	/*private final static String CHECKISADMIN="SELECT * FROM SYS_USER_ROLE WHERE usercode=? AND rolecode='admin'";
	
	public int checkIsAdmin(String usercode){
		List<Map<String, Object>> roles=super.findMapListBysql(Constant.SOURCEID, CHECKISADMIN, new Object[]{usercode});
		if(roles.size()==1){
			return 1;
		}else {
			return 0;
		}
	}*/

	private final static String USERROLES="SELECT GROUP_CONCAT(rolecode) roles FROM SYS_USER_ROLE WHERE usercode=?";
	
	public String getUserRoles(String userCode) {
		Map<String, Object> map=super.findMapBysql(Constant.SOURCEID, USERROLES, new Object[]{userCode});
		return (String) map.get("roles");
	}
	
	private final static String FINDUSERCODEBYORGCODE_SQL = "SELECT usercode FROM SYS_USER WHERE orgcode = ?";
	public List<Map<String, Object>> getUsercodesByOrgcode(String orgcode) {
		return super.findMapListBysql(Constant.SOURCEID, FINDUSERCODEBYORGCODE_SQL, new Object[]{orgcode});
	}
}
