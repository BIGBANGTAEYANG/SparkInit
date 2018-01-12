package app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bean.InfoBean;
import app.common.Constant;
import app.common.DateUtil;
import app.common.HodsLogUtil;
import app.common.LogState;
import app.common.StringUtil;
import app.dao.GeneralDao;
import app.dao.RoleDao;
import app.entity.SysRole;
import app.exception.StatusException;
import app.service.RoleService;
import app.service.UserService;
import spark.annotation.Auto;
import spark.page.easyui.Page;


public class RoleServiceImpl implements RoleService {
	
	private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Auto(name = UserServiceImpl.class)
	private UserService userService;
	
	@Auto (name = GeneralDao.class)
	private GeneralDao<?> generalDao;
	
	@Auto (name = RoleDao.class)
	private RoleDao<?> roleDao;

	@Override
	public Page findRoles(Page p) {
		p = generalDao.findPageAllDatas(SysRole.class, p);
		if (p == null) {
			p = new Page();
		}
		return p;
	}
	
	@Override
	public InfoBean updateRole(String rolecode, String rolename, String des, String opflag, String _usercode) {
		InfoBean ib = new InfoBean();
		if (!userService.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (StringUtil.isEmpty(rolecode) || StringUtil.isEmpty(rolename)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("角色信息不完整，请重新填写！");
			return ib;
		}
		SysRole sr = roleDao.findById(rolecode);
		if ("0".equals(opflag) && sr != null) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("该角色已存在，请重新填写！");
			return ib;
		}
		String op = null;
		try {
			if ("0".equals(opflag)) {
				op = "新增";
				sr = new SysRole();
				sr.setRolecode(rolecode);
				sr.setRolename(rolename);
				sr.setDes(des);
				sr.setCreatetime(DateUtil.formatTime(System.currentTimeMillis(), DateUtil.DEF_DATETIME_FORMAT));
				roleDao.insertRole(sr);
			} else {
				op = "修改";
				sr.setRolename(rolename);
				sr.setDes(des);
				//sr.setCreatetime(DateUtil.formatTime(System.currentTimeMillis(), DateUtil.DEF_DATETIME_FORMAT));
				roleDao.updateRole(sr);
			}
			ib.setCode(InfoBean.SUCCESS);
			ib.setMessage("角色信息更新成功！");
			logger.info("用户["+ _usercode +"]"+ op +"角色["+ rolecode +"]成功！"+ op +"后角色代码["+ sr.getRolecode() +"]，角色名称["+ sr.getRolename() +"]");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "角色更新", "角色管理", "SYS_ROLE",LogState.成功, System.currentTimeMillis(),"用户["+ _usercode +"]"+ op +"角色["+ rolecode +"]成功！"+ op +"后角色代码["+ sr.getRolecode() +"]，角色名称["+ sr.getRolename() +"]", null);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("用户["+ _usercode +"]"+ op +"角色["+ rolecode +"]异常，角色名称["+ rolename +"]", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "角色更新", "角色管理", "SYS_ROLE",LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]"+ op +"角色["+ rolecode +"]异常，角色名称["+ rolename +"]异常！" + e.getMessage(), null);
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("角色信息更新失败！");
		}
		return ib;
	}
	
	@Override
	public InfoBean deleteRole(String rolecode, String _usercode) {
		InfoBean ib = new InfoBean();
		if (!userService.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (StringUtil.isEmpty(rolecode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("请选择角色！");
			return ib;
		}
		if (Constant.ROLE_ADMIN.equals(rolecode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("不允许删除超级管理员角色！");
			return ib;
		}
		int r = 0;
		try {
			r = roleDao.deleteRole(rolecode);
		} catch (Exception e) {
			logger.error("用户["+ _usercode +"]删除角色["+ rolecode +"]异常！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "角色删除", "角色管理", "SYS_ROLE",LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]删除角色["+ rolecode +"]异常！" + e.getMessage(), null);
			throw new StatusException("删除角色失败！");
		}
		if (r == 1) {
			ib.setCode(InfoBean.SUCCESS);
			ib.setMessage("删除角色成功！");
			logger.info("用户["+ _usercode +"]删除角色["+ rolecode +"]成功！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "角色删除", "角色管理", "SYS_ROLE",LogState.成功, System.currentTimeMillis(),"用户["+ _usercode +"]删除角色["+ rolecode +"]成功！" , null);
		} else {
			logger.error("用户["+ _usercode +"]删除角色["+ rolecode +"]失败！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "角色删除", "角色管理", "SYS_ROLE",LogState.失败, System.currentTimeMillis(),"用户["+ _usercode +"]删除角色["+ rolecode +"]失败！", null);
			throw new StatusException("删除角色失败！");
		}
		return ib;
	}
}
