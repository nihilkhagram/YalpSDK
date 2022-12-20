package com.cointizen.newcode;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.util.Base64;
import com.cointizen.util.RequestParamUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PostRequestString <T> extends Request<T> {

    private static final int TIMEOUT_RETRY_POLICY = 60000;
    private final Object deserializer;
    private final Class<T> clazz;
    private final CallBackInterface<T> listener;

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
    public PostRequestString(String url, Class<T> clazz, Map<String, String> params,
                           CallBackInterface<T> listener,
                           Response.ErrorListener errorListener,
                           Object deserializer) {
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

        //LogUtil.debug("--- Request : param " + mParams);
        //LogUtil.debug("--- Request : url " + url);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeader == null)
            mHeader = new HashMap<>();
        if (mParams.containsKey("AppDetails")) {
            mParams.remove("AppDetails");
            //TODO : Remove device id
            //mHeader.put("Deviceid", AppController.getInstance().getDeviceId());
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

    /*@Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            mListener = null;
        }
    }*/

    @Override
    protected void deliverResponse(T response) {
        if (listener != null)
            listener.passResult(clazz.cast(response));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            /*
             * Returns the data
             */
            String jsonStr = new String(Base64.decode(new String(response.data, StandardCharsets.UTF_8)));
            //LogUtil.debug("--- Response : " + jsonStr);
            Log.e("MyResponseData" , "" +
                    jsonStr);
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