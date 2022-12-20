package com.cointizen.plugin.qg.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.cointizen.util.StoreSettingsUtil;
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
 * 强更
 */
public class UpdateProcess {

	private static final String TAG = "UpdateProcess";
	private final Context context;
	private String url = MCHConstant.getInstance().getSdkBaseUrl()+"User/force_update";

	public UpdateProcess(Context context) {
		this.context = context;
	}

	public void post(Handler handler) {
		if (null == handler) {
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("sdk_version", "1");//表示android发送的请求，固定值0
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("launch_id", SdkDomain.getInstance().getLaunchId());
		map.put("position", SdkDomain.getInstance().getPosition());
		map.put("promote_id", StoreSettingsUtil.getStoreId());
		map.put("game_appid", SdkDomain.getInstance().getGameAppId());
		String param = RequestParamUtil.getRequestParamString(map, url);
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
			UpdateRequest loginRequest = new UpdateRequest(handler);
			loginRequest.post(url, params);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
