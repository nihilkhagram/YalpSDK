package com.cointizen.paysdk.entity;

public class UserRegister {

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String password;

	private String status;

	private String registerResult;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegisterResult() {
		return registerResult;
	}

	public void setRegisterResult(String registerResult) {
		this.registerResult = registerResult;
	}

}
