package com.yolo.homepage;

import java.util.ArrayList;

import com.example.yolo.R;
import com.yolo.homepage.SearchHelperAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PastSearchHelperAdapter extends BaseAdapter {

	private ArrayList<PastSearchData> listData;
	private ArrayList<PastSearchData> allData;
	Context mContext =null;
	private LayoutInflater layoutInflater;
	
	
	public PastSearchHelperAdapter(Context context, ArrayList<PastSearchData> listData) {
		this.mContext = context;
		this.listData = new ArrayList<PastSearchData>();
		this.listData.addAll(listData);
		this.allData = new ArrayList<PastSearchData>();
		this.allData.addAll(listData);
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater
					.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
		//	holder.idView = (TextView) convertView.findViewById(R.id.id);
			holder.nameView = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PastSearchData item = listData.get(position);
		if (item != null) {
			
		//	holder.idView.setText(""+item.getID());
			holder.nameView.setText(item.getSds().toString());
		}
		return convertView;
	}

	static class ViewHolder {
	//	TextView idView;
		TextView nameView;

	}
	
	public void filterData(String query){
		   
		  query = query.toLowerCase();
		  //Log.v("MyListAdapter", String.valueOf(continentList.size()));
		  listData.clear();
		   
		  if(query.isEmpty()){
		   listData.addAll(allData);
		  }
		  else {
		    
		   for(PastSearchData continent: allData){
		     
		     if(continent.toString().toLowerCase().contains(query)){
		      listData.add(continent);
		     }
		    }
		    
		   
		  }
		   
		  //Log.v("MyListAdapter", String.valueOf(continentList.size()));
		  notifyDataSetChanged();
		   
		 }


	
	
	
	
	
}
