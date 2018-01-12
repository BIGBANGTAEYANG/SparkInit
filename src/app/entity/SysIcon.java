package app.entity;

import spark.annotation.Table;

/**
 * 
 * 功能描述 : icon表
 * @author : YH
 * @version:
 */
@Table(name="SYS_ICON")
public class SysIcon {
	/**图片*/
	private String image;

	/**样式*/
	private String classid;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

}
