package com.cointizen.plugin.qg.utils;

import android.os.Handler;
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
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import com.cointizen.plugin.AppConstants;

public class UpdateRequest {

	private static final String TAG = "UpdateRequest";

	HttpUtils http;
	Handler mHandler;

	public UpdateRequest(Handler mHandler) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, RequestParams params) {
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null add params is null");
			noticeResult(Constant.FORCED_UPDATE_FAIL, AppConstants.STR_8bf6d5abb1b26cdf40acbe39ca945700);
			return;
		}
		MCLog.w(TAG, "fun#post url = " + url);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String response=RequestUtil.getResponse( responseInfo);
				int status;
				try {
					JSONObject json = new JSONObject(response);
					status=json.optInt("code");
					if(status==200){
						JSONObject data = json.getJSONObject("data");
						UpdateBean upDateBean = new UpdateBean();
						upDateBean.setAnd_file_size(data.optString("and_file_size","0M"));
						upDateBean.setVersion_name(data.optString("and_version_name","0"));
						upDateBean.setAnd_file_url(data.optString("and_file_url"));
						upDateBean.setIs_force_update(data.optInt("is_force_update"));
						upDateBean.setAnd_version_code(data.optInt("source_version"));
						JSONArray andRemark = data.getJSONArray("and_remark");
						ArrayList<String> strings = new ArrayList<>();
						if(andRemark!=null){
							for (int i = 0;i<andRemark.length();i++){
								strings.add(andRemark.getString(i));
							}
						}
						upDateBean.setAnd_remark(strings);
						noticeResult(Constant.FORCED_UPDATE_SUCCESS, upDateBean);
					}else{
						String msg;
						if (!TextUtils.isEmpty(json.optString("msg"))) {
							msg = json.optString("msg");
						} else {
							msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
						}
						MCLog.e(TAG, "msg:" + msg);
						noticeResult(Constant.FORCED_UPDATE_FAIL, msg);
					}
				} catch (JSONException e) {
					MCLog.e(TAG, AppConstants.LOG_c4390da894d5ca89e732b88a6910a168 + e.toString());
					noticeResult(Constant.FORCED_UPDATE_FAIL, AppConstants.STR_3c34bbf829ca6d8481717bf3a9914200);
				} catch (Exception e) {
					MCLog.e(TAG, AppConstants.LOG_c4390da894d5ca89e732b88a6910a168 + e.toString());
					noticeResult(Constant.FORCED_UPDATE_FAIL, AppConstants.STR_3c34bbf829ca6d8481717bf3a9914200);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MCLog.e(TAG, "onFailure" + msg);
				MCLog.e(TAG, "onFailure" + error.getExceptionCode()+error.getStackTrace()+error.getMessage());
				noticeResult(Constant.FORCED_UPDATE_FAIL, AppConstants.STR_44ed625b1914f08d820407a2b413dba6);
			}
		});
	}

	private void noticeResult(int type, Object obj) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = obj;
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}
}
