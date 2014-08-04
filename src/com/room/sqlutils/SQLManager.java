package com.room.sqlutils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLManager {
	public static final String TABLE_DEVICE = "bill";
	public static final String TABLE_PERSON = "person";
	private SQLHelper mDbHelper;
	public SQLManager(Context context)
	{
		mDbHelper=new SQLHelper(context);
	}
	//增
	public long addBill(String name, String item, Double money, Date date) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put("name", name);
		values.put("item", item);
		values.put("money", money);
		values.put("date", simpleDateFormat.format(date));

		long ret = db.insertOrThrow(TABLE_DEVICE, null, values);
		db.close();

		return ret;
	}
	//改
	public void updateBill(int id,String name, String item, Double money, Date date) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put("name", name);
		values.put("item", item);
		values.put("money", money);
		values.put("date", simpleDateFormat.format(date));
		db.update(TABLE_DEVICE, values, "_id = '" + id + "'", null);
		db.close();
	}
	//删
	public void removeBill(String id) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(TABLE_DEVICE, "_id = '" + id + "'", null);
		db.close();
	}
	//查
	@SuppressLint("NewApi")
	public List<Bill> getBills(String name) {
		List<Bill> bills=new ArrayList<Bill>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor=null;
		if(name.isEmpty())
		{
			cursor = db.query(TABLE_DEVICE, null, null, null, null, null, null);
		}else
		{
			cursor = db.query(TABLE_DEVICE, null, "name=?", new String[]{name}, null, null, null);
		}
	   
        if(cursor != null){ 
        	while(cursor.moveToNext())
        	{
        		Bill bill = new Bill();  
	            int _id = cursor.getInt(cursor.getColumnIndex("_id"));  
	            String n = cursor.getString(cursor.getColumnIndex("name"));  
	            String i = cursor.getString(cursor.getColumnIndex("item"));
	            String m = cursor.getString(cursor.getColumnIndex("money"));
	            String d = cursor.getString(cursor.getColumnIndex("date"));
	            bill.setId(_id+"");  
	            bill.setName(n); 
	            bill.setItem(i);
	            bill.setMoney(m);
	            bill.setDate(d);
	            bills.add(bill);
        	}
        	cursor.close();
        }
	    db.close();
		return bills;
	}
	
	@SuppressLint("NewApi")
	public List<Bill> getBills(String name,String date) {
		List<Bill> bills=new ArrayList<Bill>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor=null;
		if(name.isEmpty())
		{
			cursor = db.query(TABLE_DEVICE, null, null, null, null, null, null);
		}else
		{
			cursor = db.query(TABLE_DEVICE, null, "name=? and date like '"+date+"%'", new String[]{name}, null, null, null);
		}
	   
        if(cursor != null){ 
        	while(cursor.moveToNext())
        	{
        		Bill bill = new Bill();  
	            int _id = cursor.getInt(cursor.getColumnIndex("_id"));  
	            String n = cursor.getString(cursor.getColumnIndex("name"));  
	            String i = cursor.getString(cursor.getColumnIndex("item"));
	            String m = cursor.getString(cursor.getColumnIndex("money"));
	            String d = cursor.getString(cursor.getColumnIndex("date"));
	            bill.setId(_id+"");  
	            bill.setName(n); 
	            bill.setItem(i);
	            bill.setMoney(m);
	            bill.setDate(d);
	            bills.add(bill);
        	}
        	cursor.close();
        }
	    db.close();
		return bills;
	}
	
	@SuppressLint("NewApi")
	public List<Bill> getBillsByDate(String date) {
		List<Bill> bills=new ArrayList<Bill>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor=null;
		if(date.isEmpty())
		{
			cursor = db.query(TABLE_DEVICE, null, null, null, null, null, null);
		}else
		{
			cursor = db.query(TABLE_DEVICE, null, "date like '"+date+"%'", null, null, null, null);
		}
	   
        if(cursor != null){ 
        	while(cursor.moveToNext())
        	{
        		Bill bill = new Bill();  
	            int _id = cursor.getInt(cursor.getColumnIndex("_id"));  
	            String n = cursor.getString(cursor.getColumnIndex("name"));  
	            String i = cursor.getString(cursor.getColumnIndex("item"));
	            String m = cursor.getString(cursor.getColumnIndex("money"));
	            String d = cursor.getString(cursor.getColumnIndex("date"));
	            bill.setId(_id+"");  
	            bill.setName(n); 
	            bill.setItem(i);
	            bill.setMoney(m);
	            bill.setDate(d);
	            bills.add(bill);
        	}
        	cursor.close();
        }
	    db.close();
		return bills;
	}
	
	//增
	public long addPerson(String name) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", name);		

		long ret = db.insertOrThrow(TABLE_PERSON, null, values);
		db.close();

		return ret;
	}
	//改
	public void updatePerson(int id,String name) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", name);
		db.update(TABLE_PERSON, values, "_id = '" + id + "'", null);
		db.close();
	}
	//删
	public void removePerson(String id) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(TABLE_PERSON, "_id = '" + id + "'", null);
		db.close();
	}
	//查
	@SuppressLint("NewApi")
	public List<Person> getPersons(String name) {
		List<Person> persons=new ArrayList<Person>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor=null;
		if(name==null||name.isEmpty())
		{
			cursor = db.query(TABLE_PERSON, null, null, null, null, null, null);
		}else
		{
			cursor = db.query(TABLE_PERSON, null, "name=?", new String[]{name}, null, null, null);
		}
	   
        if(cursor != null){ 
        	while(cursor.moveToNext())
        	{
        		Person person = new Person();  
	            int _id = cursor.getInt(cursor.getColumnIndex("_id"));  
	            String n = cursor.getString(cursor.getColumnIndex("name"));  
	            person.setId(_id+"");  
	            person.setName(n); 
	            persons.add(person);
        	}
        	cursor.close();
        }
	    db.close();
		return persons;
	}
	
	public class SQLHelper extends SQLiteOpenHelper
	{
		private static final String DB_FILE = "Room.db";
		private static final int DB_VERSION = 1;

		private static final String SQLCMD_CREATE_TABLE_ROOM = "CREATE TABLE " + TABLE_DEVICE + "(" + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + "name NVARCHAR(30) NULL, "  + "item	VARCHAR(50) NULL, " + "money VARCHAR(50) NULL  , "
				+ "date	VARCHAR(30) NULL " + ");";
		private static final String SQLCMD_CREATE_TABLE_PERSON = "CREATE TABLE " + TABLE_PERSON + "(" + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + "name NVARCHAR(30) NULL"  + ");";

		public SQLHelper(Context context) {
			super(context, DB_FILE, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQLCMD_CREATE_TABLE_PERSON);
			db.execSQL(SQLCMD_CREATE_TABLE_ROOM);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQLCMD_CREATE_TABLE_PERSON);
			db.execSQL(SQLCMD_CREATE_TABLE_ROOM);
			onCreate(db);
		}
		
	}
}
