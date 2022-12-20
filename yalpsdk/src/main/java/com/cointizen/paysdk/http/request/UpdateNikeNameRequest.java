package com.cointizen.paysdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class UpdateNikeNameRequest {

    private static final String TAG = "UpdateNikeNameRequest";
    HttpUtils http;
    Handler mHandler;

    public UpdateNikeNameRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.UPDATE_NIKE_FAIL, HttpConstants.S_ElwWiRoGqB);
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
                    MCLog.e(TAG, "tip:" + tip);
                } catch (JSONException e) {
                    status = -1;
                    tip = HttpConstants.S_ibwoCxsCId;
                } catch (Exception e) {
                    status = -1;
                    tip = HttpConstants.S_ibwoCxsCId;
                }

                if (status == 200) {
                    noticeResult(Constant.UPDATE_NIKE_SUCCESS, tip);
                } else {
                    noticeResult(Constant.UPDATE_NIKE_FAIL, tip);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                noticeResult(Constant.UPDATE_NIKE_FAIL, HttpConstants.S_uVIIKmGFwV);
            }
        });
    }

    private void noticeResult(int type, String str) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = str;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
