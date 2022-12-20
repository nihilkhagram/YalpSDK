package com.cointizen.paysdk.payment;

import android.app.AlertDialog;
import android.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.payment.models.ApiResponse;
import com.cointizen.util.Base64Util;
import com.google.gson.Gson;
import com.cointizen.paysdk.R;
import com.cointizen.paysdk.payment.models.PaymentIntent;
import com.cointizen.paysdk.payment.retrofit.RetroInstance;
import com.cointizen.util.Base64;
import com.cointizen.paysdk.utils.ToastUtil;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.paymentsheet.PaymentSheetResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class StripePaymentRequest {

    AppCompatActivity activity;
    String BACKEND_URL = "";
    private static final String TAG = "StripePaymentRequest";
    private String paymentIntentClientSecret;
    PaymentSheet paymentSheet;
    Fragment fragment;
    AlertDialog loadingDialog;
    Handler mHandler;
    MCTipDialog mcTipDialog;
    int pollingCounter = 0;


    PaymentSheetResultCallback paymentSheetResultCallback = new PaymentSheetResultCallback() {
        @Override
        public void onPaymentSheetResult(@NonNull PaymentSheetResult paymentSheetResult) {
            onSheetResult(paymentSheetResult);
        }
    };

    public StripePaymentRequest(AppCompatActivity activity) {
        this.activity = activity;
        paymentSheet = new PaymentSheet(activity, paymentSheetResultCallback);
        RetroInstance.init();
    }

    /**
     * Once Payment intent is fetched then call this method to initiate a payment
     **/
    public void onPayClicked() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration("GameSphere Inc.");
        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);
    }

    /**
     * This method handles the payment results.
     **/
    public void onSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // TODO: 26/05/22 perform polling here to check whether the order is completed or not.
            Log.i(TAG, "onSheetResult: "+ paymentIntentClientSecret);
            String paymentIntentId = paymentIntentClientSecret.substring(0, paymentIntentClientSecret.indexOf("_secret"));
            String clientSecret = paymentIntentClientSecret.substring(paymentIntentClientSecret.indexOf("_secret") + 1);
            checkOrderStatus(paymentIntentId);

        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.e(TAG, "onSheetResult: cancelled ");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "onSheetResult: failed "+ ((PaymentSheetResult.Failed) paymentSheetResult).getError().getLocalizedMessage());
//            Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
//            showAlert("Payment failed", error.getLocalizedMessage());
                showErrorDialog();
        }
    }

    private JSONObject parseResponse(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new JSONObject(responseBody.string());
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error parsing response", e);
            }
        }

        return new JSONObject();
    }

    private void showAlert(String title, @Nullable String message) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create();
        dialog.show();
    }

    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_loading_stripe, null);
        builder.setView(view);
        loadingDialog = builder.create();
        loadingDialog.show();
    }

    public void createPaymentIntent(String params, boolean isWePay) {
        showLoadingDialog();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params);

        retrofit2.Call<String> paymentIntentCall;
                if (!isWePay){
                    paymentIntentCall = RetroInstance.getRetrofitInstance().createYalpPayment("stripe", requestBody);
                }else {
                    paymentIntentCall = RetroInstance.getRetrofitInstance().createWechatPaymentIntent(requestBody);
                }

        paymentIntentCall.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    String encodedPaymentIntent = response.body();
                    byte[] decode = Base64.decode(encodedPaymentIntent + "=");
                    if(decode == null) {
                        return;
                    }
                    PaymentIntent paymentIntent = null;
                    try {
                        String body = new String(decode, "utf-8");
                        Log.e(TAG, "onResponse: decoded response==> "+body);
                        Gson gson = new Gson();
                        ResponseObject responseObject = gson.fromJson(body, ResponseObject.class);
                        paymentIntentClientSecret = responseObject.data.getPaymentIntentSecret();
                        PaymentConfiguration.init(activity,  responseObject.data.getPublicKey());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (response.code() == 200) {
                        Log.e(TAG, "onResponse: json " + new Gson().toJson(paymentIntent));
                        onPayClicked();
                    } else {
                        showAlert("Error Occurred", "Something went wrong! Payment can't be done right now.");
                    }
                } else {
                    showErrorDialog();
                    ToastUtil.showToastInThread(activity, "Error Occurred!");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                showErrorDialog();
            }
        });
    }

    //polling
    public void checkOrderStatus(final String intentId){
        retrofit2.Call<String>  call = RetroInstance.getRetrofitInstance().checkOrderStatus(intentId);//Don't forget to hide url
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()){
                    String body = Base64Util.decode(response.body());
                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(body, ApiResponse.class);
                    if (apiResponse.getData().getStatus() == 1){
                        showCustomToastAndFinish("Payment done successfully!");
//                        ToastUtil.showToastInThread(activity, "Payment done successfully! ");
//                        if (loadingDialog != null && loadingDialog.isShowing()) {
//                            loadingDialog.dismiss();
//                        }
//
//                        activity.finish();
                        //close the payment window and loading dialog
                    }else{
                        if (pollingCounter < 5) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    checkOrderStatus(intentId);
                                    pollingCounter++;
                                }
                            }, 800);
                        }else {
                            pollingCounter = 0;
                            showCustomToastAndFinish("支付过程中发生错误，请重试");
//                            ToastUtil.showToastInThread(activity, "支付过程中发生错误，请重试");
//                            if (loadingDialog != null && loadingDialog.isShowing()) {
//                                loadingDialog.dismiss();
//                            }
//                            activity.finish();
                            //tried multiple times but the order status is still zero, close the loading dialog and let user retry
                        }
                    }
                }else{
                    // TODO: 26/05/22 Order Failed
                    showCustomToastAndFinish("Order Verification Failed: " + response.body());
                   // ToastUtil.showToastInThread(activity, "Order Verification Failed");

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                if (t.getLocalizedMessage().contains("unexpected end of stream")) {
                    // exception happens but succeeded actually: E/StripePaymentRequest: onFailure: unexpected end of stream on Connection{api.cointizen.com:443,
                    // proxy=DIRECT hostAddress=api.cointizen.com/54.250.68.60:443 cipherSuite=TLS_AES_256_GCM_SHA384 protocol=http/1.1}
                    ToastUtil.showToastInThread(activity, "Payment done successfully");
                }
                else {
                    // TODO: 26/05/22 Something went wrong
                    ToastUtil.showToastInThread(activity, "支付过程中发生错误，请联系客服");
                }
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                activity.finish();
            }
        });

    }

    private void showCustomToastAndFinish(String message) {
        mcTipDialog = new MCTipDialog.Builder(false).setMessage(message).show(activity, activity.getFragmentManager());
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        },3000);

    }

    private void showErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_error_payment,null);
        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Button btnOK = view.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public static class ResponseObject {
        int code;
        String msg;
        PaymentIntent data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public PaymentIntent getData() {
            return data;
        }

        public void setData(PaymentIntent data) {
            this.data = data;
        }
    }
    private void noticeResult(int type, Object obj) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = obj;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }
}

