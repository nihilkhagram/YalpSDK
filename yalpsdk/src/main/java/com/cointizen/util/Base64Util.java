package com.cointizen.util;

import com.cointizen.paysdk.utils.MCLog;

public class Base64Util {

	private static final String TAG = "Base64Util";

	/**
	 * Base64编码
	 * 
	 * @param str 要编码的字符串
	 * @return 编码完成的字符串码
	 */
	public static String encode(String str) {
		byte byteArr[] = android.util.Base64.encode(str.getBytes(),
				android.util.Base64.DEFAULT);
		String res = "";
		try {
			res = new String(byteArr);
		}catch (UnsupportedOperationException e){
			MCLog.e(TAG, "decode:" + e);
			res = "";
		}

		return res;
	}

	/**
	 * Base64解码
	 * 
	 * @param encodeStr 被编码的字符串
	 * @return 解码完成，输出原本字符串
	 */
	public static String decode(String encodeStr) {
		byte byteArr[] = android.util.Base64.decode(encodeStr,
				android.util.Base64.DEFAULT);

		String res = "";
		try {
			res = new String(byteArr);
		}catch (UnsupportedOperationException e){
			MCLog.e(TAG, "decode:" + e);
			res = "";
		}

		return res;
	}
}
