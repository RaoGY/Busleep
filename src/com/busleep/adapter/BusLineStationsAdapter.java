package com.busleep.adapter;

import java.util.List;

import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.busleep.adapter.base.AdapterBase;
import com.mr.busleep.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BusLineStationsAdapter extends AdapterBase<BusStation>{

	private Context ct;
	
	public BusLineStationsAdapter(Context ct, List<BusStation> datas) {
		this.ct = ct;
		super.appendToList(datas);
	}
	
	public void updateListView(List<BusStation> list) {
		super.clear();
		appendToList(list);
		notifyDataSetChanged();
	}
	
	static class ViewHolder {
		TextView busStation; 
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(ct).inflate(R.layout.item_bus_stations, null);
			viewHolder = new ViewHolder();
			
			viewHolder.busStation = (TextView) convertView.findViewById(R.id.tv_bus_station);
			convertView.setTag(viewHolder);
			
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BusStation busStation=getList().get(position);
		
		viewHolder.busStation.setText(busStation.getTitle());
		
		return convertView;
	}

	@Override
	protected void onReachBottom() {
		
	}
}
