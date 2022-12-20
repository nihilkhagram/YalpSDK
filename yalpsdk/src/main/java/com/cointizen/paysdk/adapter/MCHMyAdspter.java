package com.cointizen.paysdk.adapter;

import java.util.List;
import java.util.Map;

import com.cointizen.paysdk.db.comque.CommonQuesstionAdapter;
import com.cointizen.paysdk.db.comque.CommonQuesstionBiz;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MCHMyAdspter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;

	public MCHMyAdspter(Context context, List<Map<String, Object>> data) {
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
		public ImageView image;
		public TextView title;
		public Button view;
		public TextView info;
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
			convertView = layoutInflater.inflate(MCHInflaterUtils.getLayout(context,"mch_list"), null);
			zujian.image = (ImageView) convertView.findViewById(MCHInflaterUtils.getControl(context,"image"));
			zujian.title = (TextView) convertView.findViewById(MCHInflaterUtils.getControl(context,"title"));
			zujian.view = (Button) convertView.findViewById(MCHInflaterUtils.getControl(context,"view"));
			zujian.info = (TextView) convertView.findViewById(MCHInflaterUtils.getControl(context,"info"));
			convertView.setTag(zujian);
		} else {
			zujian = (Zujian) convertView.getTag();
		}
		// 绑定数据
		zujian.image.setBackgroundResource((Integer) data.get(position).get(
				"image"));
		zujian.title.setText((String) data.get(position).get("title"));
		zujian.info.setText((String) data.get(position).get("info"));
		zujian.info.setVisibility(View.GONE);
		zujian.title.setOnClickListener(new OnMultiClickListener() {
			@Override
			public void onMultiClick(View v) {
				// 从数据库读取数据
				CommonQuesstionAdapter adapter = new CommonQuesstionAdapter();
				adapter.open();
				biz = new CommonQuesstionBiz();
				biz = adapter.getComQue();
				adapter.close();
				if(biz!=null){
				if(position>=0&&position<=biz.list.size())
				showInfo(position);
			}
		}
		});
		
		return convertView;
	}
	CommonQuesstionBiz biz;
	public void showInfo(int position) {
         		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle(biz.list.get(position).TITLE)
				.setMessage(biz.list.get(position).DESCRIPTION)
				.setPositiveButton(AdapterConstants.S_GMVSJeCXeH, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
}
