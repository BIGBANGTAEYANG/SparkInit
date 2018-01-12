package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : 菜单表 
 * @author : YH
 * @version:
 */
@Table(name="SYS_MENU")
public class SysMenu {
	/**菜单编号*/
	private long menuid;

	/**菜单名称*/
	private String menuname;
	
	/**父菜单编号*/
	private long parentid;
	
	/**菜单URL*/
	private String url;
	
	/**绝对路径还是相对路径  1: 相对路径,2绝对路径*/
	private String relative;
	
	/**菜单图片*/
	private String image;
	
	/**是否可见0：不可见 ,1：可见*/
	private String visible;
	
	/**菜单提示信息*/
	private String tooltip;
	
	/**创建时间*/
	private String createtime;

	/**操作人code*/
	private String operator;

	/**菜单层级*/
	private int level;

	/**菜单排序*/
	private int nodesort;

	public long getMenuid() {
		return menuid;
	}

	public void setMenuid(long menuid) {
		this.menuid = menuid;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public long getParentid() {
		return parentid;
	}

	public void setParentid(long parentid) {
		this.parentid = parentid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRelative() {
		return relative;
	}

	public void setRelative(String relative) {
		this.relative = relative;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNodesort() {
		return nodesort;
	}

	public void setNodesort(int nodesort) {
		this.nodesort = nodesort;
	}
}
