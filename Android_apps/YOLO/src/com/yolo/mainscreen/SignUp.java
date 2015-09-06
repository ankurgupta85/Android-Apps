package com.yolo.mainscreen;

import java.util.HashMap;

import com.example.yolo.R;
import com.yolo.datastore.PastSearchDataStore;
import com.yolo.datastore.UserDataStore;
import com.yolo.homepage.CompaniesInfoParser;
import com.yolo.homepage.Homepage;

import com.yolo.util.ApplicationHashMaps;
import com.yolo.util.SearchCacheUtil;
import com.yolo.util.UserDataAccessObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends Activity implements   OnClickListener{

	EditText username = null;
	EditText password = null;
	EditText email = null;
	CheckBox remember = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		email = (EditText) findViewById(R.id.email);
		Button signup = (Button) findViewById(R.id.signup);
		signup.setOnClickListener(this);
		Button clear =(Button) findViewById(R.id.clear);
		clear.setOnClickListener(this);
		remember = (CheckBox)findViewById(R.id.remember_signup);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.signup:
		{
			
			String username_value = username.getText().toString();
			String password_value = password.getText().toString();
			String email_value = email.getText().toString();
			if(username_value.isEmpty() || password_value.isEmpty() || email_value.isEmpty())
			{
				Toast.makeText(getApplicationContext(), "All values required", Toast.LENGTH_SHORT).show();
				
				
			}
			else if(!isValidEmail(email_value))
			{
				Toast.makeText(getApplicationContext(), "Not a valid email address", Toast.LENGTH_SHORT).show();
				
			}
			else
			{
				
				UserDataStore user = new UserDataStore();
				user.setUsername(username_value);
				user.setPassword(password_value);
				user.setEmail(email_value);
				UserDataAccessObject udao = new UserDataAccessObject(getApplicationContext());
				UserDataStore temp = udao.selectUser(user);
				if(temp == null)
				{
					boolean created = udao.createUser(user);
					Toast.makeText(getApplicationContext(), "User created: "+created, Toast.LENGTH_SHORT).show();
					if(remember.isChecked())
					{
						
						// Need to keep token which will be checked on next start of app. 
						SharedPreferences pref = getSharedPreferences("loginToken",MODE_PRIVATE);
						SharedPreferences.Editor editor = pref.edit();
						editor.putBoolean("remember", true);
						editor.putString("username", username_value);
						editor.commit();
					}
					Intent intent = new Intent(SignUp.this, Homepage.class);
					new LoadSearchCache().execute(getApplicationContext());

				// new LoadSearchCache().execute(getApplicationContext());
				// new LoadCompanyInfo().execute("true");
					CompaniesInfoParser.getInstance().startLoading(
						getApplicationContext());

					startActivity(intent);

				}
				else
				{
					Toast.makeText(getApplicationContext(), "User Already exists", Toast.LENGTH_SHORT).show();
				}
			}
			
			
			break;
		}
		
		case R.id.clear:
		{
		
			
			username.setText("");
			username.setHint("Username");
			password.setText("");
			password.setHint("Password");
			email.setText("");
			email.setHint("Email ID");
			break;
		}
		
		
		}
		
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


	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	

	private class LoadSearchCache extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... arg0) {

			Context context = (Context) arg0[0];
			HashMap<Integer, PastSearchDataStore> searchCache = ApplicationHashMaps
					.getSearchCache();
			if (searchCache.size() == 0) {
				SearchCacheUtil util = new SearchCacheUtil(context);
				util.createCache();

			}

			return null;
		}
	}

}