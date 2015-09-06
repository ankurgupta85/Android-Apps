package com.yolo.homepage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.yolo.R;
import com.yolo.datastore.SearchScreenDataStore;
import com.yolo.mainscreen.MainActivity;
import com.yolo.util.ApplicationHashMaps;

public class SearchScreen extends Activity implements OnClickListener,
		OnEditorActionListener {
	TextView hello = null;
	String category = null;
	int categoryID = 0;
	String subcategory = null;
	int subcategoryID = 0;
	String dateSelected = null;
	String vendor_name = null;
	// EditText vendorView =null;
	AutoCompleteTextView vendorView = null;
	String from = null;
	String to = null;
	String type = null;
	String city = null;
	TextView fromView;
	TextView toView;
	TextView locationView;
	String company = null;
	// EditText company_name =null;
	AutoCompleteTextView company_name = null;

	String state = null;
	TextView sub;
	TextView readDownloadedFile = null;
	Button search = null;
	SearchScreenDataStore ssds = SearchScreenDataStore.getInstance();
	private static final int FROM_VIEW = 1;
	private static final int TO_VIEW = 2;
	private Calendar dateTime = Calendar.getInstance();
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	CompaniesInfoParser companyParser = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_screen);

		TextView hello = (TextView) findViewById(R.id.categories);
		hello.setOnClickListener(this);
		sub = (TextView) findViewById(R.id.subcategories);
		sub.setOnClickListener(this);
		readDownloadedFile = (TextView) findViewById(R.id.textView1);
		readDownloadedFile.setOnClickListener(this);
		company_name = (AutoCompleteTextView) findViewById(R.id.company);
		companyParser = CompaniesInfoParser.getInstance();
		ArrayList<String> company_list = companyParser.getCompaniesList();

		String[] companies = new String[company_list.size()];
		companies = company_list.toArray(companies);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, companies);
		company_name.setAdapter(adapter);

		// company_name = (EditText)findViewById(R.id.company);
		// company_name.setOnClickListener(this);
		search = (Button) findViewById(R.id.search_button);
		search.setOnClickListener(this);
		fromView = (TextView) findViewById(R.id.FROM);
		fromView.setOnClickListener(this);
		toView = (TextView) findViewById(R.id.tO);
		locationView = (TextView) findViewById(R.id.location);
		locationView.setOnClickListener(this);

		vendorView = (AutoCompleteTextView) findViewById(R.id.vendor_name);
		// vendorView.setOnEditorActionListener(this);
		ArrayList<String> vendor_list = companyParser.getVendorsList();
		String[] vendors = new String[vendor_list.size()];
		vendors = vendor_list.toArray(vendors);
		ArrayAdapter<String> vendorAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, vendors);
		vendorView.setAdapter(vendorAdapter);

		toView.setOnClickListener(this);

		to = ssds.getTo();
		from = ssds.getFrom();
		city = ssds.getCity();
		state = ssds.getState();
		company = ssds.getCompanyName();
		category = ssds.getCategory();
		categoryID = ssds.getCategoryID();
		subcategory = ssds.getSubcategory();
		subcategoryID = ssds.getSubcategoryID();
		vendor_name = ssds.getVendorName();
		if (vendor_name != null && !vendor_name.isEmpty()) {
			vendorView.setText(vendor_name);
		}
		if (company != null && !company.isEmpty()) {
			company_name.setText(company);
		}

		if (city != null) {
			locationView.setText(city);
		}
		if (from != null) {
			fromView.setText(from);
		}
		if (to != null) {

			toView.setText(to);
		}
		if (category != null && !category.isEmpty()) {
			hello.setText(category);
		}

		if (subcategory != null && !subcategory.isEmpty()) {
			sub.setText(subcategory);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		company = company_name.getText().toString();
		vendor_name = vendorView.getText().toString();
		ssds.setVendorName(vendor_name);
		ssds.setCompanyName(company);

		switch (v.getId()) {
		case R.id.categories:
			Intent intent = new Intent(getApplicationContext(),
					SearchSelectorHelper.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("category", true);

			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(intent);

			break;
		case R.id.subcategories:
			if (categoryID == 0) {
				Toast.makeText(getApplicationContext(),
						"Please select category first", Toast.LENGTH_SHORT)
						.show();

			} else {

				Intent intent1 = new Intent(getApplicationContext(),
						SearchSelectorHelper.class);
				intent1.putExtra("category", false);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent1);
			}
			break;

		case R.id.FROM:
			showDialog(FROM_VIEW);

			break;
		case R.id.tO:
			showDialog(TO_VIEW);
			break;

		case R.id.location:

			Intent intent1 = new Intent(getApplicationContext(),
					LocationPicker.class);

			intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivityForResult(intent1, RESULT_OK);
			break;

		case R.id.search_button:
			// Toast.makeText(getApplicationContext(),
			// "Need to implement search validation",
			// Toast.LENGTH_SHORT).show();
			showAlertBox();
			boolean validAllFields = validateAllFields();
			boolean validDateFormat = false;
			boolean validDateSelection = false;
			if (!validAllFields) {
				Toast.makeText(getApplicationContext(),
						"All fields are mandatory", Toast.LENGTH_SHORT).show();
			} else {
				validDateFormat = validateDateFormat();

				if (validDateFormat) {
					validDateSelection = validateDateSelection();

				}
			}
			if (validAllFields && validDateFormat && validDateSelection) {

				Toast.makeText(getApplicationContext(),
						"All validation completed", Toast.LENGTH_SHORT).show();
				// Need to start a task with values to enter or update cache.
				System.out.println(ssds.toString());
				
				
				//new DownloadFilter().execute(getApplicationContext(), ssds);
				
				
				new SaveSearchEntry().execute(getApplicationContext(), ssds);

				System.out.println(ApplicationHashMaps.getSearchCache()
						.toString());

				HashMap<String, Integer> companiesHash = companyParser
						.getCompaniesHashMap();

				if (companiesHash.containsKey(company)) {
					Toast.makeText(
							getApplicationContext(),
							"Selected Company: " + company + " ID: "
									+ companiesHash.get(company),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Company not selected from the file",
							Toast.LENGTH_SHORT).show();

				}
				HashMap<String, Integer> vendorsHash = companyParser
						.getVendorsHashMap();
				if (vendorsHash.containsKey(vendor_name)) {
					Toast.makeText(
							getApplicationContext(),
							"Selected Vendor: " + vendor_name + " ID: "
									+ vendorsHash.get(vendor_name),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Vendor not selected from the file",
							Toast.LENGTH_SHORT).show();

				}
				
	
				// Show alert box asking user if he wants to filter records or not. 
				//showAlertBox();
				

			} else {
				Toast.makeText(getApplicationContext(), "Validation failed",
						Toast.LENGTH_SHORT).show();
			}

			break;
		}

	}

	
	
	private void showAlertBox()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(SearchScreen.this);
		dialog.setCancelable(true);
		dialog.setTitle("Search Result Option");
		dialog.setMessage("Do you wish to apply filters to search results!!")
		.setNeutralButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Selected No", Toast.LENGTH_SHORT).show();
				// Need to make API call with no filters. 
				
				
			}
		})
		.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Selected Yes", Toast.LENGTH_SHORT).show();
			// Need to start activity with all the downloaded filters.
				new DownloadFilter().execute(getApplicationContext(), ssds.getSubcategoryID(),ssds.getCompanyName());
				
				FilterParser filterParser = FilterParser.getInstance();
				filterParser.startLoading(getApplicationContext());
				Intent intent = new Intent(getApplicationContext(),Filters.class);
				intent.putExtra("subcategory", ssds.getSubcategoryID());
				intent.putExtra("companyName", ssds.getCompanyName());
				startActivity(intent);
			}
		});
		
		
		AlertDialog alert = dialog.create();
		alert.show();
		
		
		
	}
	
	
	
	private boolean validateDateSelection() {
		boolean valid = true;
		Date toDate = null;
		Date fromDate = null;
		try {
			toDate = dateFormatter.parse(this.to);
			fromDate = dateFormatter.parse(this.from);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("From date: " + fromDate.toString());
		System.out.println("To date: " + toDate.toString());
		if (fromDate.compareTo(toDate) > 0) {
			Toast.makeText(getApplicationContext(),
					"From date should be before To date", Toast.LENGTH_SHORT)
					.show();
			valid = false;
		}
		return valid;
	}

	private boolean validateDateFormat() {
		boolean valid = true;
		if (this.to == "To" || this.to == null || this.to.length() != 10) {
			Toast.makeText(getApplicationContext(), "Please select To date",
					Toast.LENGTH_SHORT).show();
			valid = false;
		}
		if (this.from == "From" || this.from == null
				|| this.from.length() != 10) {
			Toast.makeText(getApplicationContext(), "Please select from date",
					Toast.LENGTH_SHORT).show();
			valid = false;
		}

		return valid;
	}

	private boolean validateAllFields() {
		boolean valid = true;

		if (this.to == null || this.from == null || this.category == null
				|| this.subcategory == null || this.city == null) {
			valid = false;
		}

		return valid;

	}

	@Override
	protected void onResume() {

		to = ssds.getTo();
		from = ssds.getFrom();
		city = ssds.getCity();
		state = ssds.getState();
		company = ssds.getCompanyName();
		category = ssds.getCategory();
		categoryID = ssds.getCategoryID();
		subcategory = ssds.getSubcategory();
		subcategoryID = ssds.getSubcategoryID();
		/*
		 * city = intent.getStringExtra("city"); state =
		 * intent.getStringExtra("state"); company =
		 * intent.getStringExtra("company");
		 */
		if (company != null && !company.isEmpty()) {
			company_name.setText(company);
		}

		if (city != null) {
			locationView.setText(city);
		}
		if (from != null) {
			fromView.setText(from);
		}
		if (to != null) {

			toView.setText(to);
		}
		
		if (hello == null) {
			hello = (TextView) findViewById(R.id.categories);
		}
		if (category != null && !category.isEmpty()) {

			hello.setText(category);
		} else {
			hello.setText(R.string.categories);
		}

		if (sub == null) {
			sub = (TextView) findViewById(R.id.subcategories);

		}
		if (subcategory != null && !subcategory.isEmpty()) {

			sub.setText(subcategory);

		} else {
			sub.setText(R.string.subcategories);
		}

		super.onResume();
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(final int id) {

		return new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				dateTime.set(year, monthOfYear, dayOfMonth);
				if (id == FROM_VIEW) {
					from = dateFormatter.format(dateTime.getTime());
					ssds.setFrom(from);
					fromView.setText(from);

				} else if (id == TO_VIEW) {
					to = dateFormatter.format(dateTime.getTime());
					ssds.setTo(to);
					toView.setText(to);

				}
				// txtAdddate.setText(dateFormatter.format(dateTime.getTime()));
			}
		}, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH),
				dateTime.get(Calendar.DAY_OF_MONTH));

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		v.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView exampleView, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						|| actionId == EditorInfo.IME_NULL
						|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

					// Do something in here
					return true;
				} else {
					return false;
				}
			}
		});
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout:

			SharedPreferences pref = getSharedPreferences("loginToken",
					MODE_PRIVATE);
			if (pref.contains("remember")) {
				SharedPreferences.Editor editor = pref.edit();
				editor.remove("remember");
				editor.commit();
			}
			Intent intent = new Intent(this, MainActivity.class);

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
		// return super.onOptionsItemSelected(item);
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
