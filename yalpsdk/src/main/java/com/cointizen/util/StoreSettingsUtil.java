package com.cointizen.util;

import android.os.Build;

import androidx.annotation.RequiresApi;
import com.cointizen.paysdk.AppConstants;
import com.cointizen.paysdk.utils.TextUtils;

/**
 * 描述：打包写入的渠道信息相关工具类
 * 时间: 2018-03-07 14:59
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class StoreSettingsUtil {

    private static final String TAG = "StoreSettingsUtil";
    private static final String DELIMITER = AppConstants.STR_fb84c58af21fbf475e3f9cd5bd66b17c;
    private static final String DEFAULT_STORE_ID = "0";
    private static final String DEFAULT_STORE_NAME = AppConstants.STR_1cb6cab05164b4416544f5270d6a59bb;

    private static String STORE_ID = DEFAULT_STORE_ID;
    private static String STORE_NAME = DEFAULT_STORE_NAME;
    private static String LAUNCH_ID = "0";  //投放平台ID
    private static String POSITION = "1";  //
    private static int GAME_VERSION = 100; //游戏版本号
    private static boolean initialized = false;

    public static String getPosition() {
        return POSITION;
    }

    public static int getGameVersion() {
        return GAME_VERSION;
    }

    public static String getLaunchId() {
        return LAUNCH_ID;
    }

    public static String getStoreId(){
        return STORE_ID;
    }

    public static String getStoreName(){
        return STORE_NAME;
    }

    public static void init(String channel) {
        if (initialized) {
            return;
        }
        if(!TextUtils.isEmpty(channel) && channel.split(DELIMITER).length >= 3) {
            String[] channelInfo = channel.split(DELIMITER);
            STORE_ID = channelInfo[0];
            STORE_NAME =  channelInfo[1];
            GAME_VERSION =  Integer.valueOf(channelInfo[2]);
            if(channelInfo.length >= 4) {
                LAUNCH_ID = channelInfo[3];
                if(channelInfo.length >= 5) {
                    POSITION = channelInfo[4];
                }
            }
            initialized = true;
        }
    }
}
