package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;

import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.DiscountProgress;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppStatus;
import com.cointizen.paysdk.utils.AppStatusManager;
import com.cointizen.paysdk.utils.MCHInflaterUtils;


/**
 * 返利折扣Activity
 */
public class MCHDiscountRebateActivity extends MCHBaseActivity {
    private final String TAG = "MCDiscountRebateActivity";
    private RelativeLayout btnBack;
    private WebView webview;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE){
            finish();
            return;
        }
        setContentView(getLayout("mch_act_discount_rebate"));
        initvView();
    }

    private void initvView(){
        webview = findViewById(getId("mch_webview"));
        btnBack = findViewById(getId("btn_mch_back"));
        mProgressBar = findViewById(MCHInflaterUtils.getControl(this, "pro_web_progress"));

        btnBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finish();
            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);

        if (webview.isHardwareAccelerated()) webview.setLayerType(View.LAYER_TYPE_HARDWARE,null);  //开启硬件加速
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        webview.setWebViewClient(webViewClient);

        DiscountProgress progress = new DiscountProgress();
        progress.post(handler);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.DISCOUNT_SUCCESS:
                    String htmlData = msg.obj.toString();
                    webview.loadData(htmlData, "text/html;charset=utf-8","utf-8");
                    break;
                case Constant.DISCOUNT_FAIL:
                    break;
            }
        }
    };

    protected int getLayout(String layoutName) {
        return MCHInflaterUtils.getLayout(this, layoutName);
    }

    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(this, idName);
    }


    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 重写此方法可以让webview处理https请求
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }
    };
}
