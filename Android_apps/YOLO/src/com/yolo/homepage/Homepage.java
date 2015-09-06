package com.yolo.homepage;

import java.util.HashMap;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.example.yolo.R;
import com.yolo.datastore.PastSearchDataStore;
import com.yolo.util.ApplicationHashMaps;

@SuppressWarnings("deprecation")
public class Homepage extends TabActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		HashMap<Integer, PastSearchDataStore> searchCache = ApplicationHashMaps.getSearchCache();
	
		TabHost tabs = getTabHost();

		
		TabSpec search = tabs.newTabSpec("Search");
		search.setIndicator("",
				getResources().getDrawable(R.drawable.search_color));

		Intent searchIntent = new Intent(this, SearchScreen.class);
		searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		searchIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		search.setContent(searchIntent);

		TabSpec know = tabs.newTabSpec("I Know Product");
		know.setIndicator("", getResources().getDrawable(R.drawable.know_color));
		Intent knowIntent = new Intent(this, KnowProduct.class);
		knowIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		knowIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		knowIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		know.setContent(knowIntent);

		TabSpec wishlist = tabs.newTabSpec("Wishlist");
		wishlist.setIndicator("",
				getResources().getDrawable(R.drawable.wishlist_pen_color));

		Intent wishlistIntent = new Intent(this, Wishlist.class);
		wishlistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		wishlistIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		wishlistIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		wishlist.setContent(wishlistIntent);

		TabSpec pastSearch = tabs.newTabSpec("Past Search");
		pastSearch.setIndicator("",
				getResources().getDrawable(R.drawable.past_bin));
		Intent pastSearchIntent = new Intent(getApplicationContext(), PastSearch.class);
		pastSearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		pastSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		pastSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		System.out.println(searchCache);
		pastSearch.setContent(pastSearchIntent);

		tabs.addTab(search);
		tabs.addTab(know);
		tabs.addTab(wishlist);
		tabs.addTab(pastSearch);

		for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
			tabs.getTabWidget().getChildAt(i).setPadding(10, 10, 10, 10);
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.homepage, menu);
		
		return true;
		//return super.onCreateOptionsMenu(menu);
	}

	
	
	@Override
	protected void onStop() {
		if(ApplicationHashMaps.isDirty())
		{
			new SaveSearchCache().execute(getApplicationContext());
		}
		super.onStop();
	}
	
	
	/*@Override
	public void onBackPressed() {
	
	
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        // your code
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	*/
	
	
	
	
}
