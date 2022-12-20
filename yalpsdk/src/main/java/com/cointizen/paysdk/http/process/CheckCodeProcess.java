package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.CheckCodeRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CheckCodeProcess {

	private static final String TAG = "CheckCodeProcess";

	public void setCode(String code) {
		this.code = code;
	}

	public void setCode_type(String code_type) {
		this.code_type = code_type;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String code = "";
	private String code_type = "";
	private String phone = "";
	private String email = "";

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		if(code_type.equals("1")){
			map.put("phone", phone);
		}else{
			map.put("email", email);
		}
		map.put("code_type", code_type);
		map.put("game_id",SdkDomain.getInstance().getGameId());
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
			new CheckCodeRequest(handler).post(MCHConstant.getInstance().getCheckCodeUrl(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
