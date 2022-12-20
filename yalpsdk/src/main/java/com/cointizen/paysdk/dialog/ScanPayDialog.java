package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.cointizen.open.ApiCallback;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.ScanRQWxBean;
import com.cointizen.paysdk.bean.ScanRQZfbBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.ScanPayResultProcess;
import com.cointizen.paysdk.http.process.ScanWxProcess;
import com.cointizen.paysdk.http.process.ScanZfbProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.BitmapHelp;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

/**
 * 描述：扫码支付弹窗
 * 时间: 2021-01-05 11:18
 */

public class ScanPayDialog extends Dialog {

    private ScanRQWxBean scanRQWxBean;
    private ScanRQZfbBean scanRQZfbBean;

    private Activity activity;
    private LayoutInflater inflater;
    private View view;
    private TextView tvZFB,tvWX;
    private ImageView imgRQCode,btnClose;
    private BitmapUtils bitmapUtils;
    private boolean isHaveZFB,isHaveWX;

    private String extra_param = "";
    private String goodsName = ""; //商品名称
    private String goodsPrice = "";  //商品价格
    private String goodsDesc = ""; //商品描述
    private String payType = "";  //充值类型 平台币 0 游戏 1
    private String extend = "";  //游戏订单信息
    private String serverName = "";  //区服名字
    private String serverId = "";  //区服ID
    private String roleName = "";  //角色名字
    private String roleId = "";  //角色Id
    private String roleLevel = ""; //角色等级
    private String couponId = ""; //代金券id
    private String goodsReserve = "";

    public ScanPayDialog(@NonNull Activity mActivity,int themeResId,boolean isHaveZFB,boolean isHaveWx) {
        super(mActivity, themeResId);
        activity = mActivity;
        this.isHaveZFB = isHaveZFB;
        this.isHaveWX = isHaveWx;
        setCancelable(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(activity);
        view = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_saoma"), null);
        setContentView(view);

        btnClose = findViewById(MCHInflaterUtils.getIdByName(activity, "id", "btn_close"));
        imgRQCode = findViewById(MCHInflaterUtils.getIdByName(activity, "id", "img_QR_code"));
        tvZFB = findViewById(MCHInflaterUtils.getIdByName(activity, "id", "tv_zfb"));
        tvWX = findViewById(MCHInflaterUtils.getIdByName(activity, "id", "tv_wx"));


        btnClose.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                wxHandler.removeCallbacks(wxTimeCounterRunnable);
                zfbHandler.removeCallbacks(zfbTimeCounterRunnable);
                dismiss();
                ApiCallback.mScanPayCallback.onResult("-1");
            }
        });

        tvZFB.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                setPayType(1);
            }
        });

        tvWX.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                setPayType(2);
            }
        });

        bitmapUtils = BitmapHelp.getBitmapUtils(activity.getApplicationContext());

        if (isHaveZFB && isHaveWX){
            tvZFB.setVisibility(View.VISIBLE);
            tvWX.setVisibility(View.VISIBLE);
            setPayType(1);
        }else if (isHaveZFB){
            tvZFB.setVisibility(View.VISIBLE);
            tvWX.setVisibility(View.GONE);
            setPayType(1);
        }else if (isHaveWX){
            tvZFB.setVisibility(View.GONE);
            tvWX.setVisibility(View.VISIBLE);
            setPayType(2);
        }
    }


    private void setPayType(int type){
        switch (type){
            case 1:  //选中的支付宝
                tvZFB.setTextColor(Color.parseColor("#21b1eb"));
                tvWX.setTextColor(Color.parseColor("#999999"));
                tvZFB.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvWX.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                if (scanRQZfbBean == null){
                    getZfbRqCode();
                }else {
                    // todo:
//                    bitmapUtils.display(imgRQCode,scanRQZfbBean.getQrcode_url());
                }
                break;
            case 2:  //选中的微信
                tvZFB.setTextColor(Color.parseColor("#999999"));
                tvWX.setTextColor(Color.parseColor("#21b1eb"));
                tvZFB.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWX.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                if (scanRQWxBean == null){
                    getWxRqCode();
                }else {
                    // todo:
//                    bitmapUtils.display(imgRQCode,scanRQWxBean.getQrcode_url());
                }
                break;
        }
    }

    private void getWxRqCode(){
//        showDialog(DialogConstants.S_kXRfKNojKC);
        ScanWxProcess process = new ScanWxProcess();
        process.setGoodsName(goodsName);
        process.setGoodsPrice(goodsPrice);
        process.setGoodsDesc(goodsDesc);
        process.setExtend(extend);
        process.setPayType(payType);
        process.setCouponId(couponId);
        process.setRoleName(roleName);
        process.setRoleId(roleId);
        process.setRoleLevel(roleLevel);
        process.setServerName(serverName);
        process.setServerId(serverId);
        process.setGoodsReserve(goodsReserve);
        process.setExtra_param(extra_param);
//        process.post(myHandler);// todo:
    }

    private void getZfbRqCode(){
//        showDialog(DialogConstants.S_gFYaURAWrs);
        ScanZfbProcess process = new ScanZfbProcess();
        process.setGoodsName(goodsName);
        process.setGoodsPrice(goodsPrice);
        process.setGoodsDesc(goodsDesc);
        process.setExtend(extend);
        process.setPayType(payType);
        process.setCouponId(couponId);
        process.setRoleName(roleName);
        process.setRoleId(roleId);
        process.setRoleLevel(roleLevel);
        process.setServerName(serverName);
        process.setServerId(serverId);
        process.setGoodsReserve(goodsReserve);
        process.setExtra_param(extra_param);
//        process.post(myHandler);// todo:
    }

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.SCAN_ZFB_SUCCESS:
                    scanRQZfbBean = (ScanRQZfbBean) msg.obj;
                    bitmapUtils.display(imgRQCode,scanRQZfbBean.getQrcode_url());
                    zfbTimeCounterRunnable.run(); //支付宝二维码获取成功，开始轮询订单支付结果
                    break;
                case Constant.SCAN_ZFB_FAIL:
                    ToastUtil.show(activity,DialogConstants.S_fkpYcffGKe+ msg.obj.toString());
                    break;
                case Constant.SCAN_WX_SUCCESS:
                    scanRQWxBean = (ScanRQWxBean) msg.obj;
                    bitmapUtils.display(imgRQCode,scanRQWxBean.getQrcode_url());
                    wxTimeCounterRunnable.run();
                    break;
                case Constant.SCAN_WX_FAIL:
                    ToastUtil.show(activity,DialogConstants.S_pdPukmvqIf+ msg.obj.toString());
                    break;
                case Constant.SCAN_PAY_RESULT_SUCCESS:
                    wxHandler.removeCallbacks(wxTimeCounterRunnable);
                    zfbHandler.removeCallbacks(zfbTimeCounterRunnable);
                    dismiss();
                    ApiCallback.mScanPayCallback.onResult("0");
                    MCLog.w(DialogConstants.Log_SJJerpwUMc,DialogConstants.Log_QhpxTgisNP);
                    break;
                case Constant.SCAN_PAY_RESULT_FAIL:
                    MCLog.w(DialogConstants.Log_SJJerpwUMc,DialogConstants.Log_lIFcGefhQf);
                    break;
            }
            dismisDialog();
        }
    };

    private Handler wxHandler = new Handler(Looper.getMainLooper()); //查询微信扫码结果Handler
    Runnable wxTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {
            getWxPayResult(scanRQWxBean.getOrderno());//执行的任务查询微信扫码支付结果
            wxHandler.postDelayed(this, 1500);
        }
    };

    private Handler zfbHandler = new Handler(Looper.getMainLooper()); //查询支付宝扫码结果Handler
    Runnable zfbTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {
            getZfbPayResult(scanRQZfbBean.getOrderno());//执行的任务查询支付宝扫码支付结果
            zfbHandler.postDelayed(this, 1500);
        }
    };


    //查询微信扫码支付结果
    private void getWxPayResult(String orderID){
        ScanPayResultProcess payResultProcess = new ScanPayResultProcess();
        payResultProcess.setOut_trade_no(orderID);
//        payResultProcess.post(myHandler); todo:
    }

    //查询支付宝扫码支付结果
    private void getZfbPayResult(String orderID){
        ScanPayResultProcess payResultProcess = new ScanPayResultProcess();
        payResultProcess.setOut_trade_no(orderID);
//        payResultProcess.post(myHandler); todo:
    }


    public void setExtra_param(String extra_param) {
        this.extra_param = extra_param;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public void setGoodsReserve(String goodsReserve) {
        this.goodsReserve = goodsReserve;
    }

    //信息提示框
    MCTipDialog mcTipDialog;
    private void showDialog(String message) {
        mcTipDialog = new MCTipDialog.Builder().setMessage(message).show(activity, activity.getFragmentManager());
    }

    private void dismisDialog() {
        if (null != mcTipDialog) {
            mcTipDialog.dismiss();
        }
    }
}
