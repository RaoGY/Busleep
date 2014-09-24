package com.busleep.baidu;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.busleep.bean.MrBusLine;
import com.busleep.config.Constant;
import com.busleep.utils.LogUtils;

/**
 * 公交路线搜索帮助类;
 * @author Render;
 */
public class BusLineHelper implements OnGetPoiSearchResultListener,OnGetBusLineSearchResultListener{

	public static final String TAG = "BusLineHelper";
	
	private PoiSearch mPoiSearch = null; 			//搜索模块;	
	private BusLineSearch mBusLineSearch = null;	//公交路线搜索模块;
	private MrBusLine mBusLine=null;				//搜索到的公交线路;
	private String mUid=null;						//正向的Uid;
	
	private OnBusLineSearchListener mrBusLineSearchListener;	//自定义的搜索监听器;
	
	
	public void init(){
		if(mPoiSearch==null||mBusLineSearch==null){
			
			mPoiSearch = PoiSearch.newInstance();
			mPoiSearch.setOnGetPoiSearchResultListener(this);
			mBusLineSearch = BusLineSearch.newInstance();
			mBusLineSearch.setOnGetBusLineSearchResultListener(this);
		}
	}
	
	/**
	 * 添加自定义公交线路搜索监听器;
	 * @param mrBusLineSearchListener
	 */
	public void setMrBusLineSearchListener(OnBusLineSearchListener mrBusLineSearchListener) {
		
		this.mrBusLineSearchListener = mrBusLineSearchListener;
	}

	/**
	 * 根据Uid查询公交线路;
	 * @param uid
	 */
	public void searchBusLineByUid(String uid){

		mBusLine=new MrBusLine();
		mBusLine.setUid(uid);
		mBusLineSearch.searchBusLine(new BusLineSearchOption().city(Constant.city).uid(uid));
	}
	
	/**
	 * 根据名称查询该公交线路;
	 * @param busName
	 */
	public void searchBusLineByName(String busName,String uid){
		
		mBusLine=new MrBusLine();
		mUid=uid;
		//根据名字查公交的话先要用PoiSearch;
		mPoiSearch.searchInCity(new PoiCitySearchOption().city(Constant.city).keyword(busName));
	}
	
	
	@Override
	public void onGetBusLineResult(BusLineResult result) {
	
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			
			LogUtils.i(TAG, "没找到该公交路线");
			return;
		}
		
		
		mBusLine.setBusLineResult(result);
		
		/**
		 * 回调实现该接口的对象;
		 */
		if(mrBusLineSearchListener!=null){
			mrBusLineSearchListener.onGetBusLineResult(mBusLine);
		}
	}
	
	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		
		
	}
	@Override
	public void onGetPoiResult(PoiResult result) {
	
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			
			LogUtils.i(TAG, "Poi公交路线搜索失败");
			return;
		}
		
		for(PoiInfo poi:result.getAllPoi()){
			
			if (poi.type == PoiInfo.POITYPE.BUS_LINE){
				
				//如果正向Uid为空,则查询到的一个uid直接赋值;
				if(mUid==null){
					mBusLine.setUid(poi.uid);
					break;
				}else {
					//如果正向uid等于查询到的uid,则继续查找,查找的下一个uid即为需要反向uid,否则直接为反向uid;
					if(mUid.equals(poi.uid)){
						continue;
					}else {
						mBusLine.setUid(poi.uid);
						break;
					}
				}
			}
		}
		//poi搜索完成后,根据搜索到的uid继续搜索;
		mBusLineSearch.searchBusLine(new BusLineSearchOption().city(Constant.city).uid(mBusLine.getUid()));
	}
	
	public interface OnBusLineSearchListener {

		public void onGetBusLineResult(MrBusLine mBusLine);
	}

}
