package com.cointizen.streaming.actions;

import android.content.Context;

import com.android.volley.VolleyError;
import java.util.List;
import java.util.Map;

import com.cointizen.streaming.PostStringRequest;
import com.cointizen.open.YalpApplication;
import com.cointizen.streaming.bean.StreamingChannel;
import com.cointizen.streaming.http.ResponseData;

public class GetListFromServer<T> {

    private Context context;
    private ListCommunicatorInterface<T> listCommunicatorInterface;

    public GetListFromServer(Context context, ListCommunicatorInterface<T> listCommunicatorInterface) {
        this.context = context;
        this.listCommunicatorInterface = listCommunicatorInterface;
    }

    public void getListFromServer(String url, Class<T> mclass, Map<String, String> params, String tag) {

        if (context == null || url == null) {
            if (listCommunicatorInterface != null)
                listCommunicatorInterface.onFailed("Process interrupted");
            return;
        }

        PostStringRequest baseAction = new PostStringRequest<>(url, params, responseData -> {
            if (responseData.getCode() == 200) {
                listCommunicatorInterface.onSuccess((List<T>) responseData.getData());
            } else {
                listCommunicatorInterface.onFailed(responseData.getMessage());
            }
        }, error -> listCommunicatorInterface.onError(error), null);
//
//        BaseHttpRequest baseHttpRequest = new BaseHttpRequest<>(url, ResponseData.class, params,
//                responseData -> {
//                    if (responseData.getStatus()) {
//                        listCommunicatorInterface.onSuccess((List<T>) responseData.getData());
//                    } else {
//                        listCommunicatorInterface.onFailed(responseData.getMessage());
//                    }
//                },
//                error -> listCommunicatorInterface.onError(error), new BaseDeserializerList<>(ResponseData.class, mclass));

        YalpApplication.getInstance().addToRequestQueue(baseAction, "tag");

    }

    public interface ListCommunicatorInterface<T> {
        void onError(VolleyError error);

        void onSuccess(List<T> updatedList);

        void onFailed(String message);
    }


}
