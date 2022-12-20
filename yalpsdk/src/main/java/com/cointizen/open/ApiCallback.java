package com.cointizen.open;

import com.cointizen.paysdk.bean.MCPayModel;
import com.cointizen.paysdk.callback.MGatePayCallback;
import com.cointizen.paysdk.callback.ScanPayCallback;
import com.cointizen.paysdk.callback.WFTWapPayCallback;
import com.cointizen.paysdk.callback.StripePayCallback;

public class ApiCallback {

    public static WFTWapPayCallback mWFTWapPayCallback;

    public static StripePayCallback mStripePayCallback;

    public static ScanPayCallback mScanPayCallback;
    private static MGatePayCallback mGatePayCallback;

    public static IGPExitObsv getExitObsv() {
        return YalpGamesSdk.getYalpGamesSdk().getExitObsv();
    }

    public static OrderInfo order() {
        return MCPayModel.Instance().order();
    }

    public static UserLoginCallback getLoginCallback() {
        return YalpGamesSdk.getYalpGamesSdk().getLoginCallback();
    }

    /**
     * 微信wap支付结果回调
     *
     * @param wftWapPayCallback
     */
    public static void setWFTWapPayCallback(WFTWapPayCallback wftWapPayCallback) {
        if (null != wftWapPayCallback) {
            mWFTWapPayCallback = wftWapPayCallback;
        }
    }

    /**
     * 支付宝wap支付结果回调
     *
     * @param stripePayCallback
     */
    public static void setZFBWapPayCallback(StripePayCallback stripePayCallback) {
        if (null != stripePayCallback) {
            mStripePayCallback = stripePayCallback;
        }
    }

    /**
     * 扫码支付结果回调
     * @param scanPayCallback
     */
    public static void setScanPayCallback(ScanPayCallback scanPayCallback) {
        if (null != scanPayCallback) {
            mScanPayCallback = scanPayCallback;
        }
    }

    public static void setMGatePayCallback(MGatePayCallback mgatePayCallback) {
        if(null != mGatePayCallback) {
            mGatePayCallback = mgatePayCallback;
        }
    }
}
