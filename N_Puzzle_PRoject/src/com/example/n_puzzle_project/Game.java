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
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

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
	private final long startTime = 10 * 1000;
	private final long interval = 1 * 1000;
	private long remainingTime = -1;
	MyCountDownTimer countdownTimer = null;
	long leftTime = -1;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		Intent inputIntent = getIntent();
		input = inputIntent.getIntArrayExtra("inputArray");
		moves_count = inputIntent.getIntExtra("moves_count", 0);
		leftTime = inputIntent.getLongExtra("remaining_time", startTime);
		no_rows = inputIntent.getIntExtra("no_rows",
				(int) Math.sqrt(input.length));

		no_cols = inputIntent.getIntExtra("no_cols",
				(int) Math.sqrt(input.length));
		file_name = inputIntent.getStringExtra("file_name");
		countdownTimer = new MyCountDownTimer(leftTime, interval);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_easy__game);

		// NEed to additionally determine if the game is done by the user.
		if (input[0] == 1 && input[input.length-1] == -1) {
			boolean gameOver = this.checkGameOver(input);

			if (gameOver) {
				if (timerHasStarted) {
					countdownTimer.cancel();
					timerHasStarted = false;
				}
				// this.saveData();
				this.displayDialog();
			}

		}

		Context context = getApplicationContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		countdownTimer.start();
		timerHasStarted = true;
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		RelativeLayout page = (RelativeLayout) findViewById(R.id.easy_game_layout);

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

		// Logic to add 3 buttons at once for the given input array in the
		// intent.
		// 3 buttons will make one row and that row is added to table.
		TableRow row = new TableRow(this);
		for (int i = 1; i < input.length + 1; i++) {

			Button b1 = new Button(this);
			if (input[i - 1] == -1) {
				b1.setText("");
				
			} else if(remainingTime!=0){
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

	private void saveData() {
		String time = countdownTimer.formatTime(startTime - remainingTime);
		String moves = "" + moves_count;
		long scores = 0;
		if (moves_count != 0) {
			scores = remainingTime * 100 / moves_count;
		}
		this.writeToFile(time + " " + scores + " " + moves + "\n");

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
		try{
		String message0="Please try again";
		if (checkGameOver(input)) {
			this.saveData();
			message0= "Congratulations!!!!";
		}
		this.deleteSharedPref();
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Game Over");
		
		String message1 = "Time:"
				+ (countdownTimer.formatTime(startTime - remainingTime + 2));
		String message2 = "Moves: " + moves_count;
		dialog.setMessage(message0+"\n"+message1 + "\n" + message2);
		dialog.setCancelable(false);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "New Game",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent game = new Intent(getApplicationContext(),
								Game_type_selection.class);
						game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(game);

					}
				});

		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Restart Game",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent game = null;
						String path = getFilesDir() + "../shared_prefs";
						File f = null;
						String file_path = "";

						file_path = path + "/" + file_name + ".xml";
						f = new File(file_path);
						if (f.exists()) {
							f.delete();
						}
						game = new Intent(getApplicationContext(), Game.class);
						game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						game.putExtra("inputArray", input);
						game.putExtra("moves_count", 0);
						game.putExtra("no_rows", no_rows);
						game.putExtra("no_cols", no_cols);
						game.putExtra("file_name", file_name);
						game.putExtra("remaining_time", startTime-1000);
						startActivity(game);

					}
				});

		dialog.show();
		}
		catch(BadTokenException e)
		{
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.easy__game, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Button pressed_button = (Button) v;
		String number_pressed = pressed_button.getText().toString();
		// Increment move count.... doesnt matter if numbers swapped or not
		moves_count++;
		moves.setText("" + moves_count);
		Toast.makeText(getApplicationContext(), number_pressed,
				Toast.LENGTH_LONG).show();
		// Need to determine if the button position can be swapped with the
		// empty button

		this.checkAndChange(number_pressed);
	}

	private boolean checkGameOver(int[] input) {
		boolean gameOver = false;
		if (input[input.length - 1] != -1) {
			gameOver = false;
		} else {
			for (int i = 1; i < input.length - 2; i++) {
				if (input[i - 1] >= input[i]) {
					gameOver = false;
					break;
				} else {
					gameOver = true;
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
			game.putExtra("moves_count", moves_count);
			game.putExtra("remaining_time", remainingTime);
			game.putExtra("no_rows", no_rows);
			game.putExtra("no_cols", no_cols);
			game.putExtra("file_name", file_name);

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
		// TODO Auto-generated method stub
		if (timerHasStarted) {
			countdownTimer.cancel();
		}
		this.saveTempData();
		Toast.makeText(this, "On Pause called", Toast.LENGTH_SHORT).show();
		super.onPause();
	}

	private void saveTempData() {
		SharedPreferences pref = getSharedPreferences(file_name, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong("time_left", remainingTime);

		editor.putInt("moves_count", moves_count);
		editor.putInt("no_rows", no_rows);
		editor.putInt("no_cols", no_cols);
		editor.putString("file_name", file_name);
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < input.length; i++) {
			str.append(input[i]).append(",");
		}
		editor.putString("input_array", str.toString());
		editor.commit();
		Toast.makeText(getApplicationContext(),
				"Preferences saved for " + file_name, Toast.LENGTH_LONG).show();
		pref = getSharedPreferences("gameData", MODE_PRIVATE);
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

	/*
	 * @Override public void onBackPressed() { // Save user information. Same as
	 * onPause. super.onBackPressed(); System.gc(); Intent I = new Intent(this,
	 * Game_type_selection.class); I.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * startActivity(I); }
	 */

	private void deleteSharedPref() {
		String path = getFilesDir() + "../shared_prefs";
		String file_path = path + "/" + file_name + ".xml";
		File f = new File(file_path);
		if (f.exists()) {
			f.delete();
		}
	}

	private boolean ifSharedPrefExist() {
		String path = getFilesDir() + "../shared_prefs";
		String file_path = path + "/" + file_name + ".xml";
		File f = new File(file_path);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
