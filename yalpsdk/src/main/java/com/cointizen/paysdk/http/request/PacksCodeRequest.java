package com.cointizen.paysdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.PackCodeEntity;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class PacksCodeRequest {

    private static final String TAG = "PacksCodeRequest";

    HttpUtils http;
    Handler mHandler;

    public PacksCodeRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params, final String packName) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.PACKS_CODE_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                PackCodeEntity packCode = new PackCodeEntity();
                packCode.setPackName(packName);
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    MCLog.e(TAG, "onSuccess retrn_msg=" + json.optString("msg"));
                    status = json.optInt("code");
                    if (status == -1) {//uc用户不支持礼包码
                        noticeResult(Constant.PACKS_CODE_FAIL, json.opt("msg"));
                        return;
                    }
                    if (status == 200) {
                        packCode.setNovice(json.optString("data"));

                        noticeResult(Constant.PACKS_CODE_SUCCESS, packCode);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(json.optInt("code"));
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.PACKS_CODE_FAIL, msg);
                    }

                } catch (JSONException e) {
                    noticeResult(Constant.PACKS_CODE_FAIL, "解析参数异常");
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.PACKS_CODE_FAIL, "Failed to connect to the server");
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.PACKS_CODE_FAIL, "Failed to connect to the server");
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
