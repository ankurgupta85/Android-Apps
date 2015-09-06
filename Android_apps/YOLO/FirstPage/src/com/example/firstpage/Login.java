package com.example.firstpage;

import android.os.Bundle;
import android.app.Activity;
import android.text.LoginFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.SearchView.OnCloseListener;

public class Login extends Activity implements OnClickListener {

	EditText login_username = null;
	EditText login_password = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		login_username = (EditText) findViewById(R.id.login_username);
		login_username.requestFocus();
		login_password = (EditText) findViewById(R.id.login_password);
		Button signin = (Button) findViewById(R.id.signin);
		signin.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signin: {
			String username = login_username.getText().toString();
			String password = login_password.getText().toString();
			if(username.trim().isEmpty() || password.trim().isEmpty())
			{
				Toast.makeText(getApplicationContext(), "Please enter login information.", Toast.LENGTH_SHORT).show();
				login_username.requestFocus();
				
			}
			else
			{
				if(username.equals("ankur") && password.equals("ankur"))
				{
					Toast.makeText(getApplicationContext(), "Valid login information", Toast.LENGTH_SHORT).show();
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), "InValid login information", Toast.LENGTH_SHORT).show();
					login_username.requestFocus();
				}
			}
			break;
		}
		}
	}

}
