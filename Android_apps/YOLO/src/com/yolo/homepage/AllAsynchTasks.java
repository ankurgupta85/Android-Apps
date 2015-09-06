package com.yolo.homepage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.AsyncTask;

import com.yolo.datastore.PastSearchDataStore;
import com.yolo.datastore.SearchScreenDataStore;
import com.yolo.util.ApplicationHashMaps;
import com.yolo.util.SearchCacheUtil;

public class AllAsynchTasks {

}

class DownloadFilter extends AsyncTask<Object, Object, Object> {

	@Override
	protected Object doInBackground(Object... params) {
		Context context = (Context) params[0];
		//SearchScreenDataStore ssds = (SearchScreenDataStore) params[1];
		int subcategoryID = (Integer) params[1];
		String companyName = (String)params[2];
		int companyID = 0;
		if (companyName != null && !companyName.isEmpty()) {

			CompaniesInfoParser companyParser = CompaniesInfoParser
					.getInstance();
			companyID = companyParser.getCompaniesHashMap().get(companyName);

		}

		saveFilters(context, subcategoryID, companyID);

		return null;
	}

	private void saveFilters(Context context, int subID, int companyID) {

		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);

		// String url
		// ="http://192.168.0.194:5000/yolo/api/v1.0/subcategories?category_id="+categoryID;
		// context = (Context) params[1];

		HashMap<String, String> urlCache = ApplicationHashMaps.getURLCache();
		String filtersURL = urlCache.get("filters");
		String filtersFile = ApplicationHashMaps.getFileCache().get("filters");
		filtersURL = filtersURL + subID;
		if (companyID != 0) {
			filtersURL = filtersURL + "&companyID=" + companyID;
		}

		HttpGet request = new HttpGet(filtersURL);
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			if (reader != null) {
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
					// Toast.makeText(context, "Found text: "+line,
					// Toast.LENGTH_SHORT).show();
					data.append(line);
				}
			}
			writeTofile(context, data.toString(), filtersFile);
			// readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// return data.toString();

	}

	private void writeTofile(Context context, String line, String filename)
			throws IOException {
		File file = context.getFileStreamPath(filename);
		if (file.exists()) {
			file.delete();
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(
					filename, context.MODE_PRIVATE));
			out.write(line);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class SaveSearchEntry extends AsyncTask<Object, Object, Object> {

	@Override
	protected Object doInBackground(Object... arg0) {
		Context context = (Context) arg0[0];
		SearchScreenDataStore ssds = (SearchScreenDataStore) arg0[1];
		SearchCacheUtil util = new SearchCacheUtil(context);
		util.updateCache(ssds);
		HashMap<Integer, PastSearchDataStore> searchCache = ApplicationHashMaps
				.getSearchCache();
		System.out.println("In Save Search entry Asynch task");
		System.out.println(searchCache);
		// Toast.makeText(getApplicationContext(),
		// "Search cache: "+searchCache.toString(),
		// Toast.LENGTH_SHORT).show();

		return null;
	}
}

class SaveSearchCache extends AsyncTask<Object, Object, Object> {

	@Override
	protected Object doInBackground(Object... arg0) {
		Context context = (Context) arg0[0];
		SearchCacheUtil util = new SearchCacheUtil(context);
		util.dumpCache();

		return null;
	}
}
