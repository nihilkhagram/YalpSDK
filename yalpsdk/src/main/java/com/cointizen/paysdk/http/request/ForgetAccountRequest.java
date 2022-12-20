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
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

public class ForgetAccountRequest {

    private static final String TAG = "ForgetAccountRequest";

    HttpUtils http;
    Handler mHandler;

    public ForgetAccountRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.GET_USER_INFO_FAIL, null, HttpConstants.S_VKHKJPHQds);
            return;
        }
        http.send(HttpRequest.HttpMethod.POST, url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String response = RequestUtil.getResponse(responseInfo);
                        MCLog.e(HttpConstants.Log_FGeNNyKDHx, response);
                        ChannelAndGameInfo info = new ChannelAndGameInfo();
                        try {
                            JSONObject json = new JSONObject(response);
                            int status = json.optInt("code");
                            if (status == 200) {

                                JSONObject data = json.getJSONObject("data");
//                                if(code_type==1){
                                    String phone = data.optString("phone", "").trim();
                                    if (TextUtils.isEmpty(phone) || "null".equals(phone)) {
                                        phone = "";
                                    }
                                    info.setPhoneNumber(phone);
//                                }else if(code_type==2){
                                    info.seteMail(data.optString("email", ""));
//                                }
                                info.setAccount(data.optString("account", ""));
                                info.setId(data.optString("user_id", ""));
                                noticeResult(Constant.GET_USER_INFO_SUCCESS, info, "");
                            } /*else if ("-1".equals(status)) {
                                noticeResult(Constant.GET_USER_INFO_FAIL, null, HttpConstants.S_ujXyrqDQQi);
                            } */ else {
                                String msg = json.getString("msg");
                                MCLog.e(TAG, "msg:" + msg);
                                if (TextUtils.isEmpty(msg)) {
                                    msg = HttpConstants.S_rLrNAHMNhs;
                                }
                                noticeResult(Constant.GET_USER_INFO_FAIL, null, msg);
                            }
                        } catch (JSONException e) {
                            noticeResult(Constant.GET_USER_INFO_FAIL, null, HttpConstants.S_mJGaHLIGRo);
                            MCLog.e(TAG, "fun#get json e = " + e);
                        } catch (Exception e) {
                            noticeResult(Constant.GET_USER_INFO_FAIL,
                                    null, HttpConstants.S_mJGaHLIGRo);
                            MCLog.e(TAG, "fun#get json e = " + e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                        MCLog.e(TAG, "onFailure" + msg);
                        noticeResult(Constant.GET_USER_INFO_FAIL, null, HttpConstants.S_uVIIKmGFwV);
                    }
                });
    }

    protected float stringToFloat(String optString) {
        if (TextUtils.isEmpty(optString)) {
            return 0.00f;
        }
        float money;
        try {
            money = Float.parseFloat(optString);
            if(money==0){
                money=0.00f;
            }
        } catch (NumberFormatException e) {
            money = 0.00f;
        } catch (Exception e) {
            money = 0.00f;
        }

        return money;
    }

    private void noticeResult(int type, ChannelAndGameInfo info, String res) {
        Message msg = new Message();
        msg.what = type;
        if (type == Constant.GET_USER_INFO_FAIL) {
            msg.obj = res;
        } else {
            msg.obj = info;
        }

        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
