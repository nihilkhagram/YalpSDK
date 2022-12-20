package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.paysdk.activity.MCHUserCenterActivity;
import com.cointizen.paysdk.bean.SignBean;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：签到Dialog
 * 时间: 2018-10-16 11:06
 */

@SuppressLint("ValidFragment")
public class MCSignaaaDialog extends DialogFragment {

    private final SignBean signBean;
    private Context context;
    private View btnClose;
    private TextView tvOneJf;
    private TextView tvOneDay;
    private TextView tvTweJf;
    private TextView tvTweDay;
    private TextView tvThreeJf;
    private TextView tvThreeDay;
    private TextView tvFourJf;
    private TextView tvFourDay;
    private TextView tvFiveJf;
    private TextView tvFiveDay;
    private TextView tvSixJf;
    private TextView tvSixDay;
    private TextView tvSevenJf;
    private TextView tvSevenDay;
    private RelativeLayout rlOne,rlTwe,rlThree,rlFour,rlFive,rlSix,rlSeven;

    @SuppressLint("ValidFragment")
    public MCSignaaaDialog(MCHUserCenterActivity mcUserCenterActivity, SignBean obj) {
        this.context = mcUserCenterActivity;
        signBean = obj;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(context, "style", "mch_MCCustomDialog"));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(context, "layout", "mch_dialog_sign"), container, false);
        WindowManager.LayoutParams params = ((Activity)context).getWindow().getAttributes();
        params.alpha = 0.5f;((Activity)context).getWindow().setAttributes(params);
        btnClose = containerView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "btn_close"));
        rlOne = containerView.findViewById(getId("mch_layout_one"));
        rlTwe = containerView.findViewById(getId("mch_layout_twe"));
        rlThree = containerView.findViewById(getId("mch_layout_three"));
        rlFour = containerView.findViewById(getId("mch_layout_four"));
        rlFive = containerView.findViewById(getId("mch_layout_five"));
        rlSix = containerView.findViewById(getId("mch_layout_six"));
        tvOneJf = containerView.findViewById(getId("tv_one_jf"));
        tvOneDay = containerView.findViewById(getId("tv_one_day"));
        tvTweJf = containerView.findViewById(getId("tv_twe_jf"));
        tvTweDay = containerView.findViewById(getId("tv_twe_day"));
        tvThreeJf = containerView.findViewById(getId("tv_three_jf"));
        tvThreeDay = containerView.findViewById(getId("tv_three_day"));
        tvFourJf = containerView.findViewById(getId("tv_four_jf"));
        tvFourDay = containerView.findViewById(getId("tv_four_day"));
        tvFiveJf = containerView.findViewById(getId("tv_five_jf"));
        tvFiveDay = containerView.findViewById(getId("tv_five_day"));
        tvSixJf = containerView.findViewById(getId("tv_six_jf"));
        tvSixDay = containerView.findViewById(getId("tv_six_day"));
        tvSevenJf = containerView.findViewById(getId("tv_seven_jf"));
        tvSevenDay = containerView.findViewById(getId("tv_seven_day"));
        btnClose.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                dismiss();
            }
        });

        if(signBean!=null){
            tvOneJf.setText(signBean.getPoint_arr().getDay1()+"");
            tvTweJf.setText(signBean.getPoint_arr().getDay2()+"");
            tvThreeJf.setText(signBean.getPoint_arr().getDay3()+"");
            tvFourJf.setText(signBean.getPoint_arr().getDay4()+"");
            tvFiveJf.setText(signBean.getPoint_arr().getDay5()+"");
            tvSixJf.setText(signBean.getPoint_arr().getDay6()+"");
            tvSevenJf.setText(signBean.getPoint_arr().getDay7()+"");
            switch (signBean.getSigned_day()){
                case 1:
                    rlOne.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvOneDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvOneJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvOneJf.setTextColor(Color.parseColor("#FFEBC0"));
                    break;
                case 2:
                    rlOne.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvOneDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvOneJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvOneJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlTwe.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvTweDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvTweJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvTweJf.setTextColor(Color.parseColor("#FFEBC0"));
                    break;
                case 3:
                    rlOne.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvOneDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvOneJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvOneJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlTwe.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvTweDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvTweJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvTweJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlThree.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvThreeDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvThreeJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvThreeJf.setTextColor(Color.parseColor("#FFEBC0"));
                    break;
                case 4:
                    rlOne.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvOneDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvOneJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvOneJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlTwe.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvTweDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvTweJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvTweJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlThree.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvThreeDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvThreeJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvThreeJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlFour.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvFourDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvFourJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvFourJf.setTextColor(Color.parseColor("#FFEBC0"));
                    break;
                case 5:
                    rlOne.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvOneDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvOneJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvOneJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlTwe.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvTweDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvTweJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvTweJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlThree.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvThreeDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvThreeJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvThreeJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlFour.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvFourDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvFourJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvFourJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlFive.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvFiveDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvFiveJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvFiveJf.setTextColor(Color.parseColor("#FFEBC0"));
                    break;
                case 6:
                    rlOne.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvOneDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvOneJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvOneJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlTwe.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvTweDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvTweJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvTweJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlThree.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvThreeDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvThreeJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvThreeJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlFour.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvFourDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvFourJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvFourJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlFive.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvFiveDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvFiveJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvFiveJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlSix.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvSixDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvSixJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvSixJf.setTextColor(Color.parseColor("#FFEBC0"));
                    break;
                case 7:
                    rlOne.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvOneDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvOneJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvOneJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlTwe.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvTweDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvTweJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvTweJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlThree.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvThreeDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvThreeJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvThreeJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlFour.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvFourDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvFourJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvFourJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlFive.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvFiveDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvFiveJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvFiveJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlSix.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvSixDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvSixJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvSixJf.setTextColor(Color.parseColor("#FFEBC0"));

                    rlSeven.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_sign_bg_s"));
                    tvSevenDay.setTextColor(Color.parseColor("#FFEBC0"));
                    tvSevenJf.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_icon_signin_s"));
                    tvSevenJf.setTextColor(Color.parseColor("#FFEBC0"));
                    break;
            }
        }
        return containerView;
    }

    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(getActivity(), idName);
    }

    protected int getColor(String idName) {
        return MCHInflaterUtils.getColor(getActivity(), idName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager.LayoutParams params = ((Activity)context).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity)context).getWindow().setAttributes(params);
    }


}
