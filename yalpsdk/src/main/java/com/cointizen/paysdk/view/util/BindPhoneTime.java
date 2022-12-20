package com.cointizen.paysdk.view.util;

public class BindPhoneTime extends TimeUtil {

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return super.getType();
	}

	private static BindPhoneTime bindTiem;

	public static TimeUtil getTiemutil() {

		if (null == bindTiem) {
			bindTiem = new BindPhoneTime();
		}
		return bindTiem;
	}
}
