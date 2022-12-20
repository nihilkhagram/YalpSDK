package com.cointizen.paysdk.http.process;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.ZFBOrderInfoRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;
import android.text.TextUtils;

public class ZFBOrderInfoProcess {

	private static final String TAG = "ZfbOrderInfoProcess";

	private String extra_param = "";
	private String goodsName = ""; //商品名称
	private String goodsPrice = "";  //商品价格
	private String goodsDesc = ""; //商品描述
	private String payType = "";  //充值类型 平台币 0 游戏 1
	private String extend = "";  //游戏订单信息
	private String serverName = "";  //区服名字
	private String serverId = "";  //区服ID
	private String roleName = "";  //角色名字
	private String roleId = "";  //角色Id
	private String roleLevel = ""; //角色等级
	private String couponId = ""; //代金券id
	private String goodsReserve = "";

	private boolean isWapPay = false;

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public ZFBOrderInfoProcess(boolean isWapPay) {
		this.isWapPay = isWapPay;
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

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public void setExtra_param(String extra_param) {
		this.extra_param = extra_param;
	}

	public void setGoodsReserve(String goodsReserve) {
		this.goodsReserve = goodsReserve;
	}

	public void post(Handler handler) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sdk_version", "1");//表示android发送的请求，固定值1
		map.put("title", goodsName);
		map.put("price", goodsPrice);
		map.put("body", goodsDesc);
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("game_appid", SdkDomain.getInstance().getGameAppId());
		map.put("code", payType);
		map.put("account", UserLoginSession.getInstance().getAccount());
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("extend", extend);
		map.put("extra_param", extra_param);
		map.put("small_id", UserLoginSession.getInstance().getSmallAccountUserID());
		if (!TextUtils.isEmpty(couponId)){
			map.put("coupon_id",couponId);
		}

		if (payType.equals("1")){  //游戏内购买道具
			map.put("server_name", serverName);
			map.put("server_id", serverId);
			map.put("game_player_name", roleName);
			map.put("game_player_id", roleId);
			map.put("role_level", roleLevel);
			map.put("goods_reserve", goodsReserve);
		}
		String param = RequestParamUtil.getRequestParamString(map);
		RequestParams params = new RequestParams();
		MCLog.e(TAG, "fun#post postSign:" + map.toString());
		try {
			params.setBodyEntity(new StringEntity(param.toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}
		if (null != handler && null != params) {
			ZFBOrderInfoRequest zfbPayRequest = new ZFBOrderInfoRequest(handler);
			zfbPayRequest.post(MCHConstant.getInstance().getZfbVerificationOrderUrl(), params, this.isWapPay);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
