package com.cointizen.paysdk.http.request;

import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：获取微信官方支付结果
 * 作者：Administrator
 * 时间: 2017-12-11 17:00
 */

public class WXPayResultRequest {
    private static final String TAG = "WXPayResultRequest";
    Handler mHandler;
    HttpUtils http;

    public WXPayResultRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (android.text.TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.GET_WX_PAY_RESULT_FAIL, "参数为空");
            return;
        }
        MCLog.w(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                int status = -1;
                try {
                    JSONObject obj = new JSONObject(response);
                    status = obj.optInt("code");
                    if (status == 200) {
                        noticeResult(Constant.GET_WX_PAY_RESULT_SUCCESS, "Payment done successfully");
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(obj.optString("msg"))) {
                            msg = obj.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(obj.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.GET_WX_PAY_RESULT_FAIL, msg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure:" + msg);
                MCLog.e(TAG, "onFailure:" + error.getExceptionCode() + error.getStackTrace() + error.getMessage());
                noticeResult(Constant.GET_WX_PAY_RESULT_FAIL, "Failed to connect to the server");
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
