package com.cointizen.paysdk.view.util;

public class BindNewPhoneTime extends TimeUtil {

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return super.getType();
	}

	private static BindNewPhoneTime bindTiem;

	public static TimeUtil getTiemutil() {

		if (null == bindTiem) {
			bindTiem = new BindNewPhoneTime();
		}
		return bindTiem;
	}
}
