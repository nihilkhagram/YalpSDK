package com.cointizen.paysdk.http.thirdlogin;

import android.content.Context;
import android.os.Handler;

import com.cointizen.util.StoreSettingsUtil;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * user login by wx
 * @author Administrator
 *
 */
public class ThirdLoginProcess {

	public static final int THIRDLOGIN_WB = 0x01;
	public static final int THIRDLOGIN_QQ = 0x02;
	public static final int THIRDLOGIN_WX = 0x03;
	public static final int THIRDLOGIN_BD = 0x04;
	public static final int THIRDLOGIN_YK = 0x05;


	/**
	 * 第三方账号的唯一标示
	 * */
	public String qqopenid;

	public String accessToken;

	/**
	 * 微博第三方账号登录参数
	 * */
	public String wbuid;
	public String wbaccesstoken;

	/**
	 * 百度第三方账号登录参数
	 * */
	public String bdaccesstoken;
	/**
	 * 微信临时票据code
	 */
	public String wxCode;

	/**
	 * 游客帐号
	 */
	public String ykAccount;
	public String ykPassword;

	/**
	 * 第三方登录方式
	 */
	public int thirdLoginType;


	private static final String TAG = "ThirdLoginProcess";
	private Context context;

	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		//表明登录请求的类型
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("game_appid", SdkDomain.getInstance().getGameAppId());
		map.put("promote_id", StoreSettingsUtil.getStoreId());
		map.put("promote_account", StoreSettingsUtil.getStoreName());

		switch (thirdLoginType){
			case THIRDLOGIN_WB:
				map.put("login_type", "wb");
				map.put("openid", wbuid);
//				map.put("accesstoken", wbaccesstoken);
				break;
			case THIRDLOGIN_QQ:
				map.put("login_type", "qq");
				map.put("openid", qqopenid);
				map.put("accessToken",accessToken);
				break;
			case THIRDLOGIN_WX:
				map.put("login_type", "wx");
				map.put("code", wxCode);
				break;
			case THIRDLOGIN_BD:
				map.put("login_type", "bd");
				map.put("accessToken", bdaccesstoken);
				break;
			case THIRDLOGIN_YK:
				map.put("login_type", "yk");
				if(ykAccount!=null&&!ykAccount.equals("")){
					map.put("account", ykAccount);
					map.put("password", ykPassword);
				}
				break;
			default:
				break;
		}

		MCLog.w(TAG, "fun#getParamStr params:" + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}
	public void setContext(final Context context){
		this.context = context;

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
			//dialog.dismiss();
			ThirdLoginRequest request = new ThirdLoginRequest(handler);
			if(THIRDLOGIN_YK == thirdLoginType){
				request.setYKLogin(true);
			}else {
				request.setYKLogin(false);
			}
			request.post(MCHConstant.getInstance().getThirdloginrequest(), params,context);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
