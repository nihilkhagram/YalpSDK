package com.cointizen.paysdk.bean;

import static com.cointizen.paysdk.bean.PayMethod.BINDING_DOLLAR;
import static com.cointizen.paysdk.bean.PayMethod.MGATE;
import static com.cointizen.paysdk.bean.PayMethod.STRIPE_USD;
import static com.cointizen.paysdk.bean.PayMethod.YLPD;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cointizen.open.ApiCallback;
import com.cointizen.open.FlagControl;
import com.cointizen.paysdk.activity.MCHCouponMyActivity;
import com.cointizen.paysdk.activity.MCHWapPayActivity;
import com.cointizen.paysdk.bean.pay.MGatePay;
import com.cointizen.paysdk.bean.pay.OttUPay;
import com.cointizen.paysdk.bean.pay.StripePay;
import com.cointizen.paysdk.callback.MGatePayCallback;
import com.cointizen.paysdk.callback.ScanPayCallback;
import com.cointizen.paysdk.callback.StripePayCallback;
import com.cointizen.paysdk.callback.WFTWapPayCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.dialog.PTBPayResultDialog;
import com.cointizen.paysdk.dialog.ScanPayDialog;
import com.cointizen.paysdk.entity.GamePayTypeEntity;
import com.cointizen.paysdk.entity.PTBPayResult;
import com.cointizen.paysdk.entity.UserDiscountEntity;
import com.cointizen.paysdk.entity.UserPTBInfo;
import com.cointizen.paysdk.entity.WXOrderInfo;
import com.cointizen.paysdk.http.process.GetAvailableCouponProcess;
import com.cointizen.paysdk.http.process.PTBPayProcess;
import com.cointizen.paysdk.http.process.PayTypeProcess;
import com.cointizen.paysdk.http.process.UserDiscountProcess;
import com.cointizen.paysdk.http.process.UserPtbBalanceProcess;
import com.cointizen.paysdk.http.process.UserPtbRemainProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.payment.MGatePaymentRequest;
import com.cointizen.paysdk.payment.OttUpayPaymentRequest;
import com.cointizen.paysdk.payment.StripePaymentRequest;
import com.cointizen.paysdk.service.ServiceManager;
import com.cointizen.paysdk.utils.DeviceInfo;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.MCMoneyUtils;
import com.cointizen.paysdk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.cointizen.paysdk.AppConstants;

/**
 * 支付相关逻辑
 * 20200214
 */

@SuppressLint("NewApi")
public class ChoosePaymentMethod {
    private static final String TAG = "ChoosePaymentMethod";
    private Activity context;
    private View view;

    // 用户平台币余额
    private String userPtbMoney = "";
    // 用户绑定平台币余额
    private String userBindPtbMoney = "";
    // 获取平台币信息提示框
    MCTipDialog mcTipDialog;

    GamePayTypeEntity typeEntity;
    private boolean isBind; //选中的是平台币还是绑币支付
    private boolean isChooseCoupon; //是否选择了代金券
    private boolean isHaveDiscount;
    private String listPrice = "0"; //原价
    private String toPayPrice = "0";  //最终的实际价格
    private String couponID = ""; //选中的代金券id
    private double couponPrice; //减去代金券后的价格
    private int selectPosition = -1;  //选中的券的坐标

    private float buyGoodsDiscount;

    private View layoutMGate;
    private View layoutStripeCard;

    private View layoutYLPD;
    private View layoutBindingDollar;
    private View layoutVerificationCodeBox;
    private TextView btnScanPay;
    private TextView tvMchPrice; //折扣后的实际价
    private TextView tvRealPrice; //顶部需支付的价格
    private TextView tvBanalcePTB;
    private TextView tvBanalceBindPTB;
    private ImageView imgMGate;
    private ImageView imgStripeCard;
    private ImageView imgYLPD;
    private ImageView imgBindingDollar;
    private TextView tvCouponPrice;  //使用的代金券减去金额文字
    private TextView tvCanUseCouponNum;  //可用的代金券数量
    private TextView txtPayTimer;
    private TextView txtPayRemark;
    private ImageView btnMGate;
    private ImageView btnStripeCreditCard;

    private ImageView btnYLPD;
    private ImageView btnBindingDollar;

    private TextView btnGetEmailValidateMessage;
    private TextView btnGetPhoneValidateMessage;

    private PayMethod payType = STRIPE_USD;  // payment method：1 YLP 2微信 3平台币 4绑币

    private List<CouponBean> couponList = new ArrayList<>(); //满足金额条件，可用的代金券
    CountDownTimer timer;
    //Stripe params
    private StripePaymentRequest stripePaymentRequest;
    private MGatePaymentRequest mGatePaymentRequest;
    private OttUpayPaymentRequest ottUpayPaymentRequest;

    public ChoosePaymentMethod(AppCompatActivity context, View view) {
        init(context, view);
        initView();
        initPaymentInfo();
        stripePaymentRequest = new StripePaymentRequest(context);
        mGatePaymentRequest = new MGatePaymentRequest(context);
        ottUpayPaymentRequest = new OttUpayPaymentRequest(context);

    }

    private void init(Activity context, View view) {
        this.context = context;
        this.view = view;
    }

    private void initView() {

        btnScanPay = view.findViewById(idName("btn_scan_pay"));
        btnScanPay.setVisibility(View.GONE);
        tvRealPrice = view.findViewById(idName("tv_real_price"));
        TextView tvProductName = view.findViewById(idName("tv_wupin_name"));
        tvMchPrice = view.findViewById(idName("tv_mch_price"));
        tvBanalcePTB = view.findViewById(idName("txt_mch_ptb_balance"));
        tvBanalceBindPTB = view.findViewById(idName("txt_mch_bb_balance"));
        imgMGate = view.findViewById(idName("mch_img_choose_mgate"));
        imgStripeCard = view.findViewById(idName("mch_img_choose_stipe_stripe_card"));
        imgStripeCard.setVisibility(View.VISIBLE);
        imgYLPD = view.findViewById(idName("mch_img_choose_ylpd"));
        imgBindingDollar = view.findViewById(idName("mch_img_choose_binding_dollar"));
        TextView btnPay = view.findViewById(idName("btn_mch_pay"));
        tvCouponPrice = view.findViewById(idName("tv_coupon_price"));
        tvCanUseCouponNum = view.findViewById(idName("tv_choose_coupon"));
        txtPayRemark = view.findViewById(idName("btn_mch_remark"));
        txtPayRemark.setText("");
//        txtPayTimer = view.findViewById(idName("txt_pay_timer"));

        btnMGate = view.findViewById(idName("btn_mgate_pay"));
        btnStripeCreditCard = view.findViewById(idName("btn_stripe_card_pay"));

        btnYLPD = view.findViewById(idName("btn_ylpd_pay"));
        btnBindingDollar = view.findViewById(idName("btn_binding_dollar_pay"));

        btnGetEmailValidateMessage = view.findViewById(idName("get_email_code"));
        btnGetPhoneValidateMessage = view.findViewById(idName("get_sms_code"));

//        String productPrice = String.format(Locale.CHINA, AppConstants.STR_556ca35421e760d27ae33f6fa46e2d86, Float.parseFloat(price));
//        tvWupinName.setText(String.format("%s%s", ApiCallback.order().getProductName(), productPrice));
        tvProductName.setText(ApiCallback.order().getProductName());

        listPrice = ApiCallback.order().getGoodsPriceYuan();

//        tvMchPrice.setText(String.format("%s USD", ApiCallback.order().get));
        tvRealPrice.setVisibility(View.INVISIBLE);
        tvBanalcePTB.setVisibility(View.GONE);
        tvBanalceBindPTB.setVisibility(View.GONE);
        tvCouponPrice.setVisibility(View.GONE);

        layoutMGate = view.findViewById(idName("layout_mgate"));
        layoutStripeCard = view.findViewById(idName("layout_stripe_card"));

        layoutYLPD = view.findViewById(idName("layout_ylpd"));
        layoutBindingDollar = view.findViewById(idName("layout_binding_dollar"));

        layoutMGate.setOnClickListener(mgateBtnClickListener);
        layoutStripeCard.setOnClickListener(stripeCreditCardBtnClickListener);
        layoutYLPD.setOnClickListener(ylpdBtnClickListener);
        layoutBindingDollar.setOnClickListener(bindingDollarBtnClickListener);

        btnPay.setOnClickListener(startPayListener);

        layoutVerificationCodeBox = view.findViewById(idName("mch_verification_code"));

        btnMGate.setOnClickListener(mgateBtnClickListener);
        btnStripeCreditCard.setOnClickListener(stripeCreditCardBtnClickListener);
        btnYLPD.setOnClickListener(ylpdBtnClickListener);
        btnBindingDollar.setOnClickListener(bindingDollarBtnClickListener);

        View btnMchBack = view.findViewById(idName("btn_mch_back"));
        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                sendPayResult("-1");//支付取消
            }
        });

        btnScanPay.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                if (Double.parseDouble(toPayPrice) <= 0){
                    ToastUtil.show(context, AppConstants.STR_4bb08afe6b109f0fe15d619ee2b02e6a);
                    return;
                }
                ApiCallback.setScanPayCallback(mScanPayCallback);
                ScanPayDialog scanPayDialog = new ScanPayDialog(context,
                        MCHInflaterUtils.getIdByName(context, "style", "mch_MyDialogStyle"),
                        typeEntity != null && typeEntity.isHaveZFB,
                        typeEntity != null && typeEntity.isHaveWX);

                scanPayDialog.setGoodsName(ApiCallback.order().getProductName());
//                scanPayDialog.setGoodsPrice(ApiCallback.order().getGoodsPriceYuan());
                scanPayDialog.setGoodsDesc(ApiCallback.order().getProductDesc());
                scanPayDialog.setExtend(ApiCallback.order().getExtendInfo());
                scanPayDialog.setPayType("1");
                scanPayDialog.setCouponId(couponID);
                scanPayDialog.setRoleName(ApiCallback.order().getRoleName());
                scanPayDialog.setRoleId(ApiCallback.order().getRoleId());
                scanPayDialog.setRoleLevel(ApiCallback.order().getRoleLevel());
                scanPayDialog.setServerName(ApiCallback.order().getServerName());
                scanPayDialog.setServerId(ApiCallback.order().getGameServerId());
                scanPayDialog.setGoodsReserve(ApiCallback.order().getGoodsReserve());
                scanPayDialog.setExtra_param(ApiCallback.order().getExtra_param());
                scanPayDialog.show();
            }
        });

        tvCanUseCouponNum.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                toSelectedCouponView();
            }
        });

        tvCouponPrice.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                toSelectedCouponView();
            }
        });
    }
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void launcherTimer() {
        timer = new CountDownTimer(30 * 60 * 1000, 1000) {
            //        timer = new CountDownTimer(30 * 1000, 1000) {//30秒
            @Override
            public void onTick(long millisUntilFinished) {
                int sTotal = (int) (millisUntilFinished / 1000);
//                MCLog.w(TAG, "sTotal:" + sTotal);
                int s = (int) (sTotal % 60);
                int m = sTotal / 60;
                String str = String.format("00:%s:%s", (m < 10) ? "0" + m : m, (s < 10) ? "0" + s : s);
                txtPayTimer.setText(str);
            }

            @Override
            public void onFinish() {
                sendPayResult("-1");
            }
        }.start();
    }

    //扫码支付结果回调
    private final ScanPayCallback mScanPayCallback = new ScanPayCallback() {
        @Override
        public void onResult(String code) {
            MCLog.e(TAG, "fun # scanPayCallback code = " + code);
            sendPayResult(code);
        }
    };

    private void toSelectedCouponView() {
        if (couponList != null) {
            Intent intent = new Intent(context, MCHCouponMyActivity.class);
            intent.putExtra("isbind", isBind);
            intent.putExtra("listData", (Serializable) couponList);
            if (selectPosition != -1) {
                intent.putExtra("select_position", selectPosition);
            }
            context.startActivityForResult(intent, Constant.CHOOSE_COUPON_CODE);
        }
    }

    private void cancelCouponSelection() {
        isChooseCoupon = false;
        couponID = "";
        tvCouponPrice.setVisibility(View.GONE);
        couponPrice = Double.parseDouble(listPrice);
        selectPosition = -1;
    }

    //选中的代金券信息
    public void setChooseCouponData(String coupon_id, String coupon_price, int pos){
        MCLog.e(TAG,"coupon_id:" + coupon_id + ", coupon price:" + coupon_price);
        if (couponID.equals(coupon_id)){ //取消选中
            cancelCouponSelection();
        }else {
            isChooseCoupon = true;
            couponID = coupon_id;
            tvCouponPrice.setVisibility(View.VISIBLE);
            tvCouponPrice.setText(String.format("Coupon- %s USD", coupon_price));
            BigDecimal bigDecimalPrice = new BigDecimal(listPrice);
            BigDecimal bigDecimalcCouponPrice = new BigDecimal(coupon_price);
            couponPrice = bigDecimalPrice.subtract(bigDecimalcCouponPrice).doubleValue(); //优惠后价格
            if (couponPrice < 0) {
                couponPrice = 0;
            }
            selectPosition = pos;
        }
        addCouponToOrderInfo();
        //查询折扣信息
//        UserDiscountProcess discountProcess = new UserDiscountProcess();
//        discountProcess.post(mHandler);
    }

    private void addCouponToOrderInfo() {
        if (isHaveDiscount) {
            toPayPrice = String.format(Locale.CHINA, "%.2f", (isChooseCoupon ? couponPrice : Float.parseFloat(listPrice)) * buyGoodsDiscount / 10);
            tvMchPrice.setText(String.format(AppConstants.STR_c0ce112f2e1514a0b71d009edcfa97d1, toPayPrice, buyGoodsDiscount));
        }else {
            toPayPrice = String.format(Locale.CHINA, "%.2f", isChooseCoupon ? couponPrice : Float.parseFloat(listPrice));
            tvMchPrice.setText(String.format("%s USD", toPayPrice));
        }
        tvRealPrice.setVisibility(View.VISIBLE);
        tvRealPrice.setText(toPayPrice);
    }

    private void initPaymentInfo() {
        buyGoodsDiscount = 10;

        UserDiscountProcess discountProcess = new UserDiscountProcess(); //请求折扣信息
        discountProcess.post(discountHandler);

        UserPtbBalanceProcess ptbBalanceProcess = new UserPtbBalanceProcess();//请求平台币、绑币余额信息
        ptbBalanceProcess.post(coinHandler);

        PayTypeProcess payTypeProcess = new PayTypeProcess();  //请求支付方式列表
        payTypeProcess.post(payTypeHandler);
    }

    public void initCoupon() {
        GetAvailableCouponProcess availableProcess = new GetAvailableCouponProcess(); //请求可用满减券
        availableProcess.setPrice(listPrice);
        availableProcess.setBind(isBind ? "1" : "0");
        availableProcess.post(couponHandler);
    }

    private final Handler getProductPriceHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == Constant.GET_PRODUCT_PRICE_SUCCESS) {
                JSONObject product = (JSONObject) msg.obj;
                try {
                    listPrice = new BigDecimal(product.get("unitPrice").toString()).divide(new BigDecimal(100)).toString();
                } catch (JSONException e) {
                }
                tvRealPrice.setText(listPrice);
                toPayPrice = listPrice;
                couponPrice = Double.parseDouble(listPrice);
                initCoupon();
            }else {
                ToastUtil.show(context, (String) msg.obj);
            }
            return false;
        }
    });

    private final Handler payTypeHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (Constant.GAME_PAY_TYPE_SUCCESS == msg.what) {
                typeEntity = (GamePayTypeEntity)msg.obj;
//                layoutMGate.setVisibility(typeEntity.supportYlp ? View.VISIBLE : View.GONE);
                layoutStripeCard.setVisibility(typeEntity.supportFiat ? View.VISIBLE : View.GONE);
                btnScanPay.setVisibility(typeEntity.isHaceScan ? View.VISIBLE : View.GONE);
                txtPayRemark.setText(typeEntity.zfbRemark);
            }else {
                ToastUtil.show(context, (String) msg.obj);
            }
            return false;
        }
    });

    private final Handler discountHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == Constant.GET_USER_DISCOUNT_SUCCESS) {
                UserDiscountEntity userDiscount = (UserDiscountEntity) msg.obj;
                if (0 != userDiscount.getDiscountType() && 10 != userDiscount.getDiscountNum()) {
                    isHaveDiscount = true;
                    buyGoodsDiscount = userDiscount.getDiscountNum();  //折扣
                    toPayPrice = String.format(Locale.CHINA, "%.2f", Float.parseFloat(listPrice) * buyGoodsDiscount / 10);
                    tvMchPrice.setText(String.format(AppConstants.STR_13169d5e4cec9f86b472f88343427271, toPayPrice, buyGoodsDiscount));
                } else {
                    toPayPrice = String.format(Locale.CHINA, "%.2f", Float.parseFloat(listPrice));
//                    if(payType == STRIPE_WECHAT) { // USD
//                        BigDecimal price = new BigDecimal(toPayPrice).multiply(new BigDecimal("0.8"));
//                        tvMchPrice.setText(String.format("%s USD", price));
//                        tvToken.setText(AppConstants.STR_8b52e2b69a4121334528b24b8f6f07f2);
//                        tvTokenPrice.setText(price.divide(tokenPrices.get("ylp"), 6, RoundingMode.HALF_UP) + " YLP");
//                    }
//                    else {
                        tvMchPrice.setText(String.format("%s USD", toPayPrice));
//                    }
                }
                tvRealPrice.setVisibility(View.VISIBLE);
                tvRealPrice.setText(toPayPrice);
            }else {
                ToastUtil.show(context, (String) msg.obj);
            }
            return false;
        }
    });

    private final Handler coinHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == Constant.PTB_BALANCE_SUCCESS) {
                UserPTBInfo userPtb = (UserPTBInfo) msg.obj;
                tvBanalcePTB.setVisibility(View.VISIBLE);
                tvBanalceBindPTB.setVisibility(View.VISIBLE);
                tvBanalcePTB.setText(String.format(Locale.CHINA, "%.2f", userPtb.getPtbMoney()));
                tvBanalceBindPTB.setText(String.format(Locale.CHINA, "%.2f", userPtb.getBindptbMoney()));
            }else {
                ToastUtil.show(context, (String) msg.obj);
            }
            return false;
        }
    });

    private final Handler couponHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == Constant.USABLE_COUPON_SUCCESS) {
                couponList = (List<CouponBean>) msg.obj;
                tvCanUseCouponNum.setText(String.format(Locale.CHINA, "%d available", couponList.size()));
            }
            return false;
        }
    });

    private final OnClickListener mgateBtnClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            isBind = false;
            if (payType == STRIPE_USD) {
                cancelCouponSelection();
            }
            payType = MGATE;
//            txtPayRemark.setText(typeEntity.ptbRemark);
            layoutVerificationCodeBox.setVisibility(View.GONE);
            addCouponToOrderInfo();
            showChooseIcon(payType);
        }
    };

    private final OnClickListener stripeCreditCardBtnClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            isBind = false;
            if (payType != STRIPE_USD) {
                cancelCouponSelection();
            }
            payType = STRIPE_USD;
            isBind = true;
            txtPayRemark.setText(typeEntity.bindRemark);
            layoutVerificationCodeBox.setVisibility(View.GONE);
            addCouponToOrderInfo();
            showChooseIcon(payType);
        }
    };

    private final OnClickListener ylpdBtnClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            isBind = false;
            if (payType != STRIPE_USD) {
                cancelCouponSelection();
            }
            payType = YLPD;
            isBind = true;
            txtPayRemark.setText(typeEntity.bindRemark);
            layoutVerificationCodeBox.setVisibility(View.GONE);
            addCouponToOrderInfo();
            showChooseIcon(payType);
        }
    };
    private final OnClickListener bindingDollarBtnClickListener = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            isBind = false;
            if (payType != STRIPE_USD) {
                cancelCouponSelection();
            }
            payType = BINDING_DOLLAR;
            isBind = true;
            txtPayRemark.setText(typeEntity.bindRemark);
            layoutVerificationCodeBox.setVisibility(View.GONE);
            addCouponToOrderInfo();
            showChooseIcon(payType);
        }
    };

    OnMultiClickListener priceAdjustListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            MCLog.i(TAG, "" + v.getId());
        }
    };

    private void showChooseIcon(PayMethod payType){
        imgMGate.setVisibility(View.GONE);
        imgStripeCard.setVisibility(View.GONE);
        imgYLPD.setVisibility(View.GONE);
        imgBindingDollar.setVisibility(View.GONE);
        switch (payType){
            case MGATE:
//                BigDecimal price = new BigDecimal(toPayPrice).multiply(new BigDecimal("0.8"));
//                tvMchPrice.setText(String.format("%s USD", price));
                tvRealPrice.setVisibility(View.VISIBLE);
                imgMGate.setVisibility(View.VISIBLE);
                break;
            case STRIPE_USD:
                tvMchPrice.setText(String.format("%s USD", toPayPrice));
                tvRealPrice.setVisibility(View.VISIBLE);
                imgStripeCard.setVisibility(View.VISIBLE);
                break;
            case YLPD:
//                BigDecimal price = new BigDecimal(toPayPrice).multiply(new BigDecimal("0.8"));
//                tvMchPrice.setText(String.format("%s USD", price));
                tvRealPrice.setVisibility(View.VISIBLE);
                imgYLPD.setVisibility(View.VISIBLE);
                break;
            case BINDING_DOLLAR:
                tvMchPrice.setText(String.format("%s USD", toPayPrice));
                tvRealPrice.setVisibility(View.VISIBLE);
                imgBindingDollar.setVisibility(View.VISIBLE);
                break;
        }

    }


    //开始发起支付
    private final OnClickListener startPayListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {

            String sessionuid = UserLoginSession.getInstance().getUserId();
            if (!TextUtils.isEmpty(sessionuid)) {
                startPay();
            } else {
                UserReLogin reLogin = new UserReLogin(context);
                reLogin.userToLogin(res -> {
                    MCLog.e(TAG, "reLogin res = " + res);
                    if (res) {
                        startPay();
                    } else {
                        show("Please login first");
                    }
                });
            }
        }
    };

    //This method will initiate a stripe payment
//    private void startStripePay(){
//        try {
//            stripePaymentRequest.createPaymentIntent(ApiCallback.order().getPayerWalletAddress(),
//                    ApiCallback.order().getProductId());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void startPay() {
        switch (payType){
            case OTT_UPAY:
                startOttUPay();
            case MGATE:
//                startOttUPay();
                startMGatePay();
                break;
            case STRIPE_USD:
                startStripePay(false);
                break;
            case YLPD:
                queryYLPDBalance();
                break;
            case BINDING_DOLLAR:
                showDialog("BINDING_DOLLAR");
                queryBindingDollarBalance();
                break;
        }
    }

    private void startOttUPay() {
        ApiCallback.setMGatePayCallback(ottUPayCallback);
        OttUPay ottUPay = new OttUPay(context, ottUpayPaymentRequest);
        ottUPay.doOttUPayPayment(couponID);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismisDialog();
            }
        },1000);
    }

    private void startMGatePay() {
        ApiCallback.setMGatePayCallback(mGatePayCallback);
        MGatePay mGatePay = new MGatePay(context, mGatePaymentRequest);
        mGatePay.doMGatePayment(couponID);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismisDialog();
            }
        },1000);
    }

    // TODO 支付宝-------------start
    private void startStripePay(boolean isWePay) {
        ApiCallback.setZFBWapPayCallback(stripePayCallback);
        StripePay stripePay = new StripePay(context, stripePaymentRequest);
        stripePay.stripePayProcess(couponID, isWePay);
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
    private final MGatePayCallback ottUPayCallback = code -> {
        MCLog.e(TAG, "fun # ottUPayCallback code = " + code);
        sendPayResult(code);
    };

    private final StripePayCallback stripePayCallback = code -> {
        MCLog.e(TAG, "fun # stripeCallback code = " + code);
        sendPayResult(code);
    };

    private final WFTWapPayCallback mWFTWapPayCallback = code -> {
        MCLog.e(TAG, "fun # wftPayCallback code = " + code);
        sendPayResult(code);
    };

    private void queryYLPDBalance() {
        showDialog(AppConstants.STR_0438dcdd316e41292a8f14afd5dafdca);
        new UserPtbRemainProcess().post(cpHandler, true);
    }

    private void queryBindingDollarBalance() {
        showDialog(AppConstants.STR_4382f8aa8b1d31428d0357740c73ea3a);
        new UserPtbRemainProcess().post(cpHandler,false);
    }


    private void processYLPDPayment(Object obj) {
        FlagControl.BUTTON_CLICKABLE = true;
        UserPTBInfo userPtb = (UserPTBInfo) obj;
        userPtbMoney = String.format(Locale.CHINA, "%.2f", userPtb.getPtbMoney());
        userBindPtbMoney = String.format(Locale.CHINA, "%.2f", userPtb.getBindptbMoney());

        float price = Float.parseFloat(toPayPrice);
        float ptb = MCMoneyUtils.priceToFloat(userPtbMoney);
        if ((ptb - price) >= 0) {
            ptb_pay("1");
        } else {
            show("Balance not enough");
        }
    }


    private void processBindingDollarPayment(Object obj) {
        FlagControl.BUTTON_CLICKABLE = true;
        UserPTBInfo userPtb = (UserPTBInfo) obj;
        userPtbMoney = String.format(Locale.CHINA, "%.2f", userPtb.getPtbMoney());
        userBindPtbMoney = String.format(Locale.CHINA, "%.2f", userPtb.getBindptbMoney());
        float price = Float.parseFloat(toPayPrice);
        float bindptbMoney = MCMoneyUtils.priceToFloat(userBindPtbMoney);
        MCLog.w(TAG, "fun#price:" + price + ", bindptbMoney:" + bindptbMoney);
        if ((bindptbMoney - price) >= 0) {
            ptb_pay("2");
        } else {
            show("Balance not enough");
        }
    }
    // TODO 平台币余额------------------end

    // TODO 平台币支付请求------------------start
    public final void ptb_pay(String payType) {  //1平台币 2绑币
        showDialog("Processing payment ...");
        PTBPayProcess ptbpayProcess = new PTBPayProcess();
        ptbpayProcess.setGoodsName(ApiCallback.order().getProductName());
        ptbpayProcess.setGoodsPrice(ApiCallback.order().getGoodsPriceYuan());
        ptbpayProcess.setGoodsDesc(ApiCallback.order().getProductDesc());
        ptbpayProcess.setExtend(ApiCallback.order().getExtendInfo());
        ptbpayProcess.setRoleName(ApiCallback.order().getRoleName());
        ptbpayProcess.setServerName(ApiCallback.order().getServerName());
        ptbpayProcess.setRoleId(ApiCallback.order().getRoleId());
        ptbpayProcess.setServerId(ApiCallback.order().getGameServerId());
        ptbpayProcess.setExtra_param(ApiCallback.order().getExtra_param());
        ptbpayProcess.setRoleLevel(ApiCallback.order().getRoleLevel());
        ptbpayProcess.setGoodsReserve(ApiCallback.order().getGoodsReserve());
        ptbpayProcess.setCouponId(couponID);
        ptbpayProcess.setCode(payType);
        ptbpayProcess.post(cpHandler, context);
    }

    protected void handlerPTBPayResult(Object obj) {
        PTBPayResult ptbPayResult = (PTBPayResult) obj;
        dismisDialog();
        if (null != ptbPayResult && ptbPayResult.getReturn_status().equals("1")) {
            new PTBPayResultDialog.Builder()
                    .setMoney(toPayPrice)
                    .setGoodsName(ApiCallback.order().getProductName())
                    .setTradeWay( isBind ? "FIAT" : "YLP")
                    .setTradeNo(ptbPayResult.getOrderNumber())
                    .setDialogKeyListener(backKeyListener).
                    setCloseClick(closeClick)
                    .show(context, context.getFragmentManager());
        } else {
            String tipStr = AppConstants.STR_4548cc1f2ccb9d405bfb9b630776c024;
            if (null != ptbPayResult && !TextUtils.isEmpty(ptbPayResult.getReturn_msg())) {
                tipStr = ptbPayResult.getReturn_msg();
            }
            show(tipStr);
            sendPayResult("-1");
        }
    }

    private final OnClickListener closeClick = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            sendPayResult("0");
        }
    };


    /**
     * 返回按钮监听
     */
    DialogInterface.OnKeyListener backKeyListener = new DialogInterface.OnKeyListener() {

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            sendPayResult("0");
            return false;
        }
    };

    /**
     * 发送支付结果
     */
    protected void sendPayResult(String payResultCode) {
        MCPayModel.Instance().getPaymentCallback().callback(payResultCode);
        context.finish();
    }

    public Handler cpHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismisDialog();
            switch (msg.what) {
                case Constant.WFT_ORDERINFO_SUCCESS:
                    WXOrderInfo wxOrderInfo = (WXOrderInfo) msg.obj;
                    if (ServiceManager.getInstance().isBaiDuYunOS) {
                        ServiceManager.getInstance().launcherPay(wxOrderInfo.getUrl());
                    }else {
                        boolean isWxAvaliable = DeviceInfo.isWeixinAvilible(context);
                        if (!isWxAvaliable) {
                            ToastUtil.show(context, AppConstants.STR_d0a5c71cc9f5eeafa594ba9d5924bf66);
                            FlagControl.BUTTON_CLICKABLE = true;
                            return;
                        }

                        Intent intent = new Intent(context, MCHWapPayActivity.class);
                        intent.putExtra("WapPayOrderInfo", wxOrderInfo);
                        context.startActivity(intent);
                    }
                    break;
                case Constant.WFT_ORDERINFO_FAIL:
                case Constant.ZFB_WAPPAY_ORDERINFO_FAIL:
                    show(AppConstants.STR_244e5dd1ea2819fccabf8a5527dd1aca + msg.obj);
                    FlagControl.BUTTON_CLICKABLE = true;
                    break;
                case Constant.ZFB_WAPPAY_ORDERINFO_SUCCESS:
                    WXOrderInfo zfbwapPayOrderInfo = (WXOrderInfo) msg.obj;
                    if (ServiceManager.getInstance().isBaiDuYunOS) {
                        ServiceManager.getInstance().launcherPay(zfbwapPayOrderInfo.getUrl());
                    }else {
                        Intent zfbWapPayintent = new Intent(context, MCHWapPayActivity.class);
                        zfbWapPayintent.putExtra("WapPayOrderInfo", zfbwapPayOrderInfo);
                        context.startActivity(zfbWapPayintent);
                    }
                    break;
                case Constant.YLPD_BALANCE_QUERY_SUCCESS:  //查询平台币余额信息
                    processYLPDPayment(msg.obj);
                    break;
                case Constant.YLPD_BALANCE_QUERY_FAIL:
                    show(AppConstants.STR_1aefd1316b135c3cc8b494107ee57975 + msg.obj);
                    FlagControl.BUTTON_CLICKABLE = true;
                    break;
                case Constant.BIND_PTB_MONEY_SUCCESS:  //查询绑币余额信息
                    processBindingDollarPayment(msg.obj);
                    break;
                case Constant.BIND_PTB_MONEY_FAIL:
                    show(AppConstants.STR_4a6ba4c814e46498ea3b882fa83daef0 + msg.obj);
                    FlagControl.BUTTON_CLICKABLE = true;
                    break;
                case Constant.YLPD_PAY_SUCCESS:// 平台币支付
                    handlerPTBPayResult(msg.obj);
                    break;
                case Constant.YLPD_PAY_FAIL:
                    show(AppConstants.STR_244e5dd1ea2819fccabf8a5527dd1aca+ msg.obj);
                    break;
                default:
                    break;

            }
        }
    };

    /**
     * 打开提示信息
     */
    private void showDialog(String message) {
        mcTipDialog = new MCTipDialog.Builder().setMessage(message).show(context, context.getFragmentManager());
    }

    /**
     * 关闭提示信息
     */
    private void dismisDialog() {
        if (null != mcTipDialog) {
            mcTipDialog.dismiss();
        }
    }

    private void show(String message) {
        ToastUtil.show(context, message);
        MCLog.e(TAG, message);
    }

    private int idName(String name) {
        return MCHInflaterUtils.getIdByName(context, "id", name);
    }

}
