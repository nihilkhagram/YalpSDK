package com.cointizen.paysdk.http.hfbpay;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import android.os.Handler;

public class HFBPayProcess {

	private static final String TAG = "HFBPayProcess";

	/** 金额(必填) */
	private String goodsPrice;
	/** 物品名称(必填) */
	private String goodsName;
	/** 商户添加备注(可选) */
	private String remark;
	/** 充值类型 平台币 0 游戏 1 */
	private String payType;
	/**游戏订单信息*/
	private String extend;
	/*** 商品描述 */
	private String goodsDesc;
	public void post(Handler handler) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sdk_version", "0");//表示android发送的请求，固定值0
//		map.put("extend", extend);
		map.put("title", goodsName);
		map.put("price", goodsPrice);
		map.put("body",goodsDesc);
		
		map.put("game_id", SdkDomain.getInstance().getGameId());
		map.put("game_name", SdkDomain.getInstance().getGameName());
		map.put("game_appid", SdkDomain.getInstance().getGameAppId());
		// map.put("promote_id", MCHConstant.getInstance().getPromoteId());
		// map.put("promote_account", MCHConstant.getInstance().getPromoteAccount());
		map.put("code",payType);
		map.put("account", UserLoginSession.getInstance().getAccount());
		map.put("user_id", UserLoginSession.getInstance().getUserId());
		map.put("extend",extend);
		
		map.put("pay_type", "10");//汇付宝支付类型等发布出去时候写个列表界面
		String param = RequestParamUtil.getRequestParamString(map);
		RequestParams params = new RequestParams();
		MCLog.e(TAG, "fun#jby_pay params:" + map.toString());
		try {
			params.setBodyEntity(new StringEntity(param.toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#jby_pay UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			HFBPayRequest request = new HFBPayRequest(handler);
			request.post(MCHConstant.getInstance().getHfbOrderInfoUrl(), params);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
	public String getExtend() {
		return extend;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public String getGoodsName() {
		return goodsName;
	}
	
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}