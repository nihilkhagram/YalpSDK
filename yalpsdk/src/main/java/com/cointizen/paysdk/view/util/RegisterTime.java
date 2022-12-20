package com.cointizen.paysdk.view.util;

public class RegisterTime extends TimeUtil {

	@Override
	public int getType() {
		return 1;
	}

	/**
	 * 注册倒计时
	 */
	private static RegisterTime registerTime;

	public static TimeUtil getTimeUtil() {
		if (null == registerTime) {
			registerTime = new RegisterTime();
		}
		return registerTime;
	}

}
