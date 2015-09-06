package com.yolo.homepage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yolo.util.ApplicationHashMaps;

import android.annotation.SuppressLint;
import android.content.Context;

public class CompaniesInfoParser {

	private static CompaniesInfoParser companiesInfo;
	private ArrayList<String> companiesList;
	private ArrayList<String> vendorsList;
	private HashMap<String, Integer> companiesHashMap = new HashMap<String, Integer>();

	public HashMap<String, Integer> getCompaniesHashMap() {
		return companiesHashMap;
	}

	public HashMap<String, Integer> getVendorsHashMap() {
		return vendorsHashMap;
	}

	private HashMap<String, Integer> vendorsHashMap = new HashMap<String, Integer>();

	private CompaniesInfoParser() {
		// TODO Auto-generated constructor stub
	}

	public static CompaniesInfoParser getInstance() {
		if (companiesInfo == null) {
			companiesInfo = new CompaniesInfoParser();

		}

		return companiesInfo;

	}

	public ArrayList<String> getVendorsList() {
		return vendorsList;
	}

	private void setVendorsList(ArrayList<String> vendorsList) {
		this.vendorsList = vendorsList;
	}

	public void startLoading(Context context) {
		
		companiesInfo.setCompaniesList(companiesInfo.getCompanyList(context));
		companiesInfo.setVendorsList(companiesInfo.getVendorsList(context));
	}

	@SuppressLint("NewApi")
	private ArrayList<String> getVendorsList(Context context) {
		ArrayList<String> results = new ArrayList<String>();
		String vendorsFile = ApplicationHashMaps.getFileCache().get("vendors");
		
		try {
			String jsonData = inputStreamToString(vendorsFile, context);

			try {
				// Creating JSONObject from String
				JSONObject jsonObjMain = new JSONObject(jsonData);

				// Creating JSONArray from JSONObject
				JSONArray jsonArray = jsonObjMain.getJSONArray("vendor_names");

				// JSONArray has four JSONObject
				for (int i = 0; i < jsonArray.length(); i++) {

					// Creating JSONObject from JSONArray
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					// Getting data from individual JSONObject
					int id = jsonObj.getInt("id");
					String name = jsonObj.getString("name");
					companiesInfo.vendorsHashMap.put(name, id);

					results.add(name);
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

	@SuppressLint("NewApi")
	private ArrayList<String> getCompanyList(Context context) {
		ArrayList<String> results = new ArrayList<String>();
		String companiesFile = ApplicationHashMaps.getFileCache().get("companies");
		try {
			String jsonData = inputStreamToString(companiesFile, context);

			try {
				// Creating JSONObject from String
				JSONObject jsonObjMain = new JSONObject(jsonData);

				// Creating JSONArray from JSONObject
				JSONArray jsonArray = jsonObjMain.getJSONArray("company_names");

				// JSONArray has four JSONObject
				for (int i = 0; i < jsonArray.length(); i++) {

					// Creating JSONObject from JSONArray
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					// Getting data from individual JSONObject
					int id = jsonObj.getInt("id");
					String name = jsonObj.getString("name");
					companiesInfo.companiesHashMap.put(name, id);

					results.add(name);
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

	public ArrayList<String> getCompaniesList() {
		return companiesInfo.companiesList;
	}

	private void setCompaniesList(ArrayList<String> companiesList) {
		this.companiesList = companiesList;
	}

}
