package com.cointizen.paysdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.PTBPayResult;
import com.cointizen.paysdk.http.RequestUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.utils.ConfigureApp;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class PTBPayRequest {

    private static final String TAG = "PTBPayRequest";

    HttpUtils http;
    Handler mHandler;

    public PTBPayRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params, Context context) {
        if(!ConfigureApp.getInstance().Configure(context)){
            return;
        }
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.YLPD_PAY_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                PTBPayResult ptbPayResult = new PTBPayResult();

                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {
                        ptbPayResult.setReturn_code(json.optString("code"));
                        ptbPayResult.setReturn_status("1");
                        ptbPayResult.setReturn_msg(json.optString("msg"));
                        JSONObject data = json.getJSONObject("data");
                        ptbPayResult.setOrderNumber(data.optString("out_trade_no"));
                        noticeResult(Constant.YLPD_PAY_SUCCESS, ptbPayResult);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.YLPD_PAY_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.YLPD_PAY_FAIL, "解析参数异常");
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.YLPD_PAY_FAIL, "Failed to connect to the server");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.YLPD_PAY_FAIL, "Failed to connect to the server");
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
