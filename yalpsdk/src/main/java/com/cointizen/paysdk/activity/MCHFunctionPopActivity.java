package com.cointizen.paysdk.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cointizen.paysdk.Channels;
import com.cointizen.paysdk.R;
import com.cointizen.paysdk.adapter.ChannelsActivityAdapter;
import com.cointizen.paysdk.adapter.MCHFunctionAdapter;
import com.cointizen.paysdk.bean.FunctionBean;
import com.cointizen.paysdk.bean.SwitchManager;
import com.cointizen.paysdk.bean.UserLoginSession;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.service.ServiceManager;
import com.cointizen.paysdk.utils.AppStatus;
import com.cointizen.paysdk.utils.AppStatusManager;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.paysdk.utils.TextUtils;
import com.cointizen.paysdk.view.round.NiceImageView;
import com.google.gson.JsonObject;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：功能弹窗
 * 时间: 2018-09-08 11:11
 */

public class MCHFunctionPopActivity extends MCHBaseActivity {

    private View view_null;
    private View viewById;
    private NiceImageView hread;
    private GridView recyclerView;
    public ArrayList<FunctionBean> list = new ArrayList<FunctionBean>();
    private View layout_yindao;
    private View img_mc_1;
    private TextView mch_funtion_tv_name;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "https://run.mocky.io/v3/85cf9aaf-aa4f-41bf-b10c-308f032f7ccc";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏(主要功能就是去除页面弹出时顶部黑色条)

        if (AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE) {
            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        ComeOutType();
        super.onCreate(savedInstanceState);
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        setContentView(MCHInflaterUtils.getLayout(this, "mch_activity_function"));
        recyclerView = (GridView) findViewById(MCHInflaterUtils.getIdByName(this, "id", "recycler_view"));
        view_null = findViewById(MCHInflaterUtils.getIdByName(this, "id", "view_mch_function_null"));
        layout_yindao = findViewById(MCHInflaterUtils.getIdByName(this, "id", "layout_yindao"));
        mch_funtion_tv_name = findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_funtion_tv_name"));
        img_mc_1 = findViewById(MCHInflaterUtils.getIdByName(this, "id", "img_mc_1"));
        this.viewById = findViewById(MCHInflaterUtils.getIdByName(this, "id", "lay_con"));
        hread = findViewById(MCHInflaterUtils.getIdByName(this, "id", "mch_funtion_hread_img"));
        hread.isCircle(true);
        String head_img = UserLoginSession.getInstance().getChannelAndGame().getHead_img();
        if (!TextUtils.isEmpty(head_img)) {
            BitmapUtils bitmapUtils = new BitmapUtils(this);
            bitmapUtils.display(hread, head_img);
        } else {
            if (UserLoginSession.getInstance().getChannelAndGame().getSex() == 0) {
                hread.setImageDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(MCHFunctionPopActivity.this, "mch_nav_pic_touxiang")));
            } else {
                hread.setImageDrawable(getResources().getDrawable(MCHInflaterUtils.getDrawable(MCHFunctionPopActivity.this, "mch_nav_pic_touxiang_women")));
            }
        }
        view_null.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                finish();
            }
        });
        img_mc_1.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                layout_yindao.setVisibility(View.GONE);
                view_null.setVisibility(View.VISIBLE);
                startActivity(new Intent(MCHFunctionPopActivity.this, MCHUserCenterActivity.class));
                MCHFunctionPopActivity.this.finish();
            }
        });
        layout_yindao.setBackgroundColor(Color.parseColor("#80000000"));
        initData();
        mch_funtion_tv_name.setText(UserLoginSession.getInstance().getChannelAndGame().getAccount());
        MCHFunctionAdapter MCHFunctionAdapter = new MCHFunctionAdapter(list, this, layout_yindao);
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setNumColumns(2);
        } else {
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
//            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setNumColumns(4);
        }
        recyclerView.setAdapter(MCHFunctionAdapter);
        //是否需要新手引导
        if (SharedPreferencesUtils.getInstance().getIsFirstOpen(this) && !ServiceManager.getInstance().isBaiDuYunOS) {
            layout_yindao.setVisibility(View.VISIBLE);
            view_null.setVisibility(View.GONE);
            SharedPreferencesUtils.getInstance().setIsFirstOpen(this, false);
        } else {
            layout_yindao.setVisibility(View.GONE);
            view_null.setVisibility(View.VISIBLE);
        }

        //getData();

    }

    private void initData() {
        list.clear();
        FunctionBean wode = new FunctionBean();
        wode.icon = "mch_tab_icon_wo_n";
        wode.name = ActivityConstants.S_wPDcxcRzHG;
        list.add(wode);

        FunctionBean libao = new FunctionBean();
        libao.icon = "mch_tab_icon_libao_n";
        libao.name = ActivityConstants.S_IjYVRtxunN;
        list.add(libao);

        FunctionBean daijinquan = new FunctionBean();
        daijinquan.icon = "mch_tab_icon_discount_n";
        daijinquan.name = ActivityConstants.S_HUgOFuOykW;
        list.add(daijinquan);

        FunctionBean message = new FunctionBean();
        message.icon = "mch_nav_icon_message";
        message.name = ActivityConstants.S_IbmyuCUJrx;
        list.add(message);

        FunctionBean joinstream = new FunctionBean();
        joinstream.icon = String.valueOf(R.drawable.live);
        joinstream.name = "Stream";
        list.add(joinstream);

        if (Constant.MCH_BACKGROUND_VERSION < Constant.VERSION_920) {
            FunctionBean zhekou = new FunctionBean();
            zhekou.icon = "mch_tab_icon_zhekou";
            zhekou.name = ActivityConstants.S_yVsXIvSFal;
            list.add(zhekou);
        }

        FunctionBean zhangdan = new FunctionBean();
        zhangdan.icon = "mch_pay_record_check";
        zhangdan.name = ActivityConstants.S_SvBXTVvVFk;
        list.add(zhangdan);

        FunctionBean kefu = new FunctionBean();
        kefu.icon = "mch_tab_icon_kefu_n";
        kefu.name = ActivityConstants.S_HDOVBcTDbb;
        list.add(kefu);

        if (SwitchManager.getInstance().changeAccount() && !ServiceManager.getInstance().isBaiDuYunOS) {
            FunctionBean qiehuan = new FunctionBean();
            qiehuan.icon = "mch_tab_icon_zhuxiao_n";
            qiehuan.name = ActivityConstants.S_iiOKlNIgMm;
            list.add(qiehuan);
        }
    }


    /**
     * 弹窗背景设置
     * 横竖屏弹出动画不同
     */
    private void ComeOutType() {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        window.setDimAmount(0f);    //设置背景透明度   0~1
        // 判断Android当前的屏幕是横屏还是竖屏。横竖屏判断
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            overridePendingTransition(MCHInflaterUtils.getIdByName(this, "anim", "mch_push_bottom_in")
                    , 0);
        } else {
            //横屏
            overridePendingTransition(MCHInflaterUtils.getIdByName(this, "anim", "mch_push_left_in")
                    , 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        //注释掉activity本身的过渡动画
        //判断Android当前的屏幕是横屏还是竖屏。横竖屏判断
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            overridePendingTransition(0
                    , MCHInflaterUtils.getIdByName(this, "anim", "mch_push_buttom_out"));
        } else {
            //横屏
            overridePendingTransition(0
                    , MCHInflaterUtils.getIdByName(this, "anim", "mch_push_left_out"));
        }
    }


}
