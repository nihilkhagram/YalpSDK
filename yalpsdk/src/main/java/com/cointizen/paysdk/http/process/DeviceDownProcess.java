package com.cointizen.paysdk.http.process;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.cointizen.util.StoreSettingsUtil;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.DeviceDownRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import com.cointizen.paysdk.http.HttpConstants;

/**
 * 设备下线
 */
public class DeviceDownProcess {

	private static final String TAG = "DeviceDownProcess";
	private final Context context;

	public DeviceDownProcess(final Context context) {
		this.context = context;
	}

	public void post() {

		if(!Constant.deviceIsOnLine){
			MCLog.w(TAG,HttpConstants.Log_WfrkcmTmKm);
			return;
		}

		if (TextUtils.isEmpty(RequestParamUtil.IMEI)){
			String AndroidID = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			if (!TextUtils.isEmpty(AndroidID)){
				RequestParamUtil.IMEI = AndroidID;
				sendMsg();
			}
		}else {
			sendMsg();
		}

	}


	private void sendMsg(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("sdk_version", "1");//表示android发送的请求，固定值1
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("promote_id", StoreSettingsUtil.getStoreId());
		String param = RequestParamUtil.getRequestParamString(map);
		if (TextUtils.isEmpty(param)) {
			MCLog.e(TAG, "fun#post param is null");
			return;
		}
		RequestParams params = new RequestParams();
		MCLog.w(TAG, "fun#post postSign:" + map.toString());
		try {
			params.setBodyEntity(new StringEntity(param.toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != params) {
			DeviceDownRequest loginRequest = new DeviceDownRequest();
			loginRequest.post(MCHConstant.getInstance().getDeviceDownUrl(), params,context);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
