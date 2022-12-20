package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.view.round.NiceImageView;

/**
 * 描述：赞我list条目
 * 时间: 2018-09-19 15:58
 */

public class MCMsgZWListHolder extends BaseHolder<String> {

    private final FragmentActivity fragmentActivity;
    private LayoutInflater mInflater;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvCon;
    private NiceImageView imgIcon;

    public MCMsgZWListHolder(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    protected void refreshView(String s, int position, Activity activity) {

    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_zw");
        View inflate = mInflater.inflate(layout, null);
        imgIcon = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "img_icon"));
        tvName = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_name"));
        tvTime = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_time"));
        tvCon = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mc_con"));
        imgIcon.setCornerRadius(10);
        inflate.setTag(this);
        return inflate;
    }

}
