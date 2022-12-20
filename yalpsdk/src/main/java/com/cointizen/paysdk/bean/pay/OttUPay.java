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
import com.cointizen.paysdk.http.process.OttUPayPaymentProcess;
import com.cointizen.paysdk.payment.OttUpayPaymentRequest;
import com.cointizen.paysdk.utils.ToastUtil;

public class OttUPay {

    private Activity mainActivity;
    OttUpayPaymentRequest ottUPayPaymentRequest;
    OttUPayPaymentProcess ottUPayPaymentProcess;

    public OttUPay(Activity context, OttUpayPaymentRequest ottUPayPaymentRequest) {
        if(null != mainActivity) {
            this.mainActivity = context;
        }
        this.ottUPayPaymentRequest = ottUPayPaymentRequest;
        this.ottUPayPaymentProcess = new OttUPayPaymentProcess(ottUPayPaymentRequest);
    }

    public void topupYLPD (String prodName, String prodDesc, float amount, String extInfo) {
            ottUPayPaymentProcess.setGoodsName(prodName);
            ottUPayPaymentProcess.setGoodsPrice(String.format("%.2f", amount));
            ottUPayPaymentProcess.setGoodsDesc(prodDesc);
            ottUPayPaymentProcess.setExtend(extInfo);
            ottUPayPaymentProcess.setPayType("0");
            ottUPayPaymentProcess.post(ottUPayOrderPaymentHandler);

    }

    public void doOttUPayPayment(String couponID) {
        ottUPayPaymentProcess.setGoodsName(ApiCallback.order().getProductName());
        ottUPayPaymentProcess.setGoodsPrice(ApiCallback.order().getGoodsPriceYuan());
        ottUPayPaymentProcess.setGoodsDesc(ApiCallback.order().getProductDesc());
        ottUPayPaymentProcess.setServerName(ApiCallback.order().getServerName());
        ottUPayPaymentProcess.setRoleName(ApiCallback.order().getRoleName());
        ottUPayPaymentProcess.setRoleId(ApiCallback.order().getRoleId());
        ottUPayPaymentProcess.setServerId(ApiCallback.order().getGameServerId());
        ottUPayPaymentProcess.setExtra_param(ApiCallback.order().getExtra_param());
        ottUPayPaymentProcess.setRoleLevel(ApiCallback.order().getRoleLevel());
        ottUPayPaymentProcess.setGoodsReserve(ApiCallback.order().getGoodsReserve());
        ottUPayPaymentProcess.setPayType("1");
        ottUPayPaymentProcess.setCouponId(couponID);
        ottUPayPaymentProcess.setExtend(ApiCallback.order().getExtendInfo());
        ottUPayPaymentProcess.post(ottUPayOrderPaymentHandler);
    }

    private final Handler ottUPayOrderPaymentHandler = new Handler(Looper.getMainLooper()){
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
