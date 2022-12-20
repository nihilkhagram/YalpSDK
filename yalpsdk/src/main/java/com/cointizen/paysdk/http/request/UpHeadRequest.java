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

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

public class UpHeadRequest {

    private static final String TAG = "UpHeadRequest";

    HttpUtils http;
    Handler mHandler;

    public UpHeadRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null");
            noticeResult(Constant.RECORD_LIST_FAIL, null, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        //设置当前请求的缓存时间
        http.configCurrentHttpCacheExpiry(0);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                MCLog.e(TAG,HttpConstants.Log_cVvRnfgbQG+response);
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {//  头像上传成功
                        String total= json.getString("data");
                        noticeResult(Constant.AVATAR_UPLOAD_SUCCESS, total, "");
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.AVATAR_UPLOAD__FAIL, null, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.AVATAR_UPLOAD__FAIL, null, HttpConstants.S_bcwiMnfyeK);
                    MCLog.e(TAG, "fun#post  JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.AVATAR_UPLOAD__FAIL, null, HttpConstants.S_bcwiMnfyeK);
                    MCLog.e(TAG, HttpConstants.Log_ywWJMAegKQ + e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.AVATAR_UPLOAD__FAIL, null, HttpConstants.S_uVIIKmGFwV);
            }
        });
    }

    private void noticeResult(int type, String addPtbRecordEntity, String str) {
        Message msg = new Message();
        msg.what = type;
        if (Constant.AVATAR_UPLOAD_SUCCESS == type) {
            msg.obj = addPtbRecordEntity;
        } else {
            msg.obj = str;
        }
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
