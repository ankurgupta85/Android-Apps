package com.example.n_puzzle_project;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

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

		TextView exit_game = (TextView) findViewById(R.id.exit_game);
		exit_game.setOnClickListener(this);

		/*
		 * TextView create_file= (TextView)findViewById(R.id.create_file);
		 * create_file.setOnClickListener(this);
		 */
		TextView delete_file = (TextView) findViewById(R.id.delete_file);
		delete_file.setOnClickListener(this);

		/*
		 * TextView display_image= (TextView)findViewById(R.id.display_image);
		 * display_image.setOnClickListener(this);
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_page, menu);
		return true;
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

		String text = "Nothing pressed";
		switch (v.getId()) {
		case R.id.continue_game:
			text = "Continue Game";
			Intent continue_game = new Intent(this, Continue_Game.class);
			startActivity(continue_game);

			break;
		case R.id.new_game:
			text = "New Game";
			try {
				this.checkAndCreateFiles();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent new_game = new Intent(this, Select_Game_Type.class);
			// Intent new_game = new Intent(this, Game_type_selection.class);
			startActivity(new_game);
			break;
		case R.id.scores:
			text = "Scores";
			Intent scores = new Intent(this, Scores_Display.class);
			startActivity(scores);
			break;
		case R.id.help:
			text = "Help";
			Intent help = new Intent(this, Help.class);
			startActivity(help);
			break;

		case R.id.exit_game:
			text = "Exit Game";
			this.finish();

			/*
			 * case R.id.create_file: text="Create File"; this.createFile();
			 * break;
			 */
		case R.id.delete_file:
			text = "delete file";
			String path = getApplicationContext().getFilesDir().getPath();
			File f = new File(path + "/easy.txt");
			Toast.makeText(getApplicationContext(),
					"easy File exist: " + f.exists(), Toast.LENGTH_SHORT)
					.show();
			File f1 = new File(path + "/medium.txt");
			Toast.makeText(getApplicationContext(),
					"medium File exist: " + f1.exists(), Toast.LENGTH_SHORT)
					.show();
			File f2 = new File(path + "/hard.txt");
			Toast.makeText(getApplicationContext(),
					"hard File exist: " + f2.exists(), Toast.LENGTH_SHORT)
					.show();
			boolean deleted = f.delete();
			Toast.makeText(getApplicationContext(),
					"easy File has been deleted: " + deleted,
					Toast.LENGTH_SHORT).show();
			deleted = f1.delete();
			Toast.makeText(getApplicationContext(),
					"medium File has been deleted: " + deleted,
					Toast.LENGTH_SHORT).show();
			deleted = f2.delete();
			Toast.makeText(getApplicationContext(),
					"hard File has been deleted: " + deleted,
					Toast.LENGTH_SHORT).show();

			File sharedPrefsDir = new File(getFilesDir(), "../shared_prefs");
			File[] files = sharedPrefsDir.listFiles();
			for (File file : files) {
				file.delete();
				Toast.makeText(getApplicationContext(),
						"File has been deleted: " + file, Toast.LENGTH_SHORT)
						.show();
			}

			break;

		}

		this.showMessage(text);

	}

	private void checkAndCreateFiles() throws IOException {
		String text = "delete file";
		String path = getApplicationContext().getFilesDir().getPath();
		File f = new File(path + "/easy.txt");
		if (!f.exists()) {
			boolean created = f.createNewFile();
			Toast.makeText(getApplicationContext(),
					"File create for easy: " + created, Toast.LENGTH_SHORT)
					.show();

		} else {
			Toast.makeText(getApplicationContext(),
					"easy File exist: " + f.exists(), Toast.LENGTH_SHORT)
					.show();
		}
		File f1 = new File(path + "/medium.txt");
		if (!f1.exists()) {
			boolean created = f1.createNewFile();
			Toast.makeText(getApplicationContext(),
					"File create for medium: " + created, Toast.LENGTH_SHORT)
					.show();

		} else {

			Toast.makeText(getApplicationContext(),
					"medium File exist: " + f1.exists(), Toast.LENGTH_SHORT)
					.show();
		}
		File f2 = new File(path + "/hard.txt");
		if (!f2.exists()) {
			boolean created = f2.createNewFile();
			Toast.makeText(getApplicationContext(),
					"File create for hard: " + created, Toast.LENGTH_SHORT)
					.show();

		} else {
			Toast.makeText(getApplicationContext(),
					"hard File exist: " + f2.exists(), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void createFile() {

		// Toast.makeText(getApplicationContext(), "File Created: "+created,
		// Toast.LENGTH_LONG).show();
		this.writeToFile("00:21 46 2013\n");
		this.writeToFile("10:12 108 10\n");
		Toast.makeText(getApplicationContext(), "File created",
				Toast.LENGTH_LONG).show();

	}

	private void writeToFile(String data) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					openFileOutput("hard_Scores.txt", MODE_APPEND));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	private void showMessage(String text) {
		Context context = getApplicationContext();

		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
	}

}
