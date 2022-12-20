package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cointizen.paysdk.entity.GamePayRecordEntity;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：游戏消费list条目
 * 时间: 2018-09-19 15:58
 */

public class MCGamePayListHolder extends BaseHolder<GamePayRecordEntity.ListsBean> {

    private final Context context;
    private LayoutInflater mInflater;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvMoney;
    private TextView tvPayType;

    public MCGamePayListHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void refreshView(GamePayRecordEntity.ListsBean s, int position, Activity activity) {
        tvName.setText(s.getGame_name());
        tvMoney.setText("-"+s.getPay_amount());
        tvPayType.setText(s.getPay_way());
        tvTime.setText(AppUtils.MonthDay(s.getPay_time()+"","yyyy/MM/dd HH:mm:ss"));

    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_game_pay");
        View inflate = mInflater.inflate(layout, null);
        tvName = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_name"));
        tvTime = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_time"));
        tvMoney = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_money"));
        tvPayType = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_pay_type"));
        inflate.setTag(this);
        return inflate;
    }

}
