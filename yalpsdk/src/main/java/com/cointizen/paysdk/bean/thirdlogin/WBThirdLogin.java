package com.cointizen.paysdk.bean.thirdlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.activity.MCHThirdLoginActivity;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.thirdlogin.ThirdLoginProcess;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.bean.BeanConstants;

/**
 * Created by zhujinzhujin
 * on 2017/1/9.
 */

public class WBThirdLogin {

    private static final String TAG = "WBThirdLogin";

    private static WBThirdLogin mWBLogin;

    public static WBThirdLogin Instance(){
        if(mWBLogin == null){
            mWBLogin = new WBThirdLogin();
        }
        return mWBLogin;
    }

    private WBThirdLogin(){
    }

    public void lunchWBLogin(String wbAppKey, String redirectUrl, String soap){

        Activity context = (Activity) YalpGamesSdk.getMainActivity();
        if (null == context) {
            MCLog.e(TAG, "activity is null");
            return;
        }
        if (TextUtils.isEmpty(wbAppKey)) {
            ToastUtil.show(context, BeanConstants.S_XSVvgbVkFT);
            return;
        }

        Intent intent = new Intent(context, MCHThirdLoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("logintype", "wblogin");
        bundle.putString("wbappkey", wbAppKey);
        bundle.putString("wbredirecturl", redirectUrl);
        bundle.putString("wbsoap", soap);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 用后台返回的值登录
     * @param wbuid
     * @param accessToken
     */
    public void uploadWBUid(String wbuid, String accessToken){
        MCLog.w(TAG, "wbuid:" + wbuid);
        ThirdLoginProcess process = new ThirdLoginProcess();
        process.thirdLoginType = ThirdLoginProcess.THIRDLOGIN_WB;
        process.wbuid = wbuid;
        process.wbaccesstoken = accessToken;
        process.setContext(YalpGamesSdk.getMainActivity());
        process.post(handlerWBLogin);
    }

    private Handler handlerWBLogin = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.USER_THIRD_PARAMS_SUCCESS://第三方登录成功
                    MCLog.i(TAG, "wblogin success");
                    LoginModel.instance().loginSuccess(false, true, (UserLogin) msg.obj);
                    break;
                case Constant.USER_THIRD_PARAMS_FAIL://第三方登录失败
                    MCLog.i(TAG, "wblogin fail");
                    LoginModel.instance().loginFail();
                    break;
                default:
                    break;
            }

        }
    };
}
