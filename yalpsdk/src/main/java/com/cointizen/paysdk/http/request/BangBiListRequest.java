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
import com.cointizen.paysdk.bean.BangBiListBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.ConfigureApp;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import com.cointizen.paysdk.http.HttpConstants;

public class BangBiListRequest {

    private static final String TAG = "BangBiListRequest";

    HttpUtils http;
    Handler mHandler;

    public BangBiListRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params, Context activity) {
        if(!ConfigureApp.getInstance().Configure(activity)){
            return;
        }
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.BANGBI_LIST_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                MCLog.e(TAG,HttpConstants.Log_SUPalGvWjA+response);
                BangBiListBean giftDetModel = new BangBiListBean();
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {
                        JSONArray data = json.getJSONArray("data");
                        ArrayList<BangBiListBean.DataBean> dataBeans = new ArrayList<>();
                        for (int i = 0;i<data.length();i++){
                            JSONObject jsonObject = data.getJSONObject(i);
                            BangBiListBean.DataBean dataBean = new BangBiListBean.DataBean();
                            dataBean.setGame_name(jsonObject.optString("game_name",""));
//                            dataBean.setGame_id(jsonObject.optString("game_id",""));
                            dataBean.setBind_balance(jsonObject.optString("bind_balance",""));
                            dataBean.setIcon(jsonObject.optString("icon",""));
                            dataBeans.add(dataBean);
                        }
                        giftDetModel.setData(dataBeans);
                        noticeResult(Constant.BANGBI_LIST_SUCCESS, giftDetModel);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.BANGBI_LIST_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.BANGBI_LIST_FAIL, HttpConstants.S_egTJZwEHGo);
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.BANGBI_LIST_FAIL, HttpConstants.S_uVIIKmGFwV);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.BANGBI_LIST_FAIL, HttpConstants.S_uVIIKmGFwV);
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
