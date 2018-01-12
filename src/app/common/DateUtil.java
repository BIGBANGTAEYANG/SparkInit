package app.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：日期处理类
 * @author      freedom.xie    
 * @version     版本：0.5  
 */
public final class DateUtil {
	private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	/** 默认日期格式 */
	public static final String DEF_DATE_FORMAT = "yyyy-MM-dd";
	/** 默认时间格式 */
	public static final String DEF_TIME_FORMAT = "HH:mm:ss";
	/** 默认日期时间格式 */
	public static final String DEF_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 精确到分钟时间格式 */
	public static final String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
	/** 精确到毫秒，无分隔符的时间格式*/
	public static final String MINUTE_FORMAT_NOSPLIT = "yyyyMMddHHmmssSSS";

	/** 默认日期匹配格式 */
	public static final String DEF_DATE_PATTERN = "\\d{4}\\-\\d{2}-\\d{2}";
	/** 默认日期时间匹配格式 */
	public static final String DEF_DATETIME_PATTERN = "\\d{4}\\-\\d{2}-\\d{2}\\p{javaWhitespace}\\d{2}:\\d{2}:\\d{2}";
	
	/**
	 * 构造函数(空)
	 */
	private DateUtil() {}
	
	/**
	 * 取得系统当前时间戳
	 * @return 系统当前时间戳对象
	 */
	public static Timestamp getSysTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 将yyyy-MM-dd格式的字符串转换为日期对象
	 * @param date yyyy-MM-dd格式字符串
	 * @return 转换后的日期对象，无法转换时返回null
	 */
	public static Date getDate(String date) {
		if (!matchesPattern(date, DEF_DATE_PATTERN)) return null;
		return parseDate(date, DEF_DATE_FORMAT);
	}
	
	/**
	 * 将yyyy-MM-dd格式的字符串转换为时间戳对象
	 * @param date yyyy-MM-dd格式字符串
	 * @return 转换后的时间戳对象，无法转换时返回null
	 */
	public static Timestamp getTimestamp(String date) {
		if (!matchesPattern(date, DEF_DATE_PATTERN)) return null;
		return new Timestamp(parseDate(date, DEF_DATE_FORMAT).getTime());
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为日期对象
	 * @param datetime yyyy-MM-dd HH:mm:ss格式字符串
	 * @return 转换后的日期对象，无法转换时返回null
	 */
	public static Date getDateTime(String datetime) {
		if (!matchesPattern(datetime, DEF_DATETIME_PATTERN)) return null;
		return parseDate(datetime, DEF_DATETIME_FORMAT);
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为时间戳期对象
	 * @param datetime yyyy-MM-dd HH:mm:ss格式字符串
	 * @return 转换后的时间戳对象，无法转换时返回null
	 */
	public static Timestamp getDateTimeStamp(String datetime) {
		if (!matchesPattern(datetime, DEF_DATETIME_PATTERN)) return null;
		return new Timestamp(parseDate(datetime, DEF_DATETIME_FORMAT).getTime());		
	}
	
	/**
	 * 将指定格式的字符串对象转换为日期对象
	 * @param date 字符串
	 * @param pattern 指定的格式
	 * @return 转换后的日期，无法转换时返回null
	 */
	public static Date getDate(String date, String pattern) {
		return getDate(date, pattern, null);
	}
	
	/**
	 * 将指定格式的字符串对象转换为日期对象
	 * @param date 字符串
	 * @param pattern 指定的格式
	 * @param defVal 默认返回值
	 * @return 转换后的日期，无法转换时返回defVal指定值
	 */
	public static Date getDate(String date, String pattern, Date defVal) {
		if (date == null || pattern == null) return null;
		Date ret = parseDate(date, pattern);
		return (ret == null) ? defVal : ret;
	}
	
	/**
	 * 根据指定的格式格式将传入字符串转化为日期对象
	 * @param date 传入字符串
	 * @param format 指定格式
	 * @return 格式化后日期对象
	 */
	private static Date parseDate(String date, String format) {
		Date d;
		try {
			d = new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			logger.error("根据指定的格式格式将传入字符串转化为日期对象错误！", e);
			d = null;
		}
		return d;
	}
	
	/**
	 * 检测输入字符串是否与指定格式匹配
	 * @param input 待检测字符串
	 * @param pattern 检测格式
	 * @return
	 * <li>true：匹配</li>
	 * <li>false：不匹配</li>
	 */
	private static boolean matchesPattern(String input, String pattern) {
		return (input != null) && (input.matches(pattern));
	}
	
	/**
	 * 将日期对象格式化成yyyy-mm-dd类型的字符串
	 * @param date 日期对象
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formatDate(Date date) {
		return formatDateToString(date, DEF_DATE_FORMAT);
	}
	
	/**
	 * 将日期对象格式化成HH:mm:ss类型的字符串
	 * @param date 日期对象
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formatTime(Date date) {
		return formatDateToString(date, DEF_TIME_FORMAT);
	}
	
	/**
	 * 将日期对象格式化成yyyy-MM-dd HH:mm:ss类型的字符串
	 * @param date 日期对象
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formatDateTime(Date date) {
		return formatDateToString(date, DEF_DATETIME_FORMAT);
	}
	
	/**
	 * 将日期对象格式化成yyyy-MM-dd HH:mm类型的字符串
	 * @param date 日期对象
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formateMinuteDate(Date date) {
		return formatDateToString(date, MINUTE_FORMAT);
	}
	
	/**
	 * 将日期对象格式化成指定的格式字符串
	 * @param date 日期对象
	 * @param format 格式
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	private static String formatDateToString(Date date, String format) {
		if (date == null) return null;
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 将日期对象格式化成指定格式的字符串
	 * @param date 日期对象
	 * @param format 格式
	 * @return 格式化后的字符串，无法格式化时，返回null
	 */
	public static String formatDate(Date date, String format) {
		if (date == null || format == null) return null;
		String ret;		
		try {
			ret = new SimpleDateFormat(format).format(date);
		} catch (RuntimeException e) {
			logger.error("将日期对象格式化成指定格式的字符串错误！", e);
			ret = null;
		}
		return ret;
	}
	
	/**
	 * 将毫秒级日期格式化成指定的字符串
	 * @param time
	 * @param format
	 * @return
	 */
	public static String formatTime(long time, String format) {
		if (time <= 0 || format == null) return null;
		String ret;		
		try {
			ret = new SimpleDateFormat(format).format(time);
		} catch (RuntimeException e) {
			logger.error("将毫秒级日期格式化成指定的字符串错误！", e);
			ret = null;
		}
		return ret;
	}
	
	/**
	 * 取得指定日期所在月的最后一天日期对象
	 * @param d 指定日期
	 * @return 指定日期当月的最后一天日期对象，如指定日期为null时，返回当前月的最后一天日期对象
	 */
	public static Date getLastDayObjectInMonth(Date d) {
		Calendar cal = Calendar.getInstance();
		if (d != null) cal.setTime(d);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
	
	/**
	 * 取得指定日期所在月的最后一天日期值
	 * @param d 指定日期
	 * @return 当月的最后一天日期值，如指定日期为null时，返回当前月的最后一天日期值
	 * @see #getLastDayObjectInMonth(Date)
	 */
	public static int getLastDayInMonth(Date d) {
		Date date = getLastDayObjectInMonth(d);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	
	/**
	 * 返回给定的两个时间之间的差值
	 * @param bgDate
	 * @param edDate
	 * @return 以分为单位返回
	 */
	public static long getTimeInterval(long bgDate,long edDate) {
		return (edDate - bgDate)/1000/60;
	}
	
	/**
	 * 返回给定的两个时间的差值
	 * @param bgDate 
	 * @param edData 
	 * @param formart 日期的格式化类型
	 * @return
	 */
	public static long getTimeInterval(String bgDate, String edData, String formart) {
		return getDate(edData, formart).getTime() - getDate(bgDate, formart).getTime();
	}
	
	/**
	 * 返回指定时间段日期的毫秒表示形式
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @return
	 */
	public static long getSpecifyDate(int hourOfDay,int minute,int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 
				hourOfDay, 
				minute, 
				second);
		return cal.getTimeInMillis();
	}
	
	/**
	 * 将字符串转换成long型
	 * @param time
	 * @param format
	 * @return
	 */
	public static long getLongTime(String time,String format) throws ParseException {
		return new SimpleDateFormat(format).parse(time).getTime();
	}
	
	/**
	 * 比较日期大小
	 * @param d1
	 * @param d2
	 * @return true：d1大于d2<br>false：d1小于等于d2
	 */
	public static boolean compareDate(Date d1, Date d2) {
		return d1.compareTo(d2) > 0;
	}
	
	/**
	 * 与系统日期比较大小
	 * @param date
	 * @param formart 指定日期格式
	 * @return true：大于当前系统日期<br>false：小于等于系统日期
	 */
	public static boolean compareDate(String date, String formart) throws Exception {
		DateFormat df = new SimpleDateFormat(formart);
		Date d = df.parse(date);
		return compareDate(d, new Date());
	}
	
	public static void main(String[] ages) throws Exception{
		String t = formatTime(System.currentTimeMillis(), MINUTE_FORMAT_NOSPLIT);
		System.out.println(t);
		
		String DateStr1 = "2014-08-27";
		String DateStr2 = "2014-08-27";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime1 = dateFormat1.parse(DateStr1);
		Date dateTime2 = dateFormat1.parse(DateStr2);
		boolean c = compareDate(dateTime1, dateTime2);
		System.out.println(c);
		
		c = compareDate("2017-07-26","yyyy-MM-dd");
		System.out.println(c);
	}
}
