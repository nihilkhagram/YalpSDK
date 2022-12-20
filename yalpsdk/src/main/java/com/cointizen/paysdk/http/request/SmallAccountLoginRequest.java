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
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

/**
 * 描述：添加小号Request
 * 时间: 2018-07-17 9:50
 */
public class SmallAccountLoginRequest {
    private String TAG = "SmallAccountLoginRequest";
    private boolean isYKLogin;  //是否是游客登录
    HttpUtils http;
    Handler mHandler;

    public SmallAccountLoginRequest(Handler mHandler,boolean isYK) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
        this.isYKLogin = isYK;
    }


    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.SMALL_ACCOUNT_LOGIN_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                UserLogin smallAccountLogin = new UserLogin();
                String response = RequestUtil.getResponse(responseInfo);
                int code;
                String tip = "";
                try {
                    JSONObject json = new JSONObject(response);
                    code = json.optInt("code");
                    tip = json.optString("msg");
                    MCLog.e(TAG, "tip:" + tip);

                    if (code == 200) {
                        JSONObject jsonData = json.getJSONObject("data");
                        smallAccountLogin.setLoginStatus("1");
                        smallAccountLogin.setYKLogin(isYKLogin);
                        smallAccountLogin.setLoginMsg(tip);
                        smallAccountLogin.setAccountUserId(jsonData.optString("small_id"));
                        smallAccountLogin.setUserName(jsonData.optString("nickname"));
                        smallAccountLogin.setToken(jsonData.optString("token"));
                        smallAccountLogin.setExtra_param(jsonData.optString("extra_param"));
                        noticeResult(Constant.SMALL_ACCOUNT_LOGIN_SUCCESS, smallAccountLogin);
                    } else {
                        noticeResult(Constant.SMALL_ACCOUNT_LOGIN_FAIL, tip);
                    }

                } catch (Exception e) {
                    MCLog.e(TAG, HttpConstants.Log_MagGdYHvFn + e);
                    noticeResult(Constant.SMALL_ACCOUNT_LOGIN_FAIL, HttpConstants.S_dVxBoHExtD);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                noticeResult(Constant.SMALL_ACCOUNT_LOGIN_FAIL, HttpConstants.S_uVIIKmGFwV);
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
