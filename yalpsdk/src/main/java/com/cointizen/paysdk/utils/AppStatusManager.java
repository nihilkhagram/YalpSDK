package com.cointizen.paysdk.utils;

/**
 * 描述：
 * 时间: 2018-11-07 14:02
 */

public class AppStatusManager {
    public int appStatus = AppStatus.STATUS_RECYVLE;    //APP状态 初始值为不在前台状态

    public static AppStatusManager appStatusManager;

    private AppStatusManager(){}

    //单例模式
    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppStatusManager();
        }
        return appStatusManager;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}
