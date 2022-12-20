package com.cointizen.paysdk.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cointizen.paysdk.R;
/**
 * this has to be added to the page:
 * <script type="text/javascript">
 *     function closeWindow() {
 *         window.close();
 *     }
 *     document.getElementById('donePaymentBtn').onclick = closeWindow;
 * </script>
 */
public class MGatePaymentActivity extends AppCompatActivity {
    private String TAG = "MGatePaymentActivity";

    private WebView webView;
    private AppCompatActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mch_mgate_payment);

        Log.e(TAG, "onCreate: ");
        final ProgressDialog pd = ProgressDialog.show(this, "", "Loading...",true);

        String url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.layout_mgate);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view,  WebResourceRequest request) {
                String url = request.getUrl().getPath();
                Log.e(TAG, "shouldOverrideUrlLoading: "+url + ", " + request.getUrl().toString());
                if (url.contains("donePayment")) {
                    //do something
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("done","donePayment");
                    editor.apply();
                    Toast.makeText(MGatePaymentActivity.this, "Payment successfully", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",200);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                } else if (url.contains("failedPayment")){
                    Toast.makeText(MGatePaymentActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",500);
                    setResult(RESULT_CANCELED,returnIntent);
                    finish();
                }
                else if(url.contains("startApp")){
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(request.getUrl().toString().replace("startApp", "startapp"))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        new AlertDialog.Builder(MGatePaymentActivity.this)
                                .setMessage("未检测到支付宝客户端，请安装后重试。")
                                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                        startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                                    }
                                }).setNegativeButton("Cancel", null).show();
                    }
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString().replace("alipays", "alipay")));
//                    startActivity(intent);
                }
                else {
                    view.loadUrl(request.getUrl().toString());
                }
                return true; // Handle By application itself
            }
            private void startAlipayActivity(String url) {
                if(true) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return;
                }
                Intent intent;
                try {
                    intent = Intent.parseUri(url,
                            Intent.URI_INTENT_SCHEME);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setComponent(null);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if(pd!=null && pd.isShowing())
                {
                    pd.dismiss();
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }
}
