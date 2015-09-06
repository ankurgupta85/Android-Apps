package com.yolo.datastore;

public class KnowProductDataStore {
	
	private static KnowProductDataStore kpds;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getProd_desc() {
		return prod_desc;
	}
	public void setProd_desc(String prod_desc) {
		this.prod_desc = prod_desc;
	}
	private String to=null;
	private String from = null;
	private String prod_desc = null;
	
	private  KnowProductDataStore() {
		// TODO Auto-generated constructor stub
	}
	public static KnowProductDataStore getInstance() {
		if (kpds == null) {
			kpds= new KnowProductDataStore();

		}

		return kpds;

	}

	

}
