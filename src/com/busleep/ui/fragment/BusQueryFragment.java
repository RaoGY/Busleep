package com.busleep.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.mr.busleep.R;
import com.busleep.adapter.BasePageAdapter;
import com.busleep.config.Constant;
import com.busleep.ui.base.BaseFragment;

/**
 * 公交查询片段;
 * @author Render;
 */
public class BusQueryFragment extends BaseFragment{

	private View rootView;	
	private TextView mTvRoute;
	private TextView mTvStation;
	private TextView mTvTransfer;
	
	private ViewPager mPager;
	private ImageView mIvCursor;
	private BasePageAdapter mAdapter;
	
	private int mOffset=0;			//动画偏移量;
	private int mCurrentItem=0;		//当前的编号;
	private int mBmpWidth;			//图片宽度;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(rootView==null){
			rootView=inflater.inflate(R.layout.fragment_bus_query, null);
		}
		
		initTextView();
		initViewPager();
		initImageView();
		
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
	private void initTextView(){
		
		mTvRoute=(TextView)rootView.findViewById(R.id.tv_bus_route);
		mTvStation=(TextView)rootView.findViewById(R.id.tv_bus_station);
		mTvTransfer=(TextView)rootView.findViewById(R.id.tv_bus_transfer);
		mIvCursor=(ImageView)rootView.findViewById(R.id.iv_cursor);
		
		mTvRoute.setOnClickListener(new MyOnClickListener(0));
		mTvStation.setOnClickListener(new MyOnClickListener(1));
		mTvTransfer.setOnClickListener(new MyOnClickListener(2));
		
		 mTvRoute.setSelected(true);
		 mTvStation.setSelected(false);
		 mTvTransfer.setSelected(false);
		
	}
	
	private void initViewPager(){
		mPager=(ViewPager) rootView.findViewById(R.id.vPager);
		
		mAdapter=new BasePageAdapter(getActivity());
		List<String> strNames=new ArrayList<String>();
		strNames.add(Constant.ROUTE);
		strNames.add(Constant.STATION);
		strNames.add(Constant.TRANSFER);
		mAdapter.addFragment(strNames);
		
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	
	private void initImageView(){
		mIvCursor = (ImageView) rootView.findViewById(R.id.iv_cursor);
		mBmpWidth = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度;
		mOffset = (screenW / 3 - mBmpWidth) / 2;// 计算偏移量;
		Matrix matrix = new Matrix();
		matrix.postTranslate(mOffset, 0);
		mIvCursor.setImageMatrix(matrix);// 设置动画初始位置
		
	}
	
	
	public class MyOnClickListener implements OnClickListener{

		private int index=0;
		
		public MyOnClickListener(int i){
			index=i;
		}
		
		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
			
		}
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener{
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		
			
		}

		@Override
		public void onPageSelected(int arg0) {
			
			int one=mOffset*2+mBmpWidth; // 页卡1 -> 页卡2 偏移量
			int two=one*2;				 // 页卡1 -> 页卡3 偏移量
			
			 Animation animation = null;
			 
			 switch (arg0) {
			 case 0:
				 if (mCurrentItem == 1) {
					 animation = new TranslateAnimation(one, 0, 0, 0);
				 } else if (mCurrentItem == 2) {
					 animation = new TranslateAnimation(two, 0, 0, 0);
				 }
				 mTvRoute.setSelected(true);
				 mTvStation.setSelected(false);
				 mTvTransfer.setSelected(false);
				 break;
			 case 1:
				 if (mCurrentItem == 0) {
					 animation = new TranslateAnimation(mOffset, one, 0, 0);
				 } else if (mCurrentItem == 2) {
					 animation = new TranslateAnimation(two, one, 0, 0);
				 }
				 mTvRoute.setSelected(false);
				 mTvStation.setSelected(true);
				 mTvTransfer.setSelected(false);
				 break;
			 case 2:
				 if (mCurrentItem == 0) {
					 animation = new TranslateAnimation(mOffset, two, 0, 0);
				 } else if (mCurrentItem == 1) {
					 animation = new TranslateAnimation(one, two, 0, 0);
				 }
				 mTvRoute.setSelected(false);
				 mTvStation.setSelected(false);
				 mTvTransfer.setSelected(true);
				 break;
			 }
			 mCurrentItem = arg0;
			 animation.setFillAfter(true);// True:图片停在动画结束位置
			 animation.setDuration(300);
			 mIvCursor.startAnimation(animation);
			
		}
	}
}
