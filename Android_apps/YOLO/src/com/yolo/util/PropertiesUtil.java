package com.yolo.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import android.content.Context;


public class PropertiesUtil {

	Context context =null;
	private String urlProperties = "url.properties";
	private String fileProperties ="filename.properties";
	public PropertiesUtil(Context context)
	{
		this.context = context;
	}
	
	
	public void createURLCache()
	{
		HashMap<String,String> urlCache = ApplicationHashMaps.getURLCache();
		Properties prop = new Properties();
		try {
			prop = this.loadPropties(urlProperties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		Iterator iter = prop.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String,String> pairs =(Entry<String,String>) iter.next();
			String key = pairs.getKey();
			String value = pairs.getValue();
			urlCache.put(key, value);
		
		}
		
		
		
		
	}
	
	
	public void createFileCache()
	{
		HashMap<String,String> fileCache = ApplicationHashMaps.getFileCache();
		Properties prop = new Properties();
		try {
			prop = this.loadPropties(fileProperties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		Iterator iter = prop.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String,String> pairs =(Entry<String,String>) iter.next();
			String key = pairs.getKey();
			String value = pairs.getValue();
			fileCache.put(key, value);
		
		}
		
		
		
	}
	

	//Write this function in your activity -

	private Properties loadPropties(String filename) throws IOException {
	    /*String[] fileList = { "url.properties" };
	    Properties prop = new Properties();
	    for (int i = fileList.length - 1; i >= 0; i--) {
	    String file = fileList[i];
	    */
		Properties prop = new Properties();
	    
		try {
	      InputStream fileStream = this.context.getAssets().open(filename);
	      prop.load(fileStream);
	      fileStream.close();
	      }  catch (FileNotFoundException e) {
	    	  e.printStackTrace();
	      }
	  //}
	  return prop;
	 }
	
}

