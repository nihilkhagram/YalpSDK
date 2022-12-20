package com.cointizen.paysdk.payment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cointizen.paysdk.R;
import com.cointizen.paysdk.activity.OttUPayPaymentActivity;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.payment.models.ApiResponse;
import com.cointizen.paysdk.payment.retrofit.RetroInstance;
import com.cointizen.util.Base64Util;
import com.cointizen.paysdk.utils.ToastUtil;
import com.google.gson.Gson;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.paymentsheet.PaymentSheetResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class OttUpayPaymentRequest {

    AppCompatActivity activity;
    private static final String TAG = "OttUpayPaymentRequest";
    Fragment fragment;
    AlertDialog loadingDialog;
    Handler mHandler;
    MCTipDialog mcTipDialog;
    int pollingCounter = 0;

    PaymentSheetResultCallback paymentSheetResultCallback = new PaymentSheetResultCallback() {
        @Override
        public void onPaymentSheetResult(@NonNull PaymentSheetResult paymentSheetResult) {
        }
    };

    public OttUpayPaymentRequest(AppCompatActivity activity) {
        this.activity = activity;
        RetroInstance.init();
    }

    /**
     * Once Payment intent is fetched then call this method to initiate a payment
     **/
    public void onPayClicked() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration("GameSphere Inc.");
        // Present Payment Sheet
    }

    /**
     * This method handles the payment results.
     **/


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

    public void doPayment(String params) {
        showLoadingDialog();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params);

        retrofit2.Call<String> paymentIntentCall = RetroInstance.getRetrofitInstance()
                .createYalpPayment("ott_upay", requestBody);

        paymentIntentCall.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    String encodedPaymentIntent = response.body();
                    String body = Base64Util.decode(encodedPaymentIntent);
                    if(body == null) {
                        showAlert("Error Occurred", "Something went wrong! Payment can't be done right now.");
                        return;
                    }

                    Gson gson = new Gson();
                    ResponseObject responseObject = gson.fromJson(body, ResponseObject.class);
                    Intent intent = new Intent(activity, OttUPayPaymentActivity.class);
                    intent.putExtra("data", responseObject.data.codeUrl);
                    Log.d(TAG, responseObject.data.codeUrl);
                    activity.startActivityForResult(intent,5001);
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
                        }
                    }
                }else{
                    // TODO: 26/05/22 Order Failed
                    showCustomToastAndFinish("Order Verification Failed: " + response.body());

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                // TODO: 26/05/22 Something went wrong
                ToastUtil.showToastInThread(activity, "支付过程中发生错误，请联系客服");
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
        btnOK.setOnClickListener(v -> dialog.dismiss());


    }

    public static class ResponseObject {
        int code;
        String msg;
        ResponseData data;

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

        public Object getData() {
            return data;
        }

        public void setData(ResponseData data) {
            this.data = data;
        }
    }

    private static class ResponseData {
        String rspCode;
        String rspMsg;
        String codeUrl;
        int amount;

        public String getRspCode() {
            return rspCode;
        }

        public void setRspCode(String rspCode) {
            this.rspCode = rspCode;
        }

        public String getRspMsg() {
            return rspMsg;
        }

        public void setRspMsg(String rspMsg) {
            this.rspMsg = rspMsg;
        }

        public String getCodeUrl() {
            return codeUrl;
        }

        public void setCodeUrl(String codeUrl) {
            this.codeUrl = codeUrl;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
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

