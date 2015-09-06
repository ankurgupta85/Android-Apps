package com.yolo.homepage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yolo.R;

public class SearchScreen extends Activity implements OnClickListener {
	TextView hello = null;
	String category = null;
	int categoryID = 0;
	String subcategory = null;
	int subcategoryID = 0;
	String dateSelected = null;
	String from = null;
	String to = null;
	String type = null;
	String city =null;
	TextView fromView;
	TextView toView;
	TextView locationView;
	String company=null;
	EditText company_name =null;
	String state=null;
	TextView sub ;
	Button search = null;
	SearchScreenDataStore ssds = SearchScreenDataStore.getInstance();
	private static final int FROM_VIEW = 1;
	private static final int TO_VIEW = 2;
	private Calendar dateTime = Calendar.getInstance();
     private SimpleDateFormat dateFormatter = new SimpleDateFormat(
             "MMMM dd yyyy");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_screen);
		TextView hello = (TextView) findViewById(R.id.categories);
		hello.setOnClickListener(this);
		sub = (TextView) findViewById(R.id.subcategories);
		sub.setOnClickListener(this);
		company_name = (EditText)findViewById(R.id.company);
		search =(Button) findViewById(R.id.search_button);
		search.setOnClickListener(this);
		fromView = (TextView) findViewById(R.id.FROM);
		fromView.setOnClickListener(this);
		toView = (TextView) findViewById(R.id.tO);
		locationView = (TextView) findViewById(R.id.location);
		locationView.setOnClickListener(this);
		
		toView.setOnClickListener(this);
	/*	Intent intent = getIntent();
	*/	/*to = intent.getStringExtra("to");
		from = intent.getStringExtra("from");
		*/
		to = ssds.getTo();
		from = ssds.getFrom();
		city = ssds.getCity();
		state = ssds.getState();
		company = ssds.getCompanyName();
		category = ssds.getCategory();
		categoryID = ssds.getCategoryID();
		subcategory = ssds.getSubcategory();
		subcategoryID = ssds.getSubcategoryID();
	/*	city = intent.getStringExtra("city");
		state = intent.getStringExtra("state");
		company = intent.getStringExtra("company");
	*/	
		if( company!=null && !company.isEmpty())
		{
			company_name.setText(company);
		}
	
		if(city!=null)
		{
			locationView.setText(city);
		}
		if (from != null) {
			fromView.setText(from);
		}
		if (to != null) {

			toView.setText(to);
		}
		/*category = intent.getStringExtra("category");
		categoryID = intent.getIntExtra("categoryID", 0);
		subcategory = intent.getStringExtra("subcategory");
		subcategoryID = intent.getIntExtra("subcategoryID", 0);*/
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
		ssds.setCompanyName(company);
		switch (v.getId()) {
		case R.id.categories:
			Intent intent = new Intent(getApplicationContext(),
					SelectorHelper.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

/*			intent.putExtra("to", to);
			intent.putExtra("city", city);
			intent.putExtra("state", state);
			intent.putExtra("company", company);
			intent.putExtra("from",from);
			*/startActivity(intent);

			break;
		case R.id.subcategories:
			if (categoryID == 0) {
				Toast.makeText(getApplicationContext(),
						"Please select category first", Toast.LENGTH_SHORT)
						.show();

			} else {

				Intent intent1 = new Intent(getApplicationContext(),
						SelectorHelper.class);
			/*	intent1.putExtra("category", category);
				intent1.putExtra("to", to);
				intent1.putExtra("from", from);
				intent1.putExtra("categoryID", categoryID);
				intent1.putExtra("city", city);
				intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent1.putExtra("state", state);
				intent1.putExtra("company", company);
			*/			
				intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent1);
			}
			break;

		case R.id.FROM:
			/*Intent fromIntent = new Intent(getApplicationContext(),
					CalendarCheck.class);
			fromIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			fromIntent.putExtra("category", category);
			fromIntent.putExtra("categoryID", categoryID);
			fromIntent.putExtra("subcategory", subcategory);
			fromIntent.putExtra("subcategoryID", subcategoryID);
			fromIntent.putExtra("to", to);
			fromIntent.putExtra("askFor", "from");

			fromIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			fromIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(fromIntent);
			break;
*/
			showDialog(FROM_VIEW);
			
			break;
		case R.id.tO:
/*			Intent toIntent = new Intent(getApplicationContext(),
					CalendarCheck.class);
			toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			toIntent.putExtra("category", category);
			toIntent.putExtra("categoryID", categoryID);
			toIntent.putExtra("subcategory", subcategory);
			toIntent.putExtra("subcategoryID", subcategoryID);
			toIntent.putExtra("from", from);
			toIntent.putExtra("askFor", "to");

			toIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(toIntent);*/
			showDialog(TO_VIEW);
			break;

		case R.id.location:

			Intent intent1 = new Intent(getApplicationContext(),
					LocationPicker.class);
			/*intent1.putExtra("category", category);
			intent1.putExtra("to", to);
			intent1.putExtra("from", from);
			intent1.putExtra("categoryID", categoryID);
			intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent1.putExtra("subcategory", subcategory);
			intent1.putExtra("subcategoryID", subcategoryID);
			intent1.putExtra("company", company);
			*/
			intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivityForResult(intent1,RESULT_OK);
			break;
			
		case R.id.search_button:
			Toast.makeText(getApplicationContext(), "Need to implement search validation", Toast.LENGTH_SHORT).show();
			// Validate if user selected all the fields before pressing enter.
			// Validate from and to date. 
			// Validate if user enter company name or not. 
			// Store user search information with date and time in json file
			// send user search information to server
			
			break;
		}

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
	/*	city = intent.getStringExtra("city");
		state = intent.getStringExtra("state");
		company = intent.getStringExtra("company");
	*/	
		if( company!=null && !company.isEmpty())
		{
			company_name.setText(company);
		}
	
		if(city!=null)
		{
			locationView.setText(city);
		}
		if (from != null) {
			fromView.setText(from);
		}
		if (to != null) {

			toView.setText(to);
		}
		/*category = intent.getStringExtra("category");
		categoryID = intent.getIntExtra("categoryID", 0);
		subcategory = intent.getStringExtra("subcategory");
		subcategoryID = intent.getIntExtra("subcategoryID", 0);*/
		if (category != null && !category.isEmpty()) {
			if(hello == null)
			{
				hello =(TextView) findViewById(R.id.categories);
			}
			hello.setText(category);
		}

		if (subcategory != null && !subcategory.isEmpty()) {
			if(sub==null)
			{
				sub=(TextView) findViewById(R.id.subcategories);
				
			}
			sub.setText(subcategory);
			
		}

		super.onResume();
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(final int id) {
		
			return new DatePickerDialog(this,  new OnDateSetListener()
            {

                @Override
                public void onDateSet(DatePicker view, int year,
                        int monthOfYear, int dayOfMonth)
                {
                    dateTime.set(year, monthOfYear, dayOfMonth);
                    if(id == FROM_VIEW)
                    {
                    	ssds.setFrom(dateFormatter.format(dateTime.getTime()));
                    fromView.setText(dateFormatter.format(dateTime.getTime()));
                    }
                    else if(id == TO_VIEW)
                    {
                    	ssds.setTo(dateFormatter.format(dateTime.getTime()));
                    	toView.setText(dateFormatter.format(dateTime.getTime()));
                        	
                    }
                    // txtAdddate.setText(dateFormatter.format(dateTime.getTime()));
                }
            }, dateTime.get(Calendar.YEAR),
               dateTime.get(Calendar.MONTH),
               dateTime.get(Calendar.DAY_OF_MONTH));
				

	}
}
