package app.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.db.ObjectDao;
import spark.page.easyui.Page;
import app.common.Constant;
import app.entity.SysMenu;

/**
 * 
 * 功能描述 : 菜单类数据访问 
 * @author : YH
 * @version: 
 * @param <T>
 */
public class MenuDao<T> extends ObjectDao<T> {
	private final static Logger logger = LoggerFactory.getLogger(MenuDao.class);
	
	private final static String FIND_MENU = "SELECT * FROM SYS_MENU WHERE menuid = ?";
	public T getMenu(long menuid) {
		return super.findEntityBySql(Constant.SOURCEID, FIND_MENU, SysMenu.class, new Object[]{menuid});
	}
	
	/**获取所有可用菜单SQL，*/
	private final static String FINDALLMENU_SQL = "SELECT * FROM SYS_MENU ORDER BY LEVEL,nodesort ASC";
	/**
	 * 获取所有菜单
	 * @return
	 */
	public List<?> findAllMenus() {
		return super.findBySqlList(Constant.SOURCEID, FINDALLMENU_SQL, SysMenu.class, null);
	}
	
	/**获取所有可用菜单SQL，*/
	private final static String GETALLVISIBLEMENU_SQL = "SELECT * FROM SYS_MENU WHERE visible = '1' ORDER BY LEVEL,nodesort ASC";
	/**
	 * 获取所有可见菜单
	 * @return
	 */
	public List<?> getVisibleMenus() {
		return super.findBySqlList(Constant.SOURCEID, GETALLVISIBLEMENU_SQL, SysMenu.class, null);
	}
	
	/**菜单树分页检索SQL，*/
	private final static String FINDBY_MENU_TREEGRID_SQL = "SELECT * FROM (SELECT  menuid, menuname, parentid _parentId, parentid, url, relative, image, nodesort, visible, tooltip, createtime, operator, LEVEL FROM SYS_MENU ORDER BY LEVEL,nodesort ASC)t";
	
	/**
	 * 菜单树分页检索
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findByList() throws Exception{
		return super.findMapListBysql(Constant.SOURCEID, FINDBY_MENU_TREEGRID_SQL, null);
	}
	
	/**
	 * 菜单树分页检索
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public Page findByPage(Page p) throws Exception{
		return super.findPageByMysql(Constant.SOURCEID, FINDBY_MENU_TREEGRID_SQL, p, null);
	}
	
	/**下拉树菜单查询SQL*/
	private static final String FINDBYMENUCOMBOTREE_SQL = "select * from (SELECT menuid AS id,menuname AS TEXT,parentid ,url,relative,image,nodesort,visible,tooltip,createtime,operator,level FROM SYS_MENU)t";
	/**
	 * 描述：查询下拉菜单树
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findByMenuCombotree(){
		return super.findMapListBysql(Constant.SOURCEID, FINDBYMENUCOMBOTREE_SQL, null);
	}
	
	/**菜单修改SQL*/
	private static final String MODIFY_SQL = "update SYS_MENU set menuname = ?, parentid = ?,url = ?, relative = ?, image = ?, visible = ?, tooltip = ?, nodesort = ?, operator = ?, createtime = ? where menuid = ?";
	/**菜单新增SQL*/
	private static final String INSERT_SQL = "insert into SYS_MENU (menuname,parentid,url,relative,image,visible,tooltip,nodesort,level,operator,createtime) values(?,?,?,?,?,?,?,?,?,?,?)";
	/**
	 * 菜单新增或更新
	 * @param menu
	 * @param updateFlag
	 * @return
	 */
	public int saveOrUpdateMenu(SysMenu menu) {
		if (menu.getMenuid() > 0) {
			// 修改
			return super.update(Constant.SOURCEID, MODIFY_SQL, new Object[]{menu.getMenuname(),menu.getParentid(),menu.getUrl(),menu.getRelative(),menu.getImage(),menu.getVisible(),menu.getTooltip(),menu.getNodesort(),menu.getOperator(), menu.getCreatetime(),menu.getMenuid()});
		} else {
			// 新增
			return super.update(Constant.SOURCEID, INSERT_SQL, new Object[]{menu.getMenuname(),menu.getParentid(),menu.getUrl(),menu.getRelative(),menu.getImage(),menu.getVisible(),menu.getTooltip(),menu.getNodesort(),menu.getLevel(),menu.getOperator(),menu.getCreatetime()});
		}
	}
	
	/**删除菜单表SQL*/
	private static final String DELETEBYCODE_MENU_SQL = "delete from SYS_MENU where menuid = ?";
	/**删除菜单权限表SQL*/
	private static final String DELETEBYCODE_MENU_AUTH_SQL = "delete from SYS_MENU_AUTH where menuid=?";
	/**删除角色菜单权限表SQL*/
	private static final String DELETEBYCODE_ROLE_MENU_AUTH_SQL = "delete from SYS_ROLE_MENU_AUTH where menuid=?";
	/**
	 * 删除菜单（同步删除菜单权限、角色菜单权限）
	 * @param menuid 菜单编号
	 * @return
	 */
	public boolean deleteMenu(long menuid) {
		try {
			super.update(Constant.SOURCEID, DELETEBYCODE_MENU_AUTH_SQL, new Object[]{menuid});
			super.update(Constant.SOURCEID, DELETEBYCODE_ROLE_MENU_AUTH_SQL, new Object[]{menuid});
			super.update(Constant.SOURCEID, DELETEBYCODE_MENU_SQL, new Object[]{menuid});
		} catch (Exception e) {
			logger.error("删除菜单失败！", e);
			return false;
		}
		return true;
	}
}
