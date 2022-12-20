package com.cointizen.paysdk.dialog.permission;

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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
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
import com.cointizen.paysdk.dialog.privacy.SdkTextClickableSpan;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.dialog.DialogConstants;


public class PermissionTipsDialog extends DialogFragment {

    private static final String TAG = "AboutAboutAddAccountDialog";
    private Context activity;

    private final String privacyStr = DialogConstants.S_TFvAPbegKO;

    public PermissionTipsDialog() { }

    @SuppressLint("ValidFragment")
    public PermissionTipsDialog(Context activity) {
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_permission_tips"), container, false);
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 0.5f;
        ((Activity)activity).getWindow().setAttributes(params);

        TextView tvMsg = (TextView) containerView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "tv_msg"));
        String agreeTxt = String.format("%s", PrivacyManager.getInstance().privacyPolicyTitle());
        String tipStr = String.format(privacyStr, agreeTxt);

        int firstPos = tipStr.indexOf(DialogConstants.S_xnsIMqIsyI + agreeTxt + DialogConstants.S_CScgyhifQP);
        SdkTextClickableSpan privacyClick = new SdkTextClickableSpan((Activity)activity, PrivacyManager.getInstance().privacyPolicyUrl());
        SpannableString privacySpanned = new SpannableString(tipStr);
        privacySpanned.setSpan(privacyClick, firstPos, firstPos + agreeTxt.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvMsg.setMovementMethod(LinkMovementMethod.getInstance());
        tvMsg.setText(privacySpanned);


        Button btnCancel = (Button) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_permission"));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.getInstance().setPrivacyCollecty(activity);
                dismissAllowingStateLoss();
            }
        });

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
        int width = windowSize.x;
        int height = windowSize.y;
        if (width >= height) {// 竖屏
            window.getAttributes().width = (int) (height * 0.85);
            window.getAttributes().height = (int) (height * 0.88);
        } else {// 横屏
            window.getAttributes().width = (int) (width * 0.85);
            window.getAttributes().height = (int) (width * 0.88);
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mBundle;
        private PermissionTipsDialog dialog;

        private View.OnClickListener mmDelSuccessClick;

        public Builder() {
            mBundle = new Bundle();
        }

        private PermissionTipsDialog create(Activity activity) {
            dialog = new PermissionTipsDialog(activity);
            // 1,设置显示内容
            dialog.setArguments(mBundle);

            return dialog;
        }

        public PermissionTipsDialog show(Activity activity, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            PermissionTipsDialog dialog = create(activity);
            MCLog.d(TAG, "show AboutAddAccountDialog.");

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }


    }

}
