package com.cointizen.open;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.cointizen.paysdk.activity.MCHLoginActivity;
import com.cointizen.paysdk.activity.MCHTransparencyActivity;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.channel.sign2.WalleChannelReader;
import com.cointizen.paysdk.floatview.MCHFloatView;
import com.cointizen.paysdk.http.process.DeviceOnlineProcess;
import com.cointizen.paysdk.service.ServiceManager;
import com.cointizen.paysdk.utils.AppStatus;
import com.cointizen.paysdk.utils.AppStatusManager;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.bean.InitModel;
import com.cointizen.paysdk.bean.MCPayModel;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.bean.UploadRole;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.DialogUtil;
import com.cointizen.paysdk.dialog.HideBallDialog;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.ScreenshotUtils;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.util.StoreSettingsUtil;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.plugin.qg.utils.UpdateUtils;
import com.cointizen.streaming.ChannelsListActivity;
import com.cointizen.streaming.WatchLiveStreamingActivity;

public class YalpGamesSdk {

    private static final String TAG = "YalpGamesSdk";

    private static Activity mainActivity;
    private static Context mainContext;

    private boolean isLogining = false;
    /**
     * 退出回调接口
     */
    private IGPExitObsv exitObsv;

    /**
     * 个人中心退出回调接口，判断用不能合并
     */
    private UserLoginCallback loginCallback;

    private LogoutCallback logoutCallback;

    private boolean floatingIsShow = false;
    private boolean showFloatingButtonAtLogin = false;

    private static YalpGamesSdk instance;

    public static YalpGamesSdk getYalpGamesSdk() {
        if (null == instance) {
            instance = new YalpGamesSdk();
        }
        return instance;
    }

    public static void initialize(Context context) {
        getYalpGamesSdk().init(context, false);
    }

    private YalpGamesSdk() { }

    public static Activity getMainActivity() {
        return mainActivity;
    }

    public String version() {
        return "9.7.0.2";
    }

    /**
     * @param application Application
     */
    public static void initApplication(Application application) { }

    public void init(Context con) {
        init(con, true, null);
    }

    public void init(Context con, IGPSDKInitObsv initObsv) {
        init(con, true, initObsv);
    }

    public void init(final Context con, boolean isDebug) {
        init(con, isDebug, null);
    }

    public void init(final Context con, boolean isDebug, IGPSDKInitObsv initObsv) {
        MCLog.s("sdk", "-- init --");
        if (null == con) {
            MCLog.e(TAG, AppConstants.LOG_5bfab62832ebe842095bd729523cd8ae);
            return;
        }
        MCLog.isDebug = isDebug;
        mainActivity = (Activity) con;
        mainContext = con;
        ServiceManager.getInstance().registerService(mainActivity);
        AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL);
        String channel = WalleChannelReader.getChannel(con);
        StoreSettingsUtil.init(channel);
        InitModel.init().doInit(con, initObsv);
        UpdateUtils.getInstance().checkForUpdates(mainContext);

    }

    public void startLogin(Activity context){
        MCLog.e(TAG, "startlogin-" + isLogining);
        if (isLogining) {
            return;
        }
        isLogining = true;
        if (null == context) {
            MCLog.e(TAG, "fun#login context is null");
            return;
        }
        mainActivity = context;

        if (!SdkDomain.getInstance().haveReadGameInfo()) {
            MCLog.e(TAG, AppConstants.LOG_53943194c5c5cbf7eb0e277d93feba9f);
            ToastUtil.show(context, AppConstants.STR_32d8bcf1e7e3ecd6122189888a6fc001);
            return;
        }

        if(LoginModel.instance().isLogin()){
            MCLog.e(TAG, AppConstants.LOG_2d678665c3154a66b66d5bdb2c235472);
            return;
        }
        Constant.showedNoteDialog = false;

        boolean autoLogin = SharedPreferencesUtils.getInstance().getAutoLogin(context);
        if(autoLogin && !SharedPreferencesUtils.getInstance().isLoggedOut(context) &&
                !SharedPreferencesUtils.getInstance().isThirdPartyLogin(context)){
            //如果开启了自动登录、上次没有注销登录、上次不是QQ微信第三方快捷登录、没有开启极验，则调用自动登录
            autoLogin(context);
        }else{
            //调用登录接口
            login(context);
        }
    }

    public static void setLoginCallback(UserLoginCallback mUserObsv) {
        getYalpGamesSdk().loginCallback = mUserObsv;
    }

    /**
     * sdk 登录
     */
    private void login(Activity context) {
        MCLog.e(TAG, "startlogin-login");
        if (null == context) {
            MCLog.e(TAG, "fun#startlogin context is null");
            return;
        }
        mainActivity = context;
        Intent loginIntent = new Intent(context, MCHTransparencyActivity.class);
        mainActivity.startActivity(loginIntent);
    }


    /**
     * sdk 自动登录
     */
    private void autoLogin(final Activity activity) {
        if (null == activity) {
            MCLog.e(TAG, "fun#startlogin context is null");
            return;
        }
        mainActivity = activity;

        String account = SharedPreferencesUtils.getInstance().getLoginAccount(activity);
        String pwd = SharedPreferencesUtils.getInstance().getLoginPassword(activity);
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)){
            MCLog.w(AppConstants.LOG_b2d790cee9d852f298465961eae48887,AppConstants.LOG_d7a47ec7e88c674a38a455802f95b667+account);
            MCHLoginActivity loginActivity = new MCHLoginActivity(activity);
            loginActivity.startUserLogin(account,pwd,activity,true);
        }else {
            login(activity);
        }
    }

    /**
     * 验证登录
     */
    public boolean isLogin() {
        return LoginModel.instance().isLogin();
    }

    /**
     * 注销
     */
    public static void logout(Activity context) {
        if (ServiceManager.getInstance().isBaiDuYunOS){
            return;
        }
        UserLoginSession.getInstance().clearUserLoginInfo(context, getYalpGamesSdk().logoutCallback);
    }

    /**
     * 弹窗注销
     */
    public static void exitPopup(Activity context) {
        if (ServiceManager.getInstance().isBaiDuYunOS){
            return;
        }
        YalpGamesSdk.getYalpGamesSdk().isLogining = false;//切换账号
        UserLoginSession.getInstance().clearUserLoginInfoShowDialog(context,
                YalpGamesSdk.getYalpGamesSdk().logoutCallback);
    }

    public void startNewScreen(View view) {
        Intent intent = new Intent(mainContext, WatchLiveStreamingActivity.class);
        mainActivity.startActivity(intent);
    }

    public void joinStream(View view) {
        Intent intent = new Intent(mainActivity, ChannelsListActivity.class);
        mainActivity.startActivity(intent);
    }


    /**
     * 退出
     */
    public static void exitDialog(Context context, IGPExitObsv exitObsv) {
        if (ServiceManager.getInstance().isBaiDuYunOS){
            return;
        }
        if (null == context) {
            MCLog.e(TAG, "fun#startlogin context is null");
            return;
        }
        if (null != exitObsv) {
            getYalpGamesSdk().exitObsv = exitObsv;
        } else {
            getYalpGamesSdk().exitObsv = null;
            MCLog.w(TAG, "fun#startlogin IGPExitObsv is null");
        }
        DialogUtil.mch_alert_exit((Activity) context);
    }

    /**
     * 开始支付
     *
     * @param orderInfo 商品信息
     * @param pck       支付回调
     */
    public void pay(Context context, OrderInfo orderInfo, PaymentCallback pck) {
        MCLog.e(TAG, orderInfo.toString());
        MCPayModel.Instance().pay(context, orderInfo, pck);
    }

    /**
     * 角色信息上传
     * @param roleCallBack 回调
     */
    public void uploadCharacter(RoleInfo roleinfo, UploadCharacterCallBack roleCallBack) {
        UploadRole uploadRole = new UploadRole(roleinfo, roleCallBack);
        uploadRole.upload();
    }

    public static void allowPrivacy(Activity activity, PrivacyCallback privacyCallback) {
        if (activity == null){
            MCLog.e(TAG,"activity is null");
            return;
        }
        PrivacyManager.getInstance().allowPrivacy(activity, privacyCallback);
    }


    @SuppressLint("NewApi")
    public void startFloating(final Context context) {
        if (!floatingIsShow && HideBallDialog.IsShow) {
            floatingIsShow = true;
            MCLog.i(TAG, "fun#startFloating");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MCHFloatView.getInstance(mainActivity).show();
                }
            });
        }
    }

    public void stopFloating(Context context) {
        if (floatingIsShow) {
            floatingIsShow = false;
            MCLog.i(TAG, "fun#stopFloating");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MCHFloatView.getInstance(mainActivity).close();
                }
            });
        }
    }

    /**
     * 游戏从后台开始，开始计时
     */
    public void onResume(Activity context) {

        //请求设备上线
        DeviceOnlineProcess deviceOnlineProcess = new DeviceOnlineProcess(context);
        deviceOnlineProcess.post();

        if (FlagControl.isLogin && FlagControl.isFloatingOpen&&!showFloatingButtonAtLogin) {
            MCLog.w(TAG,AppConstants.LOG_0c867a5167b9d56a769e454698eb97f9);
            startFloating(context);
        }
    }

    public void onPause(Activity context) {
        YalpGamesSdk.getYalpGamesSdk().stopFloating(context);  //关闭悬浮球
    }

    /**
     * 游戏进入后台或退出游戏时，请求下线
     */
    public void onStop(final Activity activity) {
        if (!AppUtils.isAppOnForeground(activity)){
            MCLog.w(TAG,AppConstants.LOG_5f93ce44fa8c6d339d90bbcc1d1aa8b1);
            InitModel.init().offLine(activity, false);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ScreenshotUtils.getInstance().ActivityResult(requestCode,resultCode,data);
    }

    public void onDestroy() {
        ServiceManager.getInstance().unregisterReceiver(mainActivity);
        ScreenshotUtils.getInstance().onDestroy();
        if(DialogUtil.lDialog!=null&&DialogUtil.lDialog.isShowing()){
            DialogUtil.lDialog.dismiss();
        }
    }

    public UserLoginCallback getLoginCallback() {
        isLogining = false; //登录结束
        return loginCallback;
    }

    protected IGPExitObsv getExitObsv() {
        return exitObsv;
    }

    public static void setLogoutCallback(LogoutCallback logoutCallback) {
        getYalpGamesSdk().logoutCallback = logoutCallback;
    }

    public LogoutCallback getLogoutCallback(){
        if (logoutCallback!=null){
            return logoutCallback;
        }
        return null;
    }

    public void startNewLogin(Activity activity) {

    }
}
