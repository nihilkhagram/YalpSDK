package com.cointizen.paysdk.utils;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.os.Handler;
import android.os.Message;

import com.cointizen.paysdk.activity.MCHUserCenterActivity;
import com.cointizen.paysdk.bean.SignBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.MCSignaaaDialog;
import com.cointizen.paysdk.http.process.SignDetProcess;
import com.cointizen.paysdk.http.process.SignInProcess;

/**
 * 描述：
 * 时间: 2018-11-26 16:26
 */

public class SignInUtils {

    private MCHUserCenterActivity mcUserCenterActivity;
    private FragmentManager fragmentManager;
    private int isSign;

    public void Sign(MCHUserCenterActivity mcUserCenterActivity, FragmentManager fragmentManager, int isSign){
        this.mcUserCenterActivity = mcUserCenterActivity;
        this.fragmentManager = fragmentManager;
        this.isSign = isSign;

        if (isSign == 0){ //今日未签，执行签到请求
            SignInProcess signInProcess = new SignInProcess();
            signInProcess.post(handler);
        }else { //已签过，查询签到详情
            new SignDetProcess().post(handler);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.SIGNIN_SUCCESS:  //签到成功
                    mcUserCenterActivity.retrieveUserInfo();
                    new SignDetProcess().post(handler);
                    ToastUtil.show(mcUserCenterActivity,msg.obj.toString());
                    break;
                case Constant.SIGNIN_FAIL:
                    ToastUtil.show(mcUserCenterActivity,msg.obj.toString());
                    break;
                case Constant.SIGN_DET_SUCCESS:  //查询签到详情成功
                    SignBean signBean = (SignBean) msg.obj;
                    MCSignaaaDialog mcSignDialog = new MCSignaaaDialog(mcUserCenterActivity,signBean);
                    mcSignDialog.show(fragmentManager,"MCSignDialog");
                    break;
                case Constant.SIGN_DET_FAIL:
                    ToastUtil.show(mcUserCenterActivity,msg.obj.toString());
                    break;
            }
        }
    };

}
