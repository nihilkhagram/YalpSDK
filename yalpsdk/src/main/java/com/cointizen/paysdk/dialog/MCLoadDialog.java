package com.cointizen.paysdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.cointizen.paysdk.utils.MCHInflaterUtils;



/**
 * 描述：加载中弹窗
 * 时间: 2019/4/17 8:19 PM
 */
public class MCLoadDialog extends Dialog {

    private ImageView jiazai;
    private AnimationDrawable background;
    public static MCLoadDialog mInstace = null;

    public MCLoadDialog(Context context) {
        super(context);
    }

    public MCLoadDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MCLoadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MCHInflaterUtils.getLayout(getContext(),"mch_dialog_load"));
        mInstace = this;
        jiazai = (ImageView)findViewById(MCHInflaterUtils.getControl(getContext(),"jiazai"));
        jiazai.setBackgroundResource(MCHInflaterUtils.getDrawable(getContext(),"mch_loading"));
        background = (AnimationDrawable) jiazai.getBackground();

        if(background.isRunning())//是否正在运行？

        {
            background.stop();//停止
            return;

        }
        background.start();//启动

    }

}
