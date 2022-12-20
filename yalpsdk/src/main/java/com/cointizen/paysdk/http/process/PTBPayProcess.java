package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.PTBPayRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;
import com.cointizen.paysdk.utils.TextUtils;

import android.content.Context;
import android.os.Handler;

public class PTBPayProcess {

	private static final String TAG = "PtbPayProcess";

	private String code;  //支付类型 1 非绑定 2绑定
	private String extra_param = "";
	private String goodsName = ""; //商品名称
	private String goodsPrice = "";  //商品价格
	private String goodsDesc = ""; //商品描述
	private String extend = "";  //游戏订单信息
	private String serverName = "";  //区服名字
	private String serverId = "";  //区服ID
	private String roleName = "";  //角色名字
	private String roleId = "";  //角色Id
	private String roleLevel = ""; //角色等级
	private String goodsReserve = "";
	private String couponId = ""; //代金券id

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public void setExtra_param(String extra_param) {
		this.extra_param = extra_param;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setGoodsReserve(String goodsReserve) {
		this.goodsReserve = goodsReserve;
	}

	public void post(Handler handler, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", goodsName);
		map.put("price", goodsPrice);
		map.put("body", goodsDesc);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("game_appid", SdkDomain.getInstance().getGameAppId());
		map.put("code", code);
		map.put("extend", extend);
		map.put("extra_param", extra_param);
		map.put("account", UserLoginSession.getInstance().getAccount());
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("small_id", UserLoginSession.getInstance().getSmallAccountUserID());
		map.put("server_name", serverName);
		map.put("server_id", serverId);
		map.put("game_player_name", roleName);
		map.put("game_player_id", roleId);
		map.put("role_level", roleLevel);
		map.put("goods_reserve", goodsReserve);
		map.put("coupon_id",couponId);

		if(!TextUtils.isEmpty(UserLoginSession.getInstance().getIs_uc())){
			map.put("is_uc", UserLoginSession.getInstance().getIs_uc());
		}
		
		String param = RequestParamUtil.getRequestParamString(map);

		RequestParams params = new RequestParams();
		MCLog.e(TAG, "fun#ptb_pay params:" + map.toString());
		try {
			params.setBodyEntity(new StringEntity(param.toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#ptb_pay UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			PTBPayRequest ptbPayRequest = new PTBPayRequest(handler);
			ptbPayRequest.post(MCHConstant.getInstance().getPtbPayOrderUrl(), params, context);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}

}
