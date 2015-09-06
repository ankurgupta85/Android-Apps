package com.yolo.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.yolo.datastore.PastSearchDataStore;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SearchDataAccessObject {
	@SuppressLint("SimpleDateFormat")
	// Database fields
	private static SQLiteDatabase database;
	private static SQLiteHelper dbHelper;
	private static String[] allColumns = { SQLiteHelper.SEARCH_ID,
			SQLiteHelper.category, SQLiteHelper.subcategory, SQLiteHelper.from,
			SQLiteHelper.to, SQLiteHelper.location, SQLiteHelper.company_name,
			SQLiteHelper.vendor_name, SQLiteHelper.created_timestamp,
			SQLiteHelper.updated_timestamp, SQLiteHelper.count };

	public SearchDataAccessObject(Context context) {
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
	public HashMap<Integer, PastSearchDataStore> getAllSearches() {
		Map<Integer, PastSearchDataStore> allSearch = ApplicationHashMaps
				.getSearchCache();
		open();
		Cursor cursor = database.query(SQLiteHelper.TABLE_SEARCH, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PastSearchDataStore sds = cursorToSearch(cursor);
			allSearch.put(cursor.getInt(0), sds);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		close();
		ApplicationHashMaps.setDirty(false);
		return (HashMap<Integer, PastSearchDataStore>) allSearch;
	}

	public boolean isEqual(PastSearchDataStore sds1, PastSearchDataStore sds2) {
		if ((sds1.getCount() == sds2.getCount())
				&& sds1.getCreatedAt().equals(sds2.getCreatedAt())
				&& sds1.getUpdatedAt().equals(sds2.getUpdatedAt())) {
			return true;
		} else {
			return false;
		}

	}

	// Method to write back to database with all values in cache.
	// Need to update/insert/delete the cache entries
	// This method is called when user closes the app.
	// Functions when called. onPause or onFinish
	@SuppressLint("UseSparseArrays")
	@SuppressWarnings("rawtypes")
	public void updateDatabase() {
		open();
		Map<Integer, PastSearchDataStore> allSearch = ApplicationHashMaps
				.getSearchCache();
		Map<Integer, PastSearchDataStore> newEntries = new HashMap<Integer, PastSearchDataStore>();

		// These entries will have count == -1
		Map<Integer, PastSearchDataStore> deleteEntries = new HashMap<Integer, PastSearchDataStore>();

		Iterator it = allSearch.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pairs = (Map.Entry) it.next();
			int hash = (Integer) pairs.getKey();
			PastSearchDataStore cachesds = (PastSearchDataStore) pairs.getValue();
			// Case when the entries need to be deleted from database.
			// This is the case when the entries goes beyond count of 100.
			// Particualr entry is identified and count is made 0.
			// Added those entries to temporary storage which will call delete
			// on database for given hash.
			if (cachesds.getCount() == 0) {
				deleteEntries.put(hash, cachesds);
			} else {
				PastSearchDataStore dbsds = selectSearch(hash);
				// Case when there are new entries in cache that are not in
				// database.
				// Added those entries in a temporary storage which will call
				// insert on database.
				if (dbsds == null) {
					newEntries.put(hash, cachesds);
				} else {
					// Case when we entry is present in both the cache and
					// database and count ! = -1.
					// We need to check if the values are different in cache and
					// db.
					// Needs to update the db value from cache values.
					if (!isEqual(cachesds, dbsds)) {
						updateSearch(hash, cachesds);
					}
				}
			}

			// it.remove(); // avoids a ConcurrentModificationException
		}

		// Deleting entries from database using deleteMAp

		it = deleteEntries.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pairs = (Map.Entry) it.next();
			int hash = (Integer) pairs.getKey();
			deleteSearch(hash);
		}

		it = newEntries.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pairs = (Map.Entry) it.next();
			int hash = (Integer) pairs.getKey();
			PastSearchDataStore sds = (PastSearchDataStore) pairs.getValue();
			createSearch(hash, sds);
		}

		// Need to clear allSearch cache after database is updated.
		deleteEntries.clear();
		newEntries.clear();
		//allSearch.clear();
		ApplicationHashMaps.setDirty(false);
		close();
	}

	// Create a search data store object with the current cursor position.
	// Called by getAllSearches to create object.
	private PastSearchDataStore cursorToSearch(Cursor cursor) {
		PastSearchDataStore sds = new PastSearchDataStore();
		sds.setCategory(cursor.getString(1));
		sds.setSubcategory(cursor.getString(2));
		sds.setFrom(cursor.getString(3));
		sds.setTo(cursor.getString(4));
		sds.setCity(cursor.getString(5));
		sds.setCompanyName(cursor.getString(6));
		sds.setVendorName(cursor.getString(7));
		sds.setCreatedAt(cursor.getString(8));
		sds.setUpdatedAt(cursor.getString(9));
		sds.setCount(cursor.getLong(10));

		return sds;
	}

	// Creates new entry in table with given hash and search screen data store.
	// Used by hash to update the database table.
	public boolean createSearch(int hash, PastSearchDataStore sds) {
		boolean entered = false;
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.SEARCH_ID, hash);
		values.put(SQLiteHelper.category, sds.getCategory());
		values.put(SQLiteHelper.subcategory, sds.getSubcategory());
		values.put(SQLiteHelper.from, sds.getFrom());
		values.put(SQLiteHelper.to, sds.getTo());
		values.put(SQLiteHelper.location, sds.getCity());
		values.put(SQLiteHelper.company_name, sds.getCompanyName());
		values.put(SQLiteHelper.vendor_name, sds.getVendorName());
		values.put(SQLiteHelper.created_timestamp, sds.getCreatedAt());
		values.put(SQLiteHelper.updated_timestamp, sds.getUpdatedAt());
		values.put(SQLiteHelper.count, sds.getCount());

		long insertID = database
				.insert(SQLiteHelper.TABLE_SEARCH, null, values);
		if (insertID != -1) {
			entered = true;
		}

		return entered;
	}

	// / Delete a search with the given hash
	public PastSearchDataStore selectSearch(int hash) {
		Cursor cursor = database.query(SQLiteHelper.TABLE_SEARCH, allColumns,
				SQLiteHelper.SEARCH_ID + "=" + hash, null, null, null, null);

		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			PastSearchDataStore sds = new PastSearchDataStore();
			sds.setHash(hash);
			sds.setCategory(cursor.getString(1));
			sds.setSubcategory(cursor.getString(2));
			sds.setFrom(cursor.getString(3));
			sds.setTo(cursor.getString(4));
			sds.setCity(cursor.getString(5));
			sds.setCompanyName(cursor.getString(6));
			sds.setVendorName(cursor.getString(7));
			sds.setCreatedAt(cursor.getString(8));
			sds.setUpdatedAt(cursor.getString(9));
			sds.setCount(cursor.getLong(10));
			return sds;
		} else {
			return null;
		}
	}

	// / Delete a search with the given hash
	public boolean deleteSearch(int hash) {
		int rowsDeleted = database.delete(SQLiteHelper.TABLE_SEARCH,
				SQLiteHelper.SEARCH_ID + "=" + hash, null);
		if (rowsDeleted == 1) {
			return true;
		} else
			return false;
	}

	

	// / Delete all rows in given table
	public void deleteAllSearch() {
		database = dbHelper.getWritableDatabase();
			
		int rowsDeleted = database.delete(SQLiteHelper.TABLE_SEARCH,
				null, null);
		if(rowsDeleted == 0)
		{
			return;
		}
	}

	
	
	// Updates a row with the given hash and overwrite only updated time and
	// count for the same.
	public boolean updateSearch(int hash, PastSearchDataStore sds) {
		boolean updated = false;
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.SEARCH_ID, hash);
		/*
		 * values.put(SQLiteHelper.category, sds.getCategory());
		 * values.put(SQLiteHelper.subcategory, sds.getSubcategory());
		 * values.put(SQLiteHelper.from, sds.getFrom());
		 * values.put(SQLiteHelper.to, sds.getTo());
		 * values.put(SQLiteHelper.location, sds.getCity());
		 * values.put(SQLiteHelper.company_name, sds.getCompanyName());
		 * values.put(SQLiteHelper.vendor_name, sds.getVendorName());
		 * 
		 * values.put(SQLiteHelper.created_timestamp, sds.getCreatedAt());
		 */
		values.put(SQLiteHelper.updated_timestamp, sds.getUpdatedAt());
		values.put(SQLiteHelper.count, sds.getCount());

		// updating row
		int rowsUpdated = database.update(SQLiteHelper.TABLE_SEARCH, values,
				SQLiteHelper.SEARCH_ID + " = " + hash, null);
		if (rowsUpdated == 1) {
			updated = true;
		}

		return updated;

	}

}
