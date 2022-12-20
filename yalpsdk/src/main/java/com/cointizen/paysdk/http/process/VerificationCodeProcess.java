package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.VerificationCodeRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class VerificationCodeProcess {

private static final String TAG = "VerifyPhoneCodeProcess";
	
	private String phone;
	private String type = "";    //发送验证码时后端用于判断此手机号是否已经被绑定过

	private String reg = "";    //1注册 2找回 3解绑 4绑定

	private String account = "";


	public void setReg(String reg) {
		this.reg = reg;
	}
	public void setAccount(String account) {
		this.account = account;
	}



	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("phone", phone);
		if(!reg.equals("1")){
			if(!account.equals("")){
				map.put("account", account);
			}else{
				map.put("account", UserLoginSession.getInstance().getAccount());
			}
		}
		map.put("reg",reg);

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
			
			new VerificationCodeRequest(handler).post(MCHConstant.getInstance().getVerifyPhoneCodeUrl(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setType(String type) {
		this.type = type;
	}
}
