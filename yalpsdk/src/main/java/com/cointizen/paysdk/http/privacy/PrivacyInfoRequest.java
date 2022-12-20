package com.cointizen.paysdk.http.privacy;

import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.PrivacyInfoEntity;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

public class PrivacyInfoRequest {

    private static final String TAG = "SwitchInfoRequest";

    HttpUtils http;
    Handler mHandler;

    public PrivacyInfoRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params, final boolean isShowDialog) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.HTTP_REQUEST_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.w(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);
                PrivacyInfoEntity privacyInfoEntity = new PrivacyInfoEntity();
                privacyInfoEntity.isShowDialog = isShowDialog;
                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");
                    if(status == 200){
                        JSONObject data = json.getJSONObject("data");
                        privacyInfoEntity.setPrivacy_link(data.optString("privacy_url", ""));
                        privacyInfoEntity.setPrivacy_name(data.optString("privacy_title", ""));
                        privacyInfoEntity.setAgreement_name(data.optString("protocol_title", ""));
                        privacyInfoEntity.setAgreement_link(data.optString("protocol_url", ""));
                        privacyInfoEntity.setClose_name(data.optString("unsubscribe_protocol_title", ""));
                        privacyInfoEntity.setClose_link(data.optString("unsubscribe_protocol_url", ""));
                        noticeResult(Constant.PRIVACY_SUCCESS, privacyInfoEntity);
                    }else {
                        String tip = json.optString("msg");
                        MCLog.e(TAG, "tip:" + tip);
                        noticeResult(Constant.HTTP_REQUEST_FAIL, tip);
                    }

                } catch (JSONException e) {
                    noticeResult(Constant.HTTP_REQUEST_FAIL, HttpConstants.S_mJGaHLIGRo);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.HTTP_REQUEST_FAIL, HttpConstants.S_uVIIKmGFwV);
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
