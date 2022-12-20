package com.cointizen.streaming.actions;

import android.content.Context;

import com.android.volley.VolleyError;

import java.util.Map;

import com.cointizen.streaming.PostStringRequest;
import com.cointizen.open.YalpApplication;
import com.cointizen.streaming.bean.StreamingChannel;
import com.cointizen.streaming.deserializer.BaseDeserializer;
import com.cointizen.streaming.deserializer.BaseDeserializerList;
import com.cointizen.streaming.http.ResponseData;

public class GetSingleItem<T> {

    private Context context;
    private ListCommunicatorInterface<T> listCommunicatorInterface;

    public GetSingleItem(Context context, ListCommunicatorInterface<T> listCommunicatorInterface) {
        this.context = context;
        this.listCommunicatorInterface = listCommunicatorInterface;
    }

    public void getItemFromServer(String url, Class<T> mClass, Map<String, String> params, String tag) {
        if (context == null || url == null) {
            if (listCommunicatorInterface != null)
                listCommunicatorInterface.onFailed("Process interrupted");
            return;
        }

        PostStringRequest baseAction = new PostStringRequest<>(url, params, responseData -> {
            if (responseData.getCode() == 200) {
                listCommunicatorInterface.onSuccess((T) responseData.getData());
            } else {
                listCommunicatorInterface.onFailed(responseData.getMessage());
            }
        }, error -> listCommunicatorInterface.onError(error), new BaseDeserializer(StreamingChannel.class));

        YalpApplication.getInstance().addToRequestQueue(baseAction, tag);
    }

    public interface ListCommunicatorInterface<T> {
        void onError(VolleyError error);

        void onSuccess(T updatedList);

        void onFailed(String message);
    }
}
