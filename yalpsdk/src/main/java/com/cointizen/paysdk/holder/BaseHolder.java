package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public abstract class BaseHolder<Data> {
	protected View contentView;
	protected Data data;
	
	public BaseHolder(Context context){
		contentView=initView(context);
		contentView.setTag(this); 
	}

	protected BaseHolder() {
	}

	public void setData(Data data,int position, Activity activity) {
			this.data=data;
			refreshView(data,position,activity);
	}

	protected abstract void refreshView(Data data,int position, Activity activity);
	/**
	 * 初始化View对象 及其控件
	 * @return
	 * @param context
	 */
	protected abstract View initView(Context context);
	
	/**
	 * 当Holder中的view对象显示到界面上的时候调用
	 * @return
	 */
	public View getContentView() {
		return contentView;
	}


}
