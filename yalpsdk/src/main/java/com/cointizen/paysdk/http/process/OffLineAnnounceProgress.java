package com.cointizen.paysdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.OffLineAnnounceRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5
 */
//下线
public class OffLineAnnounceProgress {
    private static final String TAG = "OffLineAnnounceProgress";

    private String account = UserLoginSession.getInstance().getAccount();
    private String game_id = SdkDomain.getInstance().getGameId();
    private String promote_id = SdkDomain.getInstance().getChannelId();
    private String sdk_version = "1";
    private boolean offLine = false;

    public void post(Handler handler) {
        if (null == handler) {
            MCLog.e(TAG, "fun#post handler is null");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("game_id", game_id);
        if(offLine){
            map.put("state", "1");
        }
        map.put("promote_id", promote_id);
        map.put("user_id",  UserLoginSession.getInstance().getUserId());
        map.put("sdk_version", sdk_version);

        String param = RequestParamUtil.getRequestParamString(map);

        if (TextUtils.isEmpty(param)) {
            MCLog.e(TAG, "fun#post param is null");
            return;
        }
        RequestParams params = new RequestParams();
        MCLog.w(TAG, "fun#post postSign:" + map.toString());
        try {
            params.setBodyEntity(new StringEntity(param.toString()));
        } catch (UnsupportedEncodingException e) {
            params = null;
            MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
        }
        if (null != params) {
            OffLineAnnounceRequest offLineAnnounceRequest = new OffLineAnnounceRequest(handler);
            offLineAnnounceRequest.post(MCHConstant.getInstance().getOfflineAnnouceUrl(), params);
        } else {
            MCLog.e(TAG, "fun#post RequestParams is null");
        }
    }

    public void setOffLine(boolean offLine) {
        this.offLine = offLine;
    }
}
