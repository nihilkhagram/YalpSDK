package com.cointizen.paysdk.http.process;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.cointizen.open.YalpGamesSdk;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.http.request.UploadTalkingCodeRequest;
import com.cointizen.paysdk.utils.MCLog;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 上下线 上报talking_code
 */
public class UploadTalkingCodeProcess {

	private static final String TAG = "ShareProcess";
	private Context context = YalpGamesSdk.getMainActivity();
	private String talking_code;
	private int login_type;  //0:下线 1：上线

	public void setTalking_code(String talking_code) {
		this.talking_code = talking_code;
	}

	public void setLogin_type(int login_type) {
		this.login_type = login_type;
	}

	public void post() {
		if (TextUtils.isEmpty(RequestParamUtil.IMEI)){
			String AndroidID = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			if (!TextUtils.isEmpty(AndroidID)){
				RequestParamUtil.IMEI = AndroidID;
				sendMsg();
			}
		}else {
			sendMsg();
		}
	}


	private void sendMsg(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("talking_code",talking_code);
		map.put("login_type",login_type + "");
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
			UploadTalkingCodeRequest request = new UploadTalkingCodeRequest();
			request.post(MCHConstant.getInstance().getUploadTalkingCode(), params);
		} else {
			MCLog.e(TAG, "fun#post RequestParams is null");
		}
	}
}
