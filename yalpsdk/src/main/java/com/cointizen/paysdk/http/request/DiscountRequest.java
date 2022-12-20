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
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

/**
 * 描述：折扣Request
 * 时间: 2019-12-23 9:50
 */
public class DiscountRequest {
    private String TAG = "DiscountRequest";
    HttpUtils http;
    Handler mHandler;

    public DiscountRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.DISCOUNT_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                int status;
                String tip = "";
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    tip = json.optString("msg");

                    if (status == 200 || status == 1) {
                        JSONObject jsonData = json.getJSONObject("data");
                        String htmlData = jsonData.optString("html");
                        noticeResult(Constant.DISCOUNT_SUCCESS, htmlData);
                    } else {
                        noticeResult(Constant.DISCOUNT_FAIL, tip);
                    }

                } catch (Exception e) {
                    MCLog.e(TAG, HttpConstants.Log_SQPVfbaIXX + e);
                    noticeResult(Constant.DISCOUNT_FAIL, HttpConstants.S_SwNbiXIiCR);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                noticeResult(Constant.DISCOUNT_FAIL, HttpConstants.S_uVIIKmGFwV);
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
