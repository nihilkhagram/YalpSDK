package com.cointizen.paysdk.http.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.bean.GiftDetModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.ConfigureApp;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

public class GiftDetRequest {

    private static final String TAG = "GiftDetRequest";

    HttpUtils http;
    Handler mHandler;

    public GiftDetRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params, Context context) {
        if(!ConfigureApp.getInstance().Configure(context)){
            return;
        }
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.GIFTDET_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                MCLog.e(TAG,HttpConstants.Log_lOKbjBxJWP+response);
                GiftDetModel giftDetModel = new GiftDetModel();
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {
                        JSONObject list = json.getJSONObject("data");
                        giftDetModel.setId( list.optString("gift_id"));
                        giftDetModel.setGiftbag_name( list.optString("giftbag_name"));
                        giftDetModel.setStart_time( list.optString("start_time"));
                        giftDetModel.setEnd_time( list.optString("end_time"));
                        giftDetModel.setDesribe( list.optString("desribe"));
                        giftDetModel.setDigest( list.optString("digest"));
                        giftDetModel.setIcon( list.optString("icon"));
                        giftDetModel.setGame_name( list.optString("game_name"));
                        giftDetModel.setRecord_novice( list.optString("novice"));
                        giftDetModel.setNotice( list.optString("notice"));
                        giftDetModel.setReceived( list.optInt("received"));
                        giftDetModel.setSurplus( list.optInt("surplus"));
                        giftDetModel.setNovice_num( list.optInt("novice_num"));
                        noticeResult(Constant.GIFTDET_SUCCESS, giftDetModel);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.GIFTDET_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.GIFTDET_FAIL, HttpConstants.S_egTJZwEHGo);
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.GIFTDET_FAIL, HttpConstants.S_uVIIKmGFwV);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.GIFTDET_FAIL, HttpConstants.S_uVIIKmGFwV);
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
