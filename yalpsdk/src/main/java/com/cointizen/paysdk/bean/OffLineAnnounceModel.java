package com.cointizen.paysdk.bean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.OffLineAnnounceProgress;
import com.cointizen.paysdk.utils.MCLog;

/**
 * Created by Administrator on 2017/5/5.
 * 下线通知
 */

public class OffLineAnnounceModel {

    public static final String TAG = "OffLineAnnounceModel";
    private Context context;

    public OffLineAnnounceModel(Context context) {
        this.context = context;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case Constant.OFFLINE_SUCCESS:
//                    UserLoginSession.getInstance().getChannelAndGame()=new ChannelAndGameInfo();
//                    FlagControl.isLogin=false;
                    MCLog.w(TAG,BeanConstants.Log_GwMClYLvlb);
                    Constant.userIsOnLine = false;
                    break;
                case Constant.OFFLINE_FAIL:
                    leaveGameTime=System.currentTimeMillis();
                    MCLog.e(TAG,BeanConstants.Log_nsNqIGJXlh);
                    break;
            }
        }
    };

    //离开游戏时间
    public static long leaveGameTime=0;


    /**
     * 下线通知
     */
    public void offLineAnnouce() {
        OffLineAnnounceProgress offLineAnnounceProgress=new OffLineAnnounceProgress();
        offLineAnnounceProgress.post(mHandler);

    }
}
