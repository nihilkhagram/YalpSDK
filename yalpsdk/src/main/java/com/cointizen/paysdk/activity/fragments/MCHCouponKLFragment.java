package com.cointizen.paysdk.activity.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHCouponAdapter;
import com.cointizen.paysdk.bean.CouponBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.CouponAllProcess;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 可领取代金券Fragment
 */
public class MCHCouponKLFragment extends Fragment{

    private View inflate;
    private GridView gridView;
    private TextView tvNoData;
    private MCHCouponAdapter adapter;
    private List<CouponBean> listData = new ArrayList<>();
    private int type = 1;  // 1可领取  2已领取 3已失效

    private boolean isBind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(MCHInflaterUtils.getLayout(getActivity(), "mch_fm_coupon"), null);
        gridView = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "mch_gridview"));
        tvNoData = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "mch_tv_no_data"));
        init();
        return inflate;
    }


    private void init(){
        adapter = new MCHCouponAdapter(getActivity(),handler);
        adapter.setType(type);
        gridView.setAdapter(adapter);

        if (isScreenOriatationPortrait(getActivity())) {  //判断是否是竖屏
            gridView.setNumColumns(2);
        } else {
            gridView.setNumColumns(3);
        }

        CouponAllProcess process = new CouponAllProcess();
        process.setType(type);
        process.setIsbind(isBind ? "1" : "0");
        process.post(handler);
    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.ALL_COUPON_SUCCESS:
                    listData = (List<CouponBean>) msg.obj;
                    if (listData != null && listData.size() > 0) {
                        adapter.setData(listData);
                        gridView.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    } else {
                        gridView.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                    break;
                case Constant.ALL_COUPON_FAIL:
                    gridView.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(msg.obj.toString())) {
                        ToastUtil.show(getActivity(), msg.obj.toString());
                    }
                    break;
            }
        }
    };

    public void setBind(boolean bind) {
        isBind = bind;
    }

    //判断是否是竖屏
    private boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
