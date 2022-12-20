package com.cointizen.paysdk.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cointizen.paysdk.entity.GameRecordEntity;
import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;

/**
 * 游戏充值记录
 * 
 * @author Administrator
 *
 */
public class MCHGameRecordAdapter extends BaseAdapter {

	private static final String TAG = "MCGameRecordAdapter";

	Context ctx;
	/**
	 * 充值记录列表
	 */
	private List<GameRecordEntity> gameRecordEntities = new ArrayList<GameRecordEntity>();

	private LayoutInflater mInflater;

	public MCHGameRecordAdapter(Context ctx,
                                List<GameRecordEntity> gameRecodeList) {
		this.ctx = ctx;
		this.gameRecordEntities = gameRecodeList;
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return gameRecordEntities.size();
	}

	@Override
	public Object getItem(int position) {
		return gameRecordEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 *
	 * @param position
	 * @param convertView 布局文件
	 * @param parent
	 * @return
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// mch_item_addptb_record
		GameRecordEntity gameRecordEntity = gameRecordEntities.get(position);
		MCLog.e(TAG, "fun#getGameRecodeList  gameRecordEntity = "
				+ gameRecordEntity.toString());

		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					MCHInflaterUtils.getLayout(ctx, "mch_item_game_record"),
					null);
			viewHolder.txtRecodetime = (TextView) convertView
					.findViewById(MCHInflaterUtils.getControl(ctx,
							"txt_mch_rocord_time"));
			viewHolder.txtRecodeName = (TextView) convertView
					.findViewById(MCHInflaterUtils.getControl(ctx,
							"txt_mch_rocord_money"));
			viewHolder.txtRecodeMoney = (TextView) convertView
					.findViewById(MCHInflaterUtils.getControl(ctx,
							"txt_mch_rocord_name"));
			viewHolder.txtRecodeType = (TextView) convertView
					.findViewById(MCHInflaterUtils.getControl(ctx,
							"txt_mch_rocord_type"));

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String time = gameRecordEntity.getPayTime();
		String money = gameRecordEntity.getPayMoney();
		String name = gameRecordEntity.getPayName();
		String type = gameRecordEntity.getPayType();
		viewHolder.txtRecodetime.setText(formatTime(time));
		viewHolder.txtRecodeMoney.setText(money);
		viewHolder.txtRecodeName.setText(name);
		viewHolder.txtRecodeType.setText(ref(type));
		return convertView;
	}

	/** 格式化时间戳 */
	@SuppressLint("SimpleDateFormat")
	private static String formatTime(String timeStamp) {
		final String format1 = "yy-MM-dd HH:mm:ss";
		@SuppressWarnings("unused")
		final String format2 = "yyyy-MM-dd HH:mm:ss";
		if (null == timeStamp || "".equals(timeStamp)) {
			return "";
		}
		/** 10位数时间戳 */
		if (timeStamp.length() == 10) {
			timeStamp += "000";
		}
		DateFormat df = new SimpleDateFormat(format1);
		long ltime = Long.parseLong(timeStamp);
		return df.format(ltime);
	}

	/** 根据状态码返回支付方式 */
	public static String ref(String returnCode) {
		switch (Integer.parseInt(returnCode)) {
		case 0:
			returnCode = AdapterConstants.S_jCnaOvAseO;
			break;
		case 1:
			returnCode = AdapterConstants.S_hItkPLcjbH;
			break;
		case 2:
			returnCode = AdapterConstants.S_XuBpseizwy;
			break;
		case 3:
			returnCode = "";
			break;
		case 4:
			returnCode = AdapterConstants.S_BRtDdejEds;
			break;
		default:
			break;
		}
		return returnCode;
	}

	static class ViewHolder {
		TextView txtRecodetime;
		TextView txtRecodeName;
		TextView txtRecodeMoney;
		TextView txtRecodeType;
	}
}
