package com.cointizen.streaming.bean;


import com.cointizen.streaming.http.ResponseData;

public class ChannelData extends ResponseData {

    String uid_s;
    String channel_name;
    String tokenuid_subscriber;
    String user_id;

    public String getUid_s() {
        return uid_s;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public String getTokenuid_subscriber() {
        return tokenuid_subscriber;
    }

    public String getUser_id() {
        return user_id;
    }
}
