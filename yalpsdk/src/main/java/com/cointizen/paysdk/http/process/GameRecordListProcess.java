package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.GameRecordListRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;
import com.cointizen.paysdk.http.HttpConstants;

/**
 * 游戏充值记录
 * 
 * @author Administrator
 *
 */
public class GameRecordListProcess {

	private static final String TAG = "GameRecordListProcess";

	// private String account;

	// public void get(Handler handler){
	// MCLog.e(TAG, "fun#get account:" + account);
	// StringBuilder url = new StringBuilder();
	// url.append(MCHConstant.getInstance().getAddPTBRecordURL())
	// .append("/account/").append(account);
	//
	// if (null != handler) {
	// AddPTBRecordRequest addPTBRecordRequest = new
	// AddPTBRecordRequest(handler);
	// addPTBRecordRequest.post(url.toString());
	// return;
	// } else {
	// MCLog.e(TAG, "fun#post handler is null or url is null");
	// }
	// }

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		MCLog.e(TAG, HttpConstants.Log_okmaiUFFph + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}

	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr().toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, HttpConstants.Log_IUyeEbbnRn + e);
		} catch (Exception e) {

		}
		if (null != handler) {
			new GameRecordListRequest(handler).post(MCHConstant.getInstance()
					.getGameRecodeUrl(), params);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
