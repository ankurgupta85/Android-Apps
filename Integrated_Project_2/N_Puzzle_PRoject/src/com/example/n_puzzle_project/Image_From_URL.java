package com.example.n_puzzle_project;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
	DownloadImageTask downloadTask=null;
	
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
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(getApplicationContext(),
				"Pressed: " + ((Button) v).getText(), Toast.LENGTH_SHORT)
				.show();
		switch (v.getId()) {
		case R.id.load_image: {
			imgview.setImageDrawable(null);
			continue_button.setEnabled(false);
			String urlString = urlText.getText().toString();
			// Validate URL
			if (urlString.trim().length() <= 7) {
				Toast.makeText(getApplicationContext(), "Please enter URL",
						Toast.LENGTH_LONG).show();
			} else {
				urlString = urlString.trim();
				if (!URLUtil.isValidUrl(urlString)) {
					Toast.makeText(
							this,
							"Invalid URL specified.\nPlease add 'http://' if URL is correct.",
							Toast.LENGTH_LONG).show();
				} else {

					new DownloadImageTask(imgview, getApplicationContext(),
							continue_button).execute(urlString);
				}
			}
			break;
		}

		case R.id.continue_game: {
			if(imgview.getDrawable() == null)
			{
				Toast.makeText(getApplicationContext(), "Please load image first", Toast.LENGTH_SHORT).show();
				continue_button.setEnabled(false);
				
			}
			else
			{
				Intent image_game= new Intent(getApplicationContext(),Game_type_selection.class);
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

			// NOTE: In the author's example, he uses an identifier
			// called searchBar. If setting this code on your EditText
			// then use v.getWindowToken() as a reference to your
			// EditText is passed into this callback as a TextView

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
			String mimeType = URLConnection.guessContentTypeFromStream(in);
			mIcon11 = BitmapFactory.decodeStream(in);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		if (result == null) {
			Toast.makeText(context, "Not an image", Toast.LENGTH_LONG).show();

		}

		else {
			ImageUtility imgUtil = new ImageUtility();
			
			boolean imageSaved= imgUtil.saveImageToInternalStorage(result, this.context);
			if(imageSaved)
			{
				System.out.println("Image Saved");
			bmImage.setImageBitmap(result);
			continue_button.setEnabled(true);
			}
			else
			{
				System.out.println("Error saving image");
			}
			}
	}
	

	
	
}


