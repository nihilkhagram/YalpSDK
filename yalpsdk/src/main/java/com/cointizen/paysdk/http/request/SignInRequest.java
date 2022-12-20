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
import com.cointizen.paysdk.bean.SignBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInRequest {

    private static final String TAG = "SignInRequest";

    HttpUtils http;
    Handler mHandler;

    public SignInRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.SIGNIN_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                SignBean signBean = new SignBean();
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200 ) {
//                        JSONObject data = json.getJSONObject("data");
//                        String msg = json.optString("msg", "");
//                        signBean.setMsg(msg);
//                        signBean.setSign_day(data.optInt("sign_day"));          //签到了几天
//                        signBean.setBase_point(data.optInt("base_point"));      //基础积分
//                        signBean.setIncrease_point(data.optInt("increase_point"));//递增的积分  根据天数变化的
                        noticeResult(Constant.SIGNIN_SUCCESS, json.optString("msg"));
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg","");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.SIGNIN_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.SIGNIN_FAIL, "解析参数异常");
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.SIGNIN_FAIL, "Failed to connect to the server");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.SIGNIN_FAIL, "Failed to connect to the server");
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
