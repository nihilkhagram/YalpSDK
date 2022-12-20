package com.cointizen.paysdk.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lidroid.xutils.BitmapUtils;
import com.cointizen.paysdk.bean.SwitchManager;
import com.cointizen.paysdk.bean.UserInfoBean;
import com.cointizen.paysdk.bean.login.MCHAccountPopuWindow;
import com.cointizen.paysdk.bean.privacy.PrivacyManager;
import com.cointizen.paysdk.callback.PlatformLoginCallback;
import com.cointizen.paysdk.callback.PopWindowClearCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHEtUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.PreSharedManager;
import com.cointizen.paysdk.utils.WebViewUtil;
import com.cointizen.paysdk.view.SpinerPopWindow;

import java.util.LinkedList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PlatformLoginDialog extends DialogFragment implements
        View.OnFocusChangeListener, TextWatcher, View.OnLongClickListener {
    /**
     * 日志打印
     */
    private static final String TAG = "PlatformLoginDialog";

    /**
     * 用户名
     **/
    protected static final String KEY_ACCOUNT = "mc_account";

    /**
     * 密码
     **/
    protected static final String KEY_PASSWORD = "mc_password";

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 返回键监听
     */
    private OnKeyListener mDialogKeyListener;

    /**
     * 登录
     */
    private PlatformLoginCallback mLoginCallback;

    /**
     * 忘记密码
     */
    private OnClickListener mForgmentPwdClick;

    /**
     * 注册
     */
    private OnClickListener mRegisterClick;

    private OnClickListener mNormalRegisterClick;
    /**
     * 游客登录
     */
    private OnClickListener mYKLoginClick;

    private OnClickListener mLoginCancelClick;

    private OnClickListener mThirdLoginClick;
    private OnClickListener ll_qucklogin_click;

    private SpinerPopWindow mSpinerPopWindow;
    private LinkedList<UserInfoBean> list;
    LinearLayout ll_wblogin;
    LinearLayout ll_qqlogin;
    LinearLayout ll_wxlogin;
    LinearLayout ll_bdlogin;


    /**
     * 失去焦点事件
     */
    private View.OnFocusChangeListener mFocusChangeListener;

    /**
     * 用户名输入框
     */
    EditText edtAccount;

    RelativeLayout rlAccountClear;
    RelativeLayout rlPwdClear;
    RelativeLayout rlPwdEye;
    ImageView ivEye;

    private boolean flag = true;

    /**
     * 密码输入框
     */
    EditText edtPassword;
    private View ll_qucklogin;

    public PlatformLoginDialog() { }

    @SuppressLint("ValidFragment")
    public PlatformLoginDialog(Context con) {
        mContext = con;
    }

    private ImageView logoImg;
    private ImageView ivSwitchAccount;
    private BitmapUtils bitmapUtils;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(mContext,
                "layout", "mch_dialog_platform_login"), container, false);
        // 如果有信息,显示加载信息
        Bundle bundle = getArguments();
        String account = "";
        String pwd = "";


        if (bundle != null) {
            account = bundle.getString(KEY_ACCOUNT, "");
            pwd = bundle.getString(KEY_PASSWORD, "");
        }

        //加载logo
        logoImg = (ImageView) containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContext, "id", "mch_iv_log"));
        bitmapUtils = new BitmapUtils(mContext);

        edtAccount = (EditText) containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContext, "id", "edt_mc_platform_login_account"));
        if (!TextUtils.isEmpty(account)) {
            edtAccount.setText(account);
            edtAccount.setSelection(account.length());//将光标移至文字末尾
        }

        ivSwitchAccount = (ImageView) containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContext, "id", "iv_mch_login_switch_account"));
        RelativeLayout rlSwithAccount = (RelativeLayout) containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContext, "id", "rl_mch_login_switch_account"));
        rlSwithAccount.setOnClickListener(selectAccount);

        edtPassword = (EditText) containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContext, "id", "edt_mc_platform_login_password"));
        if (!TextUtils.isEmpty(pwd)) {
            edtPassword.setText(pwd);
        }
        rlAccountClear = (RelativeLayout) (RelativeLayout) containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id",
                        "rl_mch_login_acc_clear"));

        rlPwdClear = (RelativeLayout) (RelativeLayout) containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id",
                        "rl_mch_login_pwd_clear"));
        View btnTozhuce = containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id",
                        "btn_mch_to_register"));
        btnTozhuce.setVisibility(SwitchManager.getInstance().isCloseRegister() ? View.GONE : View.VISIBLE);
        btnTozhuce.setOnClickListener(new OnMultiClickListener() {

            @Override
            public void onMultiClick(View v) {
                dismissAllowingStateLoss();
                if(SwitchManager.getInstance().isOpenPhoneRegister()) {
                    if (null != mRegisterClick) {
                        mRegisterClick.onClick(v);
                    }
                    return;
                }
                if(SwitchManager.getInstance().isOpenAccountRegister() || SwitchManager.getInstance().isOpenEmailRegister()) {
                    if (null != mNormalRegisterClick) {
                        mNormalRegisterClick.onClick(v);
                    }
                }
            }
        });

        rlPwdEye = (RelativeLayout) containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id",
                        "rl_mch_login_eye"));

        ivEye = (ImageView) containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id",
                        "iv_mch_login_eye"));


        new MCHEtUtils().etHandle(mContext, edtAccount, rlAccountClear, null, null);
        new MCHEtUtils().etHandle(mContext, edtPassword, rlPwdClear, rlPwdEye, ivEye);

        TextView btnRegister = (TextView) containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id", "tv_mch_login_to_register"));
        String str = btnRegister.getText().toString().trim();
        btnRegister.setText(str);
        LinearLayout btnQuickRegister = (LinearLayout) containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id", "btn_mc_platform_toquickregister"));

        btnQuickRegister.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(final View v) {
                if (null != mYKLoginClick) {
                    mYKLoginClick.onClick(v);
                }
            }
        });

        containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContext, "id", "mch_tv_login_close")).setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (null != mLoginCancelClick) {
                    mLoginCancelClick.onClick(v);
                }
            }
        });
        containerView.findViewById(MCHInflaterUtils
                .getIdByName(mContext, "id", "mch_tv_login_close")).setVisibility(View.GONE);

        TextView btnforgmentPwd = (TextView) containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id",
                        "tv_mch_platform_forgment_pwd"));

        String strbtnforgmentPwd = btnforgmentPwd.getText().toString().trim();

        btnforgmentPwd.setText(strbtnforgmentPwd);
        btnforgmentPwd.setOnClickListener(new OnMultiClickListener() {

            @Override
            public void onMultiClick(View v) {
                if (null != mForgmentPwdClick) {
                    mForgmentPwdClick.onClick(v);
                }
            }
        });

        Button btnLogin = (Button) (Button) containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id",
                        "btn_mc_platform_login"));
        btnLogin.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (null != mLoginCallback) {
                    mLoginCallback.platformLogin(edtAccount.getText()
                            .toString().trim(), edtPassword.getText()
                            .toString().trim(), true);
                }
            }
        });

		/*第三方登录按钮*/
        final int wbid = MCHInflaterUtils.getIdByName(mContext, "id", "ll_wblogin");
        final int qqid = MCHInflaterUtils.getIdByName(mContext, "id", "ll_qqlogin");
        final int wxid = MCHInflaterUtils.getIdByName(mContext, "id", "ll_wxlogin");
        final int bdid = MCHInflaterUtils.getIdByName(mContext, "id", "ll_bdlogin");
        OnClickListener thirdloginListener = new OnMultiClickListener() {
            @Override
            public void onMultiClick(final View v) {
                mThirdLoginClick.onClick(v);
            }
        };

        ll_qucklogin = containerView
                .findViewById(MCHInflaterUtils.getIdByName(mContext, "id",
                        "ll_qucklogin"));

        LinearLayout ll_btv_wblogin = (LinearLayout) containerView.findViewById(wbid);
        ll_btv_wblogin.setTag("wb");
        LinearLayout ll_btv_qqlogin = (LinearLayout) containerView.findViewById(qqid);
        ll_btv_qqlogin.setTag("qq");
        LinearLayout ll_btv_wxlogin = (LinearLayout) containerView.findViewById(wxid);
        ll_btv_wxlogin.setTag("wx");
        LinearLayout ll_btv_bdlogin = (LinearLayout) containerView.findViewById(bdid);
        ll_btv_bdlogin.setTag("bd");
        ll_btv_wblogin.setOnClickListener(thirdloginListener);
        ll_btv_qqlogin.setOnClickListener(thirdloginListener);
        ll_btv_wxlogin.setOnClickListener(thirdloginListener);
        ll_btv_bdlogin.setOnClickListener(thirdloginListener);

        ll_wblogin = (LinearLayout) containerView.findViewById(
                MCHInflaterUtils.getIdByName(mContext, "id", "ll_mch_wblogin"));
        ll_wblogin.setVisibility(View.GONE);
        ll_qqlogin = (LinearLayout) containerView.findViewById(
                MCHInflaterUtils.getIdByName(mContext, "id", "ll_mch_qqlogin"));
        ll_qqlogin.setVisibility(View.GONE);
        ll_wxlogin = (LinearLayout) containerView.findViewById(
                MCHInflaterUtils.getIdByName(mContext, "id", "ll_mch_wxlogin"));
        ll_wxlogin.setVisibility(View.GONE);
        ll_bdlogin = (LinearLayout) containerView.findViewById(
                MCHInflaterUtils.getIdByName(mContext, "id", "ll_mch_bdlogin"));
        ll_bdlogin.setVisibility(View.GONE);

        TextView txtAgree = containerView.findViewById(
                MCHInflaterUtils.getControl(mContext, "txt_btn_agreement"));
//        txtAgree.setText(String.format(DialogConstants.S_jDptaiiNKv, PrivacyManager.getInstance().userAgreementTitle()));
        PrivacyManager.getInstance().setUserAgreement(txtAgree);
        txtAgree.setOnClickListener(agreeClick);
        TextView txtPrivacy = containerView.findViewById(
                MCHInflaterUtils.getControl(mContext, "txt_btn_protocal"));
//        txtPrivacy.setText(String.format(DialogConstants.S_jDptaiiNKv, PrivacyManager.getInstance().privacyPolicyTitle()));
        PrivacyManager.getInstance().setPrivacyPolicy(txtPrivacy);
        txtPrivacy.setOnClickListener(privacyClick);

        setCancelable(false);
        return containerView;
    }

    public void setmDialogKeyListener(OnKeyListener mDialogKeyListener) {
        this.mDialogKeyListener = mDialogKeyListener;
    }

    public void setmLoginCallback(PlatformLoginCallback mLoginCallback) {
        this.mLoginCallback = mLoginCallback;
    }

    public void setmForgmentPwdClick(OnClickListener mForgmentPwdClick) {
        this.mForgmentPwdClick = mForgmentPwdClick;
    }

    public void setmRegisterClick(OnClickListener mRegisterClick) {
        this.mRegisterClick = mRegisterClick;
    }

    public void setNormalRegisterClick(OnClickListener normalRegisterClick) {
        this.mNormalRegisterClick = normalRegisterClick;
    }

    public void setYKLoginClick(OnClickListener mQuickRegisterClick) {
        this.mYKLoginClick = mQuickRegisterClick;
    }

    public void setmLoginCancelClick(OnClickListener mLoginCancelClick) {
        this.mLoginCancelClick = mLoginCancelClick;
    }

    public void setThirdLoginClick(OnClickListener thirdLoginClick) {
        this.mThirdLoginClick = thirdLoginClick;
    }

    private final OnMultiClickListener agreeClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            WebViewUtil.webView((Activity) mContext, PrivacyManager.getInstance().agreementUrl(), true);
        }
    };

    private final OnMultiClickListener privacyClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            WebViewUtil.webView((Activity) mContext, PrivacyManager.getInstance().privacyPolicyUrl(), true);
        }
    };

    private final OnMultiClickListener selectAccount = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            list = PreSharedManager.getUserInfoList(mContext);
            // 使光标始终在最后位置
            Selection.setSelection(edtAccount.getText(), edtAccount.length());
            if (flag) {
                flag = false;
                ivSwitchAccount.setImageResource(MCHInflaterUtils.getDrawable(mContext, "mch_account_up"));
                if (null != list && list.size() != 0) {
                    mSpinerPopWindow = new SpinerPopWindow(mContext, list, itemClickListener, itemClearListener);
                    mSpinerPopWindow.setBackgroundDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(mContext,"mch_circle_5dp_pop_gray")));
                    mSpinerPopWindow.setOnDismissListener(dismissListener);
                    mSpinerPopWindow.setWidth(edtAccount.getWidth());

                    mSpinerPopWindow.showAsDropDown(edtAccount,0,10);
                }
            } else {
                flag = true;
                ivSwitchAccount.setImageResource(MCHInflaterUtils.getDrawable(mContext, "mch_account_down"));
            }
        }
    };

    //判断是否开起第三方登录
    public void setThirdLoginButtonShow(boolean iswb, boolean isqq,
                                        boolean iswx, boolean isbd, boolean isyk) {
        ll_wblogin.setVisibility(iswb ? View.VISIBLE : View.GONE);
        ll_qqlogin.setVisibility(isqq ? View.VISIBLE : View.GONE);
        ll_wxlogin.setVisibility(iswx ? View.VISIBLE : View.GONE);
        ll_bdlogin.setVisibility(isbd ? View.VISIBLE : View.GONE);
        if(isyk){
            ll_qucklogin.setVisibility(View.VISIBLE);
        }

        //加载logo
        MCLog.e(TAG, Constant.SDK_LOGO_URL);
        if(!com.cointizen.paysdk.utils.TextUtils.isEmpty(Constant.SDK_LOGO_URL)){
            logoImg.setVisibility(View.VISIBLE);
            bitmapUtils.display(logoImg, Constant.SDK_LOGO_URL );
        }else {
            logoImg.setVisibility(View.GONE);
        }
    }

    public void setLl_qucklogin_click(OnClickListener ll_qucklogin_click) {
        this.ll_qucklogin_click = ll_qucklogin_click;
    }

    public void setmOnfocusChangeLinser(View.OnFocusChangeListener mFocusChangeListener) {
        this.mFocusChangeListener = mFocusChangeListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(mContext, "style", "mch_MCHTransparent"));
    }

    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        // 1, 设置对话框的大小
        Window window = getDialog().getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        int width = windowSize.x;
        int height = windowSize.y;
        if (width >= height) {// 横屏
            window.getAttributes().width = (int) (windowSize.y * 0.85);
            window.getAttributes().height = (int) (windowSize.y * 0.88);
        } else {// 竖屏
            window.getAttributes().width = (int) (windowSize.x * 0.786);
            window.getAttributes().height = (int) (windowSize.x * 0.8138);
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mmBundle;

        private OnKeyListener mmDialogKeyListener;
        private PlatformLoginCallback mmLoginCallback;
        private OnClickListener mmForgmentPwdClick;
        private OnClickListener mmRegisterClick;
        private OnClickListener mmNormalRegisterClick;
        private OnClickListener mmYKLoginClick;
        private OnClickListener mmLoginCancelClick;
        private OnClickListener mmThirdLoginClick;
        //		private OnClickListener mmll_wblogin_click;
//		private OnClickListener mmll_qqlogin_click;
//		private OnClickListener mmll_wxlogin_click;
//		private OnClickListener mmll_bdlogin_click;
        private OnClickListener mmll_qucklogin_click;

        public Builder() {
            mmBundle = new Bundle();
        }

        public Builder setAccount(CharSequence account) {
            mmBundle.putCharSequence(KEY_ACCOUNT, account);
            return this;
        }

        public Builder setPassword(CharSequence pwd) {
            mmBundle.putCharSequence(KEY_PASSWORD, pwd);
            return this;
        }

        public Builder setDialogKeyListener(OnKeyListener mmDialogKeyListener) {
            this.mmDialogKeyListener = mmDialogKeyListener;
            return this;
        }

        public Builder setLoginCallback(PlatformLoginCallback mmLoginCallback) {
            this.mmLoginCallback = mmLoginCallback;
            return this;
        }

        public Builder setRegisterClick(OnClickListener mmRegisterClick) {
            this.mmRegisterClick = mmRegisterClick;
            return this;
        }

        public Builder setNormalRegisterClick(OnClickListener normalRegisterClick) {
            this.mmNormalRegisterClick = normalRegisterClick;
            return this;
        }

        public Builder setYKLoginClick(
                OnClickListener mmQuickRegisterClick) {
            this.mmYKLoginClick = mmQuickRegisterClick;
            return this;
        }

        public Builder setLoginCancelClick(
                OnClickListener mmLoginCancelClick) {
            this.mmLoginCancelClick = mmLoginCancelClick;
            return this;
        }


        public Builder setMmll_qucklogin_click(OnClickListener mmll_qucklogin_click) {
            this.mmll_qucklogin_click = mmll_qucklogin_click;
            return this;
        }

        public Builder setThirdLoginClick(OnClickListener thirdLoginClick) {
            this.mmThirdLoginClick = thirdLoginClick;
            return this;
        }

//		public Builder setMmll_wblogin_click(OnClickListener mmll_wblogin_click) {
//			this.mmll_wblogin_click = mmll_wblogin_click;
//			return this;
//		}
//
//		public Builder setMmll_qqlogin_click(OnClickListener mmll_qqlogin_click) {
//			this.mmll_qqlogin_click = mmll_qqlogin_click;
//			return this;
//		}
//
//		public Builder setMmll_wxlogin_click(OnClickListener mmll_wxlogin_click) {
//			this.mmll_wxlogin_click = mmll_wxlogin_click;
//			return this;
//		}
//
//		public Builder setMmll_bdlogin_click(OnClickListener mmll_bdlogin_click) {
//			this.mmll_bdlogin_click = mmll_bdlogin_click;
//			return this;
//		}

        public Builder setForgmentPwdClick(OnClickListener mmForgmentPwdClick) {
            this.mmForgmentPwdClick = mmForgmentPwdClick;
            return this;
        }

        private PlatformLoginDialog create(Context context) {
            final PlatformLoginDialog dialog = new PlatformLoginDialog(context);
            // 1,设置显示内容
            dialog.setArguments(mmBundle);

            dialog.setmDialogKeyListener(mmDialogKeyListener);
            dialog.setmLoginCallback(mmLoginCallback);
            dialog.setmForgmentPwdClick(mmForgmentPwdClick);
            dialog.setmRegisterClick(mmRegisterClick);
            dialog.setNormalRegisterClick(mmRegisterClick);
            dialog.setYKLoginClick(mmYKLoginClick);
            dialog.setmLoginCancelClick(mmLoginCancelClick);
            dialog.setThirdLoginClick(mmThirdLoginClick);
//			dialog.setLl_wblogin_click(mmll_wblogin_click);
//			dialog.setLl_bdlogin_click(mmll_bdlogin_click);
//			dialog.setLl_qqlogin_click(mmll_qqlogin_click);
//			dialog.setLl_wxlogin_click(mmll_wxlogin_click);
            dialog.setLl_qucklogin_click(mmll_qucklogin_click);
            return dialog;
        }

        public PlatformLoginDialog show(Context context, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            PlatformLoginDialog dialog = create(context);
            MCLog.d(TAG, "show PlatformLoginDialog.");
//			dialog.show(fm, TAG);

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }
    }

    //	@Override
//	public void show(FragmentManager manager, String tag) {
//		FragmentTransaction ft = manager.beginTransaction();
//		ft.add(this, tag);
//        ft.commitAllowingStateLoss();
//	}
    @Override
    public void onFocusChange(View v, boolean hasFocus) {//焦点改变
        EditText dd = (EditText) v;
        int edtAccount = MCHInflaterUtils.getIdByName(mContext, "id",
                "edt_mc_platform_login_account");
        if (v.getId() == edtAccount && !hasFocus) {// 手机号失去焦点
            if (null != mFocusChangeListener) {
                mFocusChangeListener.onFocusChange(v, hasFocus);
            }
            return;
        }
    }

    @Override
    public boolean onLongClick(View view) {//长按事件
        System.out.print(DialogConstants.S_GIlSmgZTnZ);
        showAccountList(((EditText) view).getText().toString().trim());

        return false;
    }

    @Override//文字改变响应事件
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//		showAccountList(charSequence.toString());
    }

    @Override//文字改变之前
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override//文字改变之后
    public void afterTextChanged(Editable editable) {
    }

    private void showAccountList(String tmp) {
        MCHAccountPopuWindow aa = new MCHAccountPopuWindow();
        aa.act = (Activity) mContext;
        aa.etNumber = edtAccount;
        aa.etPassword = edtPassword;
        aa.showSelectNumberDialog();
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            if (list != null && list.size() != 0) {
//                tvSwitchAccount.setText(list.get(position).getAccount() + "");
                edtAccount.setText(list.get(position).getAccount());
                edtAccount.setSelection(list.get(position).getAccount().length());//将光标移至文字末尾
                edtPassword.setText(list.get(position).getPwd());
            }
        }
    };

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            flag = true;
            ivSwitchAccount.setImageResource(MCHInflaterUtils.getDrawable(mContext, "mch_account_down"));
        }
    };

    private PopWindowClearCallback itemClearListener = new PopWindowClearCallback() {
        @Override
        public void reslut(final int position) {
            DialogUtil.mch_alert_msg(mContext, DialogConstants.S_rvlSenplVy, DialogConstants.S_iFPIpYYJXT, mContext, DialogConstants.S_eWBfpNUUQu, DialogConstants.S_LsXupyLcLb, new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    if (list != null && list.size() != 0) {
                        if (edtAccount.getText().toString().trim().equals(list.get(position).getAccount())) {
                            list.remove(position);
                            if (list.size() > 0) {
                                edtAccount.setText(list.get(0).getAccount());
                                edtAccount.setSelection(list.get(0).getAccount().length());//将光标移至文字末尾
                                edtPassword.setText(list.get(0).getPwd());
                            } else {
                                edtAccount.setText("");
                                edtPassword.setText("");
                            }
                        }
                        mSpinerPopWindow.dismiss();
                        PreSharedManager.removeAndSaveUserInfoList(mContext, position);
                    }
                }
            }).show();

        }
    };

}
