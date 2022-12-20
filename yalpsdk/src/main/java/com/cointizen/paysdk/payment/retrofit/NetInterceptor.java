package com.cointizen.paysdk.payment.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetInterceptor implements Interceptor {

    private static final String TAG = "NetInterceptor";


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .build();
        Response response = chain.proceed(newRequest);

        return response;
    }
}
