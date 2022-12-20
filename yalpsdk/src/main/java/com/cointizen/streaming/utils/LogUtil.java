package com.cointizen.streaming.utils;

import android.util.Log;


public class LogUtil {

    private static final String TAG = "EarlyStudy";
    private static final String ERROR_RECEIVER_NOT_REGISTERED = "Receiver not registered";
    private static final boolean DEBUG = true;
    private static final boolean DEBUG2 = true;
    private static final boolean DEBUG_ERROR = true;

    public static void debug(String msg) {
        if (DEBUG && msg != null && !msg.equals("")) {
            Log.d(TAG, msg);
        }
    }

    public static void debug2(Object msg) {
        if (DEBUG2 && msg != null && !msg.equals("")) {
            Log.d(TAG, String.valueOf(msg));
        }
    }

    public static void error(String msg) {
        if (DEBUG_ERROR) {
            Log.e(TAG, msg);
        }
    }

    public static void info(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void error(Exception ex) {
        String msg = ex.getMessage();
        error(msg);

        if (msg != null && msg.contains(ERROR_RECEIVER_NOT_REGISTERED)) {
            return;
        }
        if (DEBUG_ERROR) {
            ex.printStackTrace();
        }
    }
}
