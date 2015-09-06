package com.yolo.homepage;

import com.yolo.datastore.PastSearchDataStore;

public class PastSearchData{
	 
    private int hash;
    private PastSearchDataStore sds;
    
 
    public int getHash() {
		return hash;
	}


	public void setHash(int hash) {
		this.hash = hash;
	}


	public PastSearchDataStore getSds() {
		return sds;
	}


	public void setSds(PastSearchDataStore sds) {
		this.sds = sds;
	}


	@Override
    public String toString() {
        return "[ id=" + hash+ ", Name=" + 
                sds.toString()+"]";
    }

}
