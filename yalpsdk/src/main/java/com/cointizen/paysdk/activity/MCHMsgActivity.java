package com.cointizen.paysdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cointizen.smartrefresh.layout.SmartRefreshLayout;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHMsgListAdapter;
import com.cointizen.paysdk.entity.MsgTZModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.MsgTZProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppStatus;
import com.cointizen.paysdk.utils.AppStatusManager;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import java.util.ArrayList;

/**
 * 描述：消息页面
 * 时间: 2018-10-11 17:05
 */

public class MCHMsgActivity extends FragmentActivity {

    private SmartRefreshLayout layoutHavedata;
    private TextView tvMchNodata;
    private MCHMsgListAdapter MCHMsgListAdapter;
    ArrayList<MsgTZModel.ListBean> listBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE){
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_msg"));
        View btnMchBack = findViewById(getId("btn_mch_back"));
        ListView listMsg = findViewById(MCHInflaterUtils.getIdByName(this, "id", "list_msg"));
        layoutHavedata = findViewById(MCHInflaterUtils.getIdByName(this, "id", "layout_havedata"));
        tvMchNodata = findViewById(MCHInflaterUtils.getIdByName(this, "id", "tv_mch_nodata"));
        tvMchNodata.setText(ActivityConstants.S_VfvlvuawkF);

        layoutHavedata.setEnableLoadMore(false);
        layoutHavedata.setEnableRefresh(false);

        MCHMsgListAdapter = new MCHMsgListAdapter(listBeans,this);

        listMsg.setAdapter(MCHMsgListAdapter);
        listMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MCHMsgActivity.this, MCHMsgDetActivity.class);
                intent.putExtra("notice_id",listBeans.get(i).getNotice_id());
                intent.putExtra("title",listBeans.get(i).getTitle());
                intent.putExtra("time",listBeans.get(i).getCreate_time() + "");
                intent.putExtra("viewUrl",listBeans.get(i).getUrl());
                startActivity(intent);
            }
        });
        getData();
        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
    }


    private void getData(){
        MsgTZProcess msgTZProcess = new MsgTZProcess();
        msgTZProcess.setLimit("1");
        msgTZProcess.post(handler);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.MSGTZ_SUCCESS:
                    MsgTZModel obj = (MsgTZModel) msg.obj;
                    listBeans.addAll(obj.getList());
                    MCHMsgListAdapter.notifyDataSetChanged();
                    if(listBeans.size() == 0) {
                        layoutHavedata.setVisibility(View.GONE);
                        tvMchNodata.setVisibility(View.VISIBLE);
                    }else {
                        int size = obj.getList().size();
                        if(size == 0){
                            ToastUtil.show(MCHMsgActivity.this,ActivityConstants.S_kYTjBFajtL);
                        }
                    }
                    break;
                case Constant.MSGTZ_FAIL:
                    ToastUtil.show(MCHMsgActivity.this,(String)msg.obj);
                    break;
            }
            layoutHavedata.finishRefresh();
            layoutHavedata.finishLoadMore();
        }
    };




    protected int getLayout(String layoutName) {
        return MCHInflaterUtils.getLayout(this, layoutName);
    }

    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(this, idName);
    }
}
