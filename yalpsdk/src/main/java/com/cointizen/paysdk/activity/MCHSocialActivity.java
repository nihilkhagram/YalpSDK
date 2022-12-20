package com.cointizen.paysdk.activity;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

public class MCHSocialActivity extends ActivityGroup {
	private ListView listView = null;
	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MCHInflaterUtils.getLayout(this,"mch_activity_social"));
		context=this;
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		TextView txtTitle = (TextView) findViewById(MCHInflaterUtils.getControl(context, "tv_mch_header_title"));
		txtTitle.setText(ActivityConstants.S_bPsAknulPu);
		ImageView ivBack = (ImageView) findViewById(MCHInflaterUtils.getControl(context,"iv_mch_header_back"));
		ivBack.setVisibility(View.VISIBLE);
		ivBack.setOnClickListener(backClick);
		TabHost m = (TabHost) findViewById(MCHInflaterUtils.getControl(context,"tabhost"));
		m.setup();
		m.setup(this.getLocalActivityManager());
//		LayoutInflater i = LayoutInflater.from(this);
//		i.inflate(R.layout.list, m.getTabContentView());
//		i.inflate(R.layout.tab2, m.getTabContentView());
//		m.addTab(m.newTabSpec("tab3").setIndicator("常见问题").setContent(R.id.LinearLayout02));
		m.addTab(m.newTabSpec("tab1").setIndicator("常见问题").setContent(new Intent(this, MCHListViewActivity.class)));
		m.addTab(m.newTabSpec("tab2").setIndicator("社区留言").setContent(new Intent(this, MCHCommunicateActivity.class)));
		m.setCurrentTab(0);
	}
	OnClickListener backClick = new OnMultiClickListener() {

		@Override
		public void onMultiClick(View v) {
			finish();
		}
	};
}
