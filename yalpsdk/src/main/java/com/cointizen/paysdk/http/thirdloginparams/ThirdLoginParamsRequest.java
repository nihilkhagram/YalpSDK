package com.cointizen.paysdk.http.thirdloginparams;

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
import com.cointizen.paysdk.entity.ThirdLoginParamsBean;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

public class ThirdLoginParamsRequest {

    private static final String TAG = "ThirdLoginParamsRequest";

    HttpUtils http;
    Handler mHandler;


    public ThirdLoginParamsRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params, final String login_type) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.w(TAG, "fun#post url is null add params is null");
            return;
        }

//		final ProgressDialog dialog = new ProgressDialog(context);
//		dialog.setMessage(HttpConstants.S_dnqGmuRJhA);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
        MCLog.i(TAG, "fun#post url = " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //	dialog.dismiss();
                String response = RequestUtil.getResponse(responseInfo);
                JSONObject jsonObject = null;
                MCLog.e(HttpConstants.Log_JFOlKPbAaV,response);
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    MCLog.e(TAG, "json:" + e);
                    noticeResult(Constant.USER_GET_PARAMS_FAIL, TAG + "json fail!" + response);
                    return;
                }
                ThirdLoginParamsBean bean = new ThirdLoginParamsBean();
                bean.login_type = login_type;
                int status = jsonObject.optInt("code");
                bean.message = jsonObject.optString("msg");


                if (status == 200) {
                    JSONObject data = null;
                    try {
                        data = jsonObject.getJSONObject("data");
//                bean.bdclientid = jsonObject.optString("clientid");
                        bean.qqappid = data.optString("qqappid");
                        bean.weixinappid = data.optString("weixinappid");
//                bean.weiboappkey = jsonObject.optString("weiboappkey");
//                bean.redirecturl = jsonObject.optString("redirecccturl");
//                bean.wbscope = jsonObject.optString("scope");
                        noticeResult(Constant.USER_GET_PARAMS_SUCCESS, bean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        noticeResult(Constant.USER_GET_PARAMS_FAIL, HttpConstants.S_RLHRMnkQMF);
                    }

                } else {
                    String msg;
                    if (!TextUtils.isEmpty(jsonObject.optString("msg"))) {
                        msg = jsonObject.optString("msg");
                    } else {
                        msg = MCErrorCodeUtils.getErrorMsg(jsonObject.optInt("code"));
                    }
                    MCLog.e(TAG, "msg:" + msg);
                    noticeResult(Constant.USER_GET_PARAMS_FAIL, msg);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "onFailure exception code = " + error.getExceptionCode() + " message = " + msg);
                noticeResult(Constant.USER_GET_PARAMS_FAIL, TAG + HttpConstants.S_QYriYrdOGx + error.getExceptionCode() + " message = " + msg);
            }
        });
    }

    private void noticeResult(int type, Object str) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = str;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
