package com.busleep.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mr.busleep.R;
import com.busleep.ui.MainActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 导航栏;
 * @author Render
 */
public class NaviFragment extends Fragment {
	
	private static final int BUSNEAR_FRAGMENT=0;
	private static final int BUSQUERY_FRAGMENT=1;
	private static final int SLEEPPLAN_FRAGMENT=2;
	private static final int SHARE_FRAGMENT=3;
	private static final int MORE_FRAGMENT=4;
	
	private final String LIST_TEXT = "text";
	private final String LIST_IMAGEVIEW = "img";
	
	private MainActivity mActivity;
	
	private BusNearFragment    	mBusNearFragment;		//周边公交片段;
	private BusQueryFragment	mBusQueryFragment;		//公交查询片段;
	private SleepPlanFragment	mSleepPlanFragment;		//睡眠计划片段;
	private ShareFragment    	mShareFragment;			//分享片段; 
	private MoreFragment 		mMoreFragment;			//更多片段;
	
	private FragmentManager 	mFragmentManager;
	
	private ListView			mListView;
	private SimpleAdapter		mAdapter;	
	
	//解析Fragment布局的View;
	private View rootView;
	
	/**
	 * 显示左边导航栏的Fragment;
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(rootView==null){
			rootView=inflater.inflate(R.layout.behind_slidingmenu, null);
		}
		mFragmentManager=getFragmentManager();
		
		initListView();
		
		OnTabSelected(BUSNEAR_FRAGMENT);
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		mActivity=(MainActivity)activity;
		super.onAttach(activity);
	}
	
	/**
	 * 初始化,设置点击事件;
	 */
	private void initListView(){
		
		mListView=(ListView) rootView.findViewById(R.id.behind_list_show);
		
		mAdapter=new SimpleAdapter(getActivity(), getData(),
                R.layout.behind_list_show, new String[]{LIST_TEXT,LIST_IMAGEVIEW},
                new int[]{R.id.textview_behind_title,R.id.imageview_behind_icon})
		{
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
             
                View view = super.getView(position, convertView, parent);
                
                if (position == BUSNEAR_FRAGMENT) {
                    view.setBackgroundResource(R.drawable.back_behind_list);
                    mListView.setTag(view);
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                return view;
            }
		};
		
		mListView.setAdapter(mAdapter);
	    mListView.setOnItemClickListener(new OnItemClickListener() {
	      @Override
	      public void onItemClick(AdapterView<?> parent, View view,
	                  int position, long id) {
	    	  
	    	  if (mListView.getTag() != null) {
                  if (mListView.getTag() == view) {
                      return;
                  }
                  ((View) mListView.getTag()).setBackgroundColor(Color.TRANSPARENT);
              }
	    	  
	    	  mListView.setTag(view);
              view.setBackgroundResource(R.drawable.back_behind_list);
	    	  
	    	  OnTabSelected(position);
	      }
	  });
	}
	
	/**
	 * 获取绑定的数据;
	 * @return
	 */
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(LIST_TEXT, getResources().getString(R.string.navi_menu_bus_near));
        map.put(LIST_IMAGEVIEW, R.drawable.ic_navi_home);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(LIST_TEXT, getResources().getString(R.string.navi_menu_bus_query));
        map.put(LIST_IMAGEVIEW, R.drawable.ic_navi_home);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(LIST_TEXT, getResources().getString(R.string.navi_menu_sleep_plan));
        map.put(LIST_IMAGEVIEW, R.drawable.ic_navi_home);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(LIST_TEXT, getResources().getString(R.string.navi_menu_share));
        map.put(LIST_IMAGEVIEW, R.drawable.ic_navi_home);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(LIST_TEXT, getResources().getString(R.string.navi_menu_more));
        map.put(LIST_IMAGEVIEW, R.drawable.ic_navi_home);
        list.add(map);
        
        return list;
    }
	
	/**
	 * 选中导航对应的Tab选项;
	 * @param index
	 */
	private void OnTabSelected(int index){
		
		FragmentTransaction transaction=mFragmentManager.beginTransaction();
		String strTitle=null;
		
		//先隐藏所有的Fragment;
		hideFragments(transaction);
		
		switch (index) {
			case SLEEPPLAN_FRAGMENT:{
				
				if(null==mSleepPlanFragment){
					mSleepPlanFragment=new SleepPlanFragment();
					transaction.add(R.id.main_center, mSleepPlanFragment);
				}else {
					transaction.show(mSleepPlanFragment);
					mSleepPlanFragment.updateAlarms();
				}
				strTitle=mActivity.getString(R.string.navi_menu_sleep_plan);
				break;
			}
			case BUSNEAR_FRAGMENT:{
				if(null==mBusNearFragment){
					mBusNearFragment=new BusNearFragment();
					transaction.add(R.id.main_center, mBusNearFragment);
				}else{
					transaction.show(mBusNearFragment);
				}
				strTitle=mActivity.getString(R.string.navi_menu_bus_near);
				break;
			 }	
			 case BUSQUERY_FRAGMENT:{
				 if(null==mBusQueryFragment){
					 mBusQueryFragment=new BusQueryFragment();
					 transaction.add(R.id.main_center, mBusQueryFragment); 
				 }else{
					 transaction.show(mBusQueryFragment);
				 }
				 strTitle=mActivity.getString(R.string.navi_menu_bus_query);
				 break;
			 }
			 case SHARE_FRAGMENT:{
				 if(null==mShareFragment){
					 mShareFragment=new ShareFragment();
					 transaction.add(R.id.main_center, mShareFragment);
				 }else{
					 transaction.show(mShareFragment);
				 }
				 strTitle=mActivity.getString(R.string.navi_menu_share);
				 break;
			 }
			 case MORE_FRAGMENT:{
				 if(null==mMoreFragment){
					 mMoreFragment=new MoreFragment();
					 transaction.add(R.id.main_center, mMoreFragment);
				 }else{
					 transaction.show(mMoreFragment);
				 }
				 strTitle=mActivity.getString(R.string.navi_menu_more);
				 break;
			 }
		}
		
		transaction.commit();
		
		mActivity.initView(strTitle);
		mActivity.toggle();
	}
	
	/**
	 * 将所有Fragment都置为隐藏;
	 * @param transaction
	 */
	private void hideFragments(FragmentTransaction transaction){
		if(mSleepPlanFragment!=null){
			transaction.hide(mSleepPlanFragment);
		}
		if(mBusNearFragment!=null){
			transaction.hide(mBusNearFragment);
		}
		if(mBusQueryFragment!=null){
			transaction.hide(mBusQueryFragment);
		}
		if(mShareFragment!=null){
			transaction.hide(mShareFragment);
		}
		if(mMoreFragment!=null){
			transaction.hide(mMoreFragment);
		}
	}
}
