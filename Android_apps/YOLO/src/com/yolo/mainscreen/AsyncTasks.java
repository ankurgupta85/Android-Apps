package com.yolo.mainscreen;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;

import com.yolo.datastore.PastSearchDataStore;
import com.yolo.util.ApplicationHashMaps;
import com.yolo.util.PropertiesUtil;
import com.yolo.util.SearchCacheUtil;

public class AsyncTasks {

}

// Asynch task to load past search cache.
class LoadSearchCache extends AsyncTask<Object, Object, Object> {

	
	
	
	@Override
	protected Object doInBackground(Object... arg0) {

		
		Context context = (Context) arg0[0];
		HashMap<Integer, PastSearchDataStore> searchCache = ApplicationHashMaps
				.getSearchCache();
		if (searchCache.size() == 0) {
			SearchCacheUtil util = new SearchCacheUtil(context);
			util.createCache();

		}

		return null;
	}
}



//Asynch task to load past search cache.
class LoadURLCache extends AsyncTask<Object, Object, Object> {

	
	
	
	@Override
	protected Object doInBackground(Object... arg0) {

		
		Context context = (Context) arg0[0];
		HashMap<String, String> urlCache = ApplicationHashMaps.getURLCache();
		if (urlCache.size() == 0) {
			PropertiesUtil util = new PropertiesUtil(context);
			util.createURLCache();

		}

		return null;
	}
}


//Asynch task to load past search cache.
class LoadFileCache extends AsyncTask<Object, Object, Object> {

	
	
	
	@Override
	protected Object doInBackground(Object... arg0) {

		
		Context context = (Context) arg0[0];
		HashMap<String, String> fileCache = ApplicationHashMaps.getFileCache();
		if (fileCache.size() == 0) {
			PropertiesUtil util = new PropertiesUtil(context);
			util.createFileCache();

		}

		return null;
	}
}