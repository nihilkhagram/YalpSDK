package com.cointizen.paysdk.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cointizen.paysdk.entity.GamePackInfo;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MCHPacksAdapter extends BaseAdapter {

    protected static final String TAG = "MCPacksAdapter";

    Activity activity;
    /**
     * 礼包列表
     */
    private List<GamePackInfo> packList = new ArrayList<>();
    private List<ViewHolder> holderList = new LinkedList<>();


    private LayoutInflater mInflater;

    public MCHPacksAdapter(Activity activity) {
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPackList(List<GamePackInfo> mPackList) {
        packList = mPackList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return packList.size();
    }

    @Override
    public Object getItem(int position) {
        return packList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GamePackInfo packInfo = packList.get(position);

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(MCHInflaterUtils.getLayout(activity, "mch_item_packs"), null);
            viewHolder.tvPackName = (TextView) convertView.findViewById(
                    MCHInflaterUtils.getControl(activity, "txt_mch_pack_name"));
            viewHolder.layoutPack = convertView.findViewById(
                    MCHInflaterUtils.getControl(activity, "layout_pack"));
            viewHolder.tvPackEffective = (TextView) convertView.findViewById(
                    MCHInflaterUtils.getControl(activity, "txt_mch_pack_effective"));
            viewHolder.tvPackDesc = (TextView) convertView.findViewById(
                    MCHInflaterUtils.getControl(activity, "txt_mch_pack_desc"));
            viewHolder.btnReceivePack = (TextView) convertView.findViewById(
                    MCHInflaterUtils.getControl(activity, "btn_mch_receive_pack"));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        holderList.add(viewHolder);


        viewHolder.tvPackName.setText(packInfo.getPackName());
//		StringBuilder effectDates = new StringBuilder();
//		effectDates.append("剩余有效期：").append(packInfo.getEffectiveDates()).append("天");
        if (packInfo.getEndTime().equals("0")) {
            viewHolder.tvPackEffective.setText("有效期：长期有效");
        } else {
            viewHolder.tvPackEffective.setText("有效期：" + packInfo.getStartTime() + "至" + packInfo.getEndTime());
        }
        if (packInfo.getPackStatus().equals("1")) {  //礼包领取状态 0未领取，1已领取
            viewHolder.btnReceivePack.setEnabled(true);
            viewHolder.btnReceivePack.setText("复制");
            viewHolder.btnReceivePack.setTextColor(activity.getResources().getColor(MCHInflaterUtils.getIdByName(activity,"color","mch_hui")));
            viewHolder.btnReceivePack.setBackgroundResource(MCHInflaterUtils.getDrawable(activity, "mch_btn_hui_bg"));

            viewHolder.btnReceivePack.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(packInfo.getPackCode());
                    ToastUtil.show(activity,"复制成功");
                }
            });

        }else{
            if(packInfo.getSurplus()==0){
                viewHolder.btnReceivePack.setEnabled(false);
                viewHolder.btnReceivePack.setText("已领完");
                viewHolder.btnReceivePack.setTextColor(activity.getResources().getColor(MCHInflaterUtils.getIdByName(activity,"color","mch_hui")));
                viewHolder.btnReceivePack.setBackgroundResource(MCHInflaterUtils.getDrawable(activity, "mch_btn_hui_bg"));
            }else{
                viewHolder.btnReceivePack.setEnabled(false);
                viewHolder.btnReceivePack.setText("领取");
                viewHolder.btnReceivePack.setTextColor(activity.getResources().getColor(MCHInflaterUtils.getIdByName(activity,"color","mch_bai")));
                viewHolder.btnReceivePack.setBackgroundResource(MCHInflaterUtils.getDrawable(activity, "mch_btn_lan_yuan1"));

            }
        }
        viewHolder.tvPackDesc.setText(packInfo.getPackDesc());

//        viewHolder.layoutPack.setOnClickListener(new OnMultiClickListener() {
//
//            @Override
//            public void onMultiClick(View v) {
//                Intent intent = new Intent(activity, MCGiftDetActivity.class);
//                intent.putExtra("gift_id",packInfo.getGiftId());
//                activity.startActivity(intent);
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        TextView tvPackName;
        TextView tvPackEffective;
        TextView tvPackDesc;
        TextView btnReceivePack;
        private GamePackInfo gamePackInfo;
        public View layoutPack;
    }


}
