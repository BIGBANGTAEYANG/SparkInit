package app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.annotation.Auto;
import spark.page.easyui.Page;
import app.bean.InfoBean;
import app.bean.user.UserBean;
import app.common.Constant;
import app.common.DateUtil;
import app.common.HodsLogUtil;
import app.common.LogState;
import app.common.StringUtil;
import app.dao.GeneralDao;
import app.dao.RoleDao;
import app.dao.UserDao;
import app.dao.UserRoleDao;
import app.entity.SysRole;
import app.entity.SysUser;
import app.entity.SysUserRole;
import app.exception.StatusException;
import app.security.MD5;
import app.service.UserService;

public class UserServiceImpl implements UserService {
	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Auto(name = UserDao.class)
	private UserDao<?> userDao;
	
	@Auto(name = RoleDao.class)
	private RoleDao<?> roleDao;
	
	@Auto(name = GeneralDao.class)
	private GeneralDao<?> generalDao;
	
	@Auto(name = UserRoleDao.class)
	private UserRoleDao<?> userRoleDao;
	
	@Override
	public boolean regeditUser(SysUser user) {
		//为用户生成用户代码
		//user.setCode(new IDGenerator("user",false).nextId());
		//对用户密码进行加密
		user.setUserpwd(MD5.toMd5(user.getUserpwd()));
		int r = userDao.insert(Constant.SOURCEID, user);
		return r == 1?true:false;
	}
	
	@Override
	public SysUser getUserByCode(String usercode) {
		return userDao.getUserByCode(usercode);
	}
	
	@Override
	public UserBean getUserBeanByCode(String usercode) {
		return userDao.getUserBeanByCode(usercode);
	}
	
	@Override
	public Page getAllUsers(Page p) {
		p = userDao.findPageAllDatas(p);
		if (p == null) {
			p = new Page();
		}
		return p;
	}
	
	@Override
	public InfoBean modifyUser(long userid, String usercode, String username, String orgcode, int status, String des, String _usercode, String empid) {
		InfoBean ib = new InfoBean();
		if (!this.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (StringUtil.isEmpty(usercode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("请选择用户！");
			return ib;
		}
		// 自己不可修改比自己权限大或权限相等的其他用户
		if (!usercode.equals(_usercode)) {
			// 当前用户的最高权限
			int _max = this.getMaxRoleByUsercode(_usercode);
			// 目标用户最高权限
			int tarMax = this.getMaxRoleByUsercode(usercode);
			if (_max <= tarMax) {
				ib.setCode(InfoBean.FAILURE);
				ib.setMessage("您无权限操作此用户信息！");
				return ib;
			}
		}
		String createtime = DateUtil.formatTime(System.currentTimeMillis(), DateUtil.DEF_DATETIME_FORMAT);
		SysUser su = new SysUser();
		su.setUsercode(usercode);
		su.setUsername(username);
		su.setOrgCode(orgcode);
		su.setStatus(status);
		su.setDes(des);
		su.setEmpid(empid);
		int r = 0;
		String op = null;
		try {
			if (userid == 0) {
				// 新增
				op = "新增";
				su.setCreatetime(createtime);
				su.setUserpwd(MD5.toMd5(usercode));
				r = userDao.save(su);
			} else {
				// 修改
				op = "修改";
				SysUser old = userDao.getUserByCode(usercode);
				if (old != null && !orgcode.equals(old.getOrgCode())) {
					// 如果有机构代码改变，则清除用户的角色信息
					userRoleDao.removeRoleByUsercode(usercode);
				}
				r = userDao.updateUser(su, false);
				
			}
			if (r == 1) {
				ib.setCode(InfoBean.SUCCESS);
				ib.setMessage("更新用户信息成功！");
				logger.info("用户["+ _usercode +"]"+op+"用户["+ usercode +"]成功！"+op+"后用户代码["+ usercode +"]，用户名["+username+"]，用户机构["+ orgcode +"]，用户状态["+ status +"]");
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户更新", "用户管理", "SYS_USER", LogState.成功, System.currentTimeMillis(),"用户["+ _usercode +"]"+op+"用户["+ usercode +"]成功！"+op+"后用户代码["+ usercode +"]，用户名["+username+"]，用户机构["+ orgcode +"]，用户状态["+ status +"]", null);
			} else {
				ib.setCode(InfoBean.FAILURE);
				ib.setMessage("更新用户信息失败！");
				logger.error("用户["+ _usercode +"]"+op+"用户["+ usercode +"]失败！");
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户更新", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]"+op+"用户["+ usercode +"]失败！", null);
			}
		} catch (Exception e) {
			logger.error("用户["+ _usercode +"]"+op+"用户["+ usercode +"]异常！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户更新", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]"+op+"用户["+ usercode +"]异常！" + e.getMessage(), null);
		}
		return ib;
	}
	
	@Override
	public InfoBean updateUser(SysUser user, String _usercode, boolean isLogin) {
		InfoBean ib = new InfoBean();
		if (user == null) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("用户信息不能为空！");
		} else {
			// 更新用户信息
			try {
				int r = userDao.updateUser(user, true);
				if (r > 0) {
					ib.setCode(InfoBean.SUCCESS);
					ib.setMessage("更新用户信息成功！");
					if (!isLogin) {
						logger.info("用户["+ _usercode +"]更新目标用户成功！更新后用户代码["+ user.getUsercode() +"]，用户名["+user.getUsername()+"]，用户机构["+ user.getOrgCode() +"]，用户状态["+ user.getStatus() +"]");
					}
				} else {
					ib.setCode(InfoBean.FAILURE);
					ib.setMessage("更新用户信息失败！");
					logger.error("用户["+ _usercode +"]更新目标用户代码["+ user.getUsercode() +"]信息失败！");
				}
			} catch(Exception e) {
				logger.error("用户["+ _usercode +"]更新目标用户代码["+ user.getUsercode() +"]信息异常！", e);
				ib.setCode(InfoBean.FAILURE);
				ib.setMessage("更新用户信息失败！");
			}
		}
		return ib;
	}
	
	@Override
	public InfoBean deleteUser(String tarUserCode, String _usercode) {
		InfoBean ib = new InfoBean();
		// 判断是否有操作权限
		if (!this.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (StringUtil.isEmpty(tarUserCode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("请选择用户！");
			return ib;
		}
		// 不能删除自己
		if (tarUserCode.equals(_usercode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("不允许自删除操作！");
			return ib;
		}
		// 不能删除超级管理员
		if (this.isAdminUser(tarUserCode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("拥有超级管理员角色的用户不允许删除");
			return ib;
		}
		// 不能删除比自己权限大或权限相等的用户
		// 当前用户的最高权限
		int _max = this.getMaxRoleByUsercode(_usercode);
		// 目标用户最高权限
		int tarMax = this.getMaxRoleByUsercode(tarUserCode);
		if (_max <= tarMax) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限操作此用户信息！");
			return ib;
		}
		SysUser su = null;
		try {
			su = userDao.getUserByCode(tarUserCode);
		} catch (Exception e) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("获取用户信息失败！");
			logger.error("用户["+ _usercode +"]获取目标用户["+ tarUserCode +"]信息失败！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户删除", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]获取目标用户["+ tarUserCode +"]信息失败！" + e.getMessage(), null);
			return ib;
		}
		if (su == null) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("删除失败，用户不存在！");
			logger.error("用户["+ _usercode +"]获取目标用户["+ tarUserCode +"]信息失败！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户删除", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]获取目标用户["+ tarUserCode +"]信息失败！", null);
		} else {
			try {
				int r = userDao.deleteUser(tarUserCode);
				if (r > 0) {
					// 删除用户角色信息
					//userRoleDao.removeRoleByUsercode(tarUserCode);
					ib.setCode(InfoBean.SUCCESS);
					ib.setMessage("删除用户成功！");
					logger.info("用户["+ _usercode +"]删除目标用户["+ tarUserCode +"]成功！");
					HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户删除", "用户管理", "SYS_USER", LogState.成功, System.currentTimeMillis(),"用户["+ _usercode +"]删除目标用户["+ tarUserCode +"]成功！", null);
				} else {
					logger.error("用户["+ _usercode +"]删除目标用户["+ tarUserCode +"]失败！");
					HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户删除", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]删除目标用户["+ tarUserCode +"]失败！", null);
					ib.setCode(InfoBean.FAILURE);
					ib.setMessage("删除用户失败！");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("用户["+ _usercode +"]删除目标用户["+ tarUserCode +"]异常！", e);
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户删除", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]删除目标用户["+ tarUserCode +"]异常！" + e.getMessage(), null);
				throw new StatusException("删除用户失败！");
			}
		}
		return ib;
	}
	
	@Override
	public InfoBean resetPwdUser(String tarUserCode, String _usercode) {
		InfoBean ib = new InfoBean();
		if (!this.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (StringUtil.isEmpty(tarUserCode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("请选择用户！");
			return ib;
		}
		// 不能重置比自己权限大或权限相等的用户密码
		if (!tarUserCode.equals(_usercode)) {
			// 当前用户的最高权限
			int _max = this.getMaxRoleByUsercode(_usercode);
			// 目标用户最高权限
			int tarMax = this.getMaxRoleByUsercode(tarUserCode);
			if (_max <= tarMax) {
				ib.setCode(InfoBean.FAILURE);
				ib.setMessage("您无权限操作此用户信息！");
				return ib;
			}
		}
		SysUser su = null;
		try {
			su = userDao.getUserByCode(tarUserCode);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("用户["+ _usercode +"]重置目标用户["+ tarUserCode +"]密码异常！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户密码重置", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]重置目标用户["+ tarUserCode +"]密码异常！" + e.getMessage() , null);
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("重置密码失败！");
			return ib;
		}
		if (su == null) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("重置密码失败，用户不存在！");
		} else {
			int r = userDao.updateUserPwd(tarUserCode, MD5.toMd5(su.getUsercode()));
			if (r > 0) {
				logger.info("用户["+ _usercode +"]重置目标用户["+ tarUserCode +"]密码成功！新密码["+ su.getUsercode() +"]");
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户密码重置", "用户管理", "SYS_USER", LogState.成功, System.currentTimeMillis(),"用户["+ _usercode +"]重置目标用户["+ tarUserCode +"]密码成功！" , null);
				ib.setCode(InfoBean.SUCCESS);
				ib.setMessage("重置密码成功！");
			} else {
				logger.error("用户["+ _usercode +"]重置目标用户["+ tarUserCode +"]密码失败！");
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户密码重置", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]重置目标用户["+ tarUserCode +"]密码失败！" , null);
				ib.setCode(InfoBean.FAILURE);
				ib.setMessage("重置密码失败！");
			}
		}
		return ib;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getAllRolesByUser(String usercode, String orgcode) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<SysRole> alls = null;
//		@SuppressWarnings("unchecked")
//		List<SysRole> alls = (List<SysRole>) roleDao.findAllRoles();
		if (this.isAdminUser(usercode)) {
			alls = (List<SysRole>) roleDao.findAllRoles();
		} else {
			alls = (List<SysRole>) roleDao.findRolesByOrgcode(orgcode);
		}
		
		List<SysUserRole> res = this.getRolesByUserCode(usercode);
		Map<String,Object> map = null;
		
		if (res == null || res.isEmpty()) {
			// 如果没有配置角色，则返回所有角色信息
			for (SysRole r : alls) {
				map = new HashMap<String,Object>();
				map.put("id", r.getRolecode());
				map.put("text",r.getRolename());
				list.add(map);
			}
		} else {
			// 返回用户拥有的角色信息
			if (alls != null && !alls.isEmpty()) {				
				for (SysRole r : alls) {
					map = new HashMap<String,Object>();
					map.put("id", r.getRolecode());
					map.put("text",r.getRolename());
					for (SysUserRole i : res) {
						if(r.getRolecode().equals(i.getRolecode())) {
							map.put("checked", "true");
							break;
						}
					}
					list.add(map);
				}
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SysUserRole> getRolesByUserCode(String usercode) {
		return (List<SysUserRole>) userRoleDao.getRolesByUserCode(usercode);
	}
	
	@Override
	public InfoBean updateUserResRoles(String tarUsercode, String roles, String _usercode) {
		InfoBean info = new InfoBean();
		if (!this.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("您无权限执行此操作！");
			return info;
		}
		if (StringUtil.isEmpty(tarUsercode)) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("请选择用户！");
			return info;
		}
		// 自己不能更新自己的角色信息
		if (tarUsercode.equals(_usercode)) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("不允许对自己进行角色分配！");
			return info;
		}
		// 不能更新比自己权限大或权限相等的用户角色
		// 当前用户的最高权限
		int _max = this.getMaxRoleByUsercode(_usercode);
		// 目标用户最高权限
		int tarMax = this.getMaxRoleByUsercode(tarUsercode);
		if (_max <= tarMax) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("您无权限操作此用户信息！");
			return info;
		}
		if (StringUtil.isNotEmpty(roles) && roles.contains(Constant.ROLE_ADMIN)) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("不允许对用户分配超级管理员角色！");
			return info;
		}
		boolean isSucess = false;
		try {
			// 先删除原来用户所拥有的角色
			List<SysUserRole> oldRoles = getRolesByUserCode(tarUsercode);
			if (oldRoles != null && !oldRoles.isEmpty()) {
				userRoleDao.removeRoleByUsercode(tarUsercode);
			}
			//如果没有选择新的角色，直接返回提示信息
			if (StringUtil.isEmpty(roles)) {
				info.setMessage("用户角色更新成功!");
				logger.info("用户["+ _usercode +"]更新目标用户["+ tarUsercode +"]角色成功！更新后目标用户角色["+ roles +"]");
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户角色更新", "用户管理", "SYS_USER", LogState.成功, System.currentTimeMillis(),"用户["+ _usercode +"]更新目标用户["+ tarUsercode +"]角色成功！更新后目标用户角色["+ roles +"]！" , null);
				return info;
			}
			String[] arrs = roles.split("\\|");
			Object[][] objs = new Object[arrs.length][];
			String[] obj = null;
			int i = 0;
			for (String id : arrs) {
				obj = new String[2];
				obj[0] = id;
				obj[1] = tarUsercode;
				objs[i++] = obj;
			}
			//保存新选择的角色
			isSucess = userRoleDao.batchSave(objs);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("用户["+ _usercode +"]更新目标用户["+ tarUsercode +"]角色失败！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户角色更新", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]更新目标用户["+ tarUsercode +"]角色失败！" + e.getMessage(), null);
			throw new StatusException("用户角色更新失败！");
		}
		if (isSucess) {
			info.setMessage("用户角色更新成功!");
			logger.info("用户["+ _usercode +"]更新目标用户["+ tarUsercode +"]角色成功！更新后目标用户角色["+ roles +"]");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户角色更新", "用户管理", "SYS_USER", LogState.成功, System.currentTimeMillis(),"用户["+ _usercode +"]更新目标用户["+ tarUsercode +"]角色成功！更新后目标用户角色["+ roles +"]", null);
		} else {
			logger.error("用户["+ _usercode +"]更新目标用户["+ tarUsercode +"]角色失败！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "用户角色更新", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]更新目标用户["+ tarUsercode +"]角色失败！" , null);
			throw new StatusException("用户角色更新失败！");
		}
		return info;
	}
	
	@Override
	public InfoBean modifypwd(String usercode, String oldpwd, String newpwd) {
		InfoBean info = new InfoBean();
		// 合法性判断
		if (StringUtil.isEmpty(oldpwd) || StringUtil.isEmpty(newpwd)) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("数据错误！");
			return info;
		}
		SysUser su = userDao.getUserByCode(usercode);
		if (su == null) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("获取用户信息失败！");
			return info;
		}
		if (!MD5.toMd5(oldpwd).equals(su.getUserpwd())) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("原密码错误！");
			return info;
		}
		// 通过，修改密码
		try {
			int r = userDao.updateUserPwd(usercode, MD5.toMd5(newpwd));
			if (r == 1) {
				logger.error("用户["+ usercode +"]修改密码成功！原密码["+ oldpwd +"]，新密码["+ newpwd +"]！");
				HodsLogUtil.getInstance().AddAuditLog(usercode, "用户密码修改", "用户管理", "SYS_USER", LogState.成功, System.currentTimeMillis(),"用户["+ usercode +"]修改密码成功！" , null);
				info.setCode(InfoBean.SUCCESS);
				info.setMessage("密码修改成功，请重新登录！");
			} else {
				logger.error("用户["+ usercode +"]修改密码失败！");
				HodsLogUtil.getInstance().AddAuditLog(usercode, "用户密码修改", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ usercode +"]修改密码失败！" , null);
				info.setCode(InfoBean.FAILURE);
				info.setMessage("密码修改失败！");
			}
		} catch (Exception e) {
			logger.error("用户["+ usercode +"]修改密码异常！", e);
			HodsLogUtil.getInstance().AddAuditLog(usercode, "用户密码修改", "用户管理", "SYS_USER", LogState.失败, System.currentTimeMillis(),"用户["+ usercode +"]修改密码异常！" + e.getMessage(), null);
		}
		return info;
	}
	
	@Override
	public boolean isAdminUser(String usercode) {
		boolean isAdmin = false;
		try {
			@SuppressWarnings("unchecked")
			List<SysUserRole> roles = (List<SysUserRole>) userRoleDao.getRolesByUserCode(usercode);
			if (roles != null) {
				for (SysUserRole sur : roles) {
					if (Constant.ROLE_ADMIN.equals(sur.getRolecode())) {
						isAdmin = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("判断用户["+ usercode +"]是否管理员失败！", e);
		}
		return isAdmin;
	}
	
	@Override
	public boolean isHavePermission(String usercode, String[] codes) {
		/*boolean isOk = false;
		try {
			if (codes == null || codes.length == 0) {
				return isOk;
			}
			@SuppressWarnings("unchecked")
			List<SysUserRole> roles = (List<SysUserRole>) userRoleDao.getRolesByUserCode(usercode);
			if (roles != null) {
				for (SysUserRole sur : roles) {
					for (String role : codes) {
						if (role.equals(sur.getRolecode())) {
							isOk = true;
							break;
						}
					}
					if (isOk) {
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("判断用户["+ usercode +"]是否有权限失败！", e);
		}
		return isOk;*/
		// TODO 取消后台固定权限判断
		return true;
	}
	
	@Override
	public int getMaxRoleByUsercode(String usercode) {
		int level = 0;
		try {
			@SuppressWarnings("unchecked")
			List<SysUserRole> roles = (List<SysUserRole>) userRoleDao.getRolesByUserCode(usercode);
			if (roles != null) {
				for (SysUserRole sur : roles) {
					if (Constant.ROLE_ADMIN.equals(sur.getRolecode())) {
						level = 999;
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取用户["+ usercode +"]最高角色权限失败！", e);
		}
		return level;
	}
	
	@Override
	public String getUserRoles(String usercode) {
		return this.userDao.getUserRoles(usercode);
	}
	
	@Override
	public int saveUser(SysUser user) {
		return userDao.save(user);
	}
}
