package com.cointizen.paysdk.dialog.privacy;

import android.app.Activity;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.cointizen.paysdk.utils.WebViewUtil;

public class SdkTextClickableSpan extends ClickableSpan {

    String url = "";
    private Activity mActivity;

    public SdkTextClickableSpan(Activity activity, String url){
        this.mActivity = activity;
        this.url = url;
    }

    @Override
    public void onClick(@NonNull View widget) {
        WebViewUtil.webView(mActivity, url, true);
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.parseColor("#21b1eb"));//设置颜色
        ds.setUnderlineText(false);//去掉下划线
    }

}
