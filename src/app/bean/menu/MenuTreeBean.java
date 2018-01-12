package app.bean.menu;

/**
 * 
 * 功能描述 : 树形菜单列表
 * @author : YH
 * @version:
 */
public class MenuTreeBean {
	/**菜单编号*/
	private long id;
	
	/**菜单名称*/
	private String menuname;
	
	/**菜单链接的URL*/
	private String url;
	
	/**菜单图片*/
	private String image;
	
	/**是否可见0：不可见 ,1：可见*/
	private String visible;
	
	/**菜单图片  用于展示*/
	private String _image;
	
	/**是否可见0：不可见 ,1：可见   用于展示*/
	private String _visible;
	
	/**菜单提示信息*/
	private String tooltip;
	
	/**创建时间*/
	private String createtime;

	/**操作人code*/
	private String operator;
	
	/**菜单排序*/
	private int nodesort;
	
	/**层级，最高级为0*/
	private int level;
	
	/**父节点ID*/
	private long parent;
	
	private String parentname;
	
	/**是否为叶子节点*/
	private boolean isLeaf;
	
	
	private boolean expanded = true;
	
	private boolean loaded = true;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		
		this.image = image;
		this._image = image;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
		this._visible = visible;
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

	public int getNodesort() {
		return nodesort;
	}

	public void setNodesort(int nodesort) {
		this.nodesort = nodesort;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}
}
