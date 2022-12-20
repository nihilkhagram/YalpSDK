package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.cointizen.paysdk.bean.BangBiListBean;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.view.round.NiceImageView;

/**
 * 描述：绑币条目
 * 时间: 2018-09-28 15:44
 */

public class BBHolder extends BaseHolder<BangBiListBean.DataBean> {

    private final Activity activity;
    private LayoutInflater mInflater;
    private NiceImageView imgIcon;
    private TextView tvMchName;
    private TextView imgYue;

    public BBHolder(Activity bitmapUtils) {
        super(bitmapUtils);
        activity = bitmapUtils;
    }

    @Override
    protected void refreshView(BangBiListBean.DataBean s, int position, Activity activity) {
        BitmapUtils bitmapUtils = new BitmapUtils(activity);
        bitmapUtils.display(imgIcon,s.getIcon());
        tvMchName.setText(s.getGame_name());
        imgYue.setText(s.getBind_balance());
    }

    @Override
    protected View initView(Context context) {
//        mInflater = (LayoutInflater) YalpGamesSdk.getMCApi().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_bb");
        View inflate = LinearLayout.inflate(context, layout, null);
        imgIcon = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "img_icon"));
        tvMchName = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mch_name"));
        imgYue = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mch_yue"));
        imgIcon.setCornerRadius(10);
        return inflate;
    }
}
