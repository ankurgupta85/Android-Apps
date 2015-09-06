package com.yolo.datastore;

public class PastSearchDataStore {

	private long hash;
	private String category;
	private int categoryID;
	private String subcategory;
	private int subcategoryID;
	private String from;
	private String to;
	private String companyName;
	private String city;
	private String vendorName;
	private String createdAt;
	private String updatedAt;
	private long count;

	public long getHash() {
		return hash;
	}

	public void setHash(long hash) {
		this.hash = hash;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public int getSubcategoryID() {
		return subcategoryID;
	}

	public void setSubcategoryID(int subcategoryID) {
		this.subcategoryID = subcategoryID;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		if (companyName == null) {
			companyName = "";
		}
		this.companyName = companyName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getVendorName() {
		if (vendorName == null) {
			vendorName = "";
		}
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public String toString() {

	/*	return this.getSubcategory()+ " in "+this.getCategory()   + " \nDate Range:"
				+ this.getFrom() + " " + this.getTo() + " \n Location:" + this.getCity()
				+ " \nCompany:" + this.getCompanyName() + " Vendor:" + this.getVendorName()+" \nCount:"+this.getCount();
*/
		return this.getCategory() + " " + this.getSubcategory() + " "
				+ this.getFrom() + " " + this.getTo() + " " + this.getCity()
				+ " " + this.getCompanyName() + " " + this.getVendorName()+" "+this.getCount();

	}

}
