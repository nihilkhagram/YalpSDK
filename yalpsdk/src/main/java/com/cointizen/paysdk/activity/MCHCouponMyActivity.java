package com.cointizen.paysdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.open.ApiCallback;
import com.cointizen.paysdk.http.process.GetAvailableCouponProcess;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHCouponAdapter;
import com.cointizen.paysdk.bean.CouponBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MCHCouponMyActivity extends MCHBaseActivity {

    private final String TAG = "MCCouponAllActivity";
    private GridView gridView;
    private TextView tvNoData;
    private TextView tvMore;
    private MCHCouponAdapter adapter;
    private List<CouponBean> listData = new ArrayList<>();
    private int pos; //支付界面已经选中的券坐标
    private boolean canInitCoupon;
    boolean isbind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MCHInflaterUtils.getLayout(this, "mch_activity_coupon_my"));

        initview();

        isbind = getIntent().getBooleanExtra("isbind",false);
        pos = getIntent().getIntExtra("select_position",-1);
        final List<CouponBean> list = (List<CouponBean>) getIntent().getSerializableExtra("listData");
        if (list != null){  //判断是否 是从支付界面跳转来的
            Message msg = new Message();
            msg.what = Constant.USABLE_COUPON_SUCCESS;
            msg.obj = list;
            handler.sendMessage(msg);
        }

    }


    private void initview() {
        RelativeLayout btnBack = findViewById(MCHInflaterUtils.getControl(this, "btn_mch_back"));
        btnBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });

        tvMore = findViewById(MCHInflaterUtils.getControl(this, "btn_mch_pay"));
        tvNoData = findViewById(MCHInflaterUtils.getControl(this, "mch_tv_no_data"));
        tvNoData.setVisibility(View.GONE);

        gridView = findViewById(MCHInflaterUtils.getControl(this, "mch_gridview"));
        adapter = new MCHCouponAdapter(this,null);
        gridView.setAdapter(adapter);

        if (isScreenOriatationPortrait(this)){  //判断是否是竖屏
            gridView.setNumColumns(2);
        }else {
            gridView.setNumColumns(3);
        }

        tvMore.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                canInitCoupon = true;
                Intent intent = new Intent(MCHCouponMyActivity.this, MCHCouponAllActivity.class);
                intent.putExtra("isbind", isbind);
                startActivity(intent);
            }
        });
    }


    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            listData.clear();
            switch (msg.what){
                case Constant.USABLE_COUPON_SUCCESS:
                    listData = (List<CouponBean>) msg.obj;
                    if (listData!=null && listData.size()>0){
                        gridView.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                        adapter.setData(listData);
                        MCLog.e(TAG,ActivityConstants.Log_QKCGRNimZr);
                        if (pos != -1){
                            adapter.setSelectPosition(pos);
                        }

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                adapter.setSelectPosition(i);
                                Intent intent = new Intent();
                                intent.putExtra("coupon_id", listData.get(i).getId());
                                intent.putExtra("subtract_money", listData.get(i).getMoney());
                                intent.putExtra("select_position", i);
                                setResult(Constant.COUPON_OK_CODE, intent);
                                finish();
                            }
                        });
                    }else {
                        gridView.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                    break;
                case Constant.USABLE_COUPON_FAIL:
                    gridView.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(msg.obj.toString())){
                        ToastUtil.show(MCHCouponMyActivity.this,msg.obj.toString());
                    }
                    break;
            }
        }
    };



    //判断是否是竖屏
    private boolean isScreenOriatationPortrait (Context context){
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (canInitCoupon){
            GetAvailableCouponProcess useableProcess = new GetAvailableCouponProcess(); //请求可用满减券
            useableProcess.setPrice(ApiCallback.order().getGoodsPriceYuan());
            useableProcess.setBind(isbind ? "1" : "0");
            useableProcess.post(handler);
        }
    }
}
