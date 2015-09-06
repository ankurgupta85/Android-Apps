package com.puzzle.n_puzzle_project;

import java.util.ArrayList;
import java.util.Arrays;

import com.puzzle.n_puzzle_project.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class TempActivity extends Activity {
	TextView moves = null;
	TextView timer = null;
	Bitmap image = null;
	private final long startTime = 300 * 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp);
		Intent intent = getIntent();
		Toast.makeText(getApplicationContext(), "Last block removed for this game.", Toast.LENGTH_SHORT).show();
		final int img_width = intent.getIntExtra("width", 0);
		final int img_height = intent.getIntExtra("height", 0);
		String source = intent.getStringExtra("image_source");
		ImageUtility util = new ImageUtility();
		if (source.equals("predefined")) {
			image = intent.getParcelableExtra("image");
		} else {
			image = util.getImageFromInternalStorage(getApplicationContext());
		}
		int no_rows = intent.getIntExtra("no_rows", 0);

		final ArrayList<Bitmap> chunks = util.splitImage(image, no_rows
				* no_rows);

		final ArrayList<Bitmap> shuffledImageChunks = (ArrayList<Bitmap>) shuffleImageChunks(chunks);
		Toast.makeText(getApplicationContext(), "Shuffling image blocks", Toast.LENGTH_SHORT).show();
		
		showImage(chunks);
		new CountDownTimer(3000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {

				Intent intent = new Intent(getApplicationContext(),
						SplitImage.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				chunks.set(chunks.size() - 1, null);
				intent.putExtra("width", img_width);
				intent.putExtra("height", img_height);
				intent.putExtra("first_time", true);
				intent.putExtra("moves_count", 0);
				intent.putExtra("file_name", "easy");
				intent.putExtra("remaining_time", startTime);
				intent.putParcelableArrayListExtra("image chunks",
						shuffledImageChunks);

				startActivity(intent);
			}
		}.start();

	}

	private ArrayList<Bitmap> shuffleImageChunks(ArrayList<Bitmap> imageChunks) {
		Bitmap[] array = new Bitmap[imageChunks.size()];
		int i = 0;
		int j = imageChunks.size() - 2;
		for (; i <= j; i++, j--) {

			Bitmap firsttemp = imageChunks.get(i);
			Bitmap secondtemp = imageChunks.get(j);
			array[i] = secondtemp;

			array[j] = firsttemp;

		}

		if (imageChunks.size() == 16) {
			// Make order as 312 instead of 321
			Bitmap temp = array[13];
			array[13] = array[14];
			array[14] = temp;

		}

		return new ArrayList<Bitmap>(Arrays.asList(array));

	}

	private void showImage(ArrayList<Bitmap> imageChunks) {
		TableLayout body = (TableLayout) findViewById(R.id.body);
		DisplayMetrics metrics = getApplicationContext().getResources()
				.getDisplayMetrics();
		TableRow row = new TableRow(this);

		LinearLayout header = (LinearLayout) findViewById(R.id.header_layout);
		TableLayout table_header = new TableLayout(this);
		TableRow tr = new TableRow(this);
		int width = metrics.widthPixels;

		TextView timer_label = new TextView(this);
		timer_label.setText("Time:");
		timer_label.setWidth(width / 4);
		timer_label.setHeight(width / 8);

		tr.addView(timer_label);

		timer = new TextView(this);
		timer.setText("05:00");

		timer.setWidth(width / 4);

		timer.setHeight(width / 8);
		tr.addView(timer);

		TextView moves_label = new TextView(this);
		moves_label.setText("Moves:");
		moves_label.setWidth(width / 4);

		moves_label.setHeight(width / 8);
		tr.addView(moves_label);

		moves = new TextView(this);
		moves.setText("0");
		moves.setWidth(width / 4);
		// moves.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);
		moves.setHeight(width / 8);
		tr.addView(this.moves);

		table_header.addView(tr, new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		header.addView(table_header);

		// /ArrayList<Bitmap> imageChunks =
		// getIntent().getParcelableArrayListExtra("image chunks");
		int number = (int) Math.sqrt(imageChunks.size());
		int img_width = getIntent().getIntExtra("width", 0);
		int img_height = getIntent().getIntExtra("height", 0);
		for (int i = 1; i < imageChunks.size() + 1; i++) {
			Bitmap image = imageChunks.get(i - 1);

			image = Bitmap.createScaledBitmap(image, img_width / number,
					img_height / number, true);
			ImageView imgView = new ImageView(this);
			android.view.ViewGroup.LayoutParams layout = new LayoutParams(
					img_width / number, img_height / number);
			imgView.setLayoutParams(layout);
			if (i == imageChunks.size()) {
				imgView.setImageDrawable(null);
			} else {
				imgView.setImageBitmap(image);
			}
			row.addView(imgView);
			if (i % number == 0) {

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

}