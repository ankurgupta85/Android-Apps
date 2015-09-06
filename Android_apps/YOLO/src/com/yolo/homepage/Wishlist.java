package com.yolo.homepage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.yolo.R;
import com.yolo.mainscreen.MainActivity;

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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.logout:
			
		/*	SharedPreferences pref = getSharedPreferences("loginToken",MODE_PRIVATE);
			if(pref.contains("remember"))
			{
				SharedPreferences.Editor editor = pref.edit();
				editor.remove("remember");
				editor.commit();
			}
			Intent intent = new Intent(Wishlist.this,MainActivity.class);
			
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
*/
			LogOutOperations logoutOperation = new LogOutOperations(getApplicationContext());
			logoutOperation.performLogoutOperations();

			Intent intent = new Intent(getApplicationContext(),MainActivity.class);
			
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			break;
			
		case R.id.action_settings:
			System.out.println("hello");
			break;
		}
		
		return false;
		//return super.onOptionsItemSelected(item);
	}
	
	@Override
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
	


}
