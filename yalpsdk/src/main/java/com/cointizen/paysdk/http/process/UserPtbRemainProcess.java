package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.UserPtbRemainRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class UserPtbRemainProcess {

	private static final String TAG = "UserPtbRemainProcess";

	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", UserLoginSession.getInstance().getAccount());
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());

		return RequestParamUtil.getRequestParamString(map);
	}

	public void post(Handler handler, boolean PTBtype) {

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#noticeGameServerPayResult UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			new UserPtbRemainRequest(handler).post(MCHConstant.getInstance().getQueryUserPTBUrl(), params, PTBtype);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

}
