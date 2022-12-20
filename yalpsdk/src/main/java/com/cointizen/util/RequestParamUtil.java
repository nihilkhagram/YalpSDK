package com.cointizen.util;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.http.HttpConstants;
import com.lidroid.xutils.http.ResponseInfo;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.InitModel;
import com.cointizen.paysdk.service.ServiceManager;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.OrderInfoUtils;
import com.cointizen.paysdk.utils.PaykeyUtil;
import com.cointizen.paysdk.utils.TextUtils;
import com.cointizen.paysdk.utils.TimeConvertUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestParamUtil {

	private static final String TAG = "RequestParamUtil";
	public static String IMEI = "";

	public static String getRequestParamString(Map<String, String> paramMap) {
		return getRequestParamString(paramMap, "");
	}

	public static String getRequestParamString(Map<String, String> paramMap, String url) {
		paramMap.put("sdk_version", "1");//1 安卓 2苹果
		String token = UserLoginSession.getInstance().getToken();
		if(!TextUtils.isEmpty(token)){
			paramMap.put("token", token);
		}

		if (!TextUtils.isEmpty(UserLoginSession.getInstance().getUserId())){
			paramMap.put("user_id", UserLoginSession.getInstance().getUserId());
		}

		if (!TextUtils.isEmpty(UserLoginSession.getInstance().getSmallAccountUserID())){
			paramMap.put("small_id", UserLoginSession.getInstance().getSmallAccountUserID());
		}
		if (ServiceManager.getInstance().isBaiDuYunOS) {
			paramMap.put("is_cloud_game", "1");
		}
		paramMap.put("promote_id", StoreSettingsUtil.getStoreId());
		paramMap.put("device_name", android.os.Build.BRAND + ":" +  android.os.Build.MODEL); //手机型号
		paramMap.put("equipment_num", IMEI);
		paramMap.put("game_id", SdkDomain.getInstance().getGameId());
		paramMap.put("game_name", SdkDomain.getInstance().getGameName());
		paramMap.put("is_simulator", InitModel.init().isSimulator() ? "1" : "0");
//		paramMap.put("is_simulator", "0");
		paramMap.put("t", TimeConvertUtils.getNowTime());
		List<Map.Entry<String, String>> mappingList = new ArrayList<>(paramMap.entrySet());
		Collections.sort(mappingList, Comparator.comparing(Map.Entry::getKey));
		JSONObject jsonparam = new JSONObject();
		String values = "";
		try {
			for (Map.Entry<String, String> mapping : mappingList) {
				String key = mapping.getKey().trim();
				String value = mapping.getValue();
				if(mapping.getValue() == null) {
					MCLog.v(TAG, url + ", Key: [" + key + "], value: [" + value + "]");
					continue;
				}
				jsonparam.put(key, value);
				values += value;
			}
			MCLog.v(TAG, url + ", Parameter: [" + values + "], key: [" + OrderInfoUtils.MCH_MD5KEY() + "]");
			String md5key = PaykeyUtil.stringToMD5(values.trim() + OrderInfoUtils.MCH_MD5KEY());
			jsonparam.put("md5_sign", md5key);
			String encode = Base64.encode(jsonparam.toString().getBytes());
			MCLog.v(TAG, url + ", base64:" + encode);
			return encode;
		} catch (JSONException e) {
		}
		return "";
	}

	/**或得返回内容*/
	public static String Result(ResponseInfo<String> responseInfo){
		String result = responseInfo.result.trim().replaceAll(" ", "");
		try {
			MCLog.i(TAG, HttpConstants.Log_VLiIXXQTdV+result);
			result= new String(Base64.decode(result), "utf-8");
		} catch (Exception e1) {
			MCLog.i(TAG,HttpConstants.Log_sMThbIKmRm);
			result = Base64Util.decode(result);
		}finally{
			try {
				JSONObject object=new JSONObject(result);
				MCLog.i(TAG,HttpConstants.Log_VLiIXXQTdV+result+object.optString("msg"));
			} catch (JSONException e) {
				MCLog.i(TAG,HttpConstants.Log_VLiIXXQTdV+result);
			}
		}

		return result;
	}

}
