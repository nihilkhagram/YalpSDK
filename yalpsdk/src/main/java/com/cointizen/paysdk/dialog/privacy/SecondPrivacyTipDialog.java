package com.cointizen.paysdk.dialog.privacy;

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
import com.cointizen.paysdk.dialog.DialogConstants;


/**
 * 描述：关于小号功能说明
 * 时间: 2018-07-14 13:45
 */
public class SecondPrivacyTipDialog extends DialogFragment {

    private static final String TAG = "AboutAboutAddAccountDialog";
    private Context activity;

    private final String privacyStr = DialogConstants.S_PivUELTkMR +
            DialogConstants.S_pFlRXNGhKa;

    public SecondPrivacyTipDialog() { }

    @SuppressLint("ValidFragment")
    public SecondPrivacyTipDialog(Context activity) {
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_secondprivacy"), container, false);
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 0.5f;((Activity)activity).getWindow().setAttributes(params);

        TextView tvMsg = (TextView) containerView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "tv_msg"));
        tvMsg.setText(privacyStr);

        TextView btnReject = (TextView) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_privacy_reject"));
        Button btnagree = (Button) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_privacy_agree"));
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                PrivacyManager.getInstance().returnResult(0);
                System.exit(0);
            }
        });
        btnagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                SharedPreferencesUtils.setIsFirstPrivacy(activity, false);
                PrivacyManager.getInstance().returnResult(1);
            }
        });
//        new MCHEtUtils().etHandle(activity, etAccount, rlClear, null, null);
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
        private SecondPrivacyTipDialog dialog;

        public Builder() {
            mBundle = new Bundle();
        }

        private SecondPrivacyTipDialog create(Activity activity) {
            dialog = new SecondPrivacyTipDialog(activity);
            // 1,设置显示内容
            dialog.setArguments(mBundle);
            return dialog;
        }

        public SecondPrivacyTipDialog show(Activity activity, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            SecondPrivacyTipDialog dialog = create(activity);
            MCLog.d(TAG, "show AboutAddAccountDialog.");

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }


    }

}
