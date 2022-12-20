package com.cointizen.paysdk.http;

import com.lidroid.xutils.http.ResponseInfo;
import com.cointizen.util.Base64;
import com.cointizen.paysdk.utils.MCLog;

/**解密返回参数，打印返回参数*/
public class RequestUtil {

	private static final String TAG_HTTP = "RequestUtil";

	public static String getResponse(final ResponseInfo<String> responseInfo) {
		return getResponseAndUrl(responseInfo, "");
	}

	public static String getResponseAndUrl(final ResponseInfo<String> responseInfo, String url) {
		String response = "";
		try {
			byte[] decode = Base64.decode(responseInfo.result.trim());
			response = new String(decode, "utf-8");
		} catch (Exception e) {
			MCLog.e(TAG_HTTP, "decode:" + e);
			response = "";
		}
		MCLog.v(TAG_HTTP, url + ", response:" + response);
		return response;
	}
}
