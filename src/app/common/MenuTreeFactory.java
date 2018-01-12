package app.common;

import java.util.ArrayList;
import java.util.List;

import app.bean.menu.MenuBean;
import app.bean.menu.MenuTreeBean;
import app.entity.SysMenu;

public class MenuTreeFactory {
	/**
	 * 使用递归算法生成树节点
	 * @param parent 上级菜单展示bean
	 * @param pmenu  上级菜单实体对象
	 * @param all     所有菜单集合
	 * @param idx 用于前端设置data-index属性
	 * @return List<MenuBean> 
	 */
	public List<MenuBean> createTrees(MenuBean parent, SysMenu pmenu,List<SysMenu> all, int idx) {		
		long parentId = 0;
		if (pmenu != null) {
			parentId = pmenu.getMenuid();
		}
		List<SysMenu> childs = queryChildNode(parentId,all);
		List<MenuBean> menus = new ArrayList<MenuBean>();
		MenuBean m = null;
		if (childs != null && !childs.isEmpty()) {
			for (SysMenu child : childs) {
				m = new MenuBean();
				m.setId(child.getMenuid());
				m.setIcon(child.getImage());
				m.setName(child.getMenuname());
				m.setUrl(child.getUrl());
				m.setIdx(idx);
				idx++;
				menus.add(m);				
				this.createTrees(m, child, all,idx++);
			}
		}
		if (!menus.isEmpty())
			parent.setSubMenu(menus);
		return menus;
	}
	
	/**
	 * 使用递归算法生成树节点，用于下拉树
	 * @param parent 上级菜单展示bean
	 * @param pmenu  上级菜单实体对象
	 * @param all     所有菜单集合
	 * @return List<MenuBean> 
	 */
	public List<MenuBean> createTrees(MenuBean parent, SysMenu pmenu,List<SysMenu> all) {		
		long parentId = 0;
		if (pmenu != null) {
			parentId = pmenu.getMenuid();
		}
		List<SysMenu> childs = queryChildNode(parentId,all);
		List<MenuBean> menus = new ArrayList<MenuBean>();
		MenuBean m = null;
		if (childs != null && !childs.isEmpty()) {
			for (SysMenu child : childs) {
				m = new MenuBean();
				m.setId(child.getMenuid());
				m.setIcon(child.getImage());
				m.setName(child.getMenuname());
				m.setUrl(child.getUrl());
				menus.add(m);
				this.createTrees(m, child, all);
			}
		}
		if (!menus.isEmpty())
			parent.setSubMenu(menus);
		return menus;
	}

	/**
	 * 使用递归算法生成树节点
	 * @param datas 返回的数据
	 * @param parent 上级菜单展示bean
	 * @param pmenu  上级菜单实体对象
	 * @param all     所有菜单集合
	 * @param level 层级
	 * @return List<MenuBean> 
	 */
	public List<MenuTreeBean> createTrees(List<MenuTreeBean> datas, MenuTreeBean parent, SysMenu pmenu,List<SysMenu> all, int level) {		
		long parentId = 0;
		if (pmenu != null) {
			parentId = pmenu.getMenuid();
		}
		List<SysMenu> childs = queryChildNode(parentId, all);
		
		MenuTreeBean m = null;
		if (childs != null && !childs.isEmpty()) {
			level += 1;
			for (SysMenu child : childs) {
				m = new MenuTreeBean();
				m.setId(child.getMenuid());
				m.setMenuname(child.getMenuname());
				m.setUrl(child.getUrl());
				m.setImage(child.getImage());
				m.setVisible(child.getVisible());
				m.setTooltip(child.getTooltip());
				m.setCreatetime(child.getCreatetime());
				m.setOperator(child.getOperator());
				m.setNodesort(child.getNodesort());
				m.setParent(child.getParentid());
				m.setParentname(parent.getMenuname());
				m.setLeaf(false);
				if (child.getParentid() == 0) {
					level = 1;
					m.setLevel(level);
				} else {
					m.setLevel(level);
				}
				datas.add(m);				
				this.createTrees(datas, m, child, all, level);
			}
		} else {
			parent.setLeaf(true);
		}
		return datas;
	}
	
	/**
	 * 描述：获取菜单树子节点
	 * @param parentid 父菜单编号
	 * @param all      所有菜单实体列表
	 * @return List<SysMenu>
	 */
	private List<SysMenu> queryChildNode(long parentid, List<SysMenu> all) {
		List<SysMenu> childs = new ArrayList<SysMenu>();
		for (SysMenu  sub : all) {
			if (sub.getParentid() == parentid)
				childs.add(sub);
		}
		return childs;
	}
}
