package com.puzzle.n_puzzle_project;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;

public class Sort_Records {

	public TreeMap<Long, ArrayList<String>> sort_records(String filename,Context context) throws Exception {
		Map<Long, ArrayList<String>> map = new TreeMap<Long, ArrayList<String>>(Collections.reverseOrder());
		
		InputStream inputStream = context.openFileInput(filename);
		if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	       
		
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			
			ArrayList<String> temp =null;
			if(map.containsKey(getField(line)))
			{
				temp = map.get(getField(line));
				
			}
			else
			{
				temp = new ArrayList<String>(); 
			}
			temp.add(line);
			map.put(getField(line), temp);
		}
		bufferedReader.close();
		 }
		 
		 return (TreeMap<Long, ArrayList<String>>)map;
	}

	private static Long getField(String line) {
		return Long.parseLong(line.split(" ")[2]);// extract value you want to sort on
	}
}
