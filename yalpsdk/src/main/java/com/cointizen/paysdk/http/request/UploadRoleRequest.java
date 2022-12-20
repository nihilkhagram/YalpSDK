package com.cointizen.paysdk.http.request;

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

import org.json.JSONException;
import org.json.JSONObject;

public class UploadRoleRequest {

	private static final String TAG = "UploadRoleRequest";

	HttpUtils http;
	Handler mHandler;
	String mRole_name;


	public UploadRoleRequest(Handler mHandler, String role_name) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
		if (!TextUtils.isEmpty(role_name)) {
			this.mRole_name = role_name;
		} else {
			this.mRole_name = "";
		}
	}

	public void post(String url, RequestParams params) {
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null add params is null");
			noticeResult(Constant.UPLOAD_ROLE_FAIL, "参数为空");
			return;
		}
		MCLog.w(TAG, "fun#post url = " + url);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String response= RequestUtil.getResponse( responseInfo);
//				UserLogin loginSuccess = new UserLogin();
//				loginSuccess.setUserName(mUserName);
//				loginSuccess.setPassword(mPassword);

				int status=-1;
				try {
					JSONObject json = new JSONObject(response);
					status=json.optInt("code");
					if(status==200)
					{
						noticeResult(Constant.UPLOAD_ROLE_SUCCESS,json.optString("msg"));
					}
					else{
						String msg;
						if (!TextUtils.isEmpty(json.optString("msg"))) {
							msg = json.optString("msg");
						} else {
							msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
						}
						MCLog.e(TAG, "msg:" + msg);
						noticeResult(Constant.UPLOAD_ROLE_FAIL, msg);
					}
				} catch (JSONException e) {
					noticeResult(Constant.UPLOAD_ROLE_FAIL, "解析数据异常");
				} catch (Exception e) {
					noticeResult(Constant.UPLOAD_ROLE_FAIL, "解析数据异常");
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MCLog.e(TAG, "onFailure" + msg);
				MCLog.e(TAG, "onFailure" + error.getExceptionCode()+error.getStackTrace()+error.getMessage());
				noticeResult(Constant.UPLOAD_ROLE_FAIL, "Failed to connect to the server");
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
