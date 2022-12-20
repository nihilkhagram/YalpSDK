package com.cointizen.paysdk.http.thirdloginparams;

import android.os.Handler;

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
 * http://demo.vlcms.com/sdk.php?s=user/oauth_param
 *POST
 *安卓SDK客户端向SDK服务器请求获得客户端第三方登录需要的参数
 *
 */
public class ThirdLoginParamsProcess {

    private static final String TAG = "ThirdLoginParamsProcess";

	/**
	 *  登录类型*/
	public String login_type;


	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("promote_id", StoreSettingsUtil.getStoreId());
		map.put("login_type",login_type);
		MCLog.i(TAG, "fun#getParamStr params:" + map.toString());
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

			ThirdLoginParamsRequest request = new ThirdLoginParamsRequest(handler);
			MCLog.e(TAG,MCHConstant.getInstance().getThirdloginUrl());
			request.post(MCHConstant.getInstance().getThirdloginUrl(), params, login_type);


		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

}
