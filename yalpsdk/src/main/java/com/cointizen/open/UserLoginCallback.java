package com.cointizen.open;
/**
 * 平台账号登录回调接口
 */
public interface UserLoginCallback {
	void onFinish(final GPUserResult gpUserResult);
}
