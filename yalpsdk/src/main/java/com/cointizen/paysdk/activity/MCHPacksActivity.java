package com.cointizen.paysdk.activity;

import java.util.ArrayList;
import java.util.List;

import com.cointizen.paysdk.entity.ChannelAndGameInfo;
import com.cointizen.smartrefresh.layout.SmartRefreshLayout;
import com.cointizen.smartrefresh.layout.api.RefreshLayout;
import com.cointizen.smartrefresh.layout.header.ClassicsHeader;
import com.cointizen.smartrefresh.layout.listener.OnRefreshListener;
import com.cointizen.paysdk.adapter.MCHPacksAdapter;
import com.cointizen.paysdk.adapter.MCHVIPPacksAdapter;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.bean.UserReLogin;
import com.cointizen.paysdk.bean.UserReLogin.ReLoginCallback;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.GamePackInfo;
import com.cointizen.paysdk.entity.PacksInfo;
import com.cointizen.paysdk.http.process.GamePacksListProcess;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.http.process.GameVIPPacksListProcess;
import com.cointizen.paysdk.http.process.UserInfoProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 描述：礼包列表
 * 时间: 2018-09-11 10:34
 */
public class MCHPacksActivity extends MCHBaseActivity {

    private static final String TAG = "MCPacksActivity";
    private MCHPacksAdapter mPacksAdapter;//xlistview的适配器
    private PacksInfo packsInfo;
    private MCHVIPPacksAdapter vipPacksAdapter;
    private PacksInfo vipPackInfo;
    private int userVIPLev;

    /**
     * 礼包列表
     */
    private List<GamePackInfo> packList = new ArrayList<GamePackInfo>();
    private List<GamePackInfo> vipPackList = new ArrayList<GamePackInfo>();
    private SmartRefreshLayout layoutHavedata;
    private ListView listview,vipListview;
    private LinearLayout layoutVipGift;
    private LinearLayout layoutGift;
    private View layoutWu,mchXian;
    private GamePacksListProcess gamePacksListProcess;
    private GameVIPPacksListProcess vipPacksListProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MCHInflaterUtils.getLayout(this, "mch_act_gift"));

        initview();
        initData();
    }


    private void initview() {
        View btnMchBack = findViewById(getId("btn_mch_back"));
        layoutHavedata = findViewById(getId("layout_havedata"));
        listview = findViewById(getId("listview"));
        vipListview = findViewById(getId("listview_tequan"));
        layoutWu = findViewById(getId("layout_wu"));
        layoutVipGift = findViewById(getId("mch_layout_vip_gift"));
        layoutGift = findViewById(getId("mch_layout_gift"));
        mchXian = findViewById(getId("mch_xian"));

        btnMchBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });

        mPacksAdapter = new MCHPacksAdapter(this);
        vipPacksAdapter = new MCHVIPPacksAdapter(this);
        listview.setAdapter(mPacksAdapter);
        vipListview.setAdapter(vipPacksAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GamePackInfo gamePackInfo = packList.get(i);
                Intent intent = new Intent(MCHPacksActivity.this, MCHGiftDetActivity.class);
                intent.putExtra("gift_id",gamePackInfo.getGiftId());
                startActivity(intent);
            }
        });

        vipListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GamePackInfo gamePackInfo = vipPackList.get(i);
                Intent intent = new Intent(MCHPacksActivity.this, MCHGiftDetActivity.class);
                intent.putExtra("gift_id",gamePackInfo.getGiftId());
                intent.putExtra("user_vip",userVIPLev);
                intent.putExtra("gift_vip",gamePackInfo.getVipLimit());
                startActivity(intent);
            }
        });
        layoutHavedata.setRefreshHeader(new ClassicsHeader(this));
        layoutHavedata.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                queryPacksList();
            }
        });


        layoutWu.setVisibility(View.GONE);
        layoutGift.setVisibility(View.GONE);
        layoutVipGift.setVisibility(View.GONE);
        mchXian.setVisibility(View.GONE);
    }


    private void initData() {
        if (TextUtils.isEmpty(UserLoginSession.getInstance().getUserId())) {

            UserReLogin reLogin = new UserReLogin(MCHPacksActivity.this);
            reLogin.userToLogin(new ReLoginCallback() {

                @Override
                public void reLoginResult(boolean res) {
                    if (res) {
                        queryPacksList();
                    } else {
                        ToastUtil.show(MCHPacksActivity.this, ActivityConstants.S_MyeavUSgyJ);
                        finish();
                    }
                }
            });
            return;
        }
    }

    private void queryPacksList() {
        //普通礼包数据
        if(gamePacksListProcess==null){
            gamePacksListProcess = new GamePacksListProcess();
        }
        gamePacksListProcess.post(mHandler);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            layoutHavedata.finishRefresh();

            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.GET_PACKS_LIST_SUCCESS:  //获取普通礼包列表成功
                    packsInfo = (PacksInfo) msg.obj;
                    if (null != packsInfo.getPackInfoList() && packsInfo.getPackInfoList().size() > 0) {
                        packList.clear();
                        packList.addAll(packsInfo.getPackInfoList());
                        layoutGift.setVisibility(View.VISIBLE);
                        mPacksAdapter.setPackList(packList);
                        AppUtils.getTotalHeightofListView(listview);
                    } else {
                        layoutGift.setVisibility(View.GONE);
                        if (Constant.MCH_BACKGROUND_VERSION<Constant.VERSION_840){
                            layoutHavedata.setVisibility(View.GONE);
                            layoutWu.setVisibility(View.VISIBLE);
                        }
                    }

                    if (Constant.MCH_BACKGROUND_VERSION>=Constant.VERSION_840){
                        //获取用户信息（主要是VIP等级）
                        UserInfoProcess userInfoProcess = new UserInfoProcess();
                        userInfoProcess.post(mHandler);
                    }
                    break;

                case Constant.GET_PACKS_LIST_FAIL:
                    layoutGift.setVisibility(View.GONE);
                    MCLog.w(TAG, "error:" + (String) msg.obj);
                    break;

                case Constant.GET_USER_INFO_SUCCESS:// 获取用户信息
                    ChannelAndGameInfo info = (ChannelAndGameInfo) msg.obj;
                    if (null != info) {
                        userVIPLev = info.getVip_level();
                        vipPacksAdapter.setUserVipLev(userVIPLev);
                        //vip特权礼包数据
                        if(vipPacksListProcess==null){
                            vipPacksListProcess = new GameVIPPacksListProcess();
                        }
                        vipPacksListProcess.post(mHandler);
                    } else {
                        ToastUtil.show(MCHPacksActivity.this, ActivityConstants.S_re_login_needed);
                        finish();
                    }
                    break;
                case Constant.GET_USER_INFO_FAIL:
                    String tip = (String) msg.obj;
                    ToastUtil.show(MCHPacksActivity.this, tip);
                    break;

                case Constant.GET_TEQUAN_LIST_SUCCESS:  //获取特权礼包列表成功
                    vipPackList.clear();
                    vipPackInfo = (PacksInfo) msg.obj;
                    if (null != vipPackInfo.getPackInfoList() && vipPackInfo.getPackInfoList().size() > 0){
                        layoutVipGift.setVisibility(View.VISIBLE);
                        vipPackList.addAll(vipPackInfo.getPackInfoList());
                        vipPacksAdapter.setPackList(vipPackList);
                        AppUtils.getTotalHeightofListView(vipListview);
                        if (packsInfo.getPackInfoList().size()>0){
                            mchXian.setVisibility(View.VISIBLE);
                        }else {
                            mchXian.setVisibility(View.GONE);
                        }
                    }else {
                        layoutVipGift.setVisibility(View.GONE);
                        if (packsInfo.getPackInfoList().size()==0){
                            layoutHavedata.setVisibility(View.GONE);
                            layoutWu.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                case Constant.GET_TEQUAN_LIST_FAIL:
                    layoutVipGift.setVisibility(View.GONE);
                    MCLog.w(TAG, "error:" + (String) msg.obj);
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        queryPacksList();
    }
}
