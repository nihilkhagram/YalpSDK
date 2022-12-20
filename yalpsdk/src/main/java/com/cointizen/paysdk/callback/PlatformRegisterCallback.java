package com.cointizen.paysdk.callback;

import android.view.View;

public interface PlatformRegisterCallback {

	void platformRegister(String account, String password, String repassword, String phoneCode, int registerType, boolean isReadAgreement);

	public void getPhoneValidateMessage(View v, String userName, RefreshVerifyCode refreshCode);
}
