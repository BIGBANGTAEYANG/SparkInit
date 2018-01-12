package app.common;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 功能描述 : 页面工具类 
 * @author : YH
 * @version:
 */
final public class PageUtil {

	private final static Logger logger = LoggerFactory.getLogger(PageUtil.class);
	/**
	 * 对于AJAX传输的中文进行URL的UTF-8解码
	 * @param str
	 * @return
	 */
	public static String URLDecoder2UTF8(String str) {
		try {
			if (str != null)
				str = URLDecoder.decode(str, "utf-8");

		} catch (Exception ex) {
			logger.error("对于AJAX传输的中文进行URL的UTF-8解码错误！", ex);
			ex.printStackTrace();
		}
		return str;
	}

	/**
	 * 对于AJAX传输的中文进行URL的UTF-8编码
	 * @param str
	 * @return
	 */
	public static String URLEncoder2UTF8(String str) {
		try {
			if (str != null)
				str = URLEncoder.encode(str, "UTF-8");

		} catch (Exception ex) {
			logger.error("对于AJAX传输的中文进行URL的UTF-8编码错误！", ex);
			ex.printStackTrace();
		}
		return str;
	}

	

	/**
	 * 获取访问服务器的IP地址,由于目前较多架构使用了前端服务器来实现负载均衡，<br>
	 * 因此直接使用java方法获取的IP有可能会是前端服务器的IP，提供本方法来实现获取
	 * 用户真实访问的IP地址
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	/**
	 * 将请求中的参数封装到Map集合对象中
	 * @param request
	 * @return
	 */
	public static Map<String,String> getParamMap(HttpServletRequest request) {
		Map<String,String> paramMap = new HashMap<String,String>();
		@SuppressWarnings("rawtypes")
		Enumeration paramNames = request.getParameterNames();
		String key;
		String[] value;
		while (paramNames.hasMoreElements()) {
			key = (String) paramNames.nextElement();
			value = request.getParameterValues(key);
			if (value != null && value.length == 1) {
				if (value[0].length() > 0)
					paramMap.put(key, value[0]);
			} else {
				StringBuffer tmp = new StringBuffer();
				int len = value.length;
				for (int i = 0; i < len; i++) {
					if (i > 0)
						tmp.append(";");
					tmp.append(value[i]);
				}
				paramMap.put(key, tmp.toString());
			}
		}
		paramMap.remove("$r");// 去除随机数信息
		paramMap.remove("$v");// 去除单点登录信息
		return paramMap;
	}
}
