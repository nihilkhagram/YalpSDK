package com.cointizen.plugin.qg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.utils.ScreenshotUtils;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.plugin.AppConstants;


/**
 * 描述：强更工具
 * 时间: 2018-09-04 17:44
 */

public class UpdateUtils {

    private static UpdateUtils instance;
    public String url = MCHConstant.getInstance().getSdkBaseUrl()+"";
    private static final String TAG = "UpdateUtils";
    private int versionCode;
    private Context context;


    public static UpdateUtils getInstance() {
        if (null == instance) {
            instance = new UpdateUtils();
        }
        return instance;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.FORCED_UPDATE_SUCCESS:
                    UpdateBean dateBean = (UpdateBean) msg.obj;
                    checkUpDate(dateBean);
                    break;
                case Constant.FORCED_UPDATE_FAIL:
                    if(msg.obj.toString()!=null){
                        ToastUtil.show(context,msg.obj.toString());
                    }
                    break;
            }
        }
    };

    public void checkForUpdates(Context context){
        this.context = context;
        //获取本地 版本码
        versionCode = getAppVersionCode(context, context.getPackageName());
        UpdateProcess upDateProcess = new UpdateProcess(context);
        upDateProcess.post(handler);
    }


    /**
     * 对比是否需要更新
     * @param dateBean
     */
    private void checkUpDate(UpdateBean dateBean) {
        if(dateBean==null){
            MCLog.e(TAG,"fun#CheckUpDate  dateBean==null");
            return;
        }
        int thisGameVersion =  SdkDomain.getInstance().getUpdateVersion();
        MCLog.w(TAG,AppConstants.LOG_dc17efd1c9930c1cda60c4fa9bc886a0+dateBean.getAnd_version_code()+AppConstants.LOG_2e196ec42d7efd011efcfe4ae1a07954+thisGameVersion);
        if(thisGameVersion == 0){  //如果本地版本码为0，则视为不是从平台下载的游戏包。（有可能是CP在对接调试）这时不提示更新
            ScreenshotUtils.getInstance().init(YalpGamesSdk.getMainActivity());             //初始化注册  游客登录监听
            return;
        }
        if(dateBean.getAnd_version_code() > thisGameVersion){
            Intent intent = new Intent(context, UpgradeVersionDialog.class);
            intent.putExtra("UpdateBean",dateBean);
            context.startActivity(intent);
        }else{
            ScreenshotUtils.getInstance().init(YalpGamesSdk.getMainActivity());             //初始化注册  游客登录监听
        }
    }

    /**
     * 获取App版本码
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本码
     */
    private int getAppVersionCode(Context context, String packageName) {
        if (packageName==null||packageName.equals("")) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
