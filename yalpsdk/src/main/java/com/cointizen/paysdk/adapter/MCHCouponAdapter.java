package com.cointizen.paysdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.bean.CouponBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.dialog.MCTipDialog;
import com.cointizen.paysdk.http.process.CouponReceiveProcess;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MCHCouponAdapter extends BaseAdapter {

    private static final String TAG = "MCHCouponAdapter";

    private Activity activity;
    private LayoutInflater mInflater;
    private List<CouponBean> list = new ArrayList<CouponBean>();
    private int selectPosition = -1;
    private Handler couponListHandler;

    private int type;  // 1可领取  2已领取 3已失效

    public void setType(int type) {
        this.type = type;
    }

    public MCHCouponAdapter(Activity act, Handler handler){
        this.activity = act;
        couponListHandler = handler;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<CouponBean> listData){
        this.list = listData;
        notifyDataSetChanged();
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    public void clearSelect(){
        selectPosition = -1;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
       return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    class ViewHolder {
        private TextView tvYuan,tvPrice,tvTitle,tvTime,btnOk,tvNum;
        private ImageView imgChoose;
        private LinearLayout layoutBg;
        private MCTipDialog tipDialog;

        public void receiveCoupon(CouponBean couponModel){


            tipDialog = new MCTipDialog.Builder().setMessage(AdapterConstants.S_fLEADzfRZb).show(activity, activity.getFragmentManager());
            CouponReceiveProcess process = new CouponReceiveProcess();
            process.setCoupon_id(couponModel.getId());
            process.post(handler);
        }

        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                tipDialog.dismissAllowingStateLoss();
                switch (msg.what){
                    case Constant.RECEIVE_COUPON_SUCCESS:
                        MCLog.w(TAG, AdapterConstants.Log_rEkVqepkdl);
                        //刷新可领取的数据
//                        CouponAllProcess process = new CouponAllProcess();
//                        process.setType(type);
//                        process.post(couponListHandler);
//                        if (MCHCouponYLFragment.YLfragment != null){
//                            MCHCouponYLFragment.YLfragment.getDatas();  //刷新已领取的数据
//                        }
                        if (activity != null) {
                            activity.finish();
                        }
                        break;
                    case Constant.RECEIVE_COUPON_FAIL:
                        if (!TextUtils.isEmpty(msg.obj.toString())){
                            ToastUtil.show(activity,msg.obj.toString());
                        }
                        break;
                }
            }
        };
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final CouponBean couponModel = list.get(i);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(MCHInflaterUtils.getLayout(activity, "mch_item_coupon"), null);

            viewHolder.layoutBg = convertView.findViewById(MCHInflaterUtils.getControl(activity, "layout_bg"));
            viewHolder.imgChoose = convertView.findViewById(MCHInflaterUtils.getControl(activity, "img_choose"));
            viewHolder.tvYuan = convertView.findViewById(MCHInflaterUtils.getControl(activity, "tv_yuan"));
            viewHolder.tvPrice = convertView.findViewById(MCHInflaterUtils.getControl(activity, "tv_coupon_price"));
            viewHolder.tvTitle = convertView.findViewById(MCHInflaterUtils.getControl(activity, "tv_title"));
            viewHolder.tvTime = convertView.findViewById(MCHInflaterUtils.getControl(activity, "tv_time"));
            viewHolder.btnOk = convertView.findViewById(MCHInflaterUtils.getControl(activity, "btn_ok"));
            viewHolder.tvNum = convertView.findViewById(MCHInflaterUtils.getControl(activity, "tv_num"));

            viewHolder.imgChoose.setVisibility(View.GONE);
            viewHolder.tvNum.setVisibility(View.GONE);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (couponModel.getStatusType() == 1){  //可领取
            viewHolder.layoutBg.setBackgroundResource(MCHInflaterUtils.getIdByName(activity, "drawable", "mch_coupon_bg_n"));
            viewHolder.tvPrice.setText(couponModel.getMoney());
            viewHolder.tvTime.setText(AdapterConstants.S_WHguRPbaml+couponModel.getStart_time() + AdapterConstants.S_DMrhiWpEVs + couponModel.getEnd_time());
            if (couponModel.getLimit_money().equals("0")){
                viewHolder.tvTitle.setText(AdapterConstants.S_tsoQDWtNis);
            }else {
                viewHolder.tvTitle.setText(AdapterConstants.S_sRKpDaKRli + couponModel.getLimit_money() + AdapterConstants.S_omlMoqmQPI);
            }
            viewHolder.btnOk.setBackgroundResource(MCHInflaterUtils.getIdByName(activity, "drawable", "mch_30dp_bg_bai"));
            viewHolder.btnOk.setEnabled(true);
            viewHolder.btnOk.setTextColor(Color.parseColor("#FF7734"));
            viewHolder.btnOk.setText(AdapterConstants.S_UGTJQEcMTX);
            viewHolder.btnOk.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    viewHolder.receiveCoupon(couponModel);
                }
            });

            if (couponModel.getLimit()==0){
                viewHolder.tvNum.setVisibility(View.GONE);
            }else {
                viewHolder.tvNum.setVisibility(View.VISIBLE);
                viewHolder.tvNum.setText(AdapterConstants.S_jsADwFAWVV + couponModel.getLimit_num() + AdapterConstants.S_MMxyJSCQAf);
            }
        }

        if (couponModel.getStatusType() == 2){   //已领取
            viewHolder.layoutBg.setBackgroundResource(MCHInflaterUtils.getIdByName(activity, "drawable", "mch_coupon_bg_n"));
            viewHolder.tvPrice.setText(couponModel.getMoney());
            viewHolder.tvTime.setText(AdapterConstants.S_WHguRPbaml+couponModel.getStart_time() + AdapterConstants.S_DMrhiWpEVs + couponModel.getEnd_time());
            if (couponModel.getLimit_money().equals("0")){
                viewHolder.tvTitle.setText(AdapterConstants.S_tsoQDWtNis);
            }else {
                viewHolder.tvTitle.setText(AdapterConstants.S_sRKpDaKRli + couponModel.getLimit_money() + AdapterConstants.S_omlMoqmQPI);
            }
            viewHolder.btnOk.setEnabled(false);
            viewHolder.btnOk.setTextColor(Color.parseColor("#FF7734"));
            viewHolder.btnOk.setBackgroundResource(MCHInflaterUtils.getIdByName(activity, "drawable", "mch_30dp_coupon_bg_transparent"));
            viewHolder.btnOk.setText(AdapterConstants.S_YMaJMwOeMj);
            viewHolder.tvNum.setVisibility(View.GONE);
        }

        if (couponModel.getStatusType() == 3){  //已使用
            viewHolder.layoutBg.setBackgroundResource(MCHInflaterUtils.getIdByName(activity, "drawable", "mch_coupon_bg_d"));
            viewHolder.tvPrice.setText(couponModel.getMoney());
            viewHolder.tvTime.setText(AdapterConstants.S_WHguRPbaml+couponModel.getStart_time() + AdapterConstants.S_DMrhiWpEVs + couponModel.getEnd_time());
            if (couponModel.getLimit_money().equals("0")){
                viewHolder.tvTitle.setText(AdapterConstants.S_tsoQDWtNis);
            }else {
                viewHolder.tvTitle.setText(AdapterConstants.S_sRKpDaKRli + couponModel.getLimit_money() + AdapterConstants.S_omlMoqmQPI);
            }
            viewHolder.btnOk.setEnabled(false);
            viewHolder.btnOk.setTextColor(Color.parseColor("#BDBBBC"));
            viewHolder.btnOk.setBackgroundResource(MCHInflaterUtils.getIdByName(activity, "drawable", "mch_30dp_coupon_bg_hui"));
            viewHolder.btnOk.setText(AdapterConstants.S_bxvfnwOZJt);
            viewHolder.tvNum.setVisibility(View.GONE);
        }

        if (couponModel.getStatusType() == 4){  //已过期
            viewHolder.layoutBg.setBackgroundResource(MCHInflaterUtils.getIdByName(activity, "drawable", "mch_coupon_bg_d"));
            viewHolder.tvPrice.setText(couponModel.getMoney());
            viewHolder.tvTime.setText(AdapterConstants.S_WHguRPbaml+couponModel.getStart_time() + AdapterConstants.S_DMrhiWpEVs + couponModel.getEnd_time());
            if (couponModel.getLimit_money().equals("0")){
                viewHolder.tvTitle.setText(AdapterConstants.S_tsoQDWtNis);
            }else {
                viewHolder.tvTitle.setText(AdapterConstants.S_sRKpDaKRli + couponModel.getLimit_money() + AdapterConstants.S_omlMoqmQPI);
            }
            viewHolder.btnOk.setEnabled(false);
            viewHolder.btnOk.setTextColor(Color.parseColor("#BDBBBC"));
            viewHolder.btnOk.setBackgroundResource(MCHInflaterUtils.getIdByName(activity, "drawable", "mch_30dp_coupon_bg_hui"));
            viewHolder.btnOk.setText(AdapterConstants.S_vJwkkTYSPn);
            viewHolder.tvNum.setVisibility(View.GONE);
        }

        //这里处理条目单选/取消选择
        if(selectPosition == i){
            if (viewHolder.imgChoose.getVisibility() == View.VISIBLE){
                viewHolder.imgChoose.setVisibility(View.GONE);
            }else {
                viewHolder.imgChoose.setVisibility(View.VISIBLE);
            }
//            viewHolder.imgChoose.setVisibility(View.VISIBLE);
        } else{
            viewHolder.imgChoose.setVisibility(View.GONE);
        }

        return convertView;
    }

}
