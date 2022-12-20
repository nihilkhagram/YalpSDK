package com.cointizen.paysdk.http.arawd;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.utils.MCLog;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AwardProcess {

    private static final String TAG = "SwitchInfoProcess";

    public boolean isRegister;

    private String getParamStr(){
        Map<String, String> map = new HashMap<String, String>();
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

        AwardRequest request = new AwardRequest(handler);
        request.post(MCHConstant.getInstance().getAwardListUrl(), params, isRegister);
    }

}
