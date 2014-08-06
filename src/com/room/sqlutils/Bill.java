package com.room.sqlutils;

public class Bill {
	private String id;
	private String name;
	private String item;
	private String money;
	private String date;
	
	public Bill(String id, String name, String item, String money, String date) {
		super();
		this.id = id;
		this.name = name;
		this.item = item;
		this.money = money;
		this.date = date;
	}
	
	public Bill(){}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Bill [id=" + id + ", name=" + name + ", item=" + item
				+ ", money=" + money + ", date=" + date + "]";
	}
}
