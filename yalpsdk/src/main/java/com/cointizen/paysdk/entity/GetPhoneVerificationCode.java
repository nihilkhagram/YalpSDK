package com.cointizen.paysdk.entity;

public class GetPhoneVerificationCode {

	/**
	 * 手机号码
	 */
	private String phoneNumber;
	/**
	 * 手机验证码
	 */
	private String code;
	/**
	 * 修改成功返回字符串
	 */
	private String returnResult;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(String returnResult) {
		this.returnResult = returnResult;
	}

}
