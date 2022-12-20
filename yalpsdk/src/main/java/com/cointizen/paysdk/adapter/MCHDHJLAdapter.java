package com.cointizen.paysdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.bean.JFDHPtbBean;
import com.cointizen.paysdk.holder.DHJLHolder;

import java.util.ArrayList;

/**
 * 描述：兑换记录adapter
 * 时间: 2018-09-28 15:38
 */

public class MCHDHJLAdapter extends BaseAdapter {

    private final ArrayList<JFDHPtbBean.DataBean> dataBeans;
    private final Context context;

    public MCHDHJLAdapter(ArrayList<JFDHPtbBean.DataBean> dataBeans, Context context) {
        this.dataBeans = dataBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataBeans==null?0:dataBeans.size();
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
        DHJLHolder bbHolder = null;
        if(view==null){
             bbHolder = new DHJLHolder(context);
        }else{
            bbHolder = (DHJLHolder) view.getTag();
        }
        bbHolder.setData(dataBeans.get(i),i,null);
        return bbHolder.getContentView();
    }
}
