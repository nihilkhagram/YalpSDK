package com.cointizen.paysdk.db.grpmsg;

import com.cointizen.paysdk.db.BaseDBAdapter;

public class GroupMessageAdapter extends BaseDBAdapter {
	// 常见问题表
	public final static String tb_groupmessage = "tb_groupmessage";
	final static String key_id = "key_id";
	final static String key_cqid = "key_cqid";
	final static String key_title = "key_title";
	final static String key_description = "key_description";
	public static final String DB_GroupMessageCREATE = "" + "create table "
			+ tb_groupmessage + " (" + key_id
			+ " integer primary key autoincrement," + key_cqid
			+ " VARCHAR not null," + key_title + " VARCHAR ," + key_description
			+ " VARCHAR);";
}
