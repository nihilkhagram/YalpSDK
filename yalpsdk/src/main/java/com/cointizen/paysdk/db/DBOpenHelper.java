package com.cointizen.paysdk.db;

import com.cointizen.paysdk.db.comque.CommonQuesstionAdapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBOpenHelper extends SQLiteOpenHelper {
	/**
	 *
	 * @param context
	 * @param name 数据库名称
	 * @param factory
	 * @param version 数据库版本
	 */
	public DBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 在数据库第一次生成的时候会调用这个方法，一般在这个方法里边生成 数据库表
	 */
	@Override
	public void onCreate(SQLiteDatabase _db) {
		_db.execSQL(DBAdapter.DB_CREATE);
		_db.execSQL(CommonQuesstionAdapter.DB_CommonQuesstionCREATE);
	}

	/**
	 * 当数据库需要升级的时候，Android 系统会主动地调用这个方法。一般 在这个方法里边删除数据表，并建立新的数据表，当然是否还需要做其
	 * 他的操作，完全取决于应用的需求
	 */
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
			int _newVersion) {
		_db.execSQL("DROP TABLE IF EXISTS " + DBAdapter.DB_TABLE);
		_db.execSQL("DROP TABLE IF EXISTS " + CommonQuesstionAdapter.tb_commonquesstion);
		onCreate(_db);
	}

	/**
	 * 这是当打开数据库时的回调函数，一般也不会用到
	 */
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}