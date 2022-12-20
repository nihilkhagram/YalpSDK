package com.cointizen.paysdk.listener;

import android.view.View;

import com.cointizen.paysdk.utils.MCLog;

public abstract class OnMultiClickListener implements View.OnClickListener{
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;
    private static int lastViewId = -1;

    public abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if (lastViewId != v.getId()) {
            lastClickTime = curClickTime;
            onMultiClick(v);
            return;
        }

        long spaceTime = curClickTime - lastClickTime;
//        MCLog.e("OnMultiClickListener", "curClickTime: " + curClickTime + ", lastClickTime:" + lastClickTime);
        if(spaceTime >= MIN_CLICK_DELAY_TIME) {
            onMultiClick(v);
//            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
        }else {
            MCLog.e("double click - uninvalid", "curClickTime: " + curClickTime + ", lastClickTime:" + lastClickTime);
        }
    }

}