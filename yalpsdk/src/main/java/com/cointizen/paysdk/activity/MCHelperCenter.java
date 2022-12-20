package com.cointizen.paysdk.activity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHHelperListviewAdapter;
import com.cointizen.paysdk.bean.HelperBean;
import com.cointizen.paysdk.bean.HelperListBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.process.HelperListProcess;
import com.cointizen.paysdk.http.process.HelperProcess;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.WebViewUtil;

/**
 * 描述：帮助中心
 * 时间: 2018-09-10 17:44
 */

public class MCHelperCenter extends MCHBaseActivity {
    public String string = ActivityConstants.S_OfgRIqRtab +
            ActivityConstants.S_JCXMvxAzOT +
            ActivityConstants.S_LuCSfcpniC;
    private TextView tvHelper;
//    private MCHelperGridviewAdapter gdPayAdapter;
//    private MCHelperGridviewAdapter gdPassAdapter;
//    private MCHelperGridviewAdapter gdGiftAdapter;
//    private MCHelperGridviewAdapter gdCommomAdapter;
//    private MCHelperGridviewAdapter gdAccountAdapter;
    private ListView listview;
    private View btnKefu;
//    private View btnJiaQun;
//    private View btnPhone;
    private View btnEmail;
    private View back;
    private MCHHelperListviewAdapter MCHHelperListviewAdapter;

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.HELPER_SUCCESS:
                    bean = (HelperBean) msg.obj;
                    if ("0".equals(bean.serviceSwitch)) {
                        mchTvHelperKefu.setText(String.format(ActivityConstants.S_TgElEFqwta, bean.getGameCommunity()));
                    }else {
                        mchTvHelperKefu.setText(String.format("%s", bean.getGameCommunity()));
                    }
//                    mchTvHelperQun.setText(String.format(ActivityConstants.S_aJIVZCQMmy, bean.getAPP_QQ_GROUP()));
//                    mchTvHelperPhone.setText(String.format(ActivityConstants.S_IOGReoKCvH, bean.getAPP_TEL()));
                    mchTvHelperEmail.setText(String.format(ActivityConstants.S_ScQFcvkhED, bean.getAPP_EMAIL()));
                    break;
                case Constant.HELPER_FAIL:
                    break;
                case Constant.HELPERLIST_SUCCESS:
                    HelperListBean listBean = (HelperListBean) msg.obj;
                    MCHHelperListviewAdapter.setData(listBean.getData());
                    AppUtils.getTotalHeightofListView(listview);
                    break;
                case Constant.HELPERLIST_FAIL:
                    break;
            }
        }
    };
//    private GridView mchGridviewChongzhi;
//    private GridView mchGridviewPass;
//    private GridView mchGridviewGift;
//    private GridView mchGridviewCommon;
//    private GridView mchGridviewAccount;

//    private void setData(HelperListBean listBean) {
//        MCHelperGridviewAdapter mcHelperGridviewAdapter1 = new MCHelperGridviewAdapter();
//        MCHelperGridviewAdapter mcHelperGridviewAdapter2 = new MCHelperGridviewAdapter();
//        MCHelperGridviewAdapter mcHelperGridviewAdapter3 = new MCHelperGridviewAdapter();
//        MCHelperGridviewAdapter mcHelperGridviewAdapter4 = new MCHelperGridviewAdapter();
//        MCHelperGridviewAdapter mcHelperGridviewAdapter5 = new MCHelperGridviewAdapter();
//        mchGridviewChongzhi.setAdapter(mcHelperGridviewAdapter1);
//        mchGridviewPass.setAdapter(mcHelperGridviewAdapter2);
//        mchGridviewGift.setAdapter(mcHelperGridviewAdapter3);
//        mchGridviewCommon.setAdapter(mcHelperGridviewAdapter4);
//        mchGridviewAccount.setAdapter(mcHelperGridviewAdapter5);
//    }

    private TextView mchTvHelperKefu;
    private TextView mchTvHelperQun;
    private TextView mchTvHelperPhone;
    private TextView mchTvHelperEmail;
    private HelperBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Window window = getWindow();
//        window.requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //透明状态栏
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        super.onCreate(savedInstanceState);

        setContentView(MCHInflaterUtils.getLayout(this, "mch_activity_helper"));
        back = findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_btn_helper_back"));
        btnKefu = findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_btn_helper_kefu"));
//        btnJiaQun = findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_btn_helper_qun"));
//        btnPhone = findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_btn_helper_phone"));
        btnEmail = findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_btn_helper_email"));
        tvHelper = (TextView) findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_tv_helper"));
//        mchGridviewChongzhi = findViewById(getId("mch_gridview_chongzhi"));
//        mchGridviewPass = findViewById(getId("mch_gridview_pass"));
//        mchGridviewGift = findViewById(getId("mch_gridview_gift"));
//        mchGridviewCommon = findViewById(getId("mch_gridview_common"));
//        mchGridviewAccount = findViewById(getId("mch_gridview_account"));
        mchTvHelperKefu = (TextView) findViewById(getId("mch_tv_helper_kefu"));
        mchTvHelperQun = (TextView) findViewById(getId("mch_tv_helper_qun"));
        mchTvHelperPhone = (TextView) findViewById(getId("mch_tv_helper_phone"));
        mchTvHelperEmail = (TextView) findViewById(getId("mch_tv_helper_email"));
        tvHelper.setText(string);
        listview = (ListView) findViewById(MCHInflaterUtils.getIdByName(this, "id", "lv_mch_helper"));
        initView();
        initData();
    }

    private void initData() {
        HelperProcess helperProcess = new HelperProcess();
        helperProcess.post(handler);
        HelperListProcess helperListProcess = new HelperListProcess();
        helperListProcess.post(handler);
    }

    private void initView() {
        BtnHelperCilck btnHelperCilck = new BtnHelperCilck();
        back.setOnClickListener(btnHelperCilck);
        btnKefu.setOnClickListener(btnHelperCilck);
//        btnJiaQun.setOnClickListener(btnHelperCilck);
//        btnPhone.setOnClickListener(btnHelperCilck);
        btnEmail.setOnClickListener(btnHelperCilck);
        MCHHelperListviewAdapter = new MCHHelperListviewAdapter(this);
        listview.setAdapter(MCHHelperListviewAdapter);
    }

    class BtnHelperCilck implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId() == MCHInflaterUtils.getIdByName(MCHelperCenter.this, "id", "mch_btn_helper_back")){
                finish();
            }else if(view.getId() == MCHInflaterUtils.getIdByName(MCHelperCenter.this, "id", "mch_btn_helper_kefu")){
                //联系客服
                if(bean != null){
                    if ("0".equals(bean.serviceSwitch)) {
                        copyToClipboard(bean.getGameCommunity());
                    }else {
                        WebViewUtil.webView(MCHelperCenter.this, bean.getServiceH5URL());
                    }
                }
            }else if(view.getId() == MCHInflaterUtils.getIdByName(MCHelperCenter.this, "id", "mch_btn_helper_email")){
                //复制邮箱
                if(bean != null){
                    copyToClipboard(bean.getAPP_EMAIL());
                }
            }
        }
    }

    /**
     * 复制邮箱到粘贴板
     * @param text
     */
    private void copyToClipboard(String text) {
        ClipboardManager systemService = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        systemService.setPrimaryClip(ClipData.newPlainText("text", text));
        ToastUtil.show(this,ActivityConstants.S_KCrFEBSGBA);
    }

    /**
     * 联系客服QQ
     * @param s
     */
    private void OpenQQ(String s) {
        if (checkApkExist(this, "com.tencent.mobileqq")){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+s+"&version=1")));
        }else{
            ToastUtil.show(this,ActivityConstants.S_UnPbUkjpWi);
        }
    }

    /**
     * 一键加群
     * @param key
     * @return
     */
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
