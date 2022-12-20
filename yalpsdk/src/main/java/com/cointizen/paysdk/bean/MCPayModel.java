package com.cointizen.paysdk.bean;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.cointizen.open.OrderInfo;
import com.cointizen.open.PaymentCallback;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.activity.MCHPayActivity;
import com.cointizen.paysdk.dialog.SecurePaymentLoadingDialog;
import com.cointizen.paysdk.utils.MCLog;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 */
public class MCPayModel {

	/**
	 * 日志打印
	 */
	private final static String TAG = "MCPayModel";

	private PaymentCallback paymentCallback;
	private OrderInfo currentOrderInfo = null;
	private SecurePaymentLoadingDialog loadingDialog;
	private Context context;

	private static MCPayModel payModel;

	public static MCPayModel Instance(){
		if(null == payModel){
			payModel = new MCPayModel();
		}
		return payModel;
	}

	private MCPayModel() {
	}

	/**
	 * 商品支付
	 * @param orderInfo 商品信息
	 * @param pck 支付回调
	 */
	public void pay(final Context context, OrderInfo orderInfo, PaymentCallback pck) {
		this.context = context;

		if(!LoginModel.instance().isLogin()){
			ToastUtil.show(context,BeanConstants.S_user_not_logged_in);
			return;
		}
		this.paymentCallback = pck;
		if (!checkParams(orderInfo)) {
			return;
		}
		currentOrderInfo = orderInfo;
		showDialog(context);
		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(context, MCHPayActivity.class);
				context.startActivity(intent);
				dismissDialog();
			}
		}, 1000);
	}

   public OrderInfo order(){
	   return currentOrderInfo;
   }

	public PaymentCallback getPaymentCallback() {
		return paymentCallback;
	}

	private void showDialog(Context context) {
		loadingDialog = new SecurePaymentLoadingDialog.Builder()
				.show(context, ((Activity)context).getFragmentManager());
	}

	private void dismissDialog(){
		loadingDialog.dismiss();
	}

	private boolean checkParams(OrderInfo orderInfo) {
		if (orderInfo == null){
			MCLog.e(TAG,BeanConstants.Log_qxWnBYHQPM);
			paymentCallback.callback("-1");
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getRoleId())){
			paymentCallback.callback("-1");
			MCLog.e(TAG,BeanConstants.Log_pKTRSVFakK);
			ToastUtil.show(BeanConstants.S_AzgAOrEqVO);
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getRoleName())){
			MCLog.e(TAG,BeanConstants.Log_jLujVbQTJK);
			ToastUtil.show(BeanConstants.S_vsuMZOAUPj);
			paymentCallback.callback("-1");
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getRoleLevel())){
			MCLog.e(TAG,BeanConstants.Log_WLRIVjKKyT);
			ToastUtil.show(BeanConstants.S_SRxkbTULNs);
			paymentCallback.callback("-1");
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getGameServerId())){
			MCLog.e(TAG,BeanConstants.Log_slAKfMvfGu);
			ToastUtil.show(BeanConstants.S_PdbYIuVhEr);
			paymentCallback.callback("-1");
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getServerName())){
			MCLog.e(TAG,BeanConstants.Log_hRXRDoQFdM);
			ToastUtil.show(BeanConstants.S_UGYLOAwPVO);
			paymentCallback.callback("-1");
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getProductName())){
			MCLog.e(TAG,BeanConstants.Log_IxlxISTcpZ);
			ToastUtil.show(BeanConstants.S_YWczzVRaZL);
			paymentCallback.callback("-1");
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getProductDesc())){
			MCLog.e(TAG,BeanConstants.Log_dxpYQXmzUV);
			ToastUtil.show(BeanConstants.S_YYqGojBWaP);
			paymentCallback.callback("-1");
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getExtra_param())){
			MCLog.e(TAG,BeanConstants.Log_gNjgbuMvhC);
			ToastUtil.show(BeanConstants.S_hAtOLyYJAu);
			paymentCallback.callback("-1");
			return false;
		}
		if (android.text.TextUtils.isEmpty(orderInfo.getExtendInfo())){
			MCLog.e(TAG,BeanConstants.Log_VJKVpETnNt);
			ToastUtil.show(BeanConstants.S_EJqHoefYPu);
			paymentCallback.callback("-1");
			return false;
		}
		MCLog.e(TAG, orderInfo.toString());
		return true;
	}

}
