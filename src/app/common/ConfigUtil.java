package app.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javolution.util.FastMap;

public class ConfigUtil {

	private final static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	private static Monitor monitor;
	private static YKCPBean ykcpBean;
	private static ScheduleBean scheduleBean;
	
	private static final ConfigUtil instance = new ConfigUtil();
	private ConfigUtil(){}
	
	public static ConfigUtil getInstance() {
		return instance;
	}
	
	public Monitor getMonitor() throws Exception {
		if (monitor == null) {
			initMonitor();
		}
		return monitor;
	}
	public YKCPBean getYKCPBean() throws Exception {
		if (ykcpBean == null) {
			initYKCP();
		}
		return ykcpBean;
	}
	
	public ScheduleBean getScheculeBean() throws Exception {
		if (scheduleBean == null) {
			initSchedule();
		}
		return scheduleBean;
	}
	
	private void initMonitor() {
		if (monitor == null) {
			Properties props = new Properties();
			try {
				props.load(new InputStreamReader(new FileInputStream(getConfigFile("monitor.properties")), "UTF-8"));
				monitor = new Monitor();
				monitor.ESBappName = props.getProperty("ESBappName");
				monitor.ESBappPath = props.getProperty("ESBappPath");
				monitor.ESBappSh = props.getProperty("ESBappSh");
				monitor.WSappName = props.getProperty("WSappName");
				monitor.WSappPath = props.getProperty("WSappPath");
				monitor.WSappSh = props.getProperty("WSappSh");
			} catch (IOException e) {
				//e.printStackTrace();
				logger.error("获取监控配置参数失败！", e);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("获取监控配置参数失败！", e);
			}
		}
	}
	
	private void initYKCP() {
		if (ykcpBean == null) {
			Properties props = new Properties();
			try {
				props.load(new InputStreamReader(new FileInputStream(getConfigFile("ykcp.properties")), "UTF-8"));
				ykcpBean = new YKCPBean();
				ykcpBean.PLATFORM_ADDR="";
				if (StringUtil.isNotEmpty(props.getProperty("PLATFORM_ADDR"))) {
					ykcpBean.PLATFORM_ADDR = props.getProperty("PLATFORM_ADDR");
				}
				if (StringUtil.isNotEmpty(props.getProperty("AUTHS"))) {
					String[] aryAuths = props.getProperty("AUTHS").split("\\;");
					for (String auth : aryAuths)
						ykcpBean.AUTHS.put(auth, Integer.valueOf(0));
				}
			} catch (IOException e) {
				//e.printStackTrace();
				logger.error("获取平台配置参数失败！", e);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("获取平台配置参数失败！", e);
			}
		}
	}
	
	private void initSchedule() {
		if (scheduleBean == null) {
			Properties props = new Properties();
			try {
				props.load(new InputStreamReader(new FileInputStream(getConfigFile("schedule.properties")), "UTF-8"));
				scheduleBean = new ScheduleBean();
				scheduleBean.JDBC_DRIVER="com.mysql.jdbc.Driver";
				if (StringUtil.isNotEmpty(props.getProperty("JDBC_DRIVER"))) {
					scheduleBean.JDBC_DRIVER = props.getProperty("JDBC_DRIVER");
				}
				scheduleBean.JDBC_URL="jdbc:mysql://appserver9434.chinacloudapp.cn:3306/schedule?useUnicode=true&characterEncoding=utf-8";
				if (StringUtil.isNotEmpty(props.getProperty("JDBC_URL"))) {
					scheduleBean.JDBC_URL = props.getProperty("JDBC_URL");
				}
				scheduleBean.JDBC_USER="root";
				if (StringUtil.isNotEmpty(props.getProperty("JDBC_USER"))) {
					scheduleBean.JDBC_USER = props.getProperty("JDBC_USER");
				}
				scheduleBean.JDBC_PWD="eWtAMTIzNDU=";
				if (StringUtil.isNotEmpty(props.getProperty("JDBC_PWD"))) {
					scheduleBean.JDBC_PWD = props.getProperty("JDBC_PWD");
				}
			} catch (IOException e) {
				//e.printStackTrace();
				logger.error("获取调度配置参数失败！", e);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("获取调度配置参数失败！", e);
			}
		}
	}
	
	/**
	 * 加载配置文件
	 * 
	 * @param fileName
	 * @return
	 */
	public File getConfigFile(String fileName) throws Exception {
		File loadFile = new File("conf" + File.separator + fileName);
		if (!loadFile.exists()) {
			loadFile = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath() + File.separator
					+ fileName);
		}
		if (!loadFile.exists()) {
			loadFile = new File("src" + File.separator + "main" + File.separator + "conf" + File.separator + fileName);
		}
		return loadFile;
	}
	
	/**
	 * 获取脚本文件
	 * 
	 * @param fileName
	 * @return
	 */
	public File getScriptFile(String fileName) throws Exception {
		File loadFile = new File("script" + File.separator + fileName);
		if (!loadFile.exists()) {
			loadFile = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath() + File.separator
					+ fileName);
		}
//		if (!loadFile.exists()) {
//			loadFile = new File("src" + File.separator + "main" + File.separator + "conf" + File.separator + fileName);
//		}
		return loadFile;
	}
	
	public class Monitor {
		public String ESBappName;
		public String ESBappPath;
		public String ESBappSh;
		public String WSappName;
		public String WSappPath;
		public String WSappSh;
	}
	
	public class YKCPBean {
		public String PLATFORM_ADDR;
		public Map<String, Integer> AUTHS = new FastMap();
	}
	
	public class ScheduleBean{
		public String JDBC_DRIVER;
		public String JDBC_URL;
		public String JDBC_USER;
		public String JDBC_PWD;
	}
	
	public static void main(String[] ages) throws Exception{
		System.out.println(ConfigUtil.getInstance().getScheculeBean().JDBC_DRIVER);
	}
}
