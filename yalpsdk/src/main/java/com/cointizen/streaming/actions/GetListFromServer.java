package com.cointizen.streaming.actions;

import static io.agora.base.internal.ContextUtils.getApplicationContext;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cointizen.newcode.CallBackInterface;
import com.cointizen.newcode.PostRequestString;
import com.cointizen.open.YalpApplication;
import com.cointizen.streaming.deserializer.BaseDeserializerList;
import com.cointizen.streaming.http.ResponseData;

import java.util.List;
import java.util.Map;

public class GetListFromServer<T extends ResponseData> {

    private Context context;
    private ListCommunicatorInterface<T> listCommunicatorInterface;
    private RequestQueue mRequestQueue;

    public GetListFromServer(Context context, ListCommunicatorInterface<T> listCommunicatorInterface) {
        this.context = context;
        this.listCommunicatorInterface = listCommunicatorInterface;
    }

    public void getListFromServer(String url, Class<T> mclass, Map<String, String> param, String tag) {

        if (context == null || url == null) {
            if (listCommunicatorInterface != null)
                listCommunicatorInterface.onFailed("Process interrupted");
            return;
        }

        PostRequestString baseHttpRequest = new PostRequestString(url, ResponseData.class, param, new CallBackInterface<ResponseData>() {
            @Override
            public void passResult(ResponseData responseData) {
                if (responseData.getCode() == 200) {
                    listCommunicatorInterface.onSuccess((List<T>) responseData.getData());
                } else {
                    listCommunicatorInterface.onFailed(responseData.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listCommunicatorInterface.onError(error);
            }
        }, new BaseDeserializerList<>(ResponseData.class, mclass));

        YalpApplication.getInstance().addToRequestQueue(baseHttpRequest, "tag");

    }

    public interface ListCommunicatorInterface<T> {
        void onError(VolleyError error);

        void onSuccess(List<T> updatedList);

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
