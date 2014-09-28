package com.busleep.ui;

import com.mr.busleep.R;
import com.busleep.db.DBHelper;
import com.busleep.slidingmenu.SlidingMenu;
import com.busleep.ui.base.BaseSlidingFragmentActivity;
import com.busleep.ui.fragment.NaviFragment;
import com.busleep.utils.ToastFactory;
import com.busleep.widget.HeaderLayout;
import com.busleep.widget.HeaderLayout.HeaderStyle;
import com.busleep.widget.HeaderLayout.onLeftImageButtonClickListener;

import android.os.Bundle;

public class MainActivity extends BaseSlidingFragmentActivity {

	public static final String TAG="MainAcivity";
	
	private NaviFragment naviFragment;
	private SlidingMenu mSlidingMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initSlidingMenu();
		initView("周边公交");
		initFragment();
	}
	
	public void initView(String title){
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(title,
				R.drawable.base_action_bar_navi_bg_selector,new OnNaviButtonClickListener());
	}
	
    private void initSlidingMenu() {
        setBehindContentView(R.layout.frame_navi);
        // customize the SlidingMenu
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
       
        mSlidingMenu.setBehindScrollScale(0);
    }
	
    /**
     * 初始化片段;
     */
    private void initFragment(){
    	
    	naviFragment=new NaviFragment();
		getSupportFragmentManager().beginTransaction().
		replace(R.id.frame_navi, naviFragment).commit();
    }
    
	/**
	 * 定义左键
	 */
	public class OnNaviButtonClickListener implements onLeftImageButtonClickListener{

		@Override
		public void onClick() {
			
           toggle();
		}
	}
	
	
	private static long firstTime;
	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		
		if (firstTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			ToastFactory.getToast(this, "再按一次退出程序").show();
			
		}
		firstTime = System.currentTimeMillis();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
        try {
            DBHelper db = DBHelper.getInstance(this);
            db.closeDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
