package com.cointizen.paysdk.http.GroupMessage;

import com.cointizen.paysdk.http.RequestUtil;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.utils.MCLog;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class GroupMessageRequest {

	private static final String TAG = "CommonQuesstionRequest";

	HttpUtils http;
	Handler mHandler;

	public GroupMessageRequest(Handler mHandler) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, RequestParams params) {
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null add params is null");
			return;
		}
		MCLog.e(TAG, "fun#post url = " + url);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String response=RequestUtil.getResponse( responseInfo);
				String status = "";
				try {
					System.out.println(response.toString());
				}catch (Exception e) {
					MCLog.e(TAG,HttpConstants.Log_KRhiVBJGrN);
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MCLog.e(TAG, "onFailure" + msg);
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
