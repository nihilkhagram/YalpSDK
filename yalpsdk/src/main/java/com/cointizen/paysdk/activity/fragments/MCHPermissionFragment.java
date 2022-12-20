package com.cointizen.paysdk.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.SparseArray;

import com.cointizen.paysdk.callback.OnPermission;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.cointizen.paysdk.activity.ActivityConstants;

/**
 * 描述：权限fragment
 * 时间: 2018-08-29 13:49
 */

public class MCHPermissionFragment extends Fragment {

    private static final String PERMISSION_GROUP = "permission_group";//请求的权限
    private static final String REQUEST_CODE = "request_code";
    private static final String REQUEST_CONSTANT = "request_constant";

    private final static SparseArray<OnPermission> sContainer = new SparseArray<>();
    private String TAG = "PermissionFragment";

    public static MCHPermissionFragment newInstant(ArrayList<String> permissions, boolean constant) {
        MCHPermissionFragment fragment = new MCHPermissionFragment();
        Bundle bundle = new Bundle();

        int requestCode;
        //请求码随机生成，避免随机产生之前的请求码，必须进行循环判断
        do {
            //requestCode = new Random().nextInt(65535);//Studio编译的APK请求码必须小于65536
            requestCode = new Random().nextInt(255);//Eclipse编译的APK请求码必须小于256
        } while (sContainer.get(requestCode) != null);

        bundle.putInt(REQUEST_CODE, requestCode);
        bundle.putStringArrayList(PERMISSION_GROUP, permissions);
        bundle.putBoolean(REQUEST_CONSTANT, constant);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 准备请求
     */
    public void prepareRequest(Context activity, OnPermission call) {
        Activity activity1 = (Activity) activity;
        //将当前的请求码和对象添加到集合中
        sContainer.put(getArguments().getInt(REQUEST_CODE), call);
        activity1.getFragmentManager().beginTransaction().add(this, activity.getClass().getName()).commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> permissions = getArguments().getStringArrayList(PERMISSION_GROUP);

        if (permissions == null) return;

        if ((permissions.contains(PermissionsUtils.REQUEST_INSTALL_PACKAGES) && !isHasInstallPermission(getActivity()))
                || (permissions.contains(PermissionsUtils.SYSTEM_ALERT_WINDOW) && !isHasOverlaysPermission(getActivity()))) {

            if (permissions.contains(PermissionsUtils.REQUEST_INSTALL_PACKAGES) && !isHasInstallPermission(getActivity())) {
                //跳转到允许安装未知来源设置页面
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, getArguments().getInt(REQUEST_CODE));
            }

            if (permissions.contains(PermissionsUtils.SYSTEM_ALERT_WINDOW) && !isHasOverlaysPermission(getActivity())) {
                //跳转到悬浮窗设置页面
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, getArguments().getInt(REQUEST_CODE));
            }

        } else {
            requestPermission();
        }
    }

    /**
     * 请求权限
     */
    public void requestPermission() {
        if (isOverMarshmallow()) {
            ArrayList<String> permissions = getArguments().getStringArrayList(PERMISSION_GROUP);
            String[] strings = permissions.toArray(new String[permissions.size() - 1]);
            requestPermissions(strings, getArguments().getInt(REQUEST_CODE));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        MCLog.e(TAG,"onRequestPermissionsResult===============");
        OnPermission call = sContainer.get(requestCode);
        //根据请求码取出的对象为空，就直接返回不处理
        if (call == null) return;

        for (int i = 0; i < permissions.length; i++) {

            if (PermissionsUtils.REQUEST_INSTALL_PACKAGES.equals(permissions[i])) {
                if (isHasInstallPermission(getActivity())) {
                    grantResults[i] = PackageManager.PERMISSION_GRANTED;
                } else {
                    grantResults[i] = PackageManager.PERMISSION_DENIED;
                }
            }

            if (PermissionsUtils.SYSTEM_ALERT_WINDOW.equals(permissions[i])) {
                if (isHasOverlaysPermission(getActivity())) {
                    grantResults[i] = PackageManager.PERMISSION_GRANTED;
                } else {
                    grantResults[i] = PackageManager.PERMISSION_DENIED;
                }
            }
        }

        //获取授予权限
        List<String> succeedPermissions = getSucceedPermissions(permissions, grantResults);
        //如果请求成功的权限集合大小和请求的数组一样大时证明权限已经全部授予
        if (succeedPermissions.size() == permissions.length) {
            //代表申请的所有的权限都授予了
            call.hasPermission(succeedPermissions, true);
        } else {

            //获取拒绝权限
            List<String> failPermissions = getFailPermissions(permissions, grantResults);

            if (getArguments().getBoolean(REQUEST_CONSTANT) && checkPermissionPermanentDenied(getActivity(), failPermissions)) {
                //继续请求权限直到用户授权或者永久拒绝
                requestPermission();
                return;
            }

            //代表申请的权限中有不同意授予的，如果拒绝的时间过快证明是系统自动拒绝
            call.noPermission(failPermissions, checkPermissionPermanentDenied(getActivity(), failPermissions));

            //证明还有一部分权限被成功授予，回调成功接口
            if (!succeedPermissions.isEmpty()) {
                call.hasPermission(succeedPermissions, false);
            }
        }

        //权限回调结束后要删除集合中的对象，避免重复请求
        sContainer.remove(requestCode);
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    private boolean isBackCall;//是否已经回调了，避免安装权限和悬浮窗同时请求导致的重复回调

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MCLog.e(TAG,"onActivityResult===============");
        //super.onActivityResult(requestCode, resultCode, data);
        if (!isBackCall && requestCode == getArguments().getInt(REQUEST_CODE) ) {
            isBackCall = true;
            //需要延迟执行，不然有些华为机型授权了但是获取不到权限
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //请求其他危险权限
                    requestPermission();
                }
            }, 500);
        }
    }

    /**
     * 获取已授予的权限
     *
     * @param permissions  需要请求的权限组
     * @param grantResults 允许结果组
     */
    private List<String> getSucceedPermissions(String[] permissions, int[] grantResults) {

        List<String> succeedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {

            //把授予过的权限加入到集合中，-1表示没有授予，0表示已经授予
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                succeedPermissions.add(permissions[i]);
            }
        }
        return succeedPermissions;
    }

    /**
     * 获取没有授予的权限
     *
     * @param permissions  需要请求的权限组
     * @param grantResults 允许结果组
     */
    private List<String> getFailPermissions(String[] permissions, int[] grantResults) {
        List<String> failPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {

            //把没有授予过的权限加入到集合中，-1表示没有授予，0表示已经授予
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                failPermissions.add(permissions[i]);
            }
        }
        return failPermissions;
    }

    /**
     * 获取没有授予的权限
     *
     * @param context     上下文对象
     * @param permissions 需要请求的权限组
     */
    private ArrayList<String> getFailPermissions(Context context, List<String> permissions) {

        //必须设置目标SDK为23及以上才能正常检测安装权限
        if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.M) {
            throw new RuntimeException("The targetSdkVersion SDK must be 23 or more");
        }

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
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                if (failPermissions == null) failPermissions = new ArrayList<>();
                failPermissions.add(permission);
            }
        }

        return failPermissions;
    }

    /**
     * 检查某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permissions            请求的权限
     */
    private boolean checkPermissionPermanentDenied(Activity activity, List<String> permissions) {

        for (String permission : permissions) {

            //安装权限和浮窗权限不算在内
            if (permission.equals(PermissionsUtils.REQUEST_INSTALL_PACKAGES) || permission.equals(PermissionsUtils.SYSTEM_ALERT_WINDOW)) {
                continue;
            }

            if (isOverMarshmallow()) {
                if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                    if (!activity.shouldShowRequestPermissionRationale(permission)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否有安装权限
     */
    public boolean isHasInstallPermission(Context context) {
        if (isOverOreo()) {

            //必须设置目标SDK为26及以上才能正常检测安装权限
            if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.O) {
                MCLog.e(TAG,ActivityConstants.Log_BrvnpFntjC);
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
                MCLog.e(TAG,ActivityConstants.Log_AoEVgSZahD);
            }

            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    /**
     * 是否是6.0以上版本
     */
    private boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否是8.0以上版本
     */
    private boolean isOverOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 检测权限有没有在清单文件中注册
     *
     * @param activity           Activity对象
     * @param requestPermissions 请求的权限组
     */
    private void checkPermissions(Activity activity, List<String> requestPermissions) {
        List<String> manifest = getManifestPermissions(activity);
        if (manifest != null && manifest.size() != 0) {
            for (String permission : requestPermissions) {
                if (!manifest.contains(permission)) {
                    MCLog.e(TAG,ActivityConstants.Log_rlogxffmRj);
                }
            }
        } else {
            MCLog.e(TAG,ActivityConstants.Log_rlogxffmRj);
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
}
