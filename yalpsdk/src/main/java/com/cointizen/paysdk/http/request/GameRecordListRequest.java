package com.cointizen.paysdk.http.request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.GameRecordEntity;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.cointizen.paysdk.http.HttpConstants;

public class GameRecordListRequest {

	private static final String TAG = "GameRecordListRequest";

	HttpUtils http;
	Handler mHandler;

	public GameRecordListRequest(Handler mHandler) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, RequestParams params){
		if (TextUtils.isEmpty(url) || null == params) {
			MCLog.e(TAG, "fun#post url is null");
			noticeResult(Constant.RECORD_LIST_FAIL, null, HttpConstants.S_ElwWiRoGqB);
			return;
		}
		MCLog.e(TAG, "fun#post url = " + url);
		// 设置当前请求的缓存时间
		http.configCurrentHttpCacheExpiry(0);
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String response=RequestUtil.getResponse( responseInfo);
						
						GameRecordEntity gre = GameRecordEntity.getInstance();
						GameRecordEntity gameRecordEntity;
						int status ;
						try {
							JSONObject json = new JSONObject(response);
							status = json.optInt("status");
							// {"status":1,"list":null}
							if (status==200||status==1) {// 请求成功
								//MCLog.i("",""+json.optString("list"));
								if(TextUtils.isEmpty(json.optString("list"))){
									noticeResult(Constant.GAME_RECODE_FAIL, null, HttpConstants.S_TzvHQhdLnS);
								}else{
								JSONArray ja = json.getJSONArray("list");
								JSONObject recode;
								for (int i = 0; i < ja.length(); i++) {
									gameRecordEntity=new GameRecordEntity();
									recode = (JSONObject) ja.getJSONObject(i);
									recode=new JSONObject(recode.toString());
									String pay_time=recode.optString("pay_time");
									String pay_money=recode.optString("pay_amount");
									String pay_type=recode.optString("pay_way");
									String pay_status=recode.optString("pay_status");
									String game_name=recode.optString("props_name");
									gameRecordEntity.setPayName(game_name);
									gameRecordEntity.setPayMoney(pay_money);
									gameRecordEntity.setPayStatus(pay_status);
									gameRecordEntity.setPayTime(pay_time);
									gameRecordEntity.setPayType(pay_type);
									gre.getGameRecordes().add(gameRecordEntity);
								}
								noticeResult(Constant.GAME_RECODE_SUCCESS,gre,"");
								}
							} else {
								String msg;
								if (!TextUtils.isEmpty(json.optString("msg"))) {
									msg = json.optString("msg");
								} else {
									msg = MCErrorCodeUtils.getErrorMsg(status);
								}
								MCLog.e(TAG, "msg:" + msg);
                               noticeResult(Constant.GAME_RECODE_FAIL,null,msg);
							}

						} catch (JSONException e) {
							noticeResult(Constant.GAME_RECODE_FAIL, null,
									HttpConstants.S_bcwiMnfyeK);
							MCLog.e(TAG, "fun#post  JSONException:" + e);
						} catch (Exception e) {
							noticeResult(Constant.GAME_RECODE_FAIL, null, HttpConstants.S_bcwiMnfyeK);
							MCLog.e(TAG, HttpConstants.Log_vbKOkdjMOX + e);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						MCLog.e(TAG,
								"fun#onFailure error = "
										+ error.getExceptionCode());
						MCLog.e(TAG, "onFailure" + msg);
						noticeResult(Constant.GAME_RECODE_FAIL, null, HttpConstants.S_uVIIKmGFwV);
					}
				});
	}

	private void noticeResult(int type, GameRecordEntity gameRecordEntity,
			String str) {
		Message msg = new Message();
		msg.what = type;
		if (Constant.GAME_RECODE_SUCCESS == type) {
			msg.obj = gameRecordEntity;
		} else {
			msg.obj = str;
		}
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}
}
