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

public class CouponAllRequest {
    private static final String TAG = "CouponAllRequest";
    Handler mHandler;
    HttpUtils http;

    private int type = 0;  // 1可领取  2已领取 3已失效

    public void setType(int type) {
        this.type = type;
    }

    public CouponAllRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.ALL_COUPON_FAIL, "参数为空");
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
                        JSONArray jsonArray = obj.getJSONArray("data");

                        List<CouponBean> listData = new ArrayList<>();
                        if (jsonArray!=null && jsonArray.length()>0){
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonData = (JSONObject) jsonArray.get(i);
                                CouponBean bean = new CouponBean();
                                if (type!=3){
                                    bean.setStatusType(type);
                                }else {
                                    if (jsonData.optInt("status") == 1){
                                        bean.setStatusType(3); //已使用
                                    }else {
                                        bean.setStatusType(4); //已过期
                                    }
                                }
                                bean.setId(jsonData.optString("id"));
                                bean.setCoupon_name(jsonData.optString("coupon_name"));
                                bean.setStart_time(jsonData.optString("start_time"));
                                bean.setEnd_time(jsonData.optString("end_time"));
                                bean.setLimit_money(jsonData.optString("limit_money"));
                                bean.setMoney(jsonData.optString("money"));
                                bean.setLimit(jsonData.optInt("limit"));
                                bean.setLimit_num(jsonData.optInt("limit_num"));
                                listData.add(bean);
                            }
                        }

                        noticeResult(Constant.ALL_COUPON_SUCCESS, listData);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(obj.optString("msg"))) {
                            msg = obj.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.ALL_COUPON_FAIL, msg);
                    }
                } catch (JSONException e) {
                    MCLog.e(TAG, e.toString());
                    noticeResult(Constant.ALL_COUPON_FAIL, "解析数据异常");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure:" + msg);
                MCLog.e(TAG, "onFailure:" + error.getExceptionCode() + error.getStackTrace() + error.getMessage());
                noticeResult(Constant.ALL_COUPON_FAIL, "Failed to connect to the server");
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

