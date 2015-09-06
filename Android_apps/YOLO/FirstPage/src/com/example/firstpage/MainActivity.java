package com.example.firstpage;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TabHost tabs = getTabHost();
		
		
		
		TabSpec login = tabs.newTabSpec("Login");
		login.setIndicator("Login");
	
		Intent loginIntent = new Intent(this,Login.class);
		login.setContent(loginIntent);
		
		TabSpec signup = tabs.newTabSpec("Sign Up");
		signup.setIndicator("Sign Up");
		Intent signUpIntent = new Intent(this,SignUp.class);
		signup.setContent(signUpIntent);
		
		TabSpec aboutus = tabs.newTabSpec("Home");
		aboutus.setIndicator("Home");
		Intent aboutUsIntent = new Intent(this,AboutUs.class);
		aboutus.setContent(aboutUsIntent);
		
		TabSpec help = tabs.newTabSpec("Help");
		help.setIndicator("Help");
		Intent helpIntent= new Intent(this,Help.class);
		help.setContent(helpIntent);
		
		
		tabs.addTab(login);
		tabs.addTab(signup);
		tabs.addTab(help);
		tabs.addTab(aboutus);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
