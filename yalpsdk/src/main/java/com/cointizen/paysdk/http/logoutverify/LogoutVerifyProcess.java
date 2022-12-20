package com.cointizen.paysdk.http.logoutverify;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.utils.MCLog;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LogoutVerifyProcess {

    private static final String TAG = "SwitchInfoProcess";

    public String type = "";//1 注销账号  0 取消注销

    private String getParamStr(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        MCLog.w(TAG, "fun#ptb_pay params:" + map.toString());

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

        LogoutVerifyRequest request = new LogoutVerifyRequest(handler);
        request.post(MCHConstant.getInstance().getLogoutVerifyUrl(), params);
    }

}
