package com.cointizen.paysdk.entity;

public class UserPTBInfo {

	/**
	 * 平台币类型 0：平台币   1:绑定平台币
	 */
	private float bindptbMoney;
	
	/**
	 * 平台币金额
	 */
	private float ptbMoney;

	public float getBindptbMoney() {
		return bindptbMoney;
	}

	public void setBindptbMoney(float bindptbMoney) {
		this.bindptbMoney = bindptbMoney;
	}

	public float getPtbMoney() {
		return ptbMoney;
	}

	public void setPtbMoney(float ptbMoney) {
		this.ptbMoney = ptbMoney;
	}
	
	
	
	
}
