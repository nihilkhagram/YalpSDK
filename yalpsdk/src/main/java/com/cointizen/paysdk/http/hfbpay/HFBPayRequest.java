package com.cointizen.paysdk.http.hfbpay;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cointizen.paysdk.common.Constant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.util.Base64;
import com.cointizen.paysdk.utils.MCLog;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class HFBPayRequest {

	private static final String TAG = "HFBPayRequest";

	HttpUtils http;
	Handler mHandler;

	public HFBPayRequest(Handler mHandler) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, RequestParams params) {
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null add params is null");
			noticeResult(Constant.YLPD_PAY_FAIL, HttpConstants.S_ElwWiRoGqB);
			return;
		}
		MCLog.e(TAG, "fun#post url " + url);
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							PaymentInfo _paymentInfo;

							String result = new String(Base64.decode(responseInfo.result), "utf-8");
							JSONObject json = new JSONObject(result);
							MCLog.e(TAG, "onSuccess: result = " + result);
							MCLog.e(TAG,"onSuccess retrn_msg = " + json.optString("msg"));
							String status = json.optString("status");
							String return_code = json.optString("return_code");
							String return_msg = json.optString("msg");
							if ("1".equals(status)) {
								String token_id = json.optString("token_id");
								String out_trad_no = json.optString("out_trade_no");
								String agent_id = json.optString("agent_id");
								_paymentInfo =new PaymentInfo();
								_paymentInfo.setAgentId(agent_id);
								_paymentInfo.setBillNo(out_trad_no);
								_paymentInfo.setTokenID(token_id);
								noticeResult(Constant.HFB_PAY_REQUEST_SUCCESS,_paymentInfo);
							} else {
								_paymentInfo=new PaymentInfo();
								_paymentInfo.setMessage(return_msg);
								noticeResult(Constant.HFB_PAY_REQUEST_FAIL, _paymentInfo);
							}
						} catch (JSONException e) {
							PaymentInfo _paymentInfo=new PaymentInfo();
							_paymentInfo.setMessage(HttpConstants.S_ngntgFOVka);
							noticeResult(Constant.HFB_PAY_REQUEST_FAIL, HttpConstants.S_egTJZwEHGo);
							MCLog.e(TAG, HttpConstants.Log_GeXaQMMmfg + e);
						} catch (UnsupportedEncodingException e) {
							PaymentInfo _paymentInfo=new PaymentInfo();
							_paymentInfo.setMessage(HttpConstants.S_rLrNAHMNhs);
							noticeResult(Constant.HFB_PAY_REQUEST_FAIL,_paymentInfo);
							MCLog.e(TAG,
									HttpConstants.S_NdRiWmmcEo
											+ e);
						} catch (Exception e) {
							PaymentInfo _paymentInfo=new PaymentInfo();
							_paymentInfo.setMessage(HttpConstants.S_urgRHdozet);
							noticeResult(Constant.HFB_PAY_REQUEST_FAIL,_paymentInfo);
							MCLog.e(TAG, HttpConstants.Log_zFAYIvhwpV + e);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						String fun = " fun # onFailure ";
						String errCode = " errorCode = "+ error.getExceptionCode();
						String errMsg = " errorMsg = "+ error.getMessage();
						MCLog.e(TAG,fun+errCode+errMsg);
						PaymentInfo _paymentInfo=new PaymentInfo();
						_paymentInfo.setMessage(fun+errCode+errMsg);
						noticeResult(Constant.HFB_PAY_REQUEST_FAIL,fun+errCode+errMsg);
					}
				});
	}

	private void noticeResult(int type, Object obj) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = obj;
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}
}
