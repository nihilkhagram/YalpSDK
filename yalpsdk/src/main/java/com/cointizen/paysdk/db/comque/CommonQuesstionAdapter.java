package com.cointizen.paysdk.db.comque;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.cointizen.paysdk.db.BaseDBAdapter;
import com.cointizen.paysdk.db.DbConstants;

public class CommonQuesstionAdapter extends BaseDBAdapter {
	// 常见问题表
	public final static String tb_commonquesstion = "tb_commonquesstion";
	final static String key_id = "key_id";
	final static String key_cqid = "key_cqid";
	final static String key_title = "key_title";
	final static String key_description = "key_description";
	public static final String DB_CommonQuesstionCREATE = "" + "create table "
			+ tb_commonquesstion + " (" + key_id
			+ " integer primary key autoincrement," + key_cqid
			+ " VARCHAR not null," + key_title + " VARCHAR ," + key_description
			+ " VARCHAR);";

	public long AddComQue(CommonQuesstionBiz biz) {
		long ret = 0;
		if (biz.list.size() > 0) {
			ContentValues newValues;
			for (CommonQuesstionBiz cbiz : biz.list) {
				System.out.println(DbConstants.S_pkUNSmEmOg+cbiz.toString());
				newValues = new ContentValues();
				if (!TextUtils.isEmpty(cbiz.CQID))
					newValues.put(key_cqid, cbiz.CQID);
				if (!TextUtils.isEmpty(cbiz.TITLE))
					newValues.put(key_title, cbiz.TITLE);
				if (!TextUtils.isEmpty(cbiz.DESCRIPTION))
					newValues.put(key_description, cbiz.DESCRIPTION);
				try {
					db.insert(tb_commonquesstion, null, newValues);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	/**
	 * 常见问题列表
	 * @return
	 */
	public CommonQuesstionBiz getComQue(){
		Cursor cursor=null;
		try {
			cursor= db.query(tb_commonquesstion, new String[] {key_cqid,key_title,key_description},
					null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(cursor==null)
			return null;
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		CommonQuesstionBiz biz=new CommonQuesstionBiz();
		CommonQuesstionBiz biz2;
		for (int i=0;i<resultCounts;i++){
            biz2=new CommonQuesstionBiz();
            biz2.CQID =         cursor.getString(cursor.getColumnIndex(key_cqid));
			biz2.TITLE=         cursor.getString(cursor.getColumnIndex(key_title));
			biz2.DESCRIPTION=   cursor.getString(cursor.getColumnIndex(key_description));
			biz.list.add(biz2);
			cursor.moveToNext();
		}
		return biz;
	}
}
