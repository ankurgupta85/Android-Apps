package com.yolo.mainscreen;

import com.example.yolo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends Activity implements   OnClickListener{

	EditText username = null;
	EditText password = null;
	EditText email = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		email = (EditText) findViewById(R.id.email);
		Button signup = (Button) findViewById(R.id.signup);
		signup.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		
	}

}