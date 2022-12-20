package com.cointizen.paysdk.activity.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.GetAuthCodeProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.TextUtils;
import com.cointizen.paysdk.activity.ActivityConstants;

/**
 * 描述：积分fragment
 * 时间: 2018-09-26 15:26
 */

public class MCHBanlanceJFFragment extends Fragment{

    private View inflate;
    private TextView tvMchJf;
    private View btnHq;
    private View btnDh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(MCHInflaterUtils.getLayout(getActivity(), "mch_fm_banlance_jf"), null);
        tvMchJf = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "tv_mch_jf"));
        btnHq = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "btn_mch_hq"));
        btnDh = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "btn_mch_dh"));

        btnDh.setVisibility(View.GONE);
        btnHq.setVisibility(View.GONE);
        btnHq.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
//                getActivity().setResult(MCUserCenterActivity.QIANDAO);
//                getActivity().finish();
//                if(BuildConfig.sign){
//                    getActivity().finish();
//                    return;
//                }
//                if(BuildConfig.welfare){
//                    getActivity().startActivity(new Intent(getActivity(),AdWelfareActivity.class));
//                }

            }
        });
        btnDh.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                String jf = tvMchJf.getText().toString();
                if(TextUtils.isEmpty(jf) || jf.equals("0") || jf.equals("0.00")){
                    ToastUtil.show(getActivity(),ActivityConstants.S_NFFRrpBEom);
                }else{
                    new GetAuthCodeProcess(getActivity()).post(handler); //获取校验码,生成商城链接地址
                }
            }
        });
        return inflate;
    }

    public void setData(String bindPtbMoney) {
        tvMchJf.setText(bindPtbMoney);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.GET_AUTH_CODE_SUCCESS:
                    String authCode = (String) msg.obj;
                    if (!TextUtils.isEmpty(authCode)){
                        AppUtils.loadUrl(getActivity(),authCode,"mobile/point/index.html");
                    }
                    break;
                case Constant.GET_AUTH_CODE_FAIL:
                    String res = (String) msg.obj;
                    if (android.text.TextUtils.isEmpty(res)) {
                        res = ActivityConstants.S_SWkgnGTxNN;
                    }
                    ToastUtil.show(getActivity(),res);
                    break;
            }
        }
    };
}
