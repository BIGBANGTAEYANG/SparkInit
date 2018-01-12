package app.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spark.page.easyui.Page;

/**
 * 
 * 功能描述 : 返回信息类
 * @author : YH
 * @version:
 */
public class InfoBean {
	/**成功的状态标示位*/
	public static final String SUCCESS = "1";
	/**失败的状态标示位*/
	public static final String FAILURE = "2";
	/**警告的状态标示位*/
	public static final String WARN = "3";	
	/**代码值*/
	private String code = SUCCESS;
	/**返回消息内容*/
	private String message;
	/**分页查询结果*/
	private Page page;
	/**返回的分页信息*/
	private Map<String , String> pageInfo;
	private String fileName;
	private List<String> columns = new ArrayList<>();
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, String> getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(Map<String, String> pageInfo) {
		this.pageInfo = pageInfo;
	}

	public InfoBean(){}
	public InfoBean(String code, String message) {
		this.code = code;
		this.message = message;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
}
