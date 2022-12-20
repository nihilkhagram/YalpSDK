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
 * 描述：获取功能开关Request
 * 时间: 2020-09-30 9:50
 */
public class SwitchStatusRequest {
    private String TAG = "SwitchStatusRequest";
    HttpUtils http;
    Handler mHandler;

    public SwitchStatusRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.SWITCH_STATUS_FAIL, HttpConstants.S_ElwWiRoGqB);
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
                        Constant.RED_BAG_STATUS = jsonData.optInt("task_id",0);
                        noticeResult(Constant.SWITCH_STATUS_SUCCESS, HttpConstants.S_UrRzHuPyYY);
                    } else {
                        noticeResult(Constant.SWITCH_STATUS_FAIL, tip);
                    }

                } catch (Exception e) {
                    MCLog.e(TAG, HttpConstants.Log_xKIaeWYTyF + e);
                    noticeResult(Constant.SWITCH_STATUS_FAIL, HttpConstants.S_SwNbiXIiCR);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                noticeResult(Constant.SWITCH_STATUS_FAIL, HttpConstants.S_uVIIKmGFwV);
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
