package com.busleep.baidu;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationHelper {
	
	private LocationClient mLocationClient=null;
	
	/**
	 * 接受百度地图定位的回调类;
	 */
	private MyBDLocationListener mMyBDLocationListener=null;
	
	/**
	 * 自定义的定位回调接口,主要是对百度地图定位的封装;
	 */
	private OnLocationListener mLocationListener=null;
	
	private Context ct;
	
	public LocationHelper(Context ct){
		
		this.ct=ct;
	}
	
	/**
	 * 设置自定义的监听器;
	 * @param locationListener
	 */
	public void setOnLocationListener(OnLocationListener locationListener){
		
		this.mLocationListener=locationListener;
	}
	
	/**
	 * 停止定位;
	 */
	public void stop(){
		
		if(mLocationClient!=null){
			
			mLocationClient.stop();
		}
	}
	
	/**
	 * 启动定位;
	 */
	public void start(){
		
		mLocationClient=new LocationClient(ct);
		mMyBDLocationListener=new MyBDLocationListener();
		mLocationClient.registerLocationListener(mMyBDLocationListener);
		

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);		// 打开gps;
		option.setCoorType("bd09ll"); 	// 设置坐标类型;
		option.setScanSpan(10000);		// 每隔10s定位一次;
		option.setAddrType("all");
		
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	
	/**
	 * 实现百度地图的接口;
	 * @author Render;
	 *
	 */
	public class MyBDLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			if(mLocationListener!=null){
				
				mLocationListener.onReceiveLocation(location);
			}
		}
		
		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public interface OnLocationListener {

		public void onReceiveLocation(BDLocation location); 
		
	}

}
