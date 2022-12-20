package com.cointizen.paysdk.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.ValueCallback;
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
public class OttUPayPaymentActivity extends AppCompatActivity {
    private String TAG = "MGatePaymentActivity";

    private WebView webView;
    private AppCompatActivity activity;
    final String js = "javascript:document.all.pay_form.submit();";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mch_mgate_payment);

        Log.e(TAG, "onCreate: ");
//        final ProgressDialog pd = ProgressDialog.show(this, "", "Loading...",true);

        String data = getIntent().getStringExtra("data");
        webView = findViewById(R.id.layout_mgate);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view,  WebResourceRequest request) {
                String url = request.getUrl().getPath();
                Log.e(TAG, "shouldOverrideUrlLoading: "+url);
                if (url.contains("donePayment")) {
                    //do something
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("done","donePayment");
                    editor.apply();
                    Toast.makeText(OttUPayPaymentActivity.this, "Payment successfully", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",200);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                } else if (url.contains("failedPayment")){
                    Toast.makeText(OttUPayPaymentActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",500);
                    setResult(RESULT_CANCELED,returnIntent);
                    finish();
                }
                else if(url.contains(".action")) {
                    view.loadUrl(request.getUrl().toString());
                }
                return true; // Handle By application itself
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(Build.VERSION.SDK_INT >= 19){
                    view.evaluateJavascript(js, s -> {
                    });
                }
//                if(pd!=null && pd.isShowing())
//                {
//                    pd.dismiss();
//                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadData(data, "text/html", "utf-8");
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
