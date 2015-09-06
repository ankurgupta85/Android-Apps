package com.example.n_puzzle_project;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Game_type_selection extends Activity implements OnClickListener {
	private final long startTime = 300 * 1000;
	Intent intent = null;
	int no_rows = 0;
	int no_cols = 0;
	String file_name = "";
	int[] input = null;
	int moves_count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_type_selection);

		TextView easy_level = (TextView) findViewById(R.id.easy_level);
		easy_level.setOnClickListener(this);

		TextView medium_level = (TextView) findViewById(R.id.medium_level);
		medium_level.setOnClickListener(this);

		TextView hard_level = (TextView) findViewById(R.id.hard_level);
		hard_level.setOnClickListener(this);

	}

	private void redirectActivity(String game_type, Context context) {
		Intent game = null;
		if (game_type.equals("normal")) {
			game = new Intent(getApplicationContext(), Game.class);
			game.putExtra("inputArray", input);

		} else if (game_type.equals("image")) {
			game = new Intent(getApplicationContext(), TempActivity.class);
			String source = intent.getStringExtra("image_source");
			if (source.equals("predefined")) {
				Bitmap image = intent.getParcelableExtra("image");
				game.putExtra("image", image);
				game.putExtra("image_source", "predefined");
			} else {
				game.putExtra("image_source", "gallery_url");
			}

			int width = intent.getIntExtra("width", 0);
			int height = intent.getIntExtra("height", 0);
			game.putExtra("width", width);
			game.putExtra("height", height);

		}

		game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		game.putExtra("moves_count", moves_count);
		game.putExtra("no_rows", no_rows);
		game.putExtra("no_cols", no_cols);
		game.putExtra("file_name", file_name);
		game.putExtra("remaining_time", startTime);
		startActivity(game);
		// IF game type is normal continue with user option and let user choose
		// which configuration

	}
/*
	private void showMessage(String text) {
		Toast toast = Toast.makeText(getApplicationContext(), text,
				Toast.LENGTH_SHORT);
		toast.show();

	}
*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_type_selection, menu);
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
		String text = "Something else pressed";
		String path = getFilesDir() + "../shared_prefs";
		File f = null;
		String file_path = "";

		intent = getIntent();
		String game_type = intent.getStringExtra("game_type");

		switch (v.getId()) {
		case R.id.easy_level:
			text = " Easy Level Selected";
			input = new int[] { 8, 7, 6, 5, 4, 3, 2, 1, -1 };
			file_path = path + "/easy.xml";
			f = new File(file_path);
			if (f.exists()) {
				f.delete();
			}
			no_rows = 3;
			no_cols = 3;
			file_name = "easy";

			break;
		case R.id.medium_level:
			text = " Medium Level Selected";
			input = new int[] { 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 1,
					2, -1 };
			file_path = path + "/medium.xml";
			f = new File(file_path);
			if (f.exists()) {
				f.delete();
			}
			no_rows = 4;
			no_cols = 4;
			file_name = "medium";

			break;
		case R.id.hard_level:
			text = " Hard Level Selected";
			input = new int[] { 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13,
					12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, -1 };
			file_path = path + "/hard.xml";
			f = new File(file_path);
			if (f.exists()) {
				f.delete();
			}
			no_rows = 5;
			no_cols = 5;
			file_name = "hard";

			break;

		}
		this.redirectActivity(game_type, getApplicationContext());
	//	this.showMessage(text);

	}

}
