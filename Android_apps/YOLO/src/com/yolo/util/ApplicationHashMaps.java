package com.yolo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.yolo.datastore.PastSearchDataStore;

public class ApplicationHashMaps {
	
	private static Map<Integer,PastSearchDataStore> searchMap = null;
	private static Map<String,String> urlMap = null;
	private static Map<String,ArrayList<String>> filterMap = null;
	private static Map<String,String> fileMap = null;
	
	//private static Map<Integer,WishListDataStore> wishlistMap = new HashMap<Integer,WishListDataStore>();
	//private static boolean searchCacheLoaded= false;
	private static boolean dirty = false;
	private  ApplicationHashMaps(){
		
	}
	
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer,PastSearchDataStore> getSearchCache()
	{
		
		if(searchMap == null)
		{
			searchMap = new HashMap<Integer,PastSearchDataStore>();
			
		}
		
		
		return (HashMap<Integer, PastSearchDataStore>) searchMap;
		
		
	}
	
	
	

	@SuppressLint("UseSparseArrays")
	public static HashMap<String,String> getURLCache()
	{
		
		if(urlMap == null)
		{
			urlMap = new HashMap<String,String>();
			
		}
		
		
		return (HashMap<String,String>) urlMap;
		
		
	}
	
	

	@SuppressLint("UseSparseArrays")
	public static HashMap<String,String> getFileCache()
	{
		
		if(fileMap== null)
		{
			fileMap = new HashMap<String,String>();
			
		}
		
		
		return (HashMap<String,String>) fileMap;
		
		
	}
	
	
	
	public static void setDirty(boolean dirtybit)
	{
		dirty = dirtybit;
	}
	
	public static boolean isDirty()
	{
		return dirty;
	}
	
	/*public static HashMap<Integer,WishListDataStore> getWishListCache()
	{
		
		if(wishlistMap == null)
		{
			wishlistMap= new HashMap<Integer,WishListDataStore>();
			
		}
		
		return (HashMap<Integer, SearchDataStore>) wishlistMap;
		
		
	}*/
	
}
