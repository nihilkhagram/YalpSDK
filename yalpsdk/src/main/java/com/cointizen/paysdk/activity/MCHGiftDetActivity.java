package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.GiftDetModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.PackCodeEntity;
import com.cointizen.paysdk.http.process.GiftDetProcess;
import com.cointizen.paysdk.http.process.PacksCodeProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.BitmapHelp;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.TextUtils;
import com.cointizen.paysdk.view.gifimageview.GifImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 描述：礼包详情页
 * 时间: 2018-09-11 10:34
 */

public class MCHGiftDetActivity extends MCHBaseActivity {

    private View btnMchBack;
    private TextView txtMchPackName;
    private TextView tvShengyu;
    private TextView btnMchReceivePack;
    private View layoutJihuoma;
    private View btnFuzhi;
    private TextView tvJihuoma;
    private TextView tvMcYouxiaoqi;
    private TextView tvMcCon;
    private TextView tvMcShuoming;
    private TextView tvMcNotice;
//    private NiceImageView imgIcon;
    private GifImageView imgIcon;
    private String giftId;

    private int userVIPLev;
    private int giftVIPLev;
    private BitmapUtils bitmapUtils;
    private GiftDetModel obj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        giftId = getIntent().getStringExtra("gift_id");
        userVIPLev = getIntent().getIntExtra("user_vip",0);
        giftVIPLev = getIntent().getIntExtra("gift_vip",0);
        setContentView(MCHInflaterUtils.getLayout(this, "mch_act_giftdet"));

        imgIcon = findViewById(getId("img_mch_icon"));
        btnMchBack = findViewById(getId("btn_mch_back"));
        txtMchPackName = findViewById(getId("txt_mch_pack_name"));
        tvShengyu = findViewById(getId("tv_shengyu"));
        tvJihuoma = findViewById(getId("tv_jihuoma"));
        tvMcYouxiaoqi = findViewById(getId("tv_mc_youxiaoqi"));
        tvMcCon = findViewById(getId("tv_mc_con"));
        tvMcShuoming = findViewById(getId("tv_mc_shuoming"));
        tvMcNotice = findViewById(getId("tv_mc_notice"));
        layoutJihuoma = findViewById(getId("layout_jihuoma"));
        btnMchReceivePack = findViewById(getId("btn_mch_receive_pack"));
        btnMchReceivePack.setVisibility(View.GONE);
        btnFuzhi = findViewById(getId("btn_fuzhi"));
        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
        btnMchReceivePack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                btnMchReceivePack.setFocusable(false);
                setPackInfo();
            }
        });
        btnFuzhi.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tvJihuoma.getText().toString());
                ToastUtil.show(MCHGiftDetActivity.this,ActivityConstants.S_jcwKeodawj);
            }
        });
        bitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());
        GiftDetProcess giftDetProcess = new GiftDetProcess(this);
        giftDetProcess.setGiftId(giftId);
        giftDetProcess.post(handler);
    }

    public void setPackInfo() {
        PacksCodeProcess packCode = new PacksCodeProcess();
        packCode.setGiftId(obj.getId());
        packCode.post(handler);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.GIFTDET_SUCCESS:
                    obj = (GiftDetModel)msg.obj;
                    setData(obj);
                    break;
                case Constant.GIFTDET_FAIL:
                    String obj1 = (String) msg.obj;
                    if(obj1!=null){
                        ToastUtil.show(MCHGiftDetActivity.this,obj1);
                    }
                    break;
                case Constant.PACKS_CODE_FAIL:
                    btnMchReceivePack.setFocusable(true);
                    if((String) msg.obj!=null){
                        ToastUtil.show(MCHGiftDetActivity.this,(String) msg.obj);
                    }
                    break;
                case Constant.PACKS_CODE_SUCCESS:
                    PackCodeEntity packCode = (PackCodeEntity) msg.obj;
                    btnMchReceivePack.setVisibility(View.GONE);
                    layoutJihuoma.setVisibility(View.VISIBLE);
                    tvJihuoma.setText(packCode.getNovice());
                    GiftDetProcess giftDetProcess = new GiftDetProcess(MCHGiftDetActivity.this);
                    giftDetProcess.setGiftId(giftId);
                    giftDetProcess.post(handler);
                    break;
            }
        }
    };


    private void setData(final GiftDetModel obj) {
        txtMchPackName.setText(obj.getGiftbag_name());
//        tvShengyu.setText(obj.getSurplus()+"");
        tvShengyu.setText(AppUtils.getPercentage(obj.getNovice_num(),obj.getSurplus()) + "%");
        tvMcNotice.setText(obj.getNotice());
        if (!TextUtils.isEmpty(obj.getIcon())){
            String s = obj.getIcon().substring(obj.getIcon().length() - 4, obj.getIcon().length());
            if (s.equals(".gif") || s.equals(".GIF")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setFloatingPic(obj.getIcon());
                    }
                }).start();
            }else {
                bitmapUtils.display(imgIcon,obj.getIcon());
            }
        }

        if (userVIPLev >= giftVIPLev){
            if(obj.getReceived()==1){
                btnMchReceivePack.setVisibility(View.GONE);
                layoutJihuoma.setVisibility(View.VISIBLE);
                tvJihuoma.setText(obj.getRecord_novice());
            }else{
                btnMchReceivePack.setVisibility(View.VISIBLE);
                if(obj.getSurplus()==0){
                    btnMchReceivePack.setText(ActivityConstants.S_qTZOfAytGB);
                    btnMchReceivePack.setTextColor(getResources().getColor(MCHInflaterUtils.getIdByName(this, "color", "mch_hui")));
                    btnMchReceivePack.setBackgroundResource(getDrawable("mch_btn_hui_bg"));
                    btnMchReceivePack.setEnabled(false);
                }
            }
        }else {
            btnMchReceivePack.setVisibility(View.VISIBLE);
            btnMchReceivePack.setText(ActivityConstants.S_njxwdMpnVN);
            btnMchReceivePack.setTextColor(getResources().getColor(MCHInflaterUtils.getIdByName(this, "color", "mch_hui")));
            btnMchReceivePack.setBackgroundResource(getDrawable("mch_btn_hui_bg"));
            btnMchReceivePack.setEnabled(false);
        }

        if(obj.getEnd_time().equals("0")){
            tvMcYouxiaoqi.setText(ActivityConstants.S_JQhWbpWXVP+ AppUtils.MonthDay(obj.getStart_time(),"yyyy-MM-dd  HH:mm:ss")+" 至 长期有效");
        }else{
            tvMcYouxiaoqi.setText(ActivityConstants.S_JQhWbpWXVP+ AppUtils.MonthDay(obj.getStart_time(),"yyyy-MM-dd  HH:mm:ss")+" 至 "+ AppUtils.MonthDay(obj.getEnd_time(),"yyyy-MM-dd  HH:mm:ss"));
        }
        tvMcShuoming.setText(obj.getDesribe());
        tvMcCon.setText(obj.getDigest());
    }


    /**
     * 加载网络图片
     */
    private void setFloatingPic(String logo) {
        InputStream is = null;
        try {
            URL url = new URL(logo);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                imgIcon.setBytes(AppUtils.readStream(is));
                imgIcon.startAnimation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        imgIcon.stopAnimation();
    }
}
