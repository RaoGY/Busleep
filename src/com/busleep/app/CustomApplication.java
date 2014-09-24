package com.busleep.app;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.busleep.bean.MrAlarm;
import com.busleep.db.AlarmColumn;
import com.busleep.db.DBHelper;
import com.busleep.utils.LogUtils;

import android.app.Application;
import android.database.Cursor;

/**
 * 自定义的Android的应用类,进行一些接口的初始化操作,如百度地图;
 * @author Render
 * 
 */
public class CustomApplication  extends Application {

	private static final String TAG="CustomApplication";
	
	private static CustomApplication mCustomApplication=null;
	
	/**
	 * 记录当前用户的位置;
	 */
	public static BDLocation mLocation=null; 
	
	/**
	 * 静态的Object对象,Intent中对象传递转化为该对象;
	 */
	public static Object mObject=null;

	public static CustomApplication getInstance() {
		
		return mCustomApplication;
	}
	
	public double getLongtitude(){
		
		if(mLocation!=null)
		{
			return mLocation.getLongitude();
		}
		
		return 0.0;
	}
	
	public double getLatitude() {
		
		if(mLocation!=null)
		{
			return mLocation.getLatitude();
		}
		
		return 0.0;
	}
	
	/**
	 * 获取当前的位置;
	 * @return
	 */
	public LatLng getCurrentLatLng(){
		
		return new LatLng(getLatitude(), getLongtitude());
	}
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		
		mCustomApplication=this;
		
		//初始化百度地图;
		SDKInitializer.initialize(this);
		
		LogUtils.i(TAG, "Baidu Map Init Completed!");
	}
	
	/**
	 * 获取所有的闹铃;
	 * @return
	 */
	public List<MrAlarm> getAllAlarms(){
		
		List<MrAlarm> alarms=new ArrayList<MrAlarm>();
		
		DBHelper dbHelper=DBHelper.getInstance(getApplicationContext());
		
		Cursor cursor=dbHelper.query(AlarmColumn.TABLE_NAME, null, null, null);
		
		int nIndex=0;
	    while(cursor.moveToNext())  
	    {  
	    	MrAlarm alarm=new MrAlarm();
	    	nIndex=cursor.getColumnIndex(AlarmColumn._ID);
	    	String id=cursor.getString(nIndex);
	    	alarm.setId(id);
	    	
	    	nIndex=cursor.getColumnIndex(AlarmColumn.STATIONNAME);
	    	String stationName=cursor.getString(nIndex);
	    	alarm.setBusStationName(stationName);
	    	
	    	nIndex=cursor.getColumnIndex(AlarmColumn.LONGTITUDE);
	    	double longtitude=cursor.getDouble(nIndex);
	    	alarm.setLongtitude(longtitude);
	    	
	    	nIndex=cursor.getColumnIndex(AlarmColumn.LATITUDE);
	    	double latitude=cursor.getDouble(nIndex);
	    	alarm.setLatitude(latitude);
	    	
	    	nIndex=cursor.getColumnIndex(AlarmColumn.ISON);
	    	int ison=cursor.getInt(nIndex);
	    	if(ison!=0){
	    		alarm.setOn(true);
	    	}else {
				alarm.setOn(false);
			}
	    	alarms.add(alarm);
	    } 
	    return alarms;
	}
}
