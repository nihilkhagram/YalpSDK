package com.cointizen.paysdk.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.callback.EditAccountCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.EditAccountDialog;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.process.EditSmallNickNameProgress;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：小号管理Holder
 * 时间: 2018-07-16 14:43
 */
public class MCAccountManagementHolder extends BaseHolder<UserLogin.SmallAccountEntity> {
    private UserLogin.SmallAccountEntity data;
    private View view;
    private LayoutInflater mInflater;
    private Activity activity;
    private TextView tvAccount;
    private ImageView btnEdit;
    private RelativeLayout rlLayout;
    private TextView tvHint;
    private EditAccountDialog.editAccountBuilder dialogBuilder;
    private MCTipDialog mcTipDialog;

    public MCAccountManagementHolder(Activity activity) {
        super(activity);
        this.activity = activity;
    }


    @Override
    protected void refreshView(final UserLogin.SmallAccountEntity smallAccountEntity, int position, final Activity activity) {
        this.data = smallAccountEntity;

        if (UserLoginSession.getInstance().getSmallAccountUserID().equals(smallAccountEntity.getSmallUserId())){
            rlLayout.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"mch_account_item_bg"));
            btnEdit.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"mch_trumpet_btn_blue"));
            tvAccount.setTextColor(Color.parseColor("#ffffff"));
            tvHint.setVisibility(View.VISIBLE);
        }else {
            rlLayout.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"mch_btn_hui_bg"));
            btnEdit.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"mch_trumpet_btn_write"));
            tvAccount.setTextColor(Color.parseColor("#333333"));
            tvHint.setVisibility(View.INVISIBLE);
        }

        tvAccount.setText(smallAccountEntity.getSmallNickname());
        btnEdit.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                dialogBuilder = new EditAccountDialog.editAccountBuilder().setSureClick(callback);
                dialogBuilder.show(activity,activity.getFragmentManager());
            }
        });
    }

    /**
     * 修改小号昵称
     */
    private String nickname = "";
    EditAccountCallback callback = new EditAccountCallback() {
        @Override
        public void addAccount(String nickName) {
            dialogBuilder.closeDialog(activity.getFragmentManager());
            mcTipDialog = new MCTipDialog.Builder().setMessage(HolderConstants.S_wEADQsGZMY).show(activity,activity.getFragmentManager());

            nickname = nickName;
            EditSmallNickNameProgress progress = new EditSmallNickNameProgress();
            progress.setSmall_id(data.getSmallUserId());
            progress.setSmallNickName(nickName);
            progress.post(mHandler);

        }
    };


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != mcTipDialog) {
                mcTipDialog.dismiss();
            }
            switch (msg.what) {
                case Constant.SMALNICKNAME_SUCCESS:  //修改小号昵称成功
                    tvAccount.setText(nickname);
                    dialogBuilder.closeDialog(activity.getFragmentManager());
                    ToastUtil.show(activity, HolderConstants.S_vXnHpKKXYp);
                    break;

                case Constant.SMALNICKNAME_FAIL:  //修改小号昵称失败
                    String tips = (String) msg.obj;
                    ToastUtil.show(activity, tips);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(MCHInflaterUtils.getLayout(context, "mch_item_managment_account"), null);
        tvAccount = view.findViewById(MCHInflaterUtils.getControl(context, "tv_mch_small_account"));
        btnEdit = view.findViewById(MCHInflaterUtils.getControl(context, "btn_mch_name_edit"));
        rlLayout = view.findViewById(MCHInflaterUtils.getControl(context, "btn_play"));
        tvHint = view.findViewById(MCHInflaterUtils.getControl(context, "tv_mch_hint"));
        view.setTag(this);
        return view;
    }

}
