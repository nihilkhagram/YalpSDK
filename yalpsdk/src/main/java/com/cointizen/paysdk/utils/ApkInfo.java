package com.cointizen.paysdk.utils;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

public class ApkInfo {
	private static final String TAG = "ApkInfo";
    /**或得当前应用包名*/
	public static String packageName(Context context) {
		return context.getPackageName();
	}
    /**版本名称*/
	public static String versionName(Context context) throws Exception {
		String pkName = context.getPackageName();
		return context.getPackageManager().getPackageInfo(pkName, 0).versionName;
	}
	/**根据包名获得应用版本名称*/
	public static String versionName(Context context,String packageName) throws Exception {
		String pkName = packageName;
		return context.getPackageManager().getPackageInfo(pkName, 0).versionName;
	}
    /**版本号*/
	public static int versionCode(Context context) throws Exception {
		String pkName = context.getPackageName();
		return context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
	}
	/**根据包名获得应用版本名称*/
	public static int versionCode(Context context,String packageName) throws Exception {
		String pkName = packageName;
		return context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
	}
    /**获得当前应用md5签名*/
	public static String getSign(Context context) {
		return getSign(context, packageName(context));
	}
    /**根据包名或得应用md5签名*/
	public static String getSign(Context context, String packageName) {
		if(null==context){
			System.out.println("ApkInfo context is null");
		}
		Signature[] arrayOfSignature = getRawSignature(context, packageName);
		if ((arrayOfSignature == null) || (arrayOfSignature.length == 0)) {
			errout("signs is null");
			return null;
		}
		stdout(Md5.getMessageDigest(arrayOfSignature[0].toByteArray()));
		return Md5.getMessageDigest(arrayOfSignature[0].toByteArray());
	}
	/**或得当前应用自然签名*/
    public static String getSignature(Context context){
    	return getSignature(context, packageName(context));
    }
    /**根据包名获得自然签名*/
	public static String getSignature(Context context,String pkgname) {
		Signature[] signatures;
		StringBuilder builder = new StringBuilder();
		signatures=	getRawSignature(context,pkgname);
		for (Signature signature : signatures) {
			builder.append(signature.toCharsString());
		}
		/************** 得到应用签名 **************/
		return builder.toString();
	}

	/**根据包名获得自然签名数组*/
	private static Signature[] getRawSignature(Context context,
			String pakname) {
		if ((pakname == null) || (pakname.length() == 0)) {
			errout(UtilsConstants.S_YNzGIBwvmE);
			return null;
		}
		PackageManager localPackageManager = context.getPackageManager();
		PackageInfo localPackageInfo;
		try {
			localPackageInfo = localPackageManager.getPackageInfo(pakname,
					PackageManager.GET_SIGNATURES);
			if (localPackageInfo == null) {
				errout(UtilsConstants.S_LDUSQclZst + pakname);
				return null;
			}
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			errout(UtilsConstants.S_UPeTvCRTFD);
			return null;
		}
		return localPackageInfo.signatures;
	}
	/**
	 * 输出成功信息
	 */
	private static void stdout(String code) {
		// 输出正确日志
		MCLog.d(TAG, "stdout() called with: " + "code = [" + code + "]");
	}

	/**
	 * 输出错误信息
	 */
	private static void errout(String reason) {
		// 输出错误日志
		MCLog.d(TAG, "errout() called with: " + "reason = [" + reason + "]");
	}
	/** 
	 * 判断应用是否正在运行 
	 *  
	 * @param context 
	 * @param packageName 
	 * @return 
	 */  
	@SuppressLint("NewApi")
	private boolean isRunning(Context context, String packageName) {  
	    ActivityManager am = (ActivityManager) context  
	            .getSystemService(Context.ACTIVITY_SERVICE);  
	    List<RunningAppProcessInfo> list = am.getRunningAppProcesses();  
	    for (RunningAppProcessInfo appProcess : list) {  
	        String processName = appProcess.processName;  
	        if (processName != null && processName.equals(packageName)) {  
	            return true;  
	        }  
	    }  
	    return false;  
	}  
}
