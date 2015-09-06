package com.example.yolo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LocationPicker extends Activity implements LocationListener {
	String city, state;
	ProgressBar pb = null;
	private LocationManager locationManager = null;
	 int time_interval = 1000*60*1;
	Intent returnIntent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_picker);
		Intent intent = getIntent();
		returnIntent = new Intent(getApplicationContext(), HomePage.class);
		returnIntent.putExtra("category", intent.getStringExtra("category"));
		returnIntent.putExtra("to", intent.getStringExtra("to"));
		returnIntent.putExtra("from", intent.getStringExtra("from"));
		returnIntent
				.putExtra("categoryID", intent.getIntExtra("categoryID", 0));
		returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		returnIntent.putExtra("subcategory",
				intent.getStringExtra("subcategory"));
		returnIntent.putExtra("subcategoryID",
				intent.getIntExtra("subcategoryID", 0));
		returnIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		final ListView locationlist = (ListView) findViewById(R.id.locationList);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		String[] statesList = { "Current Location", "San Francisco" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.activity_list_item, android.R.id.text1,
				statesList);
		locationlist.setAdapter(adapter);
		locationlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int itemPosition = position;
				String itemValue = (String) locationlist
						.getItemAtPosition(position);

				Toast.makeText(
						getApplicationContext(),
						"Position :" + itemPosition + "  ListItem : "
								+ itemValue, Toast.LENGTH_SHORT).show();
				if (itemPosition == 0) {

					boolean connected = getConnectedStatus();
					if (!connected) {
						alertConnection();
					} else {
						getUserAddress();
					}
					/*
					 * if(userAddress !=null) {
					 * Toast.makeText(getApplicationContext(),
					 * "User address: "+userAddress.getLocality(),
					 * Toast.LENGTH_SHORT).show(); } else {
					 * Toast.makeText(getApplicationContext(),
					 * "Not able to find User address",
					 * Toast.LENGTH_SHORT).show();
					 * 
					 * }
					 */
				} else {

					returnIntent.putExtra("city", itemValue);
					startActivity(returnIntent);

				}

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_picker, menu);
		return true;
	}

	private boolean getConnectedStatus() {

		boolean isConnected = false;
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		boolean isMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		if (isMobile || isWifi) {
			isConnected = true;
		}

		return isConnected;
	}

	private void getUserAddress() {
		// Address address=null;
		boolean flag = displayGpsStatus();
		if (flag) {

			pb.setVisibility(View.VISIBLE);

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, time_interval, 10, this);

		} else {
			alertGPS("Gps Status!!", "Your GPS is: OFF");
		}

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

	/*----------Method to create an AlertBox ------------- */
	protected void alertGPS(String title, String mymessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your Device's GPS is Disable")
				.setCancelable(false)
				.setTitle("** Gps Status **")
				.setPositiveButton("Gps On",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// finish the current activity
								// AlertBoxAdvance.this.finish();
								Intent myIntent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// cancel the dialog box
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*----Method to Check GPS is enable or disable ----- */
	private Boolean displayGpsStatus() {
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(
				contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {
			return true;

		} else {
			return false;
		}
	}

	@Override
	public void onLocationChanged(Location location) {

		Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
			if (addresses.size() > 0)
				System.out.println(addresses.get(0).getLocality());
			Toast.makeText(getApplicationContext(),
					"City: " + addresses.get(0).getLocality(),
					Toast.LENGTH_SHORT).show();
			pb.setVisibility(View.INVISIBLE);
			
			String city = addresses.get(0).getLocality();
			String state = addresses.get(0).getAdminArea();
			if(city == null || city.isEmpty())
			{
				Toast.makeText(getApplicationContext(), "Not able to determine your location", Toast.LENGTH_SHORT).show();
			}
			else
			{
				returnIntent.putExtra("city", city);
				returnIntent.putExtra("state", state);
				
				
			}
			locationManager.removeUpdates(this);
			startActivity(returnIntent);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
