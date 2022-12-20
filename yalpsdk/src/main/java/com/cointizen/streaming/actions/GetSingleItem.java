package com.cointizen.streaming.actions;

import static io.agora.base.internal.ContextUtils.getApplicationContext;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cointizen.newcode.CallBackInterface;
import com.cointizen.newcode.PostRequestString;
import com.cointizen.open.YalpApplication;
import com.cointizen.streaming.deserializer.BaseDeserializer;
import com.cointizen.streaming.http.ResponseData;

import java.util.Map;

public class GetSingleItem<T extends ResponseData> {

    private Context context;
    private ListCommunicatorInterface<T> listCommunicatorInterface;
    private RequestQueue mRequestQueue;

    public GetSingleItem(Context context, ListCommunicatorInterface<T> listCommunicatorInterface) {
        this.context = context;
        this.listCommunicatorInterface = listCommunicatorInterface;
    }

    public void getItemFromServer(String url, Class<T> mClass, Map<String, String> param, String tag) {

        if (context == null || url == null) {
            if (listCommunicatorInterface != null)
                listCommunicatorInterface.onFailed("Process interrupted");
            return;
        }

        PostRequestString baseHttpRequest = new PostRequestString(url, mClass, param, new CallBackInterface<T>() {
            @Override
            public void passResult(T responseData) {
                Log.e("MyResponseData" , "Code ==> " + responseData.getCode());
                Log.e("MyResponseData" , "Code ==> " + responseData);
                if (responseData.getCode() == 200) {
                    listCommunicatorInterface.onSuccess(responseData);
                } else {
                    listCommunicatorInterface.onFailed(responseData.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listCommunicatorInterface.onError(error);
            }
        }, new BaseDeserializer(mClass));

        YalpApplication.getInstance().addToRequestQueue(baseHttpRequest, tag);
    }

    public interface ListCommunicatorInterface<T> {
        void onError(VolleyError error);

        void onSuccess(T updatedList);

        void onFailed(String message);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? "TAG" : tag);
        getRequestQueue().add(req);
    }

}
