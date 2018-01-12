package app.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：MD5及SHA加密
 * @author freedom.xie
 * @version 版本：0.5
 */
final public class MD5 {
	private final static Logger logger = LoggerFactory.getLogger(MD5.class);
    //使用MD5加密
	public synchronized static final String toMd5(String data) {
		try {			
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(data.getBytes());
			return encodeHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error("MD5加密错误！", e);
			//e.printStackTrace();
			return null;
		}
	}

	// 使用SHA加密
	public synchronized static final String toSHA(String data) {
		try {			
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(data.getBytes());
			return encodeHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error("SHA加密错误！", e);
//			e.printStackTrace();
			return null;
		}
	}

	// 补位操作
	public static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;
		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

}
