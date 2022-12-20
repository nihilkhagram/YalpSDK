package com.cointizen.paysdk.activity;

import android.app.Activity;
import android.os.Bundle;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.bean.thirdlogin.BDThirdLogin;
import com.cointizen.paysdk.bean.thirdlogin.QQThirdLogin;
import com.cointizen.paysdk.bean.thirdlogin.WBThirdLogin;
import com.cointizen.paysdk.bean.thirdlogin.WXThirdLogin;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

/**
 * Created by zhujinzhujin
 * on 2017/1/6.
 */

public class MCHControlResActivity extends Activity {

    private static final String TAG = "MCControlResActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handlerControlResult(getIntent().getExtras());

        finish();
    }

    private void handlerControlResult(Bundle bundle) {
        if (null != bundle) {
            String resType = bundle.getString("restype", "");
            if ("wxlogin".equals(resType)) {
                handlerWXLogin(bundle.getString("wxcode"),
                        bundle.getString("errorcode"));
            } else if ("qqlogin".equals(resType)) {
                handlerQQLogin(bundle.getString("openId"),
                        bundle.getString("accessToken"));
            } else if ("wblogin".equals(resType)) {
                handlerWBLogin(bundle.getString("wbuid"),
                        bundle.getString("accessToken"));
            } else if ("bdlogin".equals(resType)) {
                handlerBDLogin(bundle.getString("accessToken"));
            }
        }
    }

    private void handlerWXLogin(String wxCode, String errorCode) {
        if (!TextUtils.isEmpty(wxCode) &&
                !TextUtils.isEmpty(errorCode)) {
            if ("0".equals(errorCode) && !TextUtils.isEmpty(wxCode)) {
                WXThirdLogin.Instance().requestOpenId(wxCode);
            } else {
                LoginModel.instance().loginFail();
                MCLog.e(TAG, "wxCode:" + wxCode + ", errorCode:" + errorCode);
            }
        } else {
            LoginModel.instance().loginFail();
        }
    }

    private void handlerQQLogin(String openId, String accessToken) {
        if (!TextUtils.isEmpty(openId) &&
                !TextUtils.isEmpty(accessToken)) {
            QQThirdLogin.Instance().requestOpenId(openId, accessToken);
        } else {
            LoginModel.instance().loginFail();
        }
    }

    private void handlerWBLogin(String wbuuid, String accessToken) {
        if (!TextUtils.isEmpty(wbuuid)) {
            WBThirdLogin.Instance().uploadWBUid(wbuuid, accessToken);
        } else {
            LoginModel.instance().loginFail();
        }
    }

    private void handlerBDLogin(String accessToken) {
        if (!TextUtils.isEmpty(accessToken)) {
            BDThirdLogin.Instance().uploadWBUid(accessToken);
        } else {
            LoginModel.instance().loginFail();
        }
    }

}
