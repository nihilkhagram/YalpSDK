package com.cointizen.paysdk.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.cointizen.paysdk.activity.fragments.MCHNoticeFragment;
import com.cointizen.paysdk.bean.NoticeModel;

import java.util.List;

/**
 * 描述：公告adapter
 * 时间: 2018-09-26 14:45
 */

public class MCHNoticePagerAdapter extends FragmentPagerAdapter{

    private List<NoticeModel.ListBean> listBeans;
    private MCHNoticeFragment noticeFragment;

    public MCHNoticePagerAdapter(FragmentManager fm, List<NoticeModel.ListBean> list) {
        super(fm);
        listBeans = list;
    }

    @Override
    public Fragment getItem(int position) {
            noticeFragment = new MCHNoticeFragment();
        noticeFragment.setData(listBeans.get(position));
        return noticeFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MCHNoticeFragment o = (MCHNoticeFragment)super.instantiateItem(container, position);
        if(o!=null){
            o.setData(listBeans.get(position));
        }
        return o;
    }

    @Override
    public int getCount() {
        return listBeans==null?0:listBeans.size();
    }

    public void setData(List<NoticeModel.ListBean> data) {
        this.listBeans = data;
        notifyDataSetChanged();
    }
}
