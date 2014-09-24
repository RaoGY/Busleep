package com.busleep.ui.fragment;

import java.io.Serializable;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.mr.busleep.R;
import com.busleep.adapter.AlarmAdapter;
import com.busleep.adapter.AlarmAdapter.OnAlarmStateChangeListener;
import com.busleep.app.CustomApplication;
import com.busleep.app.SleepPlanService;
import com.busleep.bean.MrAlarm;
import com.busleep.config.Constant;
import com.busleep.db.AlarmColumn;
import com.busleep.db.DBHelper;
import com.busleep.ui.base.BaseFragment;
import com.busleep.view.dialog.DialogTips;

/**
 * 睡眠计划的片段;
 * @author Render;
 */
public class SleepPlanFragment extends BaseFragment implements OnItemClickListener,
	OnItemLongClickListener,OnAlarmStateChangeListener{
	
	private View rootView=null;
	private ListView mListView;
	private DBHelper mDBHelper;			//数据库操作帮助类;
	private AlarmAdapter mAlarmAdapter;	
	private List<MrAlarm> mAlarms; 		// 当前所有的闹铃列表;
	private BroadcastReceiver mReceiver=null;	//广播对象;
	private MyHandler mHandler;    		//处理消息的Handle对象;
	
	/**
	 * 标志handle中的消息类型;
	 */
	private final static int MSG_UPDATE_ALARMS=101;	//刷新闹铃列表;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(rootView==null){
			rootView=inflater.inflate(R.layout.fragment_sleep_plan, null);
		}
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		mListView=(ListView)rootView.findViewById(R.id.lv_sleep_plan_detail);
		mDBHelper=DBHelper.getInstance(getActivity());
		mAlarmAdapter=new AlarmAdapter(null, getActivity());
		mListView.setAdapter(mAlarmAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		mAlarmAdapter.setOnAlarmStateChangeLister(this);
		
		mHandler=new MyHandler();
		
		initAlarms();
		startAlarmService();
		registerBroadcastReceiver();
	}
	
	/**
	 * 插入添加的闹钟到当前的列表中;
	 */
	private void initAlarms(){
		//插入添加的闹铃;
		if(CustomApplication.mObject!=null){

			MrAlarm alarm=(MrAlarm) CustomApplication.mObject;
			
			if(alarm==null){
				return;
			}
			
			ContentValues cv=new ContentValues();
			
			cv.put(AlarmColumn.STATIONNAME, alarm.getBusStationName());
			cv.put(AlarmColumn.LONGTITUDE, alarm.getLongtitude());
			cv.put(AlarmColumn.LATITUDE, alarm.getLatitude());
			cv.put(AlarmColumn.ISON, alarm.isOn());
			
			//向数据库中插入新添加的闹铃数据;
			mDBHelper.insert(AlarmColumn.TABLE_NAME, cv);
			
			CustomApplication.mObject=null;
		}
		
		//读取所有的闹铃信息并更新;
		mAlarms=CustomApplication.getInstance().getAllAlarms();
		updateAlarms();
	}
	
	/**
	 * 更新界面上的闹钟列表;
	 */
	private void updateAlarms(){
		mAlarmAdapter.updateListView(mAlarms);
	}
	
	/**
	 * 启动服务;
	 */
	private void startAlarmService(){
		if(mAlarms.size()==0){
			return;
		}
		
		Intent intent=new Intent(getActivity(),SleepPlanService.class);
		intent.putExtra("alarms", (Serializable)mAlarms);
		getActivity().startService(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		MrAlarm alarm=(MrAlarm) mAlarmAdapter.getItem(position);
		showDeleteDialog(alarm);
		
		return false;
	}
	
	/**
	 * 删除闹铃;
	 * @param alarm
	 */
	private void showDeleteDialog(final MrAlarm alarm){
		
		if(null==alarm){
			return;
		}
		
		DialogTips dialog = new DialogTips(getActivity(),"睡眠计划","删除该闹铃？", "确定",true,true);
		
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialogInterface, int userId) {
				
				mDBHelper.delete(AlarmColumn.TABLE_NAME, Integer.parseInt(alarm.getId()));
				
				if(mAlarms.contains(alarm)){
					mAlarms.remove(alarm);
				}
				
				updateAlarms();
				startAlarmService();
			}
		});
		dialog.show();
		dialog = null;
	}
	
	@Override
	public void onAlarmStateChange(boolean b, MrAlarm alarm) {
		
		if(false==mAlarms.contains(alarm)){
			return;
		}
		
		ContentValues cV=new ContentValues();
		cV.put(AlarmColumn.ISON,b);
		cV.put(AlarmColumn.LATITUDE, alarm.getLatitude());
		cV.put(AlarmColumn.LONGTITUDE, alarm.getLongtitude());
		
		String []args={String.valueOf(alarm.getId())};
		
		mDBHelper.update(AlarmColumn.TABLE_NAME,cV, "_id=?", args);
		alarm.setOn(b);
		
		//如果是关闭闹铃,则需要判断闹铃是否正在响起;
		if(b==false){
			if(alarm.isRing()){	
				alarm.setRing(false);
			}
		}
		
		updateAlarms();
		startAlarmService();
	}
	
	/**
	 * 注册广播;
	 */
	private void registerBroadcastReceiver(){
	
		mReceiver=new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				
				int value=intent.getExtras().getInt(Constant.BROADCAST_VALUE_TYPE);
				
				if(value==Constant.LOCATION_COMPLETED){
					
					updateAlarms();				
				}
				else if(value==Constant.ALARM_RING){
					
					int []ids=intent.getExtras().getIntArray(Constant.IDS);
					
					for(int i=0;i<ids.length;i++){
					
						int id=ids[i];
						for(MrAlarm alarm:mAlarms){
							
							if(Integer.parseInt(alarm.getId())==id){
								alarm.setRing(true);
							}
						}
					}
					updateAlarms();
				}
			}
		};
		
		IntentFilter filter=new IntentFilter();
		filter.addAction(Constant.BROADCAST_ACTION);
		getActivity().registerReceiver(mReceiver,filter);
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		
		getActivity().unregisterReceiver(mReceiver);
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
				case MSG_UPDATE_ALARMS:{
					updateAlarms();
					startAlarmService();
					break;
				}
			}
		}
	}
}
