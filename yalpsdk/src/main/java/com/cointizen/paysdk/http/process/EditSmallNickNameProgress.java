package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.EditSmallNickNameRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：修改小号昵称Process
 * 时间: 2018-07-17 9:44
 */
public class EditSmallNickNameProgress {
    private String TAG  = "EditSmallNickNameProgress";
    private String small_nickname;  //小号昵称
    private String small_id; //小号id



    public void setSmallNickName(String nickname) {
        this.small_nickname = nickname;
    }

    public void setSmall_id(String id) {
        this.small_id = id;
    }

    private String getParamStr(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserLoginSession.getInstance().getUserId());
        map.put("game_id", SdkDomain.getInstance().getGameId());
        map.put("nickname", small_nickname);
        map.put("small_id", small_id);
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


        if (null != handler && null != params) {
            EditSmallNickNameRequest request = new EditSmallNickNameRequest(handler);
            request.post(MCHConstant.getInstance().getEditSmallNickname(), params);
        }else{
            MCLog.e(TAG, "fun#post handler is null or url is null");
        }
    }
}
