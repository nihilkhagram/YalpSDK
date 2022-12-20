package com.cointizen.paysdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


public class BaseDBAdapter {
	// 声明 SQLiteDatabase 对象 db
	 protected static SQLiteDatabase db;
	 public static Context context;
	// DBOpenHelper 继承了 SQLiteOpenHelper，声明 DBOpenHelper 类对象，
	// 辅助建立、更新和打开数据库
	private DBOpenHelper dbOpenHelper;
	/**
	 * 通过 SQLiteDatabase 对象直接创建数据库方法如下： 首先调用 openOrCreateDatabases()方法 创建数据
	 * 库对象，然后执行 SQL 命令建立数据库中的表和直接的关系
	 */
	public void create() {
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

}