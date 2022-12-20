package com.cointizen.streaming.bean;

import com.cointizen.streaming.http.ResponseData;

public class CreateChannelResponse extends ResponseData {

    String tokenuid_publisher;
    String channel_name;
    String uid;

    public String getToken_uid_publisher() {
        return tokenuid_publisher;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public String getUid() {
        return uid;
    }
}
