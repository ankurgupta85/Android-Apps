package com.yolo.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.yolo.datastore.PastSearchDataStore;
import com.yolo.datastore.SearchScreenDataStore;


// This class contains all the cache methods.
public class SearchCacheUtil {
	
	private final int MAX_ENTRIES = 100;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmss");
	
	// Search json in format {"search":{"hash1":{data},"hash2":{data}}}
		private Context context = null;

	public SearchCacheUtil(Context context) {
		this.context = context;
	}

	// Method to populate and create cache for past search.
	// Called when user logs in to app
	public void createCache()
	{
		HashMap<Integer, PastSearchDataStore> searchCache = ApplicationHashMaps.getSearchCache();
		searchCache.clear();
		SearchDataAccessObject sdao = new SearchDataAccessObject(this.context);
		sdao.getAllSearches();
			
		
		
		
		
		System.out.println(searchCache.size());
	}
	
	private PastSearchDataStore createNewSearchObject(int hash, SearchScreenDataStore search)
	{
		PastSearchDataStore sds = new PastSearchDataStore();
		sds.setHash(hash);
		sds.setCategory(search.getCategory());
		sds.setSubcategory(search.getSubcategory());
		sds.setFrom(search.getFrom());
		sds.setTo(search.getTo());
		sds.setCity(search.getCity());
		sds.setCompanyName(search.getCompanyName());
		sds.setVendorName(search.getVendorName());
		String timestamp= s.format(new Date());
		sds.setCreatedAt(timestamp);
		sds.setUpdatedAt(timestamp);
		sds.setCount(1);
		
		return sds;
		
	}
	
	// Method to smart dump everything from cache to database. 
	// Called when onPause or onFinish is called. Additionally called when user logs out. 
	public void dumpCache()
	{
		SearchDataAccessObject sdao = new SearchDataAccessObject(this.context);
		sdao.updateDatabase();
		
	}
	
	public void updateCache(SearchScreenDataStore search)
	{
		HashMap<Integer, PastSearchDataStore> searchCache = ApplicationHashMaps.getSearchCache();
		int searchCode = createHashCode(search);
		// If it already contains the entry, needs to update count and updatedTimestamp
		if(searchCache.containsKey(searchCode))
		{
			String timestamp= s.format(new Date());
			PastSearchDataStore sds = searchCache.get(searchCode);
			sds.setUpdatedAt(timestamp);
			if(sds.getCount() == Long.MAX_VALUE)
			{
				sds.setCount(sds.getCount());
			}
			else
			{
				sds.setCount(sds.getCount()+1);
			}
			
		}
		// if cache does not contain that search object
		else
		{
			// If size >=100, need to find the hash to make count =0 and add new entry
			// To make count =0, we need to find hash based on lowest values of count and updatedAt 
			if(searchCache.size()>=MAX_ENTRIES)
			{
				long minimumCount = getMininumCountValue();
				int hash = getHashToRemove(minimumCount);
				PastSearchDataStore tempsds = searchCache.get(hash);
				tempsds.setCount(0);
				searchCache.put(hash, tempsds);
				
				
			}
				PastSearchDataStore sds = createNewSearchObject(searchCode, search);
				searchCache.put(searchCode, sds);
				System.out.println("In Update cache");
				
			
			
		}
		
		ApplicationHashMaps.setDirty(true);
	}
	
	
	
	
	
	private int getHashToRemove(long minimumCount)
	{
		int hash = 0;
		HashMap<Integer, PastSearchDataStore> allSearch = ApplicationHashMaps.getSearchCache();
		long min = Long.MAX_VALUE;
		
		Iterator it = allSearch.entrySet().iterator();
		while(it.hasNext())
		{
			@SuppressWarnings("unchecked")
			Entry<Integer,PastSearchDataStore> pairs = (Entry<Integer,PastSearchDataStore>)it.next();
			PastSearchDataStore sds = pairs.getValue();
			long currentCount = sds.getCount();
			if(currentCount == minimumCount)
			{
				long updateTime = Long.parseLong(sds.getUpdatedAt());
				if(min > updateTime)
				{
					min = updateTime;
					hash = pairs.getKey();
				}
			}
			
		}
		
		
		
		
		
		
		return hash;
		
	}
	
	private long getMininumCountValue()
	{
		
		HashMap<Integer, PastSearchDataStore> allSearch = ApplicationHashMaps.getSearchCache();
		long min = Long.MAX_VALUE;
		Iterator it = allSearch.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer,PastSearchDataStore> pairs = (Entry<Integer,PastSearchDataStore>)it.next();
			PastSearchDataStore sds = pairs.getValue();
			long currentCount = sds.getCount();
			if(min>=currentCount)
			{
				min = currentCount;
			}
			
		}
		
		return min;
	}
	
	
	
	
	
	public int createHashCode(SearchScreenDataStore ssds) {

		StringBuilder formedString = new StringBuilder(ssds.getCategory() + "_"
				+ ssds.getSubcategory() + "_" + ssds.getFrom() + "_" 
				+ ssds.getTo() + "_" + ssds.getCity());
		if (ssds.getCompanyName() != null
				&& !ssds.getCompanyName().trim().isEmpty()) {
			formedString.append("_" + ssds.getCompanyName());
		}
		if (ssds.getVendorName() != null
				&& !ssds.getVendorName().trim().isEmpty()) {
			formedString.append("_" + ssds.getVendorName());

		}
		String tempString = formedString.toString();

		System.out.println(tempString);
		int hash = tempString.hashCode();
		//Toast.makeText(this.context, "Hash: "+hash, Toast.LENGTH_SHORT).show();
		return hash;

	}

}
