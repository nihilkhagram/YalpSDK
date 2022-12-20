package com.cointizen.paysdk.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.cointizen.paysdk.activity.fragments.MCHPermissionFragment;
import com.cointizen.paysdk.callback.OnPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：权限请求工具类
 * 时间: 2018-08-29 11:50
 */

public final class PermissionsUtils {

    public static final String REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES"; // 8.0及以上应用安装权限
    public static final String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW"; // 6.0及以上悬浮窗权限
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"; // 读取外部存储
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"; // 写入外部存储
    public static final String MOUNT_UNMOUNT_FILESYSTEMS = "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"; // 挂载卸载文件系统
    public static final String CAMERA = "android.permission.CAMERA"; // 相机

    // 存储
    public static final String[] STORAGE = new String[]{
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE};

    // 定位
    public static final String[] Location = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION ,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    //录屏权限
    public static final String[] RECORDING = new String[]{
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
            MOUNT_UNMOUNT_FILESYSTEMS,
            CAMERA};

    private final Context mActivity;
    private List<String> mPermissions = new ArrayList<>();
    private boolean mConstant;
    private String TAG = "PermissionsUtils";

    /**
     * 是否是6.0以上版本
     */
    static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否是8.0以上版本
     */
    static boolean isOverOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private PermissionsUtils(Context activity) {
        mActivity = activity;
    }

    /**
     * 设置请求的对象
     */
    public static PermissionsUtils with(Context activity) {
        return new PermissionsUtils(activity);
    }


    /**
     * 设置权限组
     */
    public PermissionsUtils permission(String... permissions) {
        mPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    /**
     * 设置权限组
     */
    public PermissionsUtils permission(String[]... permissions) {
        for (String[] group : permissions) {
            mPermissions.addAll(Arrays.asList(group));
        }
        return this;
    }

    /**
     * 被拒绝后继续申请，直到授权或者永久拒绝
     */
    public PermissionsUtils constantRequest() {
        mConstant = true;
        return this;
    }


    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasSelfPermissionForXiaomi(Context context, String permission) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        String op = AppOpsManager.permissionToOp(permission);
        if (!TextUtils.isEmpty(op)) {
            int checkOp = appOpsManager.checkOp(op, android.os.Process.myUid(), context.getPackageName());
            return checkOp == AppOpsManager.MODE_ALLOWED && ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    /**
     * 请求权限
     */
    public void request(OnPermission call) {
        //如果没有指定请求的权限，就使用清单注册的权限进行请求
        if (mPermissions == null || mPermissions.size() == 0) mPermissions = getManifestPermissions(mActivity);
        if (mPermissions == null || mPermissions.size() == 0) {
            MCLog.e(TAG,UtilsConstants.Log_dSLEYGHwmi);
        }
        //使用isFinishing方法Activity在熄屏状态下会导致崩溃
        //if (mActivity == null || mActivity.isFinishing()) throw new IllegalArgumentException("Illegal Activity was passed in");
        if (mActivity == null){
            MCLog.e(TAG,UtilsConstants.Log_PFOWOwyUXO);
            return;
        }
        if (call == null) {
            MCLog.e(TAG,UtilsConstants.Log_yXYcKVuCGd);
        }

        ArrayList<String> failPermissions = getFailPermissions(mActivity, mPermissions);

        if (failPermissions == null || failPermissions.size() == 0) {
            //证明权限已经全部授予过
            call.hasPermission(mPermissions, true);
        } else {
            //检测权限有没有在清单文件中注册
            checkPermissions(mActivity, mPermissions);

            //申请没有授予过的权限
            MCHPermissionFragment.newInstant((new ArrayList<>(mPermissions)), mConstant).prepareRequest(mActivity, call);
        }
    }



    /**
     * 返回应用程序在清单文件中注册的权限
     */
     private List<String> getManifestPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return Arrays.asList(pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检测权限有没有在清单文件中注册
     *
     * @param activity           Activity对象
     * @param requestPermissions 请求的权限组
     */
    private void checkPermissions(Context activity, List<String> requestPermissions) {
        List<String> manifest = getManifestPermissions(activity);
        if (manifest != null && manifest.size() != 0) {
            for (String permission : requestPermissions) {
                if (!manifest.contains(permission)) {
                    MCLog.e(TAG,UtilsConstants.Log_dgixXUzVoU);
                }
            }
        } else {
            MCLog.e(TAG,UtilsConstants.Log_dgixXUzVoU);
        }
    }

    /**
     * 获取没有授予的权限
     *
     * @param context     上下文对象
     * @param permissions 需要请求的权限组
     */
     private ArrayList<String> getFailPermissions(final Context context, List<String> permissions) {

        //必须设置目标SDK为23及以上才能正常检测安装权限
//        if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.M) {
//            throw new RuntimeException("The targetSdkVersion SDK must be 23 or more");
//        }

        //如果是安卓6.0以下版本就返回null
        if (!isOverMarshmallow()) {
            return null;
        }

        ArrayList<String> failPermissions = null;

        for (String permission : permissions) {

            //检测安装权限
            if (permission.equals(PermissionsUtils.REQUEST_INSTALL_PACKAGES)) {

                if (!isHasInstallPermission(context)) {
                    if (failPermissions == null) failPermissions = new ArrayList<>();
                    failPermissions.add(permission);
                }
                continue;
            }

            //检测悬浮窗权限
            if (permission.equals(PermissionsUtils.SYSTEM_ALERT_WINDOW)) {

                if (!isHasOverlaysPermission(context)) {
                    if (failPermissions == null) failPermissions = new ArrayList<>();
                    failPermissions.add(permission);
                }
                continue;
            }


            //把没有授予过的权限加入到集合中
            int i = -100;
            if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.M) {
                i = PermissionChecker.checkSelfPermission(context,permission);
            }else{
                i = context.checkSelfPermission(permission);
            }
            if ( i== PackageManager.PERMISSION_DENIED||i==-2) {
                if (failPermissions == null) failPermissions = new ArrayList<>();
                failPermissions.add(permission);
            }
         }
        return failPermissions;
    }


    /**
     * 是否有安装权限
     */
    public boolean isHasInstallPermission(Context context) {
        if (isOverOreo()) {

            //必须设置目标SDK为26及以上才能正常检测安装权限
            if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.O) {
                MCLog.e(TAG,UtilsConstants.Log_tvgJQDHmcK);
            }

            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /**
     * 是否有悬浮窗权限
     */
    public boolean isHasOverlaysPermission(Context context) {

        if (isOverMarshmallow()) {

            //必须设置目标SDK为23及以上才能正常检测安装权限
            if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.M) {
                MCLog.e(TAG,UtilsConstants.Log_lNewxrJfKN);
            }

            return Settings.canDrawOverlays(context);
        }
        return true;
    }



}
