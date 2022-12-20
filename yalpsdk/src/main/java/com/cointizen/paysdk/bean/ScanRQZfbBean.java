package com.cointizen.paysdk.bean;

/**
 * 备注：
 * 日期：2021/1/5
 */
public class ScanRQZfbBean {
    private String paytype;
    private String orderno;
    private String qrcode_url;

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }
}
