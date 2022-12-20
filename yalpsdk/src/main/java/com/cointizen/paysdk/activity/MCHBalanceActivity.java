package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.paysdk.entity.ChannelAndGameInfo;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHBanlancePagerAdapter;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.UserInfoProcess;
import com.cointizen.paysdk.utils.AppStatus;
import com.cointizen.paysdk.utils.AppStatusManager;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：余额activity
 * 时间: 2018-09-11 9:57
 */

public class MCHBalanceActivity extends FragmentActivity {

    private TextView btnMchPtb;
    private TextView btnMchBb;
    private TextView btnMchJf;
    private TextView btnMchJb;
    private MCHBanlancePagerAdapter MCHBanlancePagerAdapter = new MCHBanlancePagerAdapter(getSupportFragmentManager());
    private ViewPager balancePager;
    private RelativeLayout imgXian1,imgXian2,imgXian3,imgXian4;
    private RelativeLayout layoutTitleBar;
    private int lan;
    private int hei;
    private int pager = 3;
    private String TAG = "MCBalanceActivity";

    private boolean isFirst = true;
    private boolean isHaveJF = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE){
            finish();
            return;
        }
        setContentView(getLayout("mch_act_balance"));
        initvView();

        if (Constant.MCH_BACKGROUND_VERSION >= Constant.VERSION_840){
            isHaveJF = true;
            btnMchJf.setVisibility(View.VISIBLE);
            imgXian3.setVisibility(View.INVISIBLE);
        }else {
            pager = pager-1;
        }

        layoutTitleBar.setVisibility(View.VISIBLE);
        MCHBanlancePagerAdapter.setPager(pager);

        quereUserInfo();

//        PayTypeProcess payTypeProcess = new PayTypeProcess(); //先获取支付方式开关（主要是平台币开关）
//        payTypeProcess.post(payTypeHandler);
    }


//    @SuppressLint("HandlerLeak")
//    Handler payTypeHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case Constant.GAME_PAY_TYPE_SUCCESS:
////                    TiXianProcess tiXianProcess = new TiXianProcess();  //请求提现信息
////                    tiXianProcess.post(handler);
//                    //判断是否开启平台币功能
////                    if (GamePayTypeEntity.isHavePTB){
////                        btnMchPtb.setVisibility(View.VISIBLE);
////                    }else {
////                        pager = pager-1;
////                    }
//
//                    layoutTitleBar.setVisibility(View.VISIBLE);
//                    mcBanlancePagerAdapter.setPager(pager);
//
//                    quereUserInfo();
//
//                    break;
//                case Constant.GAME_PAY_TYPE_FAIL:
//                    ToastUtil.show(MCBalanceActivity.this,ActivityConstants.S_MeOziBvurF);
//                    break;
//            }
//        }
//    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
//                case Constant.TIXIAN_SUCCESS:
//                    //判断是否开启平台币功能
////                    if (GamePayTypeEntity.isHavePTB){
////                        btnMchPtb.setVisibility(View.VISIBLE);
////                    }else {
////                        pager = pager-1;
////                    }
//
//                    //判断是否开启小号（金币）功能
//                    TiXianBean obj = (TiXianBean) msg.obj;
//                    if(obj.getIS_OPEN_SMALL_ACCOUNT()==1){
//                        btnMchJb.setVisibility(View.VISIBLE);
//                        Constant.TiXian = obj.getUrl();
//                    }else{
//                        pager = pager-1;
//                    }
//
//                    layoutTitleBar.setVisibility(View.VISIBLE);
//                    mcBanlancePagerAdapter.setPager(pager);
//
//                    quereUserInfo();
//                    break;
//                case Constant.TIXIAN_FAIL:
//                    pager = pager-1;
//                    break;
                case Constant.GET_USER_INFO_SUCCESS:// 获取用户信息
                    ChannelAndGameInfo info = (ChannelAndGameInfo) msg.obj;
                    if (null != info) {
                        MCHBanlancePagerAdapter.setData(1,info.getPlatformMoney()+"");
                        MCHBanlancePagerAdapter.setData(3,info.getPoint()+"");
                        MCHBanlancePagerAdapter.setData(4,info.getGold_coin()+"");
                    } else {
                        ToastUtil.show(MCHBalanceActivity.this, ActivityConstants.S_re_login_needed);
                        finish();
                    }
                    break;
                case Constant.GET_USER_INFO_FAIL:
                    String tip = (String) msg.obj;
                    ToastUtil.show(MCHBalanceActivity.this, tip);
                    break;
            }

        }
    };



    private void initvView() {
        BtnClick btnClick = new BtnClick();
        View btnMchBack = findViewById(getId("btn_mch_back"));
        btnMchPtb = findViewById(getId("btn_mch_ptb"));
        btnMchBb = findViewById(getId("btn_mch_bb"));
        btnMchJf = findViewById(getId("btn_mch_jf"));
        btnMchJb = findViewById(getId("btn_mch_jb"));
        layoutTitleBar = findViewById(getId("layout_title_bar"));
        imgXian1 = findViewById(getId("img_xian_1"));
        imgXian2 = findViewById(getId("img_xian_2"));
        imgXian3 = findViewById(getId("img_xian_3"));
        imgXian4 = findViewById(getId("img_xian_4"));
        imgXian3.setVisibility(View.GONE);
        imgXian4.setVisibility(View.GONE);
        btnMchJb.setVisibility(View.GONE);
//        btnMchPtb.setVisibility(View.GONE);
        btnMchJf.setVisibility(View.GONE);
        balancePager = findViewById(getId("balance_pager"));
        btnMchBack.setOnClickListener(btnClick);
        btnMchPtb.setOnClickListener(btnClick);
        btnMchBb.setOnClickListener(btnClick);
        btnMchJf.setOnClickListener(btnClick);
        btnMchJb.setOnClickListener(btnClick);
        lan = getResources().getColor(MCHInflaterUtils.getIdByName(this, "color", "mch_yanse"));
        hei = getResources().getColor(MCHInflaterUtils.getIdByName(this, "color", "mch_hei"));
        btnMchPtb.setTextColor(lan);

        balancePager.setOffscreenPageLimit(pager);
        balancePager.setAdapter(MCHBanlancePagerAdapter);
        balancePager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                if(GamePayTypeEntity.isHavePTB){
//                    switch (position){
//                        case 0:
//                            btnMchPtb.setTextColor(lan);
//                            btnMchBb.setTextColor(hei);
//                            btnMchJf.setTextColor(hei);
//                            btnMchJb.setTextColor(hei);
//                            break;
//                        case 1:
//                            btnMchPtb.setTextColor(hei);
//                            btnMchBb.setTextColor(lan);
//                            btnMchJf.setTextColor(hei);
//                            btnMchJb.setTextColor(hei);
//                            break;
//                        case 2:
//                            btnMchPtb.setTextColor(hei);
//                            btnMchBb.setTextColor(hei);
//                            btnMchJf.setTextColor(lan);
//                            btnMchJb.setTextColor(hei);
//                            break;
//                        case 3:
//                            btnMchPtb.setTextColor(hei);
//                            btnMchBb.setTextColor(hei);
//                            btnMchJf.setTextColor(hei);
//                            btnMchJb.setTextColor(lan);
//                            break;
//                    }
//                }else {
//                    switch (position){
//                        case 0:
//                            btnMchPtb.setTextColor(hei);
//                            btnMchBb.setTextColor(lan);
//                            btnMchJf.setTextColor(hei);
//                            btnMchJb.setTextColor(hei);
//                            break;
//                        case 1:
//                            btnMchPtb.setTextColor(hei);
//                            btnMchBb.setTextColor(hei);
//                            btnMchJf.setTextColor(lan);
//                            btnMchJb.setTextColor(hei);
//                            break;
//                        case 2:
//                            btnMchPtb.setTextColor(hei);
//                            btnMchBb.setTextColor(hei);
//                            btnMchJf.setTextColor(hei);
//                            btnMchJb.setTextColor(lan);
//                            break;
//                    }
//                }
                switch (position) {
                    case 0:
                        btnMchPtb.setTextColor(lan);
                        btnMchBb.setTextColor(hei);
                        btnMchJf.setTextColor(hei);
                        btnMchJb.setTextColor(hei);
                        imgXian1.setVisibility(View.VISIBLE);
                        imgXian2.setVisibility(View.INVISIBLE);
                        if (isHaveJF){
                            imgXian3.setVisibility(View.INVISIBLE);
                        }else {
                            imgXian3.setVisibility(View.GONE);
                        }
//                        imgXian4.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        btnMchPtb.setTextColor(hei);
                        btnMchBb.setTextColor(lan);
                        btnMchJf.setTextColor(hei);
                        btnMchJb.setTextColor(hei);
                        imgXian1.setVisibility(View.INVISIBLE);
                        imgXian2.setVisibility(View.VISIBLE);
                        if (isHaveJF){
                            imgXian3.setVisibility(View.INVISIBLE);
                        }else {
                            imgXian3.setVisibility(View.GONE);
                        }
//                        imgXian4.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        btnMchPtb.setTextColor(hei);
                        btnMchBb.setTextColor(hei);
                        btnMchJf.setTextColor(lan);
                        btnMchJb.setTextColor(hei);
                        imgXian1.setVisibility(View.INVISIBLE);
                        imgXian2.setVisibility(View.INVISIBLE);
                        imgXian3.setVisibility(View.VISIBLE);
//                        imgXian4.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        btnMchPtb.setTextColor(hei);
                        btnMchBb.setTextColor(hei);
                        btnMchJf.setTextColor(hei);
                        btnMchJb.setTextColor(lan);
                        imgXian1.setVisibility(View.INVISIBLE);
                        imgXian2.setVisibility(View.INVISIBLE);
                        if (isHaveJF){
                            imgXian3.setVisibility(View.INVISIBLE);
                        }else {
                            imgXian3.setVisibility(View.GONE);
                        }
                        imgXian4.setVisibility(View.VISIBLE);
                        break;
                }
            }

        });
    }


    class BtnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId()==getId("btn_mch_back")){
                finish();
            }else if(view.getId()==btnMchPtb.getId()){
                //TODO 平台币
                btnMchPtb.setTextColor(lan);
                btnMchBb.setTextColor(hei);
                btnMchJf.setTextColor(hei);
                btnMchJb.setTextColor(hei);
                balancePager.setCurrentItem(0);
                imgXian1.setVisibility(View.VISIBLE);
                imgXian2.setVisibility(View.INVISIBLE);
                if (isHaveJF){
                    imgXian3.setVisibility(View.INVISIBLE);
                }else {
                    imgXian3.setVisibility(View.GONE);
                }
//                imgXian4.setVisibility(View.INVISIBLE);
            }else if(view.getId()==btnMchBb.getId()){
                //TODO 绑币
                btnMchPtb.setTextColor(hei);
                btnMchBb.setTextColor(lan);
                btnMchJf.setTextColor(hei);
                btnMchJb.setTextColor(hei);
                balancePager.setCurrentItem(1);
                imgXian1.setVisibility(View.INVISIBLE);
                imgXian2.setVisibility(View.VISIBLE);
                if (isHaveJF){
                    imgXian3.setVisibility(View.INVISIBLE);
                }else {
                    imgXian3.setVisibility(View.GONE);
                }
//                imgXian4.setVisibility(View.INVISIBLE);

//                if (GamePayTypeEntity.isHavePTB){
//                    balancePager.setCurrentItem(1);
//                }else {
//                    balancePager.setCurrentItem(0);
//                }
            }else if(view.getId()==btnMchJf.getId()){
                //TODO 积分
                btnMchPtb.setTextColor(hei);
                btnMchBb.setTextColor(hei);
                btnMchJf.setTextColor(lan);
                btnMchJb.setTextColor(hei);
                balancePager.setCurrentItem(2);
                imgXian1.setVisibility(View.INVISIBLE);
                imgXian2.setVisibility(View.INVISIBLE);
                imgXian3.setVisibility(View.VISIBLE);
//                imgXian4.setVisibility(View.INVISIBLE);
//                if (GamePayTypeEntity.isHavePTB){
//                    balancePager.setCurrentItem(2);
//                }else {
//                    balancePager.setCurrentItem(1);
//                }
            }else if(view.getId()==btnMchJb.getId()){
                //TODO 金币
                btnMchPtb.setTextColor(hei);
                btnMchBb.setTextColor(hei);
                btnMchJf.setTextColor(hei);
                btnMchJb.setTextColor(lan);
                balancePager.setCurrentItem(3);
                imgXian1.setVisibility(View.INVISIBLE);
                imgXian2.setVisibility(View.INVISIBLE);
                if (isHaveJF){
                    imgXian3.setVisibility(View.INVISIBLE);
                }else {
                    imgXian3.setVisibility(View.GONE);
                }
                imgXian4.setVisibility(View.VISIBLE);
//                if (GamePayTypeEntity.isHavePTB){
//                    balancePager.setCurrentItem(3);
//                }else {
//                    balancePager.setCurrentItem(2);
//                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst){
            quereUserInfo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirst = false;
    }

    /**
     * 获取用户信息
     */
    private void quereUserInfo() {
        UserInfoProcess userInfoProcess = new UserInfoProcess();
        userInfoProcess.post(handler);
    }

    protected int getLayout(String layoutName) {
        return MCHInflaterUtils.getLayout(this, layoutName);
    }

    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(this, idName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            finish();
    }
}
