package com.cointizen.paysdk.dialog.delaccount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Paint;
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
import android.widget.TextView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.callback.LogoutVerifyCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.LogoutVerifyEntity;
import com.cointizen.paysdk.http.DeleteAccount.DeleteAccountProcess;
import com.cointizen.paysdk.http.logoutverify.LogoutVerifyProcess;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.WebViewUtil;


public class DeleteAccountDialog extends DialogFragment {

    private static final String TAG = "AboutAboutAddAccountDialog";
    private Context activity;

    private TextView btnDelAgreement;

    private View.OnClickListener mDelSuccessClick;
    private LogoutVerifyCallback mLogoutVerifyClick;

    public DeleteAccountDialog() { }

    @SuppressLint("ValidFragment")
    public DeleteAccountDialog(Context activity) {
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_deleteaccount"), container, false);
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 0.5f;
        ((Activity)activity).getWindow().setAttributes(params);

        Button btnSubmit = (Button) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_del_submit"));
        Button btnCancel = (Button) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_del_cancel"));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutVerify();
            }
        });

        TextView btnDelAgreement = (TextView) containerView.findViewById(MCHInflaterUtils.getControl(activity, "txt_del_agreement"));
        btnDelAgreement.setText(PrivacyManager.getInstance().logoutName());
        btnDelAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        if (!TextUtils.isEmpty(PrivacyManager.getInstance().logoutName())) {
            btnDelAgreement.setVisibility(View.VISIBLE);
            btnDelAgreement.setText(PrivacyManager.getInstance().logoutName());
            btnDelAgreement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewUtil.webView((Activity)activity, PrivacyManager.getInstance().logoutUrl(), true);
                }
            });
        }else {
            btnDelAgreement.setVisibility(View.GONE);
        }

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
        LogoutVerifyProcess deleteAccountProcess = new LogoutVerifyProcess();
        deleteAccountProcess.type = "1";
        deleteAccountProcess.post(logoutVerifyHandle);
    }

    private final Handler logoutVerifyHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case Constant.LOGOUT_VERIFY:
                    LogoutVerifyEntity logoutVerifyEntity = (LogoutVerifyEntity)message.obj;
                    if ("3".equals(logoutVerifyEntity.getType())) {
                        PrivacyManager.getInstance().isLogout = true;
                        if (mDelSuccessClick != null) {
                            mDelSuccessClick.onClick(null);
                        }
                    }else {
                        if (mLogoutVerifyClick != null) {
                            mLogoutVerifyClick.onResult(logoutVerifyEntity.getType(),
                                    logoutVerifyEntity.getPhoneOrEmail());
                        }
                    }
                    dismissAllowingStateLoss();
                    break;
                case Constant.HTTP_REQUEST_FAIL:
                    ToastUtil.show(activity, (String) message.obj);
                    break;
            }
            return false;
        }
    });

    private void delleteAccount() {
        DeleteAccountProcess deleteAccountProcess = new DeleteAccountProcess();
        deleteAccountProcess.type = "1";
        deleteAccountProcess.post(delAccountHandle);
    }

    private final Handler delAccountHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case Constant.DELETE_ACCOUNT_SUCCESS:
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

    public void setDelSuccessClick(View.OnClickListener delSuccessClick) {
        this.mDelSuccessClick = delSuccessClick;
    }

    public void setLogoutVerifyClick(LogoutVerifyCallback logoutVerifyClick) {
        this.mLogoutVerifyClick = logoutVerifyClick;
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
        private DeleteAccountDialog dialog;

        private View.OnClickListener mmDelSuccessClick;
        private LogoutVerifyCallback mmLogoutVerifyClick;

        public Builder() {
            mBundle = new Bundle();
        }

        public Builder setDelSuccessClick(View.OnClickListener delSuccessClick) {
            this.mmDelSuccessClick = delSuccessClick;
            return this;
        }

        public Builder setLogoutVerifyClick(LogoutVerifyCallback logoutVerifyClick) {
            this.mmLogoutVerifyClick = logoutVerifyClick;
            return this;
        }

        private DeleteAccountDialog create(Activity activity) {
            dialog = new DeleteAccountDialog(activity);
            // 1,设置显示内容
            dialog.setArguments(mBundle);
            dialog.setDelSuccessClick(this.mmDelSuccessClick);
            dialog.setLogoutVerifyClick(this.mmLogoutVerifyClick);

            return dialog;
        }

        public DeleteAccountDialog show(Activity activity, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            DeleteAccountDialog dialog = create(activity);
            MCLog.d(TAG, "show AboutAddAccountDialog.");

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }


    }

}
