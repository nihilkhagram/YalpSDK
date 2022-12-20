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
import com.cointizen.paysdk.entity.MsgTZModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MsgTZRequest {

    private static final String TAG = "MsgTZRequest";

    HttpUtils http;
    Handler mHandler;

    public MsgTZRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.MSGTZ_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                MsgTZModel giftDetModel = new MsgTZModel();
                ArrayList<MsgTZModel.ListBean> listBeans = new ArrayList<>();
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {
                        JSONArray list = json.getJSONArray("data");
                        for (int i = 0; i < list.length(); i++){
                            MsgTZModel.ListBean listBean = new MsgTZModel.ListBean();
                            JSONObject jsonObject = list.getJSONObject(i);
                            listBean.setNotice_id(jsonObject.optString("id"));
                            listBean.setTitle(jsonObject.optString("title"));
//                            listBean.setContent(jsonObject.optString("content"));
//                            listBean.setRead(jsonObject.optString("read"));
                            listBean.setCreate_time(jsonObject.optLong("create_time"));
                            listBean.setMsgType(jsonObject.optString("category_id", ""));
                            listBean.setUrl(jsonObject.optString("url", ""));
//                            listBean.setType(jsonObject.optString("type"));
                            listBeans.add(listBean);
                        }
                        giftDetModel.setList(listBeans);
                        noticeResult(Constant.MSGTZ_SUCCESS, giftDetModel);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.MSGTZ_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.MSGTZ_FAIL, "解析参数异常");
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.MSGTZ_FAIL, "Failed to connect to the server");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.MSGTZ_FAIL, "Failed to connect to the server");
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
