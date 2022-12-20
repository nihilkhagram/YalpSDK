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
import com.cointizen.paysdk.bean.HelperBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

public class HelperCenterRequest {

    private static final String TAG = "HelperCenterRequest";

    HttpUtils http;
    Handler mHandler;

    public HelperCenterRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.HELPER_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);
                MCLog.e(TAG,HttpConstants.Log_eYpSyDlvQE+response);
                HelperBean giftDetModel = new HelperBean();
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {
                        JSONObject list = json.getJSONObject("data");
                        giftDetModel.setGameCommunity(list.optString("APP_QQ"));
                        giftDetModel.setAPP_TEL(list.optString("APP_TEL"));
                        giftDetModel.setAPP_EMAIL(list.optString("APP_EMAIL"));
                        giftDetModel.setQQ_GROUP_KEY(list.optString("QQ_GROUP_KEY"));
                        giftDetModel.setAPP_QQ_GROUP(list.optString("APP_QQ_GROUP"));
                        giftDetModel.setServiceH5Switch(list.optString("APP_XGKF_SWITCH"));
                        giftDetModel.setServiceH5URL(list.optString("APP_XGKF_URL"));
                        giftDetModel.serviceSwitch = list.optString("APP_XGKF_SWITCH");
                        noticeResult(Constant.HELPER_SUCCESS, giftDetModel);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.HELPER_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.HELPER_FAIL, HttpConstants.S_egTJZwEHGo);
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.HELPER_FAIL, HttpConstants.S_uVIIKmGFwV);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.HELPER_FAIL, HttpConstants.S_uVIIKmGFwV);
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
