package com.cointizen.plugin.vip.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cointizen.paysdk.holder.BaseHolder;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：VIP等级说明list条目
 * 时间: 2020-04-16 10:58
 */

public class MCVipListHolder extends BaseHolder<String> {

    private final Context context;
    private LayoutInflater mInflater;
    private TextView tvVipName;
    private TextView tvVipLevel;


    public MCVipListHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void refreshView(String s, int position, Activity activity) {
        tvVipName.setText("VIP" + (position+1));
        tvVipLevel.setText(s);
    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_vip_level");
        View inflate = mInflater.inflate(layout, null);
        tvVipName = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "mch_tv_vip_name"));
        tvVipLevel = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "mch_tv_vip_money"));
        inflate.setTag(this);
        return inflate;
    }

}
