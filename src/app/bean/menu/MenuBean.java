package app.bean.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 功能描述 : 页面导航菜单
 * @author : YH
 * @version:
 */
public class MenuBean {
	/**菜单编号*/
	private long id;
	/**菜单显示名称*/
	private String name;
	/**菜单链接的URL*/
	private String url;
	/**菜单显示的图标*/
	private String icon = "glyphicon glyphicon-certificate";
	/**是否是父节点（默认以.jsp结尾的为叶子节点，其余为父节点）*/
	private int parent;
	/**用于前端设置data-index属性*/
	private int idx;
	/**包含的子菜单*/
	private List<MenuBean> subMenu = new ArrayList<MenuBean>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		this.setParent();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<MenuBean> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<MenuBean> subMenu) {
		this.subMenu = subMenu;
	}

	public int getParent() {
		return parent;
	}

	public void setParent() {
		if(this.url.endsWith(".jsp")) this.parent = 0;
		else this.parent = 1;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
}
