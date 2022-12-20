package com.cointizen.paysdk.http.request;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.RequestUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.utils.ConfigureApp;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.utils.VerifyCodeCookisStore;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import com.cointizen.paysdk.http.HttpConstants;

public class RegisterRequest {

    private static final String TAG = "RegisterRequest";

    HttpUtils http;
    Handler mHandler;
    String mUserName;
    String mPassword;

    public RegisterRequest(Handler mHandler, String userName, String password) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
        if (!TextUtils.isEmpty(userName)) {
            this.mUserName = userName;
        } else {
            this.mUserName = "";
        }
        if (!TextUtils.isEmpty(password)) {
            this.mPassword = password;
        } else {
            this.mPassword = "";
        }
    }


    public void post(String url, RequestParams params, final Context context) {
        if(!ConfigureApp.getInstance().Configure(context)){
            return;
        }
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.REGISTER_FAIL, HttpConstants.S_kxlRBKkxSP);
            return;
        }
        MCLog.e(TAG, "fun#post url = " + url);
        http.configCookieStore(VerifyCodeCookisStore.cookieStore);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response=RequestUtil.getResponse( responseInfo);
                MCLog.e(TAG, "msg:" + response);
                UserLogin loginSuccess = new UserLogin();
                loginSuccess.setPassword(mPassword);

                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status=json.optInt("code");
                    if(status==200){
                        JSONObject data = json.getJSONObject("data");
                        loginSuccess.setLoginStatus("1");
                        loginSuccess.setLoginMsg(json.optString("msg"));
                        loginSuccess.setAccountUserId(data.optString("user_id"));
                        loginSuccess.setToken(data.optString("token"));
                        loginSuccess.setExtra_param(data.optString("extra_param"));
                        loginSuccess.setIsOpenSmallAccount(data.optInt("is_open_small_account"));
                        loginSuccess.setUserName(data.optString("account", ""));
                        JSONArray listSmallAccount = data.optJSONArray("small_list");
                        List<UserLogin.SmallAccountEntity> list = new ArrayList<>();
                        for (int i=0; i<listSmallAccount.length(); i++){
                            JSONObject smallAccount = (JSONObject) listSmallAccount.get(i);
                            UserLogin.SmallAccountEntity smallAccountEntity = new UserLogin.SmallAccountEntity();
                            smallAccountEntity.setSmallNickname(smallAccount.optString("nickname"));
                            smallAccountEntity.setSmallUserId(smallAccount.optString("small_id"));
                            list.add(smallAccountEntity);
                        }
                        loginSuccess.setSmallAccountList(list);

                        //TODO  保存账号自动登录====start
                        SharedPreferencesUtils.getInstance().setLoginAccount(context,mUserName);
                        SharedPreferencesUtils.getInstance().setLoginPassword(context,mPassword);
                        //TODO  保存账号自动登录====end
                        SharedPreferencesUtils.getInstance().setLoggedOut(context,false);     //把上次注销状态  改为未注销
                        SharedPreferencesUtils.getInstance().setIsThreeLogin(context,false);

                        noticeResult(Constant.REGISTER_SUCCESS, loginSuccess);
                    }else{
                        String msg = json.optString("msg");
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.REGISTER_FAIL, msg);
                    }
                }  catch (Exception e) {
                    MCLog.e(TAG, e.toString());
                    noticeResult(Constant.REGISTER_FAIL, HttpConstants.S_bcwiMnfyeK);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "onFailure" + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + error.getLocalizedMessage());
                MCLog.e(TAG, "onFailure" + error.getMessage());
                MCLog.e(TAG, "onFailure" + error.getStackTrace());
                noticeResult(Constant.REGISTER_FAIL, HttpConstants.S_uVIIKmGFwV);
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
