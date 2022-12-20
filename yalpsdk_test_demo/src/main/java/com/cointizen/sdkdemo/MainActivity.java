package com.cointizen.sdkdemo;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cointizen.open.YalpApplication;
import com.cointizen.open.YalpGamesSdk;
import com.cointizen.open.RoleInfo;
import com.cointizen.open.GPExitResult;
import com.cointizen.open.GPUserResult;
import com.cointizen.open.IGPExitObsv;
import com.cointizen.open.UserLoginCallback;
import com.cointizen.open.OrderInfo;
import com.cointizen.open.PaymentCallback;
import com.cointizen.open.UploadCharacterCallBack;
import com.cointizen.open.LogoutCallback;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.channel.sign2.WalleChannelReader;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.streaming.PostStringRequest;
import com.cointizen.streaming.bean.StreamingChannel;
import com.cointizen.streaming.deserializer.BaseDeserializer;
import com.cointizen.streaming.http.ResponseData;
import com.cointizen.util.StoreSettingsUtil;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private String currencyPrice = "";  // 500 for USD cents
    private int amount;
    private Activity activity;
    private String extra_param;
    private TextView appInfo;
    private EditText etPrice;
    String gmail;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        activity = this;
        setContentView(R.layout.activity_sdk_main);
        sdkInit();
        initView();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    AccountManager manager = (AccountManager) activity.getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();

                    for (Account account : list) {
                        if (account.type.equalsIgnoreCase("com.google")) {
                            gmail = account.name;
                            break;
                        }
                    }
                }
            }
            else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getLoginGmail() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.GET_ACCOUNTS }, 100);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) ==
                    PackageManager.PERMISSION_GRANTED) {
                AccountManager manager = (AccountManager) activity.getSystemService(ACCOUNT_SERVICE);
                Account[] list = manager.getAccounts();

                for (Account account : list) {
                    if (account.type.equalsIgnoreCase("com.google")) {
                        gmail = account.name;
                        break;
                    }
                }
            }
        }

        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        YalpGamesSdk.getYalpGamesSdk().onActivityResult(requestCode, resultCode, data);
    }

    /**
     * SDK Initialization
     */
    private void sdkInit() {

        // 1. Initialize SDK
        YalpGamesSdk.initialize(this);
        // set up login callback
        YalpGamesSdk.setLoginCallback(loginCallback);
        // 2. Set Account logout monitoring initialization
        YalpGamesSdk.setLogoutCallback(logoutCallback);

        YalpGamesSdk.allowPrivacy(activity, this::userPrivacy);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView() {
        // Set up game settings (don't need to be integrated with the game)
        findViewById(R.id.btn_privacy_dialog).setOnClickListener(v -> {
            YalpGamesSdk.allowPrivacy(activity, result -> {
                if (result == 1) {
                    // Display agreement to players
                    YalpGamesSdk.getYalpGamesSdk().startLogin(activity);
                }
            });
        });


        // Login button
        findViewById(R.id.login).setOnClickListener(v -> {
            // If player isn't logged in, pop up login window
            if (!YalpGamesSdk.getYalpGamesSdk().isLogin()) {
                YalpGamesSdk.getYalpGamesSdk().startLogin(activity);
            }
        });

        // Login button
        findViewById(R.id.testLoginAction).setOnClickListener(v -> {
            // If player isn't logged in, pop up login window
            Map<String, String> params = new HashMap<>();
            params.put("account", "user001");
            params.put("password", "123456");
            YalpApplication.getInstance().getRequestQueue().start();
            PostStringRequest<ResponseData> loginRequest = new PostStringRequest<>(
                    MCHConstant.getInstance().getPlatformUserLoginUrl(),
                    params, responseData -> {
                        System.out.println("callback");
            }, listener -> {
                System.out.println("listener: " + listener.getMessage());
            }, null);
            YalpApplication.getInstance().addToRequestQueue(loginRequest, "login");

        });

        etPrice = findViewById(R.id.price);
        parseAmount();

        // Purchase button
        findViewById(R.id.btnPay).setOnClickListener(this::topupOnClick);

        // Upload a character
        findViewById(R.id.btnUploadRole).setOnClickListener(this::uploadCharacterOnClick);

        // Logout button
        findViewById(R.id.btnLogout).setOnClickListener(this::logoutOnClick);

        // Exit app button
        findViewById(R.id.btnExit).setOnClickListener(this::exitOnClick);

        // Watch Streaming Button        
        
        findViewById(R.id.watchLiveStreaming).setOnClickListener(YalpGamesSdk.getYalpGamesSdk()::startNewScreen);

        findViewById(R.id.joinRoom).setOnClickListener(YalpGamesSdk.getYalpGamesSdk()::joinStream);
        
        this.appInfo = findViewById(R.id.appInfo);
        appInfo.setText(
                "Channel: " + WalleChannelReader.getChannel(this) +
                "\nStore Id: " + StoreSettingsUtil.getStoreId() +
                "\nStore Name: " + StoreSettingsUtil.getStoreName() +
                "\nGame Id: " + SdkDomain.getInstance().getGameId() +
                "\nGame Name: " + SdkDomain.getInstance().getGameName() +
                "\nGame Version: " + StoreSettingsUtil.getGameVersion() +
                "\nEmail: " + getLoginGmail());
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(currencyPrice)){
                    etPrice.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = 0;
                    try {
                        parsed = Double.parseDouble(cleanString);
                        parseAmount();
                    } catch (IllegalInputException e) {
                        etPrice.setText(currencyPrice);
                        return;
                    }
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    currencyPrice = formatted;
                    etPrice.setText(formatted);
                    etPrice.setSelection(formatted.length());
                    etPrice.addTextChangedListener(this);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void parseAmount() throws IllegalInputException {
        String cleanString = etPrice.getText().toString().replaceAll("[$,.]", "");
        try {
            double parsed = Double.parseDouble(cleanString);
            this.amount = (int) parsed;
        } catch (Exception e) {
            throw new IllegalInputException();
        }
    }

    /**
     * Player login callback
     */
    private UserLoginCallback loginCallback = new UserLoginCallback() {
        @Override
        public void onFinish(GPUserResult result) {
            switch (result.getmErrCode()) {
                case GPUserResult.USER_RESULT_LOGIN_FAIL:
                    Log.e(TAG, "SDK login callback：Login failed");
                    break;
                case GPUserResult.USER_RESULT_LOGIN_SUCC:
                    String uid = result.getAccountNo();     // User Id（unique ID of user）
                    String token = result.getToken();       // User Token
                    extra_param = result.getExtra_param();  //sdk predefined parameter, pass to SDK when starting a payment
                    Log.w(TAG,"SDK login succeeded, "+"userid = " + uid + "，token = " + token);
                    // The game needs to verify login status with the user id and the token（See docs for YalpSDK Login API notification）
                    break;
            }
        }
    };


    /**
     * Logout callback
     */
    private LogoutCallback logoutCallback = new LogoutCallback() {
        @Override
        public void logoutResult(String result) {
            if (TextUtils.isEmpty(result)) {
                return;
            }
            if ("1".equals(result)) {
                Log.i(TAG, "SDK logout: Logout succeeded");
                YalpGamesSdk.getYalpGamesSdk().stopFloating(activity); // Turn off the floating button
                YalpGamesSdk.getYalpGamesSdk().startLogin(activity); // Pop up login window
            } else {
                Log.e(TAG, "SDK logout: Logout failed");
            }
        }
    };


    /**
     * Upload character callback
     */
    private UploadCharacterCallBack uploadCharacterCallBack = new UploadCharacterCallBack() {
        @Override
        public void onUploadComplete(String result) {
            if ("1".equals(result)) {
                Log.i(TAG,"SDK upload character callback: succeeded");
                Toast.makeText(activity,"Successfully uploaded character to the server", Toast.LENGTH_LONG).show();
            }else {
                Log.e(TAG,"SDK upload character callback：failed");
                Toast.makeText(activity,"Failed to upload character to the server", Toast.LENGTH_LONG).show();
            }
        }
    };


    /**
     * Payment result callback
     */
    private PaymentCallback paymentCallback = new PaymentCallback() {
        @Override
        public void callback(String errorcode) {
            Log.d(TAG,"Payment result callback");
            if (TextUtils.isEmpty(errorcode)) {
                return;
            }
            Log.d(TAG,"SDK payment callback：" + errorcode);
            Log.w(TAG, errorcode);
            if ("0".equals(errorcode)) {
                Log.d(TAG,"SDK payment callback：succeeded");
                Toast.makeText(activity, "Payment done successfully", Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG,"SDK payment callback：failed");
                Toast.makeText(activity, "Payment failed", Toast.LENGTH_LONG).show();
            }
        }
    };


    /**
     * Exit app callback
     */
    private IGPExitObsv mExitObsv = new IGPExitObsv() {
        @Override
        public void onExitFinish(GPExitResult exitResult) {
            switch (exitResult.mResultCode) {
                case GPExitResult.GPSDKExitResultCodeCloseWindow:
                    Log.i(TAG, "SDK exit app callback: [Cancel] button clicked.");
                    break;
                case GPExitResult.GPSDKExitResultCodeExitGame:
                    Log.i(TAG, "SDK exit app callback: [OK] button clicked, exit the app");
                    YalpGamesSdk.getYalpGamesSdk().stopFloating(activity);  /// turn off the floating button
                    // Exit app
                    finish();
                    System.exit(0);
                    break;
            }
        }
    };

    /**
     * Activity lifecycle method: onResume()
     * The game comes to the front from the background, timing will start, floating button will show up
     */
    @Override
    protected void onResume() {
        super.onResume();
        YalpGamesSdk.getYalpGamesSdk().onResume(activity);   //Play gets online: start timing & check if the floating button should show up
    }

    /**
     * Activity lifecycle method: onPause()
     * Game is switched to background, floating button will be hidden
     */
    @Override
    protected void onPause() {
        super.onPause();
        YalpGamesSdk.getYalpGamesSdk().onPause(activity);
    }

    /**
     * Activity lifecycle method: onStop()
     * When game runs in background or exits, inform the server that the device is offline
     */
    @Override
    protected void onStop() {
        super.onStop();
        YalpGamesSdk.getYalpGamesSdk().onStop(activity);  // device goes offline, do statistics
    }


    /**
     * Activity lifecycle method: onDestroy()
     * When game exits, close popup windows and stop all other functions
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        YalpGamesSdk.getYalpGamesSdk().onDestroy();  // Close all popup windows and stop all other functions
    }


    /**
     * Back button, pop up exit app confirmation window
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            YalpGamesSdk.getYalpGamesSdk().exitDialog(activity, mExitObsv);  // pop up exit app confirmation window
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void topupOnClick(View view) {
        // set up order information, invoke payment screen (all parameters are mandatory, "0" for no value or empty）
        OrderInfo order = new OrderInfo();
        order.setProductName("Lightning Sword");                // Name of the in-game item
        order.setProductDesc("As fast as the lightning");  // Description of the in-game item
        order.setAmount(this.amount);                     // Price (cent)
        order.setServerName("Server001");                 // Server name
        order.setGameServerId("12");                    // Server id
        order.setRoleName("NightKnight");                 // Character name
        order.setRoleId("10");                          // Character ID
        order.setRoleLevel("70");                       // Level
        order.setGoodsReserve("more information");               // other information of the order
        order.setExtra_param(extra_param);  // Predefined value (platform domain name by default. Returned by the SDK login callback and no need to change）
        // Game callback parameter, will be posted back with the callback requests from the platform to the game.
        // can be the order # in the game（Timestamp is used as the order number from the game in the demo code）
        order.setExtendInfo(String.valueOf(System.currentTimeMillis()));
        YalpGamesSdk.getYalpGamesSdk().pay(activity, order, paymentCallback);
    }

    private void uploadCharacterOnClick(View view) {
        // Set up character information (all parameters are mandatory, "0" for no value or empty）
        RoleInfo userInfo = new RoleInfo();
        userInfo.setServerId("15");         // Server id
        userInfo.setServerName("Server001");  // Server name
        userInfo.setRoleName("General Tao");  // Character name
        userInfo.setRoleId("20");           // Character ID
        userInfo.setRoleLevel("170");        // Character level
        userInfo.setRoleCombat("500000");   // Character combat effectiveness
        userInfo.setPlayerReserve("more information");   // other information of the character
        YalpGamesSdk.getYalpGamesSdk().uploadCharacter(userInfo, uploadCharacterCallBack);
    }

    private void logoutOnClick(View v) {
        // Logout current user
        YalpGamesSdk.logout(activity);
    }

    private void exitOnClick(View v) {
        // Pop up confirmation window to confirm exit
        YalpGamesSdk.exitDialog(activity, mExitObsv);
    }

    private void userPrivacy(int result) {
        if (result == 1) {
            // Privacy Policy
            YalpGamesSdk.getYalpGamesSdk().startLogin(activity);
        }
    }
}