package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.ChannelAndGameInfo;
import com.cointizen.paysdk.http.process.CheckCodeProcess;
import com.cointizen.paysdk.http.process.ForgetAccountProcess;
import com.cointizen.paysdk.http.process.ForgmentPasswordProcess;
import com.cointizen.paysdk.http.process.VerificationCodeProcess;
import com.cointizen.paysdk.http.process.VerificationMailCodeProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.observer.SecondsWatcher;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.view.util.TimeFactory;

public class MCHForgetPasswordActivity extends MCHBaseActivity {

    private final static String TAG = "MCForgetPasswordActivity";

    TextView step_subacc;
    TextView step_secval;
    TextView step_respwd;
    TextView step_succes;
    TextView step_subacc_;
    TextView step_secval_;
    TextView step_respwd_;
    TextView step_succes_;
    TextView tv_hint, tv_hint_2;
    RelativeLayout rlForgetPwdFirst;
    LinearLayout llForgetPwdSecond;
    LinearLayout llForgetPwdThird;
    LinearLayout llForgetPwdFourth;
    View lineFirst, lineSecond, lineThird;
    // -step 1
    EditText stepone_et_username;
    TextView stepone_btn_submit;
    // -step 2
    ScrollView frogetpassword_steptwo_sc;
    TextView teptwo_tv_useraccount;
    TextView teptwo_tv_phonenumber;
    Button teptwo_btn_checknumber;
    EditText teptwo_et_checknumber;
    TextView teptwo_btn_back;
    TextView teptwo_btn_next;
    // -step 3
    ScrollView forgetpassword_stepthree_sc;
    TextView tempthree_tv_account;
    EditText tempthree_et_newpassword;// 新密码
    EditText tempthree_et_subnewpassword;// 重新输入密码
    Button tempthree_btn_back;//返回上一步
    Button tempthree_btn_submit;//验证验证码
    // -step 4
    TextView stepfour_tv_current;
    TextView stepfour_tv_back;

    /**
     * 账号
     */
    String account;

    String mPhone;
    Context con;

    String inpPhoValNum;
    private TextView btnMchBack;
    private View btnMchBack1;
    private TextView btnQiehuan;
    private ChannelAndGameInfo info;

    // private VerifyCode verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_activity_forgetpassword"));
        con = this;
        initView();
        step_subacc();
        initDate();
    }

    private void initDate() {
        mPhone = "";
    }

    private void initView() {
        btnMchBack1 = findViewById(getId("btn_mch_back"));
        step_subacc = (TextView) findViewById(getId("step_subacc"));
        step_secval = (TextView) findViewById(getId("step_secval"));
        step_respwd = (TextView) findViewById(getId("step_respwd"));
        step_succes = (TextView) findViewById(getId("step_succes"));
        step_subacc_ = (TextView) findViewById(getId("step_subacc_"));
        step_secval_ = (TextView) findViewById(getId("step_secval_"));
        step_respwd_ = (TextView) findViewById(getId("step_respwd_"));
        step_succes_ = (TextView) findViewById(getId("step_succes_"));
        rlForgetPwdFirst = (RelativeLayout) findViewById(getId("rl_mch_forgetpwd_first"));
        llForgetPwdSecond = (LinearLayout) findViewById(getId("ll_mch_forgetpwd_second"));
        llForgetPwdThird = (LinearLayout) findViewById(getId("ll_mch_forgetpwd_third"));
        forgetpassword_stepthree_sc = (ScrollView) findViewById(getId("forgetpassword_stepthree_sc"));
        llForgetPwdFourth = (LinearLayout) findViewById(getId("ll_mch_forgetpwd_fourth"));

        findViewById(getId("btn_mch_forget_pwd")).setOnClickListener(forgetPasswordListener);
        btnMchBack1.setOnClickListener(backClick);

        lineFirst = (View) findViewById(getId("line_forgetpwd_1"));
        lineSecond = (View) findViewById(getId("line_forgetpwd_2"));
        lineThird = (View) findViewById(getId("line_forgetpwd_3"));
        // -step 1
        stepone_et_username = findViewById(getId("stepone_et_username"));
        stepone_btn_submit = findViewById(getId("stepone_btn_submit"));
        stepone_btn_submit.setOnClickListener(forgetPasswordListener);
        // -step 2
        teptwo_tv_useraccount = findViewById(getId("teptwo_tv_useraccount"));
        //客服
        findViewById(getId("teptwo_tv_service")).setOnClickListener(forgetPasswordListener);
        findViewById(getId("teptwo_tv_service2")).setOnClickListener(forgetPasswordListener);
        teptwo_tv_phonenumber = findViewById(getId("teptwo_tv_phonenumber"));
        btnQiehuan = findViewById(getId("btn_qiehuan"));
        btnQiehuan.setOnClickListener(forgetPasswordListener);
        teptwo_btn_checknumber = findViewById(getId("teptwo_btn_checknumber"));
        tv_hint = findViewById(getId("tv_hint"));
        tv_hint_2 = findViewById(getId("tv_hint_2"));
        teptwo_btn_checknumber.setOnClickListener(forgetPasswordListener);
        teptwo_et_checknumber =  findViewById(getId("teptwo_et_checknumber"));
        teptwo_btn_back = findViewById(getId("teptwo_btn_back"));
        teptwo_btn_back.setOnClickListener(forgetPasswordListener);
        teptwo_btn_next =  findViewById(getId("teptwo_btn_next"));
        teptwo_btn_next.setOnClickListener(forgetPasswordListener);
        // -step 3
        frogetpassword_steptwo_sc = findViewById(getId("frogetpassword_steptwo_sc"));
        tempthree_tv_account = findViewById(getId("tempthree_tv_account"));
        tempthree_et_newpassword = findViewById(getId("tempthree_et_newpassword"));
        tempthree_et_subnewpassword = findViewById(getId("tempthree_et_subnewpassword"));
        tempthree_btn_submit =  findViewById(getId("tempthree_btn_submit"));
        tempthree_btn_submit.setOnClickListener(forgetPasswordListener);
        tempthree_btn_back = findViewById(getId("tempthree_btn_back"));
        tempthree_btn_back.setOnClickListener(forgetPasswordListener);
        // -step 4
        stepfour_tv_current = findViewById(getId("stepfour_tv_current"));
        stepfour_tv_back = findViewById(getId("stepfour_tv_back"));
        stepfour_tv_back.setOnClickListener(forgetPasswordListener);
    }

    void step_subacc() {
        lineFirst.setBackgroundColor(Color.parseColor("#ededed"));
        lineSecond.setBackgroundColor(Color.parseColor("#ededed"));
        lineThird.setBackgroundColor(Color.parseColor("#ededed"));
        rlForgetPwdFirst.setVisibility(View.VISIBLE);
        llForgetPwdSecond.setVisibility(View.GONE);
        frogetpassword_steptwo_sc.setVisibility(View.GONE);
        llForgetPwdThird.setVisibility(View.GONE);
        forgetpassword_stepthree_sc.setVisibility(View.GONE);
        llForgetPwdFourth.setVisibility(View.GONE);
        step_subacc.setBackgroundResource(getDrawable("mch_step1_80x80_blue"));
        step_secval.setBackgroundResource(getDrawable("mch_step2_80x80_grey"));
        step_respwd.setBackgroundResource(getDrawable("mch_step3_80x80_grey"));
        step_succes.setBackgroundResource(getDrawable("mch_step4_80x80_grey"));
        step_subacc_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        step_secval_.setTextColor(0x55434343);
        step_respwd_.setTextColor(0x55434343);
        step_succes_.setTextColor(0x55434343);
    }

    /**
     * 获取验证码，验证手机号
     */
    void step_secval() {
        // show view
        lineSecond.setBackgroundColor(Color.parseColor("#ededed"));
        lineThird.setBackgroundColor(Color.parseColor("#ededed"));
        rlForgetPwdFirst.setVisibility(View.GONE);
        llForgetPwdSecond.setVisibility(View.VISIBLE);
        frogetpassword_steptwo_sc.setVisibility(View.VISIBLE);
        llForgetPwdThird.setVisibility(View.GONE);
        forgetpassword_stepthree_sc.setVisibility(View.GONE);
        llForgetPwdFourth.setVisibility(View.GONE);
        step_subacc.setBackgroundResource(getDrawable("mch_step1_80x80_blue"));
        step_secval.setBackgroundResource(getDrawable("mch_step2_80x80_blue"));
        step_respwd.setBackgroundResource(getDrawable("mch_step3_80x80_grey"));
        step_succes.setBackgroundResource(getDrawable("mch_step4_80x80_grey"));
        step_subacc_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        step_secval_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        lineFirst.setBackgroundColor(getResources().getColor(getColor("mch_yanse")));
        step_respwd_.setTextColor(0x4c3c3c3c);
        step_succes_.setTextColor(0x4c3c3c3c);
    }

    void step_respwd() {
        lineThird.setBackgroundColor(Color.parseColor("#ededed"));
        tempthree_tv_account.setText(account);
        rlForgetPwdFirst.setVisibility(View.GONE);
        llForgetPwdSecond.setVisibility(View.GONE);
        frogetpassword_steptwo_sc.setVisibility(View.GONE);
        llForgetPwdThird.setVisibility(View.VISIBLE);
        forgetpassword_stepthree_sc.setVisibility(View.VISIBLE);
        llForgetPwdFourth.setVisibility(View.GONE);
        step_subacc.setBackgroundResource(getDrawable("mch_step1_80x80_blue"));
        step_secval.setBackgroundResource(getDrawable("mch_step4_80x80_blue"));
        step_respwd.setBackgroundResource(getDrawable("mch_step3_80x80_blue"));
        step_succes.setBackgroundResource(getDrawable("mch_step4_80x80_grey"));
        step_subacc_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        step_secval_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        step_succes_.setTextColor(0x4c3c3c3c);
        step_respwd_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        lineFirst.setBackgroundColor(getResources().getColor(getColor("mch_yanse")));
        lineSecond.setBackgroundColor(getResources().getColor(getColor("mch_yanse")));
//        TimeFactory.creator(0).calcel();
//        teptwo_btn_checknumber.setText(ActivityConstants.S_EtkaEFEEft);
//        teptwo_btn_checknumber.setEnabled(true);
        teptwo_btn_checknumber.setTextColor(getResources().getColor(getColor("mch_yanse")));
    }

    void step_succes() {
        rlForgetPwdFirst.setVisibility(View.GONE);
        llForgetPwdSecond.setVisibility(View.GONE);
        frogetpassword_steptwo_sc.setVisibility(View.GONE);
        llForgetPwdThird.setVisibility(View.GONE);
        forgetpassword_stepthree_sc.setVisibility(View.GONE);
        llForgetPwdFourth.setVisibility(View.VISIBLE);
        step_subacc.setBackgroundResource(getDrawable("mch_step1_80x80_blue"));
        step_secval.setBackgroundResource(getDrawable("mch_step2_80x80_blue"));
        step_respwd.setBackgroundResource(getDrawable("mch_step3_80x80_blue"));
        step_succes.setBackgroundResource(getDrawable("mch_step4_80x80_blue"));
        step_subacc_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        step_secval_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        step_respwd_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        step_succes_.setTextColor(getResources().getColor(getColor("mch_yanse")));
        lineFirst.setBackgroundColor(getResources().getColor(getColor("mch_yanse")));
        lineSecond.setBackgroundColor(getResources().getColor(getColor("mch_yanse")));
        lineThird.setBackgroundColor(getResources().getColor(getColor("mch_yanse")));
        SpannableString msp = new SpannableString(ActivityConstants.S_fKnCpadUrn);
        msp.setSpan(new UnderlineSpan(), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(0x99FFB400), 0, 4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为橙色
        stepfour_tv_back.setText(msp);

//        //定时自定关闭
//        Handler timeHandler = new Handler();
//        Runnable runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                MCForgetPasswordActivity.this.finish();
//            }
//        };
//        timeHandler.postDelayed(runnable, 2000);
    }

    OnClickListener forgetPasswordListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            if (v.getId() == getId("stepone_btn_submit")) {
                checkAccount();
                return;
            }
            if (v.getId() == getId("teptwo_btn_back")) {
                TimeFactory.creator(0).calcel();
                teptwo_btn_checknumber.setText(ActivityConstants.S_EtkaEFEEft);
                teptwo_btn_checknumber.setEnabled(true);
                step_subacc();
                return;
            }
            if (v.getId() == getId("btn_qiehuan")) {
                //切换找回密码
                step_switch();
                return;
            }
            // 检查验证码
            if (v.getId() == getId("teptwo_btn_next")) {
                checkPhoValNum();//检查验证码是否正确
            }
            // 重置密码
            if (v.getId() == getId("tempthree_btn_submit")) {
                resetPassword();
                return;
            }
            if (v.getId() == getId("stepfour_tv_back")) {
                finish();
                return;
            }
            if (v.getId() == getId("teptwo_tv_service")) {
//                MCHServiceModel.getInstance().contactService(MCForgetPasswordActivity.this,false);
                startActivity(new Intent(MCHForgetPasswordActivity.this, MCHelperCenter.class));
                return;
            }
            if (v.getId() == getId("teptwo_tv_service2")) {
//                MCHServiceModel.getInstance().contactService(MCForgetPasswordActivity.this,false);
                startActivity(new Intent(MCHForgetPasswordActivity.this, MCHelperCenter.class));
                return;
            }
            if (v.getId() == getId("btn_mch_forget_pwd")) {
                MCHForgetPasswordActivity.this.finish();
                return;
            }
            // 获取验证码
            if (v.getId() == getId("teptwo_btn_checknumber")) {
                getPhoneValidateNumber();
            }
            //step3 返回到step2
            if (v.getId() == getId("tempthree_btn_back")) {
                step_secval();
            }
        }
    };

    private void step_switch() {
        if (!teptwo_btn_checknumber.isEnabled()) {
            ToastUtil.show(this, ActivityConstants.S_yXpoPuoMQM);
            return;
        }
        String s = tv_hint.getText().toString();
        if (s.equals(ActivityConstants.S_HqpGAAtaUe)) {
            btnQiehuan.setText(ActivityConstants.S_mvEySJUuvp);
            tv_hint.setText(ActivityConstants.S_AnnVSQXUUA);
            tv_hint_2.setText(ActivityConstants.S_rZuoGnbtuQ);
            String showPhoNum = mPhone.substring(0, 3) + "****" + mPhone.substring(7, 11);
            teptwo_tv_phonenumber.setText(showPhoNum);
            teptwo_tv_useraccount.setText(info.getAccount());
        } else {
            btnQiehuan.setText(ActivityConstants.S_TItqXhyqTP);
            tv_hint.setText(ActivityConstants.S_HqpGAAtaUe);
            tv_hint_2.setText(ActivityConstants.S_utGXUQGKYq);
            teptwo_tv_phonenumber.setText(email);
            teptwo_tv_useraccount.setText(info.getAccount());
        }
    }

    OnClickListener backClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        TimeFactory.creator(0).getTimeChange()
                .addWatcher(forgetpasswordWatcher);
    }

    ;

    /**
     * 重置密码请求
     */
    protected void resetPassword() {
        String pwd = tempthree_et_newpassword.getText().toString().trim();
        String repwd = tempthree_et_subnewpassword.getText().toString().trim();

        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.show(con, ActivityConstants.S_GASfraiZxd);
            return;
        }
        if (!pwd.matches(Constant.REGULAR_ACCOUNT)) {
            ToastUtil.show(con, ActivityConstants.S_WcxIBGGQvK);
            return;
        }
        if (TextUtils.isEmpty(repwd)) {
            ToastUtil.show(con, ActivityConstants.S_MWtAfOcVoq);
            return;
        }
        if (!TextUtils.equals(pwd, repwd)) {
            ToastUtil.show(con, ActivityConstants.S_IBaVlaufjK);
            return;
        }

        ForgmentPasswordProcess forgmentProcess = new ForgmentPasswordProcess();
        if (Constant.REGULAR_MAIL(email)) {

            forgmentProcess.setCodeType(2);
        } else {
            forgmentProcess.setCodeType(1);
        }
        forgmentProcess.setRepassword(repwd);
        // forgmentProcess.setVcode(verifyCode.getCode());
        // forgmentProcess.setReCodeStr(verifyCode.getResult());
        forgmentProcess.setNewPassword(pwd);
        forgmentProcess.setVcode(inpPhoValNum);

         forgmentProcess.setAccount(account);
        forgmentProcess.post(myHandler);
    }

    protected void checkPhoValNum() {
        inpPhoValNum = teptwo_et_checknumber.getText().toString().trim();
        if (TextUtils.isEmpty(inpPhoValNum)) {
            ToastUtil.show(con, ActivityConstants.S_FKoZPHRjwy);
            return;
        }
        String s = tv_hint.getText().toString();
        CheckCodeProcess checkCodeProcess = new CheckCodeProcess();
        checkCodeProcess.setCode(inpPhoValNum);

        if (Constant.REGULAR_MAIL(email) && s.equals(ActivityConstants.S_HqpGAAtaUe)) {
            checkCodeProcess.setCode_type("2");
            checkCodeProcess.setEmail(email);
        } else {
            checkCodeProcess.setCode_type("1");
            checkCodeProcess.setPhone(mPhone);
        }

        checkCodeProcess.post(myHandler);
//		TimeFactory.creator(0).calcel();
    }

    protected void checkAccount() {
        String account = stepone_et_username.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtil.show(con, ActivityConstants.S_WqdODiyiKR);
            return;
        }
        if (!account.matches(Constant.REGULAR_ACCOUNT) && !Constant.REGULAR_MAIL(account)) {
            ToastUtil.show(con, ActivityConstants.S_FPAsQYEguT);
            return;
        }
        ForgetAccountProcess forgetAccountProcess = new ForgetAccountProcess();
        forgetAccountProcess.setAccount(account);
        forgetAccountProcess.post(myHandler);

    }

    @Override
    protected void onPause() {
        super.onPause();
        TimeFactory.creator(0).getTimeChange()
                .removeWatcher(forgetpasswordWatcher);
    }

    @SuppressLint("HandlerLeak")
    Handler secondsHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String seconds = (String) msg.obj;
                if (null != teptwo_btn_checknumber
                        && llForgetPwdSecond.isShown()) {
                    if (!seconds.equals("0")) {
                        teptwo_btn_checknumber.setText(seconds);
                        teptwo_btn_checknumber.setEnabled(false);
                        teptwo_btn_checknumber.setTextColor(Color.parseColor("#949494"));
                    } else if (seconds.equals("0")) {
                        teptwo_btn_checknumber.setText(ActivityConstants.S_EtkaEFEEft);
                        teptwo_btn_checknumber.setEnabled(true);
                            teptwo_btn_checknumber.setTextColor(getResources().getColor(getColor("mch_yanse")));

                    }
                }
            }
        }
    };

    SecondsWatcher forgetpasswordWatcher = new SecondsWatcher() {

        @Override
        public void updateSeconds(String seconds) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = seconds;
            secondsHandler.sendMessage(msg);
        }
    };

    private String email;
    private String id;
    @SuppressLint("HandlerLeak")
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.GET_USER_INFO_SUCCESS:// 根据账号查找手机号码
                    info = (ChannelAndGameInfo) msg.obj;
                    subAccToSecVal(info);
                    break;
                case Constant.GET_USER_INFO_FAIL:
                    String tip = (String) msg.obj;
                    ToastUtil.show(con, tip);
                    break;
                case Constant.VERIFYCODE_REQUEST_SUCCESS:// 请求获得手机验证码成功
                    // VerifyCode verifyCode = (VerifyCode) msg.obj;
                    teptwo_btn_checknumber.setEnabled(false);
                    teptwo_btn_checknumber.setTextColor(Color.parseColor("#949494"));
                    ToastUtil.show(con, ActivityConstants.S_xhzKnyoywq);
                    TimeFactory.creator(0).Start();
                    break;
                case Constant.VERIFYCODE_REQUEST_FAIL:// 请求获得手机验证码失败
                    teptwo_btn_checknumber.setText(ActivityConstants.S_EtkaEFEEft);
                    teptwo_btn_checknumber.setEnabled(true);

                        teptwo_btn_checknumber.setTextColor(getResources().getColor(getColor("mch_yanse")));

                    String res = (String) msg.obj;
                    ToastUtil.show(MCHForgetPasswordActivity.this, res);
                    break;
                case Constant.MODIFY_PASSWORD_SUCCESS:
                    step_succes();
                    ToastUtil.show(MCHForgetPasswordActivity.this, ActivityConstants.S_zRBqFkWAfm);
                    break;
                case Constant.MODIFY_PASSWORD_FAIL:
                    ToastUtil.show(MCHForgetPasswordActivity.this,
                            (String) msg.obj);
                    break;

                case Constant.IS_CODE_SUCCESS:
                    //忘记密码 ，验证码验证成功，继续下一步
                    step_respwd();
                    break;
                case Constant.IS_CODE_FAIL:
                    String message = msg.obj.toString();
                    if (!TextUtils.isEmpty(message)) {
                        ToastUtil.show(MCHForgetPasswordActivity.this, message);
                    }
                    break;
                case Constant.CHECK_CODE_SUCCESS:
                    //忘记密码 ，验证码验证成功，继续下一步
                    step_respwd();
                    break;
                case Constant.CHECK_CODE_FAIL:
                    String message1 = msg.obj.toString();
                    if (!TextUtils.isEmpty(message1)) {
                        ToastUtil.show(MCHForgetPasswordActivity.this, message1);
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * 验证是否绑定手机号
         *
         * @param info
         */
        private void subAccToSecVal(ChannelAndGameInfo info) {
            if (null == info || TextUtils.isEmpty(info.getAccount())) {
                if (null == info) {
                    MCLog.e(TAG, "fun#subAccToSecVal info is null ");
                }
                ToastUtil.show(con, ActivityConstants.S_rLyyOmfIkz);
                return;
            }

            if (TextUtils.isEmpty(info.getPhoneNumber()) && TextUtils.isEmpty(info.geteMail()) && !Constant.REGULAR_MAIL(info.geteMail())) {
                ToastUtil.show(con, ActivityConstants.S_NjmLRNpoIb);
//                Toast.makeText(con,ActivityConstants.S_OVwGdddWJa,Toast.LENGTH_LONG).show();
                return;
            }

            MCLog.e(TAG, "fun#subAccToSecVal id = " + info.getId());
            account = info.getAccount();
            id = info.getId();
            email = info.geteMail();
            mPhone = info.getPhoneNumber();

            String validateCode = Constant.REGULAR_PHONENUMBER;
            if (!TextUtils.isEmpty(mPhone) && !mPhone.matches(validateCode)) {
                ToastUtil.show(con, ActivityConstants.S_myMTvvtLnU + mPhone);
                return;
            }

            if (email != null && mPhone != null && !mPhone.equals("") && !email.equals("")) {
                btnQiehuan.setVisibility(View.VISIBLE);
            } else {
                btnQiehuan.setVisibility(View.GONE);
            }

            if (Constant.REGULAR_MAIL(info.geteMail())) {
                tv_hint.setText(ActivityConstants.S_HqpGAAtaUe);
                tv_hint_2.setText(ActivityConstants.S_utGXUQGKYq);
                int i = email.indexOf("@");
                teptwo_tv_phonenumber.setText(email);
                teptwo_tv_useraccount.setText(info.getAccount());
            } else if (!TextUtils.isEmpty(mPhone) && mPhone.matches(validateCode)) {
                tv_hint.setText(ActivityConstants.S_AnnVSQXUUA);
                tv_hint_2.setText(ActivityConstants.S_rZuoGnbtuQ);
                String showPhoNum = mPhone.substring(0, 3) + "****" + mPhone.substring(7, 11);
                teptwo_tv_phonenumber.setText(showPhoNum);
                teptwo_tv_useraccount.setText(info.getAccount());
            }
            step_secval();
        }
    };

    /**
     * 获取验证码
     */
    private void getPhoneValidateNumber() {
        teptwo_btn_checknumber.setEnabled(false);
            teptwo_btn_checknumber.setTextColor(getResources().getColor(getColor("mch_yanse")));

        String s = tv_hint.getText().toString();
        if (s.equals(ActivityConstants.S_HqpGAAtaUe) && Constant.REGULAR_MAIL(email)) {
            VerificationMailCodeProcess mailCodeProcess = new VerificationMailCodeProcess();
            mailCodeProcess.setMail(email);
            mailCodeProcess.setCodeType(2);
            mailCodeProcess.setAccount(account);
            mailCodeProcess.post(myHandler);
        } else {
            VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
            verifyCodeProcess.setPhone(mPhone);
            verifyCodeProcess.setReg("2");
            verifyCodeProcess.setAccount(account);
            verifyCodeProcess.post(myHandler);
        }
    }
}
