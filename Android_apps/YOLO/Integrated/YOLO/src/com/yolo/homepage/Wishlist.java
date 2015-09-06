package com.yolo.homepage;

import com.example.yolo.R;
import com.example.yolo.R.layout;
import com.example.yolo.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Wishlist extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wishlist);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wishlist, menu);
		return true;
	}

}
