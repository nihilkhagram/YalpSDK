package com.cointizen.paysdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.cointizen.paysdk.activity.MCHWebviewActivity;

public class WebViewUtil {

	public static void read(Context con, String url) {
//		String url = ipaddress.substring(0, ipaddress.indexOf("/sdk.php"))
//				+ "/media.php?s=/Article/agreement.html";
		Uri uri= Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		con.startActivity(intent);
	}

	public static void webView(Activity activity, String url) {
		webView(activity, url, false);
	}

	public static void webView(Activity activity, String url, boolean isClose) {
		Intent intent = new Intent(activity, MCHWebviewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("close", isClose);
		activity.startActivity(intent);
	}

}
