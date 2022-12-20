package com.cointizen.paysdk.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 描述：AssetsUtils
 * 时间: 2018-11-09 15:21
 */

public class AssetsUtils {

    private static final String TAG = "AssetsUtils";

    private static AssetsUtils instance;
    private YalpGameConfig gameConfig;

    public static AssetsUtils getInstance() {
        if (null == instance) {
            instance = new AssetsUtils();
        }
        return instance;
    }

    public String getGameId(Context context){
        return getStr(context).getGameId();
    }

    public String getGameName(Context context){
        return getStr(context).getGameName();
    }

    public String getGameAppId(Context context){
        return getStr(context).getGameAppId();
    }

    public String getAccessKey(Context context){
        return getStr(context).getAccessKey();
    }

    public String getPlatformDomain(Context context){
        return getStr(context).getDomainName();
    }

    private YalpGameConfig getStr(Context context) {
        if (this.gameConfig != null) {
            return this.gameConfig;
        }
        AssetManager manager = context.getResources().getAssets();
        try {
            InputStream inputStream = manager.open("yalp.json");
            Gson gson = new Gson();
            this.gameConfig = gson.fromJson(new InputStreamReader(inputStream), YalpGameConfig.class);
        } catch (IOException e) {
            MCLog.e(TAG,UtilsConstants.Log_HajEBiSOfL);
            e.printStackTrace();
        }
        return this.gameConfig;
    }

    private static class YalpGameConfig {
        private String gameId;
        private String gameName;
        private String gameAppId;
        private String accessKey;
        private String domainName;

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getGameAppId() {
            return gameAppId;
        }

        public void setGameAppId(String gameAppId) {
            this.gameAppId = gameAppId;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }
    }
}
