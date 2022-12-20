package com.cointizen.paysdk.http.request;

import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserDiscountEntity;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhujinzhujin
 * on 2017/1/12.
 */

public class UserDiscountRequest {

    private static final String TAG = "UserDiscountRequest";

    HttpUtils http;
    Handler mHandler;

    public UserDiscountRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.GET_USER_DISCOUNT_FAIL, "参数为空");
            return;
        }
        MCLog.w(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>(){
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse( responseInfo);

                MCLog.w(TAG,"response"+response);
                if(TextUtils.isEmpty(response)){
                    noticeResult(Constant.GET_USER_DISCOUNT_FAIL, "参数为空");
                }else {
                    UserDiscountEntity userDiscount = new UserDiscountEntity();
                    try {
                        JSONObject json = new JSONObject(response);
                        int code = json.optInt("code");
                        if (code == 200){
                            JSONObject jsonData = json.optJSONObject("data");
                            userDiscount.setDiscountType(jsonData.optInt("discount_type", 0));
                            userDiscount.setDiscountNum(jsonData.optString("discount","10" ));
                        }
                    } catch (JSONException e) {
                        MCLog.e(TAG, "JSONException:" + e.toString());
                    }
                    noticeResult(Constant.GET_USER_DISCOUNT_SUCCESS, userDiscount);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                noticeResult(Constant.GET_USER_DISCOUNT_FAIL, "Failed to connect to the server");
            }
        });
    }


    private void noticeResult(int type, Object object){
        Message msg = new Message();
        msg.what = type;
        msg.obj = object;

        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
