package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.cointizen.util.StoreSettingsUtil;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.UserDiscountRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhujinzhujin
 * on 2017/1/12.
 */

public class UserDiscountProcess {

    private static final String TAG = "UserDiscountProcess";

    private String getParamStr(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("promote_id", StoreSettingsUtil.getStoreId());
        map.put("user_id", UserLoginSession.getInstance().getUserId());
        map.put("game_id", SdkDomain.getInstance().getGameId());
        MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());

        return RequestParamUtil.getRequestParamString(map);
    }



    public void post(Handler handler) {
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(getParamStr().toString()));
        } catch (UnsupportedEncodingException e) {
            params = null;
            MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
        }

        if (null != handler && null != params) {
            UserDiscountRequest request = new UserDiscountRequest(handler);
            request.post(MCHConstant.getInstance().getUserDiscountUrl(), params);
        } else {
            MCLog.e(TAG, "fun#post handler is null or url is null");
        }
    }
}
