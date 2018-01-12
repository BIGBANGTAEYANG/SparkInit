package app.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 功能描述 : URL解析器.使用表达式
 * @author : YH
 * @version:
 */
public class UrlParser {
	/** 匹配URL访问的页 */
	//private static final String URL_PATTERN_PAGE = "/?[a-zA-Z]*/[a-zA-Z]*";
	//  /?(app|risk)/[a-zA-Z]*;
	private static String URL_PATTERN_PAGE = "/?#/[a-zA-Z]*";
	/** 匹配URL访问的方法 */
	private static String URL_PATTERN_FUN = "view|delete|modify";	
	
	/**
	 * 抽取当前访问的页
	 * @param url 访问路径
	 * @param params 正则表达式匹配参数
	 * @return 匹配的字符
	 */
	public static String parserUrlPage(String url,String params) {
		url = url.replace("/app", "");
		Pattern p = Pattern.compile(URL_PATTERN_PAGE.replaceFirst("#", params));
        Matcher m = p.matcher(url);
        if (m.find()){        	
        	return m.group();
        }
        return url.replaceFirst("/", "");
			
	}
	
	/**
	 * 抽取当前访问的页的方法
	 * @param url 访问路径
	 * @param pattern 正则表达式匹配范式
	 * @return 匹配的字符
	 */
	public static String parserUrlAuth(String url,String pattern) {
		Pattern p = Pattern.compile(pattern == null ? URL_PATTERN_FUN : pattern);
        Matcher m = p.matcher(url);
        if (m.find()){        	
        	return m.group();
        }
        return null;
     }
	
	public static void main(String[]args) {
		System.out.println(parserUrlPage("/app/sys/menu/view",Constant.PATSERURL));
		//System.out.println(URL_PATTERN_PAGE.replaceFirst("#", "(app|risk)"));
	}
}
