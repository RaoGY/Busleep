package com.busleep.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * 周边的公交车信息;
 * @author Render;
 */
public class MrNearbyBus {

	/**
	 * 公交车名称;
	 */
	private String busName=null;
	
	/**
	 * 该公交线路的uid;
	 */
	private String uid=null;
	
	/**
	 * 反向公交的Uid;
	 */
	private String reverseUid=null;
	
	/**
	 * 查到周围的站点名称,根据该名称查到的公交线路;
	 */
	private String stationName=null;
	
	/**
	 * 站点位置;
	 */
	private LatLng stationLaction=null;
	
	/**
	 * 开往的方向;
	 */
	private String busDrection=null;
	
	/**
	 * 构造函数;
	 */
	public MrNearbyBus(){
		
	}
	
	public String getBusName() {
		return busName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getReverseUid() {
		return reverseUid;
	}

	public void setReverseUid(String reverseUid) {
		this.reverseUid = reverseUid;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public LatLng getStationLaction() {
		return stationLaction;
	}

	public void setStationLaction(LatLng stationLaction) {
		this.stationLaction = stationLaction;
	}

	public String getBusDrection() {
		return busDrection;
	}

	public void setBusDrection(String busDrection) {
		this.busDrection = busDrection;
	}
}
