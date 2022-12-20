package com.cointizen.paysdk.entity;

public class StripeVerificationResult {

	private String sign;
	private String zfbMd5Key;
	private String orderInfo;
	private String code;
	private String msg;
	private String orderNumber;

	public StripeVerificationResult(){
		sign = "";
		zfbMd5Key = "";
		code = "";
		msg = "";
	}

	public String getZfbMd5Key() {
		return zfbMd5Key;
	}

	public void setZfbMd5Key(String zfbMd5Key) {
		this.zfbMd5Key = zfbMd5Key;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "StripeVerificationResult [zfbMd5Key=" + zfbMd5Key + ", code=" + code + ", msg=" + msg
				+ "]";
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	
}
