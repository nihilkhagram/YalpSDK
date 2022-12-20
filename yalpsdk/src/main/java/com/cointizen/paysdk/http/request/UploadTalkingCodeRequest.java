package com.cointizen.paysdk.http.request;
import android.text.TextUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCLog;
import org.json.JSONObject;
import com.cointizen.paysdk.http.HttpConstants;

public class UploadTalkingCodeRequest {

    private static final String TAG = "UploadTalkingCodeRequest";

    HttpUtils http;

    public UploadTalkingCodeRequest() {
        http = new HttpUtils();
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                try {
                    JSONObject json = new JSONObject(response);
                    int status = json.optInt("code");
                    String msg = json.optString("msg");

                } catch (Exception e) {
                    MCLog.e(TAG,HttpConstants.Log_cBhqIQKcGw + e.toString());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
            }
        });
    }

}
