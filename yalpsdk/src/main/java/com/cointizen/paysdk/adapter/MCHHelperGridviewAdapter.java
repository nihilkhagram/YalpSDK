package com.cointizen.paysdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.holder.MCHelperGridHolder;

import java.util.List;

/**
 * 描述：帮助中心条目适配器
 * 时间: 2018-09-12 18:14
 */

public class MCHHelperGridviewAdapter extends BaseAdapter{

    private final Context activity;
    private List<String> strings;

    public MCHHelperGridviewAdapter(Context activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return strings==null?0:strings.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MCHelperGridHolder holder = null;
        if(view==null){
            holder = new MCHelperGridHolder(activity);
        }else{
            holder = (MCHelperGridHolder)view.getTag();
        }
        holder.setData(strings.get(i),i,null);
        return holder.getContentView();
    }

    public void setData(List<String> second_title) {
        strings = second_title;
        notifyDataSetChanged();
    }
}
