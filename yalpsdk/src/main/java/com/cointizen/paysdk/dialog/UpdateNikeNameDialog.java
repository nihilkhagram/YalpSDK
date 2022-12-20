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
import android.text.Selection;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.callback.UpdateNikeNameCallback;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHEtUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

/**
 * 描述：修改昵称dialog
 * 作者：Administrator
 * 时间: 2018-03-06 10:41
 */

public class UpdateNikeNameDialog extends DialogFragment {

    private static final String TAG = "UpdateNikeNameDialog";

    /**
     * 上下文
     */
    private Context context;


    private UpdateNikeNameCallback mSureListener;

    public UpdateNikeNameDialog() {
    }

    @SuppressLint("ValidFragment")
    public UpdateNikeNameDialog(Context context) {
        this.context = context;
    }


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(context, "layout", "mch_dialog_update_nikename"), container, false);
        WindowManager.LayoutParams params = ((Activity)context).getWindow().getAttributes();
        params.alpha = 0.5f;((Activity)context).getWindow().setAttributes(params);

        final EditText etNikeName = (EditText) containerView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "et_mch_update_nike"));
        RelativeLayout rlClear = (RelativeLayout) containerView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "rl_mch_update_nikename_clear"));
        Button btnCancel = (Button) containerView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "btn_mch_update_nikename_cancel"));
        Button btnSure = (Button) containerView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "btn_mch_update_nikename_sure"));

        if (!TextUtils.isEmpty(UserLoginSession.getInstance().getChannelAndGame().getNikeName())){
            etNikeName.setText(UserLoginSession.getInstance().getChannelAndGame().getNikeName());
            Selection.setSelection(etNikeName.getText(), etNikeName.length());  // 使光标始终在最后位置
            rlClear.setVisibility(View.VISIBLE);
        }

        btnCancel.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        btnSure.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (null != mSureListener) {
                    dismissAllowingStateLoss();
                    mSureListener.updateNikeName(etNikeName.getText().toString().trim());
                }
            }
        });
        new MCHEtUtils().etHandle(context, etNikeName, rlClear, null, null);

        setCancelable(false);
        this.getDialog().setOnKeyListener(new OnKeyListener() {
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
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(context, "style", "mch_MCCustomDialog"));
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
            window.getAttributes().width = (int) (windowSize.x * 0.8);
            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public void setmSureClick(UpdateNikeNameCallback mSureListener) {
        this.mSureListener = mSureListener;
    }


    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mmBundle;
        private UpdateNikeNameCallback mmSureListener;

        public Builder() {
            mmBundle = new Bundle();
        }

        public Builder setSureClick(UpdateNikeNameCallback mmSureListener) {
            this.mmSureListener = mmSureListener;
            return this;
        }

        private UpdateNikeNameDialog create(Context context) {
            final UpdateNikeNameDialog dialog = new UpdateNikeNameDialog(context);
            // 1,设置显示内容
            dialog.setArguments(mmBundle);
            dialog.setmSureClick(mmSureListener);
            return dialog;
        }

        public UpdateNikeNameDialog show(Context context, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            UpdateNikeNameDialog dialog = create(context);
            MCLog.d(TAG, "show UpdateNikeNameDialog.");
//			dialog.show(fm, TAG);

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
        WindowManager.LayoutParams params = ((Activity)context).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity)context).getWindow().setAttributes(params);
    }
}
