package com.busleep.adapter;

import java.math.BigDecimal;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.busleep.adapter.base.AdapterBase;
import com.busleep.app.CustomApplication;
import com.busleep.bean.MrAlarm;
import com.mr.busleep.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmAdapter extends AdapterBase<MrAlarm>{

	private Context ct;
	private ViewHolder viewHolder=null;
	private OnAlarmStateChangeListener mLister;
	
	public AlarmAdapter(List<MrAlarm> datas,Context ct){
		this.ct=ct;
	    appendToList(datas);
	}
	
	public void updateListView(List<MrAlarm> datas){

		clear();
		appendToList(datas);
		notifyDataSetChanged();
	}
	
	public void setOnAlarmStateChangeLister(OnAlarmStateChangeListener lister) {
		this.mLister = lister;
	}

	private class ImageViewListener implements OnClickListener{

		private int position;
		
		public ImageViewListener(int position){
			this.position=position;
		}
		
		@Override
		public void onClick(View v) {
			
			if(viewHolder==null){
				return;
			}
			int vid=v.getId();
			if(vid==viewHolder.ivOpenAlarm.getId()){
				
				MrAlarm alarm=getList().get(position);
				mLister.onAlarmStateChange(false, alarm);
				
			}else if(vid==viewHolder.ivCloseAlarm.getId()) {
				
				MrAlarm alarm=getList().get(position);
				mLister.onAlarmStateChange(true, alarm);
				
			}
		}
	}
	
	static class ViewHolder {
		
		TextView tvStationName;
		TextView tvCurrentDistance;
		TextView tvAlarmState;
		ImageView ivOpenAlarm;
		ImageView ivCloseAlarm;
	}
	
	private void initViewData(int position,ViewHolder viewHolder, View convertView){
		
		MrAlarm alarm=getList().get(position);
		
		if(alarm.isRing()){
			convertView.setBackgroundColor(Color.parseColor("#9F291E"));
		}
		else {
			convertView.setBackgroundColor(Color.parseColor("#F0FFFF"));
		}

		viewHolder.tvStationName.setText("目的地："+alarm.getBusStationName());
	
		//计算当前位置距离目标位置的距离;
		double longtitude=alarm.getLongtitude();
		double latitude=alarm.getLatitude();
		LatLng destinationLng=new LatLng(latitude, longtitude);
		LatLng currentLng=CustomApplication.getInstance().getCurrentLatLng();
		
		double distance=DistanceUtil.getDistance(destinationLng, currentLng);
		
		BigDecimal bg = new BigDecimal(distance/1000.0);
		double distance1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		viewHolder.tvCurrentDistance.setText("当前距离："+distance1+"km");
	
		boolean isOn=alarm.isOn();
		if(isOn){
			viewHolder.ivOpenAlarm.setVisibility(View.VISIBLE);
			viewHolder.ivCloseAlarm.setVisibility(View.GONE);
			viewHolder.tvAlarmState.setVisibility(View.GONE);
		}else {
			viewHolder.ivOpenAlarm.setVisibility(View.GONE);
			viewHolder.ivCloseAlarm.setVisibility(View.VISIBLE);
			viewHolder.tvAlarmState.setVisibility(View.VISIBLE);
			viewHolder.tvAlarmState.setText(" | 未开启");
		}
	}
	
	/**
	 * 闹钟状态改变的监听器接口;
	 * @author Render
	 */
	public interface OnAlarmStateChangeListener{
		
		public void onAlarmStateChange(boolean b,MrAlarm alarm);
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			
			convertView = LayoutInflater.from(ct).inflate(R.layout.item_sleep_alarm, null);
			viewHolder = new ViewHolder();
		
			viewHolder.tvStationName = (TextView) convertView.findViewById(R.id.tv_bus_station);
			viewHolder.tvCurrentDistance=(TextView)convertView.findViewById(R.id.tv_current_distance);
			viewHolder.tvAlarmState=(TextView)convertView.findViewById(R.id.tv_alarm_state);
			viewHolder.ivOpenAlarm=(ImageView)convertView.findViewById(R.id.iv_open_alarm);
			viewHolder.ivCloseAlarm=(ImageView)convertView.findViewById(R.id.iv_close_alarm);
			convertView.setTag(viewHolder);
			
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
        
		viewHolder.ivOpenAlarm.setOnClickListener(new ImageViewListener(position));
		viewHolder.ivCloseAlarm.setOnClickListener(new ImageViewListener(position));
		
		initViewData(position, viewHolder,convertView);

		return convertView;
	}

	@Override
	protected void onReachBottom() {
		
		
	}
}
