package com.cointizen.paysdk.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.cointizen.paysdk.channel.sign1.FileUtil;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.utils.AssetsUtils;
import com.cointizen.paysdk.utils.MCLog;
//import com.cointizen.paysdk.utils.MCPromoteUtils;
import com.cointizen.util.StoreSettingsUtil;
import com.cointizen.paysdk.utils.TextUtils;
import com.mchsdk.paysdk.jni.MCHKeyTools;

public class SdkDomain {

	private static final String TAG = "SdkDomain";

	public static final String KEY_SIGNKEY = "paysdk_signkey";
	public static final String KEY_IP_ADDRESS = "paysdk_address";

	private static SdkDomain keyUtil;

	public static SdkDomain getInstance() {
		if (keyUtil == null) {
			keyUtil = new SdkDomain();
		}
		return keyUtil;
	}

	private String channelId;
	private String channelName;
	private String launchId;
	private String position;
	private int updateVersion;
	private String gameId;
	private String gameName;
	private String gameAppId;

	private SdkDomain() {
	}

	public void init(Context context) {
		gameId = AssetsUtils.getInstance().getGameId(context);
		gameName = AssetsUtils.getInstance().getGameName(context);
		gameAppId = AssetsUtils.getInstance().getGameAppId(context);

//		PermissionsUtils.with(context).getPermission(context);
		MCHConstant.getInstance().setMCHKEY(getKey(context));
		MCHConstant.getInstance().setSdkBaseUrl(getIPAdderss(context));
		MCHConstant.getInstance().initUrlInfo();

		channelId = StoreSettingsUtil.getStoreId();
		channelName = StoreSettingsUtil.getStoreName();
		launchId = StoreSettingsUtil.getLaunchId();
		position = StoreSettingsUtil.getPosition();
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			updateVersion = pInfo.versionCode;
		} catch (Exception e) {
			updateVersion = StoreSettingsUtil.getGameVersion();
		}

		if (TextUtils.isEmpty(channelId)) {
			FileUtil fileUtil = new FileUtil();
			channelId = fileUtil.getPromoteId();
			channelName = fileUtil.getPromoteAccount();
		}else {
			MCLog.e(TAG, "init_channelId:" + channelId);
		}
		if (TextUtils.isEmpty(launchId)) {
			launchId = StoreSettingsUtil.getLaunchId();
		}
		if (TextUtils.isEmpty(position)) {
			position = StoreSettingsUtil.getPosition();
		}

		MCLog.e(TAG, channelId + "-" + channelName );
	}

	public String getChannelId() {
		return channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getGameAppId() {
		return gameAppId;
	}

	public String getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getUpdateVersion() {
		return updateVersion;
	}

	public String getLaunchId() {
		return launchId;
	}

	public String getPosition() {
		return position;
	}

	public boolean haveReadGameInfo(){
		if (TextUtils.isEmpty(gameId) || TextUtils.isEmpty(gameName) ||
				android.text.TextUtils.isEmpty(gameAppId) ||
				android.text.TextUtils.isEmpty(channelId) ||
				android.text.TextUtils.isEmpty(channelName)) {
			MCLog.e(TAG, "gameId:" + gameId + ", channelId:" + channelId);
			return false;
		}
		return true;
	}

	/**
 	* cp传的参数里面获得
 	*/
	private String getKey(Context context) {
		String paysdk_signkey = getMetaValue(context, KEY_SIGNKEY);
		if(TextUtils.isEmpty(paysdk_signkey)){
			paysdk_signkey = AssetsUtils.getInstance().getAccessKey(context);
		}
		if(!TextUtils.isEmpty(paysdk_signkey)){
			String signkey = MCHKeyTools.getInstance().secToNor(paysdk_signkey);
			return signkey;
		}

		return "";
	}

	private String getIPAdderss(Context context) {
		String paysdk_ipaddress = getMetaValue(context, KEY_IP_ADDRESS);
		if(TextUtils.isEmpty(paysdk_ipaddress)){
			paysdk_ipaddress = AssetsUtils.getInstance().getPlatformDomain(context);
		}
		return paysdk_ipaddress;
	}

	private String getMetaValue(Context context, String keystr){
		String res = "";
		try {
			res = getAppInfo(context).metaData.getString(keystr, "");
		}catch (Exception e){
			MCLog.w(TAG, keystr + " is null." + e.toString());
			res = "";
		}
		return  res;
	}

	private ApplicationInfo getAppInfo(Context context) {
		if (null == context) {
			return null;
		}
		ApplicationInfo appInfo;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo;
		} catch (PackageManager.NameNotFoundException e) {
			MCLog.w(TAG, "package name not found, error:" + e.toString());
			return null;
		}
	}

}
