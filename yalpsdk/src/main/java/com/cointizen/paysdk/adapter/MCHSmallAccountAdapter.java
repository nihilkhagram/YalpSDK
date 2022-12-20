package com.cointizen.paysdk.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cointizen.paysdk.entity.UserLogin;
import com.cointizen.paysdk.holder.MCSmallAccountHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：小号列表Adapter
 * 时间: 2018-07-16 14:39
 */
public class MCHSmallAccountAdapter extends BaseAdapter{
    private String TAG = "MCSmallAccountAdapter";
    private Activity activity;
    private List<UserLogin.SmallAccountEntity> listData = new ArrayList<>();
    private UserLogin login;


    public MCHSmallAccountAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setListData(List<UserLogin.SmallAccountEntity> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public void setLoginUser(UserLogin login) {
        this.login = login;
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
        MCSmallAccountHolder holder = null;
        if (convertView == null){
            holder = new MCSmallAccountHolder(activity);
        }else {
            holder = (MCSmallAccountHolder) convertView.getTag();
        }
        holder.setSize(listData.size());
        holder.setActivity(activity);
        holder.setUserInfo(login);
        holder.setData(listData.get(position),position,activity);
        return holder.getContentView();
    }


}
