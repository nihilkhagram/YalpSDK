package com.cointizen.paysdk.http.comque;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;
/**
 * List of common problems and solutions for SDK
 * @author Administrator
 *
 */
public class CommonQuesstionProcess {

	private static final String TAG = "CommonQuesstionProcess";
	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", SdkDomain.getInstance().getGameId());
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}
	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr().toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			CommonQuesstionRequest request = new CommonQuesstionRequest(handler);
			request.post(MCHConstant.getInstance().getCommonProblem(),
					params);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
