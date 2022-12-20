package com.cointizen.paysdk.view.util;

public class TimeFactory {
	/**
	 *  倒计时工厂
	 * @param type 倒计类型
	 * @return
	 */
	public static TimeUtil creator(int type) {
		if (0 == type) {//忘记密码
			return FrogetPwdTime.getTimeUtil();
		} else if (1 == type) {//注册
			return RegisterTime.getTimeUtil();
		} else if (2 == type) {//绑定手机
			return BindPhoneTime.getTimeUtil();
		} else if (3 == type) {//解绑手机
			return UnBindPhoneTime.getTimeUtil();
		} else if (4 == type) {//绑定新手机
			return BindNewPhoneTime.getTimeUtil();
		} else if (5 == type) {//绑定新手机
			return VerifyPhoneTime.getTimeUtil();
		}  else {
			return TimeUtil.getTimeUtil();
		}
	}
}
