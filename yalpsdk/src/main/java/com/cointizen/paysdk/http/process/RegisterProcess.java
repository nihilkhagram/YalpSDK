package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.util.StoreSettingsUtil;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.RegisterRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class RegisterProcess {

	private static final String TAG = "RegisterProcess";
	
	private String account;
	private String password;
	private String type;		//1  普通注册     2手机号  3邮箱
	private String code;
	private String repassword;
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setType(String type) {
		this.type = type;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
	
	public void post(Handler handler, Context context) {
		MCLog.e(TAG,HttpConstants.Log_RbVtUMNAtZ);
		if (null == handler){
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", account);
		map.put("password", password);
		if(type.equals("1")){
			map.put("repassword", repassword);
		}else{
			map.put("code", code);
		}
		map.put("type", type);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("game_appid", SdkDomain.getInstance().getGameAppId());
		map.put("promote_id", StoreSettingsUtil.getStoreId());
		map.put("promote_account", StoreSettingsUtil.getStoreName());

		String param = RequestParamUtil.getRequestParamString(map);
		if (TextUtils.isEmpty(param)) {
			MCLog.e(TAG, "fun#post param is null");
			return;
		}
		RequestParams params = new RequestParams();
		MCLog.w(TAG, "fun#post postSign:" + map.toString());
		try {
			params.setBodyEntity(new StringEntity(param.toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}
		
		if (null != params) {
			RegisterRequest registerRequest = new RegisterRequest(handler,
					account, password);
			registerRequest.post(MCHConstant.getInstance().getPlatformUserRegisterUrl(), params, context);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}
}
