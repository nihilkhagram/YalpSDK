package com.cointizen.plugin.guess.http.process;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.util.StoreSettingsUtil;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.plugin.guess.http.request.GuessYouLikeRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 猜你喜欢
 */
public class GuessYouLikeProcess {

	private static final String TAG = "GuessYouLikeProcess";

	public void post(Handler handler, Context mcUserCenterActivity) {
		if (null == handler) {
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("promote_id", StoreSettingsUtil.getStoreId());
		map.put("p", "1");
		map.put("row", "30");
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
			GuessYouLikeRequest loginRequest = new GuessYouLikeRequest(handler);
			loginRequest.post(MCHConstant.getInstance().getGuessYouLike(), params, mcUserCenterActivity);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
