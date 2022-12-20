package com.cointizen.streaming;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import com.cointizen.paysdk.R;
import com.cointizen.streaming.actions.GetSingleItem;
import com.cointizen.streaming.http.ResponseData;
import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class ActivityWatchStream extends AppCompatActivity {

    // Fill the App ID of your project generated on Agora Console.
    private final String appId = "7865d4a952ff4e20a86b7d2fe561ae48";
    // Fill the channel name.
    //private String channelName = "7d72365eb983485397e3e3f9d460bdda1670573814";
    // Fill the temp token generated on Agora Console.
    private String token = "0067865d4a952ff4e20a86b7d2fe561ae48IADa41cbkxqLZQgr9U+TZCNgYuqsvogmJlOcm6t9dN3Y5iYMqS/zuxRUEAA9QgAAdkCUYwEAAQA2bZhj";

    private String Dummytoken = "etrgaslibuasgriefgrbgrehgnoweriuhguergqfreutihg54789yhgerjbgljfdnb;lgfknbjndg;fknlj=";

    // An integer that identifies the local user.
    private int Uid;

    private boolean isJoined = false;

    private RtcEngine agoraEngine;
    //SurfaceView to render local video in a Container.
    private SurfaceView localSurfaceView;
    //SurfaceView to render Remote video in a Container.
    private SurfaceView remoteSurfaceView;
    Button watchStream;
    String channelName, subscriberToken, deletChannelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_stream);
        getSupportActionBar().hide();

        watchStream = findViewById(R.id.JoinStream);

        channelName = getIntent().getStringExtra("channelName");
        deletChannelId = getIntent().getStringExtra("deletChannelId");
        subscriberToken = getIntent().getStringExtra("subscriberToken");
        Uid = Integer.parseInt(getIntent().getStringExtra("userId"));
        Log.e("MyLogData", " deletChannelId  "+deletChannelId);


        setupVideoSDKEngine();

        joinChannel();

        watchStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinChannel();
            }
        });

    }

    private void setupVideoSDKEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(config);
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine.enableVideo();
        } catch (Exception e) {
            //showMessage(e.toString());
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote host joining the channel to get the uid of the host.
        public void onUserJoined(int uid, int elapsed) {
            showMessage("Remote user joined " + uid);
            //remoteUid = uid;
            // Set the remote video view
            runOnUiThread(() -> setupRemoteVideo(uid));
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            isJoined = true;
            showMessage("Joined Channel " + channel);
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            showMessage("Remote user offline " + uid + " " + reason);

            //deleteChannel(deletChannelId);

            finish();


            runOnUiThread(() -> remoteSurfaceView.setVisibility(View.GONE));
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            showMessage("Error" + err);
        }
    };

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.watch_stream_container);
        remoteSurfaceView = new SurfaceView(getBaseContext());
        remoteSurfaceView.setZOrderMediaOverlay(true);
        container.addView(remoteSurfaceView);
        agoraEngine.setupRemoteVideo(new VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
        // Display RemoteSurfaceView.
        remoteSurfaceView.setVisibility(View.VISIBLE);
    }

    public void joinChannel() {
        //if (checkSelfPermission()) {
        ChannelMediaOptions options = new ChannelMediaOptions();

        // For a Video call, set the channel profile as COMMUNICATION.
        options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;
        // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        // Display LocalSurfaceView.
        //setupLocalVideo();
        //localSurfaceView.setVisibility(View.VISIBLE);
        // Start local preview.
        agoraEngine.startPreview();
        // Join the channel with a temp token.
        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
        agoraEngine.joinChannel(subscriberToken, channelName, Uid, options);
        /*} else {
            Toast.makeText(getApplicationContext(), "Permissions was not granted", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void leaveChannel() {
        if (!isJoined) {
            agoraEngine.leaveChannel();
            showMessage("You left the channel");
            // Stop remote video rendering.
            if (remoteSurfaceView != null) remoteSurfaceView.setVisibility(View.GONE);
            // Stop local video rendering.
            if (localSurfaceView != null) localSurfaceView.setVisibility(View.GONE);
            isJoined = false;
        }
    }

    void showMessage(String message) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        leaveChannel();
    }

    protected void onDestroy() {
        super.onDestroy();
        agoraEngine.stopPreview();
        agoraEngine.leaveChannel();

        // Destroy the engine in a sub-thread to avoid congestion
        new Thread(() -> {
            RtcEngine.destroy();
            agoraEngine = null;
        }).start();
    }

    private void deleteChannel(String uid){

        Map<String, String> param = new HashMap<>();
        param.put("type", "1");
        param.put("user_id", uid);
        param.put("action", "action");
        Log.e("MyLogData", " PARAM  "+param);

        GetSingleItem<ResponseData> getSingleItem = new GetSingleItem<>(getApplicationContext(), new GetSingleItem.ListCommunicatorInterface<ResponseData>() {
            @Override
            public void onError(VolleyError error) {
                Log.e("MyLogData", " delete channel onError-->  "+error);
            }

            @Override
            public void onSuccess(ResponseData updatedList) {
                Log.e("MyLogData  ", " delete channel onSuccess-->   "+updatedList);
                /*try {
                    startActivity(new Intent(ActivityWatchStream.this , Class.forName("com.cointizen.paysdk.activity.MCHUserCenterActivity")));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }*/
                finish();
            }

            @Override
            public void onFailed(String message) {
                Log.e("MyLogData", " delete channel onFailed-->  "+message);
            }
        });
        getSingleItem.getItemFromServer(ServerHelper.DELETE_CHANNEL,ResponseData.class,  param, "tag");

    }

}