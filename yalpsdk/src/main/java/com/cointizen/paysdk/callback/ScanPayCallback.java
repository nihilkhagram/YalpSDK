package com.cointizen.paysdk.callback;

/**
 * 描述：扫码支付回调
 * 时间: 2020-01-05 15:50
 */

public interface ScanPayCallback {
    void onResult(String code);
}
