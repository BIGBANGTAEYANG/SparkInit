package app.service;

import java.util.List;
import java.util.Map;

import app.bean.InfoBean;
import app.bean.user.UserBean;
import app.entity.SysUser;
import app.entity.SysUserRole;
import spark.page.easyui.Page;

/**
 * 
 * 功能描述 : 用户服务类 
 * @author : YH
 * @version:
 */
public interface UserService {

	/**
	 * 注册
	 * @param user
	 * @return
	 */
	public boolean regeditUser(SysUser user);
	
	/**
	 * 根据账号查询用户
	 * @param usercode 用户账号
	 * @return
	 */
	public SysUser getUserByCode(String usercode);
	
	/**
	 * 关联机构表查询用户信息
	 * @param usercode 用户账号
	 * @return
	 */
	public UserBean getUserBeanByCode(String usercode);
	
	/**
	 * 分页获取用户信息
	 * @param p Page
	 * @return
	 */
	public Page getAllUsers(Page p);
	
	/**
	 * 更新用户信息
	 * @param userid 用户编号
	 * @param usercode 用户代码
	 * @param username 用户名称
	 * @param orgcode 机构代码
	 * @param status 状态（0-无效 1-有效）
	 * @param des 备注
	 * @param _usercode 操作员用户代码
	 * @param empid 员工编号
	 * @return
	 */
	public InfoBean modifyUser(long userid, String usercode, String username, String orgcode, int status, String des, String _usercode, String empid);
	
	/**
	 * 更新用户信息
	 * @param user 用户信息
	 * @param _usercode 操作员用户代码
	 * @param isLogin true：登录更新  false：非登录更新
	 * @return
	 */
	public InfoBean updateUser(SysUser user, String _usercode, boolean isLogin);
	
	/**
	 * 删除用户（同步删除用户角色关系、用户临时授权的涉密数据）
	 * @param tarUserCode 目标用户代码
	 * @param _usercode 当前操作员代码
	 * @return
	 */
	public InfoBean deleteUser(String tarUserCode, String _usercode);
	
	/**
	 * 重置用户密码
	 * @param tarUserCode 目标用户代码
	 * @param _usercode 当前操作员代码
	 * @return
	 */
	public InfoBean resetPwdUser(String tarUserCode, String _usercode);
	
	/**
	 * 查看用户所有角色信息
	 * @param usercode 用户代码
	 * @param orgcode 用户机构代码
	 * @return
	 */
	public List<Map<String,Object>> getAllRolesByUser(String usercode,String orgcode);
	
	/**
	 * 根据usercode查询用户的角色信息
	 * @param usercode 用户代码
	 * @return
	 */
	public List<SysUserRole> getRolesByUserCode(String usercode);
	
	/**
	 * 更新用户所拥有的角色
	 * @param tarUsercode 目标用户代码
	 * @param roles 更新的角色信息
	 * @param _usercode 当前操作员代码
	 * @return
	 */
	public InfoBean updateUserResRoles(String tarUsercode, String roles, String _usercode);
	
	/**
	 * 用户密码修改
	 * @param usercode 用户编号
	 * @param oldpwd 原密码
	 * @param newpwd 新密码
	 * @return
	 */
	public InfoBean modifypwd(String usercode, String oldpwd, String newpwd);
	
	/**
	 * 判断用户是否为超级管理员
	 * @param usercode 用户编号
	 * @return true-管理员  false-普通用户
	 */
	public boolean isAdminUser(String usercode);
	
	/**
	 * 是否有权限
	 * @param usercode 用户代码
	 * @param codes 需要判断的角色代码数组
	 * @return true：有权限  false：无权限
	 */
	public boolean isHavePermission(String usercode, String[] codes);
	
	/**
	 * 获取用户最高权限<br>999：代表超级管理员（admin）<br>998：代表系统管理员（sys）<br>997：代表用户管理员（user）<br>0：其它用户
	 * @param usercode
	 * @return
	 */
	public int getMaxRoleByUsercode(String usercode);
	
	/**
	 * 获取用户角色，并拼接为字符串
	 * @param usercode
	 * @return
	 */
	public String getUserRoles(String usercode);
	
	/**
	 * 保存用户信息
	 * @param user
	 * @return
	 */
	public int saveUser(SysUser user);
}
