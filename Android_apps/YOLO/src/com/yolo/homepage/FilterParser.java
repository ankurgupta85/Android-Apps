package com.yolo.homepage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yolo.util.ApplicationHashMaps;

public class FilterParser {
	private HashMap<String, ArrayList<String>> filterMap = new HashMap<String, ArrayList<String>>();

	private static FilterParser filterParser = null;

	private FilterParser() {
		// TODO Auto-generated constructor stub
	}

	public static FilterParser getInstance() {
		if (filterParser == null) {
			filterParser = new FilterParser();

		}

		return filterParser;

	}

	private void setFilters(HashMap<String, ArrayList<String>> filters) {
		filterParser.filterMap = filters;
	}

	public HashMap<String, ArrayList<String>> getFilters() {
		return filterParser.filterMap;
	}

	public void startLoading(Context context) {

		filterParser.setFilters(filterParser.getFilters(context));
	}

	@SuppressLint("NewApi")
	private HashMap<String, ArrayList<String>> getFilters(Context context) {
		HashMap<String, ArrayList<String>> results = new HashMap<String, ArrayList<String>>();
		//String filtersFile = ApplicationHashMaps.getFileCache().get("filters");
		
		
		
		try {
			String jsonData = inputStreamToString(context);
			//String jsonData = inputStreamToString(filtersFile, context);

			try {
				// Creating JSONObject from String
				JSONObject jsonObjMain = new JSONObject(jsonData);

				// Creating JSONArray from JSONObject
				JSONArray jsonArray = jsonObjMain.getJSONArray("filters");

				// JSONArray has four JSONObject
				for (int i = 0; i < jsonArray.length(); i++) {

					// Creating JSONObject from JSONArray
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					// Getting data from individual JSONObject
					String key = jsonObj.getString("key");
					JSONArray value = jsonObj.getJSONArray("values");
					ArrayList<String> items  = new ArrayList<String>();
					if (value != null) {
					for(int j =0;j<value.length();j++)
					{
						items.add(value.getString(j));
					}
					
					} else {
						items = new ArrayList<String>();
					}
					// String name = jsonObj.getString("values");
					// companiesInfo.companiesHashMap.put(name, id);
					results.put(key, items);
					// results.add(name);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	private String inputStreamToString(String fileName, Context context) {
		// Reading text file from assets folder
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(context.getAssets()
					.open(fileName)));
			String temp;
			while ((temp = br.readLine()) != null)
				sb.append(temp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close(); // stop reading
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String myjsonstring = sb.toString();
		return myjsonstring;
	}


	private String inputStreamToString(Context context) {
		// Reading text file from assets folder
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(context.getAssets()
					.open("filters.json")));
			String temp;
			while ((temp = br.readLine()) != null)
				sb.append(temp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close(); // stop reading
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String myjsonstring = sb.toString();
		return myjsonstring;
	}

}
