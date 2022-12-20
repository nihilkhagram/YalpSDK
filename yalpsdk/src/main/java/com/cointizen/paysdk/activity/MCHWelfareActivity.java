package com.cointizen.paysdk.activity;

import android.os.Bundle;
import android.view.View;

/**
 * 描述：福利activity
 * 时间: 2018-10-22 11:03
 */

public class MCHWelfareActivity extends MCHBaseActivity {

    private View btnMchBack;
    private View btnMcDaka;
    private View btnMcAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_welfare"));
        btnMchBack = findViewById(getId("btn_mch_back"));
        btnMcDaka = findViewById(getId("btn_mc_daka"));
        btnMcAd = findViewById(getId("btn_mc_ad"));
        BtnClick btnClick = new BtnClick();
        btnMchBack.setOnClickListener(btnClick);
        btnMcDaka.setOnClickListener(btnClick);
        btnMcAd.setOnClickListener(btnClick);

    }

    class BtnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId()==btnMchBack.getId()){
                finish();
            }else if(view.getId()==btnMcDaka.getId()){
//                DakaDialog dakaDialog = new DakaDialog(WelfareActivity.this);
//                dakaDialog.show(getFragmentManager(),"DakaDialog");
            }else if(view.getId()==btnMcAd.getId()){
                //广告福利
            }
        }
    }

}
