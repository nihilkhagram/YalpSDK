package com.cointizen.paysdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.entity.MsgTZModel;
import com.cointizen.paysdk.holder.MCMsgListHolder;

import java.util.ArrayList;

/**
 * 描述：消息Adapter
 * 时间: 2018-10-11 17:22
 */

public class MCHMsgListAdapter extends BaseAdapter {

    private ArrayList<MsgTZModel.ListBean> listBeans;
    private final Context context;

    public MCHMsgListAdapter(ArrayList<MsgTZModel.ListBean> listBeans, Context context) {
        this.context = context;
        this.listBeans = listBeans;
    }

    @Override
    public int getCount() {
        return listBeans.size();
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
        MCMsgListHolder mcMsgListHolder = null;
        if(view==null){
            mcMsgListHolder = new MCMsgListHolder(context);
        }else{
            mcMsgListHolder = (MCMsgListHolder)view.getTag();
        }
        MsgTZModel.ListBean listBean = listBeans.get(i);
        mcMsgListHolder.setData(listBean,i,null);
        return mcMsgListHolder.getContentView();
    }

    public void setData(ArrayList<MsgTZModel.ListBean> data) {
        this.listBeans = data;
        notifyDataSetChanged();
    }
}
