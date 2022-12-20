package com.cointizen.plugin.yc.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.plugin.yc.http.process.ClarityProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.cointizen.plugin.AppConstants;
public class NetWorkUtils {


    private static NetWorkUtils instance;
    private static String IPAddress;
    private static String TAG = "NetWorkUtils";
    private static TextView tvDelay;
    private ImageView txtChannel;
    private static String obj;
    private int Failure;
    //创建Scheduled线程池
    private Runnable runnable;
    private InetAddress byName;
    private @SuppressLint("HandlerLeak")
    Handler handler;
    boolean isOk = false;

    public static NetWorkUtils getInstance() {
        if (null == instance) {
            instance = new NetWorkUtils();
        }
        return instance;
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.HOST_SUCCESS:
                    if(!isOk){
                        isOk = true;
                        obj = (String) msg.obj;
                        getHostDelay(obj);
                    }
                    break;
                case Constant.HOST_FAIL:
                    MCLog.e(TAG,(String) msg.obj);
                    break;
                case 2:
                    tvDelay.setText( msg.obj.toString());
                    if(msg.obj.toString().equals("1000")){
                        Failure = Failure+1;
                    }else{
                        Failure = 0;
                    }
                    if(Failure==1){
                        MCLog.e(TAG,AppConstants.LOG_d7aa1355be99a9cf23a25a09ce74aa0c+IPAddress+AppConstants.LOG_b245011a40168439060d37c4240a73e8);
                        IPAddress = byName.getHostAddress();
                        Failure=0;
                    }
                    break;
            }
        }
    };

    private void Chuangjian(){
        if(handler==null){
            HandlerThread thread = new HandlerThread("MyHandlerThread");
            thread.start();
            handler = new Handler(thread.getLooper()){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case 1:
                            getDelay();
                            break;
                    }
                }
            };
        }
    }


    private  void getHostDelay(final String host) {
        if(runnable==null){
            new Thread(() -> {
                Chuangjian();
                InetAddress ReturnStr1 = null;
                MCLog.e(TAG,AppConstants.LOG_95fd62436d2488b70373808bcaeb4d71+host);
                try {
                    ReturnStr1 = InetAddress.getByName(host);
                    IPAddress = ReturnStr1.getHostAddress();
                    byName = InetAddress.getByName("www.google.com");
                    handler.sendEmptyMessage(1);
                    MCLog.w(TAG,AppConstants.LOG_520a603c7c75cfa366428b9387a3156a+host+AppConstants.LOG_315a08ddfb55a2a71f650fca3bdebad3+IPAddress);
                    return;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                try {
                    MCLog.e(TAG,AppConstants.LOG_d27ae472d796a934b51a82ee17b447ca);
                    ReturnStr1 = InetAddress.getByName("www.google.com");
                    IPAddress = ReturnStr1.getHostAddress();
                    handler.sendEmptyMessage(1);
                    MCLog.w(TAG,AppConstants.LOG_4d99ae72448ecb7fa1d8168735cff7a6+IPAddress);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    /**
     * 根据域名获取ip
     * @param tvDelay
     * @param txtChannel
     */
    public void getDelayByHost(TextView tvDelay, ImageView txtChannel) {
        if(tvDelay==null||txtChannel==null){
            MCLog.e(TAG,AppConstants.LOG_ebaf1aa24f096043c416a33c82b0b5ae);
            return;
        }
        this.tvDelay = tvDelay;
        this.txtChannel = txtChannel;
        if(obj==null){
            ClarityProcess clarityProcess = new ClarityProcess();
            clarityProcess.post(mHandler);
        }
    }


    /**
     * 根据IP获取延迟
     */
    private synchronized void getDelay(){
        if(IPAddress!=null){
            runnable = new Runnable() {

                @Override
                public void run() {
                    String delay = new String();
                    Process p = null;
                    try {
                        p = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + IPAddress);
                        BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String str = "";
                        while ((str = buf.readLine()) != null) {
                            if (str.contains("avg")) {
                                int i = str.indexOf("/", 20);
                                int j = str.indexOf(".", i);
                                delay = str.substring(i + 1, j);
                            }
                        }
                        if (delay.equals("")) {
                            delay = "1000";
//                            MCLog.w(TAG, AppConstants.LOG_9d9a328f4b91413965711d0d1bbbd294 + 1000);
                        } else {
                            Failure = 0;
//                            MCLog.w(TAG, AppConstants.LOG_9d9a328f4b91413965711d0d1bbbd294 + delay);
                        }
                        Message message = mHandler.obtainMessage();
                        message.obj = delay;
                        message.what = 2;
                        mHandler.sendMessage(message);
                        handler.postDelayed(this, 1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                        MCLog.e(TAG, "fun#getDelay  IOException:" + e.toString());
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }


    /**
     * 取消获取延迟
     */
    public void Cancel(){
        if(txtChannel!=null){
            txtChannel.setVisibility(View.VISIBLE);
        }
        MCLog.w(TAG,AppConstants.LOG_46da095098752a03c5530e880eb9580d);
    }


}
