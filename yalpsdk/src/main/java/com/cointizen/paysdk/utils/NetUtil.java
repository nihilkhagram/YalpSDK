package com.cointizen.paysdk.utils;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 判断手机是否联网 AndroidManifest.xml文件中添加如下权限： <uses-permission
 * android:name="android.permission.INTERNET"/> <uses-permission
 * android:name="android.permission.ACCESS_NETWORK_STATE"/> <uses-permission
 * android:name="android.permission.ACCESS_WIFI_STATE"/>
 * 
 * @author Administrator
 * 
 *         others
 *         如果拟开发一个网络应用的程序，首先考虑是否接入网络，在Android手机中判断是否联网可以通过ConnectivityManager
 *         类的isAvailable()方法判断，首先获取网络通讯类的实例 ConnectivityManager
 *         cwjManager=(ConnectivityManager
 *         )getSystemService(Context.CONNECTIVITY_SERVICE);
 *         ，使用cwjManager.getActiveNetworkInfo().isAvailable();
 *         来返回是否有效，如果为True则表示当前Android手机已经联网
 *         ，可能是WiFi或GPRS、HSDPA等等，具体的可以通过ConnectivityManager
 *         类的getActiveNetworkInfo() 方法判断详细的接入方式，需要注意的是有关调用需要加入<uses-permission
 *         android
 *         :name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
 *         这个权限，android开发网提醒大家在真机上Market和Browser程序都使用了这个方法，来判断是否继续，
 *         同时在一些网络超时的时候也可以检查下网络连接是否存在，以免浪费手机上的电力资源
 *
 */
public class NetUtil {
	// 判断网络连接
	public static boolean isNetworkConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable()) {
			// 当前无可用网络
			return false;
		} else {
			// 当前有可用网络
			return true;
		}
	}

	// 判断网络连接是否可用
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			// 如果仅仅是用来判断网络连接
			// 则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 判断GPS是否打开
	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = lm.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	// 判断WIFI是否打开，打开不连接wifi，false，打开连接不可用的wifi true
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	// 判断是否是3G网络
	public static boolean is3G(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	// 判断是wifi还是3g网络,用户的体现性在这里了，wifi就可以建议下载或者在线播放。
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
	
	public static boolean showNetMsg(Context context){
		if (!isNetworkAvailable(context)) {
			ToastUtil.show(context,UtilsConstants.S_ANvLxxKBNc);
			return false;
		}
		return true;
	}
}
