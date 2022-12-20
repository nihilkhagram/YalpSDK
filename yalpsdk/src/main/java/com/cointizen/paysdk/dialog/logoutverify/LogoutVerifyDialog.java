package com.cointizen.paysdk.dialog.logoutverify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.logoutsms.LogoutSendSMSProcess;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.dialog.DialogConstants;

public class LogoutVerifyDialog extends DialogFragment {

    private static final String TAG = "AboutAboutAddAccountDialog";
    private Context activity;

    private EditText edtLogoutSms;

    private String mPhoneOrEmail = "";
    private String mVerifyType = "";

    private View.OnClickListener mDelSuccessClick;

    public LogoutVerifyDialog() { }

    @SuppressLint("ValidFragment")
    public LogoutVerifyDialog(Context activity) {
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_logoutverify"), container, false);
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 0.5f;
        ((Activity)activity).getWindow().setAttributes(params);

        edtLogoutSms = (EditText) containerView.findViewById(MCHInflaterUtils.getControl(activity, "edt_logout_sms"));

        Button btnSubmit = (Button) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_logout_submit"));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutVerify();
            }
        });

        String sendInfo = mPhoneOrEmail;
        if ("1".equals(mVerifyType)) {
            sendInfo = mPhoneOrEmail.substring(0, 3) + "****" + mPhoneOrEmail.substring(mPhoneOrEmail.length() - 3);
        }else {
            int pos = mPhoneOrEmail.indexOf("@");
            if (pos >= 0) {
                String exp = mPhoneOrEmail.substring(pos);
                String con = mPhoneOrEmail.substring(0, pos);
                if (con.length() > 5) {
                    sendInfo = con.substring(0, 3) + "****" + con.substring(con.length() - 2) + exp;
                }
            }
        }
        TextView txtTips = (TextView) containerView.findViewById(MCHInflaterUtils.getControl(activity, "tv_sms_tips"));
        txtTips.setText(String.format(DialogConstants.S_xrZBJRlsMl, sendInfo));

        setCancelable(false);
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissAllowingStateLoss();
                    return true;
                } else {
                    return false;
                }
            }
        });
        return containerView;
    }

    private void logoutVerify() {
        String code = edtLogoutSms.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show(activity, DialogConstants.S_uHeDKERHxd);
            return;
        }
        LogoutSendSMSProcess sendSMSProcess = new LogoutSendSMSProcess();
        sendSMSProcess.type = mVerifyType;
        sendSMSProcess.phoneOrEmail = mPhoneOrEmail;
        sendSMSProcess.code = code;
        sendSMSProcess.post(sendSMSHandle);
    }

    private final Handler sendSMSHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case Constant.LOGOUT_SEND_SMS:
                    dismissAllowingStateLoss();
                    PrivacyManager.getInstance().isLogout = true;
                    if (mDelSuccessClick != null) {
                    mDelSuccessClick.onClick(null);
                }
                    break;
                case Constant.HTTP_REQUEST_FAIL:
                    ToastUtil.show(activity, (String) message.obj);
                    break;
            }
            return false;
        }
    });

    public void setPhoneOrEmail(String phoneOrEmail) {
        this.mPhoneOrEmail = phoneOrEmail;
    }

    public void setVerifyType(String verifyType) {
        this.mVerifyType = verifyType;
    }

    public void setDelSuccessClick(View.OnClickListener delSuccessClick) {
        this.mDelSuccessClick = delSuccessClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(activity, "style", "mch_MCTipDialog"));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity)activity).getWindow().setAttributes(params);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        // 1, 设置对话框的大小
        Window window = getDialog().getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        window.getAttributes().width = windowSize.x;
        window.getAttributes().height = windowSize.y;
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mBundle;
        private LogoutVerifyDialog dialog;

        private String mmPhoneOrEmail = "";
        private String mmVerifyType = "";

        private View.OnClickListener mmDelSuccessClick;

        public Builder() {
            mBundle = new Bundle();
        }

        public Builder setDelSuccessClick(View.OnClickListener delSuccessClick) {
            this.mmDelSuccessClick = delSuccessClick;
            return this;
        }

        public Builder setPhoneOrEmail(String phoneOrEmail) {
            this.mmPhoneOrEmail = phoneOrEmail;
            return this;
        }

        public Builder setVerifyType(String verifyType) {
            this.mmVerifyType = verifyType;
            return this;
        }

        private LogoutVerifyDialog create(Activity activity) {
            dialog = new LogoutVerifyDialog(activity);
            // 1,设置显示内容
            dialog.setArguments(mBundle);
            dialog.setPhoneOrEmail(this.mmPhoneOrEmail);
            dialog.setVerifyType(this.mmVerifyType);
            dialog.setDelSuccessClick(this.mmDelSuccessClick);

            return dialog;
        }

        public LogoutVerifyDialog show(Activity activity, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            LogoutVerifyDialog dialog = create(activity);
            MCLog.d(TAG, "show AboutAddAccountDialog.");

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }


    }


}
