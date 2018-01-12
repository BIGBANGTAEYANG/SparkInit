package app.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import app.entity.AuditLog;
import javolution.util.FastTable;

/**
 * 操作日志存取的工具
 * 
 * @author DEV031681
 *
 */
public class HodsLogUtil {

	private HodsLogUtil() {
	}

	private static HodsLogUtil instance = new HodsLogUtil();

	// 生成log4j对象
	private static Logger Log = LoggerFactory.getLogger(HodsLogUtil.class);

	// 获取对象
	public static HodsLogUtil getInstance() {
		return instance;
	}

	// 定义全局的日志对象集合
	private FastTable<AuditLog> auditLogList = new FastTable<AuditLog>().shared();

	/**
	 * 新增日志信息
	 * 
	 * @param userCode
	 *            : 操作员工编号
	 * @param auditType
	 *            ：操作类型 :"删除"，"修改"，"查看"，"新增"
	 * @param auditModule
	 *            ： 操作的模块
	 * @param auditTable
	 *            ：操作的表
	 * @param state
	 *            ： 操作的状态 ："1"表示成功；"2"表示失败
	 * @param logTime
	 *            ： 操作时间
	 * @param des
	 *            ： 日志详情
	 * @param attachment
	 *            ：附件图片的地址（用于工单附件），这个字段只需要存图片的名称即可。
	 * @return
	 */
	public boolean AddAuditLog(String userCode, String auditType, String auditModule, String auditTable, LogState state,
			long logTime, String des, String attachment) {
		boolean result = false;
		// 创建日志对象
		AuditLog alog = new AuditLog();
		// 给对象赋值
		alog.setEmpNo(userCode);
		alog.setAuditType(auditType);
		alog.setAuditModule(auditModule);
		alog.setAuditTable(auditTable);
		alog.setState(state.getState());
		alog.setLogTime(logTime);
		if (StringUtil.isNotEmpty(des) && des.length() > 2000) {
			des = des.substring(0, 1999);
		}
		alog.setDes(des);
		alog.setAttachment(attachment);
		// 判断内存中的日志数量是否超值，超值则删除第一条记录，并存入新的日志数据
		/*synchronized (auditLogList) {
			if (auditLogList.size() >= 1000) {
				auditLogList.remove(0);
				auditLogList.add(alog);
				result = true;
			} else {
				auditLogList.add(alog);
				result = true;
				// Log.debug("userCode=="+alog.getEmpNo()+",,,table=="+alog.getAuditTable()+",,,time=="+alog.getLogTime());
			}
		}*/
		// 如果队列中数量大于1000条，则移除第一条 modify by yyh 2016-02-25
		if (auditLogList.size() >= 1000) {
			auditLogList.remove(0);
		}
		auditLogList.add(alog);
		result = true;
		return result;
	}

	// 获取内存中的日志对象集合
	public FastTable<AuditLog> getAuditLog() {
		Log.debug("获取日志list数量:" + auditLogList.size());
		return auditLogList;
	}

}
