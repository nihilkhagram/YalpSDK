package com.cointizen.paysdk.entity;

public class UserBindPGetPVNumebr {

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 新密码
	 */
	private String paeeword;
	/**
	 * 手机号码
	 */
	private String phoneNumber;
	/**
	 * 随机串
	 */
	private String senRanDom;
	/**
	 * 请求标示
	 */
	private String senType;
	/**
	 * 修改成功返回字符串
	 */
	private String changeResult;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPaeeword() {
		return paeeword;
	}

	public void setPaeeword(String paeeword) {
		this.paeeword = paeeword;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSenRanDom() {
		return senRanDom;
	}

	public void setSenRanDom(String senRanDom) {
		this.senRanDom = senRanDom;
	}

	public String getSenType() {
		return senType;
	}

	public void setSenType(String senType) {
		this.senType = senType;
	}

	public String getChangeResult() {
		return changeResult;
	}

	public void setChangeResult(String changeResult) {
		this.changeResult = changeResult;
	}
}
