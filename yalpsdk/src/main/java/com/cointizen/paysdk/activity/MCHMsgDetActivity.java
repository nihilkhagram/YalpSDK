package com.cointizen.paysdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppUtils;

/**
 * 描述：消息详情Activity
 * 时间: 2018-10-11 19:59
 */

public class MCHMsgDetActivity extends MCHBaseActivity {

    private String noticeId;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvMcCon;
    private TextView btnShare;
    private View layoutJiangli;
    private TextView tvPtb;
    private TextView tvGame;
    private TextView tvAccount;
    private TextView tvShijian;
    private WebView webview;
    TextView tvJlptb;
    private String title;
    private String time;
    private String context;
    private String viewUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_msgdet"));
        Intent intent = getIntent();
        noticeId = intent.getStringExtra("notice_id");
        title = intent.getStringExtra("title");
        time = intent.getStringExtra("time");
        context = intent.getStringExtra("context");
        viewUrl = intent.getStringExtra("viewUrl");

        View btnMchBack = findViewById(getId("btn_mch_back"));
        tvTitle = findViewById(getId("tv_title"));
        tvTime = findViewById(getId("tv_time"));
        tvMcCon = findViewById(getId("tv_mc_con"));
        layoutJiangli = findViewById(getId("layout_jiangli"));
        tvPtb = findViewById(getId("tv_ptb"));
        tvJlptb = findViewById(getId("tv_jlptb"));
        tvGame = findViewById(getId("tv_game"));
        tvAccount = findViewById(getId("tv_account"));
        tvShijian = findViewById(getId("tv_shijian"));
        btnShare = findViewById(getId("btn_share"));
        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
        btnShare.setVisibility(View.GONE);
//        btnShare.setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View view) {
//                if(!Constant.ShareUrl.equals("")){
//                    startActivity(new Intent(MCMsgDetActivity.this,MCShareActivity.class));
//                }else{
//                    ToastUtil.show(MCMsgDetActivity.this,ActivityConstants.S_ufPguTBQWV);
//                }
//            }
//        });

        layoutJiangli.setVisibility(View.GONE);
        tvMcCon.setVisibility(View.GONE);
        tvMcCon.setText(context);
        tvTitle.setText(title);
        tvTime.setText(AppUtils.MonthDay(time,"yyyy/MM/dd HH:mm:ss"));
        webview = findViewById(getId("mch_msg_webview"));
        webview.setVisibility(View.VISIBLE);
        webview.loadUrl(viewUrl);
    }

}
