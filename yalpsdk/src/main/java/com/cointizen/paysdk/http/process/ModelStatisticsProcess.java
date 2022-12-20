package com.cointizen.paysdk.http.process;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.ModelStatisticsRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备统计
 */
public class ModelStatisticsProcess {

	private static final String TAG = "ModelStatisticsProcess";
	private final Context context;

	private String model;
	private String unique_code;
	private String mark;

	public ModelStatisticsProcess(Context context) {
		this.context = context;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setUnique_code(String unique_code) {
		this.unique_code = unique_code;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public void post(Handler handler) {
		if (null == handler) {
			MCLog.e(TAG, "fun#post handler is null");
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("model", model);
		map.put("unique_code", unique_code);
		map.put("mark", mark);
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
			ModelStatisticsRequest loginRequest = new ModelStatisticsRequest(handler);
			loginRequest.post(MCHConstant.getInstance().getModelStatistics(), params);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}

}
