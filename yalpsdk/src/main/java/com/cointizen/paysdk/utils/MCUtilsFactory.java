package com.cointizen.paysdk.utils;

import android.app.Activity;

/**
 * Created by Administrator on 2017/5/10.
 * 手机详细信息
 */

public class MCUtilsFactory {

    private static final String TAG = "MCUtilsFactory";
    private static MCUtilsFactory instance;
    private static Activity activity;

    public MCUtilsFactory() { }

    public static MCUtilsFactory getInstance(Activity act) {
        activity = act;
        if (instance == null) {
            instance = new MCUtilsFactory();
        }
        return instance;
    }


    /**
     * 获取机型
     */
    public String getPhoneModel() {
        String brand = android.os.Build.BRAND;//手机品牌
        String model = android.os.Build.MODEL;//手机型号
        MCLog.w(TAG, UtilsConstants.Log_VrvVaRdUlk + brand + " " + model);
        return brand + " " + model;
    }

    /**
     * 获取操作系统
     *
     * @return
     */
    public String getOS() {
        MCLog.w(TAG, UtilsConstants.Log_PnjbCRNHfk + "Android" + android.os.Build.VERSION.RELEASE);
        return "Android" + android.os.Build.VERSION.RELEASE;
    }




}
