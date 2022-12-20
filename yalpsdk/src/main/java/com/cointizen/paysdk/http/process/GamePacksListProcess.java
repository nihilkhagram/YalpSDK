package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.GamePacksListRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class GamePacksListProcess {

	private static final String TAG = "GamePacksListProcess";
	private String is_vip = "0"; //请求特权礼包列表 0否 1是

	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("is_vip" , is_vip);
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
			new GamePacksListRequest(handler).post(MCHConstant.getInstance().getGamePacksList(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

}
