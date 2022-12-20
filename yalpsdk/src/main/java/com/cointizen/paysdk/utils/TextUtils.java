package com.cointizen.paysdk.utils;

/**
 * Created by zhujinzhujin on 2017/1/5.
 */

public class TextUtils {

    public static boolean isEmpty(String str){
        if (str == null || str.length() == 0) {
            return true;
        }
        else {
            return "null".equals(str);
        }
    }
}
