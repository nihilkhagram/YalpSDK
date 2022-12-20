package com.cointizen.paysdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.cointizen.paysdk.entity.WXOrderInfo;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.http.HttpConstants;

public class WFTOrderInfoRequest {

    private static final String TAG = "WFTOrderInfoRequest";

    HttpUtils http;
    Handler mHandler;

    /**
     * 商品类型
     */
    public String goodsType;// 标示微信支付

    public WFTOrderInfoRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.WFT_ORDERINFO_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String response = RequestUtil.getResponse(
                                responseInfo);
                        try {
                            JSONObject json = new JSONObject(response);
                            int status = json.optInt("code");
                            if (status == 200) {
                                WXOrderInfo wxOrderInfo = new WXOrderInfo();
                                JSONObject data = json.getJSONObject("data");
                                if (data.optString("paytype").equals("wx"))//微信官方
                                {
                                    wxOrderInfo.setTag("wx");
                                    wxOrderInfo.setOrderNo(data.optString("orderno"));
                                    wxOrderInfo.setUrl(data.optString("url"));
                                    wxOrderInfo.setCal_url(data.optString("cal_url"));
                                }
                                if (data.optString("paytype").equals("wft"))//威富通
                                {
                                    wxOrderInfo.setTag("wft");
                                    wxOrderInfo.setUrl(data.optString("url"));
                                }
                                if (data.optString("paytype").equals("jft"))//竣付通
                                {
                                    wxOrderInfo.setTag("jft");
                                    wxOrderInfo.setUrl(data.optString("url"));
                                }
                                noticeResult(Constant.WFT_ORDERINFO_SUCCESS, wxOrderInfo);
                            } else {
                                String msg;
                                if (!TextUtils.isEmpty(json.optString("msg"))) {
                                    msg = json.optString("msg");
                                } else {
                                    msg = MCErrorCodeUtils.getErrorMsg(status);
                                }
                                MCLog.e(TAG, "msg:" + msg);
                                noticeResult(Constant.WFT_ORDERINFO_FAIL, msg);
                            }
                        } catch (JSONException e) {
                            noticeResult(Constant.WFT_ORDERINFO_FAIL, HttpConstants.S_mJGaHLIGRo);
                            MCLog.e(TAG, "fun#post JSONException:" + e);
                        } catch (Exception e) {
                            noticeResult(Constant.WFT_ORDERINFO_FAIL, HttpConstants.S_IzOAOllHRh);
                            MCLog.e(TAG, "fun#post JSONException: " + e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MCLog.e(TAG, "[onFailure] ExceptionCode:" + error.getExceptionCode());
                        MCLog.e(TAG, "onFailure" + msg);
                        noticeResult(Constant.WFT_ORDERINFO_FAIL, HttpConstants.S_uVIIKmGFwV);
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
