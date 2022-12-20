package com.cointizen.paysdk.bean.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.cointizen.open.ApiCallback;
import com.cointizen.open.FlagControl;
import com.cointizen.paysdk.activity.MCHWapPayActivity;
import com.cointizen.paysdk.bean.MCPayModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.PayResult;
import com.cointizen.paysdk.entity.WXOrderInfo;
import com.cointizen.paysdk.entity.StripeVerificationResult;
import com.cointizen.paysdk.http.process.StripeOrderInfoProcess;
import com.cointizen.paysdk.payment.StripePaymentRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.AppConstants;

public class StripePay {

	private final static String TAG = "StripePay";

	private boolean isBuyPtb;
	private Activity mainActivity;
	private StripeOrderInfoProcess stripeOrderInfoProcess;
	private StripeBuyYLPDEvent stripeBuyYLPDEvent;

	public StripePay(Activity activity, StripePaymentRequest stripePaymentRequest){
		this.isBuyPtb = false;
		this.stripeOrderInfoProcess = new StripeOrderInfoProcess(stripePaymentRequest);
		if(null != activity){
			mainActivity = activity;
		}
	}

	private final Handler stripeOrderPaymentHandler = new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.ZFB_PAY_VALIDATE_SUCCESS:// 请求支付订单成功
					queryStripePaymentResult(msg.obj);
					break;
				case Constant.ZFB_PAY_VALIDATE_FAIL:// 请求支付订单失败
					FlagControl.BUTTON_CLICKABLE = true;
					ToastUtil.show(mainActivity, AppConstants.STR_244e5dd1ea2819fccabf8a5527dd1aca + msg.obj.toString());
					break;
				case Constant.SDK_PAY_FLAG:// 支付结果
					handlerStripePaymentResult(msg.obj);
					break;
				case Constant.ZFB_WAPPAY_ORDERINFO_SUCCESS:
					WXOrderInfo zfbwapPayOrderInfo = (WXOrderInfo) msg.obj;
					Intent zfbWapPayintent = new Intent(mainActivity, MCHWapPayActivity.class);
					zfbWapPayintent.putExtra("WapPayOrderInfo", zfbwapPayOrderInfo);
					mainActivity.startActivity(zfbWapPayintent);
					break;
				case Constant.ZFB_WAPPAY_ORDERINFO_FAIL:
					ToastUtil.show(mainActivity, AppConstants.STR_244e5dd1ea2819fccabf8a5527dd1aca + msg.obj);
					FlagControl.BUTTON_CLICKABLE = true;
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 支付宝app游戏充值
	 */
	public void stripePayProcess(String coupon_id,boolean isWepay) {
		stripeOrderInfoProcess.setGoodsName(ApiCallback.order().getProductName());
		stripeOrderInfoProcess.setGoodsPrice(ApiCallback.order().getGoodsPriceYuan());
		stripeOrderInfoProcess.setGoodsDesc(ApiCallback.order().getProductDesc());
		stripeOrderInfoProcess.setServerName(ApiCallback.order().getServerName());
		stripeOrderInfoProcess.setRoleName(ApiCallback.order().getRoleName());
		stripeOrderInfoProcess.setRoleId(ApiCallback.order().getRoleId());
		stripeOrderInfoProcess.setServerId(ApiCallback.order().getGameServerId());
		stripeOrderInfoProcess.setExtra_param(ApiCallback.order().getExtra_param());
		stripeOrderInfoProcess.setRoleLevel(ApiCallback.order().getRoleLevel());
		stripeOrderInfoProcess.setGoodsReserve(ApiCallback.order().getGoodsReserve());
		stripeOrderInfoProcess.setPayType("1");
		stripeOrderInfoProcess.setCouponId(coupon_id);
		stripeOrderInfoProcess.setExtend(ApiCallback.order().getExtendInfo());
		stripeOrderInfoProcess.post(stripeOrderPaymentHandler,isWepay);
	}

	/**
	 * 支付宝平台币充值
	 * @param goodsName 需要充值的物品名称
	 * @param amount 需要充值的物品价格
	 * @param goodsDesc
	 * @param stripeBuyYLPDEvent 支付回调接口
	 */
	public void topupYLPD(String goodsName, String goodsDesc, float amount, String extInfo,
								 StripeBuyYLPDEvent stripeBuyYLPDEvent) {
		isBuyPtb = true;
		if(null != stripeBuyYLPDEvent){
			this.stripeBuyYLPDEvent = stripeBuyYLPDEvent;
		}
		stripeOrderInfoProcess.setGoodsName(goodsName);
		stripeOrderInfoProcess.setGoodsPrice(String.format("%.2f", amount));
		stripeOrderInfoProcess.setGoodsDesc(goodsDesc);
		stripeOrderInfoProcess.setPayType("0");
		stripeOrderInfoProcess.setExtend(extInfo);
		stripeOrderInfoProcess.post(stripeOrderPaymentHandler, false);
	}

	/**
	 * 支付宝平台币wap充值
	 * @param goodsName 需要充值的物品名称
	 * @param goodsPrice 需要充值的物品价格
	 * @param goodsDesc
	 * @param zfbBuPtbEvent 支付回调接口
	 */
	public void zfbPayPTBWapProcess(String goodsName, String goodsPrice,
									String goodsDesc, StripeBuyYLPDEvent zfbBuPtbEvent, boolean isWepay, Handler handler){
		isBuyPtb = true;
		if(null != zfbBuPtbEvent){
			stripeBuyYLPDEvent = zfbBuPtbEvent;
		}
		stripeOrderInfoProcess.setGoodsName(goodsName);
		stripeOrderInfoProcess.setGoodsPrice(goodsPrice);
		stripeOrderInfoProcess.setGoodsDesc(goodsDesc);
		stripeOrderInfoProcess.setPayType("0");
		stripeOrderInfoProcess.setExtend(AppConstants.STR_f5d2226c360da912951f424524181024);
		stripeOrderInfoProcess.post(handler,isWepay);
	}

	/**
	 * 处理响应结果，支付宝发起支付
	 * @param obj
	 */
	private void queryStripePaymentResult(Object obj) {
//		Button btnPay=
		if(null == mainActivity){
			MCLog.e(TAG, AppConstants.LOG_7396d8941f8802dec3afe6278bd5292d);
			return;
		}
		StripeVerificationResult stripePaymentResult = (StripeVerificationResult) obj;
		if (null == stripePaymentResult) {
			MCLog.e(TAG, AppConstants.LOG_c2e6c149d08d4831288fb67751db006f);
			return;
		}
		if (TextUtils.isEmpty(stripePaymentResult.getOrderInfo())) {
			String msg = stripePaymentResult.getMsg();
			if (TextUtils.isEmpty(msg)) {
				msg = AppConstants.STR_115169ca6df413a27e55168e951eed7b;
			}
			MCLog.e(TAG , "error:" + msg);
			return;
		}
		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = stripePaymentResult.getOrderInfo();

		// + "&sign=\"" + zfbPayResult.getSign()+ "\"&sign_type=\"RSA\"";
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
//				PayTask alipay = new PayTask(mActivity);
//				FlagControl.flag = true;
//				// 调用支付接口，获取支付结果
//				String result = alipay.pay(payInfo, true);
//				Message msg = new Message();
//				msg.what = Constant.SDK_PAY_FLAG;
//				msg.obj = result;
//				zfbpayHandler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 9000Payment done successfully  8000正在确认  4000订单Payment failed
	 * 5000支付取消  6001支付取消  6002网络连接出错 6004未获取到支付结果
	 * 4001支付宝页面Payment done successfully（目前sdk对4001的处理将其视为支付结果待确认状态，可以视为Payment done successfully）
	 * 处理支付宝支付结果
	 */
	private void handlerStripePaymentResult(Object obj) {
		PayResult payResult = new PayResult((String) obj);
		String resultStatus = "-1";
		if (!TextUtils.isEmpty(payResult.getResultStatus())) {
			resultStatus = payResult.getResultStatus();
		}
		MCLog.e(TAG, "fun#handlerZfbSDKResult " + resultStatus);
		if (TextUtils.equals(resultStatus, "9000")) {
			resultStatus = "0";
		} else if (TextUtils.equals(resultStatus, "8000")||(TextUtils.equals(resultStatus,"4001"))) {
			resultStatus = "1";
		} else if (TextUtils.equals(resultStatus, "6004")) {
			resultStatus = "2";
		}

		if(isBuyPtb) {
			if(null != stripeBuyYLPDEvent){
				boolean res = "0".equals(resultStatus) || "1".equals(resultStatus);
				stripeBuyYLPDEvent.buyYLPDResult(res);
			}
		}else {
			MCPayModel.Instance().getPaymentCallback().callback(resultStatus);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mainActivity.finish();
				}
			}, 200);
		}
        FlagControl.BUTTON_CLICKABLE =true;
	}

}
