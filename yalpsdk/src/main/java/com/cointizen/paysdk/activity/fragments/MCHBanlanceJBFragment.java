package com.cointizen.paysdk.activity.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：金币fragment
 * 时间: 2018-09-26 15:26
 */

public class MCHBanlanceJBFragment extends Fragment{

    private View inflate;
    private TextView tvJB;
    private View BtnTX;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(MCHInflaterUtils.getLayout(getActivity(), "mch_fm_banlance_jb"), null);
        tvJB = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "tv_mch_jb"));
        BtnTX = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "btn_mch_tixian"));
        BtnTX.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                AppUtils.openBrowser(getActivity(),Constant.TiXian);
            }
        });

        return inflate;
    }

    public void setData(String bindPtbMoney) {
        tvJB.setText(bindPtbMoney);
    }
}
