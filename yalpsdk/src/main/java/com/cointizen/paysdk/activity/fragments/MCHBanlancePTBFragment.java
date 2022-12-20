package com.cointizen.paysdk.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cointizen.paysdk.activity.MCHTopupYLPDActivity;
import com.cointizen.paysdk.bean.SwitchManager;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：平台币fragment
 * 时间: 2018-09-26 15:26
 */

public class MCHBanlancePTBFragment extends Fragment{

    private View inflate;
    private TextView tvPTB;
    private View pay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(MCHInflaterUtils.getLayout(getActivity(), "mch_fm_banlance_ptb"), null);
        tvPTB = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "tv_mch_ptb"));
        pay = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "btn_mch_ylpd"));
        pay.setVisibility(SwitchManager.getInstance().ptb() ? View.VISIBLE : View.GONE);
        pay.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                startActivityForResult(new Intent(getActivity(), MCHTopupYLPDActivity.class),5002);
            }
        });
        return inflate;
    }

    public void setData(String bindPtbMoney) {
        tvPTB.setText(String.format("%.2f", Float.parseFloat(bindPtbMoney)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
