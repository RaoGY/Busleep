package com.busleep.ui;

import com.mr.busleep.R;
import com.busleep.ui.base.BaseActivity;

import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

	private static final long DELAY_TIME = 2000L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		//定位到主页面;
		redirectByTime();
	}
	
	private void redirectByTime() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				redictToActivity(SplashActivity.this, MainActivity.class, null);
				finish();
			}
		}, DELAY_TIME);
	}
}
