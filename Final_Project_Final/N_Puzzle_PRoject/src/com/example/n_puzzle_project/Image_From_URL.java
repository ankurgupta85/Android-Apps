package com.example.n_puzzle_project;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class Image_From_URL extends Activity implements OnClickListener,
		OnEditorActionListener {
	EditText urlText = null;
	Button load_button = null;
	Button continue_button = null;
	ImageView imgview = null;
	DownloadImageTask downloadTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image__from__url);
		urlText = (EditText) findViewById(R.id.text_url);

		imgview = (ImageView) findViewById(R.id.imageview);
		urlText.setOnEditorActionListener(this);
		load_button = (Button) findViewById(R.id.load_image);

		load_button.setOnClickListener(this);
		continue_button = (Button) findViewById(R.id.continue_game);
		continue_button.setEnabled(false);
		continue_button.setOnClickListener(this);

		DisplayMetrics metrics = getApplicationContext().getResources()
				.getDisplayMetrics();
		int width = metrics.widthPixels;
		urlText.setWidth(width);
		urlText.setSelection(urlText.getText().length());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 getMenuInflater().inflate(R.menu.image__from__url, menu);
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


	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	
	private void displayNetworkDialog()
	{
		try {
			String message = "You are not connected to internet.\n Please connect and try again.";
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setTitle("Game Over");

			dialog.setMessage(message);
			dialog.setCancelable(false);
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Change Image",
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
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.load_image: {
			boolean networkAvailable = this.isNetworkAvailable();
			if(!networkAvailable)
			{
				
				this.displayNetworkDialog();
			}
			
			imgview.setImageDrawable(null);
			continue_button.setEnabled(false);
			String urlString = urlText.getText().toString();
			// Validate URL
			if (urlString.trim().length() <= 7) {
				Toast.makeText(getApplicationContext(), "Please enter URL",
						Toast.LENGTH_SHORT).show();
			} else {
				urlString = urlString.trim();
				if (!URLUtil.isValidUrl(urlString)) {
					Toast.makeText(
							this,
							"Invalid URL specified.\nPlease add 'http://' if URL is correct.",
							Toast.LENGTH_SHORT).show();
				} else {

					new DownloadImageTask(imgview, getApplicationContext(),
							continue_button).execute(urlString);
				}
			}
			break;
		}

		case R.id.continue_game: {
			if (imgview.getDrawable() == null) {
				Toast.makeText(getApplicationContext(),
						"Please load image first", Toast.LENGTH_SHORT).show();
				continue_button.setEnabled(false);

			} else {
				Intent image_game = new Intent(getApplicationContext(),
						Game_type_selection.class);
				image_game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				image_game.putExtra("width", imgview.getWidth());
				image_game.putExtra("height", imgview.getHeight());
				image_game.putExtra("game_type", "image");
				image_game.putExtra("image_source", "url");
				startActivity(image_game);
			}

		}

		}

		// TODO Auto-generated method stub

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


			in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			return true;

		}
		return false;
	}
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	Context context;
	Button continue_button = null;

	public DownloadImageTask(ImageView bmImage, Context context,
			Button continue_button) {
		this.bmImage = bmImage;
		this.context = context;
		this.continue_button = continue_button;

	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			URL url = new java.net.URL(urldisplay);

			InputStream in = url.openStream();
			mIcon11 = BitmapFactory.decodeStream(in);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		if (result == null) {
			Toast.makeText(context, "Not an image", Toast.LENGTH_SHORT).show();

		}

		else {
			ImageUtility imgUtil = new ImageUtility();

			boolean imageSaved = imgUtil.saveImageToInternalStorage(result,
					this.context);
			if (imageSaved) {
				bmImage.setImageBitmap(result);
				continue_button.setEnabled(true);
			} else {
				Toast.makeText(context, "Not able to save image. Please try again.", Toast.LENGTH_SHORT).show();

			}
		}
	}

}
