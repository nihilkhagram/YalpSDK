package com.cointizen.paysdk.dialog;

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
import android.widget.TextView;

import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

/**
 * 描述：关于小号功能说明
 * 时间: 2018-07-14 13:45
 */
public class AboutAddAccountDialog extends DialogFragment {
    private static final String TAG = "AboutAboutAddAccountDialog";
    private Context activity;

    public AboutAddAccountDialog() {
    }

    @SuppressLint("ValidFragment")
    public AboutAddAccountDialog(Context activity) {
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_about_account"), container, false);
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 0.5f;((Activity)activity).getWindow().setAttributes(params);

        TextView btnCancel = containerView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "btn_tv_mch_cancel"));
        btnCancel.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                dismissAllowingStateLoss();
            }
        });
//        TextView tvMsg = (TextView) containerView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "tv_msg"));
//        tvMsg.setText("\u3000\u3000"+"解决同区服多个角色需要重复创建帐号的问题，同时也避免帐号过多不易记住。");
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
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(activity, "style", "mch_MCCustomDialog"));
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
            window.getAttributes().width = (int) (windowSize.y * 0.8);
            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {// 竖屏
            size_x = 0.9f;
            size_y = 0.855f;
            window.getAttributes().width = (int) (windowSize.x * 0.9);
            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }



    public static class aboutBuilder {
        /**
         * 存放数据的容器
         **/
        private Bundle mBundle;
        private AboutAddAccountDialog dialog;

        public aboutBuilder() {
            mBundle = new Bundle();
        }

        private AboutAddAccountDialog create(Activity activity) {
            dialog = new AboutAddAccountDialog(activity);
            // 1,设置显示内容
            dialog.setArguments(mBundle);
            return dialog;
        }

        public AboutAddAccountDialog show(Activity activity, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            AboutAddAccountDialog dialog = create(activity);
            MCLog.d(TAG, "show AboutAddAccountDialog.");

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity)activity).getWindow().setAttributes(params);
    }
}
