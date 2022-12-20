package com.cointizen.plugin.vip.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.activity.MCHBaseActivity;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.ListViewUtils;
import com.cointizen.plugin.vip.adapter.MCVipListAdapter;
import com.cointizen.plugin.vip.http.process.VipProcess;

import java.util.List;

/**
 * 描述：等级Activity
 * 时间: 2018-11-23 11:52
 */

public class MCHGradeActivity extends MCHBaseActivity {

    private ListView listView;
    private MCVipListAdapter listAdapter;
    private TextView tvVipLevel;
    private TextView tvVipMoney;
    private LinearLayout llVipMoney;

    private int vipLevel;
    private String nextVipMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_grade"));
        View btnMchBack = findViewById(getId("btn_mch_back"));
        listView = findViewById(getId("mch_vip_level_list"));
        tvVipLevel = findViewById(getId("mch_tv_vip_leve"));
        tvVipMoney = findViewById(getId("mch_tv_vip_money"));
        llVipMoney = findViewById(getId("mch_ll_vip_money"));
        llVipMoney.setVisibility(View.GONE);

        vipLevel = getIntent().getIntExtra("vip_level",0);
        nextVipMoney = getIntent().getStringExtra("next_vip");

        tvVipLevel.setText("VIP" + vipLevel);

        if (Double.valueOf(nextVipMoney)!=0){
            llVipMoney.setVisibility(View.VISIBLE);
            tvVipMoney.setText("¥" + nextVipMoney);
        }

        listAdapter = new MCVipListAdapter(this);
        listView.setAdapter(listAdapter);

        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });

        VipProcess vipProcess = new VipProcess();
        vipProcess.post(handler);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.VIP_SUCCESS:
                    List<String> listData = (List<String>) msg.obj;
                    listAdapter.setListData(listData);
                    ListViewUtils.calculateListViewHeight(listView);
                    break;
                case Constant.VIP_FAIL:
                    ToastUtil.show(MCHGradeActivity.this,msg.obj.toString());
                    break;
            }
        }
    };
}

