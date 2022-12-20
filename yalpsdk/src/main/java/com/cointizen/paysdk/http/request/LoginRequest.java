package com.cointizen.paysdk.http.request;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.floatview.MCHFloatLoginView;
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

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.cointizen.paysdk.http.HttpConstants;

public class LoginRequest {
    public static boolean LOGIN_NETWORK_ERROR;
    private static final String TAG = "LoginRequest";

    HttpUtils http;
    Handler mHandler;
    String mUserName;
    String mPassword;

    public LoginRequest(Handler mHandler, String userName, String password) {
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

    public void post(final String url, RequestParams params, final Context context) {
        LOGIN_NETWORK_ERROR = false;
        if (!ConfigureApp.getInstance().Configure(context)) {
            return;
        }
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.LOGIN_FAIL, HttpConstants.S_VKHKJPHQds + params + "===========" + url);
            return;
        }
        MCHFloatLoginView.getInstance(YalpGamesSdk.getMainActivity(), 1000, mUserName, false).show();
        MCLog.w(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);
                MCLog.e(TAG, "msg:" + response);
                UserLogin loginSuccess = new UserLogin();
                loginSuccess.setPassword(mPassword);

                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {
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
                        for (int i = 0; i < listSmallAccount.length(); i++) {
                            JSONObject smallAccount = (JSONObject) listSmallAccount.get(i);
                            UserLogin.SmallAccountEntity smallAccountEntity = new UserLogin.SmallAccountEntity();
                            smallAccountEntity.setSmallNickname(smallAccount.optString("nickname"));
                            smallAccountEntity.setSmallUserId(smallAccount.optString("small_id"));
                            list.add(smallAccountEntity);
                        }
                        loginSuccess.setSmallAccountList(list);
                        Log.e("MyLogData  ", "getLoginAccount  login activity " + loginSuccess.getToken());
                        Log.e("MyLogData  ", "getLoginAccount  UserId " + loginSuccess.getAccountUserId());

                        //TODO  保存账号自动登录====start
                        SharedPreferencesUtils.getInstance().setLoginAccount(context, mUserName);
                        SharedPreferencesUtils.getInstance().setLoginPassword(context, mPassword);
                        SharedPreferencesUtils.getInstance().setToken(context, loginSuccess.getAccountUserId());
                        //TODO  保存账号自动登录====end
                        SharedPreferencesUtils.getInstance().setLoggedOut(context, false);     //把上次注销状态  改为未注销
                        SharedPreferencesUtils.getInstance().setIsThreeLogin(context, false);

                        noticeResult(Constant.LOGIN_SUCCESS, loginSuccess);
                    } else {
                        String msg = json.optString("msg");
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.LOGIN_FAIL, msg);
                    }
                } catch (Exception e) {
                    MCLog.e(TAG, e.toString());
                    noticeResult(Constant.LOGIN_FAIL, HttpConstants.S_bcwiMnfyeK);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "onFailure" + msg);
                MCLog.e(TAG, "onFailure" + error.getExceptionCode() + error.getStackTrace() + error.getMessage());

//				reLogin(mUserName,mPassword,context);
            }
        });
    }

//	private void reLogin(String account,String password,Context context){
//		if (LOGIN_NETWORK_ERROR){ //备用域名访问也失败，则登录失败
//			MCLog.e(TAG,HttpConstants.Log_apZzGBiJvO);
//			noticeResult(Constant.LOGIN_FAIL, HttpConstants.S_uVIIKmGFwV);
//			return;
//		}
//
//		String str = oldUrl.substring(oldUrl.indexOf("://") + 3,oldUrl.indexOf(".")); //需要被替换的字符
//		String backUrl = oldUrl.replace(str,"sdkbak"); //替换为sdkbak
//		MCLog.e(TAG,HttpConstants.Log_kGlOmvRykD + backUrl);
//
//		MCHConstant.getInstance().setIpAddress(backUrl);
//		MCHConstant.getInstance().initUrlInfo();
//
//		MCHLoginActivity loginActivity = new MCHLoginActivity(context);
//		loginActivity.startUserLogin(account,password,(Activity) context,true);
//
//		LOGIN_NETWORK_ERROR = true;
//	}

    private void noticeResult(int type, Object obj) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = obj;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
