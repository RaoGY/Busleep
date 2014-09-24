package com.busleep.baidu;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.busleep.app.CustomApplication;
import com.busleep.bean.MrNearbyBus;
import com.busleep.config.Constant;
import com.busleep.utils.LogUtils;

/**
 * 周边公交查询帮助类;
 * @author Render;
 */
public class NearbySearchHelper implements OnGetPoiSearchResultListener,

	OnGetBusLineSearchResultListener{

	public static final String TAG = "NearbySearchHelper";
	
	private final int BUSSTATION=0;					//先查询周边的公交站点信息;
	private final int BUSLINE=1;					//站点的公交信息;
	private PoiSearch mSearch = null; 				//搜索模块;	
	private BusLineSearch mBusLineSearch = null;	//公交路线搜索模块;
	
	private int mType;								//查询类型;
	private int nodeIndex=-1;						//查询的索引;
	
	private OnNearbySearchListener nearBySearchListener; //查询结束监听对象;
	private List<MrNearbyBus> nearbyBuses=null; 		 //查询的结果;
	
	public NearbySearchHelper(){
	
	}

	public void setNearBySearchListener(OnNearbySearchListener nearBySearchListener) {
		this.nearBySearchListener = nearBySearchListener;
	}

	/**
	 * 初始化;
	 */
	public void init(){
		
		if(mSearch==null||mBusLineSearch==null){
			
			mSearch = PoiSearch.newInstance();
			mSearch.setOnGetPoiSearchResultListener(this);
			mBusLineSearch = BusLineSearch.newInstance();
			mBusLineSearch.setOnGetBusLineSearchResultListener(this);
			
			nearbyBuses=new ArrayList<MrNearbyBus>();
			
		}else {
			nearbyBuses.clear();
		}
	}
	
	/**
	 * 销毁函数;
	 */
	public void Destory(){
		
		mSearch.destroy();
		mBusLineSearch.destroy();
	}
	
	public void searchNearby(){
		
		mType=BUSSTATION;
		
		double latitude=CustomApplication.mLocation.getLatitude();
		double longtitude=CustomApplication.mLocation.getLongitude();
		
		//查询周边公交线路;
		mSearch.searchNearby(new PoiNearbySearchOption().keyword("公交站")
				.location(new LatLng(latitude,longtitude)).radius(Constant.nearbyRadius));
		
	}
	
	@Override
	public void onGetBusLineResult(BusLineResult arg0) {
		
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		
		switch(mType)
		{
			case BUSSTATION:
			{
				//如果查询失败直接返回;
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR)
				{
					LogUtils.i(TAG, "查询站点失败！");
					nearBySearchListener.onRefreshBusNearby(nearbyBuses);
					return;
				}
				
				ProcessBusStation(result);
				break;
			}
			case BUSLINE:
			{
				//如果查询失败直接返回;
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR)
				{
					LogUtils.i(TAG, "查询线路失败！");
					nearBySearchListener.onRefreshBusNearby(nearbyBuses);
					return;
				}
				
				ProcessBusLine(result);
				break;
			}
		}
	}

	/**
	 * 主要是寻找该站点中是否存在相同的公交,如果存在则取两趟公交距离当前位置最近的点;
	 * 由于是搜索周边的公交站，可能会存在一趟公交车经过周边的两个或者三个公交站，为了排除冗余的信息，
	 * 故需要判断同一辆公交车距那个车站最近
	 * @return
	 */
	private boolean FindSameBus(MrNearbyBus nearbyBus){
		
		String busName=nearbyBus.getBusName();
		
		LatLng nowLocation=new LatLng(CustomApplication.mLocation.getLatitude(), 
				CustomApplication.mLocation.getLongitude());
		
		LatLng newLocation=nearbyBus.getStationLaction();
		
		double newDistance=DistanceUtil.getDistance(nowLocation, newLocation);
		double oldDistance=0.0;
		
		for(int i=0;i<nearbyBuses.size();i++)
		{
			//如果存在名称相同的公交线路;
			if(nearbyBuses.get(i).getBusName().equals(busName))
			{
				LatLng oldLocation=nearbyBuses.get(i).getStationLaction();
				
				oldDistance=DistanceUtil.getDistance(nowLocation, oldLocation);
				
				//如果当前的距离小于之前的距离,则更新该公交车的信息;
				if(newDistance<oldDistance)
				{
					nearbyBuses.get(i).setStationLaction(newLocation);
					nearbyBuses.get(i).setStationName(nearbyBus.getStationName());
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 处理查询后的结果是公交站点的情况;
	 * @param result
	 */
	private void ProcessBusStation(PoiResult result){
		
		for (PoiInfo poi : result.getAllPoi())
		{
			if (poi.type == PoiInfo.POITYPE.BUS_STATION)
			{	
				String busNames []=poi.address.split(";");
				
				for(int i=0;i<busNames.length;++i){
					
					MrNearbyBus nearbyBus=new MrNearbyBus();
					nearbyBus.setBusName(busNames[i]);
					nearbyBus.setStationName(poi.name);
					nearbyBus.setStationLaction(poi.location);
					
					boolean bRes=FindSameBus(nearbyBus);
					
					if(!bRes)
					{
						nearbyBuses.add(nearbyBus);
					}		
				}
			}
		}
		searchBusLine();
		nearBySearchListener.onRefreshBusNearby(nearbyBuses);
	}
	
	/**
	 * 查询公交线路;
	 */
	private void searchBusLine(){
		
		nodeIndex++;
		
		mType=BUSLINE;
		
		if(nearbyBuses.isEmpty()){
			return;
		}
		
		//部分刷新;
		if(nodeIndex%5==0){
			nearBySearchListener.onRefreshBusNearby(nearbyBuses);
		}
		
		//如果此时返回表明查询完毕;
		if(nodeIndex >= nearbyBuses.size()){
			LogUtils.i(TAG, "查询完毕！");
			nearBySearchListener.onRefreshBusNearby(nearbyBuses);
			return;
		}
		
		//这里还是采用搜索周边的方式,效率会更高;
		double latitude=CustomApplication.mLocation.getLatitude();
		double longtitude=CustomApplication.mLocation.getLongitude();
		
		String busLine=nearbyBuses.get(nodeIndex).getBusName();
		mSearch.searchNearby(new PoiNearbySearchOption().keyword(busLine+"公交")
				.location(new LatLng(latitude,longtitude)).radius(Constant.nearbyRadius));
	}
	
	/**
	 * 处理公交路线的信息;
	 * @param result
	 */
	private void ProcessBusLine(PoiResult result)
	{
		for (PoiInfo poi : result.getAllPoi())
		{
			if (poi.type == PoiInfo.POITYPE.BUS_LINE)
			{
				MrNearbyBus nearbyBus=nearbyBuses.get(nodeIndex);
				
				//如果是第一次进来;
				if(nearbyBus.getUid()==null){
	
					nearbyBus.setUid(poi.uid);
					String drection=poi.name;
					int index=drection.indexOf("(");
					int length=drection.length();
					
					drection=drection.substring(index+1,length-1);
					nearbyBus.setBusDrection(drection);
					
				}else {	//否则是返程公交车;
					
					nearbyBus.setReverseUid(poi.uid);;
				}
			}
		}
		searchBusLine();
	}
	
	/**
	 * 周边搜索接口监听器;
	 * @author Render;
	 */
	public interface OnNearbySearchListener {
		/**
		 * 查询刷新函数;
		 */
		public void onRefreshBusNearby(List<MrNearbyBus> list);
	}
}
