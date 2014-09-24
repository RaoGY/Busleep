package com.busleep.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.mr.busleep.R;
import com.busleep.app.CustomApplication;
import com.busleep.bean.MrBusLine;
import com.busleep.ui.base.BaseFragmentActivity;
import com.busleep.utils.LogUtils;

/**
 * 用百度地图来显示公交路线;
 * @author Render;
 *
 */
public class BuslineBaiduMapActivity extends BaseFragmentActivity implements BaiduMap.OnMapClickListener{

	private static final String TAG = "BusLineBaiduMapActivity";

	/**
	 * 当前显示的为该公交车的路线信息;
	 */
	private BusLineResult mBusLineResult=null;
	
	private BaiduMap mBaiduMap = null;
	
	private int nodeIndex=-1;
	
	private Button mBtnPre=null;
	
	private Button mBtnNext=null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		
		try {
			setContentView(R.layout.activity_busline_baidu_map);
		} catch (Exception e) {
			LogUtils.i(TAG, e.getMessage());
		}

		init();
	}
	
	/**
	 * 初始化界面;
	 */
	private void init(){
		
		MrBusLine busLine=(MrBusLine) CustomApplication.mObject;
		
		if(busLine==null){
			return;
		}
		
		mBusLineResult=busLine.getBusLineResult();
		initTopBarForLeft(busLine.getBusName());
		
		mBaiduMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_baidu_map)).getBaiduMap();
		mBaiduMap.setOnMapClickListener(this);
		
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		
		showBuslineOnBaiduMap();
	}

	/**
	 * 在百度地图上显示公交信息;
	 */
	private void showBuslineOnBaiduMap(){
		
		mBaiduMap.clear();
		nodeIndex = -1;
		BusLineOverlay overlay = new BusLineOverlay(mBaiduMap);
		mBaiduMap.setOnMarkerClickListener(overlay);
		overlay.setData(mBusLineResult);
		overlay.addToMap();
		overlay.zoomToSpan();
		mBtnPre.setVisibility(View.VISIBLE);
		mBtnNext.setVisibility(View.VISIBLE);
		Toast.makeText(BuslineBaiduMapActivity.this,
				mBusLineResult.getBusLineName(),Toast.LENGTH_SHORT).show();
		
	}
	
	/**
	 * 节点浏览示例
	 * @param v
	 */
	public void nodeClick(View v) {

		if (nodeIndex < -1 || mBusLineResult == null
				|| nodeIndex >= mBusLineResult.getStations().size())
			return;
		
		TextView popupText = new TextView(this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xff000000);
		// 上一个节点
		if (mBtnPre.equals(v) && nodeIndex > 0) {
			// 索引减
			nodeIndex--;
		}
		// 下一个节点
		if (mBtnNext.equals(v) && nodeIndex < (mBusLineResult.getStations().size() - 1)) {
			// 索引加
			nodeIndex++;
		}
		// 移动到指定索引的坐标
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mBusLineResult
				.getStations().get(nodeIndex).getLocation()));
		// 弹出泡泡
		popupText.setText(mBusLineResult.getStations().get(nodeIndex).getTitle());
		mBaiduMap.showInfoWindow(new InfoWindow(popupText, mBusLineResult.getStations()
				.get(nodeIndex).getLocation(), null));
	}
	
	
	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		mBaiduMap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
