package com.puzzle.n_puzzle_project;

import java.io.File;
import java.util.StringTokenizer;

import com.puzzle.n_puzzle_project.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Continue_Game extends Activity implements OnClickListener {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_continue__game);
		TextView easy_continue = (TextView) findViewById(R.id.easy_level_continue);
		TextView medium_continue = (TextView) findViewById(R.id.medium_level_continue);
		TextView hard_continue = (TextView) findViewById(R.id.hard_level_continue);
		boolean easy_pref_exist = false;
		boolean medium_pref_exist = false;
		boolean hard_pref_exist = false;
		File sharedPrefsDir = new File(getFilesDir(), "../shared_prefs");
		File[] files = sharedPrefsDir.listFiles();
		for (File file : files) {
			if (file.getName().equals("easy.xml")) {

				easy_pref_exist = true;
			}
			if (file.getName().equals("medium.xml")) {

				medium_pref_exist = true;
			}

			if (file.getName().equals("hard.xml")) {

				hard_pref_exist = true;
			}

		}
		SharedPreferences pref = null;
		long time_left = 0;

		if (!easy_pref_exist) {

			easy_continue.setEnabled(false);

		} else {
			pref = getSharedPreferences("easy", MODE_PRIVATE);
			time_left = pref.getLong("time_left", 0);
			if (time_left <= 0) {
				easy_continue.setEnabled(false);
			}

		}

		if (!medium_pref_exist) {
			medium_continue.setEnabled(false);

		} else {
			pref = getSharedPreferences("medium", MODE_PRIVATE);
			time_left = pref.getLong("time_left", 0);
			if (time_left <= 0) {
				medium_continue.setEnabled(false);
			}

		}

		if (!hard_pref_exist) {
			hard_continue.setEnabled(false);

		} else {
			pref = getSharedPreferences("hard", MODE_PRIVATE);
			time_left = pref.getLong("time_left", 0);
			if (time_left <= 0) {
				hard_continue.setEnabled(false);
			}

		}

		easy_continue.setOnClickListener(this);
		medium_continue.setOnClickListener(this);
		hard_continue.setOnClickListener(this);

	}
/*
	private void saveTempData() {
		SharedPreferences pref = getSharedPreferences(file_name, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong("time_left", this.remainingTime);

		editor.putInt("moves_count", this.moves_count);
		editor.putInt("no_rows", no_rows);
		editor.putInt("no_cols", no_cols);
		editor.putString("file_name", file_name);
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < input.length; i++) {
			str.append(input[i]).append(",");
		}
		editor.putString("input_array", str.toString());
		editor.commit();
		pref = getSharedPreferences("gameData", MODE_PRIVATE);
		editor = pref.edit();
		editor.putInt("saved", 1);
		editor.commit();
		System.gc();

	}
*/
	
	@Override
	public void onBackPressed() {

		//saveTempData();

		Intent game = new Intent(getApplicationContext(),
				Select_Game_Type.class);
		game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(game);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.continue__game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.Main_Page:
			intent = new Intent(getApplicationContext(), MainPage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.Help:
			intent = new Intent(getApplicationContext(), Help.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.Scores:
			intent = new Intent(getApplicationContext(), Scores_Display.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.exit_game:
			finish();
			return true;
		case R.id.Different_Game:
			intent = new Intent(getApplicationContext(), Select_Game_Type.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}

	}

	@Override
	public void onClick(View v) {
		SharedPreferences pref = null;
		Intent game = new Intent(this, Game.class);
		int no_rows = 0;
		int no_cols = 0;
		switch (v.getId()) {
		case R.id.easy_level_continue:
			pref = getSharedPreferences("easy", MODE_PRIVATE);

			break;
		case R.id.medium_level_continue:

			pref = getSharedPreferences("medium", MODE_PRIVATE);

			break;
		case R.id.hard_level_continue:
			pref = getSharedPreferences("hard", MODE_PRIVATE);

			break;
		}

		game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		game.putExtra("remaining_time", pref.getLong("time_left", 0));
		no_rows = pref.getInt("no_rows", 0);
		no_cols = pref.getInt("no_cols", 0);
		game.putExtra("no_rows", no_rows);
		game.putExtra("no_cols", no_cols);
		game.putExtra("file_name", pref.getString("file_name", ""));
		game.putExtra("moves_count", pref.getInt("moves_count", 0));

		String savedString = pref.getString("input_array", "");
		StringTokenizer st = new StringTokenizer(savedString, ",");
		int[] savedList = new int[no_rows * no_cols];
		for (int i = 0; i < no_rows * no_cols; i++) {
			savedList[i] = Integer.parseInt(st.nextToken());
		}
		game.putExtra("inputArray", savedList);
		startActivity(game);

		// this.showMessage(text);

	}

	/*
	 * private void showMessage(String text) {
	 * Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show(); }
	 */

}