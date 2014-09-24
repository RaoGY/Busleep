package com.busleep.ui.fragment;

import com.mr.busleep.R;
import com.busleep.ui.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 导航栏;
 * @author Render
 */
public class NaviFragment extends Fragment implements OnClickListener {
	
	private static final int MAIN_FRAGMENT=0;
	private static final int SLEEPPLAN_FRAGMENT=1;
	private static final int BUSNEAR_FRAGMENT=2;
	private static final int BUSQUERY_FRAGMENT=3;
	private static final int SHARE_FRAGMENT=4;
	private static final int MORE_FRAGMENT=5;
	
	//关联主Activity;
	private MainActivity mActivity;
	
	private TextView   mainTextView; 
	private TextView   sleepPlanTextView;
	private TextView   busNearTextView;
	private TextView   busQueryTextView;
	private TextView   shareTextView;
	private TextView   moreTextView;
	
	MainFragment    	mMainFragment;			//个人中心片段;
	SleepPlanFragment	mSleepPlanFragment;		//睡眠计划片段;
	BusNearFragment    	mBusNearFragment;		//周边公交片段;
	BusQueryFragment	mBusQueryFragment;		//公交查询片段;
	ShareFragment    	mShareFragment;			//分享片段; 
	MoreFragment 		mMoreFragment;			//更多片段;
	
	private FragmentManager fragmentManager;
	
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
		fragmentManager=getFragmentManager();
		
		init();
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		
		mActivity=(MainActivity)activity;
		super.onAttach(activity);
	}
	
	/**
	 * 初始化,设置点击事件;
	 */
	private void init(){
		
		mainTextView=(TextView)rootView.findViewById(R.id.tv_navi_menu_user_center);
		sleepPlanTextView=(TextView)rootView.findViewById(R.id.tv_navi_menu_sleep_plan);
		busNearTextView=(TextView)rootView.findViewById(R.id.tv_navi_menu_bus_near);
		busQueryTextView=(TextView)rootView.findViewById(R.id.tv_navi_menu_bus_query);
		shareTextView=(TextView)rootView.findViewById(R.id.tv_navi_menu_share);
		moreTextView=(TextView)rootView.findViewById(R.id.tv_navi_menu_more);
		
		mainTextView.setSelected(false);
		sleepPlanTextView.setSelected(false);
		busNearTextView.setSelected(true);
		busQueryTextView.setSelected(false);
		shareTextView.setSelected(false);
		moreTextView.setSelected(false);
		
		mainTextView.setOnClickListener(this);
		sleepPlanTextView.setOnClickListener(this);
		busNearTextView.setOnClickListener(this);
		busQueryTextView.setOnClickListener(this);
		shareTextView.setOnClickListener(this);
		moreTextView.setOnClickListener(this);
		
		OnTabSelected(BUSNEAR_FRAGMENT);
	}
	
	/**
	 * 选中导航对应的Tab选项;
	 * @param index
	 */
	private void OnTabSelected(int index){
		
		FragmentTransaction transaction=fragmentManager.beginTransaction();
		String strTitle=null;
		
		//先隐藏所有的Fragment;
		hideFragments(transaction);
		
		switch (index) {
			case MAIN_FRAGMENT:{
	
				if(null==mMainFragment){
					mMainFragment=new MainFragment();
					transaction.add(R.id.main_center, mMainFragment);
				}else {
					transaction.show(mMainFragment);
				}
				strTitle=mActivity.getString(R.string.navi_menu_user_center);
				break;
			}
			case SLEEPPLAN_FRAGMENT:{
				
				if(null==mSleepPlanFragment){
					mSleepPlanFragment=new SleepPlanFragment();
					transaction.add(R.id.main_center, mSleepPlanFragment);
				}else {
					transaction.show(mSleepPlanFragment);
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
		if(mMainFragment!=null){
			transaction.hide(mMainFragment);
		}
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
	
	/**
	 * 点击导航栏切换 同时更改标题;
	 */
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
		switch (view.getId()) {
		case R.id.tv_navi_menu_user_center:
			mainTextView.setSelected(true);
			sleepPlanTextView.setSelected(false);
			busNearTextView.setSelected(false);
			busQueryTextView.setSelected(false);
			shareTextView.setSelected(false);
			moreTextView.setSelected(false);
			
			OnTabSelected(MAIN_FRAGMENT);
			break;
			
		case R.id.tv_navi_menu_sleep_plan:
			mainTextView.setSelected(false);
			sleepPlanTextView.setSelected(true);
			busNearTextView.setSelected(false);
			busQueryTextView.setSelected(false);
			shareTextView.setSelected(false);
			moreTextView.setSelected(false);
			
			OnTabSelected(SLEEPPLAN_FRAGMENT);
			break;
		
		case R.id.tv_navi_menu_bus_near:
			mainTextView.setSelected(false);
			sleepPlanTextView.setSelected(false);
			busNearTextView.setSelected(true);
			busQueryTextView.setSelected(false);
			shareTextView.setSelected(false);
			moreTextView.setSelected(false);
			
			OnTabSelected(BUSNEAR_FRAGMENT);
			break;
			
		case R.id.tv_navi_menu_bus_query:
			mainTextView.setSelected(false);
			sleepPlanTextView.setSelected(false);
			busNearTextView.setSelected(false);
			busQueryTextView.setSelected(true);
			shareTextView.setSelected(false);
			moreTextView.setSelected(false);
			
			OnTabSelected(BUSQUERY_FRAGMENT);
			break;
			
		case R.id.tv_navi_menu_share:
			mainTextView.setSelected(false);
			sleepPlanTextView.setSelected(false);
			busNearTextView.setSelected(false);
			busQueryTextView.setSelected(false);
			shareTextView.setSelected(true);
			moreTextView.setSelected(false);
			
			OnTabSelected(SHARE_FRAGMENT);
			break;
			
		case R.id.tv_navi_menu_more:
			mainTextView.setSelected(false);
			sleepPlanTextView.setSelected(false);
			busNearTextView.setSelected(false);
			busQueryTextView.setSelected(false);
			shareTextView.setSelected(false);
			moreTextView.setSelected(true);
			
			OnTabSelected(MORE_FRAGMENT);
			break;
		
		}	
	}
}
