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
import com.cointizen.paysdk.bean.JFDHPtbBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JfDHJLRequest {

    private static final String TAG = "JfDHJLRequest";

    HttpUtils http;
    Handler mHandler;

    public JfDHJLRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.JFDHJL_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    JFDHPtbBean jfdhPtbBean = new JFDHPtbBean();
                    if (status == 200) {
                        JSONArray data = json.getJSONArray("data");
                        ArrayList<JFDHPtbBean.DataBean> dataBeans = new ArrayList<>();
                        for (int i = 0;i<data.length();i++){
                            JSONObject jsonObject = data.getJSONObject(i);
                            JFDHPtbBean.DataBean dataBean = new JFDHPtbBean.DataBean();
                            dataBean.setCount(jsonObject.optString("count",""));
                            dataBean.setId(jsonObject.optString("id",""));
                            dataBean.setGood_name(jsonObject.optString("good_name",""));
                            dataBean.setGood_type(jsonObject.optString("good_type",""));
                            dataBean.setNumber(jsonObject.optString("number",""));
                            dataBean.setPay_amount(jsonObject.optString("pay_amount",""));
                            dataBean.setCreate_time(jsonObject.optString("create_time",""));
                            dataBeans.add(dataBean);
                        }
                        jfdhPtbBean.setData(dataBeans);
                        noticeResult(Constant.JFDHJL_SUCCESS, jfdhPtbBean);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.JFDHJL_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.JFDHJL_FAIL, "解析参数异常");
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.JFDHJL_FAIL, "Failed to connect to the server");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.JFDHJL_FAIL, "Failed to connect to the server");
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
