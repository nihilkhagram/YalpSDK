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
 * 描述：添加小号Request
 * 时间: 2018-07-17 9:50
 */
public class AddSmallAccountRequest {
    private String TAG = "AddSmallAccountRequest";
    HttpUtils http;
    Handler mHandler;

    public AddSmallAccountRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.ADD_SMALL_ACCOUNT_FAIL, HttpConstants.S_ElwWiRoGqB);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            UserLogin loginSuccess = new UserLogin();
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                int status;
                String tip = "";
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    tip = json.optString("msg");
                    MCLog.e(TAG, "tip:" + tip);

                    if (status == 200) {
                        JSONArray listSmallAccount = json.getJSONArray("data");
                        List<UserLogin.SmallAccountEntity> list = new ArrayList<>();
                        for (int i = 0; i < listSmallAccount.length(); i++) {
                            JSONObject smallAccount = (JSONObject) listSmallAccount.get(i);
                            UserLogin.SmallAccountEntity smallAccountEntity = new UserLogin.SmallAccountEntity();
                            smallAccountEntity.setSmallAccount(smallAccount.optString("account"));
                            smallAccountEntity.setSmallNickname(smallAccount.optString("nickname"));
                            smallAccountEntity.setSmallUserId(smallAccount.optString("small_id"));
                            list.add(smallAccountEntity);
                        }
                        loginSuccess.setSmallAccountList(list);
                        noticeResult(Constant.ADD_SMALL_ACCOUNT_SUCCESS, loginSuccess);
                    } else {
                        noticeResult(Constant.ADD_SMALL_ACCOUNT_FAIL, tip);
                    }

                } catch (Exception e) {
                    MCLog.e(TAG, HttpConstants.Log_wTyZBtnkbi + e);
                    noticeResult(Constant.ADD_SMALL_ACCOUNT_FAIL, HttpConstants.S_wfVNuKWdda);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                noticeResult(Constant.ADD_SMALL_ACCOUNT_FAIL, HttpConstants.S_uVIIKmGFwV);
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
