package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cointizen.paysdk.R;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;


/**
 *
 * 自定义dialog
 * 15080222
 */
@SuppressLint("ValidFragment")
public class MCTipDialog extends DialogFragment
{
    /** fragment的名字. */
    protected static final String TAG = "ChatDialog";
    /** 上下文 */
    private Context con;

    /** 对话框标题 **/
    protected static final String KEY_TITLE  = "c_title";
    /** 对话框内容 **/
    protected static final String KEY_MESSAGE  = "c_message";
    /** 对话框【左边】按钮文字 **/
    protected static final String KEY_LEFT_BTN_TEXT  = "c_left_btn_text";
    /** 对话框【右边】按钮文字 **/
    protected static final String KEY_RIGHT_BTN_TEXT  = "c_right_btn_text";
    /** 是否能够取消  **/
    protected static final String KEY_CANCELABLE = "cancelable";

    private DialogInterface.OnCancelListener mOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnShowListener mOnShowListener;
    public static boolean isShowLoading = true;


    /**
     * 创建CustomDialog, 使用{@link Builder}
     */

    public MCTipDialog(Context con) {
        this.con = con;
    }



    /**
     * 创建CustomDialog, 使用{@link Builder}
     */
    public MCTipDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(con, "layout",
        										"mch_dialog_tip_custom"), container, false);
        // 如果有信息,显示加载信息
        Bundle bundle = getArguments();
        if(bundle == null)
        {
            // 没有信息直接返回
            return containerView;
        }

        CharSequence message = bundle.getCharSequence(KEY_MESSAGE);
        TextView contentView = (TextView) containerView.findViewById(MCHInflaterUtils.getIdByName(con, "id",
				"message"));
        if(TextUtils.isEmpty(message))
        {
            contentView.setVisibility(View.GONE);
        }
        else
        {
            contentView.setVisibility(View.VISIBLE);
            contentView.setText(message);
        }

        if (!isShowLoading){
            containerView.findViewById(R.id.progressBar1).setVisibility(View.GONE);
        }

        // 5,是否可以取消, 默认可以取消
        setCancelable(bundle.getBoolean(KEY_CANCELABLE, true));

        return containerView;
    }

    public void setIsShowLoading(boolean isShowLoading){
        this.isShowLoading = isShowLoading;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener mOnCancelListener)
    {
        this.mOnCancelListener = mOnCancelListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener mOnDismissListener)
    {
        this.mOnDismissListener = mOnDismissListener;
    }

    public void setOnShowListener(DialogInterface.OnShowListener mOnShowListener)
    {
        this.mOnShowListener = mOnShowListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(con, "style",
				"mch_MCTipDialog"));
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        if(mOnCancelListener != null) {
            mOnCancelListener.onCancel(dialog);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
        if(mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    @SuppressLint("NewApi")
	@Override
    public void onStart()
    {
        // 在onStart()里面调用,保证dialog已经完成初始化.

        // 1, 设置对话框的大小
        Window window = getDialog().getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        // 设置宽度为屏幕宽度88%
//        window.getAttributes().width = (int) (windowSize.x * 0.5f);
        window.getAttributes().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);

        super.onStart();

        // 3, 设置对话的事件
        getDialog().setOnShowListener(mOnShowListener);
    }


    public static class Builder {

        /** 存放数据的容器  **/
        private Bundle mmBundle;

        private DialogInterface.OnCancelListener mmOnCancelListener;
        private DialogInterface.OnDismissListener mmOnDismissListener;
        private DialogInterface.OnShowListener mmOnShowListener;



        /**
         * Constructor using a context for this builder and the {@link MCTipDialog} it creates.
         */
        public Builder() {
            mmBundle = new Bundle();
        }

        public Builder(boolean loading) {
            mmBundle = new Bundle();
            isShowLoading = loading;
        }

        /**
         * Set the message to display.<BR>
         *
         * 如果想隐藏内容信息, 这个方法不调用即可
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(CharSequence message) {
            mmBundle.putCharSequence(KEY_MESSAGE, message);
            return this;
        }



        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            mmBundle.putBoolean(KEY_CANCELABLE, cancelable);
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         *
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(DialogInterface.OnDismissListener) setOnDismissListener}.</p>
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(DialogInterface.OnDismissListener)
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            mmOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            mmOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is showed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            mmOnShowListener = onShowListener;
            return this;
        }


        /**
         * Creates a {@link MCTipDialog} with the arguments supplied to this builder.
         */
        private MCTipDialog create(Context con) {
            final MCTipDialog dialog = new MCTipDialog(con);
            // 1,设置显示内容
            dialog.setArguments(mmBundle);

            dialog.setOnCancelListener(mmOnCancelListener);
            dialog.setOnDismissListener(mmOnDismissListener);
            dialog.setOnShowListener(mmOnShowListener);

            return dialog;
        }

        private MCTipDialog create(Context con,boolean isShowLoading) {
            final MCTipDialog dialog = new MCTipDialog(con);
            // 1,设置显示内容
            dialog.setArguments(mmBundle);

            dialog.setOnCancelListener(mmOnCancelListener);
            dialog.setOnDismissListener(mmOnDismissListener);
            dialog.setOnShowListener(mmOnShowListener);

            return dialog;
        }

        /**
         * Creates a {@link MCTipDialog} with the arguments supplied to this builder and
         * {@link DialogFragment#show(FragmentManager, String)}'s the dialog.
         */
        public MCTipDialog show(Context con, FragmentManager fm) {
            if(fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }
            MCTipDialog dialog = create(con);
            MCLog.d(TAG, "show custom dialog.");
//            dialog.show(fm, TAG);
            
            FragmentTransaction ft = fm.beginTransaction();
			ft.add(dialog, TAG);
	        ft.commitAllowingStateLoss();
            return dialog;
        }
    }
}
