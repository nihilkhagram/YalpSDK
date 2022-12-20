package com.mchsdk.paysdk.jni;

import com.cointizen.paysdk.utils.MCLog;

public class MCHKeyTools {
	private static final String TAG = "MCHKeyTools";
	private static MCHKeyTools mt = null;
	static {
		try {
			System.loadLibrary("mchpaysdk");
		} catch (Exception e) {
			MCLog.e(TAG, JniConstants.Log_wTMTfcxcBp);
			e.printStackTrace();
		}
	}
	private MCHKeyTools() {
		natInit();
	}

	public static MCHKeyTools getInstance() {
		if (null == mt) {
			mt = new MCHKeyTools();
		}
		return mt;
	}
	/** 初始化,设置默认的对称加密的key gnauhcgnem */
	public native void natInit();

	/** 将加密之后的字符串进行解密 **/
	public native String secToNor(String secStr);

	public native String getHost();
}
