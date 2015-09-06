package com.yolo.homepage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yolo.R;
import com.yolo.datastore.SearchScreenDataStore;
import com.yolo.util.ApplicationHashMaps;
import com.yolo.util.NetworkUtil;

public class SearchSelectorHelper extends Activity implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	ListView myList = null;
	ArrayList<Items> listData = new ArrayList<Items>();
	SearchHelperAdapter listAdapter = null;
	SearchView searchView = null;
	int categoryID = 0;
	boolean categorySelected = false;
	String category = null;
	String from = null;
	String to = null;
	String city = null;
	String state = null;
	String company = null;
	SearchScreenDataStore ssds = SearchScreenDataStore.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selector_helper);

		// listData = getListData();
		/*
		 * ListView lv1 = (ListView) findViewById(R.id.custom_list);
		 * lv1.setAdapter(new CustomListAdapter(this, listData));
		 */
		searchView = (SearchView) findViewById(R.id.searchView1);

		int id = searchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		final TextView textView = (TextView) searchView.findViewById(id);
		// searchView.setQueryHint("Category");
		textView.setTextColor(Color.WHITE);
		searchView.setOnQueryTextListener(this);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(this);
		categoryID = ssds.getCategoryID();
		categorySelected = getIntent().getBooleanExtra("category", false);
		displayList();
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				Object o = myList.getItemAtPosition(position);
				Items newsData = (Items) o;
				Toast.makeText(SearchSelectorHelper.this,
						"Selected :" + " " + newsData, Toast.LENGTH_LONG)
						.show();

				if (categorySelected) {

					if (ssds.getCategoryID() != newsData.getID()) {
						ssds.setCategory(newsData.getName());
						ssds.setCategoryID(newsData.getID());
						ssds.setSubcategory(null);
						ssds.setSubcategoryID(0);
				
						File file = getFileStreamPath(ApplicationHashMaps
								.getFileCache().get("subcategory"));

						if (file.exists()) {
							file.delete();

						}
						if (!NetworkUtil
								.isNetworkAvailable(getApplicationContext())) {
							alertConnection();
						}
						else
						{

						new DownloadSubcategories().execute(newsData.getID());
						}
					}
					
					

				} else {

					ssds.setSubcategory(newsData.getName());
					ssds.setSubcategoryID(newsData.getID());
				}
				Intent intent1 = new Intent(getApplicationContext(),
						Homepage.class);

				intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				setResult(RESULT_OK);
				finish();
			}

		});

	}

	private void alertConnection() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Please select one of the option.")
				.setCancelable(false)
				.setTitle("Internet Connection failed")
				.setPositiveButton("Wi-Fi",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// finish the current activity
								// AlertBoxAdvance.this.finish();
								Intent myIntent = new Intent(
										Settings.ACTION_WIFI_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						})
				.setNegativeButton("Mobile Data",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent myIntent = new Intent(
										Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// method to expand all groups
	private void displayList() {
		// Intent intent = getIntent();
		categoryID = ssds.getCategoryID();
		category = ssds.getCategory();
		if (categorySelected) {

			String categoryFile = ApplicationHashMaps.getFileCache().get(
					"category");
			// listData = getListData();
			/*
			 * BufferedReader reader = null; try { reader = new
			 * BufferedReader(new
			 * InputStreamReader(openFileInput(categoryFile))); } catch
			 * (FileNotFoundException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } StringBuffer jsonData=new StringBuffer();
			 * String line =""; try { while((line=reader.readLine())!=null) {
			 * 
			 * jsonData.append(line); //System.out.println("Read line: "+line);
			 * //Toast.makeText(context, "Read line: "+line,
			 * Toast.LENGTH_SHORT).show(); } } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */
			String jsonString = this.fileToString(categoryFile);
			listData = getListData(jsonString, "categories");

		} else {

			String subcategoryFile = ApplicationHashMaps.getFileCache().get(
					"subcategory");
			File file = getFileStreamPath(subcategoryFile);
			if (!file.exists()) {

				new DownloadSubcategories().execute(ssds.getCategoryID());

			}

			/*
			 * BufferedReader reader = null; try { reader = new
			 * BufferedReader(new
			 * InputStreamReader(openFileInput(subcategoryFile))); } catch
			 * (FileNotFoundException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } StringBuffer jsonData=new StringBuffer();
			 * String line =""; try { while((line=reader.readLine())!=null) {
			 * 
			 * jsonData.append(line); //System.out.println("Read line: "+line);
			 * //Toast.makeText(context, "Read line: "+line,
			 * Toast.LENGTH_SHORT).show(); } } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */

			while (!file.exists()) {

			}
			String jsonString = this.fileToString(subcategoryFile);
			listData = getListData(jsonString, "subcategories");
			// listData = getListData(categoryID);
			// new ShowSubcategories().onPreExecute();
		}
		// display the list

		// get reference to the ExpandableListView
		myList = (ListView) findViewById(R.id.custom_list);
		// create the adapter by passing your ArrayList data
		listAdapter = new SearchHelperAdapter(SearchSelectorHelper.this,
				listData);
		// attach the adapter to the list
		myList.setAdapter(listAdapter);

	}

	private ArrayList<Items> getListData(String jsonData, String rootName) {
		ArrayList<Items> results = new ArrayList<Items>();
		try {
			// String jsonData = inputStreamToString("subcategory.json");

			try {
				// Creating JSONObject from String
				JSONObject jsonObjMain = new JSONObject(jsonData);

				// Creating JSONArray from JSONObject
				JSONArray jsonArray = jsonObjMain.getJSONArray(rootName);

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

	private class DownloadSubcategories extends
			AsyncTask<Object, Object, Object> {

		ProgressDialog progressDialog = null;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SearchSelectorHelper.this);
			progressDialog.setMessage("Loading Sub-Categories....");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.show();

		};

		@Override
		protected Object doInBackground(Object... params) {
			// Need to check if network is available
			int categoryID = (Integer) params[0];
			saveSubCategories(categoryID);
			// = getListData(jsonSub);
			// System.out.println(listData);
			return null;

		}

		private void saveSubCategories(int categoryID) {

			final HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpClient client = new DefaultHttpClient(httpParams);

			// String url
			// ="http://192.168.0.194:5000/yolo/api/v1.0/subcategories?category_id="+categoryID;
			// context = (Context) params[1];

			HashMap<String, String> urlCache = ApplicationHashMaps
					.getURLCache();
			String subcategoryURL = urlCache.get("subcategory");
			String subcategoryFile = ApplicationHashMaps.getFileCache().get(
					"subcategory");
			subcategoryURL = subcategoryURL + categoryID;

			HttpGet request = new HttpGet(subcategoryURL);
			HttpResponse response = null;
			try {
				response = client.execute(request);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String line = "";
			StringBuffer data = new StringBuffer();
			try {
				while ((line = reader.readLine()) != null) {
					// System.out.println(line);
					// Toast.makeText(context, "Found text: "+line,
					// Toast.LENGTH_SHORT).show();
					data.append(line);
				}
				writeTofile(data.toString(), subcategoryFile);
				// readFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// return data.toString();

		}

		private void writeTofile(String line, String filename)
				throws IOException {
			File file = getFileStreamPath(filename);
			if (file.exists()) {
				file.delete();
			}
			try {
				OutputStreamWriter out = new OutputStreamWriter(openFileOutput(
						filename, MODE_PRIVATE));
				out.write(line);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private String fileToString(String fileName) {
		// Reading text file from assets folder
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					openFileInput(fileName)));
			if (br != null) {
				String temp;
				while ((temp = br.readLine()) != null)
					sb.append(temp);
			}
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
		Toast.makeText(getApplicationContext(), "OnQueryTextChange: " + arg0,
				Toast.LENGTH_SHORT).show();
		listAdapter.filterData(arg0);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(getApplicationContext(), "OnQueryTextSubmit: " + query,
				Toast.LENGTH_SHORT).show();
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