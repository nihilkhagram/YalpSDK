package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.UpdateVisitorInfoRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhujinzhujin
 * on 2017/1/17.
 */

public class UpdateVisitorInfoProcess {

    private static final String TAG = "UpdateVisitorInfoProcess";

    private String account;
    private String pwd;


    private String getParamStr(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("password", pwd);
        map.put("game_id", SdkDomain.getInstance().getGameId());
        map.put("code_type", "account");
        map.put("user_id", UserLoginSession.getInstance().getUserId());
        MCLog.w(TAG, "fun#ptb_pay params:" + map.toString());

        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(getParamStr().toString()));
        } catch (UnsupportedEncodingException e) {
            params = null;
            MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
        }

        if (null != handler && null != params) {
            UpdateVisitorInfoRequest request = new UpdateVisitorInfoRequest(handler);
            request.post(MCHConstant.getInstance().getUpdateUserInfoUrl(), params);
        } else {
            MCLog.e(TAG, "fun#post handler is null or url is null");
        }
    }


    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
