package com.cointizen.paysdk.utils;

import android.util.Log;

import com.cointizen.open.YalpGamesSdk;

public class MCLog {

    public static boolean isDebug = true;

    public static final int FLAG_NONE = 0x000;

    public static final int FLAG_THREAD_NAME = 0x001;

    private static final String TAG = "SDK" + YalpGamesSdk.getYalpGamesSdk().version() + "_yn";

    public static void s(String tag, String msg) {
        Log.w(TAG, "SDK");
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(TAG, getExtraInfoWithTag(tag, msg));
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(TAG, getExtraInfoWithTag(tag, msg));
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(TAG, getExtraInfoWithTag(tag, msg));
        }
    }


    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(TAG, getExtraInfoWithTag(tag, msg));
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(TAG, getExtraInfoWithTag(tag, msg));
        }
    }


    private static String getExtraInfoWithTag(String tag, String msg) {
        return tag + ":" + msg;
    }


//    public static class Tag {
//
//        public int flag;
//
//        public String tag;
//
//        public int level;
//
//        public Tag(int flag, String tag, int level) {
//            //TODO  flag,tag和level的错误处理
//            this.flag |= flag;
//            this.tag = tag;
//            this.level = level;
//        }
//    }
}
