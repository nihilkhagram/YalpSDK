package com.cointizen.paysdk.http.switchinfo;

import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.bean.SwitchManager;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SwitchInfoRequest {

    private static final String TAG = "SwitchInfoRequest";

    HttpUtils http;
    Handler mHandler;

    public SwitchInfoRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.THIRD_LOGIN_TYPE_FAIL, "参数异常");
            return;
        }
        MCLog.w(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);

                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");
                    if(status == 200){
                        JSONObject data = json.getJSONObject("data");

                        SwitchManager.getInstance().setAccountRegister(data.optString("account_register_switch", "1"));
                        SwitchManager.getInstance().setPhoneRegister(data.optString("phonenum_register_switch", "1"));
                        SwitchManager.getInstance().setEmailRegister(data.optString("email_register_switch", "1"));
                        SwitchManager.getInstance().setVipLevel(data.optString("vip_level_show_switch"));
                        SwitchManager.getInstance().setFloatingNetwork(data.optString("network_speed_show_switch"));
                        SwitchManager.getInstance().setShare(data.optString("share_entrance_switch"));
                        SwitchManager.getInstance().setFloating(data.optString("suspend_show_status", "1"));
                        SwitchManager.getInstance().setFloatingIconUrl(data.optString("suspend_icon"));
                        SwitchManager.getInstance().setLogoutAdvert(data.optString("logout_adv_show_switch", "1"));
                        SwitchManager.getInstance().setPtbAdd(data.optString("ptb_recharge_switch", "1"));
                        SwitchManager.getInstance().setChangeAccount(data.optString("loginout_status", "1"));
                    }

                } catch (JSONException e) {
                }

                noticeResult(Constant.THIRD_LOGIN_TYPE_SUCCESS, "");

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.THIRD_LOGIN_TYPE_FAIL, "Failed to connect to the server");
            }
        });
    }

    private void noticeResult(int type, String str) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = str;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }

}
