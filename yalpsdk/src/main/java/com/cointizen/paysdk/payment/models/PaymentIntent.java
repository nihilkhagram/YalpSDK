package com.cointizen.paysdk.payment.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentIntent {

    @SerializedName("paymentIntentSecret")
    @Expose
    private String paymentIntentSecret;
    @SerializedName("ephemeralSecret")
    @Expose
    private String ephemeralSecret;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("publicKey")
    @Expose
    private String publicKey;

    public String getPaymentIntentSecret() {
        return paymentIntentSecret;
    }

    public void setPaymentIntentSecret(String paymentIntentSecret) {
        this.paymentIntentSecret = paymentIntentSecret;
    }

    public String getEphemeralSecret() {
        return ephemeralSecret;
    }

    public void setEphemeralSecret(String ephemeralSecret) {
        this.ephemeralSecret = ephemeralSecret;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
