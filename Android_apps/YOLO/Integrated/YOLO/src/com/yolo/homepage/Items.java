package com.yolo.homepage;

public class Items {
	 
    private int id;
    private String name;
    
 
    public int getID() {
        return id;
    }
 
    public void setID(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String Name) {
        this.name = Name;
    }
 
    @Override
    public String toString() {
        return "[ id=" + id+ ", Name=" + 
                name+"]";
    }
}