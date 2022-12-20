package com.cointizen.plugin.vip.http.request;

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
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.plugin.vip.bean.McVipBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.cointizen.plugin.AppConstants;

public class VipRequest {

    private static final String TAG = "VipRequest";

    HttpUtils http;
    Handler mHandler;

    public VipRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.VIP_FAIL, AppConstants.STR_906262736165d3c7cb57669a5eac37b4);
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                MCLog.e(TAG,"vip:"+response);
                McVipBean mcVipBean = new McVipBean();
                List<String> listData = new ArrayList<>();
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200 ) {
                        JSONArray arrarData = json.getJSONArray("data");
                      if (arrarData!=null && arrarData.length()>0){
                          for (int i=0;i<arrarData.length();i++){
                              listData.add((String) arrarData.get(i));
                          }
                      }
                        noticeResult(Constant.VIP_SUCCESS, listData);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.VIP_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.VIP_FAIL, AppConstants.STR_0177fa60c54cb1ab7f53f90969dd219d);
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.VIP_FAIL, AppConstants.STR_44ed625b1914f08d820407a2b413dba6);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.VIP_FAIL, AppConstants.STR_44ed625b1914f08d820407a2b413dba6);
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
