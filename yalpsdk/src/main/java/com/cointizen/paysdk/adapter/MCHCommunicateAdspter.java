package com.cointizen.paysdk.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cointizen.paysdk.utils.MCHInflaterUtils;

public class MCHCommunicateAdspter extends BaseAdapter implements
		OnClickListener {

	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;

	public MCHCommunicateAdspter(Context context, List<Map<String, Object>> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	/**
	 * 组件集合，对应list.xml中的控件
	 * 
	 * @author Administrator
	 */
	public final class Zujian {
		public LinearLayout ll_left;
		public ImageView iv_ico_left;
		public TextView tv_name_left;
		public LinearLayout ll_message;
		public TextView tv_message;
		public LinearLayout ll_right;
		public ImageView iv_ico_right;
		public TextView tv_name_right;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	/**
	 * 获得某一位置的数据
	 */
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	/**
	 * 获得唯一标识
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	Zujian zujian = null;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			zujian = new Zujian();
			// 获得组件，实例化组件
			convertView = layoutInflater.inflate(MCHInflaterUtils.getLayout(context,"mch_list_communicate"),null);
			zujian.ll_left = (LinearLayout) convertView
					.findViewById(MCHInflaterUtils.getControl(context,"ll_left"));
			zujian.iv_ico_left = (ImageView) convertView
					.findViewById(MCHInflaterUtils.getControl(context,"iv_ico_left"));
			zujian.tv_name_left = (TextView) convertView
					.findViewById(MCHInflaterUtils.getControl(context,"tv_name_right"));
			zujian.ll_message = (LinearLayout)convertView.findViewById(MCHInflaterUtils.getControl(context,"ll_message"));
			zujian.tv_message = (TextView) convertView
					.findViewById(MCHInflaterUtils.getControl(context,"tv_message"));
			zujian.ll_right = (LinearLayout) convertView
					.findViewById(MCHInflaterUtils.getControl(context,"ll_right"));
			zujian.iv_ico_right = (ImageView) convertView
					.findViewById(MCHInflaterUtils.getControl(context,"iv_ico_right"));
			zujian.tv_name_right = (TextView) convertView
					.findViewById(MCHInflaterUtils.getControl(context,"tv_name_right"));

			convertView.setTag(zujian);
		} else {
			zujian = (Zujian) convertView.getTag();
		}
		// 绑定数据
	    String  type=(String)data.get(position).get("type");
        if("me".equals(type)){
       	zujian.ll_message.setGravity(Gravity.RIGHT);
		zujian.ll_left.setVisibility(View.INVISIBLE);
		zujian.ll_right.setVisibility(View.VISIBLE);
		zujian.iv_ico_right.setBackgroundResource((Integer) data.get(position).get("iv_ico"));
		zujian.tv_name_right.setText((String)data.get(position).get("tv_name"));
		zujian.tv_message.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_yzx_green_rightbubble"));
        }else if("oth".equals(type)){// 其他用户消息
        	zujian.ll_message.setGravity(Gravity.LEFT);
        	zujian.ll_left.setVisibility(View.VISIBLE);
    		zujian.ll_right.setVisibility(View.INVISIBLE);
    		zujian.iv_ico_left.setBackgroundResource((Integer) data.get(position).get("iv_ico"));
    		zujian.tv_name_left.setText((String)data.get(position).get("tv_name"));
    		zujian.tv_message.setBackgroundResource(MCHInflaterUtils.getDrawable(context,"mch_yzx_green_leftbubble"));
        }
		zujian.tv_message.setText((String)data.get(position).get("tv_message"));
		return convertView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}