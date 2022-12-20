package com.cointizen.paysdk.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.ShareProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：游戏分享弹窗
 * 时间: 2018-10-15 10:12
 */

public class MCHShareActivity extends Activity {

    public static final String TAG = "MCShareActivity";

    private View btn_cancel;

    private boolean isGetData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        window.setDimAmount(0.5f);    //设置背景透明度   0~1
        super.onCreate(savedInstanceState);
        setContentView(MCHInflaterUtils.getLayout(this, "mch_popu_share"));
        TextView tvGame = findViewById(MCHInflaterUtils.getIdByName(this, "id", "tv_mch_game"));
        TextView tvShare = findViewById(MCHInflaterUtils.getIdByName(this, "id", "tv_mch_share"));
        btn_cancel = findViewById(MCHInflaterUtils.getIdByName(this, "id", "btn_cancel"));
        View view_null = findViewById(MCHInflaterUtils.getIdByName(this, "id", "view_mch_null"));
        view_null.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
        BtnOnClick btnOnClick = new BtnOnClick();
        btn_cancel.setOnClickListener(btnOnClick);

        ShareProcess shareNumProcess = new ShareProcess();
        shareNumProcess.post(shareHandle);
    }

    private Handler shareHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            isGetData = false;
            switch (msg.what) {
                case Constant.GET_SHARE_SUCCESS:
                    isGetData = true;
                    break;
            }
            return false;
        }
    });

    class BtnOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (!isGetData) {
                ToastUtil.show(YalpGamesSdk.getMainActivity(), ActivityConstants.S_QpwtuRcYhn);
                return;
            }
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1f;
        getWindow().setAttributes(params);
        //注释掉activity本身的过渡动画
        overridePendingTransition(0, 0);
    }
}
