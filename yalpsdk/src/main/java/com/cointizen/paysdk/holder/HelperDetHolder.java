package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cointizen.paysdk.bean.HelperDetBean;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：
 * 时间: 2018-10-27 11:02
 */

public class HelperDetHolder extends BaseHolder<HelperDetBean.DataBean>{

    private final Context context;
    private LayoutInflater mInflater;
    private TextView tvCon;
    private TextView tvTitle;

    public HelperDetHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void refreshView(HelperDetBean.DataBean s, int position, Activity activity) {
        tvTitle.setText(s.getTitle());
        tvCon.setText(s.getContend());
    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_helperdet");
        View inflate = mInflater.inflate(layout, null);
        tvTitle = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_title"));
        tvCon = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_con"));
        return inflate;
    }
}
