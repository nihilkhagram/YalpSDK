package com.cointizen.paysdk.bean.privacy;


import static android.text.TextUtils.isEmpty;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.cointizen.open.PrivacyCallback;
import com.cointizen.paysdk.bean.InitModel;
import com.cointizen.paysdk.dialog.DialogConstants;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.privacy.AllowPrivacyDialog;
import com.cointizen.paysdk.dialog.privacy.SecondPrivacyTipDialog;
import com.cointizen.paysdk.entity.PrivacyInfoEntity;
import com.cointizen.paysdk.http.privacy.PrivacyInfoProcess;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.bean.BeanConstants;


public class PrivacyManager {

    private static final String TAG = "PrivacyManager";

    private static PrivacyManager instance;
    private TextView tvPrivacyPolicy;
    private TextView tvUserAgreement;

    public static PrivacyManager getInstance(){
        if(null == instance){
            instance = new PrivacyManager();
        }
        return instance;
    }
    private PrivacyManager() { }

    private Activity mActivity;
    private PrivacyCallback mPrivacyCallback;
    private PrivacyInfoEntity privacyInfoEntity;

    public boolean isLogout = false;
    public boolean privacyStatus = true;//服务端是否支持隐私授权功能

    public void allowPrivacy(Activity activity, PrivacyCallback privacyCallback) {
        if (activity == null){
            MCLog.e(TAG,"activity is null");
            return;
        }
        boolean isFirst = SharedPreferencesUtils.getIsFirstPrivacy(activity);
        if (!isFirst){
            if(privacyCallback != null) {
                privacyCallback.userPrivacy(1);
            }
            return;
        }
        mPrivacyCallback = privacyCallback;
        mActivity = activity;
        queryAgreeInfo(true);
    }

    public void secondPrivacy() {
        if (mActivity == null){
            MCLog.e(TAG,"activity is null");
            return;
        }
        //延时显示，解决小米手机显示第二个弹窗，没有半透明背景问题
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new SecondPrivacyTipDialog.Builder()
                        .show(mActivity, mActivity.getFragmentManager());
            }
        }, 500);

    }

    public void returnResult(int result) {
        if (mPrivacyCallback != null) {
            mPrivacyCallback.userPrivacy(result);
        }
    }

    public void queryAgreeInfo(boolean isShowDialog) {
        new PrivacyInfoProcess().post(privacyHandle, isShowDialog);
    }

    Handler privacyHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case Constant.PRIVACY_SUCCESS:
                    privacyInfoEntity = (PrivacyInfoEntity)message.obj;
                    if (privacyInfoEntity.isShowDialog) {
                        new AllowPrivacyDialog.Builder().show(mActivity, mActivity.getFragmentManager());
                    }
                    if(tvPrivacyPolicy != null && isEmpty(tvPrivacyPolicy.getText())) {
                        setPrivacyPolicy(tvPrivacyPolicy);
                    }
                    if (tvUserAgreement != null && isEmpty(tvUserAgreement.getText())) {
                        setUserAgreement(tvUserAgreement);
                    }
                    InitModel.init().initCallback(0);
                    break;
                case Constant.HTTP_REQUEST_FAIL:
                    privacyStatus = false;
                    MCLog.e(TAG, BeanConstants.Log_hmVTgxVeco + (String) message.obj);
                    if (mActivity != null) {
                        ToastUtil.show(mActivity, (String) message.obj);
                    }
                    InitModel.init().initCallback(1);
                    break;
                case Constant.PRIVACY_STATUS_CLOSE:
                    privacyStatus = false;
                    MCLog.e(TAG, BeanConstants.Log_MLQwngvhLE);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public void setUserAgreement(TextView textView) {
        this.tvUserAgreement = textView;
        textView.setText(userAgreementTitle());
    }
    public void setPrivacyPolicy(TextView textView) {
        this.tvPrivacyPolicy = textView;
        textView.setText(privacyPolicyTitle());
    }

    public String privacyPolicyTitle() {
        if (privacyInfoEntity == null) {
            return "";
        }
        return privacyInfoEntity.getPrivacy_name();
    }

    public String privacyPolicyUrl() {
        if (privacyInfoEntity == null) {
            return "";
        }
        return privacyInfoEntity.getPrivacy_link();
    }

    public String userAgreementTitle() {
        if (privacyInfoEntity == null) {
            return "";
        }
        return privacyInfoEntity.getAgreement_name();
    }

    public String agreementUrl() {
        if (privacyInfoEntity == null) {
            return "";
        }
        return privacyInfoEntity.getAgreement_link();
    }

    public String logoutName() {
        if (privacyInfoEntity == null) {
            return "";
        }
        return privacyInfoEntity.getClose_name();
    }

    public String logoutUrl() {
        if (privacyInfoEntity == null) {
            return "";
        }
        return privacyInfoEntity.getClose_link();
    }
}
