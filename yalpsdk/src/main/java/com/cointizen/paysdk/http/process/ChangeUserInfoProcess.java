package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.ChangePasswordRequest;
import com.cointizen.paysdk.http.request.VerificationRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class ChangeUserInfoProcess {

	private static final String TAG = "ChangeUserInfoProcess";

	private String code;
	private String pwd;
	private String repwd;
	private String phone;
	private String email;
	private String nickname;
	private String phoneCode;
	private String verifyPhone;

	private int type; //	1 rephone：解绑手机  2 phone:绑定手机 3 reemail：解绑邮箱 4 email：绑定邮箱 5 nickname,修改昵称 6 pwd：修改密码

	/**
	 * 修改密码请求需要的参数
	 * @return 返回一个String
	 */
	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", UserLoginSession.getInstance().getAccount());
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());

		if(type == 1){
			map.put("code_type", "rephone");
			map.put("phone", phone);
			map.put("code", code);
		}
		if(type == 2){
			map.put("code_type", "phone");
			map.put("phone", phone);
			map.put("code", code);
		}
		if(type == 3){
			map.put("code_type", "reemail");
			map.put("email", email);
			map.put("code", code);
		}
		if(type == 4){
			map.put("code_type", "email");
			map.put("email", email);
			map.put("code", code);
			map.put("phone_code", phoneCode);
			map.put("phone", verifyPhone);
		}
		if(type == 5){
			map.put("code_type", "nickname");
			map.put("nickname", nickname);
		}
		if(type == 6){
			map.put("code_type", "pwd");
			map.put("password", repwd);
			map.put("old_password", pwd);
		}

		MCLog.w(TAG, "fun#ptb_pay params:" + map.toString());
		
		return RequestParamUtil.getRequestParamString(map);
	}

	/**
	 * 掉用此方法启动请求
	 * @param handler 请求后用到
	 */
	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr().toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {

			if(type == 6){
				ChangePasswordRequest request = new ChangePasswordRequest(handler);
				request.post(MCHConstant.getInstance().getUpdateUserInfoUrl(), params);
			}else{
				VerificationRequest request = new VerificationRequest(handler);
				request.post(MCHConstant.getInstance().getUpdateUserInfoUrl(), params);
			}

		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setRepwd(String repwd) {
		this.repwd = repwd;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public void setVerifyPhone(String verifyPhone) {
		this.verifyPhone = verifyPhone;
	}
}
