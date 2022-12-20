package com.cointizen.paysdk.utils;

import android.content.Context;
import android.text.*;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cointizen.paysdk.listener.OnMultiClickListener;

/**
 * 描述：
 * 作者：Administrator
 * 时间: 2018-03-02 10:08
 */

public class MCHEtUtils {
    private static final String TAG = "MCHEtUtils";
    private static MCHEtUtils instance;
    private int viewClearId = -1, viewVisibleId = -1;
    private EditText editText;
    private boolean isClose = true;
    private Context context;
    private ImageView ivEye;


    public MCHEtUtils() {
    }

//    public static MCHEtUtils getInstance() {
//        if (instance == null) {
//            instance = new MCHEtUtils();
//        }
//        return instance;
//    }

    /**
     * @param mContext
     * @param editText
     * @param viewClear
     * @param viewVisible
     * @param imageView
     */
    public void etHandle(Context mContext, final EditText editText, final View viewClear, View viewVisible, ImageView imageView) {
        if (mContext == null) {
            MCLog.e(TAG, "Context is null");
            return;
        }
        context = mContext;
        if (editText == null) {
            MCLog.e(TAG, "EditText is null");
            return;
        }
        this.editText = editText;
        if (imageView != null) {
            ivEye = imageView;
        }
        if (viewClear != null) {
            viewClear.setOnClickListener(onClickListener);
            viewClearId = viewClear.getId();
        }
        if (viewVisible != null) {
            viewVisible.setOnClickListener(onClickListener);
            viewVisibleId = viewVisible.getId();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (viewClear != null) {
                    if (!android.text.TextUtils.isEmpty(editText.getText().toString()) && editText.getText().toString().length()>0) {
                        viewClear.setVisibility(View.VISIBLE);
                    } else {
                        viewClear.setVisibility(View.GONE);
                    }
                }
            }
        });


    }

    View.OnClickListener onClickListener = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {

            int id = v.getId();
            if (id == viewClearId) {
                editText.setText("");
            }
            if (id == viewVisibleId) {
                if (isClose) {
                    //普通文本
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivEye.setImageResource(MCHInflaterUtils.getDrawable(context, "mch_eye_open"));
                    // 使光标始终在最后位置
                    Selection.setSelection(editText.getText(), editText.length());
                    isClose = false;
                } else {
                    //显示为密码
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivEye.setImageResource(MCHInflaterUtils.getDrawable(context, "mch_eye_close"));
                    // 使光标始终在最后位置
                    Selection.setSelection(editText.getText(), editText.length());
                    isClose = true;
                }
            }

        }
    };
}
