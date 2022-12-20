package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cointizen.paysdk.adapter.MCHHelperDetListAdapter;
import com.cointizen.paysdk.bean.HelperDetBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.HelperDetProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;

/**
 * 描述：问题详情
 * 时间: 2018-10-27 10:36
 */

public class MCHelperDetActivity extends MCHBaseActivity {

    private ListView listHelper;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_helperdet"));
        String mark = getIntent().getStringExtra("mark");
        String title = getIntent().getStringExtra("title");
        pos = getIntent().getIntExtra("pos", 0);
        listHelper = findViewById(getId("list_helper"));
        View btnMchBack = findViewById(getId("btn_mch_back"));
        TextView tvYue = findViewById(getId("tv_yue"));
        tvYue.setText(title);

        HelperDetProcess helperDetProcess = new HelperDetProcess();
        helperDetProcess.setMark(mark);
        helperDetProcess.post(handler);
        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.HELPERDET_SUCCESS:
                    HelperDetBean obj = (HelperDetBean) msg.obj;
                    MCHHelperDetListAdapter MCHHelperDetListAdapter = new MCHHelperDetListAdapter(obj.getData(),MCHelperDetActivity.this);
                    listHelper.setAdapter(MCHHelperDetListAdapter);
                    listHelper.smoothScrollToPosition(pos);
                    break;
                case Constant.HELPERDET_FAIL:
                    break;
            }
        }
    };
}
