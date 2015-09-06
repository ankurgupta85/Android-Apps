package com.yolo.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {

	
	
	
	public static boolean isNetworkAvailable(Context context)
	{
		boolean networkAvailable = false;
		networkAvailable = getConnectedStatus(context);
		
		return networkAvailable;
	}
	

	private static boolean getConnectedStatus(Context context) {

		boolean isConnected = false;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		boolean isMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		if (isMobile || isWifi) {
			isConnected = true;
		}

		return isConnected;
	}
	
	public static String getCity(Context context)
	{
		String city="";
		
		
		return city;
	}

}
