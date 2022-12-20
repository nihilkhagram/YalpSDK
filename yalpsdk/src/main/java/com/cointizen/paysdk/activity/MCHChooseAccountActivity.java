package com.cointizen.paysdk.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHSmallAccountAdapter;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.callback.AddAccountCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.AboutAddAccountDialog;
import com.cointizen.paysdk.dialog.AddAccountDialog;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.process.AddSmallAccountProgress;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.TextUtils;

import java.util.List;

/**
 * 描述：选择小号Activity
 * 时间: 2018-07-13 16:22
 */
public class MCHChooseAccountActivity extends MCHBaseActivity {
    private String TAG = "MCChooseAccountActivity";
    private TextView btnAddAccount;  //添加小号按钮
    private TextView btnAbout; //小号创建说明
    private TextView tvLoginNotificationSubject;
    private TextView tvLoginNotificationContent;
    private LinearLayout notificationLayout;
    private UserLogin userLogin;
    private ListView smaleAccountListView;
    private List<UserLogin.SmallAccountEntity> smallAccountList;
    private MCHSmallAccountAdapter adapter;
    private AddAccountDialog.addAccountBuilder dialogBuilder;
    private MCTipDialog mcTipDialog;
    private UserLogin.LoginNotification loginNotification;
    private ImageView btnBack;
    private boolean canShowToast = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setContentView(getLayout("mch_act_choose_account"));
        userLogin = (UserLogin) getIntent().getSerializableExtra("user_small_list");
        smallAccountList = userLogin.getSmallAccountList();
        loginNotification = userLogin.getLoginNotification();
        initView();
    }

    private void initView(){
        btnAddAccount = (TextView) findViewById(MCHInflaterUtils.getControl(this, "btn_tv_add_account"));
        btnAbout = (TextView) findViewById(MCHInflaterUtils.getControl(this, "btn_tv_explain_account"));
        smaleAccountListView = (ListView) findViewById(MCHInflaterUtils.getControl(this, "list_account"));
        btnBack = findViewById(MCHInflaterUtils.getControl(this, "btn_back"));
        notificationLayout = findViewById(MCHInflaterUtils.getControl(this, "login_notification"));

        tvLoginNotificationSubject = findViewById(MCHInflaterUtils.getControl(this, "login_notifcation_subject"));
        tvLoginNotificationContent = findViewById(MCHInflaterUtils.getControl(this, "login_notification_content"));

        if (userLogin == null){
            ToastUtil.show(MCHChooseAccountActivity.this,ActivityConstants.S_xsyiSYmUum);
            return;
        }
        if (loginNotification == null) {
            notificationLayout.setVisibility(View.GONE);
        }
        else {
            notificationLayout.setVisibility(View.VISIBLE);
            tvLoginNotificationSubject.setText(loginNotification.getSubject());
            tvLoginNotificationContent.setText(loginNotification.getContent());
        }

        adapter = new MCHSmallAccountAdapter(this);
        adapter.setLoginUser(userLogin);
        smaleAccountListView.setAdapter(adapter);

        if (smallAccountList.size()>0){
            adapter.setListData(smallAccountList);
        }else {
            canShowToast = false;
            mcTipDialog = new MCTipDialog.Builder().setMessage(ActivityConstants.S_mIUkgkwFNv).show(MCHChooseAccountActivity.this,getFragmentManager());
            AddSmallAccountProgress addSmallAccountProgress = new AddSmallAccountProgress();
            addSmallAccountProgress.setSmaleNickname(userLogin.getUserName()+ActivityConstants.S_DSnosaKgvi);
            addSmallAccountProgress.setUserId(userLogin.getAccountUserId());
            addSmallAccountProgress.setGameId(SdkDomain.getInstance().getGameId());
            addSmallAccountProgress.post(mHandler);
        }

        //添加小号按钮点击事件
        btnAddAccount.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (Constant.MCH_BACKGROUND_VERSION < Constant.VERSION_920){
                    if (smallAccountList.size()>=10){
                        ToastUtil.show(MCHChooseAccountActivity.this,ActivityConstants.S_FYpCEizXYW);
                    }else {
                        dialogBuilder = new AddAccountDialog.addAccountBuilder().setSureClick(clickcallback);
                        dialogBuilder.show(MCHChooseAccountActivity.this, getFragmentManager());
                    }
                }else { //9.2.0版本 添加小号上限为20个
                    if (smallAccountList.size()>=20){
                        ToastUtil.show(MCHChooseAccountActivity.this,ActivityConstants.S_AXkeMPGlTy);
                    }else {
                        dialogBuilder = new AddAccountDialog.addAccountBuilder().setSureClick(clickcallback);
                        dialogBuilder.show(MCHChooseAccountActivity.this, getFragmentManager());
                    }
                }
            }
        });
        //关于小号说明
        btnAbout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                new AboutAddAccountDialog.aboutBuilder().show(MCHChooseAccountActivity.this, getFragmentManager());
            }
        });

        btnBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                logout();
            }
        });

    }


    /**
     * 添加小号弹窗确认按钮点击事件
     */
    AddAccountCallback clickcallback = new AddAccountCallback() {
        @Override
        public void addAccount(String Account) {
            if (TextUtils.isEmpty(Account)){
                ToastUtil.show(MCHChooseAccountActivity.this,ActivityConstants.S_qswpYNiBAN);
                return;
            }

            dialogBuilder.closeDialog(getFragmentManager());
            mcTipDialog = new MCTipDialog.Builder().setMessage(ActivityConstants.S_mIUkgkwFNv).show(MCHChooseAccountActivity.this,getFragmentManager());
            AddSmallAccountProgress addSmallAccountProgress = new AddSmallAccountProgress();
            addSmallAccountProgress.setSmaleNickname(Account.trim());
            addSmallAccountProgress.setUserId(userLogin.getAccountUserId());
            addSmallAccountProgress.setGameId(SdkDomain.getInstance().getGameId());
            addSmallAccountProgress.post(mHandler);
        }
    };


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != mcTipDialog) {
                mcTipDialog.dismiss();
            }
            switch (msg.what) {
                case Constant.ADD_SMALL_ACCOUNT_SUCCESS:  //添加小号成功
                    UserLogin login = (UserLogin)msg.obj;
                    smallAccountList = login.getSmallAccountList();
                    adapter.setListData(smallAccountList);
                    if (canShowToast){
                        ToastUtil.show(MCHChooseAccountActivity.this, ActivityConstants.S_cmxvWPfuhq);
                    }
                    break;
                case Constant.ADD_SMALL_ACCOUNT_FAIL:  //添加小号失败
                    String tip = (String) msg.obj;
                    ToastUtil.show(MCHChooseAccountActivity.this, tip);
                    break;
                default:
                    break;
            }
        }
    };



    /**
     * 注销平台帐号登录
     */
    private void logout(){
//        Dialog dialog = DialogUtil.mch_alert_msg(MCHChooseAccountActivity.this, ActivityConstants.S_rrINzSbHUu, ActivityConstants.S_suaLoKGGfI,
//                MCHChooseAccountActivity.this, ActivityConstants.S_wwJdcNQadU, ActivityConstants.S_IrCizoTJWc, new OnMultiClickListener() {
//                    @Override
//                    public void onMultiClick(View v) {
//                        YalpGamesSdk.getMCApi().offLineAnnounce(MCHChooseAccountActivity.this);
//                        UserLoginSession.getInstance().clearUserInfoAll();
//                        FlagControl.flag = true;
//                        FlagControl.isLogin = false;
//                        FlagControl.isStart = false;
//                        FlagControl.isJump = false;
//                        FlagControl.isJump2 = false;
//                        FlagControl.isJumpFromBaseToBase = false;
//                        FlagControl.isFloatingOpen = false;
//                        finish();
//                    }
//                });
//        dialog.show();
        //注销确认弹窗
        YalpGamesSdk.getYalpGamesSdk().exitPopup(this);
    }


    /**
     * 返回按钮点击监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            logout();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }


}
