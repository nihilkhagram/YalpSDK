package com.cointizen.streaming.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jignesh on 07-01-2015.
 */
public class BaseHttpRequest<T> extends Request<T> {
    private static final int TIMEOUT_RETRY_POLICY = 60000;
    private final Object deserializer;
    private final Class<T> clazz;
    private final CallbackInterface<T> listener;

    private Map<String, String> mHeader = new HashMap<>();
    private Map<String, String> mParams = new HashMap<>();

    private static String authorizationParam = "Authorization";
    private static String purchaseCodeParam = "PurchaseCode";
    private static String tokenParam = "Token";

    /**
     * @param url           calling URL
     * @param clazz         Our final conversion type
     *                      //  @param headers       Accompanying the request header information
     * @param listener      //  @param appendHeader  Additional head data
     * @param errorListener error response
     */
    public BaseHttpRequest(String url, Class<T> clazz, Map<String, String> params,
                           CallbackInterface<T> listener, ErrorListener errorListener, Object deserializer) {
        super(Method.POST, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_RETRY_POLICY,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.clazz = clazz;
        this.listener = listener;
        this.deserializer = deserializer;
        mParams.clear();
        mParams.putAll(params);

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeader == null)
            mHeader = new HashMap<>();
        if (mParams.containsKey("AppDetails")) {
            mParams.remove("AppDetails");
            //TODO : Remove device id
            //mHeader.put("Deviceid", YalpApplication.getInstance().getDeviceId());
            mHeader.put("Deviceid", "c51e4be67e9df9f5");
            mHeader.put("Devicetype", "Android");
            //mHeader.put("Appversion", String.valueOf(BuildConfig.VERSION_CODE));
        }
        if (mParams.get(authorizationParam) != null && mParams.containsKey(authorizationParam)) {
            mHeader.put(authorizationParam, String.valueOf(mParams.get(authorizationParam)));
            mParams.remove(authorizationParam);
        }
        if (mParams.get(purchaseCodeParam) != null && mParams.containsKey(purchaseCodeParam)) {
            mHeader.put(purchaseCodeParam, String.valueOf(mParams.get(purchaseCodeParam)));
            mParams.remove(purchaseCodeParam);
        }
        if (mParams.get(tokenParam) != null && mParams.containsKey(tokenParam)) {
            mHeader.put(tokenParam, String.valueOf(mParams.get(tokenParam)));
            mParams.remove(tokenParam);
        }
        //LogUtil.debug("--- Request : header " + mHeader);
        return mHeader;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
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

            /*
             * Into the object
             */
            return Response.success(gson.fromJson(jsonStr, clazz), HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}