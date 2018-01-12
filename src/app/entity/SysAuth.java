package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : 权限表
 * @author : YH
 * @version:
 */
@Table(name="SYS_AUTH")
public class SysAuth {
	/**权限代码，唯一*/
	private String authcode;
	
	/**权限中文名称*/
	private String authname;
	
	/**创建时间*/
    private String createtime;

    /**备注信息*/
	private String des;

	 /**功能图片*/
	private String image;

	 /**是否有效: 1有效  0无效，默认值为1*/
	private String status;

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public String getAuthname() {
		return authname;
	}

	public void setAuthname(String authname) {
		this.authname = authname;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
