package com.cointizen.paysdk.http.request;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.bean.VerifyCode;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.VerifyCodeCookisStore;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class VerificationCodeRequest {

    private static final String TAG = "VerifyPhoneCodeRequest";

    HttpUtils http;
    Handler mHandler;

    public VerificationCodeRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, HttpConstants.S_CDpNomfyon);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
//		 http.configCookieStore(cookieStore);
        if (null != VerifyCodeCookisStore.cookieStore) {
            http.configCookieStore(VerifyCodeCookisStore.cookieStore);
            MCLog.e(TAG, "fun#post cookieStore not null");
        }
        http.configCurrentHttpCacheExpiry(0);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                MCLog.e(TAG,HttpConstants.Log_VOevvicjLd+response);
//                noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, HttpConstants.S_uVIIKmGFwV);
                if (null == VerifyCodeCookisStore.cookieStore) {
                    DefaultHttpClient dh = (DefaultHttpClient) http.getHttpClient();
                    VerifyCodeCookisStore.cookieStore = dh.getCookieStore();
                    MCLog.e(HttpConstants.Log_lEokVLZWok, "" + (VerifyCodeCookisStore.cookieStore == null) + "");
                }
//				CookieStore cs = dh.getCookieStore();
//				List<Cookie> cookies = cs.getCookies();
//				String aa = null;
//				for (int i = 0; i < cookies.size(); i++) {
//					if ("JSESSIONID".equals(cookies.get(i).getName())) {
//						aa = cookies.get(i).getValue();
//						break;
//					}
//				}
//				MCLog.e(TAG, "sessionid" + aa);

                VerifyCode verifyCode = new VerifyCode();

                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");
                    if (status == 200) {
                        verifyCode.setCode("");
                        noticeResult(Constant.VERIFYCODE_REQUEST_SUCCESS, verifyCode);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, HttpConstants.S_INOOELsLct);
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, HttpConstants.S_uVIIKmGFwV);
            }
        });
    }

    protected String getCodeByRes(String optString) {
        String code = "";
        JSONObject json;
        try {
            json = new JSONObject(optString);
            code = json.optString("return_code");
        } catch (JSONException e) {
            code = "";
            MCLog.e(TAG, "fun#getCodeByRes JSONException:" + e);
        }
        return code;
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
