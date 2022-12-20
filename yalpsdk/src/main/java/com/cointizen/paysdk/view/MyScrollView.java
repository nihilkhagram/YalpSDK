package com.cointizen.paysdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 描述：自定义MyScrollView  监听滑动到底部
 * 时间: 2018-11-05 9:00
 */

public class MyScrollView extends ScrollView{
    private boolean b = true;
    private ScrollBottomListener bottomListener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        int i = computeVerticalScrollRange();
        if(t+getHeight() >=i &&b){
            b = false;
            bottomListener.ScrollBottom(t);
        }else{
            b = true;
        }
    }

    public void setScrollBottomListener(ScrollBottomListener listener){
        bottomListener = listener;
    }

    public interface ScrollBottomListener{
        void ScrollBottom(int t);
    }
}
