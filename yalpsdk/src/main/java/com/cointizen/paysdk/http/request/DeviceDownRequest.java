package com.cointizen.paysdk.http.request;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.ConfigureApp;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

/**
 * 设备下线
 */
public class DeviceDownRequest {

	private static final String TAG = "DeviceDownRequest";

	HttpUtils http;

	public DeviceDownRequest() {
		http = new HttpUtils();

	}

	public void post(String url, RequestParams params, final Context context) {
		if(!ConfigureApp.getInstance().Configure(context)){
			return;
		}
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null add params is null");
			noticeResult(Constant.LOGIN_FAIL, HttpConstants.S_VKHKJPHQds+params+"==========="+url);
			return;
		}

		MCLog.w(TAG, "fun#post url = " + url);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Constant.deviceIsOnLine = false;
				String response=RequestUtil.getResponse( responseInfo);
				try {
					JSONObject obj = new JSONObject(response);
					int status = obj.getInt("code");
					if (status == 200) {
						MCLog.w(TAG, HttpConstants.Log_wFrnbwUGkv);
					} else {
						MCLog.e(TAG, HttpConstants.Log_sklSWrxbbK);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					MCLog.e(TAG, HttpConstants.S_bcwiMnfyeK);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MCLog.e(TAG, "onFailure" + msg);
				MCLog.e(TAG, "onFailure" + error.getExceptionCode()+error.getStackTrace()+error.getMessage());
			}
		});
	}

	private void noticeResult(int type, Object obj) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = obj;

	}
}
