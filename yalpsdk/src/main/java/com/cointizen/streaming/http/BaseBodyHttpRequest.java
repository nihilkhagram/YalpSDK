package com.cointizen.streaming.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jignesh on 07-01-2015.
 */
public class BaseBodyHttpRequest<T> extends Request<T> {
    private static final int TIMEOUT_RETRY_POLICY = 60000;
    private final Object deserializer;
    private final Class<T> clazz;
    private final CallbackInterface<T> listener;
    private static Map<String, String> mHeader = new HashMap<>();
    private static JSONObject mParams = new JSONObject();

    /**
     * @param url           calling URL
     * @param clazz         Our final conversion type
     *                      // * @param headers       Accompanying the request header information
     * @param listener      // * @param appendHeader  Additional head data
     * @param errorListener error response
     */
    public BaseBodyHttpRequest(String url, Class<T> clazz, JSONObject params, CallbackInterface<T> listener, ErrorListener errorListener, Object deserializer) {
        super(Method.POST, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_RETRY_POLICY,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.clazz = clazz;
        this.listener = listener;
        this.deserializer = deserializer;
        mParams = params;
        //LogUtil.debug("--- Request : param " + mParams);
    }


    @Override
    public Map<String, String> getHeaders() {
        if (mHeader == null)
            mHeader = new HashMap<>();
        /*mHeader.put("Commontoken", BuildConfig.Commontoken);
        mHeader.put("Devicetype", BuildConfig.Devicetype);
        mHeader.put("appversion", BuildConfig.VERSION_NAME);
        mHeader.put("deviceid", YalpApplication.getInstance().getDeviceId());*/
        try {
            if (mParams.get("Authorization") != null) {
                mHeader.put("Authorization", String.valueOf(mParams.get("Authorization")));
                mParams.remove("Authorization");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //LogUtil.debug("--- Request : header " + mHeader);
        return mHeader;
    }

    @Override
    public byte[] getBody() {
        return mParams == null ? null : mParams.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void deliverResponse(T response) {
        if (listener != null)
            listener.passResult(clazz.cast(response));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonStr = new String(response.data, StandardCharsets.UTF_8);
            //LogUtil.debug("--- Response : " + jsonStr);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(clazz, deserializer);
            Gson gson = gsonBuilder.create();
            return Response.success(gson.fromJson(jsonStr, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
