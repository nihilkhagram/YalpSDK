package com.cointizen.plugin.qg.utils;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.utils.ScreenshotUtils;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHVersionListviewAdapter;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.bartools.ImmersionBar;
import com.cointizen.plugin.AppConstants;

/**
 * 描述：版本更新
 * 时间: 2018-10-10 14:51
 */

public class UpgradeVersionDialog extends Activity {

    private static final String TAG = "UpgradeVersionDialog";
    private ListView listTv;
    public int gao = 270;          //设置listview  高度
    private UpdateBean upDateBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();
        setContentView(MCHInflaterUtils.getLayout(this,"mch_dialog_version"));
        View close = findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_tv_close"));
        View BtnXiaci = findViewById(MCHInflaterUtils.getIdByName(this, "id", "btn_xiaci"));
        View BtnGengxin = findViewById(MCHInflaterUtils.getIdByName(this, "id", "btn_gengxin"));
        TextView tvVersion = findViewById(MCHInflaterUtils.getIdByName(this, "id", "tv_mch_version"));
        TextView tvSize = findViewById(MCHInflaterUtils.getIdByName(this, "id", "tv_mch_size"));
        listTv = (ListView)findViewById(MCHInflaterUtils.getIdByName(this, "id", "list_tv"));

        upDateBean = (UpdateBean) getIntent().getSerializableExtra("UpdateBean");
        tvVersion.setText(AppConstants.STR_e94e3fe31975adabd4baae99dc0826b1+ upDateBean.getVersion_name()+")");
        tvSize.setText(AppConstants.STR_3a90cce8b831c0a23a3da42c87aac16e+ upDateBean.getAnd_file_size());

        MCHVersionListviewAdapter mcVersionListviewAdapter = new MCHVersionListviewAdapter(upDateBean.getAnd_remark(),this);
        listTv.setAdapter(mcVersionListviewAdapter);
        BtnClik btnClik = new BtnClik();
        close.setOnClickListener(btnClik);
        BtnXiaci.setOnClickListener(btnClik);
        BtnGengxin.setOnClickListener(btnClik);
        getTotalHeightofListView(listTv);
    }

    class BtnClik implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId()==MCHInflaterUtils.getIdByName(UpgradeVersionDialog.this, "id", "mch_tv_close")){
//                YalpGamesSdk.getMCApi().setVersionDialogIsShow(false);
//                YalpGamesSdk.getMCApi().VersionOverLogin();
                if(upDateBean.getIs_force_update()==1){
                    finish();
                    Activity initContext = (YalpGamesSdk.getMainActivity());
                    initContext.finish();
                    System.exit(0);
                }else{
                    finish();
                    ScreenshotUtils.getInstance().init(YalpGamesSdk.getMainActivity());             //初始化注册  游客登录监听
                }
            }else if(view.getId()==MCHInflaterUtils.getIdByName(UpgradeVersionDialog.this, "id", "btn_xiaci")){
                if(upDateBean.getIs_force_update()==1){
                    finish();
                    Activity initContext = (YalpGamesSdk.getMainActivity());
                    initContext.finish();
                    System.exit(0);
                }else{
                    finish();
                    ScreenshotUtils.getInstance().init(YalpGamesSdk.getMainActivity());             //初始化注册  游客登录监听
                }
            }else if(view.getId()==MCHInflaterUtils.getIdByName(UpgradeVersionDialog.this, "id", "btn_gengxin")){
                if (!upDateBean.getAnd_file_url().equals("")){
                    AppUtils.openBrowser(UpgradeVersionDialog.this,upDateBean.getAnd_file_url());
                }else {
                    ToastUtil.show(UpgradeVersionDialog.this,AppConstants.STR_86ab845dbc5dcf1791498cbdb6282b1d);
                }
            }
        }
    }

    public int getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
//            mView.measure(
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int i = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        if(i>gao){
            params.height = gao;
        }
        listView.setLayoutParams(params);
        listView.requestLayout();
        return totalHeight;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy(); //必须调用该方法，防止内存泄漏
    }
}
