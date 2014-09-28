package com.busleep.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.location.BDLocation;
import com.mr.busleep.R;
import com.busleep.adapter.BusNearAdapter;
import com.busleep.app.CustomApplication;
import com.busleep.baidu.LocationHelper;
import com.busleep.baidu.LocationHelper.OnLocationListener;
import com.busleep.baidu.NearbySearchHelper;
import com.busleep.baidu.NearbySearchHelper.OnNearbySearchListener;
import com.busleep.bean.MrNearbyBus;
import com.busleep.config.Constant;
import com.busleep.ui.BuslineDetailActivity;
import com.busleep.ui.base.BaseFragment;
import com.busleep.utils.LogUtils;
import com.busleep.widget.XListView;
import com.busleep.widget.XListView.IXListViewListener;

/**
 * 公交查询中的周边片段;
 * @author Render;
 */
public class BusNearFragment extends BaseFragment implements OnNearbySearchListener,IXListViewListener
	,OnItemClickListener,OnLocationListener{

	public final String TAG="BusNearFragment";
	
	private boolean isFirstUpdate=true;
	
	/**
	 * 周边搜索帮助类;
	 */
	public NearbySearchHelper nearbySearchHelper=null;
	private View rootView;
	private XListView  mBusNearListView;
	private ProgressDialog progress=null ;
	private MyHandler mHandler;    		//处理消息的Handle对象;
	
	/**
	 * 定位帮助类;
	 */
	private LocationHelper locationHelper=null;
	
	/**
	 * 适配器;
	 */
	private BusNearAdapter busNearAdapter=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(rootView==null){
			
			rootView=inflater.inflate(R.layout.fragment_bus_near,container,false);
		}
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initXListView();
		
		initData();
	}
	
	/**
	 * 初始化数据;
	 */
	private void initData(){
		
		/**
		 * 创建定位帮助类;
		 */
		locationHelper=new LocationHelper(getActivity());
		locationHelper.setOnLocationListener(this);
		
		/**
		 * 创建周边搜索帮助类;
		 */
		nearbySearchHelper=new NearbySearchHelper();
		nearbySearchHelper.init();
		nearbySearchHelper.setNearBySearchListener(this);
		
		mHandler=new MyHandler();
		mHandler.sendEmptyMessage(0);
	}
	
	private void initXListView(){
		
		mBusNearListView=(XListView)rootView.findViewById(R.id.list_near_bus_info);
		mBusNearListView.setPullLoadEnable(false);
		mBusNearListView.setPullRefreshEnable(true);
		mBusNearListView.setXListViewListener(this);
		mBusNearListView.setOnItemClickListener(this);
		mBusNearListView.pullRefreshing();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onDestroy() {
		
		/**
		 * 销毁周边搜索帮助类;
		 */
		nearbySearchHelper.Destory();
		super.onDestroy();
	}

	/**
	 * 搜索周边公交,步骤：先定位,再搜索;
	 */
	private void excuteSearch(){
		
		if(isFirstUpdate){
			progress = new ProgressDialog(getActivity());
			progress.setMessage("正在定位...");
			progress.setCanceledOnTouchOutside(true);
			progress.show();
		}

		if(locationHelper!=null){
			locationHelper.start();
		}
	}
	
	//接受定位的信息;
	@Override
	public void onReceiveLocation(BDLocation location) {
		
		CustomApplication.mLocation=location;
		Constant.city=location.getCity();
		Constant.address=location.getAddrStr();
		
		locationHelper.stop();
		
		if(isFirstUpdate){
			progress.setMessage("正在搜索周边信息...");
			progress.setCanceledOnTouchOutside(true);
		}
		
		//开始搜索周边信息;
		nearbySearchHelper.searchNearby();
	}
	
	@Override
	public void onRefresh() {
	
		if(null==mHandler){
			return;
		}
		mHandler.sendEmptyMessage(0);
	}

	@Override
	public void onLoadMore() {
		
	}

	/**
	 * 点击Item节点，进行跳转;
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		MrNearbyBus nearbyBus = (MrNearbyBus) busNearAdapter.getItem(position-1);
		
		if(nearbyBus==null){
			return;
		}
		
		Bundle bundle=new Bundle();
		bundle.putString("buslineUid", nearbyBus.getUid());
		bundle.putString("busName", nearbyBus.getBusName());
		bundle.putString("reverseBuslineUid",nearbyBus.getReverseUid());
		
		redictToActivity(getActivity(), BuslineDetailActivity.class, bundle);
	}
	
	private void refreshPull(){
		if (mBusNearListView.getPullRefreshing()) {
			mBusNearListView.stopRefresh();
		}
	}

	@Override
	public void onRefreshBusNearby(List<MrNearbyBus> list) {
		
		if(isFirstUpdate){
			progress.dismiss();
			isFirstUpdate=false;
		}
		
		for(int i=0;i<list.size();i++){
			
			LogUtils.i(TAG, list.get(i).getBusName());	
		}
		
		if(busNearAdapter==null){
			busNearAdapter=new BusNearAdapter(getActivity(), list);
			mBusNearListView.setAdapter(busNearAdapter);
		}
		else {
			busNearAdapter.updateListView(list);
		}
		
		refreshPull();
	}
	

	/**
	 * 自定义的Handle对象;
	 * @author Render;
	 *
	 */
	private class MyHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch(msg.what){
				case 0:{
					excuteSearch();
					break;
				}
			}
		}
	}
	
}
