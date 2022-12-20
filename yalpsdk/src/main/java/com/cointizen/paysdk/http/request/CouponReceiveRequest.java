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
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

/**
 * 描述：领取代金券
 * 时间: 2019-12-06 9:26
 */

public class CouponReceiveRequest {
    private static final String TAG = "CouponReceiveRequest";
    Handler mHandler;
    HttpUtils http;

    public CouponReceiveRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.RECEIVE_COUPON_FAIL, HttpConstants.S_VKHKJPHQds);
            return;
        }

        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);
                int status = -1;
                try {
                    JSONObject obj = new JSONObject(response);
                    status = obj.getInt("code");
                    if (status == 200 || status == 1) {
                        noticeResult(Constant.RECEIVE_COUPON_SUCCESS, HttpConstants.S_NfdRMASeFW);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(obj.optString("msg"))) {
                            msg = obj.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.RECEIVE_COUPON_FAIL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    noticeResult(Constant.RECEIVE_COUPON_FAIL, HttpConstants.S_bcwiMnfyeK);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure:" + msg);
                MCLog.e(TAG, "onFailure:" + error.getExceptionCode() + error.getStackTrace() + error.getMessage());
                noticeResult(Constant.RECEIVE_COUPON_FAIL, HttpConstants.S_uVIIKmGFwV);
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

