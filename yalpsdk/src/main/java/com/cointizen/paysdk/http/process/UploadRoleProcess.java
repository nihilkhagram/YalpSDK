package com.cointizen.paysdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.open.RoleInfo;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.UploadRoleRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smile on 2017/8/18.
 */

public class UploadRoleProcess {

    private static final String TAG = "UploadRoleProcess";
    private RoleInfo roleInfo;

    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    public void post(Handler handler) {
        if (null == handler) {
            MCLog.e(TAG, "fun#post handler is null");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserLoginSession.getInstance().getUserId());
        map.put("game_id", SdkDomain.getInstance().getGameId());
        map.put("server_id", roleInfo.getServerId());
        map.put("server_name", roleInfo.getServerName());
        map.put("game_player_name", roleInfo.getRoleName());
        map.put("game_player_id", roleInfo.getRoleId());
        map.put("role_level", roleInfo.getRoleLevel());
        map.put("combat_number", roleInfo.getRoleCombat());
        map.put("small_id", UserLoginSession.getInstance().getSmallAccountUserID());
        map.put("player_reserve", roleInfo.getPlayerReserve());

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
            UploadRoleRequest uploadRoleRequest = new UploadRoleRequest(handler, null);
            uploadRoleRequest.post(MCHConstant.getInstance().getUploadRoleUrl(), params);
        } else {
            MCLog.e(TAG, "fun#post RequestParams is null");
        }

    }
}
