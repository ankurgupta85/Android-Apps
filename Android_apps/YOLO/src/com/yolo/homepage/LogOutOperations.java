package com.yolo.homepage;

import com.yolo.mainscreen.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class LogOutOperations {
	
	Context context = null;
	
	public LogOutOperations(Context context)
	{
		this.context = context;
	}
	
	
	public void performLogoutOperations()
	{
		SharedPreferences pref = this.context.getSharedPreferences("loginToken",this.context.MODE_PRIVATE);
		if(pref.contains("remember"))
		{
			SharedPreferences.Editor editor = pref.edit();
			editor.remove("remember");
			editor.remove("username");
			editor.commit();
		}
		
		Intent logoutIntent = new Intent(this.context, LogoutService.class);
		this.context.startService(logoutIntent);
		
		}
	
	

}
