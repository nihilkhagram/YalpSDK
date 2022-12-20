package com.cointizen.paysdk.view.util;

public class VerifyPhoneTime extends TimeUtil {

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return super.getType();
	}

	private static VerifyPhoneTime bindTiem;

	public static TimeUtil getTiemutil() {

		if (null == bindTiem) {
			bindTiem = new VerifyPhoneTime();
		}
		return bindTiem;
	}
}
