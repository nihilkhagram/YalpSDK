package com.cointizen.paysdk.activity.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cointizen.paysdk.bean.NoticeModel;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：公告fragment
 * 时间: 2018-10-17 14:34
 */

public class MCHNoticeFragment extends Fragment {
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvQufu;
    private TextView tvCon;
    private NoticeModel.ListBean listBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fmMchNotice = inflater.inflate(MCHInflaterUtils.getLayout(getActivity(), "mch_fm_notice"), null);
        tvTitle = fmMchNotice.findViewById(getId("tv_title"));
        tvTime = fmMchNotice.findViewById(getId("tv_time"));
        tvQufu = fmMchNotice.findViewById(getId("tv_qufu"));
        tvCon = fmMchNotice.findViewById(getId("tv_con"));
        tvTime.setVisibility(View.GONE);
        tvQufu.setVisibility(View.GONE);
        if(listBean!=null){
            tvTitle.setText(listBean.getTitle());
            tvCon.setText(listBean.getContent());
        }
        return fmMchNotice;
    }

    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(getActivity(), idName);
    }

    public void setData(NoticeModel.ListBean listBean) {
        this.listBean = listBean;
        if(tvTitle!=null){
            tvTitle.setText(listBean.getTitle());
            tvCon.setText(listBean.getContent());
        }
    }
}
