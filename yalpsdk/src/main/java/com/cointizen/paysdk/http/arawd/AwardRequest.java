package com.cointizen.paysdk.http.arawd;

import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.AwardEntity;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AwardRequest {

    private static final String TAG = "SwitchInfoRequest";

    HttpUtils http;
    Handler mHandler;

    public AwardRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params, final boolean isRegister) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.HTTP_REQUEST_FAIL, "参数异常");
            return;
        }
        MCLog.w(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);
                AwardEntity awardEntity = new AwardEntity();
                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");
                    if(status == 200){
                        JSONObject data = json.getJSONObject("data");
                        String openStatus;
                        JSONArray imgJson;
                        if (isRegister) {
                            openStatus = data.optString("register_award_switch", "0");
                            imgJson = data.optJSONArray("register_award_pic");
                        }else {
                            openStatus = data.optString("real_name_auth_award_switch", "0");
                            imgJson = data.optJSONArray("real_name_auth_award_pic");
                        }
                        awardEntity.isOpen = "1".equals(openStatus);
                        List<String> images = new ArrayList<>();
                        if (imgJson != null) {
                            for (int i = 0; i < imgJson.length(); i++) {
                                images.add(imgJson.get(i).toString());
                            }
                            awardEntity.imageUrlList = images;
                        }

                        noticeResult(Constant.AWARD_SUCCESS, awardEntity);
                    }else {
                        String tip = json.optString("msg");
                        MCLog.e(TAG, "tip:" + tip);
                        noticeResult(Constant.HTTP_REQUEST_FAIL, tip);
                    }

                } catch (JSONException e) {
                    noticeResult(Constant.HTTP_REQUEST_FAIL, "数据解析异常");
                }



            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.HTTP_REQUEST_FAIL, "Failed to connect to the server");
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
