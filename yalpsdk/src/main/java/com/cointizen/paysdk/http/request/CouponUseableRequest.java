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
import com.cointizen.paysdk.bean.CouponBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：可用代金券列表
 * 时间: 2019-12-06 9:26
 */

public class CouponUseableRequest {
    private static final String TAG = "CouponUseableRequest";
    Handler mHandler;
    HttpUtils http;

    public CouponUseableRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.USABLE_COUPON_FAIL, "参数为空");
            return;
        }

        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);
                int status = -1;
                try {
                    JSONObject obj = new JSONObject(response);
                    status = obj.getInt("code");
                    if (status == 200 || status == 1) {
                        JSONArray jsonData = obj.getJSONArray("data");

                        List<CouponBean> listData = new ArrayList<>();
                        if (jsonData!=null && jsonData.length()>0){
                            for (int i=0;i<jsonData.length();i++){
                                JSONObject unReceiveData = (JSONObject) jsonData.get(i);
                                CouponBean bean = new CouponBean();
                                bean.setStatusType(2);  //已领取
                                bean.setId(unReceiveData.optString("id"));
                                bean.setCoupon_name(unReceiveData.optString("coupon_name"));
                                bean.setStart_time(unReceiveData.optString("start_time"));
                                bean.setEnd_time(unReceiveData.optString("end_time"));
                                bean.setLimit_money(unReceiveData.optString("limit_money"));
                                bean.setMoney(unReceiveData.optString("money"));
                                listData.add(bean);
                            }
                        }

                        noticeResult(Constant.USABLE_COUPON_SUCCESS, listData);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(obj.optString("msg"))) {
                            msg = obj.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.USABLE_COUPON_FAIL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    noticeResult(Constant.USABLE_COUPON_FAIL, "解析数据异常");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure:" + msg);
                MCLog.e(TAG, "onFailure:" + error.getExceptionCode() + error.getStackTrace() + error.getMessage());
                noticeResult(Constant.USABLE_COUPON_FAIL, "Failed to connect to the server");
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

