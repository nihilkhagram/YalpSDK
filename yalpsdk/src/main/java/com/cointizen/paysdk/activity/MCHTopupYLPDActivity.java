package com.cointizen.paysdk.activity;

import static com.cointizen.paysdk.AppConstants.STR_49a213e91491a2adcf1dd4722b0d44c3;
import static com.cointizen.paysdk.AppConstants.STR_f5d2226c360da912951f424524181024;
import static com.cointizen.paysdk.bean.PayMethod.MGATE;
import static com.cointizen.paysdk.bean.PayMethod.STRIPE_USD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cointizen.open.ApiCallback;
import com.cointizen.open.FlagControl;
import com.cointizen.paysdk.bean.MCPayModel;
import com.cointizen.paysdk.bean.PayMethod;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.bean.pay.MGatePay;
import com.cointizen.paysdk.bean.pay.StripePay;
import com.cointizen.paysdk.callback.MGatePayCallback;
import com.cointizen.paysdk.callback.StripePayCallback;
import com.cointizen.paysdk.entity.ChannelAndGameInfo;
import com.cointizen.paysdk.payment.MGatePaymentRequest;
import com.cointizen.paysdk.payment.StripePaymentRequest;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.callback.ScanPayCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.dialog.ScanPayDialog;
import com.cointizen.paysdk.entity.GamePayTypeEntity;
import com.cointizen.paysdk.entity.WXOrderInfo;
import com.cointizen.paysdk.http.process.PayTypeProcess;
import com.cointizen.paysdk.http.process.UserInfoProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.testwatch.PtbNumWatcher;
import com.cointizen.paysdk.utils.DeviceInfo;
import com.cointizen.paysdk.utils.MCHEtUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.MCMoneyUtils;

/**
 * 描述：平台币充值
 * 时间: 2018-09-29 9:46
 */
public class MCHTopupYLPDActivity extends MCHBaseCompatAcitvity {

    private static final String TAG = "MCPayPTBActivity";

    private final int PAYTYPE_WX = 0x01;
    private final int PAYTYPE_ZFB = 0x02;

    private Context context;
    private GamePayTypeEntity payTypeEntity;
    private float amount;

    private TextView txtAccount;
    private TextView btnTopupYLPD;
    private TextView txtPayRmb;
//    private LinearLayout llZFBPay;
//    private ImageView imgbtnSelZFB;

    private ImageView imgMGate;
    private ImageView imgStripeCard;

    private View layoutMGate;
    private View layoutStripeCard;


    private ImageView btnMGate;
    private ImageView btnStripeCreditCard;

    private View btnSelZFB;
//    private LinearLayout llWXPay;
//    private ImageView imgbtnSelWX;
    private View btnSelWX;
    private MCTipDialog mcTipDialog;
    private boolean isZFBWapPay; //用于操作支付宝支付时是走App支付还是WAP支付
    private TextView btnScanToPay;
    private PayMethod payType = MGATE;


    private StripePaymentRequest stripePaymentRequest;
    private MGatePaymentRequest mGatePaymentRequest;

    /**
     * 或取支付方式
     */
    private final Handler payTypeHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.GAME_PAY_TYPE_SUCCESS:
                    payTypeEntity = (GamePayTypeEntity)msg.obj;
                    handlerGamePayType();
                    break;
                case Constant.GAME_PAY_TYPE_FAIL:
                    MCLog.w(TAG, ActivityConstants.Log_koWDmJtsOx + (String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.GET_USER_INFO_SUCCESS:
                    handlerUserInfo(msg.obj);
                    break;
                case Constant.GET_USER_INFO_FAIL:
                    String tip = (String) msg.obj;
                    show(tip);
                    finish();
                    break;
                case Constant.WFT_ORDERINFO_SUCCESS:
                    dismisDialog();
                    boolean isWxAvaliable = DeviceInfo.isWeixinAvilible(context);
                    if (!isWxAvaliable) {
                        ToastUtil.show(context, ActivityConstants.S_SwxZhiIlmE);
                        FlagControl.BUTTON_CLICKABLE = true;
                        return;
                    }
                    WXOrderInfo wxOrderInfo = (WXOrderInfo) msg.obj;
                    Intent intent = new Intent(context, MCHWapPayActivity.class);
                    intent.putExtra("WapPayOrderInfo", wxOrderInfo);
                    context.startActivity(intent);
                    break;
                case Constant.WFT_ORDERINFO_FAIL:
                    dismisDialog();
                    show(ActivityConstants.S_SUmmRRhKBU + msg.obj);
                    FlagControl.BUTTON_CLICKABLE = true;
                    break;

                case Constant.ZFB_WAPPAY_ORDERINFO_SUCCESS:
                    WXOrderInfo zfbwapPayOrderInfo = (WXOrderInfo) msg.obj;
                    Intent zfbWapPayintent = new Intent(context, MCHWapPayActivity.class);
                    zfbWapPayintent.putExtra("WapPayOrderInfo", zfbwapPayOrderInfo);
                    context.startActivity(zfbWapPayintent);
                    break;
                case Constant.ZFB_WAPPAY_ORDERINFO_FAIL:
                    dismisDialog();
                    show(ActivityConstants.S_SUmmRRhKBU + msg.obj);
                    FlagControl.BUTTON_CLICKABLE = true;
                    break;
                default:
                    break;
            }
            dismisDialog();
        }
    };
    private void handlerGamePayType() {
        int selectType = -1;
        selectType = payTypeEntity.isHaveWX ? PAYTYPE_WX : selectType;
        selectType = payTypeEntity.isHaveZFB ? PAYTYPE_ZFB : selectType;
        if (-1 != selectType) {
            btnTopupYLPD.setVisibility(View.VISIBLE);
        }

        isZFBWapPay = payTypeEntity.ZFBisWapPay;
        btnScanToPay.setVisibility(payTypeEntity.isHaceScan ? View.VISIBLE : View.GONE);
//        selectPayType(selectType);
    }

    protected void handlerUserPtb() {
        String oldPtb = UserLoginSession.getInstance().getUserPtb();
        float ptbOld = Float.parseFloat(oldPtb);
        UserLoginSession.getInstance().getChannelAndGame().setPlatformMoney(amount + ptbOld);
        MCLog.w(TAG, "rmb = " + amount + ", ptbOld = " + ptbOld);
        show(ActivityConstants.S_IPwUdxxUls);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_pay_ptb"));
        context = this;
        amount = 0;
        View btnMchBack = findViewById(getId("btn_mch_back"));
        View btnMchCzjl = findViewById(getId("btn_mch_czjl"));
        initView();
        initPay();
        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
        btnMchCzjl.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                startActivity(new Intent(MCHTopupYLPDActivity.this, MCHMoneyRecordActivity.class));
            }
        });

        stripePaymentRequest = new StripePaymentRequest(this);
        mGatePaymentRequest = new MGatePaymentRequest(this);
    }

    //加载支付列表
    private void initPay() {
        FlagControl.BUTTON_CLICKABLE = true;
        btnTopupYLPD.setVisibility(View.GONE);
        PayTypeProcess payTypeProcess = new PayTypeProcess();
        payTypeProcess.post(payTypeHandler);
    }

    private void initView() {
        btnScanToPay = findViewById(getId("btn_saoma_pay"));
        btnScanToPay.setVisibility(View.GONE);
        txtAccount = findViewById(getId("edt_mch_account"));

        btnTopupYLPD = findViewById(getId("btn_mch_topup_ylpd"));
        btnTopupYLPD.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                checkAccount();
            }
        });

        txtPayRmb = (TextView) findViewById(getId("txt_mch_pay_rmb"));
        txtPayRmb.setText("0");
        //充值数量EditText
        EditText edtPtbNum = (EditText) findViewById(getId("edt_mch_ptb_number"));
        edtPtbNum.addTextChangedListener(new PtbNumWatcher(this,txtPayRmb, btnTopupYLPD, edtPtbNum));
        txtAccount.setText(UserLoginSession.getInstance().getAccount());

        RelativeLayout rlClear=(RelativeLayout) findViewById(getId("rl_mch_add_ptb_clear"));
        new MCHEtUtils().etHandle(context,edtPtbNum,rlClear,null,null);


        imgMGate = findViewById(getId("mch_img_choose_mgate"));
        imgStripeCard = findViewById(getId("mch_img_choose_stipe_stripe_card"));



        btnMGate = findViewById(getId("btn_mgate_pay"));
        btnStripeCreditCard = findViewById(getId("btn_stripe_card_pay"));

        btnMGate.setOnClickListener(mgateBtnClickListener);
        btnStripeCreditCard.setOnClickListener(stripeCreditCardBtnClickListener);

        layoutMGate = findViewById(getId("layout_mgate"));
        layoutStripeCard = findViewById(getId("layout_stripe_card"));

        layoutMGate.setOnClickListener(mgateBtnClickListener);
        layoutStripeCard.setOnClickListener(stripeCreditCardBtnClickListener);

        btnScanToPay.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                amount = MCMoneyUtils.priceToFloat(txtPayRmb.getText().toString().trim());
                if (amount != 0) {
                    ApiCallback.setScanPayCallback(mScanPayCallback);
                    ScanPayDialog scanPayDialog = new ScanPayDialog(MCHTopupYLPDActivity.this,
                            MCHInflaterUtils.getIdByName(context, "style", "mch_MyDialogStyle"),
                            payTypeEntity != null && payTypeEntity.isHaveZFB,
                            payTypeEntity != null && payTypeEntity.isHaveWX);

                    scanPayDialog.setGoodsName(ActivityConstants.S_etAYgQxTaf);
                    scanPayDialog.setGoodsPrice(String.format("%.2f", amount));
                    scanPayDialog.setGoodsDesc(ActivityConstants.S_cMseCldAjH);
                    scanPayDialog.setExtend(ActivityConstants.S_cMseCldAjH);
                    scanPayDialog.setPayType("0");
                    scanPayDialog.show();
                    FlagControl.BUTTON_CLICKABLE = false;
                } else {
                    show(ActivityConstants.S_aDegYUqSXh);
                }

            }
        });
    }
    private final View.OnClickListener mgateBtnClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            payType = MGATE;
            showChooseIcon(payType);
        }
    };
    private final View.OnClickListener stripeCreditCardBtnClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            payType = STRIPE_USD;
            showChooseIcon(payType);
        }
    };


    //扫码支付结果回调
    private ScanPayCallback mScanPayCallback = new ScanPayCallback() {
        @Override
        public void onResult(String code) {
            MCLog.e(TAG, "fun # scanPayCallback code = " + code);
            FlagControl.BUTTON_CLICKABLE =true;
            if (code.equals("0")) {
                show(ActivityConstants.S_leNmFnXvpE);
                finish();
            } else {
                show(ActivityConstants.S_AQylfgcFqy);
            }
        }
    };

//    private View.OnClickListener selectPayListener = new OnMultiClickListener() {
//        @Override
//        public void onMultiClick(View v) {
//            selectPayType((int) v.getTag());
//        }
//    };

    /**
     * 处理查询的用户信息
     */
    protected void handlerUserInfo(Object obj) {
        ChannelAndGameInfo info = (ChannelAndGameInfo) obj;
        if (null != info) {
            topup();
        }
    }

    /**
     * 检查账号
     */
    protected void checkAccount() {
        String account = txtAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            show(ActivityConstants.S_WqdODiyiKR);
            return;
        }

        if (UserLoginSession.getInstance().getAccount().equals(account)) {
            topup();
        } else {
            UserInfoProcess userInfoProcess = new UserInfoProcess();
            userInfoProcess.post(mHandler);
        }
    }

    /**
     * 充值
     */
    private void topup() {
        amount = MCMoneyUtils.priceToFloat(txtPayRmb.getText().toString().trim());
        if (amount != 0) {
            if (FlagControl.BUTTON_CLICKABLE) {
                topupYLPD();
                FlagControl.BUTTON_CLICKABLE = false;
            }
        } else {
            show(ActivityConstants.S_aDegYUqSXh);
        }
    }

    protected void topupYLPD() {
        switch (payType) {
            case MGATE:
                startMGatePay();
                break;
            case STRIPE_USD:
                startStripePay();
                break;
            default:
                break;
        }
    }
    private void startMGatePay() {
        ApiCallback.setMGatePayCallback(mGatePayCallback);
        MGatePay mGatePay = new MGatePay(this, mGatePaymentRequest);
        mGatePay.topupYLPD(STR_49a213e91491a2adcf1dd4722b0d44c3, STR_f5d2226c360da912951f424524181024, amount, STR_f5d2226c360da912951f424524181024);
        new Handler().postDelayed(() -> dismisDialog(),1000);
    }

    private void startStripePay() {
        ApiCallback.setZFBWapPayCallback(stripePayCallback);
        StripePay stripePay = new StripePay(this, stripePaymentRequest);
        stripePay.topupYLPD(STR_49a213e91491a2adcf1dd4722b0d44c3, STR_f5d2226c360da912951f424524181024, amount, STR_f5d2226c360da912951f424524181024, null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismisDialog();
            }
        },1000);
    }

    private final MGatePayCallback mGatePayCallback = code -> {
        MCLog.e(TAG, "fun # zfbPayCallback code = " + code);
        sendPayResult(code);
    };

    private final StripePayCallback stripePayCallback = code -> {
        MCLog.e(TAG, "fun # zfbPayCallback code = " + code);
        sendPayResult(code);
    };

    protected void sendPayResult(String payResultCode) {
        MCPayModel.Instance().getPaymentCallback().callback(payResultCode);
        finish();
    }
    /**
     * 平台币支付宝充值
     */
//    private void submitZfbPay() {
//        ZfbPay zfbPay = new ZfbPay((Activity) context, isZFBWapPay);
//        if (isZFBWapPay) {
//            ApiCallback.setZFBWapPayCallback(mStripePayCallback);
//            zfbPay.zfbPayPTBWapProcess("平台币", String.format("%.2f", rmb), "平台币充值", zfbBuPtbEvent, mHandler);
//            showDialog("给支付宝下单...");
//        }else {
//            zfbPay.zfbPayPTBProcess("平台币", String.format("%.2f", rmb), "平台币充值", zfbBuPtbEvent);
//        }
//    }
//
//
//    private StripePayCallback mStripePayCallback = new StripePayCallback() {
//        @Override
//        public void onResult(String code) {
//            MCLog.e(TAG, "fun # zfbPayCallback code = " + code);
//            if (code.equals("0")) {
//                FlagControl.flag=true;
//                show("Payment done successfully！");
//                finish();
//            } else {
//                show("Payment failed！");
//            }
//        }
//    };
//
//    private ZfbBuPtbEvent zfbBuPtbEvent = new ZfbBuPtbEvent() {
//        @Override
//        public void buyPTBResult(boolean issuccess) {
//            if (issuccess) {
//                handlerUserPtb();
//            } else {
//                MCLog.e(TAG, "购买平台币失败！");
//            }
//        }
//    };
//
//    // TODO 微富通
//    private void wftPayProcess(){
//        ApiCallback.setWFTWapPayCallback(mWFTWapPayCallback);
//        WFTOrderInfoProcess wftProcess = new WFTOrderInfoProcess();
//        wftProcess.setGoodsName("平台币");
//        wftProcess.setGoodsPrice(String.format("%.2f", rmb));
//        wftProcess.setGoodsDesc("平台币充值");
//        wftProcess.setExtend("平台币充值");
//        wftProcess.setPayType("0");
//        wftProcess.post(mHandler);
//        showDialog("正在给微信下单..");
//    }
//
//    private WFTWapPayCallback mWFTWapPayCallback = new WFTWapPayCallback() {
//        @Override
//        public void onResult(String code) {
//            MCLog.e(TAG, "fun # wftPayCallback code = " + code);
//            if (code.equals("0")) {
//                handlerUserPtb();
//            } else {
//                show("Payment failed！");
//            }
//        }
//    };

    private void showChooseIcon(PayMethod payType){
        imgMGate.setVisibility(View.GONE);
        imgStripeCard.setVisibility(View.GONE);
        switch (payType){
            case MGATE:
                imgMGate.setVisibility(View.VISIBLE);
                break;
            case STRIPE_USD:
                imgStripeCard.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * 选择支付方式
     *
     * @param type
     */
//    protected void selectPayType(int type) {
//        payType = type;
////        imgbtnSelZFB.setBackgroundResource(getDrawable("mch_choosepay_unselect"));
////        imgbtnSelWX.setBackgroundResource(getDrawable("mch_choosepay_unselect"));
//        switch (type) {
//            case PAYTYPE_ZFB:
////                imgbtnSelZFB.setBackgroundResource(getDrawable("mch_choosepay_select"));
//                break;
//            case PAYTYPE_WX:
////                imgbtnSelWX.setBackgroundResource(getDrawable("mch_choosepay_select"));
//                break;
//            default:
//                break;
//        }
//
//    }

    /**
     * 退出监听
     */
    View.OnClickListener backClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            MCHTopupYLPDActivity.this.finish();
        }
    };



    /**
     * 关闭提示信息
     */
    private void dismisDialog() {
        if (null != mcTipDialog) {
            mcTipDialog.dismiss();
        }
    }

    /**
     * 打开提示信息
     */
    private void showDialog(String message) {
        dismisDialog();
        mcTipDialog = new MCTipDialog.Builder().setMessage(message).show(
                context, ((Activity) context).getFragmentManager());
    }

    /**
     * @param message 吐司内容
     */
    private void show(String message) {
        ToastUtil.show(context, message);
        MCLog.e(TAG, message);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5001) {
            if(resultCode == Activity.RESULT_OK){
                int result=data.getIntExtra("result",0);
                Log.e(TAG, "onActivityResult: "+result );
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }

            if (resultCode == Activity.RESULT_CANCELED) {
                new Handler().postDelayed(() -> Toast.makeText(context, "Please Try Again!", Toast.LENGTH_LONG).show(),1000);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!FlagControl.BUTTON_CLICKABLE) {
            FlagControl.BUTTON_CLICKABLE = true;
        }

    }
}
