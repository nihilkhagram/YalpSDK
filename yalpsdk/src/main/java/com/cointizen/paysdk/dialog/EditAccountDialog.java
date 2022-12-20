package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.callback.EditAccountCallback;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TextUtils;

/**
 * 描述：编辑小号昵称
 * 时间: 2018-07-14 10:05
 */
public class EditAccountDialog extends DialogFragment {
    private static final String TAG = "AddAccountDialog";
    private Context activity;
    private EditAccountCallback mSureListener;

    public EditAccountDialog() {
    }

    @SuppressLint("ValidFragment")
    public EditAccountDialog(Context mActivity) {
        this.activity = mActivity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_smallaccount_edit"), container, false);
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 0.5f;((Activity)activity).getWindow().setAttributes(params);

        final EditText edtNickName = (EditText) containerView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "edt_nick"));
        TextView btnCancel = (TextView) containerView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "btn_cancle"));
        TextView btnSure = (TextView) containerView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "btn_ok"));


        setEditTextInhibitInputSpace(edtNickName);///禁止输入空格


        btnCancel.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        btnSure.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (TextUtils.isEmpty(edtNickName.getText().toString().trim())){
                    ToastUtil.show(activity,DialogConstants.S_HxnikMygeC);
                    return;
                }

                if (null != mSureListener) {
                    mSureListener.addAccount(edtNickName.getText().toString().trim());
                }
            }
        });

        setCancelable(false);
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissAllowingStateLoss();
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
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(activity, "style", "mch_MCCustomDialog"));
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
            window.getAttributes().width = (int) (windowSize.y * 0.8);
            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {// 竖屏
            size_x = 0.9f;
            size_y = 0.855f;
            window.getAttributes().width = (int) (windowSize.x * 0.8);
            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public void setmSureClick(EditAccountCallback mSureListener) {
        this.mSureListener = mSureListener;
    }


    public static class editAccountBuilder {
        /**
         * 存放数据的容器
         **/
        private Bundle mBundle;
        private EditAccountCallback mmSureListener;
        private EditAccountDialog dialog;

        public editAccountBuilder() {
            mBundle = new Bundle();
        }

        public editAccountBuilder setSureClick(EditAccountCallback mmSureListener) {
            this.mmSureListener = mmSureListener;
            return this;
        }

        private EditAccountDialog create(Activity activity) {
            dialog = new EditAccountDialog(activity);
            // 1,设置显示内容
            dialog.setArguments(mBundle);
            dialog.setmSureClick(mmSureListener);
            return dialog;
        }

        public EditAccountDialog show(Activity activity, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            EditAccountDialog dialog = create(activity);
            MCLog.d(TAG, "show AddAccountDialog.");

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }

        //外部调用关闭此Dialog
        public EditAccountDialog closeDialog(FragmentManager fm){
            if (dialog!=null){
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(dialog);
                ft.commitAllowingStateLoss();
            }
            return dialog;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity)activity).getWindow().setAttributes(params);
    }



    /**
     * 禁止EditText输入空格  限制10位长度
     * @param editText
     */
    private void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" ")){
                    return "";
                }else{
                    return null;
                }

            }

        };
        editText.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(20)});
    }


}
