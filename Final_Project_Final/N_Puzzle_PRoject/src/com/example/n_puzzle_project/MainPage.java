package com.example.n_puzzle_project;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainPage extends Activity implements OnClickListener {
	TextView continue_game = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);

		continue_game = (TextView) findViewById(R.id.continue_game);
		continue_game.setOnClickListener(this);

		TextView new_game = (TextView) findViewById(R.id.new_game);
		new_game.setOnClickListener(this);

		TextView scores = (TextView) findViewById(R.id.scores);
		scores.setOnClickListener(this);

		TextView help = (TextView) findViewById(R.id.help);
		help.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_page, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.exit_game:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {
		System.gc();

		boolean easy_pref_exist = false;
		boolean medium_pref_exist = false;
		boolean hard_pref_exist = false;
		File sharedPrefsDir = new File(getFilesDir(), "../shared_prefs");
		File[] files = sharedPrefsDir.listFiles();
		if (files != null)
			for (File file : files) {
				continue_game.setEnabled(true);
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

		if (easy_pref_exist || medium_pref_exist || hard_pref_exist) {

			continue_game.setEnabled(true);

		} else {
			continue_game.setEnabled(false);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.continue_game:
			Intent continue_game = new Intent(this, Continue_Game.class);
			startActivity(continue_game);

			break;
		case R.id.new_game:
			Intent new_game = new Intent(this, Select_Game_Type.class);
			startActivity(new_game);
			break;
		case R.id.scores:
			Intent scores = new Intent(this, Scores_Display.class);
			startActivity(scores);
			break;
		case R.id.help:
			Intent help = new Intent(this, Help.class);
			startActivity(help);
			break;

		}

	}

}
