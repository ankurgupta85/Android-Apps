package com.yolo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_SEARCH = "search";
	public static final String TABLE_WISHLIST = "wishlist";
	public static final String TABLE_USER = "users";

	private static final String DATABASE_NAME = "yolo.db";
	private static final int DATABASE_VERSION = 1;

	// Search database column names
	public static final String SEARCH_ID = "_id";
	public static final String category = "category";
	public static final String subcategory = "subcategory";
	public static final String from = "from_date";
	public static final String to = "to_date";
	public static final String location = "location";
	public static final String company_name = "company_name";
	public static final String vendor_name = "vendor_name";
	public static final String created_timestamp = "createdAt";
	public static final String updated_timestamp = "updated_at";
	public static final String count = "count";

	public static final String username = "username";
	public static final String password = "password";
	public static final String email = "email";
	public static final String uuid = "uuid";

	// Database creation sql statement
	private static final String SEARCH_CREATE = "create table " + TABLE_SEARCH
			+ "(" + SEARCH_ID + " integer primary key , " + category
			+ " text not null," + subcategory + " text not null," + from
			+ " text not null," + to + " text not null," + location
			+ " text not null," + company_name + " text," + vendor_name
			+ " text," + created_timestamp + " text not null,"
			+ updated_timestamp + " text not null," + count + " integer);";

	private static final String USER_CREATE = "create table " + TABLE_USER
			+ "(" + uuid + " integer primary key , " + username
			+ " text not null, " + password + " text not null," + email
			+ " text not null);";

	// Database creation sql statement
	private static final String WISHLIST_CREATE = "create table "
			+ TABLE_WISHLIST + "(" + SEARCH_ID + " integer primary key , "
			+ category + " text not null," + subcategory + " text not null,"
			+ location + " text not null," + company_name + " text,"
			+ vendor_name + " text," + created_timestamp + " text not null,"
			+ updated_timestamp + " text not null," + count + " integer);";

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SEARCH_CREATE);
		db.execSQL(WISHLIST_CREATE);
		
			
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(USER_CREATE);
		/* db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
	 
	        // create new tables
	        onCreate(db);*/

	}

}
