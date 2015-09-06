package com.yolo.homepage;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.yolo.R;

public class Homepage extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
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
		Intent pastSearchIntent = new Intent(this, PastSearch.class);
		pastSearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		pastSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		pastSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
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
		getMenuInflater().inflate(R.menu.homepage, menu);
		return true;
	}

}
