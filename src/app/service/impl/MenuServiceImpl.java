package app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bean.InfoBean;
import app.bean.auth.AccessControlBean;
import app.bean.menu.MenuBean;
import app.bean.menu.MenuTreeBean;
import app.common.Constant;
import app.common.DateUtil;
import app.common.HodsLogUtil;
import app.common.LogState;
import app.common.MenuTreeFactory;
import app.common.StringUtil;
import app.common.TreeFactory;
import app.common.UrlParser;
import app.dao.AuthDao;
import app.dao.GeneralDao;
import app.dao.MenuDao;
import app.entity.SysAuth;
import app.entity.SysIcon;
import app.entity.SysMenu;
import app.exception.StatusException;
import app.service.AuthService;
import app.service.MenuService;
import app.service.RoleAuthService;
import app.service.UserService;
import spark.Request;
import spark.annotation.Auto;
import spark.page.easyui.TreeBean;
import spark.page.easyui.Page;

public class MenuServiceImpl implements MenuService {

	private final static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
	@Auto (name = RoleAuthServiceImpl.class)
	private RoleAuthService roleAuthService;
	
	@Auto (name = AuthServiceImpl.class)
	private AuthService authService;
	
	@Auto(name = UserServiceImpl.class)
	private UserService userService;
	
	@Auto (name = GeneralDao.class)
	private GeneralDao<?> generalDao;
	
	@Auto (name = MenuDao.class)
	private MenuDao<?> menuDao;
	
	@Auto (name = AuthDao.class)
	private AuthDao<?> authDao;
	
	@Override
	public List<MenuBean> initMenu(Request req) {
		// 从session里获取用户功能权限
		Map<String,Map<String,AccessControlBean>> auths = roleAuthService.reloadAuthBySession(req);
		if (auths == null || auths.isEmpty()) return null;
		//req.session().attribute(Constant.AUTHS, auths);
		// 根据用户权限初始菜单
		@SuppressWarnings("unchecked")
		List<SysMenu> lists = (List<SysMenu>) this.menuDao.getVisibleMenus();
		MenuTreeFactory tree = new MenuTreeFactory();
		SysMenu root = new SysMenu();
		root.setMenuid(0);
		return tree.createTrees(new MenuBean(), root, authMenuFilter(auths, lists), 5);
	}
	
	@Override
	public List<MenuBean> buildMenuTree(Request req) {
		// 根据用户权限初始菜单
		@SuppressWarnings("unchecked")
		List<SysMenu> lists = (List<SysMenu>) this.menuDao.getVisibleMenus();
		MenuTreeFactory tree = new MenuTreeFactory();
		MenuBean root = new MenuBean();
		root.setId(0);
		root.setName("根菜单");
		List<MenuBean> childDatas = tree.createTrees(new MenuBean(), null, lists);
		List<MenuBean> datas = new ArrayList<MenuBean>();
		root.setSubMenu(childDatas);
		datas.add(root);
		return datas;
	}
	
	/**
	 * 根据用户所拥有的权限初始化菜单
	 * @param auths
	 * @return
	 */
	private List<SysMenu> authMenuFilter(Map<String,Map<String,AccessControlBean>> auths,List<SysMenu> lists) {
		List<SysMenu> news = new ArrayList<SysMenu>();
		String url = "";
		for (SysMenu m : lists) {
			if ("/".equals(m.getUrl())) {
				news.add(m);
				continue;
			}
			url = "/"+UrlParser.parserUrlPage(m.getUrl(), Constant.PATSERURL);
			if (auths.containsKey(url)) {
			   news.add(m);	
			}
		}
		return news;
	}

	@Override
	public List<MenuTreeBean> findAllMenus(String _usercode) {
		List<?> datas = menuDao.findAllMenus();
		List<SysMenu> _datas = null;
		if (datas != null) {
			_datas = (List<SysMenu>) datas;
		} else {
			_datas = new ArrayList<SysMenu>();
		}
		MenuTreeFactory tree = new MenuTreeFactory();
		List<MenuTreeBean> returnDatas = new ArrayList<MenuTreeBean>();
		MenuTreeBean mtb = new MenuTreeBean();
		mtb.setId(0);
		mtb.setMenuname("根菜单");
		mtb.setLeaf(false);
		mtb.setParent(-1);
		
		tree.createTrees(returnDatas, mtb, null, _datas, 1);
		returnDatas.add(0, mtb);
		return returnDatas;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page findByPage(Page p, String _usercode) {
		try {
			Map<String,String> head = new HashMap<String,String>();
			head.put("menuid", "0");
			head.put("menuname", "菜单列表");
			head.put("level", "0");
			p.setMaxNum(100);
			p = menuDao.findByPage(p);
			if (p != null && p.getRows()!= null && !p.getRows().isEmpty()) {
				List<Map<String, String>> lis = new ArrayList<Map<String, String>>();
				lis.add(head);
				Map<String, Object> map = null;
				Map<String, String> mapTmp = null;
				for (int i = 0;i<p.getRows().size();i++) {
					mapTmp = new HashMap<String, String>();
					map = (Map<String, Object>) p.getRows().get(i);
					mapTmp.put("menuid", String.valueOf(map.get("menuid")));
					mapTmp.put("menuname", String.valueOf(map.get("menuname")==null?"":map.get("menuname")));
					mapTmp.put("_parentId", String.valueOf(map.get("_parentId")));
					mapTmp.put("url", String.valueOf(map.get("url")==null?"":map.get("url")));
					mapTmp.put("relative", String.valueOf(map.get("relative")==null?"":map.get("relative")));
					mapTmp.put("image", String.valueOf(map.get("image")==null?"":map.get("image")));
					mapTmp.put("nodesort", String.valueOf(map.get("nodesort")));
					mapTmp.put("visible", String.valueOf(map.get("visible")==null?"":map.get("visible")));
					mapTmp.put("tooltip", String.valueOf(map.get("tooltip")==null?"":map.get("tooltip")));
					mapTmp.put("createtime", String.valueOf(map.get("createtime")==null?"":map.get("createtime")));
					mapTmp.put("operator", String.valueOf(map.get("operator")==null?"":map.get("operator")));
					mapTmp.put("level", String.valueOf(map.get("level")));
					mapTmp.put("parentId", String.valueOf(map.get("parentId")));
					lis.add(mapTmp);
				}
				p.getRows().clear();
				//p.getRows().add(0,head);
				p.getRows().addAll(lis);
				p.total++;
			}
			if (p == null) {
				p = new Page();
			}
			return p;
		} catch (Exception e) {
			logger.error("用户["+ _usercode +"]查询菜单信息异常！", e);
			//e.printStackTrace();
		}
		return new Page();
	}

	@Override
	public List<TreeBean> findByMenuCombotree(String[] checkedArrs) {
		List<Map<String, Object>> list = menuDao.findByMenuCombotree();
		List<TreeBean> treeList = new ArrayList<TreeBean>();
		if (list != null && !list.isEmpty()) {
			//加入顶级菜单，id为0
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", 0);
			map.put("text","菜单列表");
			list.add(0,map);
			TreeFactory tree = new TreeFactory();
			treeList = tree.createTrees(list.get(0), new TreeBean(), list,checkedArrs);
		}
		return treeList;
	}
	
	@Override
	public InfoBean modifyMenu(Request req, Map<String, String> values, String _usercode) {
		InfoBean ib = new InfoBean();
		if (!userService.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (values == null || values.isEmpty()) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("菜单更新失败！");
			return ib;
		}
		String op = "新增";
		try {
			SysMenu menu = new SysMenu();
			// 判断是新增还是修改
			String menuid = values.get("menuid");
			if (StringUtil.isNotEmpty(menuid)) {
				// 修改
				op = "修改";
				menu.setMenuid(Long.parseLong(values.get("menuid")));
				menu.setLevel(Integer.parseInt(values.get("level")));
			}
			menu.setMenuname(values.get("menuname"));
			menu.setParentid(Long.parseLong(values.get("parentId")));
			menu.setUrl(values.get("url"));
//			menu.setRelative(values.get("relative"));
			menu.setRelative("1"); // 默认“1”
			menu.setImage(values.get("image"));
			menu.setTooltip(values.get("tooltip"));
			menu.setVisible(values.get("visible"));
			menu.setNodesort(Integer.parseInt(values.get("nodesort")));
			menu.setOperator(_usercode);
			menu.setCreatetime(DateUtil.formatTime(System.currentTimeMillis(), DateUtil.DEF_DATETIME_FORMAT));
			// 判断上级目录是否为叶子目录
			if (menu.getParentid() > 0) {
				Object obj = menuDao.getMenu(menu.getParentid());
				if (obj == null) {
					ib.setCode(InfoBean.FAILURE);
					ib.setMessage("菜单更新失败，未获取到父菜单信息！");
					return ib;
				}
				SysMenu parentMenu = (SysMenu) obj;
				if (parentMenu.getLevel() != 1) {
					ib.setCode(InfoBean.FAILURE);
					ib.setMessage("菜单更新失败，只支持二级菜单！");
					return ib;
				} else {
					if (menu.getLevel() == 0) {
						menu.setLevel(2);
					}
				}
				if (menu.getLevel() <= parentMenu.getLevel()) {
					ib.setCode(InfoBean.FAILURE);
					ib.setMessage("菜单更新失败，请重新选择父菜单！");
					return ib;
				}
				if (menu.getMenuid() == parentMenu.getMenuid()) {
					ib.setCode(InfoBean.FAILURE);
					ib.setMessage("菜单更新失败，请重新选择父菜单！");
					return ib;
				}
			} else {
				menu.setLevel(1);
			}
			int status = menuDao.saveOrUpdateMenu(menu);
			if (status == 1) {
				ib.setCode(InfoBean.SUCCESS);
				ib.setMessage("菜单更新成功！");
				String info = "用户["+ _usercode +"]"+ op +"菜单成功！"+ op +"后菜单名称["+ menu.getMenuname() +"]，父级菜单编号["+ menu.getParentid() +"]，菜单URL["+ menu.getUrl() +"]，菜单图标["+ menu.getImage() +"]，菜单等级["+ menu.getLevel() +"]，菜单排序["+ menu.getNodesort() +"]，菜单是否可用["+ menu.getVisible() +"]";
				logger.info(info);
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "菜单"+op, "菜单管理", "SYS_MENU", LogState.成功, System.currentTimeMillis(), info, null);
			} else {
				ib.setCode(InfoBean.FAILURE);
				ib.setMessage("菜单更新失败！");
				String info = "用户["+ _usercode +"]"+ op +"菜单失败！"+ op +"后菜单名称["+ menu.getMenuname() +"]，父级菜单编号["+ menu.getParentid() +"]，菜单URL["+ menu.getUrl() +"]，菜单图标["+ menu.getImage() +"]，菜单等级["+ menu.getLevel() +"]，菜单排序["+ menu.getNodesort() +"]，菜单是否可用["+ menu.getVisible() +"]";
				logger.error(info);
				HodsLogUtil.getInstance().AddAuditLog(_usercode, "菜单"+op, "菜单管理", "SYS_MENU", LogState.失败, System.currentTimeMillis(), info, null);
			}
		} catch (Exception e) {
			logger.error("用户["+ _usercode +"]"+ op +"菜单异常！", e);
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("菜单更新失败！");
			String info = "用户["+ _usercode +"]"+ op +"菜单异常！" + e.getMessage();
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "菜单"+op, "菜单管理", "SYS_MENU", LogState.失败, System.currentTimeMillis(), info, null);
		}
		return ib;
	}
	
	@Override
	public InfoBean deleteMenu(long menuid, String _usercode) {
		InfoBean ib = new InfoBean();
		if (!userService.isHavePermission(_usercode, new String[]{Constant.ROLE_ADMIN})) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("您无权限执行此操作！");
			return ib;
		}
		if (menuid == 0) {
			ib.setCode(InfoBean.FAILURE);
			ib.setMessage("请选择有效的菜单！");
			return ib;
		}
		boolean isSuccess = false;
		try {
			isSuccess = menuDao.deleteMenu(menuid);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("用户["+ _usercode +"]删除菜单编号["+ menuid +"]异常！", e);
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "删除菜单", "菜单管理", "SYS_MENU", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]删除菜单编号["+ menuid +"]异常！"+e.getMessage(), null);
			throw new StatusException("删除菜单失败！");
		}
		if (isSuccess) {
			ib.setCode(InfoBean.SUCCESS);
			ib.setMessage("删除菜单成功！");
			logger.info("用户["+ _usercode +"]删除菜单编号["+ menuid +"]成功！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "删除菜单", "菜单管理", "SYS_MENU", LogState.成功, System.currentTimeMillis(), "用户["+ _usercode +"]删除菜单编号["+ menuid +"]成功！", null);
		} else {
//			ib.setCode(InfoBean.FAILURE);
//			ib.setMessage("删除菜单失败！");
			logger.error("用户["+ _usercode +"]删除菜单编号["+ menuid +"]失败！");
			HodsLogUtil.getInstance().AddAuditLog(_usercode, "删除菜单", "菜单管理", "SYS_MENU", LogState.失败, System.currentTimeMillis(), "用户["+ _usercode +"]删除菜单编号["+ menuid +"]失败！", null);
			throw new StatusException("删除菜单失败！");
		}
		return ib;
	}
	
	@Override
	public Page findUIcon(Page page) {
		page = generalDao.findPageAllDatas(SysIcon.class, page);
		if (page == null) {
			page = new Page();
		}
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page findAuthByPage(String rolecode, String _usercode) {
		Page page = new Page();
		if (StringUtil.isEmpty(rolecode)) {
			return page;
		}
		// 所有菜单列表
		List<Map<String, Object>> allMenuList = null;

		// 查所有菜单
		page = this.findByPage(page, _usercode);
		if (page != null) {
			allMenuList = page.getRows();
		}
		if (allMenuList != null && !allMenuList.isEmpty()) {
			// 记录计算后的菜单列表
			List<Map<String, Object>> myMenuList = null;
			// 记录角色对应单个菜单的功能列表
			List<String> roleMenuAuthList = null;
			// 记录单个菜单功能列表
			List<String> menuAllAuthList = null;
			long menuid = 0; 
			String authcode = null;
			String checedStr = "", menuid_authcode = "";
			// 查询角色所拥有菜单功能
			Map<String, Object> tempMap = authService.findRoleMenuAuth(rolecode);
			//List<String> menucodes = (List<String>) tempMap.get("menucodes");
			Map<Long, List<String>> map = (Map<Long, List<String>>) tempMap.get("map");
			// 获取所有菜单功能列表
			Map<Long, List<String>> menuAuthMap = authService.findMenuAuthMap();
			// 获取所有权限列表
			List<SysAuth> authList = (List<SysAuth>) authDao.findAllData();
			int authNum = 0;
			int disableNum = 0;
			myMenuList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> tmap : allMenuList) {
				menuid = Long.parseLong(StringUtil.getString(tmap.get("menuid")));
				roleMenuAuthList = map.get(menuid);
				menuAllAuthList = menuAuthMap.get(menuid);
				
				// 对角色有的菜单功能处理，有权限的选中，没有权限的不选中，没有的功能置灰
				authNum = 0;
				disableNum = 0;
				for (SysAuth sa : authList) {
					authcode = sa.getAuthcode();
					menuid_authcode = "m"+menuid + "_a" + authcode;
					// 设置显示列值，以authid_+功能ID,值以菜单ID+功能ID
					if (menuAllAuthList != null && !menuAllAuthList.isEmpty() && menuAllAuthList.contains(authcode)) {
						if (roleMenuAuthList != null && !roleMenuAuthList.isEmpty() && roleMenuAuthList.contains(authcode)){
							checedStr = "<input type='checkbox' name='" + menuid_authcode + "' id='" + menuid_authcode + "' value='0' checked='checked'/> ";
							authNum ++;
						} else
							checedStr = "<input type='checkbox' name='" + menuid_authcode + "' id='" + menuid_authcode + "' value='0' />";
					} else {
						if (0 == menuid)
							checedStr = "<input type='checkbox' id='_a" +authcode + "' value='0' onclick='checkAllColumns(this)' />";
						else {
							checedStr = "<input type='checkbox' disabled='disabled'/>";
							authNum ++;
							disableNum++;
						}
					}
					tmap.put("authid_" + authcode, checedStr);
					checedStr = "";
				}
				// 判断此菜单是否全选(0：选中，1：未选中)，全选排除全部disabled的情况
				if (authNum == authList.size() && authList.size() > disableNum) {
					tmap.put("sort", 0);
				} else {
					tmap.put("sort", 1);
				}
				
				myMenuList.add(tmap);
			}
			page.setRows(myMenuList);
		}
		
		return page;
	}
}
