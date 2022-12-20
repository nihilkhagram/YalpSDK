package com.cointizen.paysdk.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.bean.UserInfoBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.process.UpdateVisitorInfoProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.PreSharedManager;
import com.cointizen.paysdk.utils.TextUtils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhujinzhujin
 * on 2017/1/17.
 * 游客绑定账号
 */

public class MCHVisitorUpdateInfo extends MCHBaseActivity {

    /**
     * 日志打印
     */
    private final static String TAG = "MCVisitorUpdateInfo";


    private EditText etVisitorName;
    private EditText etVisitorPwd;
    private EditText etVisitorPwdAgain;

    String userName = "";
    String pwd = "";
    String pwdAgain = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout("mch_activity_personal_info_setuser"));
        initTitle();
    }

    private void initTitle(){
        TextView txtTitle = findViewById(getId("tv_mch_header_title"));
        txtTitle.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        txtTitle.setText(ActivityConstants.S_ItBvziAioL);
        RelativeLayout ivBack = findViewById(getId("iv_mch_header_back"));
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(backClick);

        etVisitorName = (EditText) findViewById(getId("mch_et_account"));
        etVisitorPwd = (EditText) findViewById(getId("mch_et_password"));
        etVisitorPwdAgain = (EditText) findViewById(getId("mch_et_password_again"));

        TextView btnUpdateVisitorInfo = findViewById(getId("mch_btn_ok"));
        btnUpdateVisitorInfo.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                submitUpdateInfo();
            }
        });
    }

    View.OnClickListener backClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            finish();
        }
    };

    /**
     * 启动设置用户名请求,并配置需要的参数
     */
    private void submitUpdateInfo(){
        userName = etVisitorName.getText().toString().trim();
        pwd = etVisitorPwd.getText().toString().trim();
        pwdAgain = etVisitorPwdAgain.getText().toString().trim();

        if(TextUtils.isEmpty(userName)){
            ToastUtil.show(this,ActivityConstants.S_TpAsVMAASp);
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            ToastUtil.show(this,ActivityConstants.S_qTiIrzLWTl);
            return;
        }

        if(TextUtils.isEmpty(pwdAgain)){
            ToastUtil.show(this,ActivityConstants.S_ngaUdnLJsU);
            return;
        }

        //判断输入的用户名及密码是否为6-15位字符
        String regular= Constant.REGULAR_ACCOUNT;
        Pattern pattern=Pattern.compile(regular);
        Matcher matcher_pwd = pattern.matcher(pwd);
        Matcher matcher_userName = pattern.matcher(userName);
        boolean b_pwd = matcher_pwd.matches();
        boolean b_userName = matcher_userName.matches();
        if(!b_userName) {
            ToastUtil.show(MCHVisitorUpdateInfo.this,ActivityConstants.S_WXhsbIsldA);
            return;
        }

        if(!b_pwd) {
            ToastUtil.show(MCHVisitorUpdateInfo.this,ActivityConstants.S_aUoqklAmfi);
            return;
        }

        if (!pwd.equals(pwdAgain)){
            ToastUtil.show(MCHVisitorUpdateInfo.this,ActivityConstants.S_MIIPfdkeAF);
            return;
        }

        UpdateVisitorInfoProcess updateVisitorInfo = new UpdateVisitorInfoProcess();
        updateVisitorInfo.setAccount(userName);
        updateVisitorInfo.setPwd(pwd);
        updateVisitorInfo.post(visitorInfoHandle);
    }


    /**
     * 设置应户名请求后的操作
     */
    private Handler visitorInfoHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.UPDATE_VISITOR_INFO_SUCCESS:
                    ToastUtil.show(MCHVisitorUpdateInfo.this,ActivityConstants.S_pkItPPsKvg);
                    String ykAccount = UserLoginSession.getInstance().getChannelAndGame().getAccount();
                    LinkedList<UserInfoBean> userInfoList = PreSharedManager.getUserInfoList(YalpGamesSdk.getMainActivity());
                    if (userInfoList!=null && userInfoList.size()>0){
                        for (int i=0;i<userInfoList.size();i++){
                            if (userInfoList.get(i).getAccount().equals(ykAccount)){
                                PreSharedManager.removeAndSaveUserInfoList(YalpGamesSdk.getMainActivity(), i);
                            }
                        }
                    }
                    handlerUpdateRes((String) msg.obj);
                    break;
                case Constant.UPDATE_VISITOR_INFO_FAIL:
                    ToastUtil.show(MCHVisitorUpdateInfo.this,msg.obj.toString());
                    MCLog.e(TAG, "error:" + msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 请求成功后保存
     * @param res
     */
    private void handlerUpdateRes(String res){
        MCLog.w(TAG, "Update res:" + res);
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(userName);
        userLogin.setPassword(pwd);
        userLogin.setAccountUserId(UserLoginSession.getInstance().getUserId());
        userLogin.setYKLogin(false);
        LoginModel.instance().saveUserInfoToPre(true, true, userLogin);
        PreSharedManager.setString(Constant.CUSTOMER_YK, "", MCHVisitorUpdateInfo.this);
        finish();
    }
}
