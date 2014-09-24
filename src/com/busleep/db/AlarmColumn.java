package com.busleep.db;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;

public class AlarmColumn extends DatabaseColumn{

	public static final String TABLE_NAME="alarm";
	public static final Uri    CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);
	
	//数据库字段常量;
	public static final String STATIONNAME="station_name";
	public static final String LONGTITUDE="longtitude";
	public static final String LATITUDE="latitude";
	public static final String ISON="ison";
	
	//定义表字段与数据库操作字段的映射表;
	private static final Map<String, String> mColumnMap=new HashMap<String,String>();
	
	static{
		mColumnMap.put(_ID, "integer primary key autoincrement");
		mColumnMap.put(STATIONNAME, "text not null");
		mColumnMap.put(LONGTITUDE, "float not null");
		mColumnMap.put(LATITUDE, "float not null");
		mColumnMap.put(ISON, "bit");
	}
	
	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	public Uri getTableContent() {
		// TODO Auto-generated method stub
		return CONTENT_URI;
	}

	@Override
	protected Map<String, String> getTableMap() {
		// TODO Auto-generated method stub
		return mColumnMap;
	}

}
