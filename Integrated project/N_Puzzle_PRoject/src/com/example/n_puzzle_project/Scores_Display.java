package com.example.n_puzzle_project;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;

@Suppress
public class Scores_Display extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scores__display);
		 TabHost tabHost = getTabHost();
         
	        // Tab for Photos
	        TabSpec photospec = tabHost.newTabSpec("Easy");
	        // setting Title and Icon for the Tab
	        photospec.setIndicator("Easy", getResources().getDrawable(R.drawable.ic_launcher));
	        Intent photosIntent = new Intent(this, Easy_Scores.class);
	        photospec.setContent(photosIntent);
	         
	        // Tab for Songs
	        TabSpec songspec = tabHost.newTabSpec("Medium");        
	        songspec.setIndicator("Medium", getResources().getDrawable(R.drawable.ic_launcher));
	        Intent songsIntent = new Intent(this, Medium_Scores.class);
	        songspec.setContent(songsIntent);
	         
	        // Tab for Videos
	        TabSpec videospec = tabHost.newTabSpec("Hard");
	        videospec.setIndicator("Hard", getResources().getDrawable(R.drawable.ic_launcher));
	        Intent videosIntent = new Intent(this, Hard_Scores.class);
	        videospec.setContent(videosIntent);
	         
	        // Adding all TabSpec to TabHost
	        tabHost.addTab(photospec); // Adding photos tab
	        tabHost.addTab(songspec); // Adding songs tab
	        tabHost.addTab(videospec); // Adding videos tab
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scores__display, menu);
		return true;
	}

}
