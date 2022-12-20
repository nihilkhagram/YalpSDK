package com.cointizen.paysdk.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.cointizen.paysdk.bean.UserInfoBean;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.callback.PlatformLoginCallback;
import com.cointizen.paysdk.callback.PlatformRegisterCallback;
import com.cointizen.paysdk.callback.RefreshVerifyCode;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.PlatformLoginDialog;
import com.cointizen.paysdk.dialog.register.RegisterDialog;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.checknum.CheckRequest;
import com.cointizen.paysdk.http.process.LoginProcess;
import com.cointizen.paysdk.http.process.RegisterProcess;
import com.cointizen.paysdk.http.process.ThirdLoginTypeProcess;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.PreSharedManager;
import com.cointizen.paysdk.utils.TextUtils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MCHLoginActivity {

    private static final String TAG = "LoginActivity";

    private Context mContext;
    private PlatformLoginDialog loginDialog;
    private RegisterDialog registerDialog;
    /**
     * 是否快速登录
     */
    private boolean isQuickLogin;

    ProgressDialog dialog;

    /**
     * 判断是否为账号登陆或者手机号登陆
     */
    public static boolean isAccount = false;

    private final Handler loginHandle = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case Constant.LOGIN_SUCCESS:// 登陆成功
                    isAccount = true;
                    UserLogin loginSuccess = (UserLogin) msg.obj;
                    handlerLoginResult(loginSuccess);
//                    if(SharedPreferencesUtils.getInstance().getLaunch(context)){
//                        LaunchProcess launchProcess = new LaunchProcess(context,loginSuccess.getAccountUserId());
//                        launchProcess.post();
//                    }
                    if (auto) {
                        auto = false;
                    }
                    break;
                case Constant.LOGIN_FAIL:// 登陆失败
                    String loginErrorMsg = (String) msg.obj;
                    if (TextUtils.isEmpty(loginErrorMsg)) {
                        loginErrorMsg = ActivityConstants.S_EgidcrRSwu;
                    }
                    LoginModel.instance().loginFail();
                    ToastUtil.show(mContext, loginErrorMsg);
                    if (auto) {
                        auto = false;
                        Intent loginIntent = new Intent(mContext, MCHTransparencyActivity.class);
                        mContext.startActivity(loginIntent);
                    }
                    break;
            }
            return false;
        }
    });

    private final Handler registerHandler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.REGISTER_SUCCESS:// 注册成功
                    UserLogin registerSuccess = (UserLogin) msg.obj;
                    registerSuccess(registerSuccess);
                    break;
                case Constant.REGISTER_FAIL:// 注册失败
                    String regErrorMsg = (String) msg.obj;
                    if (TextUtils.isEmpty(regErrorMsg)) {
                        regErrorMsg = ActivityConstants.S_rmkqUBpKVp;
                    }
                    ToastUtil.show(mContext, regErrorMsg);
                    break;
            }
        }
    };

    /**
     * 是否自动登录
     */
    private boolean auto = false;
//    private Activity activity;

    /**
     * 构造方法
     *
     * @param context 上下文对象
     */
    public MCHLoginActivity(Context context) {
        this.mContext = context;
    }


    /**
     * 调用此方法显示登录对话框
     */
    public void showLoginDialog() {

        if (SdkDomain.getInstance().haveReadGameInfo()) {
            LinkedList<UserInfoBean> list = PreSharedManager.getUserInfoList(mContext);
            String account = "";
            String pwd = "";
            UserInfoBean userInfoBean;
            if (null != list && list.size() != 0) {
                userInfoBean = list.getFirst();
                account = userInfoBean.getAccount();
                pwd = userInfoBean.getPwd();
            }
            isQuickLogin = false;
            loginDialog = new PlatformLoginDialog.Builder()
                    .setAccount(account)
                    .setPassword(pwd)
                    .setLoginCallback(mLoginCallback)
                    .setForgmentPwdClick(forgetPasswordClick)
                    .setRegisterClick(showRegisterClick)
                    .setThirdLoginClick(thirdLoginClick)
                    .setYKLoginClick(visitorsLogin)
                    .setDialogKeyListener(backKeyListener)
                    .setLoginCancelClick(loginCancel)
                    .show(mContext, ((Activity) mContext).getFragmentManager());

            thirdLoginType();

        } else {
            ToastUtil.show(mContext, ActivityConstants.S_IbRrxwBsMs);
        }
    }

    /**
     * 获取后台第三方登录的方式
     */
    private void thirdLoginType() {
        new ThirdLoginTypeProcess().post(logintypeHandle);
    }

    private final Handler logintypeHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case Constant.THIRD_LOGIN_TYPE_SUCCESS:
                    splitThirdLoginType((String) msg.obj);
                    break;
                case Constant.THIRD_LOGIN_TYPE_FAIL:
                    MCLog.w(TAG, "" + msg.obj);
                    if (null != loginDialog) {
                        loginDialog.setThirdLoginButtonShow(false, false, false, false, false);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * @param loginTypeStr ActivityConstants.S_EejMZVmzTE
     */
    private void splitThirdLoginType(String loginTypeStr) {
        boolean isWB = loginTypeStr.contains("wb");
        boolean isQQ = loginTypeStr.contains("qq");
        boolean isWX = loginTypeStr.contains("wx");
        boolean isBD = loginTypeStr.contains("bd");
        boolean isYK = loginTypeStr.contains("yk");
        if (null != loginDialog) {
            loginDialog.setThirdLoginButtonShow(isWB, isQQ, isWX, isBD, isYK);
        }

    }

    /**
     * 显示注册
     */
    private final OnClickListener showRegisterClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            MCLog.w(TAG, ActivityConstants.Log_WzLLvLKrzB);
            isQuickLogin = true;
            registerDialog = new RegisterDialog.Builder()
                    .setRegisterCallback(registerCallback)
                    .setCloseToLogin(registerToLoginClick)
                    .show(mContext, ((Activity)mContext).getFragmentManager());
        }
    };

    /**
     * 显示登陆
     */
    private OnClickListener registerToLoginClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            showLoginDialog();
        }
    };

    private boolean isCheck = false;
    /**
     * 注册
     */
    private final PlatformRegisterCallback registerCallback = new PlatformRegisterCallback() {

        @Override
        public void platformRegister(String account, String password, String repassword, String code, int type,boolean isReadAgreement) {

            if (type == 0) {  //手机号注册方式
                if (checkPhoneRegister(account, code, password, isReadAgreement)) {
                    AppUtils.ShowLoadDialog(mContext);
                    RegisterProcess phoneRegprocess = new RegisterProcess();
                    phoneRegprocess.setType("2");
                    phoneRegprocess.setAccount(account);
                    phoneRegprocess.setCode(code);
                    phoneRegprocess.setPassword(password);
                    phoneRegprocess.post(registerHandler, mContext);
                }
            } else if (type == 1) { //账密注册
                if (checkAccountRegister(account, password, repassword, isReadAgreement)) {
                    AppUtils.ShowLoadDialog(mContext);
                    RegisterProcess normalRegprocess = new RegisterProcess();
                    normalRegprocess.setType("1");
                    normalRegprocess.setAccount(account);
                    normalRegprocess.setPassword(password);
                    normalRegprocess.setRepassword(repassword);
                    normalRegprocess.post(registerHandler, mContext);
                }
            } else if (type == 2) {  //邮箱注册方式
                if (checkEmailRegister(account, code, password, isReadAgreement)) {
                    AppUtils.ShowLoadDialog(mContext);
                    RegisterProcess phoneRegprocess = new RegisterProcess();
                    phoneRegprocess.setType("3");
                    phoneRegprocess.setAccount(account);
                    phoneRegprocess.setCode(code);
                    phoneRegprocess.setPassword(password);
                    phoneRegprocess.post(registerHandler, mContext);
                }
            }
        }

        @Override
        public void getPhoneValidateMessage(
                final View btnGetPhoneValidateMessage, String userName,
                RefreshVerifyCode refreshCode) {
        }
    };

    private OnClickListener loginCancel = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            exitThisActivity();
        }
    };

    private OnClickListener visitorsLogin = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            isQuickLogin = true;
            LoginModel.instance().startVisitorsLogin(mContext);
        }
    };

    /**
     * 第三方登录监听
     */
    OnClickListener thirdLoginClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            String tag = (String) v.getTag();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(ActivityConstants.S_XszfJwkZyn);
            dialog.show();
            LoginModel.instance().requestThirdLoginParams(tag, mContext, dialog);
        }
    };

    /**
     * 手机号码失去焦点
     */
    OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText phone = (EditText) v;
            String phonenum = phone.getText().toString().trim();
            boolean isPhone = checkRegister_getPhoneValidateMessage(phonenum);
            if (TextUtils.isEmpty(phonenum)) {
                ToastUtil.show(mContext, ActivityConstants.S_emvorrydrx);
                return;
            }
            if (isPhone) {
                CheckRequest checkRequest = new CheckRequest();
                checkRequest.context = mContext;
                checkRequest.postIsExit(phonenum);
            } else {
                ToastUtil.show(mContext, ActivityConstants.S_RRYDhQCufL);
            }
        }
    };


    private boolean isRegularAcc(String account) {
        String reg = Constant.REGULAR_ACCOUNT;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher_acc = pattern.matcher(account);
        boolean b_acc = matcher_acc.matches();
        if (!b_acc) {
            return false;
        }
        return true;
    }

    /**
     * 忘记密码
     */
    OnClickListener forgetPasswordClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            Intent intent = new Intent();
            intent.setClass(mContext, MCHForgetPasswordActivity.class);
            mContext.startActivity(intent);
        }
    };

    /**
     * 登录
     */
    PlatformLoginCallback mLoginCallback = new PlatformLoginCallback() {

        @Override
        public void platformLogin(String userName, String password,
                                  boolean isSavePwd) {
            if (checkLogin(userName, password)) {
                startUserLogin(userName, password, mContext);
            }
        }
    };

    /**
     * 返回按钮监听
     */
    OnKeyListener backKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            exitThisActivity();
            return false;
        }
    };

    /**
     * 退出当前页面
     */
    private void exitThisActivity() {
        LoginModel.instance().loginFail();
        MCHTransparencyActivity.instance.closeActivity();
    }

    /**
     * 用户信息检查，获取手机/邮箱验证码
     *
     * @param userName
     * @return
     */
    protected boolean checkRegister_getPhoneValidateMessage(String userName) {
        String phoneNumber = Constant.REGULAR_PHONENUMBER;
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.show(mContext, ActivityConstants.S_VGqfVanncV);
            return false;
        }
        if (!userName.matches(phoneNumber) && !Constant.REGULAR_MAIL(userName)) {
            ToastUtil.show(mContext, ActivityConstants.S_MpmwZgRbbx);
            return false;
        }
        return true;
    }

    /**
     * 注册成功
     * @param registerSuccess 返回值
     */
    private void registerSuccess(UserLogin registerSuccess) {
        if (!TextUtils.isEmpty(registerSuccess.getUserName()) && !TextUtils.isEmpty(registerSuccess.getPassword())) {
            LoginModel.instance().saveRegisterInfoToPre(true, registerSuccess.getUserName(), registerSuccess.getPassword());
            handlerLoginResult(registerSuccess);
        } else {
            ToastUtil.show(mContext, ActivityConstants.S_EgidcrRSwu);
        }
    }

    /**
     * 开始登陆
     *  @param username     用户名
     * @param password 密码
     */
    public void startUserLogin(String username, String password, Context context) {
        MCLog.w(TAG, "string_name = " + username);
        LoginProcess loginprocess = new LoginProcess(context);
        loginprocess.setAccount(username);
        loginprocess.setPassword(password);
        loginprocess.post(loginHandle);
    }
    /**
     * 自动登录开始登陆
     *  @param string_name     用户名
     * @param string_password 密码
     */
    public void startUserLogin(String string_name, String string_password, Activity context, boolean auto) {
        MCLog.w(TAG, "string_name = " + string_name);
        this.auto = auto;
        LoginProcess loginprocess = new LoginProcess(context);
        loginprocess.setAccount(string_name);
        loginprocess.setPassword(string_password);
        loginprocess.post(loginHandle);
    }

    /**
     * 登陆成功
     */
    protected void handlerLoginResult(UserLogin loginSuccess) {
        MCLog.w(TAG, "Account = " + loginSuccess.getUserName());
        if ("1".equals(loginSuccess.getLoginStatus())) {
            LoginModel.instance().loginSuccess(true, true, loginSuccess);
//            dismisDialog();
//			noticeResult(77);
        } else {
            LoginModel.instance().loginFail();
        }
    }

    /**
     * 调用此方法关闭登录对话框
     */
    public void dismissDialog() {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (loginDialog != null) {
                loginDialog.dismissAllowingStateLoss();
            }
            if (registerDialog != null) {
                registerDialog.dismissAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            MCLog.e(TAG, "dismisDialog error:" + e.toString());
        }
    }


    /**
     * 登录用户信息检查
     */
    public Boolean checkLogin(String str_account, String str_password) {
        if (TextUtils.isEmpty(str_account)) {
            ToastUtil.show(mContext, ActivityConstants.S_WqdODiyiKR);
            return false;
        }
        int length = str_account.trim().length();
        if (length < 6) {
            ToastUtil.show(mContext, ActivityConstants.S_FPAsQYEguT);
            return false;
        }
        if (TextUtils.isEmpty(str_password)) {
            ToastUtil.show(mContext, ActivityConstants.S_GASfraiZxd);
            return false;
        }
        if (!isRegularAcc(str_password)) {
            ToastUtil.show(mContext, ActivityConstants.S_OyCQAGvMai);
            return false;
        }
        return true;
    }

    /**
     * 账密注册信息检查
     */
    private Boolean checkAccountRegister(String account,String password,String rePassword,boolean isRead){
        if (TextUtils.isEmpty(account)){
            ToastUtil.show(mContext,ActivityConstants.S_WqdODiyiKR);
            return false;
        }
        if (account.length() < 6) {
            ToastUtil.show(mContext, ActivityConstants.S_FPAsQYEguT);
            return false;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtil.show(mContext, ActivityConstants.S_GASfraiZxd);
            return false;
        }
        if (!isRegularAcc(password)) {
            ToastUtil.show(mContext, ActivityConstants.S_OyCQAGvMai);
            return false;
        }
        if (TextUtils.isEmpty(rePassword)){
            ToastUtil.show(mContext, ActivityConstants.S_nkETSsYcrs);
            return false;
        }
        if (!password.equals(rePassword)){
            ToastUtil.show(mContext, ActivityConstants.S_vKizdmMDvV);
            return false;
        }
        if (!isRead){
            ToastUtil.show(mContext, ActivityConstants.S_rnONeDdIAR);
            return false;
        }
        return true;
    }

    /**
     * 邮箱注册信息检查
     */
    private Boolean checkEmailRegister(String email, String code,String password,boolean isRead){
        if (TextUtils.isEmpty(email)){
            ToastUtil.show(mContext, ActivityConstants.S_SBvDTAtrLE);
            return false;
        }
        if (!Constant.REGULAR_MAIL(email)) {
            ToastUtil.show(mContext, ActivityConstants.S_caQzACUPei);
            return false;
        }
        if (TextUtils.isEmpty(code)){
            ToastUtil.show(mContext, ActivityConstants.S_IOdRfWRGVz);
            return false;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtil.show(mContext, ActivityConstants.S_GASfraiZxd);
            return false;
        }
        if (!isRegularAcc(password)) {
            ToastUtil.show(mContext, ActivityConstants.S_OyCQAGvMai);
            return false;
        }
        if (!isRead){
            ToastUtil.show(mContext, ActivityConstants.S_rnONeDdIAR);
            return false;
        }
        return true;
    }

    /**
     * 手机号注册信息检查
     */
    private Boolean checkPhoneRegister(String phone,String code,String password,boolean isRead){
        String valCode = Constant.REGULAR_PHONENUMBER;
        if (TextUtils.isEmpty(phone)){
            ToastUtil.show(mContext, ActivityConstants.S_eFzqvNWObe);
            return false;
        }
        if (!phone.matches(valCode)) {
            ToastUtil.show(mContext, ActivityConstants.S_ONBlvdijLm);
            return false;
        }
        if (TextUtils.isEmpty(code)){
            ToastUtil.show(mContext, ActivityConstants.S_IOdRfWRGVz);
            return false;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtil.show(mContext, ActivityConstants.S_GASfraiZxd);
            return false;
        }
        if (!isRegularAcc(password)) {
            ToastUtil.show(mContext, ActivityConstants.S_OyCQAGvMai);
            return false;
        }
        if (!isRead){
            ToastUtil.show(mContext, ActivityConstants.S_rnONeDdIAR);
            return false;
        }
        return true;
    }
}
