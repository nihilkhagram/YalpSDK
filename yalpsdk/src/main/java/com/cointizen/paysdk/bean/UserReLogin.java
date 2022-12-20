package com.cointizen.paysdk.bean;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.process.LoginProcess;
import com.cointizen.paysdk.utils.PreSharedManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class UserReLogin {

	private ReLoginCallback reLoginCallback;
	private Context context;

	public UserReLogin(Context context) {
		this.context = context;
	}

	/**
	 * 用户重新登录发起请求
	 * @param reLoginCallback 用户重新登录的回调接口
	 */
	public void userToLogin(ReLoginCallback reLoginCallback) {
		this.reLoginCallback = reLoginCallback;
		LoginProcess loginprocess = new LoginProcess(context);
		loginprocess.setAccount(PreSharedManager.getString(Constant.CUSTOMER, context));
		loginprocess.setPassword(PreSharedManager.getString(Constant.PASSWORD, context));
		loginprocess.post(mHandler);
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.LOGIN_SUCCESS:// 登陆成功
				UserLogin loginSuccess = (UserLogin) msg.obj;
				// 保存用户名和密码
				UserLoginSession.getInstance().getChannelAndGame().setAccount(loginSuccess.getUserName());
				UserLoginSession.getInstance().getChannelAndGame().setPassword(loginSuccess.getPassword());
				UserLoginSession.getInstance().getChannelAndGame().setUserId(loginSuccess.getAccountUserId());
				if(null != reLoginCallback){
					reLoginCallback.reLoginResult(true);
				}
				break;
			case Constant.LOGIN_FAIL:// 登陆失败
				reLoginCallback.reLoginResult(false);
				break;
				default:
					break;
			}
		}
		
	};
	public interface ReLoginCallback{
		public void reLoginResult(boolean res);
	}
}
