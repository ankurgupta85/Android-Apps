package com.yolo.homepage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.example.yolo.R;
import com.yolo.mainscreen.MainActivity;
import com.yolo.util.ApplicationHashMaps;

public class Filters extends Activity implements OnClickListener {
	// Main filter map whcih contains the key and value pairs
	HashMap<String, ArrayList<String>> filterMap = null;

	// Map to contain the ID of filter and key of filter.
	HashMap<Integer, String> filtersIndexMap = new HashMap<Integer, String>();

	// This will be used when user wants to change values for already selected
	// values in one of the filters.
	HashMap<Integer, ArrayList<Integer>> filtersSelectedMap = new HashMap<Integer, ArrayList<Integer>>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_filters);
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		
		FilterParser fp = FilterParser.getInstance();
		int subID = getIntent().getIntExtra("subcategory", 0);
		String companyName = getIntent().getStringExtra("companyName");
		if (fp.getFilters().size() == 0) {
			new LoadFilters().execute(subID, companyName);
		}

		filterMap = fp.getFilters();
		System.out.println(filterMap);
		// Need to find structure to display Filters.
		TableLayout table = (TableLayout) findViewById(R.id.filters);
		TableRow row = new TableRow(this);
		/*TableLayout.LayoutParams tableRowParams=
		  new TableLayout.LayoutParams
		  (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

		int leftMargin=10;
		int topMargin=2;
		int rightMargin=10;
		int bottomMargin=2;

		tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
*/
		Iterator iter = filterMap.entrySet().iterator();
		int i = 1;
		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry) iter.next();
			String key = (String) pairs.getKey();
			ArrayList<String> values = (ArrayList<String>) pairs.getValue();
			System.out.println("Key: " + key);
			System.out.println("Values: " + values);

			TextView button = new TextView(this);
			button.setId(i);

			filtersIndexMap.put(i, key);
			button.setText(key);
			button.setTextSize(15);
			button.setSingleLine(false);
			//button.setMaxWidth(30);
			button.setMaxEms(10);
			button.setWidth(width/2);
			button.setGravity(Gravity.CENTER);
			//button.setLayoutParams(params);
			button.setPaddingRelative(10, 50, 10, 50);
			//button.setContentDescription(key);
			button.setOnClickListener(this);
			// button.setOnItemClickListener(this);
			row.addView(button);

			if ((filterMap.size() % 2) != 0 && i==filterMap.size() ) {
					TextView tempButton = new TextView(this);
					tempButton.setVisibility(View.GONE);
				//	tempButton.setLayoutParams(new LayoutParams(400, 400));
					
					tempButton.setText("");
					row.addView(tempButton);
					//row.setLayoutParams(tableRowParams);
					table.addView(row, new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					
				}
			

			else {
				if ((i % 2) == 0) {

					row.setGravity(Gravity.CENTER);
					
					//row.setLayoutParams(tableRowParams);
					table.addView(row, new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));

					row = new TableRow(this);
				}

			}

			i++;

		}

	}

	@Override
	public void onClick(View v) {

		final TextView buttonSelected = (TextView) v;
		final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
		final int viewID = v.getId();
		String filterSelected = filtersIndexMap.get(viewID);
		final ArrayList<String> lables = filterMap.get(filterSelected);
		String[] filterValues = new String[lables.size()];
		filterValues = lables.toArray(filterValues);
		boolean[] checkedArray = new boolean[lables.size()];
		if (filtersSelectedMap.containsKey(viewID)) {
			ArrayList<Integer> indexes = filtersSelectedMap.get(viewID);
			for (int i = 0; i < indexes.size(); i++) {
				checkedArray[indexes.get(i)] = true;
				mSelectedItems.add(indexes.get(i));
			}

		} else {
			checkedArray = null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(Filters.this);
		// Set the dialog title
		builder.setTitle(filterSelected)
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setMultiChoiceItems(filterValues, checkedArray,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									// If the user checked the item, add it to
									// the selected items

									mSelectedItems.add(which);
									System.out.println("Selected:" + which);
									System.out.println("Selected:"
											+ lables.get(which));
								} else if (mSelectedItems.contains(which)) {
									// Else, if the item is already in the
									// array, remove it
									mSelectedItems.remove(Integer
											.valueOf(which));
								}
							}
						})
				// Set the action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the mSelectedItems results
						// somewhere
						// or return them to the component that opened the
						// dialog
						if (mSelectedItems.size() == 0) {
							if(filtersSelectedMap.containsKey(viewID))
							{
								filtersSelectedMap.remove(viewID);
							}
							buttonSelected.setText(filtersIndexMap.get(viewID));
						} else {
							StringBuffer buffer = new StringBuffer();
							filtersSelectedMap.put(viewID, mSelectedItems);

							for (int i = 0; i < mSelectedItems.size(); i++) {
								buffer.append(lables.get(mSelectedItems.get(i)));
								if (i != mSelectedItems.size() - 1) {
									buffer.append(",");

								}

							}

							buttonSelected.setText(buffer.toString());
						}

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog dialog = builder.create();
		dialog.show();

	}


	@Override
	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(Filters.this);
		// Set the dialog title
		builder.setMessage("Going back will clear filters. Do you wish to continue?");
		builder.setTitle("Change search")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the mSelectedItems results
						// somewhere
						// or return them to the component that opened the
						// dialog
						Intent intent = new Intent(Filters.this, Homepage.class);

						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
								
					}

						
					
				})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog dialog = builder.create();
		dialog.show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
	
		
		
		}

		return super.onKeyDown(keyCode, event);
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filters, menu);
		return true;
	}

	private class LoadFilters extends AsyncTask<Object, Object, Object> {

		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getApplicationContext());
			dialog.setMessage("Loading filters...");
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... params) {
			int subID = (Integer) params[0];
			String companyName = (String) params[1];
			HashMap<String, String> hm = ApplicationHashMaps.getFileCache();
			String filterFile = hm.get("filters");
			File file = getFileStreamPath(filterFile);
			if (!file.exists()) {
				new DownloadFilter().execute(getApplicationContext(), subID,
						companyName);
			}
			FilterParser fp = FilterParser.getInstance();
			fp.startLoading(getApplicationContext());

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.cancel();
			super.onPostExecute(result);

		}
	}

}
