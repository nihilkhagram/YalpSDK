package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.BaseProcess;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.http.request.ScanPayResultRequest;
import com.cointizen.paysdk.utils.MCLog;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ScanPayResultProcess extends BaseProcess {

	private static final String TAG = "ScanPayResultProcess";

	private String out_trade_no = "";

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	@Override
	public String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("out_trade_no", out_trade_no);
		MCLog.w(TAG, "fun#post postSign:" + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}

	@Override
	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		
		try {
			params.setBodyEntity(new StringEntity(getParamStr()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			ScanPayResultRequest request = new ScanPayResultRequest(handler);
			request.post(MCHConstant.getInstance().getGetScanResult(), params);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
		
	}
}
