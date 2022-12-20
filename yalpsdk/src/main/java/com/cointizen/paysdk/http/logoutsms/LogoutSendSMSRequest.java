package com.cointizen.paysdk.http.logoutsms;

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
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;
import com.cointizen.paysdk.utils.VerifyCodeCookisStore;

import org.json.JSONException;
import org.json.JSONObject;

public class LogoutSendSMSRequest {

    private static final String TAG = "LogoutVerifyRequest";

    HttpUtils http;
    Handler mHandler;

    public LogoutSendSMSRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.HTTP_REQUEST_FAIL, "参数异常");
            return;
        }
        if (null != VerifyCodeCookisStore.cookieStore) {
            http.configCookieStore(VerifyCodeCookisStore.cookieStore);
            MCLog.e(TAG, "fun#post cookieStore not null");
        } else {
            MCLog.e(TAG, "fun#post cookieStore is null");
        }
        MCLog.w(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);
//                LogoutSengSMSEntity logoutVerifyEntity = new LogoutSengSMSEntity();
                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");
                    if(status == 200){
//                        if (json.optJSONObject("data") != null) {
//                            JSONObject data = json.getJSONObject("data");
//                            logoutVerifyEntity.setType(data.optString("type", ""));
//                            logoutVerifyEntity.setPhoneOrEmail(data.optString("phone", ""));
//                            logoutVerifyEntity.setPhoneOrEmail(data.optString("phone", ""));
//                        }

                        noticeResult(Constant.LOGOUT_SEND_SMS, "");
                    }else {
                        String tip = json.optString("msg");
                        MCLog.e(TAG, "tip:" + tip);
                        noticeResult(Constant.HTTP_REQUEST_FAIL, tip);
                    }

                } catch (JSONException e) {
                    noticeResult(Constant.HTTP_REQUEST_FAIL, "数据解析异常");
                }



            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.HTTP_REQUEST_FAIL, "Failed to connect to the server");
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
