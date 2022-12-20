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
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.GamePayRecordEntity;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.ConfigureApp;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import com.cointizen.paysdk.http.HttpConstants;

public class PayReCordRequest {

    private static final String TAG = "PayReCordRequest";

    HttpUtils http;
    Handler mHandler;

    public PayReCordRequest(Handler mHandler) {
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
                MCLog.e(TAG,HttpConstants.Log_kgZaLMFKRZ+response);
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200 ) {
                        JSONObject data1 = json.getJSONObject("data");
                        JSONArray data = data1.getJSONArray("lists");
                        GamePayRecordEntity gamePayRecordEntity = new GamePayRecordEntity();
                        ArrayList<GamePayRecordEntity.ListsBean> objects = new ArrayList<>();
                        for (int i=0;i<data.length();i++){
                            GamePayRecordEntity.ListsBean dataBean = new GamePayRecordEntity.ListsBean();
                            JSONObject jsonObject = data.getJSONObject(i);
                            dataBean.setId(jsonObject.getInt("id"));
                            dataBean.setPay_amount(jsonObject.getString("pay_amount"));
                            dataBean.setPay_order_number(jsonObject.getString("pay_order_number"));
                            dataBean.setPay_time(jsonObject.getLong("pay_time"));
                            dataBean.setPay_way(jsonObject.getString("pay_way"));
                            dataBean.setGame_name(jsonObject.getString("game_name"));
                            objects.add(dataBean);
                        }

                        gamePayRecordEntity.setLists(objects);
                        gamePayRecordEntity.setCount(data1.getDouble("count"));

                        noticeResult(Constant.PAY_RECORD_SUCCESS, gamePayRecordEntity);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.PAY_RECORD_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.PAY_RECORD_FAIL, HttpConstants.S_egTJZwEHGo);
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.PAY_RECORD_FAIL, HttpConstants.S_uVIIKmGFwV);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.PAY_RECORD_FAIL, HttpConstants.S_uVIIKmGFwV);
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
