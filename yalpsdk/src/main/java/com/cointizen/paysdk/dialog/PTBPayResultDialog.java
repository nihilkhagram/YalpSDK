package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.content.DialogInterface.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

/**
 * 描述：
 * 作者：Administrator
 * 时间: 2018-03-05 9:07
 */

public class PTBPayResultDialog extends DialogFragment {

    private static final String TAG = "PTBPayResultDialog";
    /**
     * 上下文
     */
    private Context context;

    private OnClickListener mCloseListener;

    /**
     * 返回键监听
     */
    private OnKeyListener mDialogKeyListener;

    protected static final String KEY_MONEY = "mch_ptb_money";
    protected static final String KEY_GOODSNAME = "mch_ptb_goodsname";
    protected static final String KEY_TRADE_WAY = "mch_ptb_trade_way";
    protected static final String KEY_TRADE_NO = "mch_ptb_trade_no";

    public PTBPayResultDialog() {
    }

    @SuppressLint("ValidFragment")
    public PTBPayResultDialog(Context con) {
        this.context = con;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(context,
                "layout", "mch_dialog_ptb_pay_result"), container, false);
        // 如果有信息,显示加载信息
        Bundle bundle = getArguments();
        String money = "";
        String goods_name = "";
        String trade_way = "";
        String trade_no = "";
        if (bundle != null) {
            money = bundle.getString(KEY_MONEY, "");
            goods_name = bundle.getString(KEY_GOODSNAME, "");
            trade_way = bundle.getString(KEY_TRADE_WAY, "");
            trade_no = bundle.getString(KEY_TRADE_NO, "");
        }

        TextView tvMoney1 = (TextView) containerView.findViewById(MCHInflaterUtils
                .getIdByName(context, "id", "tv_mch_ptb_pay_result"));
        TextView tvMoney2 = (TextView) containerView.findViewById(MCHInflaterUtils
                .getIdByName(context, "id", "tv_mch_ptb_pay_result2"));
        TextView tvGoodsName = (TextView) containerView.findViewById(MCHInflaterUtils
                .getIdByName(context, "id", "tv_mch_ptb_pay_goodsname"));
        TextView tvTradeWay = (TextView) containerView.findViewById(MCHInflaterUtils
                .getIdByName(context, "id", "tv_mch_ptb_pay_way"));
        TextView tvTradeNo = (TextView) containerView.findViewById(MCHInflaterUtils
                .getIdByName(context, "id", "tv_mch_ptb_pay_order_no"));
        TextView tvPayType = (TextView) containerView.findViewById(MCHInflaterUtils
                .getIdByName(context, "id", "tv_pay_type"));

        tvMoney1.setText(money);
        tvMoney2.setText(money);
        tvGoodsName.setText(goods_name);
        tvTradeWay.setText(trade_way);
        tvTradeNo.setText(trade_no);
        tvPayType.setText(trade_way);


        Button btnSure = (Button) containerView.findViewById(MCHInflaterUtils
                .getIdByName(context, "id", "btn_mch_ptb_pay_sure"));

        btnSure.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (null != mCloseListener) {
                    dismissAllowingStateLoss();
                    mCloseListener.onClick(v);
                }
            }
        });


        setCancelable(false);

        this.getDialog().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissAllowingStateLoss();
                    if (null != mDialogKeyListener) {
                        mDialogKeyListener.onKey(dialog, keyCode, event);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        return containerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(context, "style", "mch_MCSelectPTBTypeDialog"));
    }

    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        // 1, 设置对话框的大小
        Window window = getDialog().getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        float size_x = 0;
        float size_y = 0;
        int width = windowSize.x;
        int height = windowSize.y;
        if (width >= height) {// 横屏
            size_x = 0.7f;
            size_y = 0.8f;
            window.getAttributes().width = (int) (windowSize.y * size_y * 1.1);
            window.getAttributes().height = (int) (windowSize.y * size_y);
        } else {// 竖屏
            size_x = 0.9f;
            size_y = 0.855f;
            window.getAttributes().width = (int) (windowSize.x * size_x);
            window.getAttributes().height = (int) (windowSize.x * size_y);
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public void setmDialogKeyListener(OnKeyListener mDialogKeyListener) {
        this.mDialogKeyListener = mDialogKeyListener;
    }

    public void setmCloseClick(OnClickListener mCloseListener) {
        this.mCloseListener = mCloseListener;
    }

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mmBundle;
        private OnKeyListener mmDialogKeyListener;

        private OnClickListener mmCloseListener;


        public Builder() {
            mmBundle = new Bundle();
        }

        public Builder setMoney(CharSequence account) {
            mmBundle.putCharSequence(KEY_MONEY, account);
            return this;
        }

        public Builder setGoodsName(CharSequence account) {
            mmBundle.putCharSequence(KEY_GOODSNAME, account);
            return this;
        }

        public Builder setTradeWay(CharSequence account) {
            mmBundle.putCharSequence(KEY_TRADE_WAY, account);
            return this;
        }

        public Builder setTradeNo(CharSequence account) {
            mmBundle.putCharSequence(KEY_TRADE_NO, account);
            return this;
        }

        public Builder setDialogKeyListener(OnKeyListener mmDialogKeyListener) {
            this.mmDialogKeyListener = mmDialogKeyListener;
            return this;
        }

        public Builder setCloseClick(OnClickListener mmCloseListener) {
            this.mmCloseListener = mmCloseListener;
            return this;
        }

        private PTBPayResultDialog create(Context context) {
            final PTBPayResultDialog dialog = new PTBPayResultDialog(context);
            // 1,设置显示内容
            dialog.setArguments(mmBundle);
            dialog.setmDialogKeyListener(mmDialogKeyListener);
            dialog.setmCloseClick(mmCloseListener);
            return dialog;
        }

        public PTBPayResultDialog show(Context context, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            PTBPayResultDialog dialog = create(context);
            MCLog.d(TAG, "show PTBPayResultDialog.");
//			dialog.show(fm, TAG);

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }


    }
}
