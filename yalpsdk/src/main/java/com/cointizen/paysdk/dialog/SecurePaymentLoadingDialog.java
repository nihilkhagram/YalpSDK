package com.cointizen.paysdk.dialog;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SecurePaymentLoadingDialog extends DialogFragment {

    private static final String TAG = "PlatformLogin";

    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final char SUFFIX = '.';

    private Context mContent;
    private ImageView iv_route;
    private TextView tv;
    private TextView tv_point;

    private RotateAnimation mAnim;

    private View.OnClickListener mCloseToLogin;

    public SecurePaymentLoadingDialog() {}

    @SuppressLint("ValidFragment")
    public SecurePaymentLoadingDialog(Context con) {
        mContent = con;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(mContent,
                "layout", "mch_activity_secure_payment_loading_dialog_layout"), container, false);

        initView(containerView);

        setCancelable(true);
        return containerView;
    }

    private void initView(View containerView) {

//        containerView.findViewById(MCHInflaterUtils
//                .getIdByName(mContent, "id", "mch_img_phonelogin_close"))
//                .setOnClickListener(closeClick);
        iv_route = containerView.findViewById(MCHInflaterUtils.getControl(mContent, "iv_route"));
        tv = containerView.findViewById(MCHInflaterUtils.getControl(mContent, "tv"));
        tv_point = containerView.findViewById(MCHInflaterUtils.getControl(mContent, "tv_point"));

        initAnim();
    }

    private void initAnim() {
        mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f, Animation.RESTART,0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
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
        // 1, 设置对话框的大小
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

    @Override
    public void onResume() {
        super.onResume();
        MCLog.e(TAG, "----- onResume -----");
        iv_route.startAnimation(mAnim);
        handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
    }

    @Override
    public void dismiss() {
        mAnim.cancel();
        super.dismiss();
    }

    View.OnClickListener closeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismissAllowingStateLoss();
            if (mCloseToLogin != null) {
                mCloseToLogin.onClick(view);
            }
        }
    };

    private final Handler handler = new Handler(Looper.getMainLooper()){
        private int num = 0;

        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_TITLE_WHAT) {
                StringBuilder builder = new StringBuilder();
                if (num >= MAX_SUFFIX_NUMBER) {
                    num = 0;
                }
                num ++;
                for (int i = 0;i < num;i++) {
                    builder.append(SUFFIX);
                }
                tv_point.setText(builder.toString());
                if (isVisible()) {
                    handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
                } else {
                    num = 0;
                }
            }
        };
    };

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mmBundle;

        public Builder() {
            mmBundle = new Bundle();
        }

        private SecurePaymentLoadingDialog create(Context context) {
            final SecurePaymentLoadingDialog dialog = new SecurePaymentLoadingDialog(context);
            // 1,设置显示内容
            dialog.setArguments(this.mmBundle);

            return dialog;
        }

        public SecurePaymentLoadingDialog show(Context context, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            SecurePaymentLoadingDialog dialog = create(context);
            MCLog.d(TAG, "show SecurePaymentLoadingDialog.");
//			dialog.show(fm, TAG);

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }
    }

}

