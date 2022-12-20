package com.cointizen.paysdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.http.request.HelperCenterRequest;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 帮助中心
 */
public class HelperProcess {

	private static final String TAG = "HelperProcess";


	public void post(Handler handler) {
		if (null == handler) {
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		String param = RequestParamUtil.getRequestParamString(map);
		if (TextUtils.isEmpty(param)) {
			MCLog.e(TAG, "fun#post param is null");
			return;
		}
		RequestParams params = new RequestParams();
		MCLog.w(TAG, MCHConstant.getInstance().getHelper() + "\n" + map.toString());
		try {
			params.setBodyEntity(new StringEntity(param.toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != params) {
			HelperCenterRequest loginRequest = new HelperCenterRequest(handler);
			loginRequest.post(MCHConstant.getInstance().getHelper(), params);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
