package com.cointizen.paysdk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;


public class DBAdapter {
	// 声明 SQLiteDatabase 对象 db
	private static SQLiteDatabase db;
	private final Context context;
	// 创建用户表
		// 数据库表格名称和数据库版本
		public static final String DB_TABLE = "user";
		// 数据库表中的属性名称
		public static final String KEY_ID = "_id";
		public static final String KEY_ACCOUNT = "account";
		public static final String KEY_PASSWORD = "password";
		public static final String KEY_SMALL_ACCOUNT = "small_account";
		public static final String DB_CREATE = "create table " + DB_TABLE + "("+ KEY_ID + " integer primary key autoincrement, " + KEY_ACCOUNT+ " CHAR not null, " + KEY_PASSWORD + " CHAR);";

	// DBOpenHelper 继承了 SQLiteOpenHelper，声明 DBOpenHelper 类对象，
	// 辅助建立、更新和打开数据库
	private DBOpenHelper dbOpenHelper;
	/**
	 * 通过 SQLiteDatabase 对象直接创建数据库方法如下： 首先调用 openOrCreateDatabases()方法 创建数据
	 * 库对象，然后执行 SQL 命令建立数据库中的表和直接的关系
	 */
	public void create() {
	}

	public DBAdapter(Context _context) {
		context = _context;
	}

	// 调用了 SQLiteOpenHelper 类的 getWritableDatabase()方法和
	// getReadableDatabase()方法，打开数据库
	public void open() throws SQLiteException {
		// 声明了数据库的基本信息
		// 数据库文件的名称
		final String DB_NAME = "sdk.db";
		final int DB_VERSION = 1;
		dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
		try {
			// 负责得到一个可写的 SQLite 数据库，如果这个数据库还没有
			// 建立，那么 mOpenHelper 辅助类负责建立这个数据库。如果数
			// 据库已经建立，那么直接返回一个可写的数据库
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbOpenHelper.getReadableDatabase();
		}
	}

	public void close() {
		if (db != null) {
			// 关闭数据库
			db.close();
			db = null;
		}
	}

	public static void print(String msg, String msg2) {
		Log.i("DBAdapter", msg + msg2);
	}

	public long insert(UserInfo userInfo) {
		ContentValues newValues = new ContentValues();
		if(!TextUtils.isEmpty(userInfo.account))
		newValues.put(KEY_ACCOUNT, userInfo.account);
		if(!TextUtils.isEmpty(userInfo.password))
		newValues.put(KEY_PASSWORD, userInfo.password);
		try {
			return db.insert(DB_TABLE, null, newValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public long deleteByAccount(String account) {
		return db.delete(DB_TABLE, KEY_ACCOUNT + " = ?",new String []{account});
	}

	public long deleteAllDate() {
		// 返回删除数据的数量
		return db.delete(DB_TABLE, null, null);
	}

	public long updateByAccount(String account, UserInfo userInfo) {
		ContentValues updateValues = new ContentValues();
		if(!TextUtils.isEmpty(userInfo.account))
			updateValues.put(KEY_ACCOUNT, userInfo.account);
		if(!TextUtils.isEmpty(userInfo.password))
			updateValues.put(KEY_PASSWORD, userInfo.password);
		// 返回更新的条数
		return db.update(DB_TABLE, updateValues, KEY_ACCOUNT + "=" + account, null);
	}
	public UserInfo getOneData(String account) throws Exception{
		Cursor results = db.query(DB_TABLE, new String[] { KEY_ID, KEY_ACCOUNT,
				KEY_PASSWORD}, KEY_ACCOUNT + "=" + account, null, null, null,
				null);
		UserInfo UserInfos [] =ConvertToPeople(results);
		for (UserInfo userInfo:UserInfos) {
			return userInfo;
		}
		return null;
	}
	private UserInfo[] ConvertToPeople(Cursor cursor) {
		if(cursor==null)
			return null;
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		UserInfo[] userInfos = new UserInfo[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			userInfos[i] = new UserInfo();
			userInfos[i].ID = cursor.getInt(0);
			userInfos[i].account = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT));
			userInfos[i].password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
			cursor.moveToNext();
		}
		return userInfos;
	}
	public UserInfo[] getAllData() {
		Cursor results=null;
		try {
			results= db.query(DB_TABLE, new String[] { KEY_ID, KEY_ACCOUNT,
                    KEY_PASSWORD}, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ConvertToPeople(results);
	}
}