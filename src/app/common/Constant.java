package app.common;

public class Constant {

	/**session 中key定义*/
	public final static String USERID = "userId"; // 用户编号
	public final static String USERCODE = "userCode"; // 用户账户
	public final static String USERNAME = "userName"; // 用户名称
	public final static String USERORGNAME = "userOrgName"; // 用户机构名
	public final static String USERORGCODE = "userOrgCode"; // 用户机构号
	public final static String USERROLES = "userRoles"; // 用户角色
	public final static String AUTHS = "auths"; // 用户权限
	public final static String LOGINDATE = "loginDate"; // 用户最后登录时间
	public final static String PATSERURL = "(app|sys|monitor|tools|judicial|logManage|archive|archiveResult|metadata|module|data|notice|hbase|creditCard)"; // 权限控制URL入口
	public final static String NOT_CHECK_PERMISSION = ".jsp|.html|.htm|.css|.js|.jpg|.png|.gif";// 以此结尾的URL 不检查权限
	
	/** 定义数据源文件名*/
	public final static String SOURCEID = "jdbc"; 
	/** 定义超级管理员角色代码*/
	public final static String ROLE_ADMIN = "admin";
}
