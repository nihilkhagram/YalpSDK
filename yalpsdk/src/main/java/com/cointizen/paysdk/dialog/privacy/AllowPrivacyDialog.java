package com.cointizen.paysdk.dialog.privacy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.utils.WebViewUtil;
import com.cointizen.paysdk.dialog.DialogConstants;

/**
 * 描述：关于小号功能说明
 * 时间: 2018-07-14 13:45
 */
public class AllowPrivacyDialog extends DialogFragment {

    private static final String TAG = "AboutAboutAddAccountDialog";
    private Context activity;

    private final String privacyStr = DialogConstants.S_wnkxDfRgUX +
            DialogConstants.S_ngEBiRpRHG +
            DialogConstants.S_pyVlmeKxVR +
            DialogConstants.S_PSqDfsYhLM +
            DialogConstants.S_hwshvXhJeF +
            DialogConstants.S_TXUmipptgE +
            DialogConstants.S_CyKHofKKJa +
            DialogConstants.S_SZoGVwjDBj;

    public AllowPrivacyDialog() { }

    @SuppressLint("ValidFragment")
    public AllowPrivacyDialog(Context activity) {
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_allowprivacy"), container, false);
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 0.5f;((Activity)activity).getWindow().setAttributes(params);

        TextView tvMsg = (TextView) containerView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "tv_msg"));

        String agreeTxt = String.format("%s", PrivacyManager.getInstance().userAgreementTitle());
        int agreeL = agreeTxt.length() + 2;
        String privacyTxt = String.format("%s", PrivacyManager.getInstance().privacyPolicyTitle());
        int privacyL = privacyTxt.length() + 2;

        String tipStr = String.format(privacyStr, privacyTxt, agreeTxt, privacyTxt, agreeTxt, privacyTxt);
        int firstPos = tipStr.indexOf(DialogConstants.S_xnsIMqIsyI + agreeTxt + DialogConstants.S_CScgyhifQP);
        firstPos = (firstPos < 0)?0:firstPos;
        int secondPos = firstPos + agreeL + 1;
        int thirdPos = tipStr.indexOf(DialogConstants.S_xnsIMqIsyI + agreeTxt + DialogConstants.S_CScgyhifQP, secondPos);
        thirdPos = (thirdPos<0)?0:thirdPos;
        int fourPos = thirdPos + agreeL + 1;

        SdkClickableSpan privacyClick = new SdkClickableSpan(PrivacyManager.getInstance().privacyPolicyUrl());
        SdkClickableSpan secondPrivacyClick = new SdkClickableSpan(PrivacyManager.getInstance().privacyPolicyUrl());
        SdkClickableSpan agreeClick = new SdkClickableSpan(PrivacyManager.getInstance().agreementUrl());
        SdkClickableSpan secondAgreeClick = new SdkClickableSpan(PrivacyManager.getInstance().agreementUrl());

        SpannableString privacySpanned = new SpannableString(tipStr);
//        privacySpanned.setSpan(agreeClick, firstPos, firstPos + agreeL, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        privacySpanned.setSpan(secondAgreeClick, thirdPos, thirdPos + agreeL, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        privacySpanned.setSpan(privacyClick, secondPos, secondPos + privacyL, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        privacySpanned.setSpan(secondPrivacyClick, fourPos, fourPos + privacyL, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        //注意：此时必须加这一句，不然点击事件不会生效
        tvMsg.setMovementMethod(LinkMovementMethod.getInstance());
        tvMsg.setText(privacySpanned);

        Button btnReject = (Button) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_privacy_reject"));
        Button btnagree = (Button) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_privacy_agree"));
        btnagree.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            SharedPreferencesUtils.setIsFirstPrivacy(activity, false);
            PrivacyManager.getInstance().returnResult(1);
        });
        btnReject.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            PrivacyManager.getInstance().secondPrivacy();
        });
//        new MCHEtUtils().etHandle(activity, etAccount, rlClear, null, null);
        setCancelable(false);
        this.getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismissAllowingStateLoss();
                return true;
            } else {
                return false;
            }
        });
        return containerView;
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
        float size_x = 0;
        float size_y = 0;
        int width = windowSize.x;
        int height = windowSize.y;
        if (width >= height) {// 横屏
            size_x = 0.7f;
            size_y = 0.8f;
//            window.getAttributes().width = (int) (windowSize.y * 0.8);
            window.getAttributes().width = windowSize.x;
//            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.getAttributes().height = windowSize.y;
        } else {// 竖屏
            size_x = 0.9f;
            size_y = 0.855f;
//            window.getAttributes().width = (int) (windowSize.x * 0.85);
            window.getAttributes().width = windowSize.x;
//            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.getAttributes().height = windowSize.y;
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mBundle;
        private AllowPrivacyDialog dialog;

        public Builder() {
            mBundle = new Bundle();
        }

        private AllowPrivacyDialog create(Activity activity) {
            dialog = new AllowPrivacyDialog(activity);
            // 1,设置显示内容
            dialog.setArguments(mBundle);
            return dialog;
        }

        public AllowPrivacyDialog show(Activity activity, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            AllowPrivacyDialog dialog = create(activity);
            MCLog.d(TAG, "show AboutAddAccountDialog.");

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }


    }

    public class SdkClickableSpan extends ClickableSpan {

        String url = "";

        public SdkClickableSpan(String url){
            this.url = url;
        }

        @Override
        public void onClick(@NonNull View widget) {
            WebViewUtil.webView((Activity) activity, url, true);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#21b1eb"));//设置颜色
            ds.setUnderlineText(false);//去掉下划线
        }
    }

}
