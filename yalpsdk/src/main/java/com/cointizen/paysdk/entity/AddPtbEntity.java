package com.cointizen.paysdk.entity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.text.TextUtils;

import com.cointizen.paysdk.utils.MCLog;

public class AddPtbEntity {

	private static final String TAG = "AddPtbEntity";

	private static final String addPtbtime = "yy-MM-dd\nHH:mm";

	private String blannce;

	private String addPtbTime;

	/**
	 * 0:平台币,1:支付宝,2:微信(扫码)3微信app 4 威富通 5聚宝云
	 */
	private String buyPTBType;

	public String getBuyPTBType() {
		return buyPTBType;
	}

	public void setBuyPTBType(String buyPTBType) {
		this.buyPTBType = buyPTBType;
	}

	public String getBlannce() {
		return blannce;
	}

	public void setBlannce(String blannce) {
		this.blannce = blannce;
	}

	public String getAddPtbTime() {
		return addPtbTime;
	}

	public void setAddPtbTime(String addPtbTime) {
		this.addPtbTime = addPtbTime;
	}

	public String getAddPtbTimeStr(){
		MCLog.w(TAG, "addPtbTime = " + addPtbTime);
		if(TextUtils.isEmpty(addPtbTime)){
			return "";
		}

		long cTime;
		try {
			cTime = Long.parseLong(addPtbTime);
		} catch (NumberFormatException e) {
			MCLog.e(TAG, "fun#getAddPtbTimeStr NumberFormatException:" + e);
			return "";
		}
		return new SimpleDateFormat(addPtbtime, Locale.CHINA).format(cTime * 1000);
	}

	@Override
	public String toString() {
		return "AddPtbEntity [blannce=" + blannce + ", addPtbTime=" + addPtbTime + "]";
	}




}
