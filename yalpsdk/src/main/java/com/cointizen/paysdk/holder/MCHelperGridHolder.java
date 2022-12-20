package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：帮助中心条目
 * 时间: 2018-09-12 18:41
 */

public class MCHelperGridHolder extends BaseHolder<String> {

    private final Context activity;
    private TextView tvGrid;
    private LayoutInflater mInflater;

    public MCHelperGridHolder(Context activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void refreshView(String s, int position, Activity activity) {
        tvGrid.setText(s);
    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_helper_grid");
        View inflate = mInflater.inflate(layout, null);
        tvGrid = (TextView)inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mch_helper_grid"));
        inflate.setTag(this);
        return inflate;
    }
}
