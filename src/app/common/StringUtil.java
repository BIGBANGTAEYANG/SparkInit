package app.common;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：字符串工具类
 * @author      freedom.xie    
 * @version     版本：0.5
 */
public final class StringUtil {	
	private final static Logger logger = LoggerFactory.getLogger(StringUtil.class);
	/**
	 * 构造函数(空)
	 */
	private StringUtil() {}
	
	/**
	 * 检测字符串是否为空字符串
	 * @param input 待检测字符串
	 * @return
	 * <li>true：字符串为空</li>
	 * <li>false：字符串不为空</li>
	 */
	public static boolean isEmpty(String input) {
		if (input == null) return true;
		return (input.trim().length() == 0);
	}
	
	/**
	 * 检测字符串是否不为空字符串
	 * @param input 待检测字符串
	 * @return
	 * <li>true：字符串不为空</li>
	 * <li>false：字符串不空</li>
	 */
	public static boolean isNotEmpty(String input) {
		if (input != null && (input.trim().length() > 0)) return true;
		return false;
	}
	
	/**
	 * 将对象转换为字符串对象
	 * @param input 待取得对象
	 * @return 取得的字符串对象
	 * @see #getString(Object, String)
	 */
	public static String getString(Object input) {
		return getString(input, "");
	}
	
	/**
	 * 将对象转换为字符串对象
	 * @param input 待取得对象
	 * @param defVal 对象为空时的默认返回值
	 * @return 取得的字符串对象
	 */
	public static String getString(Object input, String defVal) {
		if (input == null) return defVal;
		String str = input.toString();
		return (str == null) ? defVal : str.trim();
	}
	
	/**
	 * 替换字符串中的指定字符
	 * @param input 待替换字符串
	 * @param oldChar 待替换字符
	 * @param newChar 替换后字符
	 * @return
	 * <li>待替换字符串为null或长度为零时，返回待替换字符串</li>
	 * <li>其它，返回替换后结果</li>
	 */
	public static String replaceAll(String input, char oldChar, char newChar) {
		if (input == null) return input;
		int len = input.length();
		if (input.length() == 0) return input;
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char ch = input.charAt(i);
			if (ch == oldChar) ch = newChar;
			sb.append(ch);
		}
		return sb.toString();
	}
	
	/**
	 * 对请求的url作Encode操作
	 * @param url  访问的url地址
	 * @param code 编码格式
	 * @return 取得的字符串对象
	 */	
	public static String urlEncode(String url,String code) {
		try {
			return URLEncoder.encode(url, code == null ? "gbk" : code);
		} catch (Exception ex) {
			logger.error("对请求的url作Encode操作为[gbk]错误！", ex);
			return url;
		}
	}
	
	/**
	 * 对请求的url作Decode操作
	 * @param url  访问的url地址
	 * @param code 编码格式
	 * @return 取得的字符串对象
	 */	
	public static String urlDecode(String url,String code) {
		try {
			return URLDecoder.decode(url, code == null ? "gbk" : code);
		} catch (Exception ex) {
			logger.error("对请求的url作Decode操作为[gbk]错误！", ex);
			return url;
		}
	}

}
