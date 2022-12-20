package com.cointizen.paysdk.activity;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.paysdk.adapter.MCHCouponPagerAdapter;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

public class MCHCouponAllActivity extends FragmentActivity {
    private final String TAG = "MCCouponAllActivity";

    private TextView btnKeLing,btnYiLing,btnGuoQi;
    private RelativeLayout imgXian1,imgXian2,imgXian3;
    private ViewPager viewPager;
    private int lan;
    private int hei;
    private MCHCouponPagerAdapter couponPagerAdapter;
    private boolean isbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MCHInflaterUtils.getLayout(this, "mch_activity_coupon_all"));
        isbind = getIntent().getBooleanExtra("isbind",false);
        initview();
    }


    private void initview() {
        BtnClick btnClick = new BtnClick();
        RelativeLayout btnBack = findViewById(MCHInflaterUtils.getControl(this, "btn_mch_back"));
        btnBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });

        btnKeLing = findViewById(MCHInflaterUtils.getControl(this, "btn_mch_keling"));
        btnKeLing.setOnClickListener(btnClick);
        btnYiLing = findViewById(MCHInflaterUtils.getControl(this, "btn_mch_yiling"));
        btnYiLing.setOnClickListener(btnClick);
        btnGuoQi = findViewById(MCHInflaterUtils.getControl(this, "btn_mch_guoqi"));
        btnGuoQi.setOnClickListener(btnClick);
        imgXian1 = findViewById(MCHInflaterUtils.getControl(this, "img_xian_1"));
        imgXian2 = findViewById(MCHInflaterUtils.getControl(this, "img_xian_2"));
        imgXian3 = findViewById(MCHInflaterUtils.getControl(this, "img_xian_3"));
        viewPager = findViewById(MCHInflaterUtils.getControl(this, "view_pager"));

        lan = getResources().getColor(MCHInflaterUtils.getIdByName(this, "color", "mch_yanse"));
        hei = getResources().getColor(MCHInflaterUtils.getIdByName(this, "color", "mch_hei"));

        couponPagerAdapter = new MCHCouponPagerAdapter(getSupportFragmentManager(), isbind);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(couponPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        btnKeLing.setTextColor(lan);
                        btnYiLing.setTextColor(hei);
                        btnGuoQi.setTextColor(hei);
                        imgXian1.setVisibility(View.VISIBLE);
                        imgXian2.setVisibility(View.INVISIBLE);
                        imgXian3.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        btnKeLing.setTextColor(hei);
                        btnYiLing.setTextColor(lan);
                        btnGuoQi.setTextColor(hei);
                        imgXian1.setVisibility(View.INVISIBLE);
                        imgXian2.setVisibility(View.VISIBLE);
                        imgXian3.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        btnKeLing.setTextColor(hei);
                        btnYiLing.setTextColor(hei);
                        btnGuoQi.setTextColor(lan);
                        imgXian1.setVisibility(View.INVISIBLE);
                        imgXian2.setVisibility(View.INVISIBLE);
                        imgXian3.setVisibility(View.VISIBLE);
                        break;
                }
            }

        });

    }



    class BtnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view.getId()==btnKeLing.getId()){
                //TODO 可领取
                btnKeLing.setTextColor(lan);
                btnYiLing.setTextColor(hei);
                btnGuoQi.setTextColor(hei);
                imgXian1.setVisibility(View.VISIBLE);
                imgXian2.setVisibility(View.INVISIBLE);
                imgXian3.setVisibility(View.INVISIBLE);
                viewPager.setCurrentItem(0);
            }else if(view.getId()==btnYiLing.getId()){
                //TODO 已领取
                btnKeLing.setTextColor(hei);
                btnYiLing.setTextColor(lan);
                btnGuoQi.setTextColor(hei);
                imgXian1.setVisibility(View.INVISIBLE);
                imgXian2.setVisibility(View.VISIBLE);
                imgXian3.setVisibility(View.INVISIBLE);
                viewPager.setCurrentItem(1);

            }else if(view.getId()==btnGuoQi.getId()){
                //TODO 已过期
                btnKeLing.setTextColor(hei);
                btnYiLing.setTextColor(hei);
                btnGuoQi.setTextColor(lan);
                imgXian1.setVisibility(View.INVISIBLE);
                imgXian2.setVisibility(View.INVISIBLE);
                imgXian3.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(2);
            }
        }
    }

    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(this, idName);
    }
}
