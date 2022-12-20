package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.CouponUseableRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GetAvailableCouponProcess {

	private final String TAG = "GetAvailableCouponProcess";
	private String price = "";
	private String isBind = "";//1=绑币

	public void setPrice(String price) {
		this.price = price;
	}

	public void setBind(String isBind) {
		this.isBind = isBind;
	}

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("pay_amount", price);
		map.put("is_bind_available", isBind);
		return RequestParamUtil.getRequestParamString(map, MCHConstant.getInstance().getCanUseCoupon());
	}

	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr().toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#ptb_pay UnsupportedEncodingException:" + e);
		}
		if (null != handler) {
			new CouponUseableRequest(handler).post(MCHConstant.getInstance().getCanUseCoupon(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

}
