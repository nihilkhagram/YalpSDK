package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.bean.UserInfoBean;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.http.process.ChangeUserInfoProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.PreSharedManager;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;

import java.util.LinkedList;

public class MCHChangePasswordActivity extends MCHBaseActivity {
    private String TAG = "MCChangePasswordActivity";
    Activity context;

    String senAcc;
    String senPwd;

    EditText oldPwd;
    EditText newPwd;
    EditText subPwd;

    String oldPwd_;
    String newPwd_;
    String subPwd_;

    private TextView btnMchChagePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(getLayout("mch_act_chagepass"));
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        View btnMchBack = findViewById(getId("btn_mch_back"));
        btnMchBack.setOnClickListener(backClick);
        senAcc = UserLoginSession.getInstance().getChannelAndGame().getAccount();
        senPwd = UserLoginSession.getInstance().getChannelAndGame().getPassword();

        oldPwd = (EditText) findViewById(getId("edit_yuanPass"));
        newPwd = (EditText) findViewById(getId("edit_newPass"));
        subPwd = (EditText) findViewById(getId("edit_queRenPass"));
        btnMchChagePass = (TextView) findViewById(getId("btn_mch_chagePass"));
        btnMchChagePass.setOnClickListener(subListener);
    }

    OnClickListener backClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            MCHChangePasswordActivity.this.finish();
        }
    };

    OnClickListener subListener = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            oldPwd_ = oldPwd.getText().toString();
            newPwd_ = newPwd.getText().toString();
            subPwd_ = subPwd.getText().toString();
            check();
        }
    };

    void check() {
        if (!LoginModel.instance().isLogin()) {
            ToastUtil.show(context, ActivityConstants.S_UzJvwrNjqb);
            return;
        }
        if (TextUtils.isEmpty(oldPwd_)) {
            ToastUtil.show(context, ActivityConstants.S_yVbzNJneUU);
            return;
        }
        if (!(oldPwd_.equals(senPwd))) {
            ToastUtil.show(context, ActivityConstants.S_LFBlkmcfdX);
            return;
        }
        if (TextUtils.isEmpty(newPwd_)) {
            ToastUtil.show(context, ActivityConstants.S_OOXCERYVqE);
            return;
        }
        if (!newPwd_.matches(Constant.REGULAR_ACCOUNT)) {
            ToastUtil.show(context, ActivityConstants.S_mYQXEndOJj);
            return;
        }
        if (TextUtils.isEmpty(subPwd_)) {
            ToastUtil.show(context, ActivityConstants.S_XsLCryVBAU);
            return;
        }
//        String regular = "^[a-zA-Z0-9]{6,15}$";
//        if (!newPwd_.matches(regular)) {// 改为正则表达式
//            ToastUtil.show(context, ActivityConstants.S_OyCQAGvMai);
//            return;
//        }
        if (!(newPwd_.equals(subPwd_))) {
            ToastUtil.show(context, ActivityConstants.S_ObgwEphDdq);
            return;
        }
        if (oldPwd_.equals(newPwd_)) {
            ToastUtil.show(context, ActivityConstants.S_TghNTzmQcD);
            return;
        }
        changePwd();
    }

    //出发更改密码请求方法
    private void changePwd() {
//		RequestParams params = createParams(senAcc, newPwd_);
//		ChangePasswordRequest changeRequest = new ChangePasswordRequest(myHandler);
//		changeRequest.post(myHandler);

        ChangeUserInfoProcess changePwdProcess = new ChangeUserInfoProcess();
        changePwdProcess.setPwd(oldPwd_);
        changePwdProcess.setRepwd(subPwd_);
        changePwdProcess.setType(6);
        changePwdProcess.post(myHandler);

    }

    /**
     * 请求后根据结果执行操作
     */
    @SuppressLint("HandlerLeak")
    public Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.MODIFY_PASSWORD_SUCCESS:// 密码修改成功
                    MCLog.e(TAG, "update pwd onSuccess");
                    UserLoginSession.getInstance().getChannelAndGame().setPassword(newPwd_);

                    if(Constant.LoginType == 3){
                        PreSharedManager.setString(Constant.CUSTOMER_YK_PASSWORD, newPwd_, context);
                    }
                    SharedPreferencesUtils.getInstance().setLoginPassword(context,newPwd_);

                    LinkedList<UserInfoBean> list = PreSharedManager.getUserInfoList(context);
                    if (null != list && list.size() != 0) {
                        UserInfoBean userInfoBean;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getAccount().equals(UserLoginSession.getInstance().getChannelAndGame().getAccount())) {
                                userInfoBean = list.get(i);
                                userInfoBean.setPwd(newPwd_);
                                PreSharedManager.saveUserInfoList(context, userInfoBean);
                            }
                        }
                    }

                    ToastUtil.show(context, ActivityConstants.S_egOWrztzyM);
                    MCHChangePasswordActivity.this.finish();
                    break;
                case Constant.MODIFY_PASSWORD_FAIL:// 密码修改失败
                    String tip = (String) msg.obj;
                    if (TextUtils.isEmpty(tip)) {
                        tip = ActivityConstants.S_pZAvlVpCxH;
                    }
                    ToastUtil.show(context, tip);
                    break;
                default:
                    break;
            }
        }
    };
}
