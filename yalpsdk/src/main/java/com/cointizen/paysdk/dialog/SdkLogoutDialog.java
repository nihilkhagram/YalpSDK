package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.lidroid.xutils.BitmapUtils;
import com.cointizen.open.ApiCallback;
import com.cointizen.open.FlagControl;
import com.cointizen.open.GPExitResult;
import com.cointizen.open.LogoutCallback;
import com.cointizen.paysdk.bean.InitModel;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.LogoutADVModel;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.LogoutProcess;
import com.cointizen.paysdk.utils.BitmapHelp;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.view.round.NiceImageView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SdkLogoutDialog extends DialogFragment {

    private static final String TAG = "PlatformLogin";

    private Context mContent;

    private View btnLikai;
    private View btnFanhui;
    private NiceImageView imgTu;
    private BitmapUtils bitmapUtils;
    private LogoutADVModel obj;

    private LogoutCallback logoutCallback;
    private boolean isExitActivity;

    public SdkLogoutDialog() {}

    @SuppressLint("ValidFragment")
    public SdkLogoutDialog(Context con) {
        mContent = con;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(mContent,
                "layout", "mch_dialog_logout"), container, false);

        bitmapUtils = BitmapHelp.getBitmapUtils(mContent);

        initView(containerView);
        queryAdsInfo();

        setCancelable(false);
        return containerView;
    }

    private void initView(View containerView) {
        btnLikai = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "btn_mc_likai"));
        btnFanhui = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "btn_mc_fanhui"));
        imgTu = containerView.findViewById(MCHInflaterUtils.getIdByName(mContent, "id", "img_icon"));
        imgTu.setCornerTopRightRadius(10);
        imgTu.setCornerTopLeftRadius(10);
        imgTu.setCornerBottomRightRadius(0);
        imgTu.setCornerBottomLeftRadius(0);

        btnLikai.setOnClickListener(exitGameClick);
        btnFanhui.setOnClickListener(closeClick);
        imgTu.setOnClickListener(adsClick);

        containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContent, "id", "btn_close"))
                .setOnClickListener(closeClick);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(mContent, "style", "mch_MCHTransparent"));
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

    View.OnClickListener closeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismissAllowingStateLoss();
            if (null != ApiCallback.getExitObsv()) {
                GPExitResult exitResult = new GPExitResult();
                exitResult.mResultCode = -3;
                ApiCallback.getExitObsv().onExitFinish(exitResult);
            }
        }
    };

    View.OnClickListener exitGameClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismissAllowingStateLoss();
            logout();
        }
    };

    View.OnClickListener adsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openBrowser();
        }
    };

    private void logout(){
        try {
            InitModel.init().offLine((Activity) mContent, true);
            UserLoginSession.getInstance().clearUserInfoAll();
            SharedPreferencesUtils.getInstance().setLoggedOut(getActivity(),true);     //注销状态设为trueThread.sleep(500);
            Thread.sleep(100);

            if (isExitActivity) {
                ((Activity)mContent).finish();
            }

            FlagControl.BUTTON_CLICKABLE = true;
            FlagControl.isLogin = false;
            FlagControl.isFloatingOpen = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (logoutCallback != null) {
            logoutCallback.logoutResult("1");
        }
    }

    public void openBrowser() {
        if(obj == null || obj.getUrl() == null){
           return;
        }
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(obj.getUrl()));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(mContent.getPackageManager()) != null) {
//            final ComponentName componentName = intent.resolveActivity(mContent.getPackageManager());
            mContent.startActivity(Intent.createChooser(intent, DialogConstants.S_yTyfxqyfXR));
        } else {
            ToastUtil.show(mContent,DialogConstants.S_QwcLQJNGrq);
        }
    }

    private void queryAdsInfo() {
        LogoutProcess logoutProcess = new LogoutProcess();
        logoutProcess.post(handler);
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case Constant.LOGOUTADV_SUCCESS:
                    obj = (LogoutADVModel) msg.obj;
                    bitmapUtils.display(imgTu, obj.getData());
                    break;
                case Constant.LOGOUTADV_FAIL:

                    break;
            }
            return false;
        }
    });

    public void setLogoutCallback(LogoutCallback logoutCallback) {
        this.logoutCallback = logoutCallback;
    }

    public void setExitActivity(boolean exitActivity) {
        isExitActivity = exitActivity;
    }

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mmBundle;

        private LogoutCallback mmLogoutCallback;

        private boolean mmIsExitActivity;

        public Builder() {
            mmBundle = new Bundle();
        }

        public Builder setLogoutCallback(LogoutCallback logoutCallback) {
            this.mmLogoutCallback = logoutCallback;
            return this;
        }

        public Builder setIsExitActivity(boolean isExitActivity) {
            this.mmIsExitActivity = isExitActivity;
            return this;
        }

        private SdkLogoutDialog create(Context context) {
            final SdkLogoutDialog dialog = new SdkLogoutDialog(context);
            // 1,设置显示内容
            dialog.setArguments(this.mmBundle);
            dialog.setLogoutCallback(this.mmLogoutCallback);
            dialog.setExitActivity(this.mmIsExitActivity);

            return dialog;
        }

        public SdkLogoutDialog show(Context context, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            SdkLogoutDialog dialog = create(context);
            MCLog.d(TAG, "show SdkLogoutDialog.");
//			dialog.show(fm, TAG);

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }
    }

}
