package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.bean.LoginModel;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.process.SmallAccountLoginProgress;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.utils.TextUtils;

/**
 * 描述：
 * 时间: 2018-07-16 14:43
 */
public class MCSmallAccountHolder extends BaseHolder<UserLogin.SmallAccountEntity> {
    private UserLogin.SmallAccountEntity data;
    private View view;
    private TextView tvAccount;
    private RelativeLayout btnPlay;
    private LayoutInflater mInflater;
    private UserLogin userInfo;  //平台账号登录信息
    private int size;
    private MCTipDialog mcTipDialog;
    private Activity activity;
    private TextView tvLastLogin;

    public MCSmallAccountHolder(Activity activity) {
        super(activity);
        this.activity = activity;
    }


    public void setSize(int listSize){
        this.size =  listSize;
    }

    public void setActivity(Activity mActivity){
        this.activity =  mActivity;
    }

    public void setUserInfo(UserLogin userInfo){
        this.userInfo = userInfo;
    }


    @Override
    protected void refreshView(final UserLogin.SmallAccountEntity smallAccountEntity, int position, Activity activity) {
        this.data = smallAccountEntity;
        if (!TextUtils.isEmpty(SharedPreferencesUtils.getInstance().getLastLoginID(activity)) &&
                SharedPreferencesUtils.getInstance().getLastLoginID(activity).equals(smallAccountEntity.getSmallUserId())){
            tvLastLogin.setVisibility(View.VISIBLE);
        }else {
            tvLastLogin.setVisibility(View.GONE);
        }

        if (smallAccountEntity.getSmallNickname().length()>16){
            tvAccount.setText(smallAccountEntity.getSmallNickname().substring(0,16) + "...");
        }else {
            tvAccount.setText(smallAccountEntity.getSmallNickname().trim());
        }
        btnPlay.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                btnPlay.setEnabled(false);
                smallAccountLogin(smallAccountEntity.getSmallUserId());
            }
        });

    }


    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(MCHInflaterUtils.getLayout(context, "mch_item_small_account"), null);
        tvAccount = view.findViewById(MCHInflaterUtils.getControl(context, "tv_mch_small_account"));
        btnPlay = view.findViewById(MCHInflaterUtils.getControl(context, "btn_play"));
        tvLastLogin = view.findViewById(MCHInflaterUtils.getControl(context, "tv_last_login"));
        view.setTag(this);
        return view;
    }


    /**
     * 小号登录
     * @param smallID
     */
    private void smallAccountLogin(String smallID){
        SmallAccountLoginProgress progress = new SmallAccountLoginProgress();
        progress.setSmallUserId(smallID);
        progress.setUserId(userInfo.getAccountUserId());
        progress.setGameId(SdkDomain.getInstance().getGameId());
        progress.setYKLogin(userInfo.isYKLogin());
        progress.setToken(userInfo.getToken());
        progress.post(mHandler);
        mcTipDialog = new MCTipDialog.Builder().setMessage(HolderConstants.S_mjzeuXRobg).show(activity,activity.getFragmentManager());
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != mcTipDialog) {
                mcTipDialog.dismiss();
            }
            switch (msg.what) {
                case Constant.SMALL_ACCOUNT_LOGIN_SUCCESS:  //小号登录成功
                    UserLogin smallAccountLogin = (UserLogin) msg.obj;
                    LoginModel.instance().smallAccountLoginSuccess(smallAccountLogin);

                    SharedPreferencesUtils.getInstance().setLastLoginID(activity,data.getSmallUserId());
                    activity.finish();
                    break;
                case Constant.SMALL_ACCOUNT_LOGIN_FAIL:  //小号登录失败
                    String tip = (String) msg.obj;
                    ToastUtil.show(activity, tip);
                    LoginModel.instance().loginFail();
                    btnPlay.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };



}
