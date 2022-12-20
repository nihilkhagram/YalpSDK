package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.UpHeadRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传头像
 */
public class UpHeadProcess {

	private static final String TAG = "UpHeadProcess";
	private File file;


	public void setFile(File file) {
		this.file = file;
	}

	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("account", UserLoginSession.getInstance().getChannelAndGame().getAccount());
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}
	
	public void post(Handler handler){
		RequestParams params = new RequestParams();
		try {
			MultipartEntity multipartEntity = new MultipartEntity();
			multipartEntity.addPart("fileimg", new FileBody(file, "image/*"));
			multipartEntity.addPart("key",new StringBody(getParamStr().toString(), Charset.forName("UTF-8")));
			params.setBodyEntity(multipartEntity);
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#ptb_pay UnsupportedEncodingException:" + e);
		}
		
		if (null != handler) {
			new UpHeadRequest(handler).post(MCHConstant.getInstance().getUpHread(), params);
		}else{
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
