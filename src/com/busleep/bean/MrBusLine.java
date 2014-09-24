package com.busleep.bean;

import java.io.Serializable;
import com.baidu.mapapi.search.busline.BusLineResult;

/**
 * 公交车信息;
 * @author Render
 *
 */
public class MrBusLine implements Serializable {

	private static final long serialVersionUID = 1L;

	public MrBusLine(){
		
	}
	
	private String busName=null;
	
	/**
	 * 百度地图中公交搜索的结果;
	 */
	private BusLineResult busLineResult=null;
	
	/**
	 * 该公交线路的uid,该字段和BusLineResult中的uid相同;
	 */
	private String uid=null;
	
	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getUid() {
		
		return uid;
	}

	public void setUid(String uid) {
		
		this.uid=uid;
	}

	public BusLineResult getBusLineResult() {
		return busLineResult;
	}

	public void setBusLineResult(BusLineResult busLineResult) {
		this.busLineResult = busLineResult;
	}
}
