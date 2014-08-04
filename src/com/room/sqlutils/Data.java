package com.room.sqlutils;

import java.util.List;

public class Data {
   private String title;
   private List<Bill> bills;
public List<Bill> getBills() {
	return bills;
}
public void setBills(List<Bill> bills) {
	this.bills = bills;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
}
