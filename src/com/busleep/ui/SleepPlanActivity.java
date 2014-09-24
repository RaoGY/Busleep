package com.busleep.ui;

import android.os.Bundle;
import com.mr.busleep.R;
import com.busleep.ui.base.BaseFragmentActivity;
import com.busleep.ui.fragment.SleepPlanFragment;

/**
 * 启动睡眠计划的Activity,该Activity是由公交线路详细信息Activity启动的，里面直接采用睡眠计划片段;
 * @author Render;
 */

public class SleepPlanActivity extends BaseFragmentActivity {

	/**
	 * 加载睡眠计划的片段;
	 */
	private SleepPlanFragment sleepPlanFragment=null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_sleep_plan);
		init();
	}
	
	//初始化;
	private void init(){
		
		initTopBarForLeft("睡眠计划");
		
		sleepPlanFragment=new SleepPlanFragment();
		getSupportFragmentManager().beginTransaction().
		replace(R.id.rl_sleep_plan, sleepPlanFragment).commit();
	}
}
