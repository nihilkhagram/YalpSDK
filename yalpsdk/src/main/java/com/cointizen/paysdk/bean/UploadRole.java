package com.cointizen.paysdk.bean;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.cointizen.open.RoleInfo;
import com.cointizen.open.UploadCharacterCallBack;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.UploadRoleProcess;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.ToastUtil;

/**
 * Created by smile on 2017/8/18.
 * 上传角色
 */

public class UploadRole {

    public static final String TAG = "UploadRole";
    RoleInfo mRoleInfo;
    UploadCharacterCallBack roleCallBack;


    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.UPLOAD_ROLE_SUCCESS:// 修改成功
                    if (null != getRoleCallBack()) {
                        getRoleCallBack().onUploadComplete("1");
                    }
                    break;
                case Constant.UPLOAD_ROLE_FAIL:// 修改失败
                    if (null != getRoleCallBack()) {
                        getRoleCallBack().onUploadComplete("0");
                    }
                    break;
            }
        }
    };

    public UploadRole(RoleInfo roleInfo, UploadCharacterCallBack roleCallBack) {
        this.mRoleInfo = roleInfo;
        this.roleCallBack=roleCallBack;
    }


    public void upload(){
        if (!checkParams()) {
            return;
        }
        if (!LoginModel.instance().isLogin()) {
            MCLog.w(TAG,BeanConstants.S_user_not_logged_in);
            getRoleCallBack().onUploadComplete("0");
            return;
        }
        UploadRoleProcess roleProcess=new UploadRoleProcess();
        roleProcess.setRoleInfo(mRoleInfo);
        roleProcess.post(mHandler);
    }

    public UploadCharacterCallBack getRoleCallBack() {
        if (roleCallBack != null) {
            return roleCallBack;
        }
        return null;
    }

    private boolean checkParams() {
        if (null == mRoleInfo) {
            MCLog.e(TAG, "roleinfo is null !");
            roleCallBack.onUploadComplete("-1");
            return false;
        }
        if (TextUtils.isEmpty(mRoleInfo.getRoleCombat())) {
            MCLog.e(TAG, BeanConstants.Log_voHdfOjVRQ);
            ToastUtil.show(BeanConstants.S_HLPBAozKrv);
            roleCallBack.onUploadComplete("-1");
            return false;
        }
        if (TextUtils.isEmpty(mRoleInfo.getRoleId())) {
            MCLog.e(TAG, BeanConstants.Log_VGAKNofmFb);
            ToastUtil.show(BeanConstants.S_zKeZMUuvAQ);
            roleCallBack.onUploadComplete("-1");
            return false;
        }
        if (TextUtils.isEmpty(mRoleInfo.getRoleLevel())) {
            MCLog.e(TAG, BeanConstants.Log_tTqMDOggmE);
            ToastUtil.show(BeanConstants.S_OdeWuMxNLL);
            roleCallBack.onUploadComplete("-1");
            return false;
        }
        if (TextUtils.isEmpty(mRoleInfo.getRoleName())) {
            MCLog.e(TAG, BeanConstants.Log_FFVqUnQRYj);
            ToastUtil.show(BeanConstants.S_hoYzAQNoOn);
            roleCallBack.onUploadComplete("-1");
            return false;
        }
        if (TextUtils.isEmpty(mRoleInfo.getServerId())) {
            MCLog.e(TAG, BeanConstants.Log_zzdIyEcPGS);
            ToastUtil.show(BeanConstants.S_yvfTcaXkLq);
            roleCallBack.onUploadComplete("-1");
            return false;
        }
        if (TextUtils.isEmpty(mRoleInfo.getServerName())) {
            MCLog.e(TAG, BeanConstants.Log_zmKnsNvfUy);
            ToastUtil.show(BeanConstants.S_xjRajCUyjz);
            roleCallBack.onUploadComplete("-1");
            return false;
        }
        return true;
    }


}
