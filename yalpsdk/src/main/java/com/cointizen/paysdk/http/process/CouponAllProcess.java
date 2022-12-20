package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.CouponAllRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CouponAllProcess {

	private static final String TAG = "CouponAllProcess";
	private int type = 1;  // 1可领取  2已领取 3已失效
	private String isbind;

	public void setType(int type) {
		this.type = type;
	}

	public void setIsbind(String isbind) {
		this.isbind = isbind;
	}

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("type",String.valueOf(type));
		map.put("is_bind_available", isbind);
//		MCLog.w(TAG, "fun#ptb_pay params:" + map.toString());
		return RequestParamUtil.getRequestParamString(map, MCHConstant.getInstance().getAllCoupon());
	}

	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#ptb_pay UnsupportedEncodingException:" + e);
		}
		if (null != handler) {
			CouponAllRequest request = new CouponAllRequest(handler);
			request.setType(type);
			request.post(MCHConstant.getInstance().getAllCoupon(), params);
		}else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

}
