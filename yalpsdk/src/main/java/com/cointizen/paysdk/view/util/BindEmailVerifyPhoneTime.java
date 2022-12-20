package com.cointizen.paysdk.view.util;

public class BindEmailVerifyPhoneTime extends TimeUtil2 {

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return super.getType();
	}

	private static BindEmailVerifyPhoneTime bindTiem;

	public static TimeUtil2 getTiemutil() {

		if (null == bindTiem) {
			bindTiem = new BindEmailVerifyPhoneTime();
		}
		return bindTiem;
	}
}
