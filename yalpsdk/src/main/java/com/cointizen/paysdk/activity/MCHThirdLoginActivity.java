package com.cointizen.paysdk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.utils.MCLog;


/**
 * Created by Administrator on 2017/9/28/028.
 * 第三方登录
 */

public class MCHThirdLoginActivity extends MCHBaseActivity {

    private static final String TAG = "ThirdLoginActivity";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        selectThirdLoginType(getIntent().getExtras());

    }

    private void selectThirdLoginType(Bundle bundle) {
        if (null != bundle) {
            String logintype = bundle.getString("logintype", "");
            MCLog.w(TAG, "logintype:" + logintype);

        } else {
            LoginModel.instance().loginFail();
            finish();
        }
    }




    private void returnBaiDuInfo(String accessToken) {
        Intent intent = new Intent(MCHThirdLoginActivity.this, MCHControlResActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("restype", "bdlogin");
        bundle.putString("accessToken", accessToken);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    private void returnQQInfo(String openId, String accessToken) {
        Intent intent = new Intent(MCHThirdLoginActivity.this, MCHControlResActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("restype", "qqlogin");
        bundle.putString("openId", openId);
        bundle.putString("accessToken", accessToken);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    private void returnWBInfo(String userid, String accessToken) {
        Intent intent = new Intent(MCHThirdLoginActivity.this, MCHControlResActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("restype", "wblogin");
        bundle.putString("wbuid", userid);
        bundle.putString("accessToken", accessToken);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
