package com.cointizen.paysdk.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cointizen.paysdk.entity.AddPtbEntity;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.paysdk.utils.TimeConvertUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MCHMoneyRecordAdapter extends BaseAdapter {
	
	private static final String TAG = "MCMoneyRecordAdapter";
	
	Context ctx;
	/**
	 * 充值记录列表
	 */
	private List<AddPtbEntity> packList = new ArrayList<AddPtbEntity>();

	private LayoutInflater mInflater;

	public MCHMoneyRecordAdapter(Context ctx, List<AddPtbEntity> packList) {
		this.ctx = ctx;
		this.packList = packList;
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
		// mch_item_addptb_record
		AddPtbEntity addPtbInfo = packList.get(position);
		MCLog.e(TAG,"fun#getGamePackList  packInfo = " + addPtbInfo.toString());
		
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(MCHInflaterUtils.getLayout(ctx, "mch_item_addptb_record"), null);
			viewHolder.txtRecodetime = (TextView) convertView.findViewById(
					MCHInflaterUtils.getControl(ctx, "txt_mch_rocord_time"));
			viewHolder.txtRecodePtb = (TextView) convertView.findViewById(
					MCHInflaterUtils.getControl(ctx, "txt_mch_rocord_ptb"));
			viewHolder.txtRecodeType  = (TextView) convertView.findViewById(
					MCHInflaterUtils.getControl(ctx, "txt_mch_rocord_type"));
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String time = TimeConvertUtils.timet(addPtbInfo.getAddPtbTime());
		viewHolder.txtRecodetime.setText(time);
		
		StringBuilder ptb = new StringBuilder();
		ptb.append(addPtbInfo.getBlannce());
		viewHolder.txtRecodePtb.setText("￥"+ptb.toString());

		viewHolder.txtRecodeType.setText(addPtbInfo.getBuyPTBType());
		
		return convertView;
	}

	static class ViewHolder {
		TextView txtRecodetime;
		TextView txtRecodePtb;
		TextView txtRecodeType;
	}
}
