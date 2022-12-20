package com.cointizen.paysdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cointizen.open.YalpGamesSdk;
import com.cointizen.paysdk.activity.MCHCouponAllActivity;
import com.cointizen.paysdk.activity.MCHDiscountRebateActivity;
import com.cointizen.paysdk.activity.MCHFunctionPopActivity;
import com.cointizen.paysdk.activity.MCHMsgActivity;
import com.cointizen.paysdk.activity.MCHelperCenter;
import com.cointizen.paysdk.activity.MCHPacksActivity;
import com.cointizen.paysdk.activity.MCHPayRecordActivity;
import com.cointizen.paysdk.activity.MCHUserCenterActivity;
import com.cointizen.paysdk.activity.MCHWelfareActivity;
import com.cointizen.paysdk.bean.FunctionBean;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.SharedPreferencesUtils;
import com.cointizen.streaming.WatchLiveStreamingActivity;

import java.util.ArrayList;

/**
 * 描述：功能弹窗adapter
 * 时间: 2018-10-25 16:33
 */

public class MCHFunctionAdapter extends BaseAdapter {


    private final ArrayList<FunctionBean> list;
    private final MCHFunctionPopActivity activity;
    private final View view;
    private LayoutInflater mInflater;

    public MCHFunctionAdapter(ArrayList<FunctionBean> list, MCHFunctionPopActivity mcFunctionPopActivity, View layout_yindao) {
        this.list = list;
        activity = mcFunctionPopActivity;
        view = layout_yindao;
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
        ViewHolder viewHolder = null;
        if(view==null){
            mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layout = MCHInflaterUtils.getLayout(activity, "mch_item_mch_fun");
            View inflate = mInflater.inflate(layout, null);
            viewHolder = new ViewHolder(inflate);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        final FunctionBean functionBean = list.get(i);
        viewHolder.icon.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,functionBean.icon));
        viewHolder.tvName.setText(functionBean.name);
        viewHolder.layoutFun.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                OnCilck(functionBean.name);
            }
        });
        return viewHolder.getView();
    }

    public class ViewHolder {

        private   View view;
        public ImageView icon;
        public TextView tvName;
        public View layoutFun;

        public ViewHolder(View itemView) {
            view = itemView;
            layoutFun = itemView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "layout_fun"));
            icon = itemView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "img_icon"));
            tvName = itemView.findViewById(MCHInflaterUtils.getIdByName(activity, "id", "tv_name"));
            itemView.setTag(this);
        }

        public View getView(){
            return view;
        }
    }

    private void OnCilck(String name){

        if(view.getVisibility()==View.VISIBLE){
            SharedPreferencesUtils.getInstance().setIsFirstOpen(activity,false);
            activity.startActivity(new Intent(activity, MCHUserCenterActivity.class));activity.finish();
            return;
        }


        if(name.equals(AdapterConstants.S_MDTpdusqWV)){
            //我的
            activity.startActivity(new Intent(activity, MCHUserCenterActivity.class));activity.finish();
        }else if(name.equals(AdapterConstants.S_TAOYKqUIHk)){
            //礼包
            activity.startActivity(new Intent(activity, MCHPacksActivity.class));activity.finish();
        }else if (name.equals(AdapterConstants.S_bCnNffuhQn)){
            //代金券
            activity.startActivity(new Intent(activity, MCHCouponAllActivity.class));activity.finish();
        } else if (name.equals(AdapterConstants.S_OqRgOXpZLe)){
            //折扣
            activity.startActivity(new Intent(activity, MCHDiscountRebateActivity.class));activity.finish();
        }else if (name.equals("Stream")){
            activity.startActivity(new Intent(activity, WatchLiveStreamingActivity.class));
            activity.finish();
        }
        else if(name.equals(AdapterConstants.S_fSpGqlzAmz)){
            //福利
            activity.startActivity(new Intent(activity, MCHWelfareActivity.class));activity.finish();
        }else if(name.equals(AdapterConstants.S_mSKCOjXERz)){
            //客服
            activity.startActivity(new Intent(activity,MCHelperCenter.class));activity.finish();
        }else if(name.equals(AdapterConstants.S_OUrZXHwuEu)){
            //游戏账单
            activity.startActivity(new Intent(activity, MCHPayRecordActivity.class));activity.finish();
        }else if(name.equals(AdapterConstants.S_aRfDzLYgme)){
            activity.startActivity(new Intent(activity, MCHMsgActivity.class));
        }else if(name.equals(AdapterConstants.S_eSdRjSJbVD)){
            //好友
        }else if(name.equals(AdapterConstants.S_XgsfnyOZVI)){
            activity.finish();
            //注销
            YalpGamesSdk.getYalpGamesSdk().exitPopup(null);
        }
    }



}
