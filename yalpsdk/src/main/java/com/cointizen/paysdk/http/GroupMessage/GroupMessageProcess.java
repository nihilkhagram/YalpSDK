package com.cointizen.paysdk.http.GroupMessage;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;
/**
 * List of messages for player and manager
 * @author Administrator
 *
 */
public class GroupMessageProcess {
    public String user_id;	//用户id
    public String content; //留言内容
	public String type;    //1代表留言
	private static final String TAG = "GroupMessageProcess";
	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		if("1".equals(type)){
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("content", content);
		map.put("type",type);
		}
		map.put("game_id", SdkDomain.getInstance().getGameId());
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}
	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}
		if (null != params) {
			GroupMessageRequest request = new GroupMessageRequest(handler);
			request.post(MCHConstant.getInstance().getProblemFeedback(), params);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
