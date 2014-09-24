package com.busleep.adapter;

import java.util.List;



import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.mr.busleep.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BusLineStationsAdapter extends BaseAdapter{

	private Context ct;
	
	private List<BusStation> datas=null;
	
	public BusLineStationsAdapter(Context ct, List<BusStation> datas) {
		this.ct = ct;
		this.datas = datas;
	}
	
	public void updateListView(List<BusStation> list) {
		this.datas = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		
		if(datas==null){
			return 0;
		}
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		if(datas==null){
			return null;
		}
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(ct).inflate(R.layout.item_bus_stations, null);
			viewHolder = new ViewHolder();
			
			viewHolder.busStation = (TextView) convertView.findViewById(R.id.tv_bus_station);
			convertView.setTag(viewHolder);
			
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BusStation busStation=datas.get(position);
		
		viewHolder.busStation.setText(busStation.getTitle());
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView busStation; 
	}
}
