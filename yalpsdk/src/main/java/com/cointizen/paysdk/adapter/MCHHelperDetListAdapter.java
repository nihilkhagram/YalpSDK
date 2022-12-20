package com.cointizen.paysdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.bean.HelperDetBean;
import com.cointizen.paysdk.holder.HelperDetHolder;

import java.util.List;

/**
 * 描述：帮助详情adapter
 * 时间: 2018-10-27 11:00
 */

public class MCHHelperDetListAdapter extends BaseAdapter {

    private final List<HelperDetBean.DataBean> dataBeans;
    private final Context context;

    public MCHHelperDetListAdapter(List<HelperDetBean.DataBean> data, Context context){
        dataBeans = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataBeans.size();
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
        HelperDetHolder helperDetHolder  =null;
        if(view==null){
            helperDetHolder = new HelperDetHolder(context);
        }else{
            helperDetHolder = (HelperDetHolder)view.getTag();
        }
        helperDetHolder.setData(dataBeans.get(i),i,null);
        return helperDetHolder.getContentView();
    }
}
