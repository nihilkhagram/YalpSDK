package com.cointizen.streaming.http;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public T data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void release() {
        message = null;
        data = null;
        callGC();
    }

    private void callGC() {
    }

}
