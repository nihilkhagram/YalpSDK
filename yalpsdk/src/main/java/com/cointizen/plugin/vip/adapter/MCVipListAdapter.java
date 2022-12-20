package com.cointizen.plugin.vip.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.plugin.vip.holder.MCVipListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：Vip等级说明列表Adapter
 * 时间: 2020-04-16 10:39
 */
public class MCVipListAdapter extends BaseAdapter{
    private String TAG = "MCVipListAdapter";
    private Activity activity;
    private List<String> listData = new ArrayList<>();


    public MCVipListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setListData(List<String> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MCVipListHolder holder = null;
        if (convertView == null){
            holder = new MCVipListHolder(activity);
        }else {
            holder = (MCVipListHolder) convertView.getTag();
        }
        holder.setData(listData.get(position),position,activity);
        return holder.getContentView();
    }


}
