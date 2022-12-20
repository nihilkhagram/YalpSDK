package com.cointizen.paysdk.http.request;

import android.os.Handler;
import android.os.Message;

import com.cointizen.paysdk.bean.UserLoginSession;
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

/**
 * Created by zhujinzhujin
 * on 2017/1/17.
 */

public class UpdateVisitorInfoRequest {

    private static final String TAG = "UpdateVisitorInfoRequest";

    HttpUtils http;
    Handler mHandler;

    public UpdateVisitorInfoRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.UPDATE_VISITOR_INFO_FAIL, "参数异常");
            return;
        }
        MCLog.w(TAG, "fun#post url = " + url);
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
                    MCLog.e(TAG, "tip:" + tip);

                    if (status == 200) {
                        JSONObject data = json.getJSONObject("data");
                        String token = data.optString("token");
                        UserLoginSession.getInstance().getChannelAndGame().setSmallAccountToken(token);
                        noticeResult(Constant.UPDATE_VISITOR_INFO_SUCCESS, tip);
                    } else {
                        noticeResult(Constant.UPDATE_VISITOR_INFO_FAIL, tip);
                    }

                } catch (JSONException e) {
                    tip = "参数异常";
                    noticeResult(Constant.UPDATE_VISITOR_INFO_FAIL, tip);
                } catch (Exception e) {
                    tip = "参数异常";
                    noticeResult(Constant.UPDATE_VISITOR_INFO_FAIL, tip);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.UPDATE_VISITOR_INFO_FAIL, "Failed to connect to the server");
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
