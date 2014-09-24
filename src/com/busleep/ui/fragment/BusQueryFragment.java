package com.busleep.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mr.busleep.R;
import com.busleep.ui.base.BaseFragment;

/**
 * 公交查询片段;
 * @author Render;
 */
public class BusQueryFragment extends BaseFragment implements OnClickListener{

	private static final int ROUTE_FRAGMENT=0;
	private static final int STATION_FRAGMENT=1;
	private static final int TRANSFER_FRAGMENT=2;
	
	private View rootView;
	
	private FragmentManager fragmentManager;
	
	private TextView routeTextView;
	private TextView stationTextView;
	private TextView transferTextView;
	
	private RouteFragment mRouteFragment;
	private StationFragment mStationFragment;
	private TransferFragment mTransferFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(rootView==null){
			rootView=inflater.inflate(R.layout.fragment_bus_query, null);
		}
		fragmentManager=getChildFragmentManager();
		
		init();
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	/**
	 * 初始化点击事件;
	 */
	private void init(){
		
		routeTextView=(TextView)rootView.findViewById(R.id.tv_bus_route);
		stationTextView=(TextView)rootView.findViewById(R.id.tv_bus_station);
		transferTextView=(TextView)rootView.findViewById(R.id.tv_bus_transfer);
		
		routeTextView.setSelected(true);
		stationTextView.setSelected(false);
		transferTextView.setSelected(false);
		
		routeTextView.setOnClickListener(this);
		stationTextView.setOnClickListener(this);
		transferTextView.setOnClickListener(this);
	}
	
	private void OnTabSelected(int index){
		
		FragmentTransaction transaction=fragmentManager.beginTransaction();
		
		hideFragments(transaction);
		
		switch (index) {
			case ROUTE_FRAGMENT:{
				if(mRouteFragment==null){
					mRouteFragment=new RouteFragment();
					transaction.add(R.id.bus_navi_center, mRouteFragment);
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				}else{
					transaction.show(mRouteFragment);
				}
				break;
			}
			case STATION_FRAGMENT:{
				if(mStationFragment==null){
					mStationFragment=new StationFragment();
					transaction.add(R.id.bus_navi_center, mStationFragment);
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				}else{
					transaction.show(mStationFragment);
				}
				break;
			}
			case TRANSFER_FRAGMENT:{
				if(mTransferFragment==null){
					mTransferFragment=new TransferFragment();
					transaction.add(R.id.bus_navi_center,mTransferFragment);
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				}else {
					transaction.show(mTransferFragment);
				}
				break;
			}
		}
		transaction.commit();
	}
	
	/**
	 * 将所有Fragment都置为隐藏;
	 * @param transaction
	 */
	private void hideFragments(FragmentTransaction transaction){
		if(mRouteFragment!=null){
			transaction.hide(mRouteFragment);
		}
		if(mStationFragment!=null){
			transaction.hide(mStationFragment);
		}
		if(mTransferFragment!=null){
			transaction.hide(mTransferFragment);
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
		switch (view.getId()) {
		case R.id.tv_bus_route:
			routeTextView.setSelected(true);
			stationTextView.setSelected(false);
			transferTextView.setSelected(false);
			
			OnTabSelected(ROUTE_FRAGMENT);
			
			break;
		case R.id.tv_bus_station:
			routeTextView.setSelected(true);
			stationTextView.setSelected(false);
			transferTextView.setSelected(false);
			
			OnTabSelected(STATION_FRAGMENT);
			break;
		
		case R.id.tv_bus_transfer:
			routeTextView.setSelected(true);
			stationTextView.setSelected(false);
			transferTextView.setSelected(false);
			
			OnTabSelected(TRANSFER_FRAGMENT);
			break;
		}	
	}
}
