package com.cointizen.paysdk.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.cointizen.paysdk.bean.ChoosePaymentMethod;
import com.cointizen.paysdk.bean.MCPayModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：充值页面
 * 时间: 2018-09-18 11:49
 */
public class MCHPayActivity extends MCHBaseCompatAcitvity {

    private ChoosePaymentMethod choosePaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int payviewLayoutId = MCHInflaterUtils.getLayout(MCHPayActivity.this, "mch_act_pay");
        View customView = LayoutInflater.from(MCHPayActivity.this).inflate(payviewLayoutId, null);
        setContentView(customView);
        choosePaymentMethod = new ChoosePaymentMethod(MCHPayActivity.this, customView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            MCPayModel.Instance().getPaymentCallback().callback("-1");//支付取消
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (choosePaymentMethod != null){
            choosePaymentMethod.initCoupon(); //获取代金券数据
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String done = sp.getString("done", "failed"); // Second parameter is the default value.
        if (done.equals("donePayment")){
            finish();
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("done");
            editor.clear();
            editor.apply();
        }
    }

    @Override
    protected void onDestroy() {
        if (choosePaymentMethod != null){
            choosePaymentMethod.onDestroy();
        }
        super.onDestroy();
    }

    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(this, idName);
    }


    /**
     * 接收到满减券页面的信息（点击选中的满减券）
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CHOOSE_COUPON_CODE){
            if (resultCode == Constant.COUPON_OK_CODE){
                String coupon_id = data.getStringExtra("coupon_id");
                String coupon_price = data.getStringExtra("subtract_money");
                int select_position  = data.getIntExtra("select_position",-1);  //选中的券的坐标
                choosePaymentMethod.setChooseCouponData(coupon_id,coupon_price,select_position);
            }
        }
    }
}
