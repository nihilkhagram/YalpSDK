package com.cointizen.paysdk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
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

import com.cointizen.paysdk.dialog.MCLoadDialog;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.AppUtils;


/**
 * 描述：webview
 * 时间: 2020-05-26 11:52
 */

public class MCHWebviewActivity extends MCHBaseActivity {
    private WebView webview;
    private ProgressBar mProgressBar;

    private MCLoadDialog loadDialog;
    private String TAG = "MCWebviewActivity";
    private String url = "";
    private ImageView imgClose;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_activity_webview"));
        webview = findViewById(getId("mch_webview"));
        mProgressBar = findViewById(MCHInflaterUtils.getControl(this, "pro_web_progress"));
        url = getIntent().getStringExtra("url");
        boolean isClose = getIntent().getBooleanExtra("close", false);

        ImageView imgClose = findViewById(getId("btn_close"));
        imgClose.setVisibility(isClose ? View.VISIBLE : View.GONE);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebViewSetting();
    }

    /**
     * 设置webview
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void WebViewSetting() {
        AppUtils.ShowLoadDialog(MCHWebviewActivity.this);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置允许JS弹窗
        settings.setUseWideViewPort(true); //自适应屏幕
        settings.setAppCacheEnabled(true);//设置缓存
//        settings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //不使用缓存，只从网络获取数据
        settings.setJavaScriptEnabled(true);// 支持js交互
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);           //资源加载超时操作
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAppCachePath(this.getCacheDir().toString());
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationDatabasePath(this.getFilesDir().toString());
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("UTF-8");
        String userAgent = settings.getUserAgentString(); // 获取到UserAgentString
//        settings.setUserAgentString(userAgent + "; app/yiniuhz");// 设置UserAgent标签
        webview.setWebViewClient(new MCHWebviewActivity.ViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);
        webview.setScrollbarFadingEnabled(true);
        webview.addJavascriptInterface(new MCHWebviewActivity.JsInterface(), "yalpsdk");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        MCLog.w(TAG,url);
        webview.loadUrl(url);
    }

    class ViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            AppUtils.DissLoadDialog();
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
            MCLog.e("WebView", "页面加载错误onReceivedError：" + s + "  ,CODE：" + i+",链接："+s1);
            AppUtils.DissLoadDialog();
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();      // 忽略ssl证书错误   继续加载页面
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            MCLog.e(ActivityConstants.Log_YZfLHaViIC, "shouldOverrideUrlLoading-url=" + url);
            if (url.startsWith("weixin:")) {  // 在非微信内部WebView的H5页面中调出微信支付
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e(ActivityConstants.S_IVuOkFfzqV, e.toString());
                }
                return true;
            } else if (url.contains("alipays://platformapi")) {  //吊起支付宝客户端支付
                try {
                    Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent3);
                } catch (Exception e) {
                    Log.e(ActivityConstants.S_BdZLpNvCIY, e.toString());
                }
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
            return true;
        }
    }


    /**
     * JS交互
     */
    class JsInterface {
        //关闭界面
        @JavascriptInterface
        public void finishPage(){
            finish();
        }

        //绑定手机号
        @JavascriptInterface
        public void toBindPhone(){
            startActivity(new Intent(MCHWebviewActivity.this, MCHBindPhoneActivity.class));
        }

        //绑定邮箱
        @JavascriptInterface
        public void toBindMail(){
            startActivity(new Intent(MCHWebviewActivity.this, MCHBindMailActivity.class));
        }

        //刷新页面
        @JavascriptInterface
        public void reloadPage(){
            webview.reload();
        }

    }



    /**
     * 返回按键监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (webview.canGoBack()){
                webview.goBack();
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (webview != null) {
            webview.onResume();
            //恢复pauseTimers状态
            webview.resumeTimers();
            webview.reload();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webview != null) {
            webview.onPause();
            //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗
            webview.pauseTimers();
        }
    }


}

