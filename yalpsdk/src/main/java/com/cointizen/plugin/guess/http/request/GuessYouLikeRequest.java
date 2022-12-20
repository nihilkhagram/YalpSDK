package com.cointizen.plugin.guess.http.request;

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
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.ConfigureApp;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.plugin.guess.bean.GuessYouLikeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GuessYouLikeRequest {

    private static final String TAG = "GuessYouLikeRequest";

    HttpUtils http;
    Handler mHandler;

    public GuessYouLikeRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params, Context mcUserCenterActivity) {
        if(!ConfigureApp.getInstance().Configure(mcUserCenterActivity)){
            return;
        }
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.GUESS_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                GuessYouLikeModel giftDetModel = new GuessYouLikeModel();
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {
                        JSONArray list = json.getJSONArray("data");
                        ArrayList<GuessYouLikeModel.ListBean> listBeans = new ArrayList<>();
                        for (int i = 0;i<list.length();i++){
                            JSONObject jsonObject = list.getJSONObject(i);
                            GuessYouLikeModel.ListBean listBean = new GuessYouLikeModel.ListBean();
                            listBean.setUrl(jsonObject.optString("url"));
                            listBean.setIcon(jsonObject.optString("icon"));
                            listBean.setTitle(jsonObject.optString("title"));
                            listBeans.add(listBean);
                        }
                        giftDetModel.setList(listBeans);
                        noticeResult(Constant.GUESS_SUCCESS, giftDetModel);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.GUESS_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.GUESS_FAIL, "解析参数异常");
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.GUESS_FAIL, "Failed to connect to the server");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.GUESS_FAIL, "Failed to connect to the server");
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
