package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cointizen.paysdk.entity.ChannelAndGameInfo;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.JfPtbProcess;
import com.cointizen.paysdk.http.process.UserInfoProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;

/**
 * 描述：兑换平台币
 * 时间: 2018-09-28 17:22
 */

public class MCHExchangePtbActivity extends MCHBaseActivity {

    private String account;
    private TextView tvJf;
    private EditText editNum;
    private TextView tvXhjf;
    private TextView tvDhbl;
    private View btnMchDuihuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_dhptb"));
        View btnMchDhjl = findViewById(getId("btn_mch_dhjl"));
        View btnMchBack = findViewById(getId("btn_mch_back"));
        tvJf = findViewById(getId("tv_jf"));
        editNum = findViewById(getId("edit_num"));
        tvXhjf = findViewById(getId("tv_xhjf"));
        tvDhbl = findViewById(getId("tv_dhbl"));
        btnMchDuihuan = findViewById(getId("btn_mch_duihuan"));

        btnMchDhjl.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                startActivity(new Intent(MCHExchangePtbActivity.this, MCHRecordExchangeActivity.class));
            }
        });

        btnMchDuihuan.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                btnMchDuihuan.setFocusable(false);
                DH();
            }
        });

        editNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString().trim();
                if(!s.equals("")){
                    Integer integer = Integer.valueOf(s);
                    int i = integer * 100;
                    tvXhjf.setText(i+"");
                }else{
                    tvXhjf.setText("0");
                }
            }
        });

        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
        quereUserInfo();
    }

    /**
     * 兑换
     */
    private void DH() {
        String s = tvXhjf.getText().toString();
        Integer integer = Integer.valueOf(s);
        String s1 = tvJf.getText().toString();
        Integer myJifen = Integer.valueOf(s1);
        if(integer==0){
            ToastUtil.show(this,ActivityConstants.S_SehUUQoTUm);
            return;
        }
        if(myJifen>integer){
            JfPtbProcess jfPtbProcess = new JfPtbProcess();
            jfPtbProcess.setNum(integer/100+"");
            jfPtbProcess.post(handler);
            editNum.setText("");
        }else{
            btnMchDuihuan.setFocusable(true);
            ToastUtil.show(this,ActivityConstants.S_URKPtPtqWv);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.GET_USER_INFO_SUCCESS:// 获取用户信息
                    ChannelAndGameInfo info = (ChannelAndGameInfo) msg.obj;
                    if (null != info) {
                        tvJf.setText(info.getPoint());
                    } else {
                        ToastUtil.show(MCHExchangePtbActivity.this, ActivityConstants.S_re_login_needed);
                        finish();
                    }
                    break;
                case Constant.GET_USER_INFO_FAIL:
                    String tip = (String) msg.obj;
                    ToastUtil.show(MCHExchangePtbActivity.this, tip);
                    break;
                case Constant.JFPTB_SUCCESS:
                    ToastUtil.show(MCHExchangePtbActivity.this, (String) msg.obj);
                    quereUserInfo();
                    btnMchDuihuan.setFocusable(true);
                    break;
                case Constant.JFPTB_FAIL:
                    btnMchDuihuan.setFocusable(true);
                    ToastUtil.show(MCHExchangePtbActivity.this, (String) msg.obj);
                    break;
            }
        }
    };


    /**
     * 获取用户信息
     */
    private void quereUserInfo() {
        account = UserLoginSession.getInstance().getChannelAndGame().getAccount();
        UserInfoProcess userInfoProcess = new UserInfoProcess();
        userInfoProcess.post(handler);
    }
}
