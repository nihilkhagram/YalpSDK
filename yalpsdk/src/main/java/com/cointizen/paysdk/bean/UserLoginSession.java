package com.cointizen.paysdk.bean;

import com.cointizen.open.FlagControl;
import com.cointizen.open.YalpGamesSdk;
import com.cointizen.open.LogoutCallback;
import com.cointizen.paysdk.dialog.DialogUtil;
import com.cointizen.paysdk.dialog.SdkLogoutDialog;
import com.cointizen.paysdk.entity.ChannelAndGameInfo;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;

public class UserLoginSession {

    private ChannelAndGameInfo channelAndGame = null;

    private boolean isUpdatePwd;

    private static UserLoginSession instance;

    public static UserLoginSession getInstance() {
        if (null == instance) {
            instance = new UserLoginSession();
        }
        return instance;
    }

    private UserLoginSession() {
        channelAndGame = new ChannelAndGameInfo();
        isUpdatePwd = false;
    }


    public ChannelAndGameInfo getChannelAndGame() {
        return this.channelAndGame;
    }
    public void clearUserInfoAll(){
        channelAndGame.setClearAll();
    }

    public String getAccount() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getAccount();
    }
    public String getEmail() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.geteMail();
    }

    public String getIs_uc() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getIs_uc();
    }

    public String getReal_name() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getReal_name();
    }

    public String getIdcard() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getIdcard();
    }

    public String getGameName() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getGameName();
    }

    public String getPhone() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getPhoneNumber();
    }

    public String getUserPtb() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getPlatformMoney() + "";
    }

    public String getBindPtb() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getBindPtbMoney() + "";
    }

    public void setPhone(String phone) {
        if (null != channelAndGame && !TextUtils.isEmpty(phone)) {
            channelAndGame.setPhoneNumber(phone);
        }
    }

    public boolean isUpdatePwd() {
        return isUpdatePwd;
    }

    public void setUpdatePwd(boolean isUpdatePwd) {
        this.isUpdatePwd = isUpdatePwd;
    }

    public String getUserId() {
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getUserId();
    }

    public String getToken(){
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getToken();
    }

    public String getSmallAccountToken(){
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getSmallAccountToken();
    }

    public String getSmallAccountUserID(){
        if (null == channelAndGame) {
            return "";
        }
        return channelAndGame.getSmallAccountUserId();
    }

    public void clearUserLoginInfo(final Activity context, final LogoutCallback logoutCallback) {
        if (!LoginModel.instance().isLogin()) {
            ToastUtil.show(context, BeanConstants.S_user_not_logged_in);
            return;
        }
        InitModel.init().offLine(context, true);
        channelAndGame = new ChannelAndGameInfo();
        if (logoutCallback != null) {
            try {
                SharedPreferencesUtils.getInstance().setLoggedOut(context,true);     //注销状态设为trueThread.sleep(500);
                UserLoginSession.getInstance().clearUserInfoAll();
                Thread.sleep(100);

                logoutCallback.logoutResult("1");
                FlagControl.BUTTON_CLICKABLE = true;
                FlagControl.isLogin = false;
                FlagControl.isFloatingOpen = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearUserLoginInfoShowDialog(final Activity activity, final LogoutCallback logoutCallback) {
        Activity context = activity;
        if (context == null) {
            context = YalpGamesSdk.getMainActivity();
        }
        if (!LoginModel.instance().isLogin()) {
            ToastUtil.show(context, BeanConstants.S_user_not_logged_in);
            return;
        }

        if(SwitchManager.getInstance().logoutAdvert()){
            new SdkLogoutDialog.Builder()
                    .setLogoutCallback(logoutCallback)
                    .setIsExitActivity(activity != null)
                    .show(context, context.getFragmentManager());
        }else{
            final Activity finalContext = context;
            Dialog dialog = DialogUtil.mch_alert_msg(context, BeanConstants.S_JogEDDkYSt, BeanConstants.S_COIxdiuKbO, context, BeanConstants.S_NSPNbmsODc, BeanConstants.S_OWVPwUHfhQ, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InitModel.init().offLine(finalContext, true);
                    channelAndGame = new ChannelAndGameInfo();
                    if (logoutCallback != null) {
                        try {
                            SharedPreferencesUtils.getInstance().setLoggedOut(finalContext,true);     //注销状态设为trueThread.sleep(500);
                            UserLoginSession.getInstance().clearUserInfoAll();
                            Thread.sleep(100);
                            if (activity != null) {
                                activity.finish();
                            }

                            logoutCallback.logoutResult("1");
                            FlagControl.BUTTON_CLICKABLE = true;
                            FlagControl.isLogin = false;
                            FlagControl.isFloatingOpen = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dialog.show();
        }

    }
}
