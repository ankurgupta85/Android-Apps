package com.yolo.homepage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.DropBoxManager.Entry;

import com.yolo.datastore.PastSearchDataStore;
import com.yolo.util.SearchDataAccessObject;

public class LogoutService extends IntentService{

	public LogoutService() {
		super("LogoutService");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	
		JSONObject finalObj = getAllPastSearchesInJSON();
		System.out.println("Inside log out service");
			System.out.println(finalObj);	
			
			// Need to check if network is available.
			// Need to send Data using http client post data. 
			// If data is sent, then delete database. 
			// Else need to store the data in file in system. Also need to rename it such that on next app start, need to upload it.
			// Also need to append the username with the json data. 
			// After data is sent, need to delete the file if exists. 
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost();
			
			
			
			
			
			
			// Delete user search database.
			// SearchDataAccessObject sdao = new SearchDataAccessObject(getApplicationContext());
			//	sdao.deleteAllSearch();
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	

	@Override
	protected void onHandleIntent(Intent intent) {
		
		JSONObject finalObj = getAllPastSearchesInJSON();
	System.out.println("Inside log out service");
		System.out.println(finalObj);	
		
		
		// Delete user search database.
		SearchDataAccessObject sdao = new SearchDataAccessObject(getApplicationContext());
		sdao.deleteAllSearch();
		
		}
	

	
	
	private JSONObject getAllPastSearchesInJSON()
	{
		SearchDataAccessObject sdao = new SearchDataAccessObject(getApplicationContext());
		HashMap<Integer, PastSearchDataStore> allSearches = sdao.getAllSearches();
		Iterator iter = allSearches.entrySet().iterator();
		JSONArray array = new JSONArray();
		while(iter.hasNext())
		{
			JSONObject jsonObj = new JSONObject();
			Map.Entry pairs = (Map.Entry) iter.next();
			try {
				jsonObj.put("hash", (Integer)pairs.getKey());
				jsonObj.put("value", pairs.getValue());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			array.put(jsonObj);
			
		}
		
		
		
		JSONObject finalObj= new JSONObject();
		try {
			finalObj.put("searches", array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return finalObj;
	}
	
}
