package com.cointizen.paysdk.payment.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.cointizen.paysdk.config.MCHConstant;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroInstance {
    private static RetroInstance instance;

    private static Retrofit retrofit = null;
    static ApiDataInterface anInterface;

    public static void init() {
        if (instance == null) instance = new RetroInstance();
    }

    private RetroInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.writeTimeout(120, TimeUnit.SECONDS);
        client.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                })
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        client.readTimeout(120, TimeUnit.SECONDS);
        client.connectTimeout(120, TimeUnit.SECONDS);
        client.retryOnConnectionFailure(false);
        client.addInterceptor(new NetInterceptor());


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MCHConstant.getInstance().getSdkBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client.build())
                    .build();
            anInterface = retrofit.create(ApiDataInterface.class);
        }
    }

    public static ApiDataInterface getRetrofitInstance() {
        return anInterface;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

}
