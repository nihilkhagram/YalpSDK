package com.cointizen.paysdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.GamePayTypeEntity;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONObject;

/**
 * Created by zhujinzhujin
 * on 2017/1/11.
 */

public class PayTypeRequest {

    private static final String TAG = "PayTypeRequest";

    HttpUtils http;
    Handler mHandler;

    public PayTypeRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(final String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.GAME_PAY_TYPE_FAIL, "参数异常");
            return;
        }

        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponseAndUrl(responseInfo, url);
                int status ;
                String msg = "";

                String isHaveZFB = "0", isHaveWX = "0", ZFBType = "0";
                String isHavePTB = "0", isHaveBindPTB = "0", isHaceScan = "0";

                GamePayTypeEntity typeEntity = new GamePayTypeEntity();
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200) {
                        JSONObject data = json.getJSONObject("data");
                        isHaveZFB = data.optString("zfb_game","0");
                        isHaveWX = data.optString("wx_game","0");
                        isHavePTB = data.optString("ptb_game","1");  //默认为开启平台币功能
                        isHaveBindPTB = data.optString("bind_game","0");
                        isHaceScan = data.optString("scan_pay","0");
                        if (isHaveZFB.equals("1")) {
                            //ZFBType 1是支付宝app支付 2是支付宝wap支付
                            ZFBType = data.optString("zfb_type","1");
                        }

                        typeEntity.isHaveZFB = ("1".equals(isHaveZFB));
                        typeEntity.isHaveWX = ("1".equals(isHaveWX));
                        typeEntity.ZFBisWapPay = ("2".equals(ZFBType));
                        typeEntity.supportYlp = ("1".equals(isHavePTB));
                        typeEntity.supportFiat = ("1".equals(isHaveBindPTB));
                        typeEntity.isHaceScan = ("1".equals(isHaceScan));
                        typeEntity.wxRemark = data.optString("wx_remark","");
                        typeEntity.zfbRemark = data.optString("zfb_remark","");
                        typeEntity.ptbRemark = data.optString("ptb_remark","");
                        typeEntity.bindRemark = data.optString("bind_remark","");
                        noticeResult(Constant.GAME_PAY_TYPE_SUCCESS,typeEntity);
                    } else {
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.GAME_PAY_TYPE_FAIL, msg);
                    }
                } catch (Exception e) {
                    noticeResult(Constant.GAME_PAY_TYPE_FAIL, "服务器异常");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.GAME_PAY_TYPE_FAIL, "Failed to connect to the server");
            }
        });
    }

    private void noticeResult(int type, Object object) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = object;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }

}
