package com.cointizen.paysdk.http.privacy;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.utils.MCLog;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PrivacyInfoProcess {

    private static final String TAG = "SwitchInfoProcess";

    private String getParamStr(){
        Map<String, String> map = new HashMap<String, String>();
        MCLog.w(TAG, "fun#ptb_pay params:" + map.toString());

        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler, boolean isShowDialog) {
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(getParamStr()));
        } catch (UnsupportedEncodingException e) {
            params = null;
            MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
        }

        PrivacyInfoRequest request = new PrivacyInfoRequest(handler);
        request.post(MCHConstant.getInstance().getPrivacyUrl(), params, isShowDialog);
    }

}
