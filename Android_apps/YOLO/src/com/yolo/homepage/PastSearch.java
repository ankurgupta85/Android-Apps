package com.yolo.homepage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yolo.R;
import com.yolo.datastore.PastSearchDataStore;
import com.yolo.datastore.SearchScreenDataStore;
import com.yolo.mainscreen.MainActivity;
import com.yolo.util.ApplicationHashMaps;

public class PastSearch extends Activity implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener,
		OnTouchListener {
	ListView myList = null;
	ArrayList<PastSearchData> listData = new ArrayList<PastSearchData>();
	PastSearchHelperAdapter listAdapter = null;
	SearchView searchView = null;
	HashMap<Integer, PastSearchDataStore> searchCache = null;
	private boolean refresh = false;
	private boolean allowRefresh = false;
	private float startY = 0;
	private float REFRESH_THRESHOLD = 100;
	private String searchQuery = null;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private final int FROM_DATE = 0;
	private final int TO_DATE = 1;
	int year;
	int month;
	int day;
	PastSearchData pastSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		searchCache = ApplicationHashMaps.getSearchCache();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_past_search);
		searchView = (SearchView) findViewById(R.id.search_past);
		int id = searchView.getContext().getResources()
				.getIdentifier("android:id/past_search_txt", null, null);
		final TextView textView = (TextView) searchView.findViewById(id);
		searchView.setOnQueryTextListener(this);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(this);

		displayList();
		myList.setOnTouchListener(this);
		myList.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				Object o = myList.getItemAtPosition(position);
				pastSearch = (PastSearchData) o;
				Toast.makeText(PastSearch.this,
						"Selected :" + " " + pastSearch, Toast.LENGTH_LONG)
						.show();

				String current_date = dateFormatter.format(new Date());
				Toast.makeText(PastSearch.this, "Today date :" + current_date,
						Toast.LENGTH_LONG).show();

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						PastSearch.this);

				// set title
				alertDialogBuilder.setTitle("Past Search");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Category: "
										+ pastSearch.getSds().getCategory()
										+ " \nSubcategory: "
										+ pastSearch.getSds().getSubcategory()
										+ " \nLocation"
										+ pastSearch.getSds().getCity()
										+ " \nCompany: "
										+ pastSearch.getSds().getCompanyName()
										+ " \nVendor: "
										+ pastSearch.getSds().getVendorName()
										+ " \nFrom: "
										+ pastSearch.getSds().getFrom()
										+ " \nTo: "
										+ pastSearch.getSds().getTo()

						)
						.setCancelable(false)
						.setPositiveButton("Search",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Need to make search api call with
										// this data.
										//Also needs to update search cache. 
										// No need to validate data as it is already validated. 

										SearchScreenDataStore ssds = SearchScreenDataStore
												.getInstance();
										ssds.setCategory(pastSearch.getSds()
												.getCategory());
										ssds.setCategoryID(pastSearch.getSds()
												.getCategoryID());
										ssds.setSubcategory(pastSearch.getSds()
												.getSubcategory());
										ssds.setSubcategoryID(pastSearch
												.getSds().getSubcategoryID());
										ssds.setFrom(pastSearch.getSds()
												.getFrom());
										ssds.setTo(pastSearch.getSds().getTo());
										ssds.setCity(pastSearch.getSds()
												.getCity());
										ssds.setCompanyName(pastSearch.getSds()
												.getCompanyName());
										ssds.setVendorName(pastSearch.getSds()
												.getVendorName());
				/*						Intent intent = new Intent(
												PastSearch.this, Homepage.class);

										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

										intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(intent);
				 */
										new SaveSearchEntry().execute(getApplicationContext(),ssds);
										
										dialog.cancel();
										
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								})
						.setNeutralButton("Modify search",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										// Need to set attributes to SearchScreendatastore object and just open Homepage. 
										SearchScreenDataStore ssds = SearchScreenDataStore
												.getInstance();
										ssds.setCategory(pastSearch.getSds()
												.getCategory());
										ssds.setCategoryID(pastSearch.getSds()
												.getCategoryID());
										ssds.setSubcategory(pastSearch.getSds()
												.getSubcategory());
										ssds.setSubcategoryID(pastSearch
												.getSds().getSubcategoryID());
										ssds.setFrom(pastSearch.getSds()
												.getFrom());
										ssds.setTo(pastSearch.getSds().getTo());
										ssds.setCity(pastSearch.getSds()
												.getCity());
										ssds.setCompanyName(pastSearch.getSds()
												.getCompanyName());
										ssds.setVendorName(pastSearch.getSds()
												.getVendorName());
										Intent intent = new Intent(
												PastSearch.this, Homepage.class);

										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

										intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(intent);

										
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}

		});

	}

	// method to expand all groups
	private void displayList() {
		// Intent intent = getIntent();

		listData = getList();
		// display the list

		// get reference to the ExpandableListView
		myList = (ListView) findViewById(R.id.past_list);
		// create the adapter by passing your ArrayList data
		listAdapter = new PastSearchHelperAdapter(PastSearch.this, listData);
		// attach the adapter to the list
		myList.setAdapter(listAdapter);

	}

	private ArrayList<PastSearchData> getList() {
		ArrayList<PastSearchData> results = new ArrayList<PastSearchData>();

		Iterator it = searchCache.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, PastSearchDataStore> pairs = (Entry<Integer, PastSearchDataStore>) it
					.next();
			PastSearchDataStore sds = pairs.getValue();
			int hash = pairs.getKey();
			PastSearchData psd = new PastSearchData();
			psd.setHash(hash);
			psd.setSds(sds);

			results.add(psd);
		}

		return results;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.past_search, menu);
		return true;
	}

	@Override
	public boolean onClose() {
		listAdapter.filterData("");
		searchView.clearFocus();
		searchQuery = null;
		return false;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		Toast.makeText(getApplicationContext(), "OnQueryTextChange: " + arg0,
				Toast.LENGTH_SHORT).show();
		listAdapter.filterData(arg0);
		searchQuery = arg0;
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(getApplicationContext(), "OnQueryTextSubmit: " + query,
				Toast.LENGTH_SHORT).show();
		listAdapter.filterData(query);
		searchView.clearFocus();
		searchQuery = query;
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Armazena a posição Y no momento em que a tela é tocada
			startY = y;
			// Permite a atualização somente quando o primeiro item da lista
			// for arrastado
			allowRefresh = true;
			// allowRefresh = (myList.getFirstVisiblePosition() == 0);
			// System.out.println("In Action Down");
			break;

		case MotionEvent.ACTION_MOVE:
			if (allowRefresh) {
				if ((y - startY) > REFRESH_THRESHOLD) {
					refresh = true;
				} else {
					refresh = false;
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			// Executa a atualização

			if (refresh && searchQuery == null) {
				Toast.makeText(getApplicationContext(),
						"Refresh needs to be performed", Toast.LENGTH_SHORT)
						.show();
				searchCache = ApplicationHashMaps.getSearchCache();
				displayList();
			}

			refresh = false;
		}

		return super.onTouchEvent(event);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.logout:
			
			SharedPreferences pref = getSharedPreferences("loginToken",MODE_PRIVATE);
			if(pref.contains("remember"))
			{
				SharedPreferences.Editor editor = pref.edit();
				editor.remove("remember");
				editor.commit();
			}
			Intent intent = new Intent(PastSearch.this,MainActivity.class);
			
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
			break;
			
		case R.id.action_settings:
			System.out.println("hello");
			break;
		}
		
		return false;
		//return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
	
	
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        // your code
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	

}
