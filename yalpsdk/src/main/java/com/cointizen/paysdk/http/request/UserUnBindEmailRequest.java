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
import com.cointizen.paysdk.utils.VerifyCodeCookisStore;

import org.json.JSONException;
import org.json.JSONObject;

public class UserUnBindEmailRequest {

    private static final String TAG = "UserUnBindEmailRequest";
    HttpUtils http;
    Handler mHandler;

    public UserUnBindEmailRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.USER_UNBIND_EMAIL_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        if (null != VerifyCodeCookisStore.cookieStore) {
            http.configCookieStore(VerifyCodeCookisStore.cookieStore);
            MCLog.e(TAG, "fun#post cookieStore not null");
        } else {
            MCLog.e(TAG, "fun#post cookieStore is null");
        }
        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String response = RequestUtil.getResponse(responseInfo);
                        int status = -1;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            status = json.optInt("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (status==200||status==1) {
                            noticeResult(Constant.USER_UNBIND_EMAIL_SUCCESS, "");
                        } else {
                            String msg;
                            if (!TextUtils.isEmpty(json.optString("msg"))) {
                                msg = json.optString("msg");
                            } else {
                                msg = MCErrorCodeUtils.getErrorMsg(status);
                            }
                            MCLog.e(TAG, "msg:" + msg);
                            noticeResult(Constant.USER_UNBIND_EMAIL_FAIL, msg);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MCLog.e(TAG, "   onFailure" + msg + "ExceptionCode" + error.getExceptionCode());
                        noticeResult(Constant.USER_UNBIND_EMAIL_FAIL, "Failed to connect to the server");
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
