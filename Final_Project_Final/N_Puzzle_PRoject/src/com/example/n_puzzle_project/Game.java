package com.example.n_puzzle_project;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class Game extends Activity implements OnClickListener {
	int no_rows = 3;
	int no_cols = 3;
	int[] input = null;
	int moves_count = 0;
	String file_name = null;
	TextView moves = null;
	TextView timer = null;
	private boolean timerHasStarted = false;
	// private final long startTime = 300 * 1000;
	private final long startTime = 300 * 1000;
	private final long interval = 1 * 1000;
	private long remainingTime = -1;
	MyCountDownTimer countdownTimer = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		Intent inputIntent = getIntent();
		input = inputIntent.getIntArrayExtra("inputArray");
		this.moves_count = inputIntent.getIntExtra("moves_count", 0);
		this.remainingTime = inputIntent.getLongExtra("remaining_time",
				startTime);
		this.timerHasStarted = inputIntent.getBooleanExtra("timerHasStarted",
				false);
		no_rows = inputIntent.getIntExtra("no_rows",
				(int) Math.sqrt(input.length));

		no_cols = inputIntent.getIntExtra("no_cols",
				(int) Math.sqrt(input.length));
		file_name = inputIntent.getStringExtra("file_name");
		this.countdownTimer = new MyCountDownTimer(this.remainingTime, interval);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_easy__game);

		if (input[0] == 1 && input[input.length - 1] == -1) {
			boolean gameOver = this.checkGameOver(input);

			if (gameOver) {
				this.countdownTimer.cancel();
				this.timerHasStarted = false;

				this.displayDialog();
			}

		}

		Context context = getApplicationContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		countdownTimer.start();
		this.timerHasStarted = true;
		int width = metrics.widthPixels;

		LinearLayout header = (LinearLayout) findViewById(R.id.header_layout);
		TableLayout table_header = new TableLayout(this);
		TableRow tr = new TableRow(this);

		TextView timer_label = new TextView(this);
		timer_label.setText("Time:");
		timer_label.setWidth(width / 4);
		timer_label.setHeight(width / 8);
		timer_label.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);

		tr.addView(timer_label);

		timer = new TextView(this);
		timer.setText(((MyCountDownTimer) countdownTimer).formatTime(startTime));

		timer.setWidth(width / 4);
		timer.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);

		timer.setHeight(width / 8);
		tr.addView(timer);

		TextView moves_label = new TextView(this);
		moves_label.setText("Moves:");
		moves_label.setWidth(width / 4);
		moves_label.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);

		moves_label.setHeight(width / 8);
		tr.addView(moves_label);

		moves = new TextView(this);
		moves.setText("" + this.moves_count);
		moves.setWidth(width / 4);
		moves.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);
		moves.setHeight(width / 8);
		tr.addView(this.moves);

		table_header.addView(tr, new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		header.addView(table_header);

		TableLayout body = (TableLayout) findViewById(R.id.body);

		TableRow row = new TableRow(this);
		for (int i = 1; i < input.length + 1; i++) {

			Button b1 = new Button(this);
			if (input[i - 1] == -1) {
				b1.setText("");

			} else if (remainingTime != 0) {
				b1.setText("" + (input[i - 1]));
				b1.setOnClickListener(this);
			}
			b1.setWidth(width / no_cols);

			b1.setHeight(width / no_rows);
			row.addView(b1);

			if (i % no_cols == 0) {

				body.addView(row, new TableLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				row = new TableRow(this);

			}

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		unbindDrawable(findViewById(R.id.easy_game_layout));
		System.gc();
	}

	private void unbindDrawable(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawable(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}

	}

	private void saveData() {
		String time = countdownTimer.formatTime(startTime - this.remainingTime);
		// String moves = "" + this.moves_count;
		long scores = 0;
		if (this.moves_count != 0) {
			scores = remainingTime * 10 / this.moves_count;
		}
		this.writeToFile(time + " " + this.moves_count + " " + scores + "\n");

	}

	private void writeToFile(String data) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					openFileOutput(file_name + ".txt", MODE_APPEND));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	private void displayDialog() {
		try {
			String message0 = "Please try again";
			if (checkGameOver(input)) {
				this.saveData();
				message0 = "Congratulations!!!!";
			}
			this.deleteSharedPref();
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setTitle("Game Over");

			String message1 = "Time:"
					+ (countdownTimer
							.formatTime(startTime - this.remainingTime));
			String message2 = "Moves: " + this.moves_count;
			dialog.setMessage(message0 + "\n" + message1 + "\n" + message2);
			dialog.setCancelable(false);
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "New Game",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent game = new Intent(getApplicationContext(),
									Select_Game_Type.class);
							game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(game);

						}
					});
			dialog.show();
		} catch (BadTokenException e) {

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.easy__game, menu);
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
		Button pressed_button = (Button) v;
		String number_pressed = pressed_button.getText().toString();
		this.moves_count++;
		moves.setText("" + this.moves_count);

		this.checkAndChange(number_pressed);
	}

	private boolean checkGameOver(int[] input) {
		boolean gameOver = true;
		if (input[input.length - 1] != -1) {
			gameOver = false;
		} else {
			for (int i = 1; i < input.length - 1; i++) {
				if (input[i - 1] >= input[i]) {
					gameOver = false;
					break;
				}
			}
		}

		return gameOver;
	}

	private void checkAndChange(String number) {
		int[][] matrix = this.convertOneDimensionalToTwoDimensional(input);
		ArrayList<Integer> position = (ArrayList<Integer>) this.getPosition(
				number, matrix);
		int i = position.get(0);
		int j = position.get(1);

		position = (ArrayList<Integer>) this.getPosition("-1", matrix);
		int empty_i = position.get(0);
		int empty_j = position.get(1);

		boolean swap_possible = this.isSwapPossible(i, j, empty_i, empty_j);
		int index_number = this.getIndex(number);
		int empty_number = this.getIndex("-1");

		// Need to rearrange the input array to reflect the changes
		if (swap_possible) {
			int temp = this.input[index_number];
			this.input[index_number] = -1;
			this.input[empty_number] = temp;
			// Start the same activity with new input array

			Intent game = new Intent(getApplicationContext(), Game.class);
			game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			game.putExtra("inputArray", input);
			game.putExtra("moves_count", this.moves_count);
			game.putExtra("remaining_time", this.remainingTime - 1000);
			game.putExtra("no_rows", no_rows);
			game.putExtra("no_cols", no_cols);
			game.putExtra("file_name", file_name);
			game.putExtra("timerHasStarted", this.timerHasStarted);
			startActivity(game);
		}

	}

	private int getIndex(String number) {
		int index = -1;
		for (index = 0; index < input.length; index++) {
			if (("" + input[index]).equals(number)) {
				break;
			}
		}

		return index;

	}

	private boolean isSwapPossible(int i, int j, int empty_i, int empty_j) {
		boolean swap_possible = false;
		if ((i == empty_i - 1 && j == empty_j)
				|| (i == empty_i + 1 && j == empty_j)
				|| (i == empty_i && j == empty_j - 1)
				|| (i == empty_i && j == empty_j + 1)) {
			swap_possible = true;
		}

		return swap_possible;
	}

	private ArrayList<Integer> getPosition(String number, int[][] matrix) {
		ArrayList<Integer> position = new ArrayList<Integer>();

		for (int i = 0; i < no_rows; i++)
			for (int j = 0; j < no_cols; j++) {
				String current_number = "" + matrix[i][j];
				if (current_number.equals(number)) {
					position.add(i);
					position.add(j);
					break;
				}
			}

		return position;
	}

	private int[][] convertOneDimensionalToTwoDimensional(int[] srcMatrix) {
		int srcMatrixLength = srcMatrix.length;
		int srcPosition = 0;

		int[][] returnMatrix = new int[no_rows][];
		for (int i = 0; i < no_rows; i++) {
			int[] row = new int[no_cols];
			int nextSrcPosition = srcPosition + no_cols;
			if (srcMatrixLength >= nextSrcPosition) {
				// Copy the data from the file if it has been written before.
				// Otherwise we just keep row empty.
				System.arraycopy(srcMatrix, srcPosition, row, 0, no_cols);
			}
			returnMatrix[i] = row;
			srcPosition = nextSrcPosition;
		}
		return returnMatrix;
	}

	@Override
	protected void onPause() {

		System.gc();

		if (!checkGameOver(input)) {
			this.saveTempData();
		} else {
			File sharedPrefsDir = new File(getFilesDir(), "../shared_prefs");
			File[] files = sharedPrefsDir.listFiles();
			for (File file : files) {
				if (file.getName().equals(file_name + ".xml")) {
					file.delete();
				}
			}
		}
		this.countdownTimer.cancel();

		// TODO Auto-generated method stub
		super.onPause();
	}

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
		/*
		 * Toast.makeText(getApplicationContext(), "Saved " + file_name,
		 * Toast.LENGTH_LONG).show();
		 */pref = getSharedPreferences("gameData", MODE_PRIVATE);
		editor = pref.edit();
		editor.putInt("saved", 1);
		editor.commit();
		System.gc();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.gc();
		super.onResume();
	}

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			timerHasStarted = false;
			remainingTime = 0;
			checkGameOver(input);
			// saveData();
			displayDialog();

		}

		@Override
		public void onTick(long millisUntilFinished) {
			remainingTime = millisUntilFinished;
			timer.setText(formatTime(millisUntilFinished));
		}

		public String formatTime(long millis) {
			String output = "00:00";
			long seconds = millis / 1000;
			long minutes = seconds / 60;

			seconds = seconds % 60;
			minutes = minutes % 60;

			String sec = String.valueOf(seconds);
			String min = String.valueOf(minutes);

			if (seconds < 10)
				sec = "0" + seconds;
			if (minutes < 10)
				min = "0" + minutes;

			output = min + ":" + sec;
			return output;
		}
	}


	private void deleteSharedPref() {
		String path = getFilesDir() + "../shared_prefs";
		String file_path = path + "/" + file_name + ".xml";
		File f = new File(file_path);
		if (f.exists()) {
			f.delete();
		}
	}
}
