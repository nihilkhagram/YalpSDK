package com.cointizen.paysdk.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.cointizen.open.FlagControl;
import com.cointizen.open.YalpGamesSdk;

import com.cointizen.paysdk.adapter.MCHNoticePagerAdapter;
import com.cointizen.paysdk.bean.NoticeModel;

import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.bartools.ImmersionBar;

import java.util.List;

/**
 * 描述：系统图文公告
 * 时间: 2018-10-17 9:19
 */

public class MCHNoticeDialogActivity extends FragmentActivity{


    private View btnClose;
    private TextView btnPrevious;
    private TextView btnNext;
    private ViewPager viewpager;
    private NoticeModel noticeModel;
    private int pager = 0;
    private MCHNoticePagerAdapter MCHNoticePagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();
        YalpGamesSdk.getYalpGamesSdk().stopFloating(YalpGamesSdk.getMainActivity());
        if(savedInstanceState!=null){
            noticeModel = (NoticeModel)savedInstanceState.getSerializable("bean");
        }else{
            noticeModel = (NoticeModel) getIntent().getSerializableExtra("NoticeModel");
        }
        setContentView(MCHInflaterUtils.getLayout(this,"mch_dialog_notice"));
        btnClose = findViewById(getId("btn_close"));
        btnPrevious = findViewById(getId("btn_previous"));
        btnNext = findViewById(getId("btn_next"));
        viewpager = findViewById(getId("viewpager"));
        MCHNoticePagerAdapter = new MCHNoticePagerAdapter(getSupportFragmentManager(), noticeModel.getList());
        viewpager.setAdapter(MCHNoticePagerAdapter);
        BtnClik btnClik = new BtnClik();
        btnClose.setOnClickListener(btnClik);
        btnPrevious.setOnClickListener(btnClik);
        btnNext.setOnClickListener(btnClik);
        if(noticeModel.getList().size()==1){
            btnPrevious.setText(ActivityConstants.S_LcMHgpQSVM);
            btnNext.setText(ActivityConstants.S_OUUnmBQKJS);
            btnPrevious.setTextColor(Color.parseColor("#666666"));
            btnNext.setTextColor(Color.parseColor("#666666"));
        }else{
            btnPrevious.setText(ActivityConstants.S_LcMHgpQSVM);
            btnPrevious.setTextColor(Color.parseColor("#666666"));
            btnNext.setText(ActivityConstants.S_XNoNzmJTju+noticeModel.getList().get(1).getTitle());
            btnNext.setTextColor(Color.parseColor("#333333"));
        }
        viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pager = position;
                List<NoticeModel.ListBean> list = noticeModel.getList();
                if(position==0 && list.size()>1){
                    btnPrevious.setText(ActivityConstants.S_LcMHgpQSVM);
                    btnPrevious.setTextColor(Color.parseColor("#666666"));
                    btnNext.setText(ActivityConstants.S_XNoNzmJTju+list.get(position+1).getTitle());
                    btnNext.setTextColor(Color.parseColor("#333333"));
                }else if(position==noticeModel.getList().size()-1 && list.size()>1){
                    btnNext.setText(ActivityConstants.S_OUUnmBQKJS);
                    btnNext.setTextColor(Color.parseColor("#666666"));
                    btnPrevious.setText(ActivityConstants.S_egMfLqJrbj+list.get(position-1).getTitle());
                    btnPrevious.setTextColor(Color.parseColor("#333333"));
                }else{
                    btnNext.setText(ActivityConstants.S_XNoNzmJTju+list.get(position+1).getTitle());
                    btnNext.setTextColor(Color.parseColor("#333333"));
                    btnPrevious.setText(ActivityConstants.S_egMfLqJrbj+list.get(position-1).getTitle());
                    btnPrevious.setTextColor(Color.parseColor("#333333"));
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(noticeModel!=null){
            MCHNoticePagerAdapter.setData(noticeModel.getList());
        }
    }

    class BtnClik implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            List<NoticeModel.ListBean> list = noticeModel.getList();
            if(view.getId()==btnClose.getId()){
                if( FlagControl.isFloatingOpen){
                    finish();
                }
               finish();
            }else if(view.getId()==btnPrevious.getId()){
                if(pager!=0&&list.size()>1){
                    viewpager.setCurrentItem(pager-1);
                }
            }else if(view.getId()==btnNext.getId()){
                if(pager!=list.size()-1&&list.size()>1){
                    viewpager.setCurrentItem(pager+1);
                }
            }
        }
    }





    protected int getId(String idName) {
        return MCHInflaterUtils.getControl(this, idName);
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("bean",noticeModel);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        noticeModel = (NoticeModel)savedInstanceState.getSerializable("bean");
        if(noticeModel!=null){
            MCHNoticePagerAdapter.setData(noticeModel.getList());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent noticeClose = new Intent();
        noticeClose.setAction("NoticeClose");
        sendBroadcast(noticeClose);
        ImmersionBar.with(this).destroy(); //必须调用该方法，防止内存泄漏
    }
}
