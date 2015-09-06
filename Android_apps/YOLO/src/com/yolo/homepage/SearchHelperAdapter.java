package com.yolo.homepage;

import java.util.ArrayList;

import com.example.yolo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchHelperAdapter extends BaseAdapter {

	private ArrayList<Items> listData;
	private ArrayList<Items> allData;
	Context mContext =null;
	private LayoutInflater layoutInflater;

	public SearchHelperAdapter(Context context, ArrayList<Items> listData) {
		this.mContext = context;
		this.listData = new ArrayList<Items>();
		this.listData.addAll(listData);
		this.allData = new ArrayList<Items>();
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

		Items item = listData.get(position);
		if (item != null) {
			
		//	holder.idView.setText(""+item.getID());
			holder.nameView.setText(item.getName());
		
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
		    
		   for(Items continent: allData){
		     
		     if(continent.getName().toLowerCase().contains(query)){
		      listData.add(continent);
		     }
		    }
		    
		   
		  }
		   
		  //Log.v("MyListAdapter", String.valueOf(continentList.size()));
		  notifyDataSetChanged();
		   
		 }

	
}