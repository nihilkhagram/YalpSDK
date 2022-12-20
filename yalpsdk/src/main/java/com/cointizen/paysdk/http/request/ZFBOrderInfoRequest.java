package com.cointizen.paysdk.http.request;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.WXOrderInfo;
import com.cointizen.paysdk.entity.StripeVerificationResult;
import com.cointizen.paysdk.http.RequestUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.util.Base64;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.OrderInfoUtils;
import com.cointizen.paysdk.utils.PaykeyUtil;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class ZFBOrderInfoRequest {

    private static final String TAG = "ZFBOrderInfoRequest";

    HttpUtils http;
    Handler mHandler;

    public ZFBOrderInfoRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params, final boolean isWapPay) {
        MCLog.e(TAG, "fun#post url = " + url);
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun # post url is null or params is null");
            noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, HttpConstants.S_alsTLDDCow);
            return;
        }
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                StripeVerificationResult zfbPayResult = new StripeVerificationResult();
                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");

                    if (status == 200){
                        JSONObject data = json.getJSONObject("data");
                        if(isWapPay) {
                            WXOrderInfo wapPayOrderInfo = new WXOrderInfo();
                            wapPayOrderInfo.setTag("zfb");
                            wapPayOrderInfo.setOrderNo(data.optString("out_trade_no"));
                            wapPayOrderInfo.setUrl(data.optString("url"));
                            noticeResult(Constant.ZFB_WAPPAY_ORDERINFO_SUCCESS, wapPayOrderInfo);
                        }else {
                            String orderInfo = data.optString("orderInfo");
                            String md5Sign = data.optString("md5_sign", "");
                            String orderSign = data.optString("order_sign");
                            zfbPayResult.setSign(orderSign);
                            zfbPayResult.setZfbMd5Key(md5Sign);
                            zfbPayResult.setOrderInfo(orderInfo);
                            zfbPayResult.setOrderNumber(data.optString("out_trade_no"));
                            String tempmd5 = PaykeyUtil.stringToMD5(orderInfo + OrderInfoUtils.MCH_MD5KEY());
//                            MCLog.e(TAG, "tempmd5 = " + tempmd5 + " ?= " + md5Sign);
                            if (md5Sign.equals(tempmd5)) {
                                String signStr = new String(Base64.decode(orderInfo), "utf-8");
                                zfbPayResult.setOrderInfo(signStr);
                                if (!TextUtils.isEmpty(orderSign)) {
                                    String sign = URLEncoder.encode(orderSign, "UTF-8");
                                    zfbPayResult.setSign(sign);
                                }
                                noticeResult(Constant.ZFB_PAY_VALIDATE_SUCCESS, zfbPayResult);
                            } else {
                                noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, HttpConstants.S_AGjujCzwxl);
                            }
                        }
                    }else if (status == 9999){ //0USD付款时
                        JSONObject data = json.getJSONObject("data");
                        WXOrderInfo wapPayOrderInfo = new WXOrderInfo();
                        wapPayOrderInfo.setTag("zfb");
                        wapPayOrderInfo.setOrderNo(data.optString("out_trade_no"));
                        wapPayOrderInfo.setUrl(data.optString("url"));
                        noticeResult(Constant.ZFB_WAPPAY_ORDERINFO_SUCCESS, wapPayOrderInfo);
                    }else {
                        String msg = json.optString("msg");
                        MCLog.e(TAG, "msg:" + msg);
                        if(isWapPay) {
                            noticeResult(Constant.ZFB_WAPPAY_ORDERINFO_FAIL, msg);
                        }else {
                            noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, msg);
                        }
                    }

                } catch (Exception e) {
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                    if(isWapPay) {
                        noticeResult(Constant.ZFB_WAPPAY_ORDERINFO_FAIL, HttpConstants.S_mJGaHLIGRo);
                    }else{
                        noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, HttpConstants.S_qAOzNcQOLK);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "error" + error.getExceptionCode());

                if(isWapPay) {
                    noticeResult(Constant.ZFB_WAPPAY_ORDERINFO_FAIL, HttpConstants.S_uVIIKmGFwV);
                }else {
                    noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, HttpConstants.S_uVIIKmGFwV);
                }
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
