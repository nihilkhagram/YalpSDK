package com.cointizen.paysdk.bean;

public enum PayMethod {

    STRIPE_WECHAT(1), STRIPE_USD(2), YLPD(3), BINDING_DOLLAR(4), MGATE(5), OTT_UPAY(6);

    int payMethod;

    PayMethod(int i) {
        payMethod = i;
    }
}
