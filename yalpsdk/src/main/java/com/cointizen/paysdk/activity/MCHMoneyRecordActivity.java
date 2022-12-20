package com.cointizen.paysdk.activity;

import java.util.ArrayList;
import java.util.List;

import com.cointizen.smartrefresh.layout.SmartRefreshLayout;
import com.cointizen.smartrefresh.layout.api.RefreshLayout;
import com.cointizen.smartrefresh.layout.footer.ClassicsFooter;
import com.cointizen.smartrefresh.layout.header.ClassicsHeader;
import com.cointizen.smartrefresh.layout.listener.OnLoadMoreListener;
import com.cointizen.smartrefresh.layout.listener.OnRefreshListener;
import com.cointizen.paysdk.adapter.MCHMoneyRecordAdapter;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.bean.UserReLogin;
import com.cointizen.paysdk.bean.UserReLogin.ReLoginCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.AddPtbEntity;
import com.cointizen.paysdk.entity.AddPtbRecordEntity;
import com.cointizen.paysdk.http.process.PTBRecordListProcess;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCLog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 平台币充值记录
 */

public class MCHMoneyRecordActivity extends MCHBaseActivity {
    private TextView txtTotal;
    private TextView tvDaodi;
    private SmartRefreshLayout layoutHaveData;
    private final static String TAG = "MCMoneyRecordActivity";

    TextView rlNoRecord;
    private final int PAGE_START_INDEX = 1;
    private int currentIndex = 1;

    private int showNum;  //当前加载展示的条数
    private int allNum; //总共数据条数

    /**
     * 充值记录列表
     */
    private List<AddPtbEntity> packList = new ArrayList<AddPtbEntity>();

    private ListView xListView;// 显示数据
    private MCHMoneyRecordAdapter recordAdapter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Constant.RECORD_LIST_SUCCESS:
                    AddPtbRecordEntity addPtbRecordEntity = (AddPtbRecordEntity) msg.obj;
                    if(addPtbRecordEntity.getAddPtbList()==null||addPtbRecordEntity.getAddPtbList().size()==0){
                        if(packList.size()==0){
                            rlNoRecord.setVisibility(View.VISIBLE);
                            layoutHaveData.setVisibility(View.GONE);
                        }else{
                            layoutHaveData.setVisibility(View.VISIBLE);
                        }

                    }else{
                        layoutHaveData.setVisibility(View.VISIBLE);
                    }


                    handlerRecordList(addPtbRecordEntity);
                    break;
                case Constant.RECORD_LIST_FAIL:
                    if (currentIndex > PAGE_START_INDEX) {
                        if(packList.size()>0){

                        }else{
                            String tipStr = (String) msg.obj;
                            if (TextUtils.isEmpty(tipStr)) {
                                tipStr = ActivityConstants.S_WBHCQICYeM;
                            }
                            ToastUtil.show(MCHMoneyRecordActivity.this, tipStr);
                            rlNoRecord.setVisibility(View.VISIBLE);
                            layoutHaveData.setVisibility(View.GONE);
                        }
                    } else {
                        tvDaodi.setVisibility(View.VISIBLE);
                        layoutHaveData.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
            layoutHaveData.finishLoadMore();
            layoutHaveData.finishRefresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout("mch_act_record"));

        initView();
        initData();
    }

    /**
     * 刷新充值记录列表
     *
     * @param addPtbRecordEntity
     */
    protected void handlerRecordList(AddPtbRecordEntity addPtbRecordEntity) {
        if (currentIndex == PAGE_START_INDEX && null == addPtbRecordEntity.getAddPtbList()) {
            rlNoRecord.setVisibility(View.VISIBLE);
            layoutHaveData.setVisibility(View.GONE);
            return;
        }

        if (addPtbRecordEntity.getAddPtbList() != null && addPtbRecordEntity.getAddPtbList().size() > 0) {
            MCLog.e(TAG, "fun#handlerRecordList  size = " + addPtbRecordEntity.getAddPtbList().size());
            packList.addAll(addPtbRecordEntity.getAddPtbList());
            if (null != recordAdapter) {
                if (currentIndex == PAGE_START_INDEX) {
                    recordAdapter.notifyDataSetInvalidated();
                } else {
                    recordAdapter.notifyDataSetChanged();
                }
            }
            AppUtils.getTotalHeightofListView(xListView);

            allNum = addPtbRecordEntity.getTotal();
            if (currentIndex == PAGE_START_INDEX){
                showNum = addPtbRecordEntity.getAddPtbList().size();
            }else {
                showNum = showNum + addPtbRecordEntity.getAddPtbList().size();
            }
            if (showNum == allNum){
                txtTotal.setVisibility(View.VISIBLE);
                txtTotal.setText(ActivityConstants.S_tptHNeucOF + allNum + ActivityConstants.S_KFXQrZGBQQ);
            }else {
                txtTotal.setVisibility(View.GONE);
            }
        } else {
            tvDaodi.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取充值记录
     */
    private void initData() {
        currentIndex = PAGE_START_INDEX;
        if (TextUtils.isEmpty(UserLoginSession.getInstance().getUserId())) {
            rlNoRecord.setVisibility(View.GONE);
            layoutHaveData.setVisibility(View.GONE);

            UserReLogin reLogin = new UserReLogin(MCHMoneyRecordActivity.this);
            reLogin.userToLogin(new ReLoginCallback() {

                @Override
                public void reLoginResult(boolean res) {
                    if (res) {
                        showRecodeList();
                    } else {
                        ToastUtil.show(MCHMoneyRecordActivity.this, ActivityConstants.S_DefHZgLfwx);
                        finish();
                    }
                }
            });
            return;
        }
        showRecodeList();
    }

    private void showRecodeList() {
        StringBuilder accountTxt = new StringBuilder();
        accountTxt.append(ActivityConstants.S_grQwBmhCms).append(UserLoginSession.getInstance().getAccount());
        MCLog.e(TAG, "accountTxt:" + accountTxt.toString());

        getPtbRecodeList();
    }

    private void getPtbRecodeList() {
        PTBRecordListProcess ptbRecordList = new PTBRecordListProcess();
        ptbRecordList.setIndex(currentIndex);
        ptbRecordList.post(mHandler);
    }

    private void initView() {
        View ivBack = findViewById(getId("btn_mch_back"));
        ivBack.setOnClickListener(backClick);

        tvDaodi = (TextView) findViewById(getId("tv_daodi"));
        txtTotal = (TextView) findViewById(getId("txt_mch_total"));
        layoutHaveData = findViewById(getId("layout_haveData"));
        rlNoRecord = (TextView) findViewById(getId("tv_nodata"));
        xListView = (ListView) findViewById(getId("xlistview_mch_record"));
        layoutHaveData.setVisibility(View.GONE);
        txtTotal.setVisibility(View.GONE);
        rlNoRecord.setVisibility(View.GONE);
        xListView.setVisibility(View.VISIBLE);
        recordAdapter = new MCHMoneyRecordAdapter(MCHMoneyRecordActivity.this, packList);
        xListView.setAdapter(recordAdapter);

        layoutHaveData.setRefreshHeader(new ClassicsHeader(this));
        layoutHaveData.setRefreshFooter(new ClassicsFooter(this));
        layoutHaveData.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentIndex = PAGE_START_INDEX;
                packList.clear();
                getPtbRecodeList();
            }
        });
        layoutHaveData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                currentIndex++;
                getPtbRecodeList();
            }
        });
    }

    OnClickListener backClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View v) {
            finish();
        }
    };
}
