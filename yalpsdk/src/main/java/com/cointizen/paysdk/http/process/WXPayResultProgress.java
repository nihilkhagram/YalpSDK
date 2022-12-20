package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.WXPayResultRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.utils.TextUtils;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：获取微信官方支付结果
 * 作者：Administrator
 * 时间: 2017-12-11 16:54
 */

public class WXPayResultProgress {
    private static final String TAG="WXPayResultProgress";

    private String orderNo;

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void post(Handler handler)
    {
        if (null == handler) {
            MCLog.e(TAG, "fun#post handler is null");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", SdkDomain.getInstance().getGameId());
        map.put("orderno",orderNo);

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
            WXPayResultRequest request = new WXPayResultRequest(handler);
            request.post(MCHConstant.getInstance().getWxPayResultUrl(), params);
        } else {
            MCLog.e(TAG, "fun#post RequestParams is null");
        }
    }
}
