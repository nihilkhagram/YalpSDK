package com.cointizen.streaming.http;

public interface CallbackInterface<T> {

    public void passResult(T responseData);
}
