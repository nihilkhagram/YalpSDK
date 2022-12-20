package com.cointizen.paysdk.adapter;

import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.holder.MCMsgZWListHolder;

/**
 * 描述：赞我Adapter
 * 时间: 2018-10-11 17:22
 */

public class MCHMsgZWListAdapter extends BaseAdapter {

    private final FragmentActivity fragmentActivity;

    public MCHMsgZWListAdapter(FragmentActivity activity) {
        fragmentActivity = activity;
    }

    @Override
    public int getCount() {
        return 6;
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
        MCMsgZWListHolder mcMsgListHolder = null;
        if(view==null){
            mcMsgListHolder = new MCMsgZWListHolder(fragmentActivity);
        }else{
            mcMsgListHolder = (MCMsgZWListHolder)view.getTag();
        }
        return mcMsgListHolder.getContentView();
    }
}
