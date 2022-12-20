package com.cointizen.paysdk.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.entity.GamePayRecordEntity;
import com.cointizen.paysdk.holder.MCGamePayListHolder;

import java.util.ArrayList;

/**
 * 描述：游戏消费Adapter
 * 时间: 2018-10-11 17:22
 */

public class MCHGamePayListAdapter extends BaseAdapter {

    private final Activity fragmentActivity;
    private final ArrayList<GamePayRecordEntity.ListsBean> list;

    public MCHGamePayListAdapter(Activity activity, ArrayList<GamePayRecordEntity.ListsBean> objects) {
        fragmentActivity = activity;
        list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
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
        MCGamePayListHolder mcMsgListHolder = null;
        if(view==null){
            mcMsgListHolder = new MCGamePayListHolder(fragmentActivity);
        }else{
            mcMsgListHolder = (MCGamePayListHolder)view.getTag();
        }
        GamePayRecordEntity.ListsBean dataBean = list.get(i);
        mcMsgListHolder.setData(dataBean,i,fragmentActivity);
        return mcMsgListHolder.getContentView();
    }
}
