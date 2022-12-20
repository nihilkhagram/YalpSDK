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
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHGamePayListAdapter;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.GamePayRecordEntity;
import com.cointizen.paysdk.http.process.PayRecordProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import java.util.ArrayList;

/**
 * 描述：游戏账单activity
 * 时间: 2019/7/15 11:16 AM
 */
public class MCHPayRecordActivity extends MCHBaseActivity {

    private View btnMchBack;
    private SmartRefreshLayout smartRefresh;
    private ListView record_list;
    private TextView tv_money;
    private View layoutHaveData;
    private View tvMchNodata;
    ArrayList<GamePayRecordEntity.ListsBean> objects = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    Handler payRecordhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                    case Constant.PAY_RECORD_SUCCESS:
                        smartRefresh.finishLoadMore();
                        smartRefresh.finishRefresh();
                        GamePayRecordEntity gamePayRecordEntity = (GamePayRecordEntity)msg.obj;
                        tv_money.setText("￥"+gamePayRecordEntity.getCount());
                        if(gamePayRecordEntity.getLists().size()==0){
                            if(objects.size()==0){
                                layoutHaveData.setVisibility(View.GONE);
                                tvMchNodata.setVisibility(View.VISIBLE);
                            }else{
                                ToastUtil.show(MCHPayRecordActivity.this,ActivityConstants.S_YPAzYNyejC);
                            }
                        }else{
                            objects.addAll(gamePayRecordEntity.getLists());
                            MCHGamePayListAdapter.notifyDataSetChanged();
                        }
                        break;
                    case Constant.PAY_RECORD_FAIL:
                        smartRefresh.finishLoadMore();
                        smartRefresh.finishRefresh();
                        if(objects.size()==0){
                            layoutHaveData.setVisibility(View.GONE);
                            tvMchNodata.setVisibility(View.VISIBLE);
                        }
                        break;
                }
        }
    };
    private int pager = 1;
    private PayRecordProcess payRecordProcess;
    private MCHGamePayListAdapter MCHGamePayListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(MCHInflaterUtils.getLayout(this, "mch_act_pay_record"));
        btnMchBack = findViewById(getId("btn_mch_back"));
        smartRefresh = (SmartRefreshLayout) findViewById(getId("SmartRefresh"));
        record_list = (ListView) findViewById(getId("record_list"));
        tv_money = (TextView) findViewById(getId("tv_money"));
        layoutHaveData = findViewById(getId("layout_haveData"));
        tvMchNodata = findViewById(getId("tv_mch_nodata"));

        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finish();
            }
        });

        smartRefresh.setRefreshHeader(new ClassicsHeader(this));
        smartRefresh.setRefreshFooter(new ClassicsFooter(this));
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = 1;
                objects.clear();
                payRecordProcess.setLimit(pager+"");
                payRecordProcess.post(payRecordhandler);
            }
        });
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pager = pager +1;
                payRecordProcess.setLimit(pager+"");
                payRecordProcess.post(payRecordhandler);
            }
        });

        MCHGamePayListAdapter = new MCHGamePayListAdapter(this,objects);
        record_list.setAdapter(MCHGamePayListAdapter);


        payRecordProcess = new PayRecordProcess(this);
        payRecordProcess.setLimit(pager+"");
        payRecordProcess.post(payRecordhandler);
    }

}
