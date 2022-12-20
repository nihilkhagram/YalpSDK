package com.cointizen.paysdk.view.util;

public class FrogetPwdTime extends TimeUtil {

	@Override
	public int getType() {
		return 0;
	}

	/**
	 * 忘记密码倒计时
	 */
	private static FrogetPwdTime frogetTime;
	
	public static TimeUtil getTimeUtil(){
		if(null == frogetTime){
			frogetTime = new FrogetPwdTime();
		}
		return frogetTime;
	}
	
}
