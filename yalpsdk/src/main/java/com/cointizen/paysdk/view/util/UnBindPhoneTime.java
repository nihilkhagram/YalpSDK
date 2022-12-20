package com.cointizen.paysdk.view.util;

public class UnBindPhoneTime extends TimeUtil {

	@Override
	public int getType() {
		return super.getType();
	}

	/**
	 * 解除绑定手机
	 */
	private static UnBindPhoneTime unBindTiem;

	public static TimeUtil getTiemutil() {
		if (null == unBindTiem) {
			unBindTiem = new UnBindPhoneTime();
		}
		return unBindTiem;
	}
}
