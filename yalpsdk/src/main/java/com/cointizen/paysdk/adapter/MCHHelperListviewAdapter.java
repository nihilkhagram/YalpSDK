package com.cointizen.paysdk.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lidroid.xutils.BitmapUtils;
import com.cointizen.paysdk.bean.HelperListBean;
import com.cointizen.paysdk.holder.MCHelperListHolder;

import java.util.List;

/**
 * 描述：帮助中心条目适配器
 * 时间: 2018-09-12 18:14
 */

public class MCHHelperListviewAdapter extends BaseAdapter{

    private final Activity activity;
    private List<HelperListBean.DataBean> dataBeans;
    private final BitmapUtils bitmapUtils;

    public MCHHelperListviewAdapter(Activity activity){
        this.activity = activity;
        bitmapUtils = new BitmapUtils(activity);
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
        MCHelperListHolder holder = null;
        if(view==null){
            holder = new MCHelperListHolder(activity,bitmapUtils);
        }else{
            holder = (MCHelperListHolder)view.getTag();
        }
        holder.setData(dataBeans.get(i),i,activity);
        return holder.getContentView();
    }

    public void setData(List<HelperListBean.DataBean> data) {
        dataBeans = data;
        notifyDataSetChanged();
    }
}
