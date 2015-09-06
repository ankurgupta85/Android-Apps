package com.yolo.test;

import com.yolo.datastore.SearchScreenDataStore;
public class SearchScreenDataStoreTest {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SearchScreenDataStore ssds = SearchScreenDataStore.getInstance();
		ssds.setCategory("Food");
		ssds.setSubcategory("Fruits");
		ssds.setTo("10-22-2013");
		ssds.setFrom("10-22-2013");
		ssds.setCity("San Francisco");
		
		
	}
	
	
}
