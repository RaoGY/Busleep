package com.busleep.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mr.busleep.R;
import com.busleep.ui.base.BaseFragment;

/**
 * 公交查询中线路的片段;
 * @author Administrator
 *
 */
public class RouteFragment extends BaseFragment{

	private View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if(rootView==null){
			rootView=inflater.inflate(R.layout.fragment_route,container,false);
		}
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
}
