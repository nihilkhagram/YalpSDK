package com.cointizen.paysdk.bean.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cointizen.open.ApiCallback;
import com.cointizen.open.FlagControl;
import com.cointizen.paysdk.AppConstants;
import com.cointizen.paysdk.activity.MCHWapPayActivity;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.WXOrderInfo;
import com.cointizen.paysdk.http.process.MGatePaymentProcess;
import com.cointizen.paysdk.payment.MGatePaymentRequest;
import com.cointizen.paysdk.utils.ToastUtil;

public class MGatePay {

    private Activity mainActivity;
    MGatePaymentRequest mGatePaymentRequest;
    MGatePaymentProcess mGatePaymentProcess;

    public MGatePay(Activity context, MGatePaymentRequest mGatePaymentRequest) {
        if(null != mainActivity) {
            this.mainActivity = context;
        }
        this.mGatePaymentRequest = mGatePaymentRequest;
        this.mGatePaymentProcess = new MGatePaymentProcess(mGatePaymentRequest);
    }

    public void topupYLPD (String prodName, String prodDesc, float amount, String extInfo) {
//            ApiCallback.setWFTWapPayCallback(mWFTWapPayCallback);
//            WFTOrderInfoProcess wftProcess = new WFTOrderInfoProcess();
            mGatePaymentProcess.setGoodsName(prodName);
            mGatePaymentProcess.setGoodsPrice(String.format("%.2f", amount));
            mGatePaymentProcess.setGoodsDesc(prodDesc);
            mGatePaymentProcess.setExtend(extInfo);
            mGatePaymentProcess.setPayType("0");
            mGatePaymentProcess.post(mgateOrderPaymentHandler);
//            showDialog("正在给微信下单..");
       // mGatePaymentProcess.post(mgateOrderPaymentHandler);

    }

    public void doMGatePayment(String couponID) {
        mGatePaymentProcess.setGoodsName(ApiCallback.order().getProductName());
        mGatePaymentProcess.setGoodsPrice(ApiCallback.order().getGoodsPriceYuan());
        mGatePaymentProcess.setGoodsDesc(ApiCallback.order().getProductDesc());
        mGatePaymentProcess.setServerName(ApiCallback.order().getServerName());
        mGatePaymentProcess.setRoleName(ApiCallback.order().getRoleName());
        mGatePaymentProcess.setRoleId(ApiCallback.order().getRoleId());
        mGatePaymentProcess.setServerId(ApiCallback.order().getGameServerId());
        mGatePaymentProcess.setExtra_param(ApiCallback.order().getExtra_param());
        mGatePaymentProcess.setRoleLevel(ApiCallback.order().getRoleLevel());
        mGatePaymentProcess.setGoodsReserve(ApiCallback.order().getGoodsReserve());
        mGatePaymentProcess.setPayType("1");
        mGatePaymentProcess.setCouponId(couponID);
        mGatePaymentProcess.setExtend(ApiCallback.order().getExtendInfo());
        mGatePaymentProcess.post(mgateOrderPaymentHandler);
    }

    private final Handler mgateOrderPaymentHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FlagControl.BUTTON_CLICKABLE = true;
            switch (msg.what) {
                case Constant.ZFB_PAY_VALIDATE_SUCCESS:// 请求支付订单成功
                    queryMGatePaymentResult(msg.obj);
                    break;
                case Constant.ZFB_PAY_VALIDATE_FAIL:// 请求支付订单失败
                    ToastUtil.show(mainActivity, AppConstants.STR_244e5dd1ea2819fccabf8a5527dd1aca + msg.obj.toString());
                    break;
                case Constant.SDK_PAY_FLAG:// 支付结果
                    handlerMGateSDKResult(msg.obj);
                    break;
                case Constant.ZFB_WAPPAY_ORDERINFO_SUCCESS:
                    WXOrderInfo zfbwapPayOrderInfo = (WXOrderInfo) msg.obj;
                    Intent zfbWapPayintent = new Intent(mainActivity, MCHWapPayActivity.class);
                    zfbWapPayintent.putExtra("WapPayOrderInfo", zfbwapPayOrderInfo);
                    mainActivity.startActivity(zfbWapPayintent);
                    break;
                case Constant.ZFB_WAPPAY_ORDERINFO_FAIL:
                    ToastUtil.show(mainActivity, AppConstants.STR_244e5dd1ea2819fccabf8a5527dd1aca + msg.obj);
                    break;
                default:
                    break;
            }
        }

        private void handlerMGateSDKResult(Object obj) {
        }

        private void queryMGatePaymentResult(Object obj) {
            
        }
    };
}
