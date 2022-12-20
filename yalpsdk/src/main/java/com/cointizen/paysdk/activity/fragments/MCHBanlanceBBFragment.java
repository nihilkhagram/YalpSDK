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
import android.widget.ListView;

import com.cointizen.smartrefresh.layout.SmartRefreshLayout;
import com.cointizen.smartrefresh.layout.api.RefreshLayout;
import com.cointizen.smartrefresh.layout.footer.ClassicsFooter;
import com.cointizen.smartrefresh.layout.header.ClassicsHeader;
import com.cointizen.smartrefresh.layout.listener.OnLoadMoreListener;
import com.cointizen.smartrefresh.layout.listener.OnRefreshListener;
import com.cointizen.paysdk.adapter.MCHBBAdapter;
import com.cointizen.paysdk.bean.BangBiListBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.BangBiListProcess;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import java.util.ArrayList;

/**
 * 描述：绑币fragment
 * 时间: 2018-09-26 15:26
 */

public class MCHBanlanceBBFragment extends Fragment {

    private View inflate;
    private ListView xListView;
    private View tvNoData;
    private SmartRefreshLayout haveData;
    private  int pager = 1;
    ArrayList<BangBiListBean.DataBean> dataBeans = new ArrayList<>();
    private MCHBBAdapter MCHBBAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(MCHInflaterUtils.getLayout(getActivity(), "mch_fm_banlance_bb"), null);
        xListView = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "list_msg"));
        haveData = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "layout_havedata"));
        tvNoData = inflate.findViewById(MCHInflaterUtils.getIdByName(getActivity(), "id", "tv_mch_nodata"));
        MCHBBAdapter = new MCHBBAdapter(dataBeans,getActivity());
        xListView.setAdapter(MCHBBAdapter);
        haveData.setRefreshHeader(new ClassicsHeader(getActivity()));
        haveData.setRefreshFooter(new ClassicsFooter(getActivity()));
        haveData.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                dataBeans.clear();
                getData();
            }
        });
        haveData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pager = pager+1;
                getData();
            }
        });
        getData();
        return inflate;
    }

    public void getData(){
        BangBiListProcess bangBiListProcess = new BangBiListProcess();
        bangBiListProcess.setLimit(pager+"");
        bangBiListProcess.post(handler,getActivity());
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.BANGBI_LIST_SUCCESS:
                    BangBiListBean obj = (BangBiListBean) msg.obj;
                    if(obj.getData()!=null && obj.getData().size()>0){
                        haveData.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                        dataBeans.addAll(obj.getData());
                        MCHBBAdapter.notifyDataSetChanged();
                    }else {
                        haveData.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                    break;
                case Constant.BANGBI_LIST_FAIL:
                    haveData.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    break;
            }
            haveData.finishLoadMore();
            haveData.finishRefresh();
        }
    };
}
