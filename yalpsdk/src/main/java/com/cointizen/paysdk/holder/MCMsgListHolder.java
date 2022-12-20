package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cointizen.paysdk.entity.MsgTZModel;
import com.cointizen.paysdk.utils.AppUtils;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

/**
 * 描述：消息list条目
 * 时间: 2018-09-19 15:58
 */

public class MCMsgListHolder extends BaseHolder<MsgTZModel.ListBean> {

    private static final String TAG = "MCMsgListHolder";
    private final Context context;
    private LayoutInflater mInflater;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView txtMsgType;
    private View tvweidu;

    public MCMsgListHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void refreshView(MsgTZModel.ListBean s, int position, Activity activity) {
        tvTitle.setText(s.getTitle());
        tvTime.setText(AppUtils.MonthDay(s.getCreate_time()+"","yyyy/MM/dd HH:mm:ss"));
//        if(s.getRead().equals("0")){
//            tvweidu.setVisibility(View.VISIBLE);
//        }else{
            tvweidu.setVisibility(View.GONE);
//        }
        txtMsgType.setText(s.typeName());
        txtMsgType.setTextColor(Color.parseColor(typeColor(s.typeName())));
    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_msg");
        View inflate = mInflater.inflate(layout, null);
        tvTitle = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_title"));
        tvTime = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_time"));
        tvweidu = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_weidu"));
        txtMsgType = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_msg_type"));
        inflate.setTag(this);
        return inflate;
    }

    //活动  ff5f5f     公告 2aa1ff    资讯 ffa229    攻略  00ae5b
    private String typeColor(String type) {
        MCLog.e(TAG, type);
        if (HolderConstants.S_aDIsPxyJuL.equals(type)) {
            return "#ff5f5f";
        }else if (HolderConstants.S_mQMngHiKZo.equals(type)) {
            return "#ffa229";
        }else if (HolderConstants.S_nAuvahnjsP.equals(type)) {
            return "#00ae5b";
        }else if (HolderConstants.S_gxGYmYuOAs.equals(type)) {
            return "#2aa1ff";
        }

        return "#FF0000";
    }

}
