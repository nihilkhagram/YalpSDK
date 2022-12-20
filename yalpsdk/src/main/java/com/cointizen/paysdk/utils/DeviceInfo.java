package com.cointizen.paysdk.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class DeviceInfo {

	private static final String TAG = "DeviceInfo";

	/**
	 * 获取手机状态栏高度
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getStatusBarHeight(Context mContext) {
		// 获取手机上部菜单高度
		Resources resources = mContext.getResources();
		int resourceId = resources.getIdentifier("status_bar_height", "dimen",
				"android");
		int height = resources.getDimensionPixelSize(resourceId);
		MCLog.v(TAG, "fun#getStatusBarHeight height:" + height);
		return height;
	}

	/** 手机屏幕高度 */
	public static int getScreenHeight(Context mContext) {
		return getWindowManager(mContext).getDefaultDisplay().getHeight();
	}

	/** 手机屏幕宽度 */
	public static int getScreenWidth(Context mContext) {
		return getWindowManager(mContext).getDefaultDisplay().getWidth();
	}

	/** 或得WindowManager */
	public static WindowManager getWindowManager(Context mContext) {
		return (WindowManager) mContext.getSystemService("window");
	}
	/**
	 * 判断当前手机屏幕是否全屏
	 */
	public static boolean isFullScream(Activity mContext) {
		int v = mContext.getWindow().getAttributes().flags;
		MCLog.v(TAG, mContext.getClass().getSimpleName() + ":" + v
				+ UtilsConstants.S_BTbwKNusUA);
		if (66816 == v) {
			return true;
		} else if (65792 == v) {
			return false;
		} else {
			return false;
		}
	}

	/** 判断当前手机屏幕是否全屏，可在service中使用 */
	public static boolean isFullScream(Context mContext) {
		View mCheckFullScreenView = new View(mContext);
		mCheckFullScreenView.setBackgroundColor(Color.parseColor("#00000000"));
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(mContext.WINDOW_SERVICE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		// 创建非模态、不可碰触
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		// 放在左上角
		params.gravity = Gravity.START | Gravity.TOP;
		params.height = 1;
		params.width = 1;
		// 设置弹出View类型
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		windowManager.addView(mCheckFullScreenView, params);
		return isFullscreen(mCheckFullScreenView);
	}

	/**
	 * Check if fullscreen is activated by a position of a top left View
	 * 
	 * @param topLeftView
	 *            View which position will be compared with 0,0
	 * @return
	 */
	public static boolean isFullscreen(View topLeftView) {
		int location[] = new int[2];
		topLeftView.getLocationOnScreen(location);
		return location[0] == 0 && location[1] == 0;
	}

	/**
	 * 判断当前屏幕是否是横屏
	 */
	public static boolean isVerticalScreen(Activity activity) {
		int flag = activity.getResources().getConfiguration().orientation;
		if (flag == 0) {
			return false;
		} else {
			return true;
		}
	}

	/** 设置全屏 */
	public static void setFullScream(Activity mContext) {
		mContext.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/** 取消全屏 */
	public static void cancelFullScream(Activity mContext) {
		mContext.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	/**
	 * 判断微信是否可用
	 * 
	 * @param con
	 * @param packname
	 * @return
	 */
	public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

	/**
	 * 判断qq是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isQQClientAvailable(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mobileqq")) {
					return true;
				}
			}
		}
		return false;
	}
	public static boolean checkApkExist(Context con, String packname) {
		if (TextUtils.isEmpty(packname)) {
			return false;
		}
		try {
			con.getPackageManager().getApplicationInfo(packname,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			MCLog.w(TAG, "fun#checkApkExist app " + packname + " exist");
			return true;
		} catch (NameNotFoundException e) {
			MCLog.e(TAG, "fun#checkApkExist NameNotFoundException:" + e);
		}
		return false;
	}

	// public boolean checkApkExist(Context context, String packageName) {
	// if (packageName == null || “”.equals(packageName))
	// return false;
	// try {
	// ApplicationInfo info = context.getPackageManager()
	// .getApplicationInfo(packageName,
	// PackageManager.GET_UNINSTALLED_PACKAGES);
	// return true;
	// } catch (NameNotFoundException e) {
	// return false;
	// }
	// }
}
