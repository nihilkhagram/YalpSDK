package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.CouponReceiveRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CouponReceiveProcess {

	private static final String TAG = "CouponReceiveProcess";
	private String coupon_id;

	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("coupon_id", coupon_id);
		MCLog.w(TAG, "fun#ptb_pay params:" + map.toString());
		return RequestParamUtil.getRequestParamString(map, MCHConstant.getInstance().getReceiveCoupon());
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
			new CouponReceiveRequest(handler).post(MCHConstant.getInstance().getReceiveCoupon(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

}
