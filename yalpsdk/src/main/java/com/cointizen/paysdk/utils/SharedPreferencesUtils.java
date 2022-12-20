package com.cointizen.paysdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 描述：SharedPreferencesUtils
 * 时间: 2018-08-28 16:36
 */

public class SharedPreferencesUtils {

    private static SharedPreferencesUtils instance;
    private boolean autoLogin = true;

    public static SharedPreferencesUtils getInstance() {
        if (null == instance) {
            instance = new SharedPreferencesUtils();
        }
        return instance;
    }

    public void initAutoLogin(boolean auto) {
        autoLogin = auto;
    }

    public boolean getAutoLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isAutoLogin", autoLogin);
    }

    public void setAutoLogin(Context context, boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAutoLogin", autoLogin);
        editor.commit();
    }

    /**
     * 上次是否注销登录
     *
     * @return
     */
    public boolean isLoggedOut(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("IsLoggedOut", false);
    }

    public void setLoggedOut(Context context, boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsLoggedOut", autoLogin);
        editor.commit();
    }

    /**
     * 是否第三方登录
     *
     * @return
     */
    public boolean isThirdPartyLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("IsThirdPartyLogin", false);
    }

    public void setIsThreeLogin(Context context, boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsThirdPartyLogin", autoLogin);
        editor.commit();
    }


    /**
     * 点击隐藏悬浮球  是否弹窗提示
     *
     * @return
     */
    public boolean getIsHiddenOrb(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("IsHiddenOrb", false);
    }

    public void setIsHiddenOrb(Context context, boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsHiddenOrb", autoLogin);
        editor.commit();
    }

    /**
     * 我的页面  第一次新手引导
     *
     * @return
     */
    public boolean getIsGuess(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("IsGuess", true);
    }

    public void setIsGuess(Context context, boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsGuess", autoLogin);
        editor.commit();
    }

    /**
     * 是否是用户邀请的
     *
     * @return
     */
    public boolean getLaunch(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("Launch", true);
    }

    public void setLaunch(Context context, boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Launch", autoLogin);
        editor.commit();
    }


    /**
     * 是否是用户邀请的
     *
     * @return
     */
    public boolean getIsFirstOpen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("firstOpen", true);
    }

    public void setIsFirstOpen(Context context, boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstOpen", autoLogin);
        editor.commit();
    }

    /**
     * 是否展示引导
     *
     * @return
     */
    public boolean getIsGuide(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("guide", true);
    }

    public void setIsGuide(Context context, boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("guide", autoLogin);
        editor.commit();
    }


    /**
     * 上次登录的小号信息
     *
     * @return
     */
    public void setLastLoginID(Context context, String url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastLogin", url);
        editor.commit();
    }

    public String getLastLoginID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        String skinUrl = sharedPreferences.getString("lastLogin", "");
        return skinUrl;
    }


    /**
     * 游客的密码信息
     *
     * @return
     */
    public void setYKPassword(Context context, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.commit();
    }

    public String getYKPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        String skinUrl = sharedPreferences.getString("password", "");
        return skinUrl;
    }


    /**
     * 当前登录的帐号信息
     *
     * @return
     */
    public void setLoginAccount(Context context, String account) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LoginAccount", account);
        editor.commit();
    }

    public String getLoginAccount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        String str = sharedPreferences.getString("LoginAccount", "");
        return str;
    }

    public void setToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LoginToken", token);
        editor.commit();
    }

    public String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        String str = sharedPreferences.getString("LoginToken", "");
        return str;
    }

    /**
     * 当前登录的密码信息
     *
     * @return
     */
    public void setLoginPassword(Context context, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LoginPassword", password);
        editor.commit();
    }

    public String getLoginPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        String skinUrl = sharedPreferences.getString("LoginPassword", "");
        return skinUrl;
    }

    /**
     * 判断是不是第一次初始化（隐私授权）
     */
    public static boolean getIsFirstPrivacy(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("yalpgameprivvacy", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("yalpgameprivvacy", true);
    }

    public static void setIsFirstPrivacy(Context context, boolean isFirst) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("yalpgameprivvacy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("yalpgameprivvacy", isFirst);
        editor.commit();
    }

    /**
     * 隐私信息收集
     */
    public boolean getPrivacyCollect(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("yalpprivvacycollect", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("privvacycollect", false);
    }

    public void setPrivacyCollecty(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("yalpprivvacycollect", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("privvacycollect", true);
        editor.commit();
    }

}
