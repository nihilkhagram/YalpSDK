package com.cointizen.paysdk.channel.sign1;

import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.channel.ChannelConstants;

public class FileUtil {

	private final String TAG = "FileUtil";

	private String channelAndChannemStr;

	public FileUtil() {
		channelAndChannemStr = getStr();
//		channelAndChannemStr = "{\"game_id\":\"58\",\"game_name\":\"\u96ea\u5200\u7fa4\u4fa0\u4f20(\u5b89\u5353\u7248)\",\"game_appid\":\"E7EF0AB66B7FEDBFF\",\"promote_id\":\"0\",\"promote_account\":\"\u7cfb\u7edf\"}";
	}

	public String getPromoteId() {
		String pId = getValueByKey("promote_id");
		if (TextUtils.isEmpty(pId)) {
			pId = "0";
		}
		return pId;
	}

	public String getPromoteAccount() {
		String pName = getValueByKey("promote_account");
		if (TextUtils.isEmpty(pName)) {
			pName = ChannelConstants.S_jLmcEMBYXn;
		}
		return pName;
	}

	private String getValueByKey(String key){
		if(TextUtils.isEmpty(channelAndChannemStr)){
			return "";
		}
		String valueStr = "";
		try {
			JSONObject js = new JSONObject(channelAndChannemStr);
			valueStr = js.getString(key);
			MCLog.w(TAG, key + ":" + valueStr);
		} catch (JSONException e) {
			valueStr = "";
		}
		return valueStr;
	}

	/**
	 * 返回文件中字符串
	 */
	private String getStr() {
		InputStream is = null;
		BufferedReader reader = null;
		String result = "";
		StringBuilder sb = new StringBuilder();
		String line = "";
		try {
			String ccfpath = "/META-INF/mch.properties";
			is = this.getClass().getResourceAsStream(ccfpath);
			reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			result = sb.toString();
		} catch (Exception e) {
			result = "";
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}
		return result;
	}
}
