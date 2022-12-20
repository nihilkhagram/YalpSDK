package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;

/**
 * 描述：隐藏悬浮球弹窗
 * 时间: 2018-09-18 14:32
 */

@SuppressLint("ValidFragment")
public class HideBallDialog extends DialogFragment{

    private Activity context;
    private LayoutInflater inflater;
    private Dialog dialog;
    private View view;
    private boolean b = true;
    private ImageView btnNoShow1;

    public static boolean IsShow = true;

    @SuppressLint("ValidFragment")
    public HideBallDialog(Activity context){
        this.context = context;
    }

    public HideBallDialog(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        WindowManager.LayoutParams params=context.getWindow().getAttributes();
        params.alpha=0.5f;
        context.getWindow().setAttributes(params);
        inflater = LayoutInflater.from(context);
        dialog = new Dialog(context, MCHInflaterUtils.getIdByName(context, "style", "mch_MCCustomDialog"));
        view = inflater.inflate(MCHInflaterUtils.getIdByName(context, "layout", "mch_dialog_hideball"), null);
        btnNoShow1 = (ImageView)view.findViewById(MCHInflaterUtils.getIdByName(context, "id", "btn_no_show"));
        View btnCancel = view.findViewById(MCHInflaterUtils.getIdByName(context, "id", "btn_mch_cancel"));
        View btnOk = view.findViewById(MCHInflaterUtils.getIdByName(context, "id", "btn_mch_ok"));
        BtnClick btnClick = new BtnClick();
        btnCancel.setOnClickListener(btnClick);
        btnNoShow1.setOnClickListener(btnClick);
        btnOk.setOnClickListener(btnClick);
        //设置取消标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置点击外部不可以消失
        dialog.setCanceledOnTouchOutside(true);
        //设置即使点击返回键也不会退出
//        setCancelable(true);
        dialog.setContentView(view);
        return dialog;
    }

    class BtnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId()==MCHInflaterUtils.getIdByName(context, "id", "btn_no_show")){
                if(b){
                    b = false;
                    SharedPreferencesUtils.getInstance().setIsHiddenOrb(getActivity(),true);
                    btnNoShow1.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_common_btn_yixuan"));
                }else{
                    b = true;
                    SharedPreferencesUtils.getInstance().setIsHiddenOrb(getActivity(),false);
                    btnNoShow1.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_common_btn_weixuan"));
                }
            }else if(view.getId()==MCHInflaterUtils.getIdByName(context, "id", "btn_mch_cancel")){
                dismiss();
            }else if(view.getId()==MCHInflaterUtils.getIdByName(context, "id", "btn_mch_ok")){
                YalpGamesSdk.getYalpGamesSdk().stopFloating(getActivity());  //关闭悬浮球
                IsShow = false;
                ToastUtil.show(getActivity(),DialogConstants.S_NwdGWhxxvl);
                dismiss();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager.LayoutParams params=context.getWindow().getAttributes();
        params.alpha=1f;
        context.getWindow().setAttributes(params);
    }
}
