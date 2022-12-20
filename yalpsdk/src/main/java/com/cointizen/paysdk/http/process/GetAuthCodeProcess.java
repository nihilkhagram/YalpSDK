package com.cointizen.paysdk.http.process;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.GetAuthCodeRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取校验码
 */
public class GetAuthCodeProcess {

	private static final String TAG = "GetAuthCodeProcess";
	private final Context context;

	public GetAuthCodeProcess(Context context) {
		this.context = context;
	}


	public void post(Handler handler) {
		if (null == handler) {
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		String param = RequestParamUtil.getRequestParamString(map);
		if (TextUtils.isEmpty(param)) {
			MCLog.e(TAG, "fun#post param is null");
			return;
		}
		RequestParams params = new RequestParams();
		MCLog.w(TAG, "fun#post postSign:" + map.toString());
		try {
			params.setBodyEntity(new StringEntity(param.toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != params) {
			GetAuthCodeRequest request = new GetAuthCodeRequest(handler);
			request.post(MCHConstant.getInstance().getGetAuthCode(), params,context);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
