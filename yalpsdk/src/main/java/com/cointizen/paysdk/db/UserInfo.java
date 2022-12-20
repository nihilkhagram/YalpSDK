package com.cointizen.paysdk.db;

public class UserInfo {
	public int ID = -1;
	public String account;//用户名
	public String password;//密码丶记住密码
	@Override
	public String toString() {
		String result = "";
		result += DbConstants.S_yNcVswlvSx + this.ID + DbConstants.S_kwDBEEiatr;
		result += DbConstants.S_MmkqouKQJD + this.account + DbConstants.S_kwDBEEiatr;
		result += DbConstants.S_ZhMTToVGSU + this.password + DbConstants.S_mUfWsxicRF;
		return result;
	}
}
