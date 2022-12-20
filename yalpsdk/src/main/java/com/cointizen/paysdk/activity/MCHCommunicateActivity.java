package com.cointizen.paysdk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cointizen.paysdk.utils.ToastUtil;
import com.cointizen.paysdk.adapter.MCHCommunicateAdspter;
import com.cointizen.paysdk.http.GroupMessage.GroupMessageProcess;

public class MCHCommunicateActivity extends MCHBaseActivity implements OnClickListener{
	ListView listView;
    EditText edt_message;
    Button btn_send;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayout("mch_activity_communicate"));
		edt_message=(EditText)findViewById(getId("edt_message"));
		btn_send=(Button)findViewById(getId("btn_send"));
		btn_send.setOnClickListener(this);
		listView = (ListView) findViewById(getId("list"));
		List<Map<String, Object>> list = getData();
		listView.setAdapter(new MCHCommunicateAdspter(this, list));
	}
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	public List<Map<String, Object>> getData() {
		GroupMessageProcess process=new GroupMessageProcess();
		process.type="0";
        process.content=ActivityConstants.S_sMnfuEzWpR;
        process.post(new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		super.handleMessage(msg);
        	}
        });
		for (int i = 0; i <3; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type","oth");//me本人消息 oth其他人消息
			map.put("iv_ico", getDrawable("mch_yzx_person"));//头像
			map.put("tv_name", "昵称");
			map.put("tv_message","小强在不？中午去那吃饭!中午去哪吃饭！");
			list.add(map);
		}
		return list;
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == getId("tv_qq")){
		    String url="mqqwpa://im/chat?chat_type=wpa&uin=501863587";
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));	
		}
		if(v.getId() == getId("btn_send")){
			//发送请求获得消息列表
			GroupMessageProcess process = new GroupMessageProcess();
			process.type="1";
	        process.content=ActivityConstants.S_sMnfuEzWpR;
	        process.post(null);
			if(TextUtils.isEmpty(edt_message.getText().toString().trim())){
				ToastUtil.show(MCHCommunicateActivity.this, ActivityConstants.S_hEIlrAAliX);
				return;
			}
			String message=edt_message.getText().toString().trim();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", "me");//me本人消息 oth其他人消息
			map.put("iv_ico", getDrawable("mch_yzx_person"));//头像
			map.put("tv_name", "我");
			map.put("tv_message",message);
			list.add(map);
			listView.setAdapter(new MCHCommunicateAdspter(this,list));
//			listView.smoothScrollToPosition(0);//移动到首部
			listView.smoothScrollToPosition(list.size()- 1);//移动到最后一行
			edt_message.setText(null);
		}
	}
}
