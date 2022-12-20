package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cointizen.open.ApiCallback;
import com.cointizen.open.FlagControl;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.entity.WXOrderInfo;
import com.cointizen.paysdk.http.process.WXPayResultProgress;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * 作者：Administrator
 * 时间: 2017-11-10 14:49
 */

public class MCHWapPayActivity extends MCHBaseActivity {

    private static final String TAG = "MCWapPayActivity";
    private WebView webview;
    private ImageView btnClose;
    private WXOrderInfo wapPayOrderInfo;
    private boolean isFirst = true;
    private MCTipDialog payResultTip;//提示对话框

    private ProgressBar mProgressBar;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.GET_WX_PAY_RESULT_SUCCESS:
                    if (payResultTip != null) {
                        payResultTip.dismiss();
                    }
                    ApiCallback.mWFTWapPayCallback.onResult("0");//成功
                    finish();
                    break;
                case Constant.GET_WX_PAY_RESULT_FAIL:
                    if (payResultTip != null) {
                        payResultTip.dismiss();
                    }
                    ApiCallback.mWFTWapPayCallback.onResult("1");
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_activity_webview"));
        FlagControl.BUTTON_CLICKABLE = false;
        wapPayOrderInfo = (WXOrderInfo) getIntent().getSerializableExtra("WapPayOrderInfo");
        initView();
    }

    private void initView() {
        webview = (WebView) findViewById(getId("mch_webview"));
        mProgressBar = (ProgressBar) findViewById(MCHInflaterUtils.getControl(this, "pro_web_progress"));

        btnClose = findViewById(getId("btn_close"));
        btnClose.setVisibility(View.GONE);
        btnClose.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                if (wapPayOrderInfo.getTag().equals("zfb")){
                    ApiCallback.mStripePayCallback.onResult("-1");
                }
                finish();
            }
        });

        initWebView();
//        WebViewUtil.read(this,wapPayOrderInfo.getUrl());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FlagControl.BUTTON_CLICKABLE = true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initWebView() {
        if (wapPayOrderInfo == null) {
            MCLog.e(TAG, "wapPayOrderInfo is null!");
            return;
        }
//        wapPayOrderInfo.setUrl("https://www.google.com/");
        if (!wapPayOrderInfo.getTag().equals("jft")) {
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 设置允许JS弹窗
        }
        webview.getSettings().setUseWideViewPort(true); //自适应屏幕
        webview.getSettings().setAppCacheEnabled(true);//设置缓存
        // x5WebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存模式(只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据)
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //缓存模式(不使用缓存，只从网络获取数据)
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);
        webview.setScrollbarFadingEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);// 支持js交互

        webview.addJavascriptInterface(new JsInterface(), "mengchuang"); //js方法
//        webview.setBackgroundColor(0);//设置webview背景透明
//        WebSettings webSettings = webview.getSettings();
//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAllowUniversalAccessFromFileURLs(true);
//        webSettings.setAppCachePath(this.getCacheDir().toString());
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setGeolocationDatabasePath(this.getFilesDir().toString());
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(false);

//        webview.loadUrl(url);
        webview.setWebChromeClient(new WebChromeClient());
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                MCLog.w(TAG,ActivityConstants.Log_CFtTAFhgIk + url);
                if (url.startsWith("weixin:")) {  // 在非微信内部WebView的H5页面中调出微信支付
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        MCLog.e(TAG, ActivityConstants.S_IVuOkFfzqV+e.toString());
                    }
                    return true;
                } else if (url.contains("alipays://platformapi")) {  //吊起支付宝客户端支付
                    try {
                        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent3);
                    } catch (Exception e) {
                        Log.e(TAG, ActivityConstants.S_BdZLpNvCIY+e.toString());
                    }
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 重写此方法可以让webview处理https请求
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (wapPayOrderInfo.getTag().equals("zfb")){
                    btnClose.setVisibility(View.VISIBLE);
                }
                mProgressBar.setVisibility(View.GONE);
            }
        };
        webview.setWebViewClient(webViewClient);
        if (wapPayOrderInfo.getTag().equals("wx")) {
            Map<String, String> extraHeaders = new HashMap<String, String>();
            extraHeaders.put("Referer", wapPayOrderInfo.getCal_url());
            webview.loadUrl(wapPayOrderInfo.getUrl(), extraHeaders);
        } else {
            webview.loadUrl(wapPayOrderInfo.getUrl());
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        isFirst = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wapPayOrderInfo != null) {
            if (!isFirst && wapPayOrderInfo.getTag().equals("wx")) {
                payResultTip = new MCTipDialog.Builder().setMessage(ActivityConstants.S_cqUezKNXTI).show(this, this.getFragmentManager());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //微信官方 获取支付结果
                        WXPayResultProgress progress = new WXPayResultProgress();
                        progress.setOrderNo(wapPayOrderInfo.getOrderNo());
                        progress.post(handler);
                    }
                },1000);
            }

        }
    }

    class JsInterface {
        @JavascriptInterface
        public void getPayResult(String result) {
            MCLog.w(TAG, "result: " + result);
            if (result.equals("succeed")) {
                if (wapPayOrderInfo.getTag().equals("wft")||wapPayOrderInfo.getTag().equals("wx")){
                    ApiCallback.mWFTWapPayCallback.onResult("0");
                }else if (wapPayOrderInfo.getTag().equals("zfb")){
                    ApiCallback.mStripePayCallback.onResult("0");
                }
            } else {
                if (wapPayOrderInfo.getTag().equals("wft")||wapPayOrderInfo.getTag().equals("wx")){
                    ApiCallback.mWFTWapPayCallback.onResult("1");
                }
                else if (wapPayOrderInfo.getTag().equals("zfb")){
                    ApiCallback.mStripePayCallback.onResult("1");
                }
            }
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        } else {
            if (wapPayOrderInfo!=null&&wapPayOrderInfo.getTag().equals("wft")||wapPayOrderInfo.getTag().equals("wx")){
                ApiCallback.mWFTWapPayCallback.onResult("1");
            } else if (wapPayOrderInfo!=null&&wapPayOrderInfo.getTag().equals("zfb")){
                ApiCallback.mStripePayCallback.onResult("1");
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
