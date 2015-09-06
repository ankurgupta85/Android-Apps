package com.yolo.homepage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yolo.R;

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
            "MMMM dd yyyy");
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
			kpds.setProd_desc(product_desc.getText().toString());
			Toast.makeText(getApplicationContext(), "Need to implement search",
					Toast.LENGTH_SHORT).show();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(product_desc.getWindowToken(), 0);
			// Validate if user entered product details or not.
			// Store user search information with date and time in json file
			// send user search information to server
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
                    kpds.setFrom(dateFormatter.format(dateTime.getTime()));
                    fromView.setText(dateFormatter.format(dateTime.getTime()));
                    }
                    else if(id == TO_VIEW)
                    {
                    	kpds.setTo(dateFormatter.format(dateTime.getTime()));
                    	toView.setText(dateFormatter.format(dateTime.getTime()));
                        	
                    }
                }
            }, dateTime.get(Calendar.YEAR),
               dateTime.get(Calendar.MONTH),
               dateTime.get(Calendar.DAY_OF_MONTH));
				

	}


}
