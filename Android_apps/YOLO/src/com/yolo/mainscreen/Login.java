package com.yolo.mainscreen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yolo.R;
import com.yolo.datastore.UserDataStore;
import com.yolo.homepage.CompaniesInfoParser;
import com.yolo.homepage.Homepage;
import com.yolo.util.ApplicationHashMaps;
import com.yolo.util.NetworkUtil;
import com.yolo.util.SearchDataAccessObject;
import com.yolo.util.UserDataAccessObject;

public class Login extends Activity implements OnClickListener {

	EditText login_username = null;
	EditText login_password = null;
	CheckBox remember = null;

	// String url = "http://192.168.0.194:5000/yolo/api/v1.0/categories";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SharedPreferences pref = getSharedPreferences("loginToken",
				MODE_PRIVATE);
		boolean remembered = pref.getBoolean("remember", false);

		// Load the caches from properties files.
		new LoadURLCache().execute(getApplicationContext());
		new LoadFileCache().execute(getApplicationContext());
		

		// If user selects "Remember me", no need to ask user for login again. 
		if (remembered) {
			if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
				alertConnection();
			} else {
				// new DownloadData().execute(url,getApplicationContext());

				new DownloadData().execute(getApplicationContext());

				Intent intent = new Intent(getApplicationContext(),
						Homepage.class);
				
				// Load search cache. 
				new LoadSearchCache().execute(getApplicationContext());

				// new LoadSearchCache().execute(getApplicationContext());
				// new LoadCompanyInfo().execute("true");
				CompaniesInfoParser.getInstance().startLoading(
						getApplicationContext());
				startActivity(intent);
			}

		}
		login_username = (EditText) findViewById(R.id.login_username);
		login_username.requestFocus();
		login_password = (EditText) findViewById(R.id.login_password);
		Button signin = (Button) findViewById(R.id.signin);
		signin.setOnClickListener(this);
		TextView backdoor = (TextView) findViewById(R.id.Backdoor);
		TextView clear_db = (TextView) findViewById(R.id.clear_db);
		clear_db.setOnClickListener(this);
		remember = (CheckBox) findViewById(R.id.remember);

		backdoor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						Homepage.class);
				new LoadSearchCache().execute(getApplicationContext());

				// new LoadSearchCache().execute(getApplicationContext());
				// new LoadCompanyInfo().execute("true");
				CompaniesInfoParser.getInstance().startLoading(
						getApplicationContext());
				startActivity(intent);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private void alertConnection() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Please select one of the option.")
				.setCancelable(false)
				.setTitle("Internet Connection failed")
				.setPositiveButton("Wi-Fi",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// finish the current activity
								// AlertBoxAdvance.this.finish();
								Intent myIntent = new Intent(
										Settings.ACTION_WIFI_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						})
				.setNegativeButton("Mobile Data",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent myIntent = new Intent(
										Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// Sign in case. This will trigger when user click "Sign in" button.
		case R.id.signin: {

			if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
				alertConnection();
			} else {
				String username = login_username.getText().toString();
				String password = login_password.getText().toString();
				if (username.trim().isEmpty() || password.trim().isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Please enter login information.",
							Toast.LENGTH_SHORT).show();
					login_username.requestFocus();

				} else {
					// Valid user information.
					// Currently checking in database in sqlite.
					// Need to replace it with API call with user information
					// for validation.
					UserDataStore user = new UserDataStore();
					user.setUsername(username);
					user.setPassword(password);
					UserDataAccessObject udao = new UserDataAccessObject(
							getApplicationContext());
					UserDataStore temp = udao.selectUser(user);
					if (temp == null) {
						Toast.makeText(getApplicationContext(),
								"User does not exist", Toast.LENGTH_SHORT)
								.show();

					} else {
						if (temp.getPassword().equals(user.getPassword())) {

							// Check if remember is checked, need to set token
							// in there...
							// this will help in checking in the next login,
							// will directly redirect to main page.
							if (remember.isChecked()) {

								// Need to keep token which will be checked on
								// next start of app.
								SharedPreferences pref = getSharedPreferences(
										"loginToken", MODE_PRIVATE);
								SharedPreferences.Editor editor = pref.edit();
								editor.putBoolean("remember", true);
								editor.putString("username", username);
								editor.commit();
							}

							// If valid user, need to redirect user but need to
							// load Past Search cache.
							Intent intent = new Intent(Login.this,
									Homepage.class);
							// new
							// DownloadData().execute(url,getApplicationContext());
							new DownloadData().execute(getApplicationContext());

							new LoadSearchCache()
									.execute(getApplicationContext());

							// new
							// LoadSearchCache().execute(getApplicationContext());
							// new LoadCompanyInfo().execute("true");
							// Also need to get companies and vendors name in
							// cache.
							CompaniesInfoParser.getInstance().startLoading(
									getApplicationContext());

							startActivity(intent);
						} else {
							Toast.makeText(getApplicationContext(),
									"Passwords didnt match", Toast.LENGTH_SHORT)
									.show();

						}
					}

				}
			}
			break;

		}

		// Need to comment and delete this code. Only for debugging purposes.
		case R.id.clear_db: {
			// Delete database.
			SearchDataAccessObject sdao = new SearchDataAccessObject(
					getApplicationContext());
			sdao.deleteAllSearch();
			UserDataAccessObject udao = new UserDataAccessObject(
					getApplicationContext());
			udao.deleteAllUsers();

		}
		}
	}

	@Override
	public void onBackPressed() {

	}

	// Disable pressing the back button on android.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// your code
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	// Asynch task to download data - categories, companies, vendors, user data.
	private class DownloadData extends AsyncTask<Object, Object, Object> {

		Context context = null;
		// ProgressDialog progress = null;

		ProgressDialog progressDialog;

		// declare other objects as per your need
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(Login.this);
			progressDialog.setMessage("Signing in...");
			progressDialog.show();
			// do initialization of required objects objects here
		};

		@Override
		protected Object doInBackground(Object... params) {
			context = (Context) params[0];
			String categoryURL = ApplicationHashMaps.getURLCache().get(
					"category");
			String categoriesFile = ApplicationHashMaps.getFileCache().get("category");
			
			String companiesURL = ApplicationHashMaps.getURLCache().get("companies");
			String companiesFile = ApplicationHashMaps.getFileCache().get("companies");
			
			String vendorsURL = ApplicationHashMaps.getURLCache().get("vendors");
			String vendorsFile = ApplicationHashMaps.getFileCache().get("vendors");
			
			saveData(categoryURL, categoriesFile);
			saveData(companiesURL, companiesFile);
			saveData(vendorsURL, vendorsFile);
			
			
			// Need to add code to download user data. (Past searches)
			// Need to push user data into sqlite database for user. 
			return null;
		}


		
		private void saveData(String url, String filename)
		{
			 final HttpParams httpParams = new BasicHttpParams();
			  HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpClient client = new DefaultHttpClient(httpParams);
			// String url =(String) params[0];
			
			HttpGet request = new HttpGet(url);
			HttpResponse response = null;
			try {
				response = client.execute(request);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			BufferedReader reader = null;
			try {
				if (response != null) {
					reader = new BufferedReader(new InputStreamReader(response
							.getEntity().getContent()));
				}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String line = "";
			StringBuffer data = new StringBuffer();
			try {
				if(reader!=null)
				{
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
					// Toast.makeText(context, "Found text: "+line,
					// Toast.LENGTH_SHORT).show();
					data.append(line);
				}
			}
				
				writeTofile(data.toString(),filename);
				// readFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		@Override
		protected void onPostExecute(Object result) {

			super.onPostExecute(result);
			progressDialog.dismiss();
		}

		@SuppressWarnings("static-access")
		private void writeTofile(String line,String filename) throws IOException {
			File file = context.getFileStreamPath(filename);
			if (file.exists()) {
				file.delete();
			}
			try {
				OutputStreamWriter out = new OutputStreamWriter(
						context.openFileOutput(filename,
								context.MODE_PRIVATE));
				out.write(line);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
