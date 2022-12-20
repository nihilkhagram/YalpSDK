package com.cointizen.paysdk.payment.retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiDataInterface {

    @POST("pay/stripe_pay")
    Call<String> createPaymentIntent(@Body RequestBody requestBody);

    @POST("pay/and_weixin_pay")
    Call<String> createWechatPaymentIntent(@Body RequestBody requestBody);

    @GET("order/status/order_num/{intent_id}")
    Call<String> checkOrderStatus(@Path("intent_id") String intent_id);

    @POST("pay/mgate_pay")
    Call<String> createMGatePayment(@Body RequestBody requestBody);

    @POST("pay/yalp_pay")
    Call<String> createYalpPayment(@Query("pay_method") String payMethod, @Body RequestBody requestBody);

}
