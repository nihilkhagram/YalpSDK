package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.activity.MCHChooseAccountActivity;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class YouKeLoginDialog extends DialogFragment {

    private static final String TAG = "PlatformLogin";

    private Context mContent;

    private UserLogin mLoginInfo;
    private String mAccount = "";
    private String mPassword = "";
//    private View.OnClickListener mCloseToLogin;

    public YouKeLoginDialog() {}

    @SuppressLint("ValidFragment")
    public YouKeLoginDialog(Context con) {
        mContent = con;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(mContent,
                "layout", "mch_dialog_ykloginok"), container, false);

        initView(containerView);

        setCancelable(true);
        return containerView;
    }

    private void initView(View containerView) {

//        containerView.findViewById(MCHInflaterUtils
//                .getIdByName(mContent, "id", "mch_img_phonelogin_close"))
//                .setOnClickListener(closeClick);
        TextView tvAccount = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "tv_mch_account"));
        TextView tvPassword = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "tv_mch_pass"));

        tvAccount.setText(mAccount);
        tvPassword.setText(mPassword);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(mContent, "style", "mch_MyDialogStyle"));
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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mLoginInfo != null) {
            //后台如果开启了小号功能则跳转到选择小号界面，否则直接登录
            if (mLoginInfo.getIsOpenSmallAccount() == 1){
                Constant.IsOpenSmallAccount = true;
                Intent intent = new Intent(YalpGamesSdk.getMainActivity(), MCHChooseAccountActivity.class);
                intent.putExtra("user_small_list", mLoginInfo);
                YalpGamesSdk.getMainActivity().startActivity(intent);
            }else {
                Constant.IsOpenSmallAccount = false;
                LoginModel.instance().smallAccountLogin(mLoginInfo);
            }
        }
    }

    //    View.OnClickListener closeClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            dismissAllowingStateLoss();
//            if (mCloseToLogin != null) {
//                mCloseToLogin.onClick(view);
//            }
//        }
//    };


    public void setLoginInfo(UserLogin loginInfo) {
        this.mLoginInfo = loginInfo;
    }

    public void setAccount(String account) {
        this.mAccount = account;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mmBundle;

        private UserLogin mmLoginInfo;
        private String mmAccount;
        private String mmPassword;

        public Builder() {
            mmBundle = new Bundle();
        }

        public Builder setLoginInfo(UserLogin loginInfo) {
            this.mmLoginInfo = loginInfo;
            return this;
        }

        public Builder setAccount(String account) {
            this.mmAccount = account;
            return this;
        }

        public Builder setPassword(String password) {
            this.mmPassword = password;
            return this;
        }

        private YouKeLoginDialog create(Context context) {
            final YouKeLoginDialog dialog = new YouKeLoginDialog(context);
            // 1,设置显示内容
            dialog.setArguments(this.mmBundle);
            dialog.setLoginInfo(this.mmLoginInfo);
            dialog.setAccount(this.mmAccount);
            dialog.setPassword(this.mmPassword);

            return dialog;
        }

        public YouKeLoginDialog show(Context context, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            YouKeLoginDialog dialog = create(context);
            MCLog.d(TAG, "show YouKeLoginDialog.");
//			dialog.show(fm, TAG);

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }
    }

}

