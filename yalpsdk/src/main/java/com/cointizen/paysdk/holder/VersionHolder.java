package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：版本更新条目
 * 时间: 2018-09-28 15:44
 */

public class VersionHolder extends BaseHolder<String> {

    private final Context context;
    private TextView tvMchName;
    private LayoutInflater mInflater;

    public VersionHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void refreshView(String s, int position, Activity activity) {
            tvMchName.setText(s);
    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_version_tv");
        View inflate = mInflater.inflate(layout, null);
        tvMchName = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_version"));
        return inflate;
    }
}
