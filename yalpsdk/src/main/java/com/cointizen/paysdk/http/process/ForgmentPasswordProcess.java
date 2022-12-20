package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.util.StoreSettingsUtil;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.ForgmentPasswordRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class ForgmentPasswordProcess {

	private static final String TAG = "ForgmentPasswordProcess";

	/**
	 * 手机号 
	 */
	private String repassword;
	
	/**
	 * 验证码 
	 */
	private String vcode; 
	
	/**
	 * 新密码 
	 */
	private String newPassword; 
	
	private String id;

	private int code_type;   //1手机短信   2邮箱
    private String account;

    private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", account);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("game_appid", SdkDomain.getInstance().getGameAppId());
		map.put("promote_id", StoreSettingsUtil.getStoreId());
		map.put("promote_account", StoreSettingsUtil.getStoreName());
		map.put("password", newPassword);
		map.put("code", vcode);
		map.put("repassword", repassword);
		map.put("code_type",code_type+"");
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		
		return RequestParamUtil.getRequestParamString(map);
	}
	
	public void post(Handler handler){

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			ForgmentPasswordRequest formentReq = new ForgmentPasswordRequest(handler);
			formentReq.post(MCHConstant.getInstance().getForgmentPasswordUrl(), params);
			return;
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCodeType(int type) {
		this.code_type = type;
	}


    public void setAccount(String account) {
        this.account = account;
    }
}
