package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.SmallAccountLoginRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：小号登录
 * 时间: 2018-07-17 16:03
 */
public class SmallAccountLoginProgress {
    private String TAG = "SmallAccountLoginProgress";
    private String user_id;  //平台账号用户ID
    private String small_id;  //小号的用户ID
    private String game_id; //游戏ID
    private String token; //大号token
    private boolean isYKLogin;  //是否是游客登录

    public void setYKLogin(boolean YKLogin) {
        isYKLogin = YKLogin;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSmallUserId(String small_id) {
        this.small_id = small_id;
    }

    public void setGameId(String game_id) {
        this.game_id = game_id;
    }

    private String getParamStr(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("small_id", small_id);
        map.put("game_id", game_id);
        map.put("token",token);
        MCLog.e(TAG, "fun#SmallAccountLogin params:" + map.toString());

        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler){

        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(getParamStr()));
        } catch (UnsupportedEncodingException e) {
            params = null;
            MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
        }

        if (null != handler && null != params) {
            SmallAccountLoginRequest smallAccountLoginRequest = new SmallAccountLoginRequest(handler,isYKLogin);
            smallAccountLoginRequest.post(MCHConstant.getInstance().getSmallAccountLoginURL(), params);
        }else{
            MCLog.e(TAG, "fun#post handler is null or url is null");
        }
    }
}
