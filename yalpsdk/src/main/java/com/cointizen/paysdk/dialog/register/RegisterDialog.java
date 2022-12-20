package com.cointizen.paysdk.dialog.register;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cointizen.paysdk.bean.SwitchManager;
import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.http.process.VerificationMailCodeProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.callback.PlatformRegisterCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;
import com.cointizen.paysdk.utils.WebViewUtil;
import com.cointizen.paysdk.dialog.DialogConstants;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RegisterDialog extends DialogFragment {

    private static final String TAG = "ForgetPasswdDialog";
    private Context mContent;

    private View.OnClickListener mCloseToLogin;
    private PlatformRegisterCallback mRegisterCallback;

//    private LinearLayout ll_phone_register;
    private LinearLayout ll_account_register;
    private LinearLayout ll_email_register;
//    private TextView txt_btn_select_phone;
    private TextView txt_btn_select_account;
    private TextView txt_btn_select_email;
//    private View select_line_phone;
    private View select_line_account;
    private View select_line_email;


    private TextView tvAgreement;
    private TextView btnVCode, btnRegister, btnEmailCode;
    private ImageView imgCheck;
    private EditText edtEmail, edtEmailCode, edtEmailPassword;
//    private EditText edtPhone, edtPhoneCode, edtPhonePassword;
    private EditText edtAccount, edtActPwd, edtActRePwd;
    private boolean isReadAgreemen = true;
    private int registerType = 2;  //0:手机号注册 1:帐号方式注册 2:邮箱注册

    public RegisterDialog() {
    }

    @SuppressLint("ValidFragment")
    public RegisterDialog(Context con) {
        mContent = con;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(mContent,
                "layout", "mch_dialog_register"), container, false);
        initView(containerView);

        setCancelable(false);
        return containerView;
    }

    private void initView(View containerView) {
        txt_btn_select_email = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_email_change_normal"));
        select_line_email = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_email_selected_line"));
        select_line_email.setVisibility(View.VISIBLE);
        txt_btn_select_email.setOnClickListener(changeToEmailClick);
        txt_btn_select_account = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_account_change_normal"));
        select_line_account = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_account_selected_line"));
        select_line_account.setVisibility(View.GONE);
        txt_btn_select_account.setOnClickListener(changeToAccountClick);
//        txt_btn_select_phone = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_phone_change_selected"));
//        select_line_phone = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_phone_selected_line"));
//        select_line_phone.setVisibility(View.GONE);
//        txt_btn_select_phone.setOnClickListener(changeToPhoneClick);

        ll_email_register = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_register_ll_email_change"));
        ll_email_register.setVisibility(View.VISIBLE);
//        ll_phone_register = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_register_ll_phone_change"));
//        ll_phone_register.setVisibility(View.GONE);
        ll_account_register = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_register_ll_account_change"));
        ll_account_register.setVisibility(View.GONE);


        btnEmailCode = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "btn_email_vcode"));
        edtEmailPassword = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_register_email_password"));
        edtEmailPassword.setTransformationMethod(new PasswordTransformationMethod());
        edtEmailCode = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_email_sms_code"));
        edtEmail = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_mc_platform_login_email"));

        btnVCode = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "btn_vcode"));
//        edtPhonePassword = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_register_password"));
//        edtPhoneCode = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_sms_code"));
//        edtPhone = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_mc_platform_login_account"));
        btnRegister = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "btn_register"));
        edtAccount = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_account"));
        edtActPwd = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_account_password"));
        edtActRePwd = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "edt_account_repassword"));

        imgCheck = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "img_check"));
        tvAgreement = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "mch_txt_register_agreement"));
        tvAgreement.setText(String.format(DialogConstants.S_jDptaiiNKv, PrivacyManager.getInstance().userAgreementTitle()));

        tvAgreement.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                WebViewUtil.read(mContent, PrivacyManager.getInstance().agreementUrl());
            }
        });

        imgCheck.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (!isReadAgreemen){
                    isReadAgreemen = true;
                    imgCheck.setImageDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(mContent, "mch_register_selected")));
                }else {
                    isReadAgreemen = false;
                    imgCheck.setImageDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(mContent, "mch_register_unselected")));
                }
            }
        });

        //获取验证码
//        btnVCode.setOnClickListener(requestSMSCode);

        //获取邮箱验证码
        btnEmailCode.setOnClickListener(requestEmailCode);

        //立即注册
        btnRegister.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (registerType == 0) { //手机号注册
//                            String phone  = edtPhone.getText().toString().trim();
//                            String phoneCode = edtPhoneCode.getText().toString().trim();
//                            String phonePwd = edtPhonePassword.getText().toString().trim();
//                            mRegisterCallback.platformRegister(phone, phonePwd, "", phoneCode, 0, isReadAgreemen);
                }else if(registerType == 1) { //账密注册
                    String account  = edtAccount.getText().toString().trim();
                    String password = edtActPwd.getText().toString().trim();
                    String rePassword = edtActRePwd.getText().toString().trim();
                    mRegisterCallback.platformRegister(account, password, rePassword, "", 1, isReadAgreemen);
                }else { //邮箱注册
                    String email  = edtEmail.getText().toString().trim();
                    String emailCode = edtEmailCode.getText().toString().trim();
                    String emailPwd = edtEmailPassword.getText().toString().trim();
                    mRegisterCallback.platformRegister(email, emailPwd, "", emailCode, 2, isReadAgreemen);
                }

            }
        });

        containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContent, "id", "mch_img_register_close"))
                .setOnClickListener(closeClick);

        initRegisterStatus(containerView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(mContent, "style", "mch_MCCustomDialog"));
    }

    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        Window window = getDialog().getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        int size_x, size_y;
        int width = windowSize.x;
        int height = windowSize.y;
        if (width >= height) {// 横屏
            size_x = (int) (height * 0.85);
            size_y = (int) (height * 0.88);
        } else {// 竖屏
            size_x = (int) (width * 0.786);
            size_y = (int) (width * 0.8138);
        }
        window.getAttributes().width = size_x;
        window.getAttributes().height = size_y;
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    private void initRegisterStatus(View containerView) {
        //注册都打开，不用修改
        if (!SwitchManager.getInstance().unopenAllRegister()) {
            return;
        }
        //邮箱注册关闭
        if (!SwitchManager.getInstance().isOpenEmailRegister()) {
            txt_btn_select_email.setVisibility(View.GONE);
            select_line_email.setVisibility(View.GONE);
            ll_email_register.setVisibility(View.GONE);
        }
        //普通注册关闭
        if (!SwitchManager.getInstance().isOpenAccountRegister()) {
            txt_btn_select_account.setVisibility(View.GONE);
            select_line_account.setVisibility(View.GONE);
            ll_account_register.setVisibility(View.GONE);
        }
        //手机号关闭
        if (!SwitchManager.getInstance().isOpenPhoneRegister()) {
//            txt_btn_select_phone.setVisibility(View.GONE);
//            select_line_phone.setVisibility(View.GONE);
//            ll_phone_register.setVisibility(View.GONE);
        }
        //选择一个默认显示
        if (SwitchManager.getInstance().isOpenEmailRegister()) {
            txt_btn_select_email.performClick();
        }else if (SwitchManager.getInstance().isOpenAccountRegister()) {
            txt_btn_select_account.performClick();
        }else {
//            txt_btn_select_phone.performClick();
        }
    }

    private void resetRegisterType() {
        txt_btn_select_email.setTextColor(Color.parseColor("#999999"));
        select_line_email.setVisibility(View.GONE);

        txt_btn_select_account.setTextColor(Color.parseColor("#999999"));
        select_line_account.setVisibility(View.GONE);

//        txt_btn_select_phone.setTextColor(Color.parseColor("#999999"));
//        select_line_phone.setVisibility(View.GONE);

    }

    View.OnClickListener changeToAccountClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            resetRegisterType();
            registerType = 1;
            select_line_account.setVisibility(View.VISIBLE);
            txt_btn_select_account.setTextColor(Color.parseColor("#000000"));
            ll_email_register.setVisibility(View.GONE);
//            ll_phone_register.setVisibility(View.GONE);
            ll_account_register.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener changeToPhoneClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            resetRegisterType();
            registerType = 0;
//            select_line_phone.setVisibility(View.VISIBLE);
//            txt_btn_select_phone.setTextColor(Color.parseColor("#000000"));
            ll_email_register.setVisibility(View.GONE);
//            ll_phone_register.setVisibility(View.VISIBLE);
            ll_account_register.setVisibility(View.GONE);
        }
    };

    View.OnClickListener changeToEmailClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            resetRegisterType();
            registerType = 2;
            select_line_email.setVisibility(View.VISIBLE);
            txt_btn_select_email.setTextColor(Color.parseColor("#000000"));
            ll_email_register.setVisibility(View.VISIBLE);
//            ll_phone_register.setVisibility(View.GONE);
            ll_account_register.setVisibility(View.GONE);
        }
    };

//    View.OnClickListener requestSMSCode = new OnMultiClickListener() {
//        @Override
//        public void onMultiClick(View v) {
////            String phone  = edtPhone.getText().toString().trim();
//            if (TextUtils.isEmpty(phone)){
//                ToastUtil.show(mContent, DialogConstants.S_WnCEnpGHPm);
//                return;
//            }

//            String valCode = Constant.REGULAR_PHONENUMBER;
//            if (!phone.matches(valCode)) {
//                ToastUtil.show(mContent, DialogConstants.S_AzDFwbXdpZ);
//                return;
//            }

//            AppUtils.ShowLoadDialog(mContent);
//            VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
//            verifyCodeProcess.setPhone(phone);
//            verifyCodeProcess.setReg("1");
//            verifyCodeProcess.post(mHandler);
//        }
//    };

    View.OnClickListener requestEmailCode = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            String email = edtEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)){
                ToastUtil.show(mContent, DialogConstants.S_DllGsaYyEZ);
                return;
            }
            if (!Constant.REGULAR_MAIL(email)) {
                ToastUtil.show(mContent, DialogConstants.S_tZdiulTCId);
                return;
            }

            AppUtils.ShowLoadDialog(mContent);
            VerificationMailCodeProcess verifyCodeProcess = new VerificationMailCodeProcess();
            verifyCodeProcess.setMail(email);
            verifyCodeProcess.setCodeType(1);
            verifyCodeProcess.post(emailCodeHandler);
        }
    };

    View.OnClickListener closeClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View view) {
            dismissAllowingStateLoss();
            if (mCloseToLogin != null) {
                mCloseToLogin.onClick(view);
            }
        }
    };

    public void setCloseToLogin(View.OnClickListener closeToLogin) {
        this.mCloseToLogin = closeToLogin;
    }

    public void setmRegisterCallback(PlatformRegisterCallback mRegisterCallback) {
        this.mRegisterCallback = mRegisterCallback;
    }

    Handler emailCodeHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.VERIFYCODE_REQUEST_SUCCESS:
                    ToastUtil.show(mContent, DialogConstants.S_prCZghwSEj);
                    AppUtils.getTiming(btnEmailCode);
                    break;
                case Constant.VERIFYCODE_REQUEST_FAIL:
                    String res = (String) msg.obj;
                    if (!TextUtils.isEmpty(res)){
                        ToastUtil.show(mContent,res);
                    }
                    break;
            }
            AppUtils.DissLoadDialog();
            return false;
        }
    });

    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.VERIFYCODE_REQUEST_SUCCESS:
                    ToastUtil.show(mContent, DialogConstants.S_prCZghwSEj);
                    AppUtils.getTiming(btnVCode);
                    break;
                case Constant.VERIFYCODE_REQUEST_FAIL:
                    String res = (String) msg.obj;
                    ToastUtil.show(mContent, res);
                    break;
            }
            AppUtils.DissLoadDialog();
        }
    };

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mmBundle;

        private View.OnClickListener mmCloseToLogin;
        private PlatformRegisterCallback mmRegisterCallback;
        
        public Builder() {
            mmBundle = new Bundle();
        }

        public Builder setCloseToLogin(View.OnClickListener closeToLogin) {
            this.mmCloseToLogin = closeToLogin;
            return this;
        }

        public Builder setRegisterCallback(PlatformRegisterCallback mmRegisterCallback) {
            this.mmRegisterCallback = mmRegisterCallback;
            return this;
        }

        private RegisterDialog create(Context context) {
            final RegisterDialog dialog = new RegisterDialog(context);
            // 1,设置显示内容
            dialog.setArguments(mmBundle);
            dialog.setmRegisterCallback(mmRegisterCallback);
            dialog.setCloseToLogin(this.mmCloseToLogin);

            return dialog;
        }

        public RegisterDialog show(Context context, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            RegisterDialog dialog = create(context);

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, Constant.FRAGMENT_TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }
    }
}
