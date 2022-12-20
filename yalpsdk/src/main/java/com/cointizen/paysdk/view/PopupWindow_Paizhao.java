package com.cointizen.paysdk.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 选择头像PopupWindow
 */
public class PopupWindow_Paizhao extends PopupWindow {

    private final Activity act;
    private View mMenuView;

    public PopupWindow_Paizhao(final Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        this.act = context;
        WindowManager.LayoutParams params=context.getWindow().getAttributes();
        params.alpha=0.5f;
        context.getWindow().setAttributes(params);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(MCHInflaterUtils.getLayout(context, "mch_view_icon_popu"), null);
        View paizhao = mMenuView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "mch_paizhao"));
        View xiangce = mMenuView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "mch_xiangce"));
        View cancel = mMenuView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "mch_cancel"));
        //取消按钮
        cancel.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                dismiss();
            }
        });
        //设置按钮监听
        xiangce.setOnClickListener(itemsOnClick);
        paizhao.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(MCHInflaterUtils.getIdByName(context, "style", "mch_MCHAnimBottom"));
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "mch_pop_layout")).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams params = act.getWindow().getAttributes();
        params.alpha=1f;
        act.getWindow().setAttributes(params);
    }
}
