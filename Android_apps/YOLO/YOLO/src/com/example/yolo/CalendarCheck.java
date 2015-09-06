package com.example.yolo;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class CalendarCheck extends Activity {

	Button change_date_but;
	TextView display_txt;
	public static final int Date_dialog_id = 1;
	// date and time
	private int mYear;
	private int mMonth;
	private int mDay;
	String from = null;
	String to = null;
	Intent intent = null;
	String askFor = null;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_calendar);
		intent = getIntent();
		askFor = intent.getStringExtra("askFor");
		to = intent.getStringExtra("to");
		from = intent.getStringExtra("from");

		// change_date_but = (Button) findViewById(R.id.change_button_id);
	//	display_txt = (TextView) findViewById(R.id.display_id);
		// change_date_but = (Button) findViewById(R.id.change_button_id);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog DPD = new DatePickerDialog(CalendarCheck.this,
				mDateSetListener, mYear, mMonth, mDay);
		DPD.show();

		/*
		 * @Override public void onClick(View v) { DatePickerDialog DPD = new
		 * DatePickerDialog( CalendarCheck.this, mDateSetListener, mYear,
		 * mMonth, mDay); DPD.show(); } });
		 */

		// updateDisplay();
	}

	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);

		((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private void updateDisplay() {
		// TODO Auto-generated method stub
		String dateSelected = new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).toString();

		Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
		intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent1.putExtra("category", intent.getStringExtra("category"));
		intent1.putExtra("categoryID", intent.getIntExtra("categoryID", 0));
		intent1.putExtra("subcategory", intent.getStringExtra("subcategory"));
		intent1.putExtra("subcategoryID",
				intent.getIntExtra("subcategoryID", 0));
		intent1.putExtra("to", to);
		intent1.putExtra("from", from);
		intent1.putExtra(askFor, dateSelected);

		intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent1);
	}

}