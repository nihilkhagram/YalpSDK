package com.cointizen.paysdk.floatview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cointizen.paysdk.utils.MCHInflaterUtils;

public class MCHFloatLoginView {
    private ImageView imgLoginIng;
    private Toast mToast;
    private RelativeLayout layoutLogining;
    private TextView tvLoginingAccount;
    private RelativeLayout layoutLoginok;
    private TextView tvLoginokAccount;
    private ImageView imgLoginOk;
    private ImageView imgMchJiaZai;

    private MCHFloatLoginView(Context context, int duration, String account, boolean isSuccess) {
        View view = LayoutInflater.from(context).inflate(MCHInflaterUtils.getLayout(context, "mch_login_floating_menu"), null);
        layoutLogining = (RelativeLayout) view.findViewById(MCHInflaterUtils.getControl(context, "layout_logining"));
        layoutLoginok = (RelativeLayout) view.findViewById(MCHInflaterUtils.getControl(context, "layout_loginok"));

        imgLoginIng = (ImageView) view.findViewById(MCHInflaterUtils.getControl(context, "mch_img_login_ing"));
        imgLoginOk = (ImageView) view.findViewById(MCHInflaterUtils.getControl(context, "mch_img_login_ok"));
        imgMchJiaZai = (ImageView) view.findViewById(MCHInflaterUtils.getControl(context, "img_mch_jiazai"));
        tvLoginingAccount = view.findViewById(MCHInflaterUtils.getControl(context, "tv_logining_account"));
        tvLoginokAccount =  view.findViewById(MCHInflaterUtils.getControl(context, "tv_loginok_account"));
        tvLoginingAccount.setText("");

        Animation operatingAnim = AnimationUtils.loadAnimation(context, MCHInflaterUtils.getIdByName(context,"anim","mch_rotate_anim"));
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        if (operatingAnim != null) {
            imgMchJiaZai.startAnimation(operatingAnim);
        }  else {
            imgMchJiaZai.setAnimation(operatingAnim);
            imgMchJiaZai.startAnimation(operatingAnim);
        }




        if (isSuccess){
            //如果是登录成功，就展示登录成功的UI
            layoutLogining.setVisibility(View.GONE);
            imgLoginIng.setVisibility(View.GONE);
            layoutLoginok.setVisibility(View.VISIBLE);
            imgLoginOk.setVisibility(View.VISIBLE);
        }else {
            layoutLogining.setVisibility(View.VISIBLE);
            imgLoginIng.setVisibility(View.VISIBLE);
            layoutLoginok.setVisibility(View.GONE);
            imgLoginOk.setVisibility(View.GONE);
        }

        if(account!=null&&tvLoginingAccount!=null){
            tvLoginingAccount.setText(account);
            tvLoginokAccount.setText(account);
        }

        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.setView(view);
    }

    public static MCHFloatLoginView getInstance(Context context, int showTime, String account, boolean isLoginSuccess) {
        return new MCHFloatLoginView(context,showTime,account,isLoginSuccess);
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
