package com.cointizen.paysdk.activity;

import android.os.Bundle;
import android.app.Activity;

import com.cointizen.paysdk.utils.AppStatus;
import com.cointizen.paysdk.utils.AppStatusManager;


public class MCHTransparencyActivity extends Activity {
    private static final String TAG = "TransparencyActivity";
    private MCHLoginActivity loginActivity;
    public static MCHTransparencyActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE){
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        instance = this;
        initData();
//        MCLog.d(TAG, "onCreat");
    }

    @Override
    protected void onStart() {
//        MCLog.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
//        MCLog.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
//        MCLog.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
//        MCLog.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
//        MCLog.d(TAG, "onRestart");
        super.onRestart();
    }

    /**
     * 调用启动登录对话框的类
     */
    private void initData() {
        loginActivity = new MCHLoginActivity(this);
        loginActivity.showLoginDialog();
    }

    /**
     * 关闭当前Activity
     */
    public void closeActivity() {
        if (!isFinishing()) {
            loginActivity.dismissDialog();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
//        MCLog.d(TAG, "onDestroy");
        closeActivity();
        super.onDestroy();
    }
}
