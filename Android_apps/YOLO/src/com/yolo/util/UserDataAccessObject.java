package com.yolo.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.yolo.datastore.UserDataStore;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDataAccessObject {
	@SuppressLint("SimpleDateFormat")
	// Database fields
	private static SQLiteDatabase database;
	private static SQLiteHelper dbHelper;
	private static String[] allColumns = {SQLiteHelper.uuid, SQLiteHelper.username,
			SQLiteHelper.password, SQLiteHelper.email};

	 public UserDataAccessObject(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	// Method to populate and create cache for past search.
	// Query whole database and create a search cache.
	// Called when user logs in.
	@SuppressLint("UseSparseArrays")
	public HashMap<Integer,UserDataStore> getAllUsers() {
		Map<Integer,UserDataStore> allSearch = new HashMap<Integer,UserDataStore>();
		open();
		Cursor cursor = database.query(SQLiteHelper.TABLE_USER, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserDataStore user = cursorToSearch(cursor);
			allSearch.put(cursor.getInt(0), user);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		close();
		//ApplicationHashMaps.setDirty(false);
		return (HashMap<Integer, UserDataStore>) allSearch;
	}




	// Create a search data store object with the current cursor position.
	// Called by getAllSearches to create object.
	private UserDataStore cursorToSearch(Cursor cursor) {
		UserDataStore sds = new UserDataStore();
		sds.setUsername(cursor.getString(1));
		sds.setPassword(cursor.getString(2));
		sds.setEmail(cursor.getString(3));
		
		return sds;
	}

	// Creates new entry in table with given hash and search screen data store.
	// Used by hash to update the database table.
	public boolean createUser(UserDataStore user) {
		boolean entered = false;
		int hash = hashCode(user);
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.uuid, hash);
		values.put(SQLiteHelper.username, user.getUsername());
		values.put(SQLiteHelper.password, user.getPassword());
		values.put(SQLiteHelper.email, user.getEmail());
		open();
		long insertID = database
				.insert(SQLiteHelper.TABLE_USER, null, values);
		if (insertID != -1) {
			entered = true;
		}
		close();
		return entered;
	}

	// / Delete a search with the given hash
	public UserDataStore selectUser(UserDataStore user) {
		int hash = hashCode(user);
		open();
		Cursor cursor = database.query(SQLiteHelper.TABLE_USER, allColumns,
				SQLiteHelper.uuid + "=" + hash, null, null, null, null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			UserDataStore userData = new UserDataStore();
			userData.setUsername(cursor.getString(1));
			userData.setPassword(cursor.getString(2));
			userData.setEmail(cursor.getString(3));
			close();
				
			return userData;
		} else {
			close();
			
			return null;
		}
	}

	// / Delete a search with the given hash
	public boolean deleteUser(UserDataStore user) {
		int hash = hashCode(user);
		open();
		int rowsDeleted = database.delete(SQLiteHelper.TABLE_USER,
				SQLiteHelper.uuid + "=" + hash, null);
		close();
		if (rowsDeleted == 1) {
			return true;
		} else
			return false;
	}

	

	// / Delete all rows in given table
	public void deleteAllUsers() {
		//database = dbHelper.getWritableDatabase();
		open();	
		int rowsDeleted = database.delete(SQLiteHelper.TABLE_USER,
				null, null);
		close();
		if(rowsDeleted == 0)
		{
			return;
		}
	}

	
	
	// Updates a row with the given hash and overwrite only updated time and
	// count for the same.
	public boolean updateSearch(UserDataStore user) {
		int hash = hashCode(user);
		boolean updated = false;
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.uuid, hash);
		values.put(SQLiteHelper.username, user.getUsername());
		values.put(SQLiteHelper.password, user.getPassword());
		values.put(SQLiteHelper.email, user.getEmail());
		
		open();

		// updating row
		int rowsUpdated = database.update(SQLiteHelper.TABLE_USER, values,
				SQLiteHelper.uuid + " = " + hash, null);
		
		close();
		if (rowsUpdated == 1) {
			updated = true;
		}

		return updated;

	}

	
	
	public int hashCode(UserDataStore user)
	{
		String userinfo = user.getUsername();
		return userinfo.hashCode();
	}

}
