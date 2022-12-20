package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.ForgetAccountRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ForgetAccountProcess {

	private static final String TAG = "ForgetAccountProcess";
	private String account = "";

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", account);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		return RequestParamUtil.getRequestParamString(map);
	}

	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr().toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#ptb_pay UnsupportedEncodingException:" + e);
		}
		if (null != handler) {
			new ForgetAccountRequest(handler).post(MCHConstant.getInstance().getForgetAccountUrl(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
