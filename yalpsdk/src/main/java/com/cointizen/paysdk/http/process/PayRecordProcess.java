package com.cointizen.paysdk.http.process;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.PayReCordRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取游戏消费记录
 */
public class PayRecordProcess {

	private static final String TAG = "PayRecordProcess";
	private final Context context;
	private String limit;

	public PayRecordProcess(Context context) {
		this.context = context;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}


	public void post(Handler handler) {
		if (null == handler) {
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("account", UserLoginSession.getInstance().getChannelAndGame().getAccount());
		map.put("p", limit);
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
			PayReCordRequest loginRequest = new PayReCordRequest(handler);
			loginRequest.post(MCHConstant.getInstance().getPayRecordUrl(), params,context);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
