package app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import app.bean.InfoBean;
import app.bean.auth.AccessControlBean;
import app.bean.menu.MenuTreeBean;
import app.common.Constant;
import app.common.DateUtil;
import app.common.HodsLogUtil;
import app.common.LogState;
import app.common.MenuTreeFactory;
import app.common.StringUtil;
import app.common.UrlParser;
import app.dao.AuthDao;
import app.dao.GeneralDao;
import app.dao.MenuAuthDao;
import app.dao.MenuDao;
import app.dao.RoleMenuAuthDao;
import app.entity.SysAuth;
import app.entity.SysMenu;
import app.entity.SysMenuAuth;
import app.entity.SysRoleMenuAuth;
import app.exception.StatusException;
import app.service.AuthService;
import app.service.UserService;
import spark.Request;
import spark.annotation.Auto;
import spark.page.easyui.Page;

public class AuthServiceImpl implements AuthService {
	private final static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	@Auto (name = GeneralDao.class)
	private GeneralDao<?> generalDao;
	
	@Auto (name = AuthDao.class)
	private AuthDao<?> authDao;
	
	@Auto (name = MenuAuthDao.class)
	private MenuAuthDao<?> menuAuthDao;
	
	@Auto (name = MenuDao.class)
	private MenuDao<?> menuDao;
	
	@Auto (name = RoleMenuAuthDao.class)
	private RoleMenuAuthDao<?> roleMenuAuthDao;
	
	@Auto(name = UserServiceImpl.class)
	private UserService userService;
	
	
	@Override
	public Page findAuths(Page p) {
		return authDao.findByPage(p);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkFuncitonAuths(Request req, String code) {
		List<String> authsLists = new ArrayList<String>();
		if(StringUtil.isEmpty(code))
			return authsLists;
		//code = code.replace("/app", "");
		String url = UrlParser.parserUrlPage(code, Constant.PATSERURL);
		Object obj = req.session().attribute(Constant.AUTHS);
		String strAuths = String.valueOf(obj);
		Map<String, Map<String, AccessControlBean>> auths = new Gson().fromJson(strAuths, Map.class);
        if (auths != null && StringUtil.isNotEmpty(url)) {
    		if (auths.containsKey(url)) {
    			Map<String, AccessControlBean> maps = auths.get(url);
    			for (String s : maps.keySet()) {
    				authsLists.add(s);
    			}
    		}
        }
        return authsLists;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page findMenuAuths(Page p, long menuid) {
		p = generalDao.findPageAllDatas(SysAuth.class, p);
		if (p != null && menuid != 0) {
			List<SysMenuAuth> list = this.findMenuAuth(menuid);
			List<Map<String, String>> authlist = p.getRows();
			String sysAuthcode = null;
			// 已经选择的菜单功能
			if (authlist != null && !authlist.isEmpty() && list != null && !list.isEmpty()) {
				List<Map<String, String>> myAuthList = new ArrayList<Map<String, String>>();
				for (Map<String, String> map : authlist) {
					sysAuthcode = map.get("authcode");
					if (sysAuthcode != null) {
						for (SysMenuAuth sma : list) {
							if (sysAuthcode.equals(sma.getAuthcode())) {
								map.put("des", "1");
								break;
							}
						}
					}
					myAuthList.add(map);
				}
				p.setRows(myAuthList);
			}
		}
		if (p == null) {
			p = new Page();
		}
		return p;
	}
	
	@Override
	public InfoBean updateAuth(String authcode, String authname, String status, String des, String updateFlag, String _usercode) {
		InfoBean ib = new InfoBean();
		if (!userService.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (StringUtil.isEmpty(updateFlag)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("操作失败！");
			return ib;
		}
		int flag = Integer.parseInt(updateFlag);
		if (flag != 1 && flag != 2) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("操作失败！");
			return ib;
		}
		if (StringUtil.isEmpty(authcode) || StringUtil.isEmpty(authname)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("请补充完整的权限信息！");
			return ib;
		}
		authcode = authcode.trim();
		authname = authname.trim();
		if (des != null) {
			des = des.trim();
		}
		if(flag == 2) {
			// 新增，判断权限是否已经存在
			SysAuth auth = authDao.findByCode(authcode);
			if (auth != null) {
				ib.setCode(InfoBean.FAILURE);
				ib.setMessage("已存在此权限代码！");
				logger.error("用户["+ _usercode +"]更新权限基本信息失败，已存在此权限代码！权限代码["+ authcode +"]，权限名称["+ authname +"]，状态["+ status +"]，备注["+ des +"]！");
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "更新权限信息", "权限管理", "SYS_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]更新权限基本信息失败，已存在此权限代码！权限代码["+ authcode +"]，权限名称["+ authname +"]，状态["+ status +"]，备注["+ des +"]！", null);
				return ib;
			}
		}
		int isSuccess = 0;
		try {
			isSuccess = authDao.updateAuth(authcode, authname, status, DateUtil.formatTime(System.currentTimeMillis(), DateUtil.DEF_DATETIME_FORMAT), des, flag);
		} catch (Exception e) {
			//logger.error("权限信息更新失败！", e);
//			e.printStackTrace();
			logger.error("用户["+ _usercode +"]更新权限基本信息失败！权限代码["+ authcode +"]，权限名称["+ authname +"]，状态["+ status +"]，备注["+ des +"]！" , e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "更新权限信息", "权限管理", "SYS_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]更新权限基本信息失败！权限代码["+ authcode +"]，权限名称["+ authname +"]，状态["+ status +"]，备注["+ des +"]！" + e.getMessage(), null);
			throw new StatusException("权限信息更新失败!");
		}
		if (isSuccess == 1) {
			ib.setCode(InfoBean.SUCCESS);
			ib.setMessage("权限信息更新成功");
			logger.info("用户["+ _usercode +"]更新权限基本信息成功！权限代码["+ authcode +"]，权限名称["+ authname +"]，状态["+ status +"]，备注["+ des +"]！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "更新权限信息", "权限管理", "SYS_AUTH", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]更新权限基本信息成功！权限代码["+ authcode +"]，权限名称["+ authname +"]，状态["+ status +"]，备注["+ des +"]！", null);
		} else {
			logger.error("用户["+ _usercode +"]更新权限基本信息失败！权限代码["+ authcode +"]，权限名称["+ authname +"]，状态["+ status +"]，备注["+ des +"]！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "更新权限信息", "权限管理", "SYS_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]更新权限基本信息失败！权限代码["+ authcode +"]，权限名称["+ authname +"]，状态["+ status +"]，备注["+ des +"]！", null);
			throw new StatusException("权限信息更新失败!");
		}
		return ib;
	}
	
	@Override
	public InfoBean deleteAuth(String authcode, String _usercode) {
		InfoBean ib = new InfoBean();
		if (!userService.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (StringUtil.isEmpty(authcode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("请选择要删除的权限！");
			return ib;
		}
		authcode = authcode.trim();
		SysAuth auth = authDao.findByCode(authcode);
		if (auth == null) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("你要删除的权限已经不存在！");
			return ib;
		}
		int isSuccess = 0;
		try {
			isSuccess = authDao.updateAuth(authcode, null, null, null, null, 3);
		} catch (Exception e) {
			//logger.error("权限信息删除失败！", e);
//			e.printStackTrace();
			logger.error("用户["+ _usercode +"]删除权限信息失败，权限代码["+ authcode +"]！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "删除权限信息", "权限管理", "SYS_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]删除权限信息失败，权限代码["+ authcode +"]！", null);
			throw new StatusException("权限信息删除失败!");
		}
		if (isSuccess == 1) {
			ib.setCode(InfoBean.SUCCESS);
			ib.setMessage("权限信息删除成功");
			logger.info("用户["+ _usercode +"]删除权限信息成功，权限代码["+ authcode +"]！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "删除权限信息", "权限管理", "SYS_AUTH", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]删除权限信息成功，权限代码["+ authcode +"]！", null);
		} else {
			logger.error("用户["+ _usercode +"]删除权限信息失败，权限代码["+ authcode +"]！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "删除权限信息", "权限管理", "SYS_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]删除权限信息失败，权限代码["+ authcode +"]！", null);
			throw new StatusException("权限信息删除失败!");
		}
		return ib;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SysMenuAuth> findMenuAuth(long menuid) {
		return (List<SysMenuAuth>) menuAuthDao.findAuthsByMenucode(menuid);
	}
	
	@Override
	public InfoBean updateMenuAuths(long menuid, String auths, String _usercode) {
		InfoBean info = new InfoBean();
		if (!userService.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("您无权限执行此操作！");
			return info;
		}
		if (auths == null) {
			auths ="";
		}
		if (menuid <= 0) {
			info.setCode(InfoBean.FAILURE);
			info.setMessage("菜单不存在，请重新选择！");
			return info;
		}
		// 找出当前菜单的功能项
		List<SysMenuAuth> oldRoles = findMenuAuth(menuid);
		// 对比修改后的功能，找出删除掉的功能
		String[] arrs = auths.split("\\|");
		List<String> delAuth = new ArrayList<String>();
		boolean isExist = false;
		for (SysMenuAuth sma : oldRoles) {
			for (int i=0;i<arrs.length;i++) {
				if (sma.getAuthcode().equals(arrs[i])) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				delAuth.add(sma.getAuthcode());
			}
		}
		boolean isSuccess = false;
		try {
			if (delAuth.size() > 0) {
				// 菜单存在删除掉的功能，删除角色菜单权限表中，对应菜单权限数据
				Object[][] objs = new Object[delAuth.size()][2];
				for (int i = 0;i<delAuth.size();i++) {
					objs[i][0] =  delAuth.get(i);
					objs[i][1] =  menuid;
				}
				roleMenuAuthDao.deleteMenuAuth(objs);
			}
			// 先删除原来菜单功能
			menuAuthDao.removeAuthByMenucode(menuid);
			
			//如果没有选择新的菜单功能，直接返回提示信息
			if (StringUtil.isEmpty(auths)) {
				info.setMessage("菜单功能更新成功!");
				logger.info("用户["+ _usercode +"]更新菜单功能成功，菜单编号["+ menuid +"]，权限代码["+ auths +"]！");
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "菜单功能更新", "菜单管理", "SYS_MENU_AUTH，SYS_ROLE_MENU_AUTH", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]更新菜单功能成功，菜单编号["+ menuid +"]，权限代码["+ auths +"]！", null);
				return info;
			}
			
			Object[][] objs = new Object[arrs.length][];
			String[] obj = null;
			int i = 0;
			for (String id : arrs) {
				obj = new String[2];
				obj[0] = id;
				obj[1] = String.valueOf(menuid);
				objs[i++] = obj;
			}
			//保存新选择的菜单功能
			isSuccess = menuAuthDao.batchSave(objs);
		} catch (Exception e) {
			//logger.error("菜单功能更新失败！", e);
//			e.printStackTrace();
			logger.error("用户["+ _usercode +"]更新菜单功能失败，菜单编号["+ menuid +"]，权限代码["+ auths +"]！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "菜单功能更新", "菜单管理", "SYS_MENU_AUTH，SYS_ROLE_MENU_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]更新菜单功能失败，菜单编号["+ menuid +"]，权限代码["+ auths +"]！" + e.getMessage(), null);
			throw new StatusException("菜单功能更新失败!");
		}
		if (isSuccess) {
			info.setMessage("菜单功能更新成功!");
			logger.info("用户["+ _usercode +"]更新菜单功能成功，菜单编号["+ menuid +"]，权限代码["+ auths +"]！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "菜单功能更新", "菜单管理", "SYS_MENU_AUTH，SYS_ROLE_MENU_AUTH", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]更新菜单功能成功，菜单编号["+ menuid +"]，权限代码["+ auths +"]！", null);
		} else {
			logger.error("用户["+ _usercode +"]更新菜单功能失败，菜单编号["+ menuid +"]，权限代码["+ auths +"]！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "菜单功能更新", "菜单管理", "SYS_MENU_AUTH，SYS_ROLE_MENU_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]更新菜单功能失败，菜单编号["+ menuid +"]，权限代码["+ auths +"]！", null);
			throw new StatusException("菜单功能更新失败!");
		}
		return info;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, List<String>> findMenuAuthMap() {
		// 通过角色菜单ID取菜单的功能列表
		List<SysMenuAuth> menuAuthList = new ArrayList<SysMenuAuth>();
		Map<Long, List<String>>  menuAuthMap = new HashMap<Long, List<String>>();
		List<String> menuAllAuthList = null;
		long menuid = 0;
		String authcode = null;
		menuAuthList = (List<SysMenuAuth>) menuAuthDao.findAllMenuAuths();
		for(SysMenuAuth sma : menuAuthList){
			menuid = sma.getMenuid();
			authcode = sma.getAuthcode();
			if (menuAuthMap.containsKey(menuid)) {
				menuAllAuthList = menuAuthMap.get(menuid);
				menuAllAuthList.add(authcode);
				menuAuthMap.put(menuid, menuAllAuthList);
			} else {
				menuAllAuthList = new ArrayList<String>();
				menuAllAuthList.add(authcode);
				menuAuthMap.put(menuid, menuAllAuthList);
			}
		}
		return menuAuthMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findRoleMenuAuth(String rolecode) {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		List<SysRoleMenuAuth> list = null;
		List<Long> menucodes = new ArrayList<Long>();
		List<String> roleMenuAuthList = null;
		Map<Long, List<String>> map = new HashMap<Long, List<String>>();
		long menuid = 0;
		String authcode = null;
		list = (List<SysRoleMenuAuth>) roleMenuAuthDao.findListByRolecode(rolecode);

		// 取角色所拥有的菜单MENUID,并按MENUID分类
		for (SysRoleMenuAuth srma : list) {
			menuid = srma.getMenuid();
			authcode = srma.getAuthcode();
			if (!menucodes.contains(menuid)) {
				menucodes.add(menuid);
			}
			if (map.containsKey(menuid)) {
				roleMenuAuthList = map.get(menuid);
				roleMenuAuthList.add(authcode);
				map.put(menuid, roleMenuAuthList);
			} else {
				roleMenuAuthList = new ArrayList<String>();
				roleMenuAuthList.add(authcode);
				map.put(menuid, roleMenuAuthList);
			}
		}
		// 角色拥有的菜单ID
		tempMap.put("menucodes", menucodes);
		// 角色拥有的菜单ID以及菜单对应的角色菜单功能
		tempMap.put("map",map);
		return tempMap;
	}

	@Override
	public InfoBean updateRoleMenuAuth(String param, String rolecode, String _usercode) {
		InfoBean ib = new InfoBean();
		if (!userService.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (StringUtil.isEmpty(rolecode)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("请先选择角色！");
			return ib;
		}
		// 如果自己不是超级管理员，则不能对超级管理员角色进行分配
		if (!userService.isAdminUser(_usercode) && rolecode.equals(Constant.ROLE_ADMIN)) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限进行此操作！");
			return ib;
		}
		boolean isSuccess = false;
		try {
			// 先删除角色所有权限
			this.roleMenuAuthDao.deleteRoleAuthByRolecode(rolecode);
			// 如果需要更新角色信息不存在，则返回成功
			if (StringUtil.isEmpty(param)) {
				logger.info("用户["+ _usercode +"]更新角色菜单权限成功，角色["+ rolecode +"]，更新菜单编号_权限代码["+ param +"]！");
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "更新角色菜单权限", "角色管理", "SYS_ROLE_MENU_AUTH", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]更新角色菜单权限成功，角色["+ rolecode +"]，更新菜单编号_权限代码["+ param +"]！", null);
				ib.setMessage("角色菜单权限更新成功!");
				return ib;
			}
			String[] roleMenuAuthIds = param.split(",");
			String menuid = "", authid = "";
			Object[][] objs = new Object[roleMenuAuthIds.length][3];
			if(roleMenuAuthIds.length > 0 && StringUtil.isNotEmpty(roleMenuAuthIds[0])) {
				String[] strAry = null;
				for(int i=0;i<roleMenuAuthIds.length;i++){
					strAry = roleMenuAuthIds[i].split("\\_");
					menuid = strAry[0].substring(0,strAry[0].length());
					authid = strAry[1].substring(0,strAry[1].length());
					objs[i][0] = rolecode;
					objs[i][1] = menuid;
					objs[i][2] = authid;
				}
			}
			isSuccess = roleMenuAuthDao.updateRoleMenuAuth(objs, rolecode);
		} catch (Exception e) {
			//logger.error("角色菜单权限更新失败！", e);
//			e.printStackTrace();
			logger.error("用户["+ _usercode +"]更新角色菜单权限失败，角色["+ rolecode +"]，更新菜单编号_权限代码["+ param +"]！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "更新角色菜单权限", "角色管理", "SYS_ROLE_MENU_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]更新角色菜单权限失败，角色["+ rolecode +"]，更新菜单编号_权限代码["+ param +"]！" + e.getMessage(), null);
			throw new StatusException("角色菜单权限更新失败!");
		}
		if (isSuccess) {
			ib.setMessage("角色菜单权限更新成功!");
			logger.info("用户["+ _usercode +"]更新角色菜单权限成功，角色["+ rolecode +"]，更新菜单编号_权限代码["+ param +"]！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "更新角色菜单权限", "角色管理", "SYS_ROLE_MENU_AUTH", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]更新角色菜单权限成功，角色["+ rolecode +"]，更新菜单编号_权限代码["+ param +"]！", null);
		} else {
			logger.error("用户["+ _usercode +"]更新角色菜单权限失败，角色["+ rolecode +"]，更新菜单编号_权限代码["+ param +"]！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "更新角色菜单权限", "角色管理", "SYS_ROLE_MENU_AUTH", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]更新角色菜单权限失败，角色["+ rolecode +"]，更新菜单编号_权限代码["+ param +"]！", null);
			throw new StatusException("角色菜单权限更新失败！");
		}
		return ib;
	}
	
	
	
	
	
	
	
	
	
	
	
	/************************************************/
	
	
	@Override
	public List<Map<String, Object>> findAuthByMenuid(long menuid) {
		return menuAuthDao.findAuthByMenuid(menuid);
	}
	
	@Override
	public Map<String, Object> findRoleMenuAuthTmp(String rolecode) {
		List<?> datas = menuDao.getVisibleMenus();
		List<SysMenu> _datas = null;
		if (datas != null) {
			_datas = (List<SysMenu>) datas;
		} else {
			_datas = new ArrayList<SysMenu>();
		}
		List<?> authsTemp = authDao.findVisibleData();
		List<SysAuth> allAuths = null;
		if (authsTemp != null)
			allAuths = (List<SysAuth>) authsTemp;
		else 
			allAuths = new ArrayList<SysAuth>();
		
		List<?> roleMenuAuthTemp = roleMenuAuthDao.findListByRolecode(rolecode);
		List<SysRoleMenuAuth> allRoleMenuAuths = null;
		if (authsTemp != null)
			allRoleMenuAuths = (List<SysRoleMenuAuth>) roleMenuAuthTemp;
		else 
			allRoleMenuAuths = new ArrayList<SysRoleMenuAuth>();
		
		
		List<?> menuAuthTmp = menuAuthDao.findAllMenuAuths();
		List<SysMenuAuth> allMenuAuths = null;
		if (menuAuthTmp != null) 
			allMenuAuths = (List<SysMenuAuth>) menuAuthTmp;
		else 
			allMenuAuths = new ArrayList<SysMenuAuth>();
		MenuTreeFactory tree = new MenuTreeFactory();
		List<MenuTreeBean> returnDatas = new ArrayList<MenuTreeBean>();
		MenuTreeBean mtb = new MenuTreeBean();
		mtb.setId(0);
		mtb.setMenuname("根菜单");
		mtb.setLeaf(false);
		mtb.setParent(-1);
		
		tree.createTrees(returnDatas, mtb, null, _datas, 1);
		returnDatas.add(0, mtb);
		
		//组织表头数据
		List<String> header = new ArrayList<String>();
		header.add("菜单编号,id");
		header.add("菜单名称,menuname");
		header.add(",checkall");
		for (SysAuth sa : allAuths) {
			header.add(sa.getAuthname()+","+sa.getAuthcode());
		}
		
		//组织表格数据
		List<Map<String, Object>> r_data = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for (MenuTreeBean bean : returnDatas) {
			dataMap = new HashMap<String, Object>();
			dataMap.put("id", bean.getId());
			if (bean.getId() == 0) {
				dataMap.put("menuname", "根菜单");
				dataMap.put("parent", -1);
			} else {
				dataMap.put("menuname", bean.getMenuname());
				dataMap.put("parent", bean.getParent());
			}
			dataMap.put("checkall", "checkalls");
			dataMap.put("isLeaf", bean.isLeaf());
			dataMap.put("level", bean.getLevel());
			dataMap.put("expanded", true);
			for (SysAuth sa : allAuths) {
				if (bean.getId() == 0) {
					// 根菜单
					dataMap.put(sa.getAuthcode(), "1|0");
				} else {
					boolean haveAuth = false;
					boolean checked = false;
					for (SysMenuAuth sma : allMenuAuths) {
						haveAuth = false;
						checked = false;
						if (sma.getMenuid() == bean.getId() && sa.getAuthcode().equals(sma.getAuthcode())) {
							haveAuth = true;
							if (Constant.ROLE_ADMIN.equals(rolecode)) {
								checked = true;
							} else {
								for (SysRoleMenuAuth srma : allRoleMenuAuths) {
									if (bean.getId() == srma.getMenuid() && sa.getAuthcode().equals(srma.getAuthcode())) {
										checked = true;
										break;
									}
								}
							}
							break;
						}
					}
					// 菜单是否有对应功能|角色是否有对应功能
					dataMap.put(sa.getAuthcode(), (haveAuth?"1":"0") + "|"+ (checked?"1":"0"));
				}
			}
			r_data.add(dataMap);
		}
		Map<String, Object> _dataMap = new HashMap<String, Object>();
		_dataMap.put("header", header);
		_dataMap.put("data", r_data);
		return _dataMap;
	}
	
	@Override
	public boolean isHavePermission(Request req, String url) {
		try {
			//logger.debug("url >>>>>> " + url);
			if (StringUtil.isNotEmpty(Constant.NOT_CHECK_PERMISSION)) {
				for (String c : Constant.NOT_CHECK_PERMISSION.split("\\|")) {
					if (url.endsWith(c)) return true;
				}
			}
			Object obj = req.session().attribute(Constant.AUTHS);
			String strAuths = String.valueOf(obj);
			Map<String, Map<String, AccessControlBean>> auths = new Gson().fromJson(strAuths, Map.class);
			String urlKey = UrlParser.parserUrlPage(url, Constant.PATSERURL);
			if (StringUtil.isEmpty(urlKey)) return false;
			// 取出该URL下的所有权限信息
			Map<String, AccessControlBean> pageAuths = auths.get(urlKey);
			if (pageAuths == null) return false;
			// 判断URL最后一段权限
			String auth = url.substring(url.lastIndexOf("/")+1);
			if (pageAuths.get(auth) != null) return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
