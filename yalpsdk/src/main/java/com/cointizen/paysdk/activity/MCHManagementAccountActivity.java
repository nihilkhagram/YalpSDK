package com.cointizen.paysdk.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHAccountManagmentAdapter;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.callback.AddAccountCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.AddAccountDialog;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.http.process.AddSmallAccountProgress;
import com.cointizen.paysdk.http.process.SmallAccountListProgress;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.TextUtils;

import java.util.List;


/**
 * 小号管理Activity
 */
public class MCHManagementAccountActivity extends MCHBaseActivity {
    private RelativeLayout btnBack;
    private ListView smaleAccountListView;
    private TextView btnAddAccount;  //添加小号按钮
    private AddAccountDialog.addAccountBuilder dialogBuilder;
    private List<UserLogin.SmallAccountEntity> smallAccountList;
    private MCHAccountManagmentAdapter adapter;
    private MCTipDialog mcTipDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setContentView(getLayout("mch_act_account_management"));
        initView();
    }

    private void initView(){
        btnAddAccount = (TextView) findViewById(MCHInflaterUtils.getControl(this, "btn_tv_add_account"));
        smaleAccountListView = (ListView) findViewById(MCHInflaterUtils.getControl(this, "list_account"));
        btnBack = findViewById(MCHInflaterUtils.getControl(this, "btn_mch_back"));

        SmallAccountListProgress progress = new SmallAccountListProgress();
        progress.post(mHandler);

        adapter = new MCHAccountManagmentAdapter(this);
        smaleAccountListView.setAdapter(adapter);

        //添加小号按钮点击事件
        btnAddAccount.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (smallAccountList.size()>=10){
                    ToastUtil.show(MCHManagementAccountActivity.this,ActivityConstants.S_FYpCEizXYW);
                }else {
                    dialogBuilder = new AddAccountDialog.addAccountBuilder().setSureClick(clickcallback);
                    dialogBuilder.show(MCHManagementAccountActivity.this, getFragmentManager());
                }
            }
        });

        //关闭界面
        btnBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });

    }

    /**
     * 添加小号弹窗确认按钮点击事件
     */
    AddAccountCallback clickcallback = new AddAccountCallback() {
        @Override
        public void addAccount(String Account) {
            if (TextUtils.isEmpty(Account)){
                ToastUtil.show(MCHManagementAccountActivity.this,ActivityConstants.S_qswpYNiBAN);
                return;
            }

            dialogBuilder.closeDialog(getFragmentManager());
            mcTipDialog = new MCTipDialog.Builder().setMessage(ActivityConstants.S_mIUkgkwFNv).show(MCHManagementAccountActivity.this,getFragmentManager());
            AddSmallAccountProgress addSmallAccountProgress = new AddSmallAccountProgress();
            addSmallAccountProgress.setSmaleNickname(Account.trim());
            addSmallAccountProgress.setUserId(UserLoginSession.getInstance().getUserId());
            addSmallAccountProgress.setGameId(SdkDomain.getInstance().getGameId());
            addSmallAccountProgress.post(mHandler);
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
                case Constant.SMALLACCOUNT_LIST_SUCCESS: //获取小号列表数据成功
                    smallAccountList = (List<UserLogin.SmallAccountEntity>) msg.obj;
                    adapter.setListData(smallAccountList);
                    break;
                case Constant.SMALLACCOUNT_LIST_FAIL:
                    ToastUtil.show(MCHManagementAccountActivity.this, msg.obj.toString());
                    break;
                case Constant.ADD_SMALL_ACCOUNT_SUCCESS:  //添加小号成功
                    UserLogin login = (UserLogin)msg.obj;
                    smallAccountList = login.getSmallAccountList();
                    adapter.setListData(smallAccountList);
                    ToastUtil.show(MCHManagementAccountActivity.this, ActivityConstants.S_cmxvWPfuhq);
                    break;
                case Constant.ADD_SMALL_ACCOUNT_FAIL:  //添加小号失败
                    String tip = (String) msg.obj;
                    ToastUtil.show(MCHManagementAccountActivity.this, tip);
                    break;
                default:
                    break;
            }
        }
    };
}
