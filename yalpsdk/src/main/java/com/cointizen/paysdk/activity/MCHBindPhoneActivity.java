package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.bean.VerifyCode;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.ChangeUserInfoProcess;
import com.cointizen.paysdk.http.process.VerificationCodeProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.observer.SecondsWatcher;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.view.util.TimeFactory;

public class MCHBindPhoneActivity extends MCHBaseActivity {

    private static final String TAG = "MCBindPhoneActivity";
    Activity context;
    String checkNumber;// 验证码
    //上面三个数字
    TextView step1, step_verifyidentity, step_securityphone;
    //上面三个步骤
    TextView step1_, step_verifyidentity_, step_securityphone_;
    //步骤间的横线
    View lineFirst, lineSecond;
    //绑定手机第一个界面
    RelativeLayout bindphone_body_setup1;
    //绑定手机第二个界面
    LinearLayout bindphone_body_setup2;
    //绑定手机成功界面
    LinearLayout bindphone_body_setsuccess;
    //解绑手机按钮和输入手机验证码的输入框
    LinearLayout mch_ll_unbindphone;
    LinearLayout ll_mch_bindphone_top;
    Button btn_unbindstep1_cancel;
    TextView btn_unbindstep1_sure;
    LinearLayout ll_mch_unbindstep1;
    //step1------------------------------------------
    //密码输入框
    LinearLayout ll_mch_unbindstep2;
    EditText edt_bindphone;
    //确定定按钮
    TextView btn_bindphone1;
    LinearLayout ll_mch_unbindstep3;
    Button btn_unbindstep3_return;
    //step2------------------------------------------
    //手机号码输入框
    EditText step2_edt_phone;
    //验证码输入框
    EditText step2_edt_checknum;
    //验证码按钮
    Button step2_btn_checknum;
    //上一步
    TextView step2_btn_back;
    //下一步
    TextView step2_btn_next;
    //联系客服
    TextView step2_tv_service;
    //step3----------------------------------------
    //已绑定手机号
    TextView txt_bindphone_tip;
    //已绑定手机号输入框
    EditText edt_unbindphone;
    Button btn_unbindstep2_previous;
    TextView btn_unbindstep2_unbind;


    private int step;
    private int isUnBind = 0;
    //	TextView text_title_add;
//	RelativeLayout relativeLayout11;
    TextView text_back;
    private TextView txtTitle;
    private TextView tvMchYuanPhone;
    private TextView btnGetCode;
    private EditText edtNewPhone;
    private EditText edtUnbindphone2;
    private TextView btnGetCode2;
    private TextView btnUnbindstep2Unbind2;
    private TextView teptwoTvService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(getLayout("mch_activity_personal_info_bindphone2"));
        initView();
        showInit();
    }

    private void initView() {
        MCLog.e(TAG, "fun#initView");
        txtTitle = (TextView) findViewById(MCHInflaterUtils.getControl(this, "tv_title"));
        RelativeLayout ivBack =  findViewById(MCHInflaterUtils.getControl(this, "btn_mch_back"));
        ivBack.setOnClickListener(backClick);

        //最上面步骤显示
        step1 = (TextView) findViewById(getId("step1"));
        step_verifyidentity = (TextView) findViewById(getId("step_verifyidentity"));
        step_securityphone = (TextView) findViewById(getId("step_securityphone"));
        step1_ = (TextView) findViewById(getId("step1_"));
        step_verifyidentity_ = (TextView) findViewById(getId("step_verifyidentity_"));
        step_securityphone_ = (TextView) findViewById(getId("step_securityphone_"));
        lineFirst = (View) findViewById(getId("line_bindphone_1"));
        lineSecond = (View) findViewById(getId("line_bindphone_2"));

        //绑定手机第一个界面
        bindphone_body_setup1 = (RelativeLayout) findViewById(getId("bindphone_body_setup1"));
        //绑定手机第二个界面
        bindphone_body_setup2 = (LinearLayout) findViewById(getId("bindphone_body_setup2"));
        //绑定手机成功界面
        bindphone_body_setsuccess = (LinearLayout) findViewById(getId("bindphone_body_setsuccess"));
        //解除绑定界面
        mch_ll_unbindphone = (LinearLayout) findViewById(getId("mch_ll_unbindphone"));
        ll_mch_bindphone_top = (LinearLayout) findViewById(getId("ll_mch_bindphone_top"));
        ll_mch_unbindstep1 = (LinearLayout) findViewById(getId("ll_mch_unbindstep1"));
        ll_mch_unbindstep2 = (LinearLayout) findViewById(getId("ll_mch_unbindstep2"));
        ll_mch_unbindstep3 = (LinearLayout) findViewById(getId("ll_mch_unbindstep3"));
        //step1------------------------------------------
         btn_unbindstep1_cancel = (Button) findViewById(getId("btn_unbindstep1_cancel"));
        btn_unbindstep1_sure = (TextView) findViewById(getId("btn_unbindstep1_sure"));
        btn_unbindstep1_cancel.setOnClickListener(clickListener);
        btn_unbindstep1_sure.setOnClickListener(clickListener);
        //密码输入框
        edt_bindphone = (EditText) findViewById(getId("edt_bindphone"));
        //确定定按钮
        btn_bindphone1 = (TextView) findViewById(getId("btn_bindphone1"));
        btn_bindphone1.setOnClickListener(clickListener);
        //step2------------------------------------------
        //手机号码输入框
        step2_edt_phone = (EditText) findViewById(getId("step2_edt_phone"));
        //验证码输入框
        step2_edt_checknum = (EditText) findViewById(getId("step2_edt_checknum"));
        //验证码按钮
        step2_btn_checknum = (Button) findViewById(getId("step2_btn_checknum"));
        step2_btn_checknum.setOnClickListener(clickListener);
        //上一步
        step2_btn_back = (TextView) findViewById(getId("step2_btn_back"));
        step2_btn_back.setVisibility(View.GONE);
        step2_btn_back.setOnClickListener(clickListener);
        //联系客服
        teptwoTvService = (TextView) findViewById(getId("teptwo_tv_service"));
        teptwoTvService.setOnClickListener(clickListener);
        //下一步

        step2_btn_next = (TextView) findViewById(getId("step2_btn_next"));
        step2_btn_next.setOnClickListener(clickListener);

        step2_tv_service = (TextView) findViewById(getId("step2_tv_service"));
        step2_tv_service.setOnClickListener(clickListener);
        //step3----------------------------------------

        txt_bindphone_tip = (TextView) findViewById(getId("txt_bindphone_tip"));

        step_verifyidentity = (TextView) findViewById(getId("step_verifyidentity"));

        step_securityphone = (TextView) findViewById(getId("step_securityphone"));
        step_securityphone.setBackgroundResource(getDrawable("mch_step3_80x80_grey"));
        step_verifyidentity_ = (TextView) findViewById(getId("step_verifyidentity_"));
        step_verifyidentity_.setTextColor(Color.parseColor("#a0a0a0"));
        step_securityphone_ = (TextView) findViewById(getId("step_securityphone_"));
        step_securityphone_.setTextColor(Color.parseColor("#a0a0a0"));

        edt_unbindphone = (EditText) findViewById(getId("edt_unbindphone"));
        btn_unbindstep2_previous = (Button) findViewById(getId("btn_unbindstep2_previous"));
        btn_unbindstep2_unbind = (TextView) findViewById(getId("btn_unbindstep2_unbind"));
        btn_unbindstep2_previous.setOnClickListener(clickListener);
        btn_unbindstep2_unbind.setOnClickListener(clickListener);


        text_back = (TextView) findViewById(getId("text_back"));
        text_back.setOnClickListener(bindphonelinstener);

        tvMchYuanPhone = (TextView) findViewById(getId("tv_mch_yuanPhone"));
        btnGetCode = (TextView) findViewById(getId("btn_get_code"));
        btnGetCode.setOnClickListener(clickListener);
        btnGetCode2 = (TextView) findViewById(getId("btn_get_code2"));
        btnGetCode2.setOnClickListener(clickListener);
        btnUnbindstep2Unbind2 = (TextView) findViewById(getId("btn_unbindstep2_unbind2"));
        btnUnbindstep2Unbind2.setOnClickListener(clickListener);
        edtNewPhone = (EditText) findViewById(getId("edt_newPhone"));
        edtUnbindphone2 = (EditText) findViewById(getId("edt_unbindphone2"));
    }

    OnClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View view) {
            /** 解除绑定*/

            if (view.getId() == btn_unbindstep1_cancel.getId()) {
                MCHBindPhoneActivity.this.finish();
                return;
            }
            if (view.getId() == getId("btn_unbindstep2_unbind2")) {
               //提交新绑定的手机号
                UpNewPhone();
            }

            if(view.getId()==getId("btn_get_code")){
                //解绑手机号   获取验证码
               unBindPhone();
            }
            if(view.getId()==getId("btn_get_code2")){
                //绑定手机号   获取验证码
                bindNewPhone();
            }

            if (view.getId() == btn_unbindstep1_sure.getId()) {
                txtTitle.setText(ActivityConstants.S_JpYKYNRzbP);
                ll_mch_unbindstep2.setVisibility(View.VISIBLE);
                mch_ll_unbindphone.setVisibility(View.VISIBLE);
                ll_mch_unbindstep1.setVisibility(View.GONE);
                return;
            }
            if (view.getId() == btn_unbindstep2_previous.getId()) {
                ll_mch_unbindstep1.setVisibility(View.VISIBLE);
                ll_mch_unbindstep2.setVisibility(View.GONE);
                return;
            }
            if (view.getId() == btn_unbindstep2_unbind.getId()) {
                unbind();
                return;
            }
            /** 控制解绑显示隐藏*/
            if (view.getId() == txt_bindphone_tip.getId()) {
                if (isUnBind == 1) {
                    title1();
                    isUnBind = 0;
                } else if (isUnBind == 0) {
                    title2();
                    isUnBind = 1;
                }
                return;
            }
            /** 绑定手机验证密码*/
            if (view.getId() == btn_bindphone1.getId()) {
                bindstep1();
                return;
            }
            /** 绑定手机发验证码*/
            if (view.getId() == step2_btn_checknum.getId()) {
                bindstep2num();
                return;
            }
            /** 绑定手机上一步*/
            if (view.getId() == step2_btn_back.getId()) {
                bodystep1();
                return;
            }
            /** 绑定手机下一步*/
            if (view.getId() == step2_btn_next.getId()) {
                bindstep2();
                return;
            }
            if (view.getId() == teptwoTvService.getId()) {
                startActivity(new Intent(MCHBindPhoneActivity.this,MCHelperCenter.class));
                return;
            }

            if (view.getId() == step2_tv_service.getId()) {
                startActivity(new Intent(MCHBindPhoneActivity.this,MCHelperCenter.class));
                return;
            }
        }
    };

    /**
     * 提交新绑定的手机号
     */
    private void UpNewPhone() {
        String phone = edtNewPhone.getText().toString().trim();
        String code = edtUnbindphone2.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(context, ActivityConstants.S_QxWaKAxgRN);
            return;
        }
        String valCode = Constant.REGULAR_PHONENUMBER;
        if (!phone.matches(valCode)) {
            ToastUtil.show(context, ActivityConstants.S_hAXDoxdEVj);
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show(context, ActivityConstants.S_FKoZPHRjwy);
            return;
        }
        try {
            ChangeUserInfoProcess changePwdProcess = new ChangeUserInfoProcess();
            changePwdProcess.setCode(code);
            changePwdProcess.setPhone(phone);
            changePwdProcess.setType(2);
            changePwdProcess.post(myHandler);
        } catch (Exception e) {
            ToastUtil.show(context, ActivityConstants.S_VqxJPliJAk);
            e.printStackTrace();
        }
        return;
    }

    /**
     * 解绑手机号    获取验证码
     */
    private void unBindPhone() {
        TimeFactory.creator(2).getTimeChange().removeWatcher(secondsWatcher);
        TimeFactory.creator(3).getTimeChange().addWatcher(unBindsecondsWatcher);
        String phone = UserLoginSession.getInstance().getPhone();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(context, ActivityConstants.S_LPTmGToLTI);
            return;
        }
        String valCode = Constant.REGULAR_PHONENUMBER;
        if (!phone.matches(valCode)) {
            ToastUtil.show(context, ActivityConstants.S_TScCsmeqYk);
            return;
        }

            btnGetCode.setTextColor(getColor("mch_yanse"));

        btnGetCode.setEnabled(false);
        VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
        verifyCodeProcess.setPhone(phone);
        verifyCodeProcess.setReg("3");
        verifyCodeProcess.post(UnBindHandler);
    }

    /**
     * 绑定新手机    获取验证码
     */
    private void bindNewPhone() {
        TimeFactory.creator(3).getTimeChange().removeWatcher(unBindsecondsWatcher);
        TimeFactory.creator(4).getTimeChange().addWatcher(secondsWatcher2);
        String phone = edtNewPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(context, ActivityConstants.S_QxWaKAxgRN);
            return;
        }
        String valCode = Constant.REGULAR_PHONENUMBER;
        if (!phone.matches(valCode)) {
            ToastUtil.show(context, ActivityConstants.S_UyoSgvjqHa);
            return;
        }

            btnGetCode2.setTextColor(getColor("mch_yanse"));

        btnGetCode2.setEnabled(false);
        VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
        verifyCodeProcess.setPhone(phone);
        verifyCodeProcess.setReg("4");
        verifyCodeProcess.post(myHandler);
    }

    /**
     * 绑定手机发送验证码
     */
    private void bindstep2num() {
        TimeFactory.creator(3).getTimeChange().removeWatcher(unBindsecondsWatcher);
        TimeFactory.creator(2).getTimeChange().addWatcher(secondsWatcher);
        step2_btn_checknum.setEnabled(true);
        step2_btn_checknum.setTextColor(Color.parseColor("#19B1EA"));
        String phone = step2_edt_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(context, ActivityConstants.S_ohChymwZYw);
            return;
        }
        String valCode = Constant.REGULAR_PHONENUMBER;
        if (!phone.matches(valCode)) {
            ToastUtil.show(context, ActivityConstants.S_hAXDoxdEVj);
            return;
        }

            step2_btn_checknum.setTextColor(getColor("mch_yanse"));

        step2_btn_checknum.setEnabled(false);
        VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
        verifyCodeProcess.setPhone(phone);
        verifyCodeProcess.setType("1");
        verifyCodeProcess.setReg("4");
        verifyCodeProcess.post(myHandler);
    }


    /**
     * 绑定手机第一步
     */
    private void bindstep1() {
        String password = edt_bindphone.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show(context, ActivityConstants.S_miMsdGyGeB);
            return;
        }
        String realpassword = UserLoginSession.getInstance().getChannelAndGame().getPassword();
        if (!password.equals(realpassword)) {
            ToastUtil.show(context, ActivityConstants.S_yKZCYzfyBp);
            return;
        }
        bodystep2();
    }

    /**
     * 绑定手机
     */
    private void bindstep2() {
        String phone = step2_edt_phone.getText().toString().trim();
        String code = step2_edt_checknum.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(context, ActivityConstants.S_QxWaKAxgRN);
            return;
        }
        String valCode = Constant.REGULAR_PHONENUMBER;
        if (!phone.matches(valCode)) {
            ToastUtil.show(context, ActivityConstants.S_hAXDoxdEVj);
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show(context, ActivityConstants.S_FKoZPHRjwy);
            return;
        }
        try {

            ChangeUserInfoProcess changePwdProcess = new ChangeUserInfoProcess();
            changePwdProcess.setCode(code);
            changePwdProcess.setPhone(phone);
            changePwdProcess.setType(2);
            changePwdProcess.post(myHandler);
        } catch (Exception e) {
            ToastUtil.show(context, ActivityConstants.S_VqxJPliJAk);
            e.printStackTrace();
        }
        return;
    }

    OnClickListener backClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            /**返回监听*/
            if (v.getId() == getId("btn_mch_back")) {
                MCHBindPhoneActivity.this.finish();
                return;
            }
        }
    };

    private void showInit() {
        step = 1;
//        bodystep1();
        bodystep2();
        String phone = UserLoginSession.getInstance().getPhone();

        /**
         * 没有绑定手机
         */
        if (TextUtils.isEmpty(phone)) {
//            bodystep1();
            bodystep2();
        }
        /**
         * 已经绑定手机btn_bindphone
         */
        if (!TextUtils.isEmpty(phone) && phone.length() >= 11) {
            bodystepunbind1();
            StringBuilder tip = new StringBuilder();
            tip.append(phone.substring(0, 3)).append("****").append(phone.substring(7));
            txt_bindphone_tip.setText(tip);
            tvMchYuanPhone.setText(tip);
            txt_bindphone_tip.setOnClickListener(clickListener);
        }
    }

    OnClickListener bindphonelinstener = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            MCHBindPhoneActivity.this.finish();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler secondsHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (null != step2_btn_checknum) {
                        String seconds = (String) msg.obj;
                        if ("0".equals(seconds)) {
                            step2_btn_checknum.setText(ActivityConstants.S_EtkaEFEEft);
                            step2_btn_checknum.setEnabled(true);
                            step2_btn_checknum.setTextColor(Color.parseColor("#21B2EA"));
                        } else {
                            step2_btn_checknum.setText((String) msg.obj);
                            step2_btn_checknum.setTextColor(Color.parseColor("#a0a0a0"));
                            step2_btn_checknum.setEnabled(false);
                        }
                    }
                    break;
                case 1:
                    if (null != btnGetCode2) {
                        String seconds = (String) msg.obj;
                        if ("0".equals(seconds)) {
                            btnGetCode2.setText(ActivityConstants.S_EtkaEFEEft);
                            btnGetCode2.setEnabled(true);
                            btnGetCode2.setTextColor(Color.parseColor("#21B2EA"));
                        } else {
                            btnGetCode2.setText((String) msg.obj);
                            btnGetCode2.setTextColor(Color.parseColor("#a0a0a0"));
                            btnGetCode2.setEnabled(false);
                        }
                    }
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler UnBindSecondsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != btnGetCode) {
                String seconds = (String) msg.obj;
                if ("0".equals(seconds)) {
                    btnGetCode.setText(ActivityConstants.S_EtkaEFEEft);
                    btnGetCode.setEnabled(true);
                    btnGetCode.setTextColor(Color.parseColor("#21B2EA"));
                } else {
                    btnGetCode.setText((String) msg.obj);
                    btnGetCode.setTextColor(Color.parseColor("#a0a0a0"));
                    btnGetCode.setEnabled(false);
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        TimeFactory.creator(3).getTimeChange().addWatcher(unBindsecondsWatcher);
    }

    ;

    SecondsWatcher secondsWatcher = new SecondsWatcher() {

        @Override
        public void updateSeconds(String seconds) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = seconds;
            secondsHandler.sendMessage(msg);
        }
    };
    SecondsWatcher secondsWatcher2 = new SecondsWatcher() {

        @Override
        public void updateSeconds(String seconds) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = seconds;
            secondsHandler.sendMessage(msg);
        }
    };
    SecondsWatcher unBindsecondsWatcher = new SecondsWatcher() {

        @Override
        public void updateSeconds(String seconds) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = seconds;
            UnBindSecondsHandler.sendMessage(msg);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        TimeFactory.creator(2).getTimeChange().removeWatcher(secondsWatcher);
        TimeFactory.creator(4).getTimeChange().removeWatcher(secondsWatcher2);
        TimeFactory.creator(3).getTimeChange().removeWatcher(unBindsecondsWatcher);
    }


    @SuppressLint("HandlerLeak")
    public Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.VERIFYCODE_REQUEST_SUCCESS:// 请求获得手机验证码成功
                    step2_btn_checknum.setTextColor(Color.parseColor("#a0a0a0"));
                    step2_btn_checknum.setEnabled(false);
                    VerifyCode code = (VerifyCode) msg.obj;
                    showCode(code);
                    break;
                case Constant.VERIFYCODE_REQUEST_FAIL:// 请求获得手机验证码失败
                    String res = (String) msg.obj;
                    ToastUtil.show(context, res);
                    step2_btn_checknum.setText(ActivityConstants.S_EtkaEFEEft);
                    step2_btn_checknum.setEnabled(true);
                    btnGetCode2.setEnabled(true);

                    step2_btn_checknum.setTextColor(getColor("mch_yanse"));

                    break;
                case Constant.MODIFY_PASSWORD_SUCCESS:// 绑定手机成功
                    ToastUtil.show(context, ActivityConstants.S_yFaDcZwOTe);
                    TimeFactory.creator(5).calcel();
                    shouBindSuccess();
                    break;
                case Constant.MODIFY_PASSWORD_FAIL:// 绑定手机失败
                    String message = (String) msg.obj;
                    if (!TextUtils.isEmpty(message)) {
                        ToastUtil.show(MCHBindPhoneActivity.this, message);
                    }

                    break;
                default:
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    public Handler UnBindHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.VERIFYCODE_REQUEST_SUCCESS:// 请求获得手机验证码成功
                    VerifyCode code = (VerifyCode) msg.obj;
                    showUnBindCode(code);
                    break;
                case Constant.VERIFYCODE_REQUEST_FAIL:// 请求获得手机验证码失败
                    String res = (String) msg.obj;
                    ToastUtil.show(context, res);
                    break;
                case Constant.MODIFY_PASSWORD_SUCCESS:// 解绑手机成功
                    txtTitle.setText(ActivityConstants.S_TKJHZHQvek);
                    mch_ll_unbindphone.setVisibility(View.VISIBLE);
                    ll_mch_unbindstep3.setVisibility(View.VISIBLE);
                    ll_mch_unbindstep2.setVisibility(View.GONE);
                    ll_mch_unbindstep1.setVisibility(View.GONE);
                    ll_mch_bindphone_top.setVisibility(View.GONE);
                    txt_bindphone_tip.setVisibility(View.GONE);
                    ToastUtil.show(context, ActivityConstants.S_CwObHkkFgR);
                    TimeFactory.creator(5).calcel();
                    break;
                case Constant.MODIFY_PASSWORD_FAIL:// 解绑手机失败
                    String message = msg.obj.toString();
                    if (!TextUtils.isEmpty(message)) {
                        ToastUtil.show(context, message);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    protected void shouBindSuccess() {
        UserLoginSession.getInstance().setPhone(edt_bindphone.getText().toString().trim());
        bodystep3();
    }


    void showCode(VerifyCode code) {
        TimeFactory.creator(2).Start();
        ToastUtil.show(context, ActivityConstants.S_zQOuweocbp);
    }

    void showUnBindCode(VerifyCode code) {
        TimeFactory.creator(3).Start();
        ToastUtil.show(context, ActivityConstants.S_zQOuweocbp);

        edt_unbindphone.setText("");
    }

    /**
     * 绑定手机第一步
     */
    private void bodystep1() {
        title1();
        hidestep();
        bindphone_body_setup1.setVisibility(View.VISIBLE);
        step1.setBackgroundResource(getDrawable("mch_step1_80x80_blue"));

            step1_.setTextColor(getColor("mch_yanse"));
            lineFirst.setBackgroundColor(getColor("mch_yanse"));

    }

    /**
     * 绑定手机第二步
     */
    private void bodystep2() {
        title2();
        hidestep();
        bindphone_body_setup2.setVisibility(View.VISIBLE);
        step_verifyidentity.setBackgroundResource(getDrawable("mch_step2_80x80_blue"));

            step_verifyidentity_.setTextColor(getColor("mch_yanse"));
            lineSecond.setBackgroundColor(getColor("mch_yanse"));

    }

    /**
     * 绑定手机第三步（成功界面）
     */
    private void bodystep3() {
        title3();
        hidestep();
        bindphone_body_setsuccess.setVisibility(View.VISIBLE);
    }

    /**
     * 解除绑定
     */
    private void bodystepunbind1() {
        txtTitle.setText(ActivityConstants.S_IXfaBeyhBp);
        hidestep();
        //设置解绑界面显示
        mch_ll_unbindphone.setVisibility(View.VISIBLE);
        ll_mch_unbindstep1.setVisibility(View.VISIBLE);
        ll_mch_bindphone_top.setVisibility(View.GONE);
    }

    /**
     * 设置所有步骤不显示
     */
    private void hidestep() {
        bindphone_body_setup1.setVisibility(View.GONE);
        bindphone_body_setup2.setVisibility(View.GONE);
        bindphone_body_setsuccess.setVisibility(View.GONE);
        ll_mch_bindphone_top.setVisibility(View.GONE);
        ll_mch_unbindstep3.setVisibility(View.GONE);
        ll_mch_unbindstep2.setVisibility(View.GONE);
        ll_mch_unbindstep1.setVisibility(View.GONE);
    }

    /**
     * 第一步显示
     */
    private void title1() {
        txtTitle.setText(ActivityConstants.S_YdXmAKmZYG);
        step1.setBackgroundResource(getDrawable("mch_step1_80x80_blue"));
        step_verifyidentity.setBackgroundResource(getDrawable("mch_step2_80x80_grey"));
        step_securityphone.setBackgroundResource(getDrawable("mch_step3_80x80_grey"));
        step_verifyidentity_.setTextColor(Color.parseColor("#a0a0a0"));
        step_securityphone_.setTextColor(Color.parseColor("#a0a0a0"));

            lineFirst.setBackgroundColor(getColor("mch_yanse"));
            step1_.setTextColor(getColor("mch_yanse"));

        lineSecond.setBackgroundColor(Color.parseColor("#a0a0a0"));
    }

    private void title2() {
        txtTitle.setText(ActivityConstants.S_BbpjqGDnrq);
        step1.setBackgroundResource(getDrawable("mch_step1_80x80_blue"));
        step_verifyidentity.setBackgroundResource(getDrawable("mch_step2_80x80_blue"));
        step_securityphone.setBackgroundResource(getDrawable("mch_step3_80x80_grey"));
        step_securityphone_.setTextColor(Color.parseColor("#a0a0a0"));

            lineFirst.setBackgroundColor(getColor("mch_yanse"));
            step1_.setTextColor(getColor("mch_yanse"));
            step_verifyidentity_.setTextColor(getColor("mch_yanse"));
            lineSecond.setBackgroundColor(getColor("mch_yanse"));

    }

    private void title3() {
        txtTitle.setText(ActivityConstants.S_yFaDcZwOTe);
        step1.setBackgroundResource(getDrawable("mch_step1_80x80_blue"));
        step_verifyidentity.setBackgroundResource(getDrawable("mch_step2_80x80_blue"));
        step_securityphone.setBackgroundResource(getDrawable("mch_step3_80x80_blue"));

            lineFirst.setBackgroundColor(getColor("mch_yanse"));
            step1_.setTextColor(getColor("mch_yanse"));
            step_verifyidentity_.setTextColor(getColor("mch_yanse"));
            lineSecond.setBackgroundColor(getColor("mch_yanse"));
            step_securityphone_.setTextColor(getColor("mch_yanse"));

    }

    /**
     * 执行解除绑定操作
     */
    private void unbind() {
        String code = edt_unbindphone.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show(context, ActivityConstants.S_FKoZPHRjwy);
            return;
        }
        try {

            ChangeUserInfoProcess changePwdProcess = new ChangeUserInfoProcess();
            changePwdProcess.setPhone(UserLoginSession.getInstance().getChannelAndGame().getPhoneNumber());
            changePwdProcess.setCode(code);
            changePwdProcess.setType(1);
            changePwdProcess.post(UnBindHandler);
        } catch (Exception e) {
            ToastUtil.show(context, ActivityConstants.S_VqxJPliJAk);
            e.printStackTrace();
        }
        return;
    }
}
