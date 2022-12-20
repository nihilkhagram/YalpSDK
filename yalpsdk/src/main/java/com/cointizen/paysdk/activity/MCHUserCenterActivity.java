package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;

import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.Channels;
import com.cointizen.paysdk.R;
import com.cointizen.paysdk.adapter.ChannelsActivityAdapter;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.entity.ChannelAndGameInfo;
import com.cointizen.streaming.ChannelsListActivity;
import com.cointizen.util.StoreSettingsUtil;
import com.lidroid.xutils.BitmapUtils;
import com.mc.hubert.guide.NewbieGuide;
import com.mc.hubert.guide.core.Controller;
import com.mc.hubert.guide.listener.OnGuideChangedListener;
import com.mc.hubert.guide.model.GuidePage;
import com.mc.hubert.guide.model.HighLight;
import com.mc.hubert.guide.util.ViewUtils;
import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.service.ServiceManager;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.SwitchManager;
import com.cointizen.paysdk.callback.LogoutVerifyCallback;
import com.cointizen.paysdk.callback.OnPermission;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.dialog.HideBallDialog;
import com.cointizen.paysdk.dialog.MCLoadDialog;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.dialog.delaccount.DeleteAccountDialog;
import com.cointizen.paysdk.dialog.delaccount.DeleteSuccessDialog;
import com.cointizen.paysdk.dialog.logoutverify.LogoutVerifyDialog;
import com.cointizen.paysdk.http.DeleteAccount.DeleteAccountProcess;
import com.cointizen.paysdk.http.process.ChangeUserInfoProcess;
import com.cointizen.paysdk.http.process.GetAuthCodeProcess;
import com.cointizen.paysdk.http.process.UpHeadProcess;
import com.cointizen.paysdk.http.process.UserInfoProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.BitmapHelp;
import com.cointizen.paysdk.utils.LQRPhotoSelectUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.PermissionsUtils;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.utils.SignInUtils;
import com.cointizen.paysdk.utils.WebViewUtil;
import com.cointizen.paysdk.view.MyScrollView;
import com.cointizen.paysdk.view.PopupWindow_Paizhao;
import com.cointizen.paysdk.view.round.NiceImageView;
import com.cointizen.plugin.guess.utils.GuessYouLikeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 描述：用户中心
 * 时间: 2018-09-17 18:46
 */
public class MCHUserCenterActivity extends MCHBaseActivity implements MyScrollView.ScrollBottomListener , ChannelsActivityAdapter.OnChannelClick{

    /**
     * 日志打印
     */
    private final static String TAG = "MCPersonalInfoActivity";

    RelativeLayout btnMchKefu;
    RelativeLayout btnMchSignin;
    RelativeLayout rl_joinStream;
    EditText etMchName;
    TextView btnMchChageName;
    LinearLayout layoutMchEditname;
    TextView tvMchMyName;
    RelativeLayout btnMchNameEdit;
    LinearLayout layoutMchName;
    TextView tvMchMyAccount;
    TextView tvMyJifen;
    TextView tvMyJinbi;
    TextView tvMyPTB;
    NiceImageView imgMchMyHread;
    ImageView imgMchMyLive;
    RelativeLayout btnMchMyLive;
    ImageView btnMchMyAutologin;
    RelativeLayout btnMchHindBall;
    LinearLayout btnMchChagePass;
    LinearLayout btnBindAccount;
    RelativeLayout btnMchBindMail;
    RelativeLayout btnMchVersion;
    TextView btnMchBackGame;
    private SignInUtils signInUtils;
    /**
     * 绑定手机号
     */
    private final int requestBindMail = 11;
    private MCTipDialog nickTip;

    private String nickName;

    private String account;
    private MCTipDialog infoTip;
    private String nick;
    private View btnMchBanlance;
//    private View btnMchShare;
    private View btnMchShequ;
    private View btnMchZhekou;
    private View btnXiaoHao;
    private View btnOpenReward;
    private ImageView btnMchLuping;
    private View mchLayoutLuping;
    public static int QIANDAO = 3;            //执行签到
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private PopupWindow_Paizhao menuWindow;
    private View layoutGuide1;
    private View layoutGuide2;
    private View layoutGuide3;
    private View layoutGuess;
    private HorizontalScrollView recyclerviewHorizontal;
    private View layoutGuide4;
    private TextView tvMcVersion;
    private BitmapUtils bitmapUtils;
    private TextView tvBindEmail;
    private TextView tvVipLive;
    private LinearLayout gameInfoImagesLayout;
    private MyScrollView userScrollView;
    private View layoutOoo;
    private boolean one = true;
//    private View tvWeiDu;
    private TextView txtDelAccount;
    private RelativeLayout btnDelAccount;
    private boolean canClickBanlance = true;
    private MCLoadDialog loadDialog;
    ChannelsActivityAdapter channelsActivityAdapter;
    private int vipLevel;
    private String nextVipMoney;
    RecyclerView recyclerView;
    List<Channels> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedPreferencesUtils.getInstance().getIsGuess(this) && !ServiceManager.getInstance().isBaiDuYunOS) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(getLayout("mch_act_my"));
        initView();
        bitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());

        if (UserLoginSession.getInstance().getChannelAndGame().getSex() == 0) {
            imgMchMyHread.setImageDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(MCHUserCenterActivity.this, "mch_nav_pic_touxiang")));
        }else {
            imgMchMyHread.setImageDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(MCHUserCenterActivity.this, "mch_nav_pic_touxiang_women")));
        }
        if (!ServiceManager.getInstance().isBaiDuYunOS) {
            Guide();
        }
        recyclerView = findViewById(R.id.recycler_view_channels);
        list = new ArrayList<>();
        GridLayoutManager layoutManager=new GridLayoutManager(MCHUserCenterActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        channelsActivityAdapter = new ChannelsActivityAdapter(getApplicationContext(), list, MCHUserCenterActivity.this);
        recyclerView.setAdapter(channelsActivityAdapter);
        getData();
        Log.e("MyLogdata" , "onCreate");

    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            processLoginResult(msg);
        }
    };

    private void processLoginResult(Message msg) {
        try {
            if (null != infoTip) {
                try {
                    infoTip.dismiss();
                    infoTip = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != nickTip) {
                nickTip.dismiss();
                nickTip = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (msg.what) {
            case Constant.GET_USER_INFO_SUCCESS:// 获取用户信息
                txtDelAccount.setText(PrivacyManager.getInstance().isLogout ? ActivityConstants.S_cancel_account_deletion : ActivityConstants.S_delete_account);
                ChannelAndGameInfo info = (ChannelAndGameInfo) msg.obj;
                if (null != info) {
                    handlerUserInfo(info);
                } else {
                    ToastUtil.show(MCHUserCenterActivity.this, ActivityConstants.S_re_login_needed);
                    finish();
                }
                break;
            case Constant.GET_USER_INFO_FAIL:
                YalpGamesSdk.getYalpGamesSdk().logout(MCHUserCenterActivity.this);
                finish();
                break;
            case Constant.MODIFY_PASSWORD_SUCCESS:
                handlerNikeName();
                break;
            case Constant.MODIFY_PASSWORD_FAIL:
                String res = (String) msg.obj;
                if (TextUtils.isEmpty(res)) {
                    res = ActivityConstants.S_SWkgnGTxNN;
                }
                ToastUtil.show(MCHUserCenterActivity.this,res);
                break;
            case Constant.AVATAR_UPLOAD_SUCCESS:
                String head = (String) msg.obj;
                if(!TextUtils.isEmpty(head)) {
                    MCLog.w(TAG, ActivityConstants.Log_zkljjlhAYt+head);
                    bitmapUtils.display(imgMchMyHread,head);
                    UserLoginSession.getInstance().getChannelAndGame().setHead_img(head);
                }else{
                    MCLog.e(TAG, ActivityConstants.Log_JdxKsCdJAT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (UserLoginSession.getInstance().getChannelAndGame().getSex() == 0) {
                            imgMchMyHread.setImageDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(MCHUserCenterActivity.this, "mch_nav_pic_touxiang")));
                        }else {
                            imgMchMyHread.setImageDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(MCHUserCenterActivity.this, "mch_nav_pic_touxiang_women")));
                        }
                    }
                }
                loadDialog.dismiss();
                ToastUtil.show(MCHUserCenterActivity.this,ActivityConstants.S_AnNffhAZFv);
                break;
            case Constant.AVATAR_UPLOAD__FAIL:
                loadDialog.dismiss();
                String str = (String) msg.obj;
                if (TextUtils.isEmpty(str)) {
                    str = ActivityConstants.S_SWkgnGTxNN;
                }
                ToastUtil.show(MCHUserCenterActivity.this,ActivityConstants.S_JlEjkwhQCl + str);
                break;
            default:
                break;
        }
    }


    /**
     * 设置用户信息
     * @param info
     */
    private int isSign;
    private void handlerUserInfo(ChannelAndGameInfo info) {
        UserLoginSession.getInstance().getChannelAndGame().setNikeName(info.getNikeName());
        UserLoginSession.getInstance().getChannelAndGame().setPlatformMoney(info.getPlatformMoney());
        UserLoginSession.getInstance().getChannelAndGame().setBindPtbMoney(info.getBindPtbMoney());
        UserLoginSession.getInstance().getChannelAndGame().setAge_status(info.getAge_status());
        UserLoginSession.getInstance().getChannelAndGame().setIdcard(info.getIdcard());
        UserLoginSession.getInstance().getChannelAndGame().setReal_name(info.getReal_name());
        UserLoginSession.getInstance().getChannelAndGame().seteMail(info.geteMail());
        UserLoginSession.getInstance().getChannelAndGame().setHead_img(info.getHead_img());
        UserLoginSession.getInstance().getChannelAndGame().setThird_authentication(info.getThird_authentication());
        UserLoginSession.getInstance().getChannelAndGame().setSex(info.getSex());
        tvMchMyAccount.setText(ActivityConstants.S_rzSOqEJITG + UserLoginSession.getInstance().getChannelAndGame().getAccount());
        nick = info.getNikeName();
        if (TextUtils.isEmpty(nick)) {
            nick = ActivityConstants.S_TOJqcWIMTT;
        }
        if(!info.getHead_img().equals("")) {
            bitmapUtils.display(imgMchMyHread,info.getHead_img());
            imgMchMyHread.isCircle(true);
        }

        if (SwitchManager.getInstance().vipLevel()) {
            if (info.getVip_level()==0) {
                imgMchMyLive.setBackgroundResource(MCHInflaterUtils.getDrawable(this, "mch_icon_vip_n"));
                tvVipLive.setTextColor(Color.parseColor("#b7b7b7"));
            }else {
                imgMchMyLive.setBackgroundResource(MCHInflaterUtils.getDrawable(this, "mch_icon_vip"));
                tvVipLive.setTextColor(Color.parseColor("#ffffff"));
            }
            vipLevel = info.getVip_level();
            nextVipMoney = info.getNext_vip();
            btnMchMyLive.setVisibility(View.VISIBLE);
            tvVipLive.setText("V" + info.getVip_level());
        }

        if(!SharedPreferencesUtils.getInstance().isThirdPartyLogin(this) && info.getUserRegisteType() != 0) {
            btnMchChagePass.setVisibility(View.VISIBLE);
        }else{
            btnMchChagePass.setVisibility(View.GONE);
        }

        tvMchMyName.setText(nick);

        if(!TextUtils.isEmpty(info.getPoint())) {
            tvMyJifen.setText(ActivityConstants.S_rpUCJhAHZV+info.getPoint());
            tvMyJifen.setVisibility(View.VISIBLE);
        }else{
            tvMyJifen.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(String.valueOf(info.getPlatformMoney())) || info.getPlatformMoney() == 0) {
            tvMyPTB.setText(ActivityConstants.S_ikGmBsmjaJ);
        }else{
            tvMyPTB.setText(ActivityConstants.S_QSpLMWHEkV + info.getPlatformMoney());
        }

        if(Constant.IsOpenSmallAccount && !TextUtils.isEmpty(info.getGold_coin())) {
            tvMyJinbi.setText(ActivityConstants.S_XtombjrqJw+info.getGold_coin());
            tvMyJinbi.setVisibility(View.VISIBLE);
        }else{
            tvMyJinbi.setVisibility(View.GONE);
        }

        //0游客 1帐号 2手机 3微信 4QQ 5百度 6微博 7邮箱
        if (info.getUserRegisteType() == 0) {
            btnBindAccount.setVisibility(View.VISIBLE);
        }else {
            btnBindAccount.setVisibility(View.GONE);
        }

        if (info.getSign_status()==1) {
            btnMchSignin.setVisibility(View.VISIBLE);
        }
        isSign = info.getToday_signed();

        isBindMail();
    }

    protected void handlerNikeName() {
        if (!TextUtils.isEmpty(nickName)) {
            ToastUtil.show(this,ActivityConstants.S_WVbzNhPvfX);
            UserLoginSession.getInstance().getChannelAndGame().setNikeName(nickName);
            tvMchMyName.setText(nickName);
            nick = nickName;
            etMchName.setText(nickName);
            layoutMchName.setVisibility(View.VISIBLE);
            layoutMchEditname.setVisibility(View.GONE);
        }
    }

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            etMchName.setText("");
            layoutMchName.setVisibility(View.VISIBLE);
            layoutMchEditname.setVisibility(View.GONE);
        }
    };


    /**
     * 新手指导
     */
    private void Guide() {
        LayoutInflater systemService = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(this, "mch_view_guide_my");
        View inflate = systemService.inflate(layout, null);
        TextView tv_mch_zi = inflate.findViewById(getId("tv_mch_zi"));
        String text = ActivityConstants.S_aKOkkOGRth;

        text = text+ActivityConstants.S_DSGgEuIgHH;
        tv_mch_zi.setText(text);
        NewbieGuide.with(this)
                .setLabel("guide1")
                .alwaysShow(false)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {

                    }

                    @Override
                    public void onRemoved(Controller controller) {
                            userScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(one) {
                                        ScrollBottom(0);
                                    }
                                }
                            }, 500);
                    }
                })
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(layoutGuide1, HighLight.Shape.ROUND_RECTANGLE,20,0,null)
                        .addHighLight(layoutGuide2,HighLight.Shape.ROUND_RECTANGLE,20,0,null)
                        .addHighLight(layoutGuide3,HighLight.Shape.ROUND_RECTANGLE,20,0,null)
                        .addHighLight(btnMchMyAutologin,HighLight.Shape.ROUND_RECTANGLE,20,0,null)
                        .setLayout(inflate))
                .show();

        SharedPreferencesUtils.getInstance().setIsGuess(MCHUserCenterActivity.this,false);
    }

    private void initView() {
        userScrollView = findViewById(getId("User_ScrollView"));
        layoutOoo = findViewById(getId("layout_ooo"));
        layoutGuide1 = findViewById(getId("layout_guide1"));
        layoutGuide2 = findViewById(getId("layout_btn1"));
        layoutGuide3 = findViewById(getId("tv_yincang"));
        layoutGuide4 = findViewById(getId("tv_youxiang"));
        btnMchKefu = findViewById(getId("btn_mch_kefu"));
        btnMchSignin = findViewById(getId("btn_mch_signin"));
        btnMchSignin.setVisibility(View.GONE);

        etMchName = findViewById(getId("et_mch_name"));
        rl_joinStream = findViewById(getId("rl_joinStream"));

        rl_joinStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(MCHUserCenterActivity.this, ChannelsListActivity.class);
                startActivity(intent);
            }
        });

        btnMchChageName = findViewById(getId("btn_mch_chage_name"));
        layoutMchEditname = findViewById(getId("layout_mch_editname"));
        tvMchMyName = findViewById(getId("tv_mch_my_name"));
        btnMchNameEdit = findViewById(getId("btn_mch_name_edit"));
        layoutMchName = findViewById(getId("layout_mch_name"));
        tvMchMyAccount = findViewById(getId("tv_mch_my_account"));
        tvMyJifen = findViewById(getId("tv_my_jifen"));
        tvMyPTB = findViewById(getId("tv_my_ptb"));
        tvMyJinbi = findViewById(getId("tv_my_jinbi"));
        btnMchBanlance = findViewById(getId("btn_mch_banlance"));
//        btnMchShare = findViewById(getId("btn_mch_share"));
        btnMchShequ = findViewById(getId("btn_mch_shequ"));
        btnMchZhekou = findViewById(getId("btn_mch_discount"));
        tvBindEmail = findViewById(getId("tv_bindEmail"));
        tvMcVersion = findViewById(getId("tv_mc_version"));
        imgMchMyHread = findViewById(getId("img_mch_my_hread"));
        imgMchMyHread.isCircle(true);
        imgMchMyLive = findViewById(getId("img_mch_my_live"));
        btnMchMyLive = findViewById(getId("btn_mch_my_live"));
        btnMchMyLive.setVisibility(View.GONE);
        tvVipLive = findViewById(getId("tv_mch_vip_live"));
        btnMchMyAutologin = findViewById(getId("btn_mch_my_autologin"));
        btnMchHindBall = findViewById(getId("btn_mch_hindBall"));
        btnMchChagePass = findViewById(getId("btn_mch_chagePass"));
        btnBindAccount = findViewById(getId("btn_mch_bind_account"));
        btnBindAccount.setVisibility(View.GONE);
        btnMchBindMail = findViewById(getId("btn_mch_Bind_mail"));
        btnMchVersion = findViewById(getId("btn_mch_version"));
        btnMchLuping = findViewById(getId("btn_mch_luping"));
        mchLayoutLuping = findViewById(getId("mch_layout_luping"));
        btnMchBackGame = findViewById(getId("btn_mch_back_game"));
        btnXiaoHao = findViewById(getId("btn_mch_xiaohao"));
        btnOpenReward = findViewById(getId("btn_mch_open_reward"));

        boolean isDel = PrivacyManager.getInstance().privacyStatus && !ServiceManager.getInstance().isBaiDuYunOS;
        btnDelAccount = findViewById(getId("btn_mch_account_del"));
        btnDelAccount.setVisibility(isDel ? View.VISIBLE : View.GONE);
        txtDelAccount = findViewById(getId("txt_mch_account_del"));
        txtDelAccount.setText(PrivacyManager.getInstance().isLogout ? ActivityConstants.S_cancel_account_deletion : ActivityConstants.S_delete_account);

        loadDialog = new MCLoadDialog(this,MCHInflaterUtils.getIdByName(this, "style", "mch_MyDialogStyle"));

        if( !SharedPreferencesUtils.getInstance().isThirdPartyLogin(this)) {
            btnMchChagePass.setVisibility(View.VISIBLE);
        }else{
            btnMchChagePass.setVisibility(View.GONE);
        }
        etMchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().equals("")) {
                    etMchName.setHint(ActivityConstants.S_GDMxBoIIEb);
                    btnMchChageName.setBackgroundResource(MCHInflaterUtils.getDrawable(
                            MCHUserCenterActivity.this,"mch_my_chage_bg2"));
                }else{
                    btnMchChageName.setBackgroundResource(MCHInflaterUtils.getDrawable(
                            MCHUserCenterActivity.this,"mch_my_chage_bg"));
                }
            }
        });
        BtnClick btnClick = new BtnClick();
        btnMchKefu.setOnClickListener(btnClick);
        btnMchChageName.setOnClickListener(btnClick);
        btnMchNameEdit.setOnClickListener(btnClick);
        btnMchMyLive.setOnClickListener(btnClick);
        btnMchMyAutologin.setOnClickListener(btnClick);
        btnMchHindBall.setOnClickListener(btnClick);
        btnMchChagePass.setOnClickListener(btnClick);
        btnMchBindMail.setOnClickListener(btnClick);
        btnMchVersion.setOnClickListener(btnClick);
        btnMchBackGame.setOnClickListener(btnClick);
        btnMchBanlance.setOnClickListener(btnClick);
//        btnMchShare.setOnClickListener(btnClick);
        btnMchLuping.setOnClickListener(btnClick);
        imgMchMyHread.setOnClickListener(btnClick);
        btnMchZhekou.setOnClickListener(btnClick);
        btnXiaoHao.setOnClickListener(btnClick);
        btnOpenReward.setOnClickListener(btnClick);
        btnBindAccount.setOnClickListener(btnClick);
        btnMchSignin.setOnClickListener(btnClick);
        btnDelAccount.setOnClickListener(btnClick);

        btnOpenReward.setVisibility(Constant.RED_BAG_STATUS == 1 ? View.VISIBLE : View.GONE);
//        btnMchShare.setVisibility(SwitchManager.getInstance().share() ? View.VISIBLE : View.GONE);

        btnMchShequ.setVisibility(View.GONE);
        if (!Constant.IsOpenSmallAccount) {
            btnXiaoHao.setVisibility(View.GONE);
        }

        userScrollView.setScrollBottomListener(this);

//        if(BuildConfig.rs&&Build.VERSION.SDK_INT>21) {
//            mchLayoutLuping.setVisibility(View.VISIBLE);
//        }else{
            mchLayoutLuping.setVisibility(View.GONE);
//        }
        if(SharedPreferencesUtils.getInstance().getAutoLogin(this)) {
            btnMchMyAutologin.setBackgroundResource(getDrawable("mch_common_btn_2"));
        }else{
            btnMchMyAutologin.setBackgroundResource(getDrawable("mch_common_btn_1"));
        }

        layoutGuess = findViewById(getId("layout_guess"));
        recyclerviewHorizontal = findViewById(getId("id_recyclerview_horizontal"));
        gameInfoImagesLayout = findViewById(getId("gameInfo_ImagesLayout"));
        GuessYouLikeUtils.getInstance().setCallback(layoutGuess, recyclerviewHorizontal, gameInfoImagesLayout, this);

        TextView txtAgree = findViewById(
                MCHInflaterUtils.getControl(this, "txt_btn_agreement"));
        txtAgree.setText(String.format(ActivityConstants.S_OxFafBiiLN, PrivacyManager.getInstance().userAgreementTitle()));
        txtAgree.setOnClickListener(agreeClick);
        TextView txtPrivacy = findViewById(
                MCHInflaterUtils.getControl(this, "txt_btn_protocal"));
        txtPrivacy.setText(String.format(ActivityConstants.S_OxFafBiiLN, PrivacyManager.getInstance().privacyPolicyTitle()));
        txtPrivacy.setOnClickListener(privacyClick);

        int promote_game_version = StoreSettingsUtil.getGameVersion();
        if(promote_game_version == 0) {
            tvMcVersion.setText(String.format("V%s", YalpGamesSdk.getYalpGamesSdk().version()));
        }else{
            tvMcVersion.setText(String.format(Locale.CHINA, "V%d", promote_game_version));
        }
    }

    private final OnMultiClickListener agreeClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            WebViewUtil.webView(MCHUserCenterActivity.this, PrivacyManager.getInstance().agreementUrl(), true);
        }
    };

    private final OnMultiClickListener privacyClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            WebViewUtil.webView(MCHUserCenterActivity.this, PrivacyManager.getInstance().privacyPolicyUrl(), true);
        }
    };

    @Override
    public void ScrollBottom(int t) {
        if(one && SharedPreferencesUtils.getInstance().getIsGuide(this) && !ServiceManager.getInstance().isBaiDuYunOS) {
            one = false;
            SharedPreferencesUtils.getInstance().setIsGuide(this,false);
            ViewUtils.juli = t;

            LayoutInflater systemService = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layout = MCHInflaterUtils.getLayout(MCHUserCenterActivity.this,
                    GuessYouLikeUtils.getInstance().isShowGuessYouLike() ? "mch_view_guide_my3" : "mch_view_guide_my5");
            View inflate = systemService.inflate(layout, null);
            View root_view = inflate.findViewById(getId("root_view"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)root_view.getLayoutParams();
            layoutParams.setMargins(0, -t,0,0);
            root_view.setLayoutParams(layoutParams);

            OnGuideChangedListener listener = null;
            GuidePage guidePage = GuidePage.newInstance()
                    .addHighLight(layoutGuide4, HighLight.Shape.ROUND_RECTANGLE,20,0,null)
                    .setLayout(inflate);
            if(GuessYouLikeUtils.getInstance().isShowGuessYouLike()) {
                guidePage = GuidePage.newInstance()
                        .addHighLight(layoutGuide4, HighLight.Shape.ROUND_RECTANGLE,20,0,null)
                        .addHighLight(layoutGuess,HighLight.Shape.ROUND_RECTANGLE,20,0,null)
                        .setLayout(inflate);
                listener = new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {

                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        userScrollView.fullScroll(ScrollView.FOCUS_UP);
                        ViewUtils.juli = 0;
                    }
                };
            }
            NewbieGuide.with(MCHUserCenterActivity.this)
                    .setLabel("guide2")
                    .setOnGuideChangedListener(listener)
                    .addGuidePage(guidePage)
                    .show();
        }
    }

    @Override
    public void onChannelClick(String userId, String deletChannelId) {
        Log.e("MyLogData", ""+deletChannelId);
        getChannelData(userId, deletChannelId);
    }


    class BtnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId() == getId("btn_mch_kefu")) {
                //TODO 联系客服
                startActivity(new Intent(MCHUserCenterActivity.this,MCHelperCenter.class));
            }else if(view.getId() == getId("btn_mch_signin")) {
                //TODO 签到
                if(signInUtils==null) {
                    signInUtils = new SignInUtils();
                }
                signInUtils.Sign(MCHUserCenterActivity.this,getFragmentManager(),isSign);
            }else if(view.getId() == getId("btn_mch_chage_name")) {
                //TODO 提交修改
                ChageName();
            }else if(view.getId() == getId("btn_mch_name_edit")) {
                //TODO 显示修改布局
                ShowChage();
            }else if(view.getId() == getId("img_mch_my_hread")) {
                //TODO 选取头像
                ChooseAvatar();
            }else if(view.getId() == getId("btn_mch_my_live")) {
                //TODO 我的等级
                new GetAuthCodeProcess(MCHUserCenterActivity.this).post(vipHandler); //获取校验码
            }else if(view.getId() == getId("btn_mch_my_autologin")) {
                //TODO 是否自动登录
                isAutoLogin();
            }else if(view.getId() == getId("btn_mch_banlance")) {
                //TODO 余额
                startActivityForResult(new Intent(MCHUserCenterActivity.this, MCHBalanceActivity.class), QIANDAO);
            }else if(view.getId() == getId("btn_mch_share")) {
                //TODO 分享
                startActivity(new Intent(MCHUserCenterActivity.this, MCHShareActivity.class));
            }
            else if (view.getId() == getId("btn_mch_open_reward")) {
                //TODO 拆红包
                new GetAuthCodeProcess(MCHUserCenterActivity.this).post(rewardHandler); //获取校验码,生成拆红包链接地址
            }else if (view.getId() == getId("btn_mch_xiaohao")) {
                //TODO 小号
                startActivity(new Intent(MCHUserCenterActivity.this, MCHManagementAccountActivity.class));
            }else if(view.getId() == getId("btn_mch_hindBall")) {
                //TODO 隐藏悬浮球
                HideBall();
            }else if (view.getId() == getId("btn_mch_discount")) {
                //TODO 折扣
                startActivity(new Intent(MCHUserCenterActivity.this, MCHDiscountRebateActivity.class));
            }else if(view.getId() == getId("btn_mch_chagePass")) {
                //TODO 修改密码
                startActivity(new Intent(MCHUserCenterActivity.this, MCHChangePasswordActivity.class));
            }else if (view.getId() == getId("btn_mch_bind_account")) {
                //TODO 绑定账号
                startActivity(new Intent(MCHUserCenterActivity.this, MCHVisitorUpdateInfo.class));
            }else if(view.getId() == getId("btn_mch_Bind_mail")) {
                //TODO 绑定邮箱
                toBindEmail();
            }else if(view.getId() == getId("btn_mch_version")) {
                //TODO 版本信息
            }else if(view.getId() == getId("btn_mch_luping")) {
                //TODO 录屏
            }else if(view.getId() == getId("btn_mch_back_game")) {
                //TODO 返回游戏
                finish();
            }else if(view.getId() == getId("btn_mch_account_del")) {
                accountManager();
            }
        }
    }

    private void toBindEmail() {
//        String phone = UserLoginSession.getInstance().getPhone();
//        if (TextUtils.isEmpty(phone)) {
//            ToastUtil.show(MCHUserCenterActivity.this, ActivityConstants.S_pvrFCkIKBg);
//            return;
//        }
        startActivity(new Intent(MCHUserCenterActivity.this, MCHBindMailActivity.class));
    }
    
    Handler rewardHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.GET_AUTH_CODE_SUCCESS:
                    String authCode = (String) msg.obj;
                    if (!com.cointizen.paysdk.utils.TextUtils.isEmpty(authCode)) {
                        loadUrl(MCHUserCenterActivity.this, authCode,"recharge/tplay/task/game_id/" + SdkDomain.getInstance().getGameId());
                    }
                    break;
                case Constant.GET_AUTH_CODE_FAIL:
                    String res = (String) msg.obj;
                    if (android.text.TextUtils.isEmpty(res)) {
                        res = ActivityConstants.S_SWkgnGTxNN;
                    }
                    ToastUtil.show(MCHUserCenterActivity.this, res);
                    break;
            }
        }
    };

    Handler vipHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.GET_AUTH_CODE_SUCCESS:
                    String authCode = (String) msg.obj;
                    if (!com.cointizen.paysdk.utils.TextUtils.isEmpty(authCode)) {
                        loadUrl(MCHUserCenterActivity.this, authCode,"/mobile/user/user_vip.html");
                    }
                    break;
                case Constant.GET_AUTH_CODE_FAIL:
                    String res = (String) msg.obj;
                    if (android.text.TextUtils.isEmpty(res)) {
                        res = ActivityConstants.S_SWkgnGTxNN;
                    }
                    ToastUtil.show(MCHUserCenterActivity.this, res);
                    break;
            }
        }
    };

    /**
     * 验证校验码，加载页面链接
     * @param activity
     * @param authCode
     * @param pageUrl
     */
    private void loadUrl(Activity activity, String authCode, String pageUrl) {
        String url = MCHConstant.getInstance().getPlatformDomain() + pageUrl;
        String base64AuthCode = android.util.Base64.encodeToString(authCode.getBytes(), android.util.Base64.DEFAULT);
        String base64URL = android.util.Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
        String location = MCHConstant.getInstance().getPlatformDomain() +
                "recharge/tplay/check_auth_code" + "/auth_code/" + base64AuthCode  + "/redirect_url/" + base64URL;

        WebViewUtil.webView(activity, location);
    }

    private void accountManager() {
        if (PrivacyManager.getInstance().isLogout) {
            resetAccount();
        }else {
            new DeleteAccountDialog.Builder()
                    .setDelSuccessClick(deleteAccountClick)
                    .setLogoutVerifyClick(logoutVerifyClick)
                    .show(MCHUserCenterActivity.this, getFragmentManager());
        }
    }

    private final LogoutVerifyCallback logoutVerifyClick = new LogoutVerifyCallback() {
        @Override
        public void onResult(final String type, final String phoneOrEmail) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new LogoutVerifyDialog.Builder()
                            .setVerifyType(type)
                            .setPhoneOrEmail(phoneOrEmail)
                            .setDelSuccessClick(deleteAccountClick)
                            .show(MCHUserCenterActivity.this, getFragmentManager());
                }
            }, 500);

        }
    };

    private final View.OnClickListener deleteAccountClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            txtDelAccount.setText(ActivityConstants.S_cancel_account_deletion);
            //延时显示，解决小米手机显示第二个弹窗，没有半透明背景问题
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showDelSuccessTip();
                }
            }, 500);
        }
    };

    private void resetAccount() {
        DeleteAccountProcess deleteAccountProcess = new DeleteAccountProcess();
        deleteAccountProcess.type = "0";
        deleteAccountProcess.post(delAccountHandle);
    }

    private Handler delAccountHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case Constant.DELETE_ACCOUNT_SUCCESS:
                    PrivacyManager.getInstance().isLogout = false;
                    txtDelAccount.setText(ActivityConstants.S_delete_account);
                    ToastUtil.show(MCHUserCenterActivity.this, ActivityConstants.S_MHeWXzpsyI);
                    break;
                case Constant.HTTP_REQUEST_FAIL:
                    break;
            }
            return false;
        }
    });

    private void showDelSuccessTip() {
        new DeleteSuccessDialog.Builder()
                .show(MCHUserCenterActivity.this, getFragmentManager());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideSoftInput();
                handler.sendEmptyMessageDelayed(1,200);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        try{
            if(v.getId() == getId("btn_mch_chage_name")) {
                return false;
            }
        }catch (Exception e) {
            MCLog.e(TAG,e.toString());
        }
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }



    /**
     * 选取头像
     */
    private void ChooseAvatar() {
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
//                 4、当拍照或从图库选取图片成功后回调
                MCLog.w(TAG, ActivityConstants.Log_lLNXVGYyxN + outputUri.toString());
                MCLog.w(TAG, ActivityConstants.Log_ojlfXrADse + outputFile.getAbsolutePath());
                Bitmap bitmap = mLqrPhotoSelectUtils.toRoundBitmap(outputFile, imgMchMyHread, MCHUserCenterActivity.this, false);
                String savetolocal = mLqrPhotoSelectUtils.Savetolocal(bitmap);
//                上传头像
                Upload(savetolocal);
            }
        }, false);//true裁剪，false不裁剪

        //实例化SelectPicPopupWindow
        menuWindow = new PopupWindow_Paizhao(this, itemsOnClick);
        //显示窗口
        menuWindow.showAtLocation(this.findViewById(
                MCHInflaterUtils.getIdByName(MCHUserCenterActivity.this, "id","layout_root")),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        retrieveUserInfo();
        Log.e("MyLogData   ", "on Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(mLqrPhotoSelectUtils!=null) {
            mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
        }
        // 执行签到
        if (QIANDAO == requestCode) {
        }
    }

    /**
     * 是否绑定邮箱
     */
    private void isBindMail() {
        if (TextUtils.isEmpty(UserLoginSession.getInstance().getEmail())) {
            tvBindEmail.setText(ActivityConstants.S_pjlmRlKfGi);
            tvBindEmail.setTextColor(Color.parseColor("#969696"));
        } else {
            tvBindEmail.setText(ActivityConstants.S_kCbLGndsIf);
            tvBindEmail.setTextColor(Color.parseColor("#000000"));
        }
    }

    /**
     * 隐藏悬浮球
     */
    private void HideBall() {
        if(!SharedPreferencesUtils.getInstance().getIsHiddenOrb(this)) {
            HideBallDialog hideBallDialog = new HideBallDialog(this);
            hideBallDialog.show(getFragmentManager(),"HideBallDialog");
        }else{
                YalpGamesSdk.getYalpGamesSdk().stopFloating(this);  //关闭悬浮球
                HideBallDialog.IsShow = false;
                ToastUtil.show(this,ActivityConstants.S_oqXcZvZaOE);
        }
    }

    /**
     * 显示修改布局
     */
    private void ShowChage() {
        layoutMchEditname.setVisibility(View.VISIBLE);
        layoutMchName.setVisibility(View.GONE);
        etMchName.setFocusable(true);
        etMchName.setFocusableInTouchMode(true);
        etMchName.requestFocus();
        if(nick!=null) {
            etMchName.setText(nick);
            etMchName.setSelection(nick.length());//将光标移至文字末尾
        }
        showSoftInput();
    }

    /**
     * 是否自动登录
     */
    private void isAutoLogin() {
        if(SharedPreferencesUtils.getInstance().getAutoLogin(this)) {
            SharedPreferencesUtils.getInstance().setAutoLogin(this,false);
            btnMchMyAutologin.setBackgroundResource(getDrawable("mch_common_btn_1"));
        }else{
            SharedPreferencesUtils.getInstance().setAutoLogin(this,true);
            btnMchMyAutologin.setBackgroundResource(getDrawable("mch_common_btn_2"));
        }
    }

    /**
     * 修改昵称
     */
    private void ChageName() {
        String nickname = etMchName.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
//            ToastUtil.show(MCUserCenterActivity.this, ActivityConstants.S_RonpEpuByb);
            return;
        }
        hideSoftInput();
        String trim = tvMchMyName.getText().toString().trim();
        if(trim.equals(nickname)) {
            etMchName.setText("");
            layoutMchName.setVisibility(View.VISIBLE);
            layoutMchEditname.setVisibility(View.GONE);
            return;
        }
        nickName = nickname;

        ChangeUserInfoProcess changePwdProcess = new ChangeUserInfoProcess();
        changePwdProcess.setNickname(nickname);
        changePwdProcess.setType(5);
        changePwdProcess.post(mHandler);
    }

    /**
     *隐藏软键盘
     */
    private void hideSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = getCurrentFocus();
        if (view == null) view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     *显示软键盘
     */
    private void showSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获取用户信息
     */
    public void retrieveUserInfo() {
        account = UserLoginSession.getInstance().getChannelAndGame().getAccount();
//        infoTip = new MCTipDialog.Builder().setMessage("").show(this, this.getFragmentManager());
        UserInfoProcess userInfoProcess = new UserInfoProcess();
        userInfoProcess.post(mHandler);
    }


    /**
     * 上传头像
     *
     * @param s
     */
    private void Upload(String s) {
        File file = new File(s);
        if (file.exists()) {
            loadDialog.show();
            UpHeadProcess upHeadProcess = new UpHeadProcess();
            upHeadProcess.setFile(file);
            upHeadProcess.post(mHandler);
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            menuWindow.dismiss();
            if(MCHInflaterUtils.getIdByName(MCHUserCenterActivity.this, "id", "mch_paizhao")==v.getId()) {
                //系统大于等于23   6.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermissionsUtils.with(MCHUserCenterActivity.this)
                            .permission(PermissionsUtils.CAMERA)
                            .request(new OnPermission() {
                                @Override
                                public void hasPermission(List<String> granted, boolean isAll) {
                                    mLqrPhotoSelectUtils.takePhoto();
                                }

                                @Override
                                public void noPermission(List<String> denied, boolean quick) {
                                    ToastUtil.show(MCHUserCenterActivity.this, ActivityConstants.S_mkFcxxRKfL);
                                }
                            });
                } else {
                    mLqrPhotoSelectUtils.takePhoto();
                }

            }else if(MCHInflaterUtils.getIdByName(MCHUserCenterActivity.this, "id", "mch_xiangce")==v.getId()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermissionsUtils.with(MCHUserCenterActivity.this)
                            .permission(PermissionsUtils.STORAGE)
                            .request(new OnPermission() {
                                @Override
                                public void hasPermission(List<String> granted, boolean isAll) {
                                    mLqrPhotoSelectUtils.selectPhoto();
                                }

                                @Override
                                public void noPermission(List<String> denied, boolean quick) {
                                    ToastUtil.show(MCHUserCenterActivity.this, ActivityConstants.S_OlSVzgmlts);
                                }
                            });
                }else {
                    mLqrPhotoSelectUtils.selectPhoto();
                }


            }
        }
    };

    private void getData() {
        list.clear();
        RequestQueue queue = Volley.newRequestQueue(MCHUserCenterActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://streaming.cointizen.com/user_api/api.php?action=read",
                null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                Log.e("MyLogdata" , "Response " + response);
                list.clear();
                try {
                    JSONArray array = response.getJSONArray("data");
                    for (int i =0 ; i < array.length() ; i++){
                        Channels channels = new Channels();
                        JSONObject object = array.getJSONObject(i);
                        channels.setId(object.getString("id"));
                        channels.setUser_id(object.getString("user_id"));
                        channels.setChannel_id(object.getString("channel_id"));
                        channels.setTitle(object.getString("title"));
                        channels.setViewcount(object.getInt("viewcount"));
                        list.add(channels);
                    }
                    channelsActivityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyLogdata" , " Error " + error);
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void getChannelData(String userId, String deletChannelId) {
        Log.e("MyLogData", ""+userId);
        RequestQueue queue = Volley.newRequestQueue(MCHUserCenterActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://streaming.cointizen.com/Tools/DynamicKey/AgoraDynamicKey/php/sample/RtcTokenBuilderSample.php?data_id="+userId,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    Intent intent = new Intent(MCHUserCenterActivity.this, YalpGamesSdk.getMainActivity().getClass());
                    intent.putExtra("channelName", jsonObject.getString("channel_name"));
                    intent.putExtra("subscriberToken", jsonObject.getString("tokenuid_subscriber"));
                    intent.putExtra("userId", jsonObject.getString("uid_s"));
                    intent.putExtra("deletChannelId", deletChannelId);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                list.clear();
                channelsActivityAdapter.notifyDataSetChanged();
                Log.e("MyResponse" , " Error " + error);
            }
        });
        queue.add(jsonObjectRequest);
    }



}
