package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.UserBindEmailRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserBindEmailProcess {

	private static final String TAG = "UserBindEmailProcess";

	private String code;
	private String phone;
	private String smcode;
	
	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("email", phone);
		map.put("code", smcode);
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
			UserBindEmailRequest request = new UserBindEmailRequest(handler);
			request.post(MCHConstant.getInstance().getBindEmail(),
					params);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
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
