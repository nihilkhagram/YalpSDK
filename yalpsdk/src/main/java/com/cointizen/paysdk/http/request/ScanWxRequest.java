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
import com.cointizen.paysdk.bean.ScanRQWxBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONObject;

public class ScanWxRequest {

    private static final String TAG = "ScanWxRequest";

    HttpUtils http;
    Handler mHandler;

    public ScanWxRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.SCAN_WX_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String response = RequestUtil.getResponse(responseInfo);
                        try {
                            JSONObject json = new JSONObject(response);
                            int status = json.optInt("code");
                            if (status == 200) {
                                JSONObject jsonData = json.optJSONObject("data");
                                ScanRQWxBean scanRQWxBean = new ScanRQWxBean();
                                scanRQWxBean.setPaytype(jsonData.optString("paytype"));
                                scanRQWxBean.setOrderno(jsonData.optString("orderno"));
                                scanRQWxBean.setQrcode_url(jsonData.optString("qrcode_url"));

                                noticeResult(Constant.SCAN_WX_SUCCESS, scanRQWxBean);
                            } else {
                                String msg;
                                if (!TextUtils.isEmpty(json.optString("msg"))) {
                                    msg = json.optString("msg");
                                } else {
                                    msg = MCErrorCodeUtils.getErrorMsg(status);
                                }
                                MCLog.e(TAG, "msg:" + msg);
                                noticeResult(Constant.SCAN_WX_FAIL, msg);
                            }
                        } catch (Exception e) {
                            noticeResult(Constant.SCAN_WX_FAIL, "数据解析异常");
                            MCLog.e(TAG, "fun#post JSONException:" + e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MCLog.e(TAG, "[onFailure] ExceptionCode:" + error.getExceptionCode());
                        MCLog.e(TAG, "onFailure" + msg);
                        noticeResult(Constant.SCAN_WX_FAIL, "Failed to connect to the server");
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
