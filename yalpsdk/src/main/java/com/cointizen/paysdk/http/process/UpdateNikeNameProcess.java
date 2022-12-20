package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.UpdateNikeNameRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class UpdateNikeNameProcess {

	private static final String TAG = "UpdateNikeNameProcess";

	private String nikeName;

	private String code;

	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("nickname", nikeName);
		map.put("code", code);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		
		return RequestParamUtil.getRequestParamString(map);
	}
	
	public void post(Handler handler){
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}
		
//		String url = MCHConstant.getInstance().getUserModifyNickname() + "/account/" + account + "/nickname/" + nikeName;
		
		if (null != handler && null != params) {
			UpdateNikeNameRequest updateNickName = new UpdateNikeNameRequest(handler);
			updateNickName.post(MCHConstant.getInstance().getUpdateUserInfoUrl(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
	
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
