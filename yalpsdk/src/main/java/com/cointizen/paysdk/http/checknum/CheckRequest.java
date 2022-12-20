package com.cointizen.paysdk.http.checknum;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.VerifyCodeCookisStore;
import com.cointizen.paysdk.http.HttpConstants;

/**
 *验证手机账号是否存在
 */
public class CheckRequest{

	private final String TAG = "CheckRequest";

	private HttpUtils http;
	public	Context context;

	  /**
     *验证手机号是否存在 
     */
	public void postIsExit(String phonenum) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", phonenum);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		String url = MCHConstant.getInstance().getSdkBaseUrl()+ "/User/checkAccount";
		post(1, url, map);
	}

	private void post(int code, String url, Map<String, String> map) {
		String strParams = RequestParamUtil.getRequestParamString(map, url);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(strParams));
		} catch (UnsupportedEncodingException e) {
		}

		HttpRequest.HttpMethod httpMethod = HttpRequest.HttpMethod.POST;
		http = new HttpUtils();
		if(null != VerifyCodeCookisStore.cookieStore){
			http.configCookieStore(VerifyCodeCookisStore.cookieStore);
			MCLog.e("1", "fun#post cookieStore not null");
		}else{
			MCLog.e("2", "fun#post cookieStore is null");
		}
		http.send(httpMethod, url, params, callBack);
	}

	/**
	 * 验证手机验证码是否正确
	 * @param phonecode 手机验证码
	 * @return
	 */
	public void postIsPhoneCode(String phonecode, String phonenum){
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", phonecode);
		map.put("phone",phonenum);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		String url = MCHConstant.getInstance().getCheckPhoneCode();
		post(2, url, map);
	}

	RequestCallBack<String> callBack = new RequestCallBack<String>() {
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			String result = RequestParamUtil.Result(responseInfo);
			JSONObject json = null;
			String status = "";
			String return_msg = "";
			try {
				json = new JSONObject(result);
				status = json.optString("status");
				return_msg = json.optString("msg");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if(!"1".equals(status)){
				ToastUtil.show(context, return_msg);
			}
		}
		
		@Override
		public void onFailure(HttpException error, String msg) {
			MCLog.e(TAG, "onFailure " + error.getExceptionCode());
			MCLog.e(TAG, "onFailure " + msg);
			ToastUtil.show(context, HttpConstants.S_GQdEOzpFjL);
	    }
	};
}
