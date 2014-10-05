package com.busleep.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mr.busleep.R;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.busleep.config.Constant;
import com.busleep.ui.base.BaseFragment;
import com.busleep.utils.LogUtils;
import com.busleep.widget.ClearEditText;

/**
 * 公交查询中线路的片段;
 * @author Administrator
 *
 */
public class RouteFragment extends BaseFragment implements OnGetSuggestionResultListener,
	
	OnGetBusLineSearchResultListener{

	Thread thread;
	
	private static final String TAG = "RouteFragment";
	
	private View rootView;
	private ClearEditText mClearEditText;
	private SuggestionSearch mSuggestSearch=null;
	private BusLineSearch mBusLineSearch=null;
	private ListView mListView;
	private SimpleAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(rootView==null){
			rootView=inflater.inflate(R.layout.fragment_route,container,false);
		}
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mListView=(ListView) rootView.findViewById(R.id.list_bus_line);
		
		initSearch();
		initEditText();
	}
	
	private void initSearch(){
		
		mBusLineSearch=BusLineSearch.newInstance();
		mBusLineSearch.setOnGetBusLineSearchResultListener(this);
		
		mSuggestSearch=SuggestionSearch.newInstance();
		mSuggestSearch.setOnGetSuggestionResultListener(this);
	}
	
	private void initEditText(){
		
		mClearEditText = (ClearEditText)rootView.findViewById(R.id.et_bus_line_search);
		
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				searchSuggestBus(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	//利用百度地图进行建议搜索;
	private void searchSuggestBus(String strName){
		
		mSuggestSearch.requestSuggestion(new SuggestionSearchOption().city(Constant.city)
				.keyword(strName+"路公交"));
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		
		if (res == null||res.getAllSuggestions()==null 
				|| res.error != SearchResult.ERRORNO.NO_ERROR) {

			LogUtils.i(TAG, "建议搜索为空");
			return;
		}
			
		for(SuggestionInfo sInfo:res.getAllSuggestions()){
			
			String strCity=sInfo.city;
			String strDistrict=sInfo.district;
			String strKey=sInfo.key;
			
			LogUtils.i(TAG, strCity+" "+strDistrict+" "+strKey);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mSuggestSearch.destroy();
	}

	@Override
	public void onGetBusLineResult(BusLineResult result) {
		
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			
			LogUtils.i(TAG, "没找到该公交路线");
			return;
		}
	}
}
