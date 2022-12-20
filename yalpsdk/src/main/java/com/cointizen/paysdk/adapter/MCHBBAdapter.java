package com.cointizen.paysdk.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.bean.BangBiListBean;
import com.cointizen.paysdk.holder.BBHolder;

import java.util.ArrayList;

/**
 * 描述：绑币adapter
 * 时间: 2018-09-28 15:38
 */

public class MCHBBAdapter extends BaseAdapter {

    private final ArrayList<BangBiListBean.DataBean> dataBeans;
    private final Activity bitmapUtils;

    public MCHBBAdapter(ArrayList<BangBiListBean.DataBean> dataBeans, Activity activity) {
        this.dataBeans = dataBeans;
        this.bitmapUtils = activity;
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
        BBHolder bbHolder = null;
        if(view==null){
             bbHolder = new BBHolder(bitmapUtils);
        }else{
            bbHolder = (BBHolder) view.getTag();
        }
        bbHolder.setData(dataBeans.get(i),i,bitmapUtils);
        return bbHolder.getContentView();
    }
}
