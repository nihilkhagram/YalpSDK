package com.cointizen.paysdk.bean;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cointizen.open.ApiCallback;
import com.cointizen.open.FlagControl;
import com.cointizen.open.GPUserResult;
import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.activity.MCHChooseAccountActivity;
import com.cointizen.paysdk.activity.MCHNoticeDialogActivity;
import com.cointizen.paysdk.activity.MCHTransparencyActivity;
import com.cointizen.paysdk.bean.thirdlogin.BDThirdLogin;
import com.cointizen.paysdk.bean.thirdlogin.QQThirdLogin;
import com.cointizen.paysdk.bean.thirdlogin.WBThirdLogin;
import com.cointizen.paysdk.bean.thirdlogin.WXThirdLogin;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.floatview.MCHFloatHreadView;
import com.cointizen.paysdk.floatview.MCHFloatLoginView;
import com.cointizen.paysdk.http.process.NoticeProcess;
import com.cointizen.paysdk.http.process.SmallAccountLoginProgress;
import com.cointizen.paysdk.http.thirdlogin.ThirdLoginProcess;
import com.cointizen.paysdk.entity.ThirdLoginParamsBean;
import com.cointizen.paysdk.http.thirdloginparams.ThirdLoginParamsProcess;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.utils.DeviceInfo;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.PreSharedManager;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.utils.TextUtils;

import java.util.List;

/**
 * Created by zhujinzhujin
 * on 2017/1/7.
 */

public class LoginModel {

    private static final String TAG = "LoginModel";


    private static final String LOGINTYPE_WB = "wb";
    private static final String LOGINTYPE_QQ = "qq";
    private static final String LOGINTYPE_WX = "wx";
    private static final String LOGINTYPE_BD = "bd";
    private static final String LOGINTYPE_YK = "yk";

    private Context context;
    private static LoginModel loginModel;
    private boolean canShowNoticeDialog = true;
    /**
     * 掉登录控件的等待对话框
     */
    ProgressDialog dialog;


    public static LoginModel instance() {
        if (null == loginModel) {
            loginModel = new LoginModel();
        }
        return loginModel;
    }

    public LoginModel() {
    }


    private final Handler mTrirdLoginHandle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.USER_GET_PARAMS_SUCCESS://获得第三方登录需要的参数
                    ThirdLoginParams(msg.obj);
                    break;
                case Constant.USER_GET_PARAMS_FAIL:
                    dissmissDialog();
                    MCLog.e(TAG, BeanConstants.Log_dGzYuJKUxP + msg.obj);
                    break;
                case Constant.USER_THIRD_PARAMS_SUCCESS://第三方登录成功
                    loginSuccess(true, true, (UserLogin) msg.obj);
                    break;
                case Constant.USER_THIRD_PARAMS_FAIL://第三方登录失败
                    if (!TextUtils.isEmpty(msg.obj.toString())){
                        ToastUtil.show(YalpGamesSdk.getMainActivity(),msg.obj.toString());
                    }
                    loginFail();
                    break;
                case Constant.NOTICE_SUCCESS:
                    canShowNoticeDialog = true;
                    NoticeModel obj = (NoticeModel) msg.obj;
                    if(obj.getList().size() > 0){
                        MCLog.e(TAG,BeanConstants.Log_NBIisAkTRa);
                        Intent intent = new Intent(YalpGamesSdk.getMainActivity(), MCHNoticeDialogActivity.class);
                        intent.putExtra("NoticeModel",obj);
                        YalpGamesSdk.getMainActivity().startActivity(intent);
                    } else{
                        Constant.showedNoteDialog = true;
                    }
                    break;
                case Constant.NOTICE_FAIL:
                    canShowNoticeDialog = true;
                    Constant.showedNoteDialog = true;
                    MCLog.e(TAG, BeanConstants.Log_XQncLoogIn + msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    public boolean isLogin() {
        String userId = UserLoginSession.getInstance().getUserId();
        String account = UserLoginSession.getInstance().getAccount();
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(userId);
    }

    /**
     * 发起游客登录
     */
    public void startVisitorsLogin(Context context) {
        String ykAccout = PreSharedManager.getString(Constant.CUSTOMER_YK, context);

        ThirdLoginProcess process = new ThirdLoginProcess();
        process.thirdLoginType = ThirdLoginProcess.THIRDLOGIN_YK;
        process.ykAccount = ykAccout;
        process.ykPassword = PreSharedManager.getString(Constant.CUSTOMER_YK_PASSWORD, context);
        process.setContext(context);
        process.post(mTrirdLoginHandle);
    }


    public void requestThirdLoginParams(String thirdLoginType, Context context, ProgressDialog mDialog) {
        this.context = context;
        if (mDialog != null) {
            dialog = mDialog;
        }
        ThirdLoginParamsProcess p = new ThirdLoginParamsProcess();
        MCLog.w(TAG, "thirdLoginType:" + thirdLoginType);
        if (LOGINTYPE_WB.equals(thirdLoginType)) {
            p.login_type = LOGINTYPE_WB;

        } else if (LOGINTYPE_QQ.equals(thirdLoginType)) {
            p.login_type = LOGINTYPE_QQ;
        } else if (LOGINTYPE_WX.equals(thirdLoginType)) {
            p.login_type = LOGINTYPE_WX;
            //判断是否安装微信
            boolean isWxAvaliable = DeviceInfo.isWeixinAvilible(context);
            if (!isWxAvaliable) {
                dissmissDialog();
                ToastUtil.show(context, BeanConstants.S_zVfRaMcNww);
                return;
            }
        } else if (LOGINTYPE_BD.equals(thirdLoginType)) {
            p.login_type = LOGINTYPE_BD;
        }
        p.post(mTrirdLoginHandle);
    }

    private void ThirdLoginParams(Object obj) {
        ThirdLoginParamsBean bean = (ThirdLoginParamsBean) obj;
        //跳转到控件调用第三方登录SDK
        if (LOGINTYPE_WX.equals(bean.login_type)) {
            if (!TextUtils.isEmpty(bean.weixinappid)) {
                WXThirdLogin.Instance().lunchWXLogin(bean.weixinappid);
                // dialog.dismiss();
            } else {
                loginFail();
                MCLog.e(TAG, "wxappid is null!");
            }
        } else if (LOGINTYPE_QQ.equals(bean.login_type)) {
            if (!TextUtils.isEmpty(bean.qqappid)) {
//                MCLog.w(TAG, "qqappid:" + bean.qqappid);
                QQThirdLogin.Instance().lunchQQLogin(bean.qqappid);
            } else {
                loginFail();
                MCLog.e(TAG, "qqappid is null!");
            }
        } else if (LOGINTYPE_WB.equals(bean.login_type)) {
            if (!TextUtils.isEmpty(bean.weiboappkey)) {
                //dialog.dismiss();
                WBThirdLogin.Instance().lunchWBLogin(
                        bean.weiboappkey, bean.redirecturl, bean.wbscope);
            } else {
                loginFail();
                MCLog.e(TAG, "weiboappkey is null!");
            }
        } else if (LOGINTYPE_BD.equals(bean.login_type)) {
            if (!TextUtils.isEmpty(bean.bdclientid)) {
                MCLog.w(TAG, "bdclientid:" + bean.bdclientid);
                BDThirdLogin.Instance().lunchBDLogin(bean.bdclientid);
            } else {
                loginFail();
                MCLog.e(TAG, "bdclientid is null!");
            }
        } else {
            loginFail();
            MCLog.e(TAG, BeanConstants.Log_LkxlLtvUlQ);
        }
    }


    /**
     * 登录失败
     */
    public void loginFail(){
        GPUserResult GPUserResult = new GPUserResult();
        GPUserResult.setmErrCode(GPUserResult.USER_RESULT_LOGIN_FAIL);
        GPUserResult.setAccountNo("");
        if (ApiCallback.getLoginCallback() != null){
            ApiCallback.getLoginCallback().onFinish(GPUserResult);
        }
        FlagControl.isLogin = false;
        dissmissDialog();
    }


    /**
     * 平台账号登录成功
     * @param isSaveUserInfo 用于是否保存用户信息
     * @param isSavePassword 用于是否保存密码
     * @param loginSuccess   实体类
     */
    public void loginSuccess(boolean isSaveUserInfo, boolean isSavePassword, UserLogin loginSuccess) {
        this.context = YalpGamesSdk.getMainActivity();
        if ("1".equals(loginSuccess.getLoginStatus())) {
            //登录窗口记住账号密码
            if (isSaveUserInfo) {
                if (loginSuccess.isYKLogin()) {
                    MCLog.e(BeanConstants.Log_BNkRxnumTD,""+ loginSuccess.getUserName());
                    PreSharedManager.setString(Constant.CUSTOMER_YK, loginSuccess.getUserName(), context);
                    PreSharedManager.setString(Constant.CUSTOMER_YK_PASSWORD, loginSuccess.getPassword(), context);
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setAccount(loginSuccess.getUserName());
                    PreSharedManager.saveUserInfoList(context, userInfoBean);
                } else {
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setAccount(loginSuccess.getUserName());
                    userInfoBean.setPwd(loginSuccess.getPassword());
                    PreSharedManager.saveUserInfoList(context, userInfoBean);
                }
            }

            UserLoginSession.getInstance().getChannelAndGame().setAccount(loginSuccess.getUserName());
            UserLoginSession.getInstance().getChannelAndGame().setPassword(loginSuccess.getPassword());
            UserLoginSession.getInstance().getChannelAndGame().setUserId(loginSuccess.getAccountUserId());
            UserLoginSession.getInstance().getChannelAndGame().setToken(loginSuccess.getToken());
//            UserLoginSession.getInstance().getChannelAndGame().setIs_uc(loginSuccess.getIs_uc());

            if(!loginSuccess.isYKLogin()){
                if (loginSuccess.getIsOpenSmallAccount() == 1){
                    Constant.IsOpenSmallAccount = true;
                    Intent intent = new Intent(YalpGamesSdk.getMainActivity(), MCHChooseAccountActivity.class);
                    intent.putExtra("user_small_list", loginSuccess);
                    YalpGamesSdk.getMainActivity().startActivity(intent);
                }else {
                    Constant.IsOpenSmallAccount = false;
                    smallAccountLogin(loginSuccess);
                }

            }
        }else {
            loginFail();
        }
    }

    private void IsShowNoticeDialog(){
        NoticeProcess noticeProcess = new NoticeProcess(YalpGamesSdk.getMainActivity());
        noticeProcess.post(mTrirdLoginHandle);
    }


    /**
     * 小号登录
     * @param userInfo
     */
    public void smallAccountLogin(UserLogin userInfo){
        List<UserLogin.SmallAccountEntity> list = userInfo.getSmallAccountList();
        if (list.size() <= 0) {
            loginFail();
            return;
        }
        UserLogin.SmallAccountEntity smallAccountEntity = list.get(0);
        SharedPreferencesUtils.getInstance().setLastLoginID(YalpGamesSdk.getMainActivity(), smallAccountEntity.getSmallUserId());
        SmallAccountLoginProgress progress = new SmallAccountLoginProgress();
        progress.setSmallUserId(smallAccountEntity.getSmallUserId());
        progress.setUserId(userInfo.getAccountUserId());
        progress.setGameId(SdkDomain.getInstance().getGameId());
        progress.setYKLogin(userInfo.isYKLogin());
        progress.setToken(userInfo.getToken());
        progress.post(mHandler);
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.SMALL_ACCOUNT_LOGIN_SUCCESS:  //小号登录成功
                    UserLogin smallAccountLogin = (UserLogin) msg.obj;
                    smallAccountLoginSuccess(smallAccountLogin);
                    break;
                case Constant.SMALL_ACCOUNT_LOGIN_FAIL:  //小号登录失败
                    String tip = (String) msg.obj;
                    ToastUtil.show(YalpGamesSdk.getMainActivity(), tip);
                    LoginModel.instance().loginFail();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 登录游戏成功
     * @param loginSuccess
     */
    public void smallAccountLoginSuccess(UserLogin loginSuccess){
        GPUserResult userResult = new GPUserResult();
        if ("1".equals(loginSuccess.getLoginStatus())) {
            MCHFloatHreadView.getInstance(YalpGamesSdk.getMainActivity(),3000).show();
            //保存用户信息（用于登录回调发给CP）
            userResult.setmErrCode(GPUserResult.USER_RESULT_LOGIN_SUCC);
            userResult.setAccountNo(loginSuccess.getAccountUserId());
            userResult.setExtra_param(loginSuccess.getExtra_param());
            userResult.setToken(loginSuccess.getToken());

            //保存小号信息（用于请求防沉迷及个人中心等）
            UserLoginSession.getInstance().getChannelAndGame().setSmallAccountUserId(loginSuccess.getAccountUserId());
            UserLoginSession.getInstance().getChannelAndGame().setSmallAccountToken(loginSuccess.getToken());

            //登录成功后设置个标记
            FlagControl.isLogin = true;

            if (ApiCallback.getLoginCallback() != null) {
                ApiCallback.getLoginCallback().onFinish(userResult);
            }

            if (SwitchManager.getInstance().floating()) {
                YalpGamesSdk.getYalpGamesSdk().startFloating(context);
                FlagControl.isFloatingOpen = true;
            }else {
                YalpGamesSdk.getYalpGamesSdk().stopFloating(context);
                FlagControl.isFloatingOpen = false;
            }

            if(MCHTransparencyActivity.instance != null){
                MCHTransparencyActivity.instance.closeActivity();
            }
            if(canShowNoticeDialog) {
                canShowNoticeDialog = false;
                IsShowNoticeDialog();
            }

            String account = UserLoginSession.getInstance().getChannelAndGame().getAccount();
            MCHFloatLoginView.getInstance(YalpGamesSdk.getMainActivity(),1000, account,true).show();

        } else {
            loginFail();
        }
    }


    public void saveRegisterInfoToPre(boolean isSavePassword, String account, String pwd) {
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(account);
        userLogin.setPassword(pwd);
        userLogin.setAccountUserId("");
        userLogin.setYKLogin(false);
        saveUserInfoToPre(true, isSavePassword, userLogin);
    }

    /**
     * 更新当前用户
     * @param loginSuccess 当前登录用户信息
     */
    public void saveUserInfoToPre(boolean isSaveUserInfo, boolean isSavePassword, final UserLogin loginSuccess) {
        Context context = YalpGamesSdk.getMainActivity();
        if (null == context) {
            return;
        }
        MCLog.w(TAG, "#saveUserInfoToPre name = " + loginSuccess.getUserName() + ", userId = " + loginSuccess.getAccountUserId());
        // 保存用户名和密码
        UserLoginSession.getInstance().getChannelAndGame().setAccount(loginSuccess.getUserName());
        UserLoginSession.getInstance().getChannelAndGame().setPassword(loginSuccess.getPassword());
        UserLoginSession.getInstance().getChannelAndGame().setUserId(loginSuccess.getAccountUserId());
//        UserLoginSession.getInstance().getChannelAndGame().setIs_uc(loginSuccess.getIs_uc());

        if (!isSaveUserInfo) {
            return;
        }

        //登录窗口记住密码
        if (loginSuccess.isYKLogin()) {
            PreSharedManager.setString(Constant.CUSTOMER_YK, loginSuccess.getUserName(), context);
            PreSharedManager.setString(Constant.CUSTOMER_YK_PASSWORD, loginSuccess.getPassword(), context);
        } else {
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setAccount(loginSuccess.getUserName());
            userInfoBean.setPwd(loginSuccess.getPassword());
            PreSharedManager.saveUserInfoList(context, userInfoBean);
        }
    }

    public void dissmissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
