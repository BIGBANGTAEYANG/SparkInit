package app;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import app.bean.InfoBean;
import app.bean.auth.AccessControlBean;
import app.common.ConfigUtil;
import app.common.Constant;
import app.common.PageUtil;
import app.common.StringUtil;
import app.service.AuthService;
import app.service.LoginService;
import app.service.MenuService;
import app.service.OrgService;
import app.service.RoleAuthService;
import app.service.RoleService;
import app.service.UserService;
import app.service.impl.AuthServiceImpl;
import app.service.impl.LoginServiceImpl;
import app.service.impl.MenuServiceImpl;
import app.service.impl.OrgServiceImpl;
import app.service.impl.RoleAuthServiceImpl;
import app.service.impl.RoleServiceImpl;
import app.service.impl.UserServiceImpl;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.Spark;
import spark.annotation.Auto;
import spark.page.easyui.Page;
import spark.render.JsonRender;
import spark.servlet.ISparkApplication;
import spark.servlet.SparkFilter;

/**
 * 总控制器
 */
public class MyAppliction implements ISparkApplication {
	@Auto(name = LoginServiceImpl.class)
	private LoginService loginServive;
	
	@Auto(name = MenuServiceImpl.class)
	private MenuService menuService;
	
	@Auto(name = AuthServiceImpl.class)
	private AuthService authService;
	
	@Auto(name = UserServiceImpl.class)
	private UserService userService;
	
	@Auto(name = OrgServiceImpl.class)
	private OrgService orgService;
	
	@Auto(name = RoleServiceImpl.class)
	private RoleService roleService;
	
	@Auto(name = RoleAuthServiceImpl.class)
	private RoleAuthService roleAuthService;
	
	private final static Logger logger = LoggerFactory.getLogger(MyAppliction.class);
	
	@Override
	public void run() {
		/************************************************** 开启定时任务 ********************************/
		
		/**********************************************************************************************************************/
		
		
		/**
		 * 用户登陆有效性验证
		 */
		Spark.before(new Filter() {
			@Override
			public void handle(Request request, Response response) {
				String url = request.raw().getRequestURI();
				if (!SparkFilter.isIgnore(url)) {
					Session session = request.session(false);
					if (session == null || session.attribute(Constant.USERNAME) == null) {
						if (session == null) {
							logger.error("session is null！");
						} else if (session.attribute(Constant.USERNAME) == null) {
							logger.error("username is null！");
						}
						if (request.raw().getHeader("x-requested-with") != null 
								&& request.raw().getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
							// 如果是ajax请求，返回超时标识，前端ajax全局方法complete统一处理超时情况
							response.raw().setHeader("sessionstatus", "timeout");	
						} else {
							response.redirect(request.raw().getContextPath() + "/login.jsp");
						}
					}
				}
				try {
					if (!ConfigUtil.getInstance().getYKCPBean().AUTHS.containsKey(url)) {
						// 检查权限
						boolean isHave = authService.isHavePermission(request, url);
						if (!isHave) {
							if (request.raw().getHeader("x-requested-with") != null 
									&& request.raw().getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
								// 如果是ajax请求，返回超时标识，前端ajax全局方法complete统一处理
								response.raw().setHeader("sessionstatus", "auth");	 
							} else {
								response.redirect(request.raw().getContextPath() + "/authstip.html");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		/**
		 * 验证是否有页面权限查看权限
		 */
		Spark.post(new Route("/app/sys/auth/vaildAuth", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				boolean vaildAuth = false;
				Session session = null;
				try {
					session = request.session(false);
					String url = request.queryParams("url");
					@SuppressWarnings("unchecked")
					Map<String,Map<String,AccessControlBean>> map = new Gson().fromJson(String.valueOf(session.attribute(Constant.AUTHS)), Map.class);
					Set<String> set = map.keySet();
					Iterator<String> i = set.iterator();
					while(i.hasNext()) {
						if (i.next().equals(url)) {
							vaildAuth = true;
							break;
						}
					}
				} catch (Exception e) {
					logger.error("证是否有页面权限查看权限错误！", e);
//					e.printStackTrace();
					vaildAuth = true;
				}
				if (!vaildAuth) {
					if (session != null) {
						session.invalidate();
					}
					response.raw().setHeader("sessionstatus", "timeout");
				}
				return vaildAuth?1:0;
			}
		});
		
		/**
		 * 登录
		 */
		Spark.post(new Route("/app/sys/auth/login", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String userCode = request.queryParams("userCode");
				String passWord = request.queryParams("passWord");
				InfoBean ib = loginServive.login(request, userCode, passWord);
				return new JsonRender().render(ib).toString();
			}
		});
		
		/**
		 * 进入主界面
		 */
		Spark.get(new Route("/app/sys/auth/main", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				logger.info("用户【"+ request.session().attribute(Constant.USERNAME) +"】登录成功，进入主界面！");
				response.redirect(request.raw().getContextPath() + "/page/main.jsp");
				return null;
			}
		});
		
		/**
		 * 登出
		 */
		Spark.get(new Route("/app/sys/auth/logout", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				Session session = request.session(false);
				if (session != null) {
					session.invalidate();
				}
				halt(401, "logout", "/login.jsp");
				return null;
			}
		});
		
		/**
		 * 初始化菜单
		 */
		Spark.post(new Route("/app/sys/menu/initMenu", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				return new JsonRender().render(menuService.initMenu(request));
			}
		});
		
		/**
		 * 初始化菜单下拉树
		 */
		Spark.post(new Route("/app/sys/menu/buildMenuTree", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				return new JsonRender().render(menuService.buildMenuTree(request));
			}
		});
		
		/**
		 * 菜单管理列表
		 */
		Spark.post(new Route("/app/sys/menu/view", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(menuService.findAllMenus(_usercode));
			}
		});
		
		/**
		 * 菜单更新
		 */
		Spark.post(new Route("/app/sys/menu/modify", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				Map<String, String> values = PageUtil.getParamMap(request.raw());
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(menuService.modifyMenu(request, values, _usercode)).toString();
			}
		});
		
		/**
		 * 删除菜单（同步删除菜单权限、角色菜单权限）
		 */
		Spark.post(new Route("/app/sys/menu/delete", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String menuid = request.queryParams("menuid");
				long menuidTmp = 0;
				if (StringUtil.isNotEmpty(menuid)) {
					menuidTmp = Long.parseLong(menuid);
				}
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(menuService.deleteMenu(menuidTmp, _usercode)).toString();
			}
		});
		
		/**
		 * 更新菜单功能
		 */
		Spark.post(new Route("/app/sys/menu/updateMenuAuth", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String auths = request.queryParams("auths");
				String menuid = request.queryParams("menuid");
				long menuidTmp = 0;
				if (StringUtil.isNotEmpty(menuid)) {
					menuidTmp = Long.parseLong(menuid);
				}
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(authService.updateMenuAuths(menuidTmp, auths, _usercode)).toString();
			}
		});
		
		/**
		 * 菜单功能列表
		 */
		Spark.post(new Route("/app/sys/auth/viewMenuAuths", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String menuid = request.queryParams("menuid");
				long menuidTmp = 0;
				if (StringUtil.isNotEmpty(menuid)) {
					menuidTmp = Long.parseLong(menuid);
				}
				return new JsonRender().render(authService.findAuthByMenuid(menuidTmp)).toString();
			}
		});
		
		
		/**
		 * 权限列表
		 */
		Spark.post(new Route("/app/sys/auth/view", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				Page p = new Page();
				p.setMaxNum(Integer.parseInt(request.raw().getParameter("rows")));
				p.setPage(Integer.parseInt(request.raw().getParameter("page")));
				p.setSort(request.raw().getParameter("sort"));
				p.setOrder(request.raw().getParameter("order"));
				return new JsonRender().render(authService.findAuths(p)).toString();
			}
		});
		
		/**
		 * 更新权限
		 */
		Spark.post(new Route("/app/sys/auth/modify", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String authcode = request.queryParams("authcode");
				String authname = request.queryParams("authname");
				String status = request.queryParams("status");
				String des = request.queryParams("des");
				String updateFlag = request.queryParams("updateFlag");
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(authService.updateAuth(authcode, authname, status, des, updateFlag, _usercode)).toString();
			}
		});
		
		/**
		 * 删除权限
		 */
		Spark.post(new Route("/app/sys/auth/delete", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String authcode = request.queryParams("authcode");
				String usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(authService.deleteAuth(authcode, usercode)).toString();
			}
		});
		
		/**
		 * 检查当前登录用户所拥有的权限列表
		 */
		Spark.get(new Route("/app/sys/auth/viewFunctionAuths", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String code = request.queryParams("code");
				return new JsonRender().render(authService.checkFuncitonAuths(request, code)).toString();
			}
		});
		
		/**************************************************************** 人员管理 *********************************************/
		/**
		 * 获取用户列表
		 */
		Spark.post(new Route("/app/sys/user/view", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				Page p = new Page();
				p.setMaxNum(Integer.parseInt(request.raw().getParameter("rows")));
				p.setPage(Integer.parseInt(request.raw().getParameter("page")));
				p.setSort(request.raw().getParameter("sort"));
				p.setOrder(request.raw().getParameter("order"));
				return new JsonRender().render(userService.getAllUsers(p)).toString();
			}
		});
		
		/**
		 * 更新用户信息
		 */
		Spark.post(new Route("/app/sys/user/modify", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String userid = request.queryParams("userid");
				long tarUserid = 0;
				if (StringUtil.isNotEmpty(userid)) {
					tarUserid = Long.parseLong(userid);
				}
				String username = request.queryParams("username");
				String status = request.queryParams("status");
				int _status = 0;
				if (StringUtil.isNotEmpty(status)) {
					_status = Integer.parseInt(status);
				}
				String des = request.queryParams("des");
				String _usercode = request.session().attribute(Constant.USERCODE);
				String orgcode=request.queryParams("orgcode");
				String usercode=request.queryParams("usercode");
				String empid=request.queryParams("empid");
				return new JsonRender().render(userService.modifyUser(tarUserid, usercode, username, orgcode, _status, des, _usercode, empid)).toString();
			}
		});
		
		/**
		 * 删除用户
		 */
		Spark.post(new Route("/app/sys/user/delete", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String tarUsercode = request.queryParams("usercode");
				InfoBean ib = null;
				if (StringUtil.isEmpty(tarUsercode)) {
					ib = new InfoBean();
					ib.setCode(InfoBean.FAILURE);
					ib.setMessage("请选择需要删除的用户！");
				} else {
					String _usercode = request.session().attribute(Constant.USERCODE);
					ib = userService.deleteUser(tarUsercode, _usercode);
				}
				return new JsonRender().render(ib).toString();
			}
		});
		
		/**
		 * 重置用户密码
		 */
		Spark.post(new Route("/app/sys/user/resetpwd", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String tarUsercode = request.queryParams("usercode");
				InfoBean ib = null;
				if (StringUtil.isEmpty(tarUsercode)) {
					ib = new InfoBean();
					ib.setCode(InfoBean.FAILURE);
					ib.setMessage("请选择需要重置密码的用户！");
				} else {
					String _usercode = request.session().attribute(Constant.USERCODE);
					ib = userService.resetPwdUser(tarUsercode, _usercode);
				}
				return new JsonRender().render(ib).toString();
			}
		});
		
		/**
		 * 查看用户所拥有的角色
		 */
		Spark.post(new Route("/app/sys/user/selectrole", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String tarUsercode = request.queryParams("tarUsercode");
				String tarOrgcode = request.queryParams("tarOrgcode");
				return new JsonRender().render(userService.getAllRolesByUser(tarUsercode,tarOrgcode)).toString();
			}
		});
		
		/**
		 * 更新用户角色
		 */
		Spark.post(new Route("/app/sys/user/updateUserRole", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String tarUsercode = request.queryParams("tarUsercode");
				String roles = request.queryParams("roles");
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(userService.updateUserResRoles(tarUsercode, roles, _usercode)).toString();
			}
		});
		
		/**
		 * 用户修改密码
		 */
		Spark.post(new Route("/app/sys/user/modifypwd", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String oldpwd = request.queryParams("oldpwd");
				String newpwd = request.queryParams("newpwd");
				String usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(userService.modifypwd(usercode, oldpwd, newpwd)).toString();
			}
		});
		
		
		/**
		 * 初始化机构下拉树
		 */
		Spark.post(new Route("/app/sys/org/buildOrgCombotree", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(orgService.findByOrgCombotree(null, usercode));
			}
		});
		
		/**************************************************************** 角色管理 *********************************************/
		/**
		 * 获取角色列表
		 */
		Spark.post(new Route("/app/sys/role/view", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				Page p = new Page();
				p.setMaxNum(Integer.parseInt(request.raw().getParameter("rows")));
				p.setPage(Integer.parseInt(request.raw().getParameter("page")));
				p.setSort(request.raw().getParameter("sort"));
				p.setOrder(request.raw().getParameter("order"));
				return new JsonRender().render(roleService.findRoles(p)).toString();
			}
		});
		
		/**
		 * 保存角色信息
		 */
		Spark.post(new Route("/app/sys/role/modify", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String rolecode = request.queryParams("rolecode");
				String rolename = request.queryParams("rolename");
				String des = request.queryParams("des");
				String opflag = request.queryParams("opflag");
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(roleService.updateRole(rolecode, rolename, des, opflag, _usercode)).toString();
			}
		});
		
		/**
		 * 删除角色信息
		 */
		Spark.post(new Route("/app/sys/role/delete", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String rolecode = request.queryParams("rolecode");
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(roleService.deleteRole(rolecode, _usercode)).toString();
			}
		});
		
		/**
		 * 角色菜单功能
		 */
		Spark.post(new Route("/app/sys/role/viewRoleMenuAuth", false, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String _usercode = request.session().attribute(Constant.USERCODE);
				String rolecode = request.queryParams("rolecode");
				return new JsonRender().render(authService.findRoleMenuAuthTmp(rolecode));
			}
		});
		
		/**
		 * 更新角色菜单权限信息
		 */
		Spark.post(new Route("/app/sys/role/updateRoleMenuAuth", true, Constant.SOURCEID) {
			@Override
			public Object handle(Request request, Response response) {
				String param = request.queryParams("parameter");
				String rolecode = request.queryParams("rolecode");
				String _usercode = request.session().attribute(Constant.USERCODE);
				return new JsonRender().render(authService.updateRoleMenuAuth(param, rolecode, _usercode)).toString();
			}
		});
	}
}