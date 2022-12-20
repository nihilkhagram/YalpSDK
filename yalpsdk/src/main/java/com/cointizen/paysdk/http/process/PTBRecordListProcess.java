package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.PTBRecordListRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class PTBRecordListProcess {

	private static final String TAG = "AddPTBRecordProcess";
	
//	private String account;

//	public void get(Handler handler){
//		MCLog.e(TAG, "fun#get account:" + account);
//		StringBuilder url = new StringBuilder();
//		url.append(MCHConstant.getInstance().getAddPTBRecordURL())
//		.append("/account/").append(account);
//		
//		if (null != handler) {
//			AddPTBRecordRequest addPTBRecordRequest = new AddPTBRecordRequest(handler);
//			addPTBRecordRequest.post(url.toString());
//			return;
//		} else {
//			MCLog.e(TAG, "fun#post handler is null or url is null");
//		}
//	}

	private int index;

	public void setIndex(int index) {
		this.index = index;
	}

	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("account", UserLoginSession.getInstance().getChannelAndGame().getAccount());
		map.put("p", index + "");
		map.put("row", "30");
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}
	
	public void post(Handler handler){
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr().toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#ptb_pay UnsupportedEncodingException:" + e);
		}
		
		if (null != handler) {
			new PTBRecordListRequest(handler).post(MCHConstant.getInstance().getAddPTBRecordURL(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
