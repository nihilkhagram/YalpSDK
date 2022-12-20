package com.cointizen.paysdk.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.cointizen.paysdk.utils.MCLog;

public class YunBaiDuMsgService extends Service {

    private static final String TAG = "YunBaiDuMsgService";

    public YunBaiDuMsgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        MCLog.d(TAG, "YunBaiDuMsgService onBind");
        ServiceManager.getInstance().isBaiDuYunOS = true;
        return new BaiduMsgBinder(this);
    }

    private static class BaiduMsgBinder extends android.os.Binder {

        private Context context;

        private BaiduMsgBinder(Context context) { this.context = context; }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String logInfo = "BaiduMsgDemoService received msg from remote-play, code: " + code;
            if (code != 24) {
                logInfo += ", code is not 24";
                MCLog.i(TAG, logInfo);
                sendMsgToActivity(logInfo);
                return super.onTransact(code, data, reply, flags);
            }
            try {
                int type = data.readInt();
                String message = data.readString();
                MCLog.d(TAG, "type: " + type + ", message: " + message);
                logInfo += ", type: " + type + ", message: " + message;
                if (type == 999) {
                    logInfo += "\n, this is a message for testing transact fail, return false.";
                    sendMsgToActivity(logInfo);
                    return false;
                }
                sendMsgToActivity(message);
                return true;
            } catch (Exception e) {
                MCLog.e(TAG, "read data from binder exception" + e);
                return false;
            }
        }

        private void sendMsgToActivity(String message) {
            Intent intent = new Intent();
            intent.setAction(ServiceManager.RECEIVER_ACTION);
            intent.putExtra("remote-play_info", message);
            context.sendBroadcast(intent);
        }
    }
}
