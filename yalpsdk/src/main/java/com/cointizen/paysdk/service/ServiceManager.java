package com.cointizen.paysdk.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import androidx.annotation.NonNull;

import com.cointizen.open.ApiCallback;
import com.cointizen.open.GPUserResult;
import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.process.LoginProcess;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class ServiceManager {

    private static final String TAG = "ServiceManager";

    public static final String RECEIVER_ACTION = " com.cointizen.paysdk.service.remotemsgreceiver";

    private static final int MSG_FORWARD = 25;
    private static final String SERVICE_8 = "business";
    private static final String SERVICE_NAME = "com.cointizen.cloudsdk_demo/com.cointizen.paysdk.service.YunBaiDuMsgService";
    private IBinder mRomService;

    public boolean isBaiDuYunOS = false;
    public String loginCode = "";
    private boolean isYunLogin = false;
    public boolean isPC = false;

    private static ServiceManager instance;
    public static ServiceManager getInstance() {
        if (null == instance) {
            instance = new ServiceManager();
        }
        return instance;
    }

    private ServiceManager() {
        connectBusinessService(SERVICE_8);
    }


    public void registerService(Activity activit) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        activit.registerReceiver(remoteMsgReceiver, intentFilter);
    }

    public void unregisterReceiver(Activity activity) {
        activity.unregisterReceiver(remoteMsgReceiver);
    }

    private void connectBusinessService(String serviceName) {
        if (mRomService != null && mRomService.isBinderAlive()) {
            MCLog.i(TAG, "service.jar has already connected!");
//            tvOpInfo.append("---> service.jar has already connected!\n");
            return;
        }
        try {
            @SuppressLint("PrivateApi") Class<?> clazz = Class.forName("android.os.ServiceManager");
            MCLog.d(TAG, "ServiceManager reflect success!");
            Method method = clazz.getMethod("getService", String.class);
            method.setAccessible(true);
            MCLog.d(TAG, "get method getService success!");
            Object redfingerService = method.invoke(null, serviceName);
            if (redfingerService != null) {
                mRomService = (IBinder) redfingerService;
                MCLog.d(TAG, "get service: " + serviceName + " success!");
            } else {
                MCLog.i(TAG, "get service: " + serviceName + ", NULL!");
//                tvOpInfo.append("---> connect to service.jar failed!\n");
            }
        } catch (Exception e) {
            MCLog.i(TAG, "get RedfingerService failed!" + e);
//            tvOpInfo.append("---> connect to service.jar exception: \n");
//            tvOpInfo.append("---> " + Log.getStackTraceString(e) + "\n");
        }
    }

    /**
     * 返回控制端信息
     * @param type 2支付  3登录失败
     * @param message type-2-支付url
     */
    public void sendMessage(int type, String message) {
        if (mRomService == null || !mRomService.isBinderAlive()) {
            MCLog.d(TAG, "click to send message but service.jar is not connected!");
            return;
        }

        try {
            Parcel data = Parcel.obtain();
            data.writeInt(type);
            data.writeString(message);
            data.writeString(SERVICE_NAME);
            Parcel reply = Parcel.obtain();
            boolean success = mRomService.transact(MSG_FORWARD, data, reply, 0);
            if (success) {
                MCLog.i(TAG, "send message to service.jar success!");
//                tvOpInfo.append("---> send message to service.jar success!\n");
            } else {
                MCLog.w(TAG, "send message to service.jar failed!");
//                tvOpInfo.append("---> send message to service.jar failed!\n");
            }
            data.recycle();
            int result = reply.readInt();
            if (result == 1) {
                MCLog.i(TAG, "service.jar sent the message to remote-play success!");
//                tvOpInfo.append("---> service.jar sent the message to remote-play success!\n");
            } else {
                MCLog.w(TAG, "service.jar sent the message to remote-play but failed! result: " + result);
//                tvOpInfo.append("---> service.jar sent the message to remote-play but failed! result: " + result + "\n");
            }
            reply.recycle();
        } catch (RemoteException e) {
            MCLog.e(TAG, "sendMessage exception" + e);
//            tvOpInfo.append("---> sendMessage exception: \n");
//            tvOpInfo.append(Log.getStackTraceString(e) + "\n");
        }
    }

    public void launcherPay(String url) {
        sendMessage(2, url);
    }

    private void login(String login_info) {
        if (isYunLogin) {
            return;
        }
        isYunLogin = true;
        try {
            JSONObject object = new JSONObject(login_info);
            loginCode = object.optString("code", "");
            isPC = "1".equals(object.optString("isPC", "0"));
            launchLogin();
        } catch (JSONException e) {
            MCLog.e(TAG, "login:" + e.toString());
            e.printStackTrace();
        }
    }

    public void launchLogin() {
        LoginProcess loginprocess = new LoginProcess(YalpGamesSdk.getMainActivity());
        loginprocess.post(loginHandle);
    }


    private final Handler loginHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == Constant.LOGIN_SUCCESS){
                UserLogin loginSuccess = (UserLogin) msg.obj;
                if ("1".equals(loginSuccess.getLoginStatus())) {
                    LoginModel.instance().loginSuccess(true, true, loginSuccess);
                }
            }else {
                GPUserResult GPUserResult = new GPUserResult();
                GPUserResult.setmErrCode(GPUserResult.USER_RESULT_LOGIN_FAIL);
                GPUserResult.setAccountNo("");
                GPUserResult.setExtra_param(loginCode);
                if (ApiCallback.getLoginCallback() != null){
                    ApiCallback.getLoginCallback().onFinish(GPUserResult);
                }
                sendMessage(3, "login fail");
                isYunLogin = false;
            }
            return false;
        }
    });


    private final BroadcastReceiver remoteMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (RECEIVER_ACTION.equals(action)) {
                String info = intent.getStringExtra("remote-play_info");
                MCLog.i(TAG, info + "");
                login(info);
            }
        }
    };

}
