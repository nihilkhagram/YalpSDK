package com.cointizen.paysdk.view.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：
 * 作者：Administrator
 * 时间: 2018-03-07 15:34
 */

public class MCHToast {

    private Toast mToast;
    private TextView textView;

    private MCHToast(Context context, CharSequence text, int duration) {
        View view = LayoutInflater.from(context).inflate(MCHInflaterUtils.getLayout(context, "mch_util_toast"), null);
        textView = (TextView) view.findViewById(MCHInflaterUtils.getControl(context, "tv_mch_toast"));
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(view);
    }

    public static MCHToast makeText(Context context, CharSequence text, int duration) {
        return new MCHToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.setGravity(Gravity.CENTER,0,0);  //显示在屏幕中间
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }

    public void setText(String msg) {
        textView.setText(msg);
    }
}
