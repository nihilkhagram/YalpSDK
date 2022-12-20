package com.cointizen.paysdk.callback;

public interface PlatformLoginCallback {
	public void platformLogin(String userName, String password, boolean isSavePwd);
}
