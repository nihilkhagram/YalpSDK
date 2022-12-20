package com.cointizen.paysdk.floatview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cointizen.paysdk.utils.MCHInflaterUtils;

public class MCHFloatHreadView {

    private Toast mToast;

    private MCHFloatHreadView(Context context, int duration) {
        mToast = new Toast(context);
        //获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //获得屏幕的宽度
        int width = wm.getDefaultDisplay().getWidth();
        //由layout文件创建一个View对象
        View view = inflater.inflate(MCHInflaterUtils.getLayout(context, "mch_floating_head_view"), null);
        LinearLayout llLayout = view.findViewById(MCHInflaterUtils.getControl(context, "ll_layout"));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置TextView的宽度为 屏幕宽度
        llLayout.setLayoutParams(layoutParams);
        mToast.setView(view);
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.setDuration(duration);
    }

    public static MCHFloatHreadView getInstance(Context context, int showTime) {
        return new MCHFloatHreadView(context,showTime);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }

}
