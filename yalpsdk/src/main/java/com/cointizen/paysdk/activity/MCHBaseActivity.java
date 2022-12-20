package com.cointizen.paysdk.activity;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.bean.InitModel;
import com.cointizen.paysdk.http.process.DeviceOnlineProcess;
import com.cointizen.paysdk.utils.AppStatus;
import com.cointizen.paysdk.utils.AppStatusManager;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MCHBaseActivity extends Activity {

    private final String TAG = "MCBaseActivity";
    private boolean isForeground = false; //App是否在后台运行

    // View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MCLog.e(TAG, "fun#onCreate");
        YalpGamesSdk.getYalpGamesSdk().stopFloating(MCHBaseActivity.this);
        if(AppStatusManager.getInstance().getAppStatus()==AppStatus.STATUS_RECYVLE){
            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isForeground){
            MCLog.w(TAG,ActivityConstants.Log_pOggRNBfAy);
            isForeground = false;
            //请求设备上线
            DeviceOnlineProcess deviceOnlineProcess = new DeviceOnlineProcess(YalpGamesSdk.getMainActivity());
            deviceOnlineProcess.post();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtils.isAppOnForeground(this)) {
            isForeground = true;
            MCLog.w(TAG,ActivityConstants.Log_GFeRZoEYnZ);
            InitModel.init().offLine(YalpGamesSdk.getMainActivity(), false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    protected int getLayout(String layoutName) {
        return MCHInflaterUtils.getLayout(this, layoutName);
    }

    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(this, idName);
    }

    protected int getDrawable(String draname) {
        return MCHInflaterUtils.getDrawable(this, draname);
    }

    protected int getColor(String idName) {
        return MCHInflaterUtils.getIdByName(this, "color",idName);
    }

}
