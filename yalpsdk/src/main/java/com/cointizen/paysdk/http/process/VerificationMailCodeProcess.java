package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.VerificationCodeRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VerificationMailCodeProcess {

private static final String TAG = "VerificationMailCodeProcess";
	
	private String mail;
	private int code_type;   // 注册传1 找回密码2  解绑传3 绑定传4


	private String account = "";

	public void setAccount(String account) {
		this.account = account;
	}
	
	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		if(code_type!=1){
			if(!account.equals("")){
				map.put("account", account);
			}else{
				map.put("account", UserLoginSession.getInstance().getAccount());
			}
		}
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("email", mail);
		map.put("code_type",code_type+"");
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		
		return RequestParamUtil.getRequestParamString(map);
	}
	
	public void post(Handler handler){
		if (null != handler) {
//			StringBuilder url = new StringBuilder();
//			url.append(MCHConstant.getInstance().getVerifyPhoneCodeUrl())
//			.append("/phone/").append(phone);
//			VerifyPhoneCodeRequest verifyCodeRequest = new VerifyPhoneCodeRequest(handler);
//			verifyCodeRequest.get(url.toString());
			
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(getParamStr().toString()));
			} catch (UnsupportedEncodingException e) {
				params = null;
				MCLog.e(TAG, "fun#ptb_pay UnsupportedEncodingException:" + e);
			}
			
			new VerificationCodeRequest(handler).post(MCHConstant.getInstance().getVerifyMailCodeUrl(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setCodeType(int type){
		this.code_type = type;
	}
}
