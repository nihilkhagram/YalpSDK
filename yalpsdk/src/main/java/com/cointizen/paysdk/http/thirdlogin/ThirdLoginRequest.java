package com.cointizen.paysdk.http.thirdlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.floatview.MCHFloatLoginView;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.cointizen.paysdk.http.HttpConstants;

public class ThirdLoginRequest {

    private static final String TAG = "ThirdLoginRequest";

    HttpUtils http;
    Handler mHandler;
    String account;

    private boolean isYKLogin;

    public void setYKLogin(boolean YKLogin) {
        isYKLogin = YKLogin;
    }

    public ThirdLoginRequest(Handler mHandler) {
        isYKLogin = false;
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params, final Context context) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            return;
        }

        MCLog.e(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                UserLogin loginSuccess = new UserLogin();
                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");

                    if (status == 200) {
                        JSONObject data = json.getJSONObject("data");
                        loginSuccess.setLoginStatus("1");
                        loginSuccess.setAccountUserId(data.optString("user_id"));
                        loginSuccess.setToken(data.optString("token"));
                        loginSuccess.setExtra_param(data.optString("extra_param"));
                        loginSuccess.setLoginMsg(HttpConstants.S_MCFAxoGZir);
                        loginSuccess.setUserName(data.optString("account"));
                        loginSuccess.setPassword(data.optString("password"));
                        loginSuccess.setWebURL(data.optString("url"));
                        loginSuccess.setNickName(data.optString("nickname"));
                        loginSuccess.setUserIcon(data.optString("head_img"));

                        UserLoginSession.getInstance().getChannelAndGame().setHead_img(data.optString("head_img"));
                        UserLoginSession.getInstance().getChannelAndGame().setSex(data.optInt("sex"));
                        loginSuccess.setSite_status(data.optInt("site_status"));
                        loginSuccess.setIsOpenSmallAccount(data.optInt("is_open_small_account"));
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
                        loginSuccess.setYKLogin(isYKLogin);
                        SharedPreferencesUtils.getInstance().setLoggedOut(context,false);     //把上次注销状态  改为未注销
                        if(isYKLogin){
                            Constant.LoginType = 3;
                            //保存账号密码  用于自动登录
                            if (!TextUtils.isEmpty(loginSuccess.getPassword())){
                                SharedPreferencesUtils.getInstance().setYKPassword(context,loginSuccess.getPassword());
                            }
//                            AutoAccountInfo autoAccountInfo = new AutoAccountInfo();
//                            autoAccountInfo.setGameid(SdkDomain.getInstance().getGameId());
//                            autoAccountInfo.setAccount(loginSuccess.getUserName());
//                            autoAccountInfo.setPass(SharedPreferencesUtils.getInstance().getYKPassword(context));
//                            autoAccountInfo.setTime(System.currentTimeMillis());
//                            FileAccountUtils.getInstance().setAccountData(autoAccountInfo);
                            SharedPreferencesUtils.getInstance().setLoginAccount(context,loginSuccess.getUserName());
                            SharedPreferencesUtils.getInstance().setLoginPassword(context,SharedPreferencesUtils.getInstance().getYKPassword(context));

                            Intent intent = new Intent();
                            intent.putExtra("account",loginSuccess.getUserName());
                            intent.putExtra("password",loginSuccess.getPassword());
                            intent.putExtra("SmallAccount",loginSuccess);
                            intent.setAction("isYKLogin");
                            context.sendBroadcast(intent);
                            SharedPreferencesUtils.getInstance().setIsThreeLogin(context,false);
                        }else{
                            SharedPreferencesUtils.getInstance().setIsThreeLogin(context,true);
                        }

                        MCHFloatLoginView.getInstance(YalpGamesSdk.getMainActivity(),1000, loginSuccess.getUserName(),false).show();
//                        if(SharedPreferencesUtils.getInstance().getLaunch(context)){
//                            LaunchProcess launchProcess = new LaunchProcess(context, loginSuccess.getAccountUserId());
//                            launchProcess.post();
//                        }

                        noticeResult(Constant.USER_THIRD_PARAMS_SUCCESS, loginSuccess);
                    } else {
                        loginSuccess.setLoginStatus("-1");
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.USER_THIRD_PARAMS_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.USER_THIRD_PARAMS_FAIL, HttpConstants.S_bcwiMnfyeK+e.toString());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure code =" + error.getExceptionCode() + " message =" + msg);
                noticeResult(Constant.USER_THIRD_PARAMS_FAIL, HttpConstants.S_SsuiVZVAek);
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
