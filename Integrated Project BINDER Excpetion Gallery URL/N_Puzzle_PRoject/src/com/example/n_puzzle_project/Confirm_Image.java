package com.example.n_puzzle_project;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Confirm_Image extends Activity implements OnClickListener {
	ImageView imgView = null;
	Bitmap image=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm__image);
		imgView = (ImageView) findViewById(R.id.ImageView01);
		Button change_image = (Button) findViewById(R.id.change_image);
		Button continue_game = (Button) findViewById(R.id.continue_game);
		continue_game.setEnabled(false);
		change_image.setOnClickListener(this);
		continue_game.setOnClickListener(this);

		Intent intent = getIntent();
		Toast.makeText(getApplicationContext(),
				"Game type: " + intent.getStringExtra("game_type"),
				Toast.LENGTH_LONG).show();
		ImageUtility util = new ImageUtility();
		image = (Bitmap) intent.getParcelableExtra("image");
		// Bitmap image =
		// util.getImageFromInternalStorage(getApplicationContext());
		if (image != null) {
			imgView.setImageBitmap(image);
			continue_game.setEnabled(true);
		} else {
			Toast.makeText(getApplicationContext(),
					"Not able to retrieve image", Toast.LENGTH_LONG).show();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirm__image, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_image:
			Intent intent = new Intent(getApplicationContext(),
					Select_Game_Type.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.continue_game: {
			if (imgView.getDrawable() == null) {
				Toast.makeText(getApplicationContext(),
						"Please select image before continuing",
						Toast.LENGTH_SHORT).show();
				Intent invalid_intent = new Intent(getApplicationContext(),
						Select_Game_Type.class);
				invalid_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(invalid_intent);
				break;
			} else {
				Toast.makeText(getApplicationContext(),
						"Image Loaded",
						Toast.LENGTH_SHORT).show();
	
				Intent valid_intent = new Intent(getApplicationContext(),
						Game_type_selection.class);
				valid_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				valid_intent.putExtra("image", image);
				valid_intent.putExtra("game_type", "image");
				valid_intent.putExtra("image_source", "predefined");
				valid_intent.putExtra("width", imgView.getWidth());
				valid_intent.putExtra("height", imgView.getHeight());
				startActivity(valid_intent);
				break;
			}

		}
		}

	}

}
