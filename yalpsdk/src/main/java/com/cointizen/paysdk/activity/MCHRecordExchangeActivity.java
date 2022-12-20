package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cointizen.smartrefresh.layout.SmartRefreshLayout;
import com.cointizen.smartrefresh.layout.api.RefreshLayout;
import com.cointizen.smartrefresh.layout.footer.ClassicsFooter;
import com.cointizen.smartrefresh.layout.header.ClassicsHeader;
import com.cointizen.smartrefresh.layout.listener.OnLoadMoreListener;
import com.cointizen.smartrefresh.layout.listener.OnRefreshListener;
import com.cointizen.paysdk.adapter.MCHDHJLAdapter;
import com.cointizen.paysdk.bean.JFDHPtbBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.DHJLProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppUtils;

import java.util.ArrayList;

/**
 * 描述：兑换记录
 * 时间: 2018-09-29 8:55
 */

public class MCHRecordExchangeActivity extends MCHBaseActivity {

    private ListView xListView;
    private View tvMchNodata;
    private SmartRefreshLayout layoutHavedata;
    private TextView txtMchTotal;
    private int pager = 1;
    ArrayList<JFDHPtbBean.DataBean> dataBeans = new ArrayList<>();
    private MCHDHJLAdapter mcbbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_dhjl"));
        View btnMchBack = findViewById(getId("btn_mch_back"));
        xListView = findViewById(getId("xlistview_mch_jl"));
        layoutHavedata = findViewById(getId("layout_havedata"));
        layoutHavedata.setVisibility(View.VISIBLE);
        txtMchTotal = findViewById(getId("txt_mch_total"));
        tvMchNodata = findViewById(getId("tv_mch_nodata"));
        txtMchTotal.setVisibility(View.GONE);

        mcbbAdapter = new MCHDHJLAdapter(dataBeans,this);
        xListView.setAdapter(mcbbAdapter);

        layoutHavedata.setRefreshHeader(new ClassicsHeader(this));
        layoutHavedata.setRefreshFooter(new ClassicsFooter(this));
        layoutHavedata.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                dataBeans.clear();
                getData();
            }
        });
        layoutHavedata.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pager = pager+1;
                getData();
            }
        });

        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
        getData();
    }

    private void getData() {
        DHJLProcess dhjlProcess = new DHJLProcess();
        dhjlProcess.setNum(pager+"");
        dhjlProcess.post(handler);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.JFDHJL_SUCCESS:
                    JFDHPtbBean obj = (JFDHPtbBean) msg.obj;
                    if(obj.getData()!=null){
                        dataBeans.addAll(obj.getData());
                        mcbbAdapter.notifyDataSetChanged();
                        AppUtils.getTotalHeightofListView(xListView);
//                        txtMchTotal.setText(ActivityConstants.S_tptHNeucOF+dataBeans.size()+ActivityConstants.S_KFXQrZGBQQ);
                    }
                    break;
                case Constant.JFDHJL_FAIL:
                    if(dataBeans.size()==0){
                        layoutHavedata.setVisibility(View.GONE);
                        tvMchNodata.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            layoutHavedata.finishRefresh();
            layoutHavedata.finishLoadMore();
        }
    };

}
