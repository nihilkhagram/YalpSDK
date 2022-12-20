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
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.cointizen.paysdk.http.HttpConstants;

/**
 * 描述：获取小号列表Request
 * 时间: 2019-12-23 9:50
 */
public class SmallAccountListRequest {
    private String TAG = "SmallAccountListRequest";
    HttpUtils http;
    Handler mHandler;

    public SmallAccountListRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.SMALLACCOUNT_LIST_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                int status;
                String tip = "";
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    tip = json.optString("msg");

                    if (status == 200 || status == 1) {
                        JSONArray jsonArray = json.optJSONArray("data");
                        if (jsonArray.length()>0){
                            List<UserLogin.SmallAccountEntity> smallAccountList = new ArrayList<>();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonData = (JSONObject) jsonArray.get(i);
                                UserLogin.SmallAccountEntity entity = new UserLogin.SmallAccountEntity();
                                entity.setSmallNickname(jsonData.optString("nickname"));
                                entity.setSmallUserId(jsonData.optString("small_id"));
                                entity.setSmallAccount(jsonData.optString("account"));
                                entity.setSmallToken(jsonData.optString("token"));
                                smallAccountList.add(entity);
                            }
                            noticeResult(Constant.SMALLACCOUNT_LIST_SUCCESS, smallAccountList);
                        }else {
                            noticeResult(Constant.SMALLACCOUNT_LIST_FAIL, HttpConstants.S_veerDACAyz);
                        }
                    } else {
                        noticeResult(Constant.SMALLACCOUNT_LIST_FAIL, tip);
                    }

                } catch (Exception e) {
                    MCLog.e(TAG, HttpConstants.Log_wHcpGXdyYM + e);
                    noticeResult(Constant.SMALLACCOUNT_LIST_FAIL, HttpConstants.S_sCXXHBqPCl);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                noticeResult(Constant.SMALLACCOUNT_LIST_FAIL, HttpConstants.S_uVIIKmGFwV);
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
