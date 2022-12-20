package com.cointizen.paysdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.cointizen.paysdk.utils.VerifyCodeCookisStore;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class ForgmentPasswordRequest {

	private static final String TAG = "ForgmentPasswordrequest";

	HttpUtils http;
	Handler mHandler;

	public ForgmentPasswordRequest(Handler mHandler) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, RequestParams params) {
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null add params is null");
			noticeResult(Constant.MODIFY_PASSWORD_FAIL, HttpConstants.S_SkiSBDHxeI);
			return;
		}
		MCLog.e(TAG, "fun#post url = " + url);
		if(null != VerifyCodeCookisStore.cookieStore){
			http.configCookieStore(VerifyCodeCookisStore.cookieStore);
			MCLog.e(TAG, "fun#post cookieStore have");
		}else{
			MCLog.e(TAG, "fun#post cookieStore is null");
		}
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String response=RequestUtil.getResponse( responseInfo);
				int status;
				try {
					JSONObject json = new JSONObject(response);
					status = json.optInt("code");
					if(status==200){
						noticeResult(Constant.MODIFY_PASSWORD_SUCCESS, "");
					}else{
						String msg;
						if (!TextUtils.isEmpty(json.optString("msg"))) {
							msg = json.optString("msg");
						} else {
							msg = MCErrorCodeUtils.getErrorMsg(status);
						}
						MCLog.e(TAG, "msg:" + msg);
						noticeResult(Constant.MODIFY_PASSWORD_FAIL, msg);
					}
				} catch (JSONException e) {
					noticeResult(Constant.MODIFY_PASSWORD_FAIL, HttpConstants.S_ElwWiRoGqB);
				}catch (Exception e) {
					noticeResult(Constant.MODIFY_PASSWORD_FAIL, HttpConstants.S_ElwWiRoGqB);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
				MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
				MCLog.e(TAG, "fun#onFailure msg = " + msg + "  error = " + error.toString());
				noticeResult(Constant.MODIFY_PASSWORD_FAIL, HttpConstants.S_uVIIKmGFwV);
			}
		});
	}
	
	
	private void noticeResult(int type, String code){
		Message msg = new Message();
		msg.what = type;
		msg.obj = code;
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}

}
