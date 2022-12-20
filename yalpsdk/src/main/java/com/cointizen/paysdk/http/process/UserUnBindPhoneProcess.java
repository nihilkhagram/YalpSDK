package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.UserUnBindPhoneRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class UserUnBindPhoneProcess {

	private static final String TAG = "UserUnBindPhoneProcess";

	private String code;
	private String phone;
	private String smcode;
	private String account;
	
	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
//		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("phone", UserLoginSession.getInstance().getChannelAndGame().getPhoneNumber());
		map.put("code", code);
		map.put("user_id", UserLoginSession.getInstance().getChannelAndGame().getUserId());
//		map.put("account",UserLoginSession.getInstance().getChannelAndGame().getAccount());
//		map.put("sms_code", smcode);
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		
		return RequestParamUtil.getRequestParamString(map);
	}

	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr().toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			UserUnBindPhoneRequest request = new UserUnBindPhoneRequest(handler);
			request.post(MCHConstant.getInstance().getUserUnBindPhoneUrl(),
					params);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
    public void setAccount(String account){
    	this.account=account;
    }
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setSmcode(String smcode) {
		this.smcode = smcode;
	}

}
