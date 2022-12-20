package com.cointizen.paysdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.holder.VersionHolder;

import java.util.List;

/**
 * 描述：版本更新适配器
 * 时间: 2018-09-12 18:14
 */

public class MCHVersionListviewAdapter extends BaseAdapter{

    private final List<String> strings;
    private final Context context;

    public MCHVersionListviewAdapter(List<String> and_remark, Context upVersionDialog) {
        strings = and_remark;
        context = upVersionDialog;
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
        VersionHolder holder = null;
        if(view==null){
            holder = new VersionHolder(context);
        }else{
            holder = (VersionHolder)view.getTag();
        }
        holder.setData(strings.get(i),i,null);
        return holder.getContentView();
    }
}
