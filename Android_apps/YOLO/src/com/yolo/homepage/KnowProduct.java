package com.yolo.homepage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yolo.mainscreen.MainActivity;
import com.example.yolo.R;
import com.yolo.datastore.KnowProductDataStore;

public class KnowProduct extends Activity implements OnClickListener {
	EditText product_desc = null;
	String from = null;
	String to = null;
	TextView fromView;
	TextView toView;
	private static final int FROM_VIEW = 1;
	private static final int TO_VIEW = 2;
	private Calendar dateTime = Calendar.getInstance();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "yyyy-MM-dd");
    KnowProductDataStore kpds = KnowProductDataStore.getInstance();
    String description = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_know_product);
		product_desc = (EditText) findViewById(R.id.product_desc);
		Button clear = (Button) findViewById(R.id.clear);
		Button search = (Button) findViewById(R.id.submit);
		clear.setOnClickListener(this);
		search.setOnClickListener(this);
		fromView = (TextView) findViewById(R.id.from_date);
		fromView.setOnClickListener(this);
		toView = (TextView) findViewById(R.id.to_date);


		toView.setOnClickListener(this);
		to = kpds.getTo();
		from = kpds.getFrom();
		description= kpds.getProd_desc();
		if (to != null) {

			toView.setText(to);
		}

		if (from != null) {

			fromView.setText(from);
		}
		if (description!= null) {

			product_desc.setText(description);
		}

		product_desc
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView exampleView,
							int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE
								|| actionId == EditorInfo.IME_NULL
								|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
							product_desc.getWindowToken(), 0);

							// Do something in here
							return true;
						} else {
							return false;
						}
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.know_product, menu);
		return true;
	}

	
	@Override
	protected void onResume() {
	
		to = kpds.getTo();
		from = kpds.getFrom();
		description= kpds.getProd_desc();
		if (to != null) {

			toView.setText(to);
		}

		if (from != null) {

			fromView.setText(from);
		}
		if (description!= null) {

			product_desc.setText(description);
		}


		
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear: {
			product_desc.setText("");
			toView.setText("To");
			fromView.setText("From");
			
			kpds.setTo("To");
			kpds.setFrom("From");
			break;
		}
		case R.id.submit: {
			
			description = product_desc.getText().toString().trim();
			kpds.setProd_desc(description);
			kpds.setFrom(from);
			kpds.setTo(to);
	/*		Toast.makeText(getApplicationContext(), "Need to implement search",
					Toast.LENGTH_SHORT).show();
	*/		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(product_desc.getWindowToken(), 0);
			// Validate if user entered product details or not.
			// Store user search information with date and time in json file
			// send user search information to server
			boolean validAllFields = validateAllFields();
			boolean validDateFormat = false;
			boolean validDateSelection = false;
			
			if(!validAllFields)
			{
				Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
			}
			else
			{
			 validDateFormat = validateDateFormat();
			
			if(validDateFormat)
			{
				validDateSelection = validateDateSelection();
			
			}	
			}
		if(validAllFields && validDateFormat && validDateSelection)
		{
			Toast.makeText(getApplicationContext(), "All validation completed", Toast.LENGTH_SHORT).show();
			
		
		}

			
			
			
			break;
		}
		case R.id.from_date: {
			showDialog(FROM_VIEW);
			break;

		}
		case R.id.to_date: {
			showDialog(TO_VIEW);
			break;

		}

		}

	}
	
	

	private boolean  validateDateSelection()
	{
		boolean valid = true;
		Date toDate =null;
		Date fromDate =null;
		try {
			toDate = dateFormatter.parse(this.to);
			fromDate = dateFormatter.parse(this.from);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("From date: "+fromDate.toString());
		System.out.println("To date: "+toDate.toString());
		if(fromDate.compareTo(toDate)>0)
		{
			Toast.makeText(getApplicationContext(), "From date should be before To date", Toast.LENGTH_SHORT).show();
			valid = false;
		}
		return valid;
	}
	
	private boolean validateDateFormat()
	{
		boolean valid = true;
		if(this.to=="To" || this.to == null || this.to.length()!=10)
		{
			Toast.makeText(getApplicationContext(), "Please select To date", Toast.LENGTH_SHORT).show();
			valid = false;
		}
		if(this.from =="From" || this.from == null || this.from.length()!=10)
		{
			Toast.makeText(getApplicationContext(), "Please select from date", Toast.LENGTH_SHORT).show();
			valid=false;
		}
		
		
		
		return valid;
	}
	
	private boolean validateAllFields()
	{
		boolean valid = true;
		
		if(this.to==null || this.from== null || this.description== null || this.description.trim().isEmpty())
		{
			valid = false;
		}
		
		return valid;
		
		
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
                    	from = dateFormatter.format(dateTime.getTime());
                    kpds.setFrom(from);
                    fromView.setText(from);
                    }
                    else if(id == TO_VIEW)
                    {
                    	to = dateFormatter.format(dateTime.getTime());
                        
                    	kpds.setTo(to);
                    	toView.setText(to);
                        	
                    }
                }
            }, dateTime.get(Calendar.YEAR),
               dateTime.get(Calendar.MONTH),
               dateTime.get(Calendar.DAY_OF_MONTH));
				

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
			Intent intent = new Intent(KnowProduct.this,MainActivity.class);
			
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
