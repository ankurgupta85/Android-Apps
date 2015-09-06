package com.example.imagefromurl;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class MainActivity extends Activity implements OnClickListener,OnEditorActionListener {
	EditText urlText = null;
	Button button = null;
	ImageView imgview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		urlText = (EditText) findViewById(R.id.text_url);

		imgview = (ImageView) findViewById(R.id.imageview);
		urlText.setOnEditorActionListener(this);
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		imgview.setImageDrawable(null);
		String urlString = urlText.getText().toString();
		// Validate URL
		if (urlString.trim().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please enter URL",
					Toast.LENGTH_LONG).show();
		} else {
			urlString = urlString.trim();
			if (!URLUtil.isValidUrl(urlString)) {
				Toast.makeText(this, "Invalid URL specified.\nPlease add 'http://' if URL is correct.", Toast.LENGTH_LONG)
						.show();
			} else {

				new DownloadImageTask(imgview, getApplicationContext())
						.execute(urlString);
			}
		}

		// TODO Auto-generated method stub

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            // NOTE: In the author's example, he uses an identifier
            // called searchBar. If setting this code on your EditText
            // then use v.getWindowToken() as a reference to your 
            // EditText is passed into this callback as a TextView

            in.hideSoftInputFromWindow(v
                    .getApplicationWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
           return true;

        }
        return false;
	}
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	Context context;

	public DownloadImageTask(ImageView bmImage, Context context) {
		this.bmImage = bmImage;
		this.context = context;
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
			bmImage.setImageBitmap(result);
		}
	}
}
