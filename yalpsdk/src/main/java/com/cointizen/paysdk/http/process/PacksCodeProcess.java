package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.BaseProcess;
import com.cointizen.paysdk.http.request.PacksCodeRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;
/**
 * 领取礼包码
 * 
 * @author Administrator
 *
 */
public class PacksCodeProcess extends BaseProcess {

	private static final String TAG = "PacksCodeProcess";

	private String giftId;
	private String packName;

	@Override
	public String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("gift_id", giftId);
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("account", UserLoginSession.getInstance().getAccount());
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());

		return RequestParamUtil.getRequestParamString(map);
	}

	@Override
	public void post(Handler handler) {
		RequestParams params = new RequestParams();

		try {
			params.setBodyEntity(new StringEntity(getParamStr()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#ptb_pay UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			PacksCodeRequest packcodeRequest = new PacksCodeRequest(handler);
			packcodeRequest.post(MCHConstant.getInstance().getPacksCodeUrl(), params, packName);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}

	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

}
