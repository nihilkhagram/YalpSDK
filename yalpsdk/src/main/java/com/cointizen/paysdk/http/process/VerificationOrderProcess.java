package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.BaseProcess;
import com.cointizen.paysdk.http.request.VerificationOrderRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class VerificationOrderProcess extends BaseProcess {

	private static final String TAG = "VerificationOrderProcess";
	private String tradeNo;

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
	@Override
	public void post(Handler handler){
//		StringBuilder param = new StringBuilder();
//		
//		param.append("{\"tradeno\":\"").append(tradeNo)
//		.append("\",\"md5key\":\"")
//		.append(PaykeyUtil.stringToMD5(tradeNo + OrderInfoUtils.MCH_MD5KEY))
//		.append("\"}");
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}
		
		if (null != handler && null != params) {
			VerificationOrderRequest verification = new VerificationOrderRequest(handler);
			verification.post(MCHConstant.getInstance().getPayResultVerificationUrl(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

	@Override
	public String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("out_trade_no", tradeNo);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		MCLog.e(TAG, "fun#post postSign:" + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}

}
