package com.cointizen.paysdk.bean.thirdlogin;

import android.annotation.SuppressLint;
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
 * on 2017/1/7.
 */

public class QQThirdLogin {
    private static final String TAG = "QQThirdLogin";

    private static QQThirdLogin mQQLogin;

    public static QQThirdLogin Instance() {
        if (mQQLogin == null) {
            mQQLogin = new QQThirdLogin();
        }
        return mQQLogin;
    }

    private QQThirdLogin() {
    }

    /**
     * @param qqappid QQ登录需要的值
     */
    public void lunchQQLogin(String qqappid) {
        Activity context = (Activity) YalpGamesSdk.getMainActivity();
        if (null == context) {
            return;
        }
        if (TextUtils.isEmpty(qqappid)) {
            ToastUtil.show(context, BeanConstants.S_HPWuHDrPyP);
            return;
        }

        Intent intent = new Intent(context, MCHThirdLoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("logintype", "qqlogin");
        bundle.putString("qqappid", qqappid);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    /**
     * 用openid值登录
     *
     * @param openid 后台返回的值
     */
    public void requestOpenId(String openid, String accessToken) {
        MCLog.w(TAG, "openid:" + openid);
        ThirdLoginProcess process = new ThirdLoginProcess();
        process.thirdLoginType = ThirdLoginProcess.THIRDLOGIN_QQ;
        process.qqopenid = openid;
        process.accessToken = accessToken;
        process.setContext(YalpGamesSdk.getMainActivity());
        process.post(handlerQQLogin);
    }

    @SuppressLint("HandlerLeak")
    private Handler handlerQQLogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.USER_THIRD_PARAMS_SUCCESS://第三方登录成功
                    MCLog.i(TAG, "qqlogin success");
                    LoginModel.instance().loginSuccess(false, true, (UserLogin) msg.obj);
                    break;
                case Constant.USER_THIRD_PARAMS_FAIL://第三方登录失败
                    MCLog.i(TAG, "qqlogin fail");
                    LoginModel.instance().loginFail();
                    break;
                default:
                    break;
            }

        }
    };
}
