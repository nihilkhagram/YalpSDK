package com.cointizen.paysdk.http.comque;

import com.cointizen.paysdk.db.comque.CommonQuesstionAdapter;
import com.cointizen.paysdk.db.comque.CommonQuesstionBiz;
import com.cointizen.paysdk.http.RequestUtil;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class CommonQuesstionRequest {

	private static final String TAG = "CommonQuesstionRequest";

	HttpUtils http;
	Handler mHandler;

	public CommonQuesstionRequest(Handler mHandler) {
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
					JSONArray ja = new JSONArray(response);
					JSONObject jo;
					CommonQuesstionBiz biz=new CommonQuesstionBiz();
					CommonQuesstionBiz biz2;
					for(int i=0;i<ja.length();i++){
	                   jo=ja.getJSONObject(i);
	                   biz2=new CommonQuesstionBiz();
	                   biz2.CQID=(String) jo.opt("id");
	                   biz2.TITLE=(String)jo.opt("title");
	                   biz2.DESCRIPTION=(String)jo.opt("description");
	                   biz.list.add(biz2);
					}
					System.out.println(biz.toString()+"-----");
					for(int i=0;i<biz.list.size();i++){
						System.out.println(biz.list.get(i).toString());
					}
					CommonQuesstionAdapter adapter=new CommonQuesstionAdapter();
					adapter.open();
				adapter.AddComQue(biz);
					adapter.close();
				}catch (Exception e) {
					MCLog.e(TAG,"出现了异常信息");
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
