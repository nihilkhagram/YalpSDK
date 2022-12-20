package com.cointizen.paysdk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHMyAdspter;
import com.cointizen.paysdk.db.comque.CommonQuesstionAdapter;
import com.cointizen.paysdk.db.comque.CommonQuesstionBiz;
import com.cointizen.paysdk.utils.StrUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MCHListViewActivity extends MCHBaseActivity implements OnClickListener{
	ListView listView;
    TextView tv_qq;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayout("mch_activity_mylistview"));
		// 从数据库读取数据
		CommonQuesstionAdapter adapter = new CommonQuesstionAdapter();
		adapter.open();
		biz = new CommonQuesstionBiz();
		biz = adapter.getComQue();
		adapter.close();
		listView = (ListView) findViewById(getId("list"));
		List<Map<String, Object>> list = getData();
		listView.setAdapter(new MCHMyAdspter(this, list));
	    tv_qq=(TextView)findViewById(getId("tv_qq"));
	    SpannableString ss= StrUtil.addLine(tv_qq.getText().toString());
	    ss=StrUtil.addColor(ss,Color.parseColor("#FFB400"));
	    tv_qq.setText(ss);
	    tv_qq.setOnClickListener(this);
		
	}

	public static CommonQuesstionBiz biz;

	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		for (CommonQuesstionBiz b : biz.list) {
			map = new HashMap<String, Object>();
			map.put("image", android.R.drawable.ic_menu_zoom);
			map.put("title", b.TITLE);
			map.put("info", b.DESCRIPTION);
			list.add(map);
		}
		return list;
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == getId("tv_qq")){
			boolean has_qq=isQQClientAvailable(getApplicationContext());
			if(has_qq){
		    try {
				String str_qq = ((TextView)v).getText().toString().trim();
				String url="mqqwpa://im/chat?chat_type=crm&uin="+str_qq+"&version=1&src_type=web&web_src=";
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			} catch (Exception e) {
				ToastUtil.show(MCHListViewActivity.this, ActivityConstants.S_oDFzCmIQdk);
				e.printStackTrace();
			}	
			}else{
				ToastUtil.show(MCHListViewActivity.this, ActivityConstants.S_pAgBtvFHcu);
			}
		}
	}
    /**
     * 判断qq是否可用
     * 
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
