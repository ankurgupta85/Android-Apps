package com.puzzle.n_puzzle_project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.puzzle.n_puzzle_project.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class Hard_Scores extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hard__scores);
		Context context = getApplicationContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
	//	int height = metrics.heightPixels;

		TableLayout table = (TableLayout) findViewById(R.id.easy_table_layout);

		TextView time = new TextView(this);
		time.setText("Time");
		time.setTextSize(15);
		time.setWidth(width / 3);
		TextView steps = new TextView(this);
		steps.setText("Moves");
		steps.setTextSize(15);
		steps.setWidth(width / 3);
		steps.setGravity(Gravity.CENTER);
		steps.setTypeface(null, Typeface.BOLD);

		TextView score = new TextView(this);
		score.setText("Score");
		score.setTextSize(15);
		score.setWidth(width / 3);
		time.setGravity(Gravity.CENTER);
		time.setTypeface(null, Typeface.BOLD);
		score.setTypeface(null, Typeface.BOLD);
		score.setGravity(Gravity.CENTER);
		TableRow rowHeader = new TableRow(this);
		rowHeader.addView(time);
		rowHeader.addView(steps);
		rowHeader.addView(score);

		rowHeader.setGravity(Gravity.CENTER);
		table.addView(rowHeader, new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		try {
			Sort_Records records = new Sort_Records();
			Map<Long, ArrayList<String>> map = records.sort_records("hard.txt",
					getApplicationContext());
			int i = 0;
			for (Entry<Long, ArrayList<String>> entry : map.entrySet()) {
				ArrayList<String> dataList = entry.getValue();
				for (String data : dataList) {

					i++;

					String[] dataSplit = data.split(" ");
					String time_took = dataSplit[0];
					String steps_took = dataSplit[1];
					String score_made = dataSplit[2];
					TableRow tr = new TableRow(this);
					tr.setId(100 + i);
					tr.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));

					TextView time_text = new TextView(this);
					time_text.setId(400 + i);
					time_text.setText(time_took);
					time_text.setWidth(width / 3);
					time_text.setGravity(Gravity.CENTER);

					time_text.setTextColor(Color.BLACK);
					time_text.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					tr.addView(time_text);

					TextView steps_text = new TextView(this);
					steps_text.setId(200 + i);
					steps_text.setText(steps_took);
					steps_text.setWidth(width / 3);
					steps_text.setGravity(Gravity.CENTER);

					steps_text.setTextColor(Color.BLACK);
					steps_text.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					tr.addView(steps_text);

					TextView scores_text = new TextView(this);
					scores_text.setId(300 + i);
					scores_text.setText(score_made);
					scores_text.setWidth(width / 3);
					scores_text.setGravity(Gravity.CENTER);
					scores_text.setTextColor(Color.BLACK);
					scores_text.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					tr.addView(scores_text);
					tr.setGravity(Gravity.CENTER);

					// Add the TableRow to the TableLayout
					table.addView(tr, new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));


				}
			}

		} catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hard__scores, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent =null;
	switch(item.getItemId())
	{
	case R.id.Main_Page:
		intent = new Intent(getApplicationContext(),MainPage.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return true;
	case R.id.Help:
		intent = new Intent(getApplicationContext(),Help.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return true;
	case R.id.exit_game:
		finish();
		return true;
	default:
		return super.onOptionsItemSelected(item);
		
	}
	
		
		
		
	}

}
