package com.cointizen.streaming;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.streaming.http.CallbackInterface;
import com.cointizen.streaming.http.ResponseData;
import com.cointizen.util.Base64;
import com.cointizen.util.RequestParamUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PostStringRequest<T> extends Request<ResponseData<T>> {

    private static final int TIMEOUT_RETRY_POLICY = 60000;
    private final Object deserializer;
    private final Object mLock = new Object();
    private final CallbackInterface<ResponseData<T>> listener;

    @Nullable
    @GuardedBy("mLock")
    private Response.Listener<ResponseData> mListener;

    private Map<String, String> mParams = new HashMap<>();

    public PostStringRequest(String url, Map<String, String> params,
                             CallbackInterface<ResponseData<T>> callbackInterface,
                             Response.ErrorListener errorListener,
                             Object deserializer) {
        super(Method.POST, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_RETRY_POLICY,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.listener = callbackInterface;
        this.deserializer = deserializer;
        mParams.clear();
        mParams.putAll(params);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        mParams.put("sdk_version", "1");//表示android发送的请求，固定值1
        mParams.put("game_id", SdkDomain.getInstance().getGameId());
        mParams.put("game_name", SdkDomain.getInstance().getGameName());
        mParams.put("game_appid", SdkDomain.getInstance().getGameAppId());
        return mParams;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params);
        }
        return null;
    }

    private byte[] encodeParameters(Map<String, String> params) {
        return RequestParamUtil.getRequestParamString(params).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            mListener = null;
        }
    }
    protected void deliverResponse(ResponseData response) {
        Response.Listener<ResponseData> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    protected Response<ResponseData<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(Base64.decode(new String(response.data, StandardCharsets.UTF_8)));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ResponseData.class, deserializer);
            Gson gson = gsonBuilder.create();
            ResponseData responseData = gson.fromJson(jsonString, ResponseData.class);
            return Response.success(responseData,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}
