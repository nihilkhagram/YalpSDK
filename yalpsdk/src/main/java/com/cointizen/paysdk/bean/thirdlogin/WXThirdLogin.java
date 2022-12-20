package com.cointizen.paysdk.bean.thirdlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.activity.MCHThirdLoginActivity;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.thirdlogin.ThirdLoginProcess;
import com.cointizen.paysdk.utils.MCLog;

public class WXThirdLogin {

	private static final String TAG = "WXThirdLogin";

	private static WXThirdLogin mWXLogin;

	public static WXThirdLogin Instance(){
		if(mWXLogin == null){
			mWXLogin = new WXThirdLogin();
		}
		return mWXLogin;
	}

	private WXThirdLogin(){
	}

	public void lunchWXLogin(String wxAppid) {
		Activity activity = (Activity) YalpGamesSdk.getMainActivity();
		if(null == activity){
			LoginModel.instance().loginFail();
			MCLog.e(TAG, "activity is null");
			return;
		}
		toLogin(activity, wxAppid);
	}


	public void toLogin(Context context, String wxappid){
		Intent intent = new Intent(context, MCHThirdLoginActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("logintype", "wxlogin");
		bundle.putString("wxappid", wxappid);
		intent.putExtras(bundle);
		context.startActivity(intent);

	}

	public void requestOpenId(String wxCode){
		MCLog.w(TAG, "wxCode:" + wxCode);
		ThirdLoginProcess process = new ThirdLoginProcess();
		process.thirdLoginType = ThirdLoginProcess.THIRDLOGIN_WX;
		process.wxCode = wxCode;
		process.setContext(YalpGamesSdk.getMainActivity());
		process.post(handlerWXLogin);
	}

	private Handler handlerWXLogin = new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case Constant.USER_THIRD_PARAMS_SUCCESS://第三方登录成功
					MCLog.i(TAG, "wxlogin success");
					LoginModel.instance().loginSuccess(false, true, (UserLogin) msg.obj);
					break;
				case Constant.USER_THIRD_PARAMS_FAIL://第三方登录失败
					MCLog.i(TAG, "wxlogin fail");
					LoginModel.instance().loginFail();
					break;
				default:
					break;
			}

		}
	};
}
