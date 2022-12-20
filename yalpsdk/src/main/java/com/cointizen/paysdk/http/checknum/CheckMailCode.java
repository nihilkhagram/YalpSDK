package com.cointizen.paysdk.http.checknum;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.VerifyCodeCookisStore;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证邮箱验证码
 */
public class CheckMailCode {

    private HttpUtils http;
    public Context context;

    /**
     * 验证手机验证码是否正确
     * @param phonecode 手机验证码
     * @return
     */
    public void postIsMailCode(String phonecode, String email, RequestCallBack<String> request){
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", phonecode);
        map.put("email",email);
        map.put("game_id", SdkDomain.getInstance().getGameId());
        String url = MCHConstant.getInstance().getCheckMailCode();
        post(2, url, map, request);
    }

    public void post(int code, String url, Map<String, String> map,
                     RequestCallBack<String> callBack) {
        String strParams = RequestParamUtil.getRequestParamString(map, url);
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(strParams));
        } catch (UnsupportedEncodingException e) {
        }

        HttpRequest.HttpMethod httpMethod = HttpRequest.HttpMethod.POST;
        http = new HttpUtils();
        if(null != VerifyCodeCookisStore.cookieStore){
            http.configCookieStore(VerifyCodeCookisStore.cookieStore);
            MCLog.e("1", "fun#post cookieStore not null");
        }else{
            MCLog.e("2", "fun#post cookieStore is null");
        }
        http.send(httpMethod, url, params, callBack);
    }
}
