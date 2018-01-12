package app.entity;

import spark.annotation.Table;

/**
 * 前台操作日志
 * @author dev031681
 *
 */
@Table(name="SYS_AUDIT_LOG")
public class AuditLog {

	/**日志序号，自增长*/
	private int logId;
	
	/**操作人工号*/
	private String empNo;
	
	/**操作类型:"删除"，"修改"，"查看"，"新增"*/
	private String auditType;
	
	/**操作模块*/
	private String auditModule;
	
	/**操作的表*/
	private String auditTable;
	
	/**操作状态："1"表示成功；"2"表示失败*/
	private int state;
	
	/**操作时间*/
	private long logTime;

	/**详细描述*/
	private String des;
	
	/**附件地址*/
	private String attachment;
	

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getAuditModule() {
		return auditModule;
	}

	public void setAuditModule(String auditModule) {
		this.auditModule = auditModule;
	}

	public String getAuditTable() {
		return auditTable;
	}

	public void setAuditTable(String auditTable) {
		this.auditTable = auditTable;
	}

	public long getLogTime() {
		return logTime;
	}

	public void setLogTime(long logTime) {
		this.logTime = logTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	
	
}
