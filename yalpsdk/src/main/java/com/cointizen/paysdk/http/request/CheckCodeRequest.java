package com.cointizen.paysdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cointizen.paysdk.entity.ChannelAndGameInfo;
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
import com.cointizen.paysdk.http.HttpConstants;

public class CheckCodeRequest {
    private static final String TAG = "CheckCodeRequest";

    HttpUtils http;
    Handler mHandler;

    public CheckCodeRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.CHECK_CODE_FAIL, null, HttpConstants.S_VKHKJPHQds);
            return;
        }
        if(null != VerifyCodeCookisStore.cookieStore){
            http.configCookieStore(VerifyCodeCookisStore.cookieStore);
            MCLog.e(TAG, "fun#post cookieStore have");
        }else{
            MCLog.e(TAG, "fun#post cookieStore is null");
        }
        http.send(HttpRequest.HttpMethod.POST, url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String response = RequestUtil.getResponse(responseInfo);
                        ChannelAndGameInfo info = new ChannelAndGameInfo();
                        try {
                            JSONObject json = new JSONObject(response);
                            int status = json.optInt("code");
                            if (status == 200) {
                                String phone = json.optString("phone", "").trim();
                                if (TextUtils.isEmpty(phone) || "null".equals(phone)) {
                                    phone = "";
                                }
                                info.setPhoneNumber(phone);
                                info.seteMail(json.optString("email", ""));
                                info.setAccount(json.optString("account", ""));
                                noticeResult(Constant.CHECK_CODE_SUCCESS, info, "");
                            } else {
                                String msg = MCErrorCodeUtils.getErrorMsg(json.getInt("code"));
                                MCLog.e(TAG, "msg:" + msg);
                                if (TextUtils.isEmpty(msg)) {
                                    msg = HttpConstants.S_rLrNAHMNhs;
                                }
                                noticeResult(Constant.CHECK_CODE_FAIL, null, msg);
                            }
                        } catch (JSONException e) {
                            noticeResult(Constant.CHECK_CODE_FAIL, null, HttpConstants.S_mJGaHLIGRo);
                            MCLog.e(TAG, "fun#get json e = " + e);
                        } catch (Exception e) {
                            noticeResult(Constant.CHECK_CODE_FAIL,
                                    null, HttpConstants.S_mJGaHLIGRo);
                            MCLog.e(TAG, "fun#get json e = " + e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                        MCLog.e(TAG, "onFailure" + msg);
                        noticeResult(Constant.CHECK_CODE_FAIL, null, HttpConstants.S_uVIIKmGFwV);
                    }
                });
    }

    private void noticeResult(int type, ChannelAndGameInfo info, String res) {
        Message msg = new Message();
        msg.what = type;
        if (type == Constant.CHECK_CODE_FAIL) {
            msg.obj = res;
        } else {
            msg.obj = info;
        }

        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
