package app.service;

import java.util.List;
import java.util.Map;

import spark.Request;
import spark.page.easyui.TreeBean;
import spark.page.easyui.Page;
import app.bean.InfoBean;
import app.bean.menu.MenuBean;
import app.bean.menu.MenuTreeBean;
import app.entity.SysMenu;

/**
 * 
 * 功能描述 : 页面导航服务类 
 * @author : YH
 * @version:
 */
public interface MenuService {

	/**
	 * 初始化左侧菜单
	 * @param req
	 * @return
	 */
	public List<MenuBean> initMenu(Request req);
	
	/**
	 * 初始化菜单下拉树
	 * @param req
	 * @return
	 */
	public List<MenuBean> buildMenuTree(Request req);
	
	/**
	 * 查询树形菜单列表
	 * @param p 分页类
	 * @param _usercode 操作员用户代码
	 * @return
	 */
	public Page findByPage(Page p, String _usercode);
	
	/**
	 * 查询所有菜单信息
	 * @param _usercode
	 * @return
	 */
	public List<MenuTreeBean> findAllMenus(String _usercode);
	
	/**
	 * 查询菜单树
	 * @param checkedArrs
	 * @return
	 */
	public List<TreeBean> findByMenuCombotree(String []checkedArrs);
	
	/**
	 * 更新菜单
	 * @param req 请求对象
	 * @param values 更新数据
	 * @param _usercode 操作员代码
	 * @return
	 */
	public InfoBean modifyMenu(Request req, Map<String, String> values, String _usercode);
	
	/**
	 * 删除菜单（同步删除菜单权限、角色菜单权限）
	 * @param menuid 菜单代码
	 * @param _usercode 操作员代码
	 * @return
	 */
	public InfoBean deleteMenu(long menuid, String _usercode);
	
	/**
	 * 获取icon列表
	 * @param page
	 * @return
	 */
	public Page findUIcon(Page page);
	
	/**
	 * 组装角色所拥有的菜单及功能
	 * @param rolecode 角色代码
	 * @param _usercode 操作员用户代码
	 * @return
	 */
	public Page findAuthByPage(String rolecode, String _usercode);
}
