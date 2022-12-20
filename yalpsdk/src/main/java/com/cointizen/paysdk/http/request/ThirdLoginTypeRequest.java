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
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

public class ThirdLoginTypeRequest {

    private static final String TAG = "ThirdLoginTypeRequest";

    HttpUtils http;
    Handler mHandler;

    public ThirdLoginTypeRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.THIRD_LOGIN_TYPE_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.w(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                String config = "";
                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");
                    if(status == 200){
                        JSONObject data = json.getJSONObject("data");
                        config = data.optString("config");
                        Constant.AGREEMENT_NAME = data.optString("portal_title");
                        Constant.SDK_LOGO_URL = data.optString("sdk_login_logo", "");
                    }

                } catch (JSONException e) {
                    config = "";
                }

                if (!TextUtils.isEmpty(config)) {
                    noticeResult(Constant.THIRD_LOGIN_TYPE_SUCCESS, config);
                } else {
                    noticeResult(Constant.THIRD_LOGIN_TYPE_FAIL, HttpConstants.S_RrDZbiQRHF);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.THIRD_LOGIN_TYPE_FAIL, HttpConstants.S_uVIIKmGFwV);
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
