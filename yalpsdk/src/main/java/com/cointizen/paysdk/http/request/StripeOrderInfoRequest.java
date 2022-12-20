package com.cointizen.paysdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.HttpConstants;
import com.cointizen.paysdk.utils.MCLog;

public class StripeOrderInfoRequest {

    private static final String TAG = "StripeOrderInfoRequest";

    HttpUtils http;
    Handler mHandler;

    public StripeOrderInfoRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        MCLog.e(TAG, "fun#post url = " + url);
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun # post url is null or params is null");
            noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, HttpConstants.S_alsTLDDCow);
            return;
        }

        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //** For Jay **//
//                String response = RequestUtil.getResponse(responseInfo);
//                PaymentIntent pi = extractPaymentIntent(response);
//                final JSONObject responseJson = parseResponse(response.body());
//                paymentIntentClientSecret = responseJson.optString("clientSecret");
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "error" + error.getExceptionCode());
                noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, HttpConstants.S_uVIIKmGFwV);
            }
        });
    }

    private void noticeResult(int type, Object obj) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = obj;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
