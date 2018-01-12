package app.security;

/**
 * 功能描述：Base64编解码方法
 * 
 * @author freedom.xie
 * @version 版本：0.5
 */
final public class Base64 {

	private Base64() {
	}

	/**
	 * RFC2045里面的Base64字母表
	 */
	private static final char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', // 0 - 7
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 8 - 15
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 16 - 24
			'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 25 - 31
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 32 - 39
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 40 - 47
			'w', 'x', 'y', 'z', '0', '1', '2', '3', // 48 - 55
			'4', '5', '6', '7', '8', '9', '+', '/' // 56 - 63
	};

	/**
	 * 反向的查找表
	 */
	private static final byte BD = -1; // bad char
	private static final byte WS = -2; // white spaces
	private static final byte PD = -3; // tail pad
	private static final byte[] inverseAlphabet = { BD, BD, BD, BD, BD,
			BD,
			BD,
			BD, // 0 - 7
			-1, WS, WS, BD, BD, WS,
			BD,
			BD, // 8 - 0x0f ?,TAB,LF,?,?,CR,?,?,
			BD, BD, BD, BD, BD, BD,
			BD,
			BD, // 0x10 - 0x17
			BD, BD, BD, BD, BD, BD,
			BD,
			BD, // 0x18 - 0x1f
			WS, BD, BD, BD, BD, BD,
			BD,
			BD, // 0x20 - 0x27 SPACE,!,",#,$,%,&,'
			BD, BD, BD, 62, BD, BD,
			BD,
			63, // 0x28 - 0x2f (,),*,+,,,-,.,/
			52, 53, 54, 55, 56, 57,
			58,
			59, // 0x30 - 0x37 0,1,2,3,4,5,6,7
			60, 61, BD, BD, BD, PD,
			BD,
			BD, // 0x38 - 0x3f 8,9,:,;,<,=,>,?
			BD, 0, 1, 2, 3, 4,
			5,
			6, // 0x40 - 0x47 @,A -
			7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, BD, BD, BD, BD,
			BD, // 0x58 - 0x5f - Z
			BD, 26, 27, 28, 29, 30, 31,
			32, // 0x60 - 0x67 `,a -
			33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
			50, 51, // 0x77 - - Z
	};

	/**
	 * 将一个byte数组翻译成Base64字符串
	 */
	public static String encode(byte[] a) {
		int len = a.length;
		char[] result = new char[4 * ((len + 2) / 3) + len / 57 + 1];
		int sourcePos = 0;
		int resultPos = 0;
		int linePos = 0;

		// 将整3个字节翻译成4个字符一组
		while (sourcePos < len - 2) {
			int byte0 = a[sourcePos++] & 0xff;
			int byte1 = a[sourcePos++] & 0xff;
			int byte2 = a[sourcePos++] & 0xff;
			result[resultPos++] = alphabet[byte0 >> 2];
			result[resultPos++] = alphabet[(byte0 << 4) & 0x3f | (byte1 >> 4)];
			result[resultPos++] = alphabet[(byte1 << 2) & 0x3f | (byte2 >> 6)];
			result[resultPos++] = alphabet[byte2 & 0x3f];
			linePos += 4;
			if (linePos == 76) {
				result[resultPos++] = '\n';
				linePos = 0;
			}
		}

		// 翻译最后的零头
		int tailBytes = a.length - sourcePos;
		if (tailBytes > 0) {
			int byte0 = a[sourcePos++] & 0xff;
			result[resultPos++] = alphabet[byte0 >> 2];
			if (tailBytes == 1) {
				result[resultPos++] = alphabet[(byte0 << 4) & 0x3f];
				result[resultPos++] = '=';
				result[resultPos++] = '=';
			} else {
				// assert numBytesInPartialGroup == 2;
				int byte1 = a[sourcePos++] & 0xff;
				result[resultPos++] = alphabet[(byte0 << 4) & 0x3f
						| (byte1 >> 4)];
				result[resultPos++] = alphabet[(byte1 << 2) & 0x3f];
				result[resultPos++] = '=';
			}
		}
		return String.valueOf(result, 0, resultPos);
	}

	/**
	 * 将一个base64字符串翻译为byte数组。 如果不是一个合法的base64字符串，抛出IllegalArgumentException
	 */
	public static byte[] decode(String s) {
		byte[] tempBytes = new byte[s.length()];
		int sLen = 0;
		int tailPos = -1;
		for (int i = 0; i < tempBytes.length; i++) {
			char c = s.charAt(i);
			byte n = c < inverseAlphabet.length ? inverseAlphabet[c] : BD;
			switch (n) {
			case BD: // bad base64 char
				throw new IllegalArgumentException("Invalid Base64 char " + c
						+ " at " + i);
			case WS: // nothing to do
				break;
			case PD:
				if (tailPos == -1) {
					tailPos = sLen;
				}
				sLen++;
				break;
			default:
				if (tailPos != -1) {
					throw new IllegalArgumentException("Extra chars after PAD");
				}
				tempBytes[sLen++] = n;
				break;
			}
		}

		if (sLen % 4 != 0) {
			throw new IllegalArgumentException("Invalid Base64 String length");
		}
		if (tailPos == -1) {
			tailPos = sLen;
		}
		int numPads = sLen - tailPos;
		byte[] result = new byte[3 * sLen / 4 - numPads];
		int resultPos = 0;
		int sourcePos = 0;
		while (sourcePos < tailPos - 3) {
			int ch0 = tempBytes[sourcePos++];
			int ch1 = tempBytes[sourcePos++];
			int ch2 = tempBytes[sourcePos++];
			int ch3 = tempBytes[sourcePos++];
			result[resultPos++] = (byte) ((ch0 << 2) | (ch1 >> 4));
			result[resultPos++] = (byte) ((ch1 << 4) | (ch2 >> 2));
			result[resultPos++] = (byte) ((ch2 << 6) | ch3);
		}

		if (numPads > 0) {
			int ch0 = tempBytes[sourcePos++];
			int ch1 = tempBytes[sourcePos++];
			result[resultPos++] = (byte) ((ch0 << 2) | (ch1 >> 4));
			if (numPads == 1) {
				int ch2 = tempBytes[sourcePos++];
				result[resultPos++] = (byte) ((ch1 << 4) | (ch2 >> 2));
			}
		}
		return result;
	}

	static public char[] encode2(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;
			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet2[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet2[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet2[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet2[val & 0x3F];
		}
		return out;
	}

	static public byte[] decode2(char[] data) {
		int len = ((data.length + 3) / 4) * 3;
		if (data.length > 0 && data[data.length - 1] == '=')
			--len;
		if (data.length > 1 && data[data.length - 2] == '=')
			--len;
		byte[] out = new byte[len];
		int shift = 0;
		int accum = 0;
		int index = 0;
		for (int ix = 0; ix < data.length; ix++) {
			int value = codes[data[ix] & 0xFF];
			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) ((accum >> shift) & 0xff);
				}
			}
		}
		if (index != out.length)
			throw new Error("miscalculated data length!");
		return out;
	}

	static private char[] alphabet2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();
	static private byte[] codes = new byte[256];
	static {
		for (int i = 0; i < 256; i++)
			codes[i] = -1;
		for (int i = 'A'; i <= 'Z'; i++)
			codes[i] = (byte) (i - 'A');
		for (int i = 'a'; i <= 'z'; i++)
			codes[i] = (byte) (26 + i - 'a');
		for (int i = '0'; i <= '9'; i++)
			codes[i] = (byte) (52 + i - '0');
		codes['+'] = 62;
		codes['/'] = 63;
	}
}
