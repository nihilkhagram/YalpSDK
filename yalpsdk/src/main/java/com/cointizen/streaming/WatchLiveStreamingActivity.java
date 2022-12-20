package com.cointizen.streaming;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.R;
import com.cointizen.paysdk.utils.AssetsUtils;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.streaming.bean.CreateChannelResponse;
import com.cointizen.streaming.actions.GetSingleItem;
import com.cointizen.streaming.http.ResponseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.ScreenCaptureParameters;
import io.agora.rtc2.video.CameraCapturerConfiguration;
import io.agora.rtc2.video.VideoCanvas;

public class WatchLiveStreamingActivity extends Activity {

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS =
            {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            };

    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    void showMessage(String message) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }

    // Fill the App ID of your project generated on Agora Console.
    private final String appId = "7865d4a952ff4e20a86b7d2fe561ae48";
    // Fill the channel name.
    private String channelName = "7d72365eb983485397e3e3f9d460bdda1670573814";
    // Fill the temp token generated on Agora Console.
    private String token = "0067865d4a952ff4e20a86b7d2fe561ae48IACrDiGzGK2PckM4dP0Xuo7zjONA3dE2KQTWp5c6LJ23ISYMqS/2JUJ2IgBXzwAAdkCUYwQAAQA2bZhjAwA2bZhjAgA2bZhjBAA2bZhj";


    private String Dummytoken = "etrgaslibuasgriefgrbgrehgnoweriuhguergqfreutihg54789yhgerjbgljfdnb;lgfknbjndg;fknlj=";


    // An integer that identifies the local user.
    private int uid = 1670573814;
    private boolean isJoined = false;

    private RtcEngine agoraEngine;
    //SurfaceView to render local video in a Container.
    private SurfaceView localSurfaceView;
    //SurfaceView to render Remote video in a Container.
    private SurfaceView remoteSurfaceView;


    private SeekBar volumeSeekBar;
    private CheckBox muteCheckBox;
    private int volume = 50;
    private int remoteUid = 0; // Stores the uid of the remote user

    // Screen sharing
    private final int DEFAULT_SHARE_FRAME_RATE = 10;
    private boolean isSharingScreen = false;
    private Intent fgServiceIntent;

    Button pipMode, joinChannel, leaveChannel;
    ImageView swapCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_live_streaming);
        String game = AssetsUtils.getInstance().getGameId(getApplicationContext());
        Log.e("MyLogData" , "gajsdfgkhjhasdfjkhasdkj  == > " + game);
        pipMode = findViewById(R.id.PipMode);
        joinChannel = findViewById(R.id.JoinButton);
        leaveChannel = findViewById(R.id.LeaveButton);
        swapCamera = findViewById(R.id.imageViewSwitchCamera);

        leaveChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isJoined) {
                    deleteChannel();

                }
            }
        });

        Log.e("MyLogData  ", "getLoginAccount   " + SharedPreferencesUtils.getInstance().getToken(getApplicationContext()));

        pipMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterPipMode();
            }
        });

        joinChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isJoined && !isSharingScreen) {
                    openGetTitleDialog();
                }
                //createNewChannel();
            }
        });

        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        setupVideoSDKEngine();

        volumeSeekBar = (SeekBar) findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
                agoraEngine.adjustRecordingSignalVolume(volume);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //Required to implement OnSeekBarChangeListener
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Required to implement OnSeekBarChangeListener
            }
        });

        muteCheckBox = (CheckBox) findViewById(R.id.muteCheckBox);
        muteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agoraEngine.muteRemoteAudioStream(remoteUid, isChecked);
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
            showMessage(e.toString());
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote host joining the channel to get the uid of the host.
        public void onUserJoined(int uid, int elapsed) {
            showMessage("Remote user joined " + uid);
            remoteUid = uid;
            // Set the remote video view
            //runOnUiThread(() -> setupRemoteVideo(uid));
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            isJoined = true;
            showMessage("Joined Channel " + channel);
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            showMessage("Remote user offline " + uid + " " + reason);
            //runOnUiThread(() -> remoteSurfaceView.setVisibility(View.GONE));
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            showMessage("Error" + err);
        }
    };

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        remoteSurfaceView = new SurfaceView(getBaseContext());
        remoteSurfaceView.setZOrderMediaOverlay(true);
        container.addView(remoteSurfaceView);
        agoraEngine.setupRemoteVideo(new VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
        // Display RemoteSurfaceView.
        remoteSurfaceView.setVisibility(View.VISIBLE);
    }

    boolean isFront;
    boolean isCameraChanging = false;

    private void setupLocalVideo() {
        FrameLayout container = findViewById(R.id.local_video_view_container);
        // Create a SurfaceView object and add it as a child to the FrameLayout.
        localSurfaceView = new SurfaceView(getBaseContext());
        container.addView(localSurfaceView);
        // Pass the SurfaceView object to Agora so that it renders the local video.
        agoraEngine.setupLocalVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        agoraEngine.setCameraCapturerConfiguration(new CameraCapturerConfiguration(CameraCapturerConfiguration.CAMERA_DIRECTION.CAMERA_REAR));
        isFront = true;
        findViewById(R.id.imageViewSwitchCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCameraChanging) {

                    isCameraChanging = true;
                    agoraEngine.setCameraCapturerConfiguration(new CameraCapturerConfiguration(isFront ? CameraCapturerConfiguration.CAMERA_DIRECTION.CAMERA_REAR : CameraCapturerConfiguration.CAMERA_DIRECTION.CAMERA_FRONT));
                    agoraEngine.stopPreview();
                    agoraEngine.startPreview();
                    if (isSharingScreen)
                    {
                        startScreenSharePreview(isSharingScreen);
                        // Update channel media options to publish the screen sharing video stream
                        updateMediaPublishOptions(isSharingScreen);
                    }


                    isFront = !isFront;
                    isCameraChanging = false;

                }



            }
        });
    }

    public void joinChannel(String token, String channelName, int uId) {
        if (checkSelfPermission()) {
            ChannelMediaOptions options = new ChannelMediaOptions();

            // For a Video call, set the channel profile as COMMUNICATION.
            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;
            // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
            // Display LocalSurfaceView.
            setupLocalVideo();
            localSurfaceView.setVisibility(View.VISIBLE);
            // Start local preview.
            agoraEngine.startPreview();
            // Join the channel with a temp token.
            // You need to specify the user ID yourself, and ensure that it is unique in the channel.
            agoraEngine.joinChannel(token, channelName, uId, options);
        } else {
            Toast.makeText(getApplicationContext(), "Permissions was not granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void leaveChannel() {
        if (!isJoined) {
            showMessage("Join a channel first");
        } else {
            agoraEngine.leaveChannel();
            leaveChannel.setVisibility(View.GONE);
            pipMode.setVisibility(View.GONE);
            joinChannel.setVisibility(View.VISIBLE);
            showMessage("You left the channel");
            // Stop remote video rendering.
            if (remoteSurfaceView != null) remoteSurfaceView.setVisibility(View.GONE);
            // Stop local video rendering.
            if (localSurfaceView != null) localSurfaceView.setVisibility(View.GONE);
            isJoined = false;
        }
    }

    public void shareScreen(View view) {
        Button sharingButton = (Button) view;
        if (!isSharingScreen) { // Start sharing
            // Ensure that your Android version is Lollipop or higher.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    fgServiceIntent = new Intent(this, YalpGamesSdk.getMainActivity().getClass());
                    startForegroundService(fgServiceIntent);
                }
                // Get the screen metrics
                /*DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);*/

                Display metrics = getWindowManager().getDefaultDisplay();

                // Set screen capture parameters
                ScreenCaptureParameters screenCaptureParameters = new ScreenCaptureParameters();
                screenCaptureParameters.captureVideo = true;
                screenCaptureParameters.videoCaptureParameters.width = metrics.getWidth();
                screenCaptureParameters.videoCaptureParameters.height = metrics.getHeight();
                screenCaptureParameters.videoCaptureParameters.framerate = DEFAULT_SHARE_FRAME_RATE;
                screenCaptureParameters.captureAudio = true;
                screenCaptureParameters.audioCaptureParameters.captureSignalVolume = 50;

                // Start screen sharing
                agoraEngine.startScreenCapture(screenCaptureParameters);
                isSharingScreen = true;
                startScreenSharePreview(true);
                // Update channel media options to publish the screen sharing video stream
                updateMediaPublishOptions(true);
                sharingButton.setText("Stop Screen Sharing");
            }
        } else { // Stop sharing
            agoraEngine.stopScreenCapture();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (fgServiceIntent != null) stopService(fgServiceIntent);
            }
            isSharingScreen = false;
            sharingButton.setText("Start Screen Sharing");
            // Restore camera and microphone publishing
            updateMediaPublishOptions(false);
            setupLocalVideo();
            startScreenSharePreview(false);
        }
    }

    private void startScreenSharePreview(boolean isSharingScreen) {
        // Create render view by RtcEngine
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = new SurfaceView(getBaseContext());
        /*if (container.getChildCount() > 0) {
            container.removeAllViews();
        }*/
        // Add to the local container
        container.removeAllViews();
        if (isSharingScreen) {
            container.addView(surfaceView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            // Setup local video to render your local camera preview
            agoraEngine.setupLocalVideo(new VideoCanvas(surfaceView, Constants.RENDER_MODE_FIT,
                    Constants.VIDEO_MIRROR_MODE_DISABLED,
                    Constants.VIDEO_SOURCE_SCREEN_PRIMARY,
                    0));

            agoraEngine.startPreview(Constants.VideoSourceType.VIDEO_SOURCE_SCREEN_PRIMARY);
        } else {
            container.removeAllViews();
        }
    }

    void updateMediaPublishOptions(boolean publishScreen) {
        ChannelMediaOptions mediaOptions = new ChannelMediaOptions();
        mediaOptions.publishCameraTrack = !publishScreen;
        mediaOptions.publishMicrophoneTrack = !publishScreen;
        mediaOptions.publishScreenCaptureVideo = publishScreen;
        mediaOptions.publishScreenCaptureAudio = publishScreen;
        agoraEngine.updateChannelMediaOptions(mediaOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isJoined && isSharingScreen) {
            enterPipMode();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isJoined && isSharingScreen) {
            enterPipMode();
            finish();
            deleteChannel();
        }
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        agoraEngine.stopPreview();
        agoraEngine.leaveChannel();
        if (isJoined) {
            deleteChannel();
        }
        /*try {
            if () {
            }
            agoraEngine.stopPreview();
            agoraEngine.leaveChannel();

            // Destroy the engine in a sub-thread to avoid congestion
            new Thread(() -> {
                try {
                    RtcEngine.destroy();
                    agoraEngine = null;
                }catch (Exception e)
                {
                    Log.d("myOnDestroy","onError");
                }
            }).start();
        }catch (Exception e)
        {
            Log.d("myOnDestroy","onError");

        }*/
    }

    /*private PictureInPictureParams updateActions() {
        ArrayList<RemoteAction> actions=new ArrayList<>();
        if (video.isPlaying()) {
            actions.add(buildRemoteAction(REQUEST_PAUSE,
                    R.drawable.ic_pause_white_24dp, R.string.pause, R.string.pause_desc));
        }
        else {
            actions.add(buildRemoteAction(REQUEST_PLAY,
                    R.drawable.ic_play_arrow_white_24dp, R.string.play, R.string.play_desc));
        }
        return(new PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .setActions(actions)
                .build());
    }*/

    public void enterPipMode() {
        Display d = getWindowManager()
                .getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int width = p.x;
        int height = p.y;



        Rational ratio
                = new Rational(3, 4);
        PictureInPictureParams.Builder pip_Builder = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            List<RemoteAction> remoteActions = new ArrayList<>();
           PendingIntent  pendingIntent= PendingIntent.getActivity(this, 0,
                    new Intent(this, WatchLiveStreamingActivity.class),0);
            RemoteAction remoteAction = new RemoteAction(Icon.createWithResource(this,R.drawable.ic_switch_camera),"Change Camera","Change Camera",pendingIntent);

            remoteActions.add(remoteAction);
            pip_Builder = new PictureInPictureParams.Builder();
            pip_Builder.setAspectRatio(ratio).build();
            pip_Builder.setActions(remoteActions);
            enterPictureInPictureMode(pip_Builder
                    .setAspectRatio(new Rational(10, 16))
                    .build());
        }



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this,"Click",Toast.LENGTH_LONG).show();
    }

    public void createNewChannel(String streamTitle) {

        Map<String, String> param = new HashMap<>();
        param.put("user_id", SharedPreferencesUtils.getInstance().getToken(getApplicationContext()));
        param.put("title", streamTitle);
        param.put("game_id", AssetsUtils.getInstance().getGameId(getApplicationContext()));
        param.put("game_name", AssetsUtils.getInstance().getGameName(getApplicationContext()));
        param.put("game_app_id", AssetsUtils.getInstance().getGameAppId(getApplicationContext()));
        Log.e("MyLogData", "Param-->  " + param);

        GetSingleItem<CreateChannelResponse> getSingleItem = new GetSingleItem<>(getApplicationContext(), new GetSingleItem.ListCommunicatorInterface<CreateChannelResponse>() {
            @Override
            public void onError(VolleyError error) {
                Log.e("MyLogData  ", "onError-->  " + error);
            }

            @Override
            public void onSuccess(CreateChannelResponse updatedList) {
                Log.e("MyLogData  ", "onSuccess-->   " + updatedList);
                joinChannel(updatedList.getToken_uid_publisher(), updatedList.getChannel_name(), Integer.parseInt(updatedList.getUid()));
                leaveChannel.setVisibility(View.VISIBLE);
                pipMode.setVisibility(View.VISIBLE);
                joinChannel.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(String message) {
                Log.e("MyLogData", "onFailed-->  " + message);
            }
        });
        getSingleItem.getItemFromServer(ServerHelper.CREATE_CHANNEL, CreateChannelResponse.class, param, "tag");
    }

    private void deleteChannel() {

        Map<String, String> param = new HashMap<>();
        param.put("type", "1");
        param.put("user_id", SharedPreferencesUtils.getInstance().getToken(getApplicationContext()));
        param.put("action", "action");
        Log.e("MyLogData", " PARAM  " + param);

        GetSingleItem<ResponseData> getSingleItem = new GetSingleItem<>(getApplicationContext(), new GetSingleItem.ListCommunicatorInterface<ResponseData>() {
            @Override
            public void onError(VolleyError error) {
                Log.e("MyLogData", " delete channel onError-->  " + error);
            }

            @Override
            public void onSuccess(ResponseData updatedList) {
                Log.e("MyLogData  ", " delete channel onSuccess-->   " + updatedList);
                leaveChannel();
            }

            @Override
            public void onFailed(String message) {
                Log.e("MyLogData", " delete channel onFailed-->  " + message);
            }
        });
        getSingleItem.getItemFromServer(ServerHelper.DELETE_CHANNEL, ResponseData.class, param, "tag");

    }

    private void openGetTitleDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.get_title_dialog);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dialog.show();

        EditText streamTitle = dialog.findViewById(R.id.edtStreamTitle);
        Button submit = dialog.findViewById(R.id.submitTitle);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String streamtitle = streamTitle.getText().toString();
                if (!streamtitle.equals("")) {
                    createNewChannel(streamtitle);
                    dialog.dismiss();
                } else {
                    Toast.makeText(WatchLiveStreamingActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {
            swapCamera.setVisibility(View.GONE);
        } else {
            swapCamera.setVisibility(View.VISIBLE);
        }
    }
}