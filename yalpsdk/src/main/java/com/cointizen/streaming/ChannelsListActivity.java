package com.cointizen.streaming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cointizen.paysdk.R;
import com.cointizen.streaming.adpaters.ChannelsActivityAdapter;
import com.cointizen.streaming.bean.ChannelData;
import com.cointizen.streaming.bean.StreamingChannel;

import com.cointizen.streaming.actions.GetListFromServer;
import com.cointizen.streaming.actions.GetSingleItem;

public class ChannelsListActivity extends AppCompatActivity implements ChannelsActivityAdapter.OnChannelClick{

    RecyclerView recyclerViewChannels;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels_list);

        recyclerViewChannels = findViewById(R.id.recycler_view_channels);
        progressDialog = new ProgressDialog(ChannelsListActivity.this);
        getSupportActionBar().hide();

        getChannels();
    }

    public void getChannels(){
        progressDialog.show();
        Map<String, String> param = new HashMap<>();
        param.put("action", "read");

        GetListFromServer getListFromServer = new GetListFromServer<>(getApplicationContext(), new GetListFromServer.ListCommunicatorInterface<StreamingChannel>() {
            @Override
            public void onError(VolleyError error) {
                Log.e("MyLogData", "onError-->  " + error);
            }

            @Override
            public void onSuccess(List<StreamingChannel> updatedList) {
                progressDialog.dismiss();
                ChannelsActivityAdapter channelsActivityAdapter = new ChannelsActivityAdapter(getApplicationContext(), updatedList, ChannelsListActivity.this);
                recyclerViewChannels.setAdapter(channelsActivityAdapter);
            }

            @Override
            public void onFailed(String message) {
                Log.e("MyLogData", "onFailed-->  " + message);
            }
        });
        getListFromServer.getListFromServer(ServerHelper.GET_CHANNELS, StreamingChannel.class, param, "tag");
    }

    public void getChannelData(String userId){

        Map<String, String> param = new HashMap<>();
        param.put("data_id", userId);
        Log.e("MyLogData", ""+userId);

        GetSingleItem<ChannelData> getSingleItem = new GetSingleItem<ChannelData>(getApplicationContext(), new GetSingleItem.ListCommunicatorInterface<ChannelData>() {
            @Override
            public void onError(VolleyError error) {
                Log.e("MyLogData", "onError-->  "+error);
            }

            @Override
            public void onSuccess(ChannelData updatedList) {
                Intent intent = new Intent(ChannelsListActivity.this, ActivityWatchStream.class);
                intent.putExtra("channelName", updatedList.getChannel_name());
                intent.putExtra("subscriberToken", updatedList.getTokenuid_subscriber());
                intent.putExtra("userId", updatedList.getUid_s());
                startActivity(intent);
            }

            @Override
            public void onFailed(String message) {
                Log.e("MyLogData", "onFailed-->  "+message);
            }
        });
        getSingleItem.getItemFromServer(ServerHelper.CREATE_CHANNEL, ChannelData.class,  param, "tag");
    }

    @Override
    public void onChannelClick(String userId) {
        Log.e("MyLogData", "Adapter CLick-->  " + userId);
        getChannelData(userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChannels();
    }
}