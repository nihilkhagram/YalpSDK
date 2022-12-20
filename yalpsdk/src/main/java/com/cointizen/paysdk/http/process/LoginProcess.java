package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.LoginRequest;
import com.cointizen.paysdk.service.ServiceManager;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

public class LoginProcess {

	private static final String TAG = "LoginProcess";
	private final Context context;
	private String account;
	private String password;

	public LoginProcess(final Context context) {
		this.context = context;

	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void post(Handler handler) {
		if (null == handler) {
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("sdk_version", "1");//表示android发送的请求，固定值1
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("game_appid", SdkDomain.getInstance().getGameAppId());
		if (ServiceManager.getInstance().isBaiDuYunOS) {
			map.put("code", ServiceManager.getInstance().loginCode);
		}else {
			map.put("account", account);
			map.put("password", password);
		}

		String param = RequestParamUtil.getRequestParamString(map);
		if (TextUtils.isEmpty(param)) {
			MCLog.e(TAG, "fun#post param is null");
			return;
		}
		RequestParams params = new RequestParams();
		MCLog.w(TAG, MCHConstant.getInstance().getPlatformUserLoginUrl() + ", param:" + map);
		try {
			params.setBodyEntity(new StringEntity(param.toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != params) {
			LoginRequest loginRequest = new LoginRequest(handler, account, password);
			loginRequest.post(MCHConstant.getInstance().getPlatformUserLoginUrl(), params,context);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
