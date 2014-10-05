package com.busleep.adapter;

import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.mr.busleep.R;
import com.busleep.adapter.base.AdapterBase;
import com.busleep.app.CustomApplication;
import com.busleep.bean.MrNearbyBus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BusNearAdapter extends AdapterBase<MrNearbyBus>{

	Context ct;
	
	public BusNearAdapter(Context ct, List<MrNearbyBus> lists) {
		this.ct = ct;
		
		super.appendToList(lists);
	}
	
	/**
	 * 根据位置不同设置背景颜色;

	private void setViewBackgroundColor(int position, View convertView){
		
		if(position%5==0)
		{
			//蓝色;
			convertView.setBackgroundColor(Color.parseColor("#2468B6"));
		}
		if(position%5==1)
		{
			//紫色;
			convertView.setBackgroundColor(Color.parseColor("#753BD5"));
		}
		if(position%5==2)
		{
			//绿色;
			convertView.setBackgroundColor(Color.parseColor("#0F952B"));
		}
		if(position%5==3)
		{
			//暗红色;
			convertView.setBackgroundColor(Color.parseColor("#9F291E"));
		}
		if(position%5==4)
		{
			//黄色;
			convertView.setBackgroundColor(Color.parseColor("#D1971B"));
		}
	}	 */
	
	static class ViewHolder {
		TextView busName;
		TextView busDrection;
		TextView busDistance; 
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(ct).inflate(R.layout.item_bus_near, null);
			viewHolder = new ViewHolder();
			
			viewHolder.busName = (TextView) convertView.findViewById(R.id.tv_bus_name);
			viewHolder.busDrection = (TextView) convertView.findViewById(R.id.tv_bus_drection);
			viewHolder.busDistance = (TextView) convertView.findViewById(R.id.tv_bus_distance);
			convertView.setTag(viewHolder);
			
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
	
		MrNearbyBus nearbyBus= getList().get(position);
		
		String busName = nearbyBus.getBusName();
		String busDrection = nearbyBus.getBusDrection();
		
		LatLng nowLatLng=CustomApplication.getInstance().getCurrentLatLng();
		LatLng stationLatLng=nearbyBus.getStationLaction();
		
		int distance=(int)DistanceUtil.getDistance(nowLatLng, stationLatLng);
		
		String busStation=nearbyBus.getStationName();
		busStation="距离  "+busStation+" 约"+String.valueOf(distance)+"米";

		viewHolder.busName.setText(busName);
		viewHolder.busDrection.setText(busDrection);
		viewHolder.busDistance.setText(busStation);
		
		return convertView;
	}

	@Override
	protected void onReachBottom() {
		
		
	}
}
