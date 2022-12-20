package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cointizen.paysdk.bean.JFDHPtbBean;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：兑换记录条目
 * 时间: 2018-09-28 15:44
 */

public class DHJLHolder extends BaseHolder<JFDHPtbBean.DataBean> {

    private final Context context;
    private LayoutInflater mInflater;
    private TextView tvPTB;
    private TextView tvJF;
    private TextView tvTime;

    public DHJLHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void refreshView(JFDHPtbBean.DataBean s, int position, Activity activity) {
        tvPTB.setText("￥"+s.getNumber());
        tvJF.setText(s.getPay_amount());
        tvTime.setText(AppUtils.MonthDay(s.getCreate_time(),"yyyy.MM.dd HH:mm:ss"));
    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_dhjl");
        View inflate = mInflater.inflate(layout, null);
        tvPTB = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mch_ptb"));
        tvJF = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mch_jf"));
        tvTime = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mch_time"));
        return inflate;
    }
}
