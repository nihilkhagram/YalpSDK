package com.cointizen.paysdk.http.request;

import org.json.JSONObject;

import com.cointizen.paysdk.entity.ChannelAndGameInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class UserInfoRequest {

    private static final String TAG = "UserInfoRequest";

    HttpUtils http;
    Handler mHandler;

    public UserInfoRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.GET_USER_INFO_FAIL, null, "参数为空");
            return;
        }
        http.send(HttpRequest.HttpMethod.POST, url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String response = RequestUtil.getResponse(responseInfo);
                        Log.e(TAG, "json###" + response);
                        ChannelAndGameInfo info = new ChannelAndGameInfo();
                        try {
                            JSONObject json = new JSONObject(response);
                            int status = json.optInt("code");
                            if (status == 200) {
                                JSONObject data = json.getJSONObject("data");
                                String gamename = data.optString("game_name", "");
                                if (TextUtils.isEmpty(gamename) || "null".equals(gamename)) {
                                    gamename = "";
                                }
                                String phone = data.optString("phone", "").trim();
                                if (TextUtils.isEmpty(phone) || "null".equals(phone)) {
                                    phone = "";
                                }
//                                float bindPtb = stringToFloat(data.optString("bind_balance", ""));
                                float ptb = stringToFloat(data.optString("balance", ""));
                                info.setAccount(data.optString("account", ""));
                                info.setNikeName(data.optString("nickname", ""));
                                info.setAge_status(data.optString("age_status", "0"));
                                info.setReal_name(data.optString("real_name", ""));
                                info.setIdcard(data.optString("idcard", ""));
                                info.seteMail(data.optString("email", ""));
                                info.setVip_level(data.optInt("vip_level", 0));
                                info.setNext_vip(data.optString("next_vip",""));
                                info.setHead_img(data.optString("head_img", ""));
                                info.setThird_authentication(data.optString("third_authentication", ""));
                                info.setPoint(data.optString("point", ""));
//                                info.setGold_coin(data.optString("gold_coin", ""));
                                info.setUserRegisteType(data.optInt("register_type", -1));
                                info.setPlatformMoney(ptb);
                                Constant.count = data.optInt("read_status");
//                                Constant.request_count = data.optInt("request_count");
//                                Constant.notice_count = data.optInt("notice_count");
                                info.setPhoneNumber(phone);
//                                info.setBindPtbMoney(bindPtb);
                                info.setGameName(gamename);
                                info.setId(data.optString("id", ""));
                                info.setSex(data.optInt("sex", 0));
                                PrivacyManager.getInstance().isLogout = "1".equals(data.optString("is_unsubscribe", "0"));//0:未注销 1:已注销

                                if (Constant.MCH_BACKGROUND_VERSION >= Constant.VERSION_840){
                                    JSONObject jsonSign = data.getJSONObject("sign_detail");
                                    info.setSign_status(jsonSign.optInt("sign_status"));
                                    info.setToday_signed(jsonSign.optInt("today_signed"));

                                }

                                noticeResult(Constant.GET_USER_INFO_SUCCESS, info, "");
                            } else {
                                String msg = "";
                                if (json!=null){
                                    msg = json.optString("msg");
                                }
                                MCLog.e(TAG, "msg:" + msg);
                                if (TextUtils.isEmpty(msg)) {
                                    msg = "服务器异常";
                                }
                                noticeResult(Constant.GET_USER_INFO_FAIL, null, msg);
                            }
                        } catch (Exception e) {
                            noticeResult(Constant.GET_USER_INFO_FAIL, null, "数据解析异常");
                            MCLog.e(TAG, "fun#get json e = " + e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                        MCLog.e(TAG, "onFailure" + msg);
                        noticeResult(Constant.GET_USER_INFO_FAIL, null, "Failed to connect to the server");
                    }
                });
    }

    protected float stringToFloat(String optString) {
        if (TextUtils.isEmpty(optString)) {
            return 0.00f;
        }
        float money;
        try {
            money = Float.parseFloat(optString);
            if(money==0){
                money=0.00f;
            }
        } catch (NumberFormatException e) {
            money = 0.00f;
        } catch (Exception e) {
            money = 0.00f;
        }

        return money;
    }

    private void noticeResult(int type, ChannelAndGameInfo info, String res) {
        Message msg = new Message();
        msg.what = type;
        if (type == Constant.GET_USER_INFO_FAIL) {
            msg.obj = res;
        } else {
            msg.obj = info;
        }

        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
