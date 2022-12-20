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
import com.cointizen.paysdk.entity.UserPTBInfo;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;

public class UserPtbBanalceRequest {

	private static final String TAG = "UserPtbRemainRequest";

	HttpUtils http;
	Handler mHandler;

	public UserPtbBanalceRequest(Handler mHandler) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, RequestParams params) {
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null add params is null");
			noticeResult(Constant.PTB_BALANCE_FAIL, "参数为空");
			return;
		}
		MCLog.e(TAG, "fun#post url = " + url);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String response=RequestUtil.getResponse( responseInfo);
				UserPTBInfo userPtb = new UserPTBInfo();
				try {
					JSONObject json = new JSONObject(response);
					int status = json.optInt("code");
					if (status==200) {
						JSONObject data = json.getJSONObject("data");
						userPtb.setPtbMoney(stringToFloat(data.optString("balance")));
						userPtb.setBindptbMoney(stringToFloat(data.optString("bind_balance")));

						noticeResult(Constant.PTB_BALANCE_SUCCESS, userPtb);
					} else {
						String msg;
						if (!TextUtils.isEmpty(json.optString("msg"))) {
							msg = json.optString("msg");
						} else {
							msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
						}
						MCLog.e(TAG, "msg:" + msg);
						noticeResult(Constant.PTB_BALANCE_FAIL, msg);
					}
				} catch (JSONException e) {
					noticeResult(Constant.PTB_BALANCE_FAIL, "数据解析异常");
					MCLog.e(TAG, "fun#get json e = " + e);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MCLog.e(TAG, "onFailure" + msg);
				Message fialmsg = new Message();
				fialmsg.what = Constant.PTB_BALANCE_FAIL;
				if (null != mHandler) {
					mHandler.sendMessage(fialmsg);
				}
			}
		});
	}

	protected float stringToFloat(String optString) {
		if (TextUtils.isEmpty(optString)) {
			return 0;
		}
		float money;
		try {
			money = Float.parseFloat(optString);
		} catch (NumberFormatException e) {
			money = 0;
		}

		return money;
	}

	protected void noticeResult(int type, Object obj) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = obj;

		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}

	}
}
