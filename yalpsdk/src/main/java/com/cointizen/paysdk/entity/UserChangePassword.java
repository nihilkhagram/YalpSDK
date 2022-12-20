package com.cointizen.paysdk.entity;

public class UserChangePassword {

	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 新密码
	 */
	private String paeeword;
	
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
	public String getChangeResult() {
		return changeResult;
	}
	public void setChangeResult(String changeResult) {
		this.changeResult =changeResult;
	}
}
