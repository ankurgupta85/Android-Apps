package com.example.n_puzzle_project;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class SplitImage extends Activity implements OnClickListener {
	TextView moves = null;
	TextView timer = null;
	int[] image_id = null;
	int no_rows, no_cols;
	int img_width, img_height;
	String file_name = null;

	ArrayList<Bitmap> imageChunks = null;
	ArrayList<Bitmap> original_imageChunks = null;
	private boolean timerHasStarted = false;
	// private final long startTime = 300 * 1000;
	private final long startTime = 300 * 1000;
	private final long interval = 1 * 1000;
	private long remainingTime = -1;
	MyCountDownTimer countdownTimer = null;
	int moves_count = 0;

	// long leftTime = -1;
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// ........
			Intent intent = new Intent(getApplicationContext(),
					Select_Game_Type.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_split_image);
		TableLayout body = (TableLayout) findViewById(R.id.body);
		DisplayMetrics metrics = getApplicationContext().getResources()
				.getDisplayMetrics();
		// Logic to add 3 buttons at once for the given input array in the
		// intent.
		// 3 buttons will make one row and that row is added to table.
		TableRow row = new TableRow(this);
		boolean first_time = getIntent().getBooleanExtra("first_time", true);

		imageChunks = getIntent().getParcelableArrayListExtra("image chunks");
		this.timerHasStarted = getIntent().getBooleanExtra("timerHasStarted",
				false);

		int number = (int) Math.sqrt(imageChunks.size());
		this.no_cols = this.no_rows = (int) Math.sqrt(imageChunks.size());
		img_width = getIntent().getIntExtra("width", 0);
		img_height = getIntent().getIntExtra("height", 0);

		if (first_time) {
			//System.out.println("First time ImageView: ");
			original_imageChunks = getIntent().getParcelableArrayListExtra(
					"original_image_chunks");
			image_id = new int[imageChunks.size()];
			for (int i = 1; i < imageChunks.size() + 1; i++) {
				if (i == imageChunks.size()) {
					image_id[i - 1] = -1;
				} else {
					image_id[i - 1] = imageChunks.size() - i;
				}
			}
			for (int i = 1; i < imageChunks.size() + 1; i++) {
				Bitmap image = imageChunks.get(i - 1);
				ImageView imgView = new ImageView(this);
				android.view.ViewGroup.LayoutParams layout = new LayoutParams(
						img_width / number, img_height / number);
				imgView.setLayoutParams(layout);

				if (image == null) {
					imgView.setImageDrawable(null);
					imgView.setId(-1);
					//System.out.println(imgView.getId());

				} else {
					System.gc();
					image = Bitmap.createScaledBitmap(image,
							img_width / number, img_height / number, true);
					imgView.setImageBitmap(image);
					imgView.setId(imageChunks.size() - i);
					//System.out.println(imgView.getId());

					imgView.setOnClickListener(this);

				}
				// System.out.println(imageChunks.size()-i+1);
				row.addView(imgView);
				if (i % number == 0) {

					body.addView(row, new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));

					row = new TableRow(this);

				}

			}

			// image_id[imageChunks.size()-1] = -1;
			/*System.out.println("Image ID array:");
			for (int i = 0; i < imageChunks.size(); i++) {
				System.out.println(image_id[i]);
			}*/
		} else {
			//System.out.println("Not first time imageView: ");
			image_id = getIntent().getIntArrayExtra("image_id");
			for (int i = 1; i < imageChunks.size() + 1; i++) {
				Bitmap image = imageChunks.get(i - 1);
				ImageView imgView = new ImageView(this);
				android.view.ViewGroup.LayoutParams layout = new LayoutParams(
						img_width / number, img_height / number);
				imgView.setLayoutParams(layout);

				if (image == null) {
					imgView.setImageDrawable(null);

				} else {
					System.gc();
					image = Bitmap.createScaledBitmap(image,
							img_width / number, img_height / number, true);
					imgView.setImageBitmap(image);

					imgView.setOnClickListener(this);

				}
				// System.out.println(imageChunks.size()-i+1);
				imgView.setId(image_id[i - 1]);
				//System.out.println(imgView.getId());
				row.addView(imgView);
				if (i % number == 0) {

					body.addView(row, new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));

					row = new TableRow(this);

				}

			}

		}

		LinearLayout header = (LinearLayout) findViewById(R.id.header_layout);
		TableLayout table_header = new TableLayout(this);
		TableRow tr = new TableRow(this);
		// Context context = getApplicationContext();
		int width = metrics.widthPixels;
		// int height = metrics.heightPixels;
		this.moves_count = getIntent().getIntExtra("moves_count", 0);
		this.remainingTime = getIntent().getLongExtra("remaining_time",
				startTime);
		file_name = getIntent().getStringExtra("file_name");

		countdownTimer = new MyCountDownTimer(this.remainingTime, interval);
		countdownTimer.start();
		this.timerHasStarted = true;

		TextView timer_label = new TextView(this);
		timer_label.setText("Time:");
		timer_label.setWidth(width / 4);
		timer_label.setHeight(width / 8);
		// timer_label.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);

		tr.addView(timer_label);

		timer = new TextView(this);
		timer.setText(((MyCountDownTimer) countdownTimer).formatTime(startTime));

		timer.setWidth(width / 4);
		// timer.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

		timer.setHeight(width / 8);
		tr.addView(timer);

		TextView moves_label = new TextView(this);
		moves_label.setText("Moves:");
		moves_label.setWidth(width / 4);
		// moves_label.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);

		moves_label.setHeight(width / 8);
		tr.addView(moves_label);

		moves = new TextView(this);
		moves.setText("" + this.moves_count);
		moves.setWidth(width / 4);
		// moves.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);
		moves.setHeight(width / 8);
		tr.addView(this.moves);

		table_header.addView(tr, new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		header.addView(table_header);

		if (image_id != null) {
			if (image_id[0] == 1 && image_id[imageChunks.size() - 1] == -1) {
				boolean game_over = checkGameOver(image_id);
				if (game_over) {
					if (timerHasStarted) {
						countdownTimer.cancel();
						timerHasStarted = false;

					}

					displayDialog();

				}
			}

		}

		// image_id = new int[imageChunks.size()];

	}

	private boolean checkGameOver(int[] input) {

		boolean gameOver = true;
		if (input == null) {
			return gameOver;
		}
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
			scores = this.remainingTime * 10 / this.moves_count;
		}
		this.writeToFile(time + " " + this.moves_count + " " + scores + "\n");

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		this.countdownTimer.cancel();

		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void displayDialog() {
		String message0 = "Please try again";
		try {

			if (checkGameOver(image_id)) {
				this.saveData();
				message0 = "Congratulations!!!!";
			}
			this.deleteSharedPref();
			AlertDialog dialog = new AlertDialog.Builder(SplitImage.this)
					.create();
			dialog.setTitle("Game Over");

			String message1 = "Time:"
					+ (countdownTimer.formatTime(startTime - this.remainingTime
							+ 2));
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

			/*
			 * dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Restart Game",
			 * new DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { Intent game = null; String path = getFilesDir() +
			 * "../shared_prefs"; File f = null; String file_path = "";
			 * 
			 * file_path = path + "/" + file_name + ".xml"; f = new
			 * File(file_path); if (f.exists()) { f.delete(); } game = new
			 * Intent(getApplicationContext(), Select_Game_Type.class);
			 * game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * game.putExtra("inputArray", image_id);
			 * game.putExtra("moves_count", 0);
			 * 
			 * game.putExtra("no_rows", no_rows); game.putExtra("no_cols",
			 * no_cols); game.putExtra("file_name", file_name);
			 * game.putExtra("remaining_time", startTime); startActivity(game);
			 * 
			 * } });
			 */
			dialog.show();
		} catch (BadTokenException e) {
			//System.out.println(e.getMessage());
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.split_image, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// Toast.makeText(getApplicationContext(),
		// "Image view clicked with id: "+v.getId(), Toast.LENGTH_SHORT).show();

		ImageView pressed_image = (ImageView) v;
		String number_pressed = String.valueOf(pressed_image.getId());
		// Increment move count.... doesnt matter if numbers swapped or not
		// Need to determine if the button position can be swapped with the
		// empty button

		this.checkAndChange(number_pressed);

	}

	private void checkAndChange(String number) {
		int[][] matrix = this.convertOneDimensionalToTwoDimensional(image_id);
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
			Bitmap firsttemp = imageChunks.get(index_number);
			// Bitmap secondtemp = imageChunks.get(empty_number);
			imageChunks.set(index_number, null);
			imageChunks.set(empty_number, firsttemp);
			int temp = image_id[index_number];
			image_id[index_number] = -1;
			image_id[empty_number] = temp;
			/*System.out.println("Image ID array after swap:");
			for (int k = 0; k < imageChunks.size(); k++) {
				System.out.println(image_id[k]);
			}
			*/Intent intent = new Intent(getApplicationContext(),
					SplitImage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// imageChunks.set(imageChunks.size()-1, null);
			intent.putParcelableArrayListExtra("image chunks", this.imageChunks);
			intent.putExtra("image_id", image_id);
			intent.putExtra("width", img_width);
			intent.putExtra("height", img_height);
			intent.putExtra("first_time", false);
			intent.putExtra("moves_count", this.moves_count + 1);
			intent.putExtra("remaining_time", this.remainingTime - 1000);
			intent.putExtra("timerHasStarted", this.timerHasStarted);

			intent.putExtra("file_name", file_name);

			startActivity(intent);

			// Start the same activity with new input array
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

	private int getIndex(String number) {
		int index = -1;
		for (index = 0; index < image_id.length; index++) {
			if (("" + image_id[index]).equals(number)) {
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

	private ArrayList<Integer> getPosition(String string, int[][] matrix) {
		ArrayList<Integer> position = new ArrayList<Integer>();

		for (int i = 0; i < no_rows; i++)
			for (int j = 0; j < no_cols; j++) {
				String current_number = "" + matrix[i][j];
				if (current_number.equals(string)) {
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

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			timerHasStarted = false;
			remainingTime = 0;
			checkGameOver(image_id);
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

}
