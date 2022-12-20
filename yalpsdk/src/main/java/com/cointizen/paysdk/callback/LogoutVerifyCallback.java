package com.cointizen.paysdk.callback;

/**
 * 描述：支付宝wep支付回调
 * 时间: 2017-11-15 15:50
 */

public interface LogoutVerifyCallback {
    void onResult(String type, String phoneOrEmail);
}
