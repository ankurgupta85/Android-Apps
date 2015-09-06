package com.example.yolo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class SelectorHelper extends Activity  implements SearchView.OnQueryTextListener,SearchView.OnCloseListener{

	ListView myList=null;
	ArrayList<Items> listData=new ArrayList<Items>();
	 HelperAdapter listAdapter=null;
	 SearchView searchView =null;
	 int categoryID =0;
	 String category=null;
	 String from=null;
	 String to = null;
	 String city =null;
	 String state =null;
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity1);

		listData = getListData();
		/*ListView lv1 = (ListView) findViewById(R.id.custom_list);
		lv1.setAdapter(new CustomListAdapter(this, listData));*/
		searchView = (SearchView)findViewById(R.id.searchView1);
		
		int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		final TextView textView = (TextView) searchView.findViewById(id);
		//searchView.setQueryHint("Category");
		textView.setTextColor(Color.WHITE);
		searchView.setOnQueryTextListener(this);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		  searchView.setIconifiedByDefault(true);
		  searchView.setOnQueryTextListener(this);
		  searchView.setOnCloseListener(this);
		  
		  displayList();
		myList.setOnItemClickListener(new OnItemClickListener() {

			
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
			
	/*			InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
					
	*/			Object o = myList.getItemAtPosition(position);
				Items newsData = (Items) o;
				Toast.makeText(SelectorHelper.this,
						"Selected :" + " " + newsData, Toast.LENGTH_LONG)
						.show();
			
				Intent intent = getIntent();
				categoryID = intent.getIntExtra("categoryID", 0);
				category = intent.getStringExtra("category");
				from = intent.getStringExtra("from");
				to = intent.getStringExtra("to");
				city = intent.getStringExtra("city");
				state = intent.getStringExtra("state");
				
				if(categoryID == 0)
				{
					Intent intent1 = new Intent(getApplicationContext(),HomePage.class);
					intent1.putExtra("category", newsData.getName());
					intent1.putExtra("categoryID", newsData.getID());
					intent1.putExtra("from", from);
					intent1.putExtra("to", to);
					intent1.putExtra("state", state);
					intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent1.putExtra("city", city);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					startActivity(intent1);
				}
				else
				{
					Intent intent1 = new Intent(getApplicationContext(),HomePage.class);
					intent1.putExtra("category", category);
					intent1.putExtra("categoryID", categoryID);
					intent1.putExtra("subcategory", newsData.getName());
					intent1.putExtra("from", from);
					intent1.putExtra("to", to);
					intent1.putExtra("city", city);
					intent1.putExtra("subcategoryID", newsData.getID());
					intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent1.putExtra("state", state);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					startActivity(intent1);
				
				}
				
				
			}

		});

	}
	
	//method to expand all groups
	 private void displayList() {
		 Intent intent = getIntent();
		  categoryID = intent.getIntExtra("categoryID", 0);
		  category=intent.getStringExtra("category");
		  if(categoryID == 0)
		  {
			  listData = getListData();

		  }
		  else
		  {
			  listData = getListData(categoryID);

		  }
	  //display the list
	   
	  //get reference to the ExpandableListView
	  myList = (ListView) findViewById(R.id.custom_list);
	  //create the adapter by passing your ArrayList data
	  listAdapter = new HelperAdapter(SelectorHelper.this, listData);
	  //attach the adapter to the list
	  myList.setAdapter(listAdapter);
	   
	 }


		@SuppressLint("NewApi")
		private ArrayList<Items> getListData(int categoryID) {
			ArrayList<Items> results = new ArrayList<Items>();
			try {
							String jsonData = inputStreamToString("subcategory.json");

				try {
					// Creating JSONObject from String
					JSONObject jsonObjMain = new JSONObject(jsonData);

					// Creating JSONArray from JSONObject
					JSONArray jsonArray = jsonObjMain.getJSONArray(""+categoryID);

					// JSONArray has four JSONObject
					for (int i = 0; i < jsonArray.length(); i++) {

						// Creating JSONObject from JSONArray
						JSONObject jsonObj = jsonArray.getJSONObject(i);

						// Getting data from individual JSONObject
						int id = jsonObj.getInt("id");
						String name = jsonObj.getString("name");
						Items item = new Items();
						item.setID(id);
						item.setName(name);
						results.add(item);
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
	private ArrayList<Items> getListData() {
		ArrayList<Items> results = new ArrayList<Items>();
		try {
						String jsonData = inputStreamToString("jsonfile.json");

			try {
				// Creating JSONObject from String
				JSONObject jsonObjMain = new JSONObject(jsonData);

				// Creating JSONArray from JSONObject
				JSONArray jsonArray = jsonObjMain.getJSONArray("employee");

				// JSONArray has four JSONObject
				for (int i = 0; i < jsonArray.length(); i++) {

					// Creating JSONObject from JSONArray
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					// Getting data from individual JSONObject
					int id = jsonObj.getInt("id");
					String name = jsonObj.getString("name");
					Items item = new Items();
					item.setID(id);
					item.setName(name);
					results.add(item);
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

	private String inputStreamToString(String fileName) {
		// Reading text file from assets folder
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(getAssets().open(
					fileName)));
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

	@Override
	public boolean onQueryTextChange(String arg0) {
		Toast.makeText(getApplicationContext(), "OnQueryTextChange: "+arg0, Toast.LENGTH_SHORT).show();
		listAdapter.filterData(arg0);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(getApplicationContext(), "OnQueryTextSubmit: "+query, Toast.LENGTH_SHORT).show();
		 listAdapter.filterData(query);
		 searchView.clearFocus();
		return false;
	}

	@Override
	public boolean onClose() {
		listAdapter.filterData("");
		searchView.clearFocus();
		return false;
	}
}