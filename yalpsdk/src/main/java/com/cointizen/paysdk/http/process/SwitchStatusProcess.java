package com.cointizen.paysdk.http.process;
import android.os.Handler;
import android.text.TextUtils;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.SwitchStatusRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;
import org.apache.http.entity.StringEntity;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取功能开关
 */
public class SwitchStatusProcess {
	private static final String TAG = "SwitchStatusProcess";

	public void post(Handler handler) {
		if (null == handler) {
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", SdkDomain.getInstance().getGameId());
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
			SwitchStatusRequest request = new SwitchStatusRequest(handler);
			request.post(MCHConstant.getInstance().getSwitchStatus(), params);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
