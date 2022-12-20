package com.cointizen.paysdk.http.request;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;

import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;
import com.cointizen.paysdk.utils.VerifyCodeCookisStore;

import android.os.Handler;
import android.os.Message;

public class ChangePasswordRequest {

	private static final String TAG = "ChangePasswordRequest";

	HttpUtils http;
	Handler mHandler;

	public ChangePasswordRequest(Handler mHandler) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, RequestParams params) {
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null add params is null");
			noticeResult(Constant.MODIFY_PASSWORD_FAIL, "参数异常");
			return;
		}
		MCLog.w(TAG, "fun#post url = " + url);
		if (null != VerifyCodeCookisStore.cookieStore) {
			http.configCookieStore(VerifyCodeCookisStore.cookieStore);
			MCLog.e(TAG, "fun#post cookieStore not null");
		} else {
			MCLog.e(TAG, "fun#post cookieStore is null");
		}
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String response=RequestUtil.getResponse( responseInfo);
				int status ;
				String tip = "";

				try {
					JSONObject json = new JSONObject(response);
					status = json.optInt("code");
					tip = json.optString("msg");

					JSONObject data = json.getJSONObject("data");
					String token = data.optString("token");
					UserLoginSession.getInstance().getChannelAndGame().setSmallAccountToken(token);
					MCLog.e(TAG, "tip22222:" + tip+"======="+token);
				} catch (Exception e) {
					status = -1;
					tip = "参数异常";
				}
				if (status == 200) {
					noticeResult(Constant.MODIFY_PASSWORD_SUCCESS, "");
				} else {
					noticeResult(Constant.MODIFY_PASSWORD_FAIL, tip);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MCLog.e(TAG, "onFailure" + msg);
				noticeResult(Constant.MODIFY_PASSWORD_FAIL, "Failed to connect to the server");
			}
		});
	}

	private void noticeResult(int type, String str) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = str;
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}
}
