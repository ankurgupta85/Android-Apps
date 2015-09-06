package com.example.n_puzzle_project;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent =null;
	switch(item.getItemId())
	{
	case R.id.Main_Page:
		intent = new Intent(getApplicationContext(),MainPage.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return true;
	case R.id.Scores:
		intent = new Intent(getApplicationContext(),Scores_Display.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return true;
	case R.id.exit_game:
		finish();
		return true;
	default:
		return super.onOptionsItemSelected(item);
		
	}
	
		
		
		
	}
}
