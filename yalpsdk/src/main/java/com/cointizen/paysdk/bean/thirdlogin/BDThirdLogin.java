package com.cointizen.paysdk.bean.thirdlogin;

import android.app.Activity;
import android.app.ProgressDialog;
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
 * 百度登录
 * on 2017/1/9.
 */

public class BDThirdLogin {
    private static final String TAG = "BDThirdLogin";

    private static BDThirdLogin mBDLogin;
    private ProgressDialog dialog;

    public static BDThirdLogin Instance(){
        if(mBDLogin == null){
            mBDLogin = new BDThirdLogin();
        }
        return mBDLogin;
    }

    private BDThirdLogin(){
    }

    public void lunchBDLogin(String clientId){
        Activity context = (Activity) YalpGamesSdk.getMainActivity();
        dialog = new ProgressDialog(context);

        if (null == context) {
            MCLog.e(TAG, "activity is null");
            return;
        }
        if (TextUtils.isEmpty(clientId)) {
            ToastUtil.show(context, BeanConstants.S_jvNVGZDhgs);
            return;
        }

        Intent intent = new Intent(context, MCHThirdLoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("logintype", "bdlogin");
        bundle.putString("bdclientid", clientId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 用token值登录
     * @param accessToken 后台返回的Token值
     */
    public void uploadWBUid(String accessToken){
        MCLog.w(TAG, "bd accessToken:" + accessToken);
        ThirdLoginProcess process = new ThirdLoginProcess();
        process.thirdLoginType = ThirdLoginProcess.THIRDLOGIN_BD;
        process.bdaccesstoken = accessToken;
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
