package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.AddSmallAccountRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：添加小号Process
 * 时间: 2018-07-17 9:44
 */
public class AddSmallAccountProgress {
    private String TAG  = "AddSmallAccountProgress";
    private String user_id;  //平台账号用户ID
    private String smale_nickname;  //添加的小号帐号
    private String game_id; //游戏ID


    public void setGameId(String game_id) {
        this.game_id = game_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setSmaleNickname(String smale_nickname) {
        this.smale_nickname = smale_nickname;
    }

    private String getParamStr(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("nickname", smale_nickname);
        map.put("game_id", game_id);
        MCLog.e(TAG, "fun#add_small params:" + map.toString());

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

//		String url = MCHConstant.getInstance().getUserModifyNickname() + "/account/" + account + "/nickname/" + nikeName;

        if (null != handler && null != params) {
            AddSmallAccountRequest addSmallAccountRequest = new AddSmallAccountRequest(handler);
            addSmallAccountRequest.post(MCHConstant.getInstance().getAddSmallAccount(), params);
        }else{
            MCLog.e(TAG, "fun#post handler is null or url is null");
        }
    }
}
