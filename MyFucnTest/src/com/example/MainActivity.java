package com.example;

import java.util.Calendar;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.example.mpchart.MPChartFragment;
import com.example.myfucntest.R;

public class MainActivity extends FragmentActivity {

	AQuery aq;
	private MenuDrawer mDrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDrawer = MenuDrawer.attach(this);
		mDrawer.setContentView(R.layout.activity_main);
		
		testSysInfo();
		
		View menuView = getLayoutInflater().inflate(R.layout.funcsmenu, null);
		mDrawer.setMenuView(menuView);
		
		// The drawable that replaces the up indicator in the action bar
		mDrawer.setSlideDrawable(R.drawable.ic_launcher);
		// Whether the previous drawable should be shown
		mDrawer.setDrawerIndicatorEnabled(true);

		final Fragment emotions = getSupportFragmentManager().findFragmentById(R.id.emotions);
		final Fragment mpCharts = getSupportFragmentManager().findFragmentById(R.id.mpCharts);
		
		aq = new AQuery(menuView);
		
		aq.id(R.id.demo_jword).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});

		aq.id(R.id.demo_pdf).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		aq.id(R.id.demo_emotion).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDrawer.closeMenu();
				setTitle("emotions");
				getSupportFragmentManager()
				.beginTransaction()
				.hide(mpCharts)
				.show(emotions)
				.commit();
			}
		});
		
		aq.id(R.id.demo_mpchart).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDrawer.closeMenu();
				setTitle("MPChart");
				getSupportFragmentManager()
				.beginTransaction()
				.hide(emotions)
				.show(mpCharts)
				.commit();
			}
		});
	}
	
	private void goDemo(Class<?> c){
		Intent intent = new Intent(this,c);
		startActivity(intent);
	}

	public void testSysInfo() {
		System.out.println("Java安装目录:\n"+System.getProperty("java.home"));
		System.out.println("用户的主目录:\n"+System.getProperty("user.home"));
		int i = Calendar.getInstance().get(Calendar.DATE);//多少号
		int k = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);//星期几
	}
	
	
	int [] images = {R.drawable.amp1,R.drawable.amp2,R.drawable.amp3
			,R.drawable.amp4,R.drawable.amp5,R.drawable.amp6,R.drawable.amp7};
	
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};

}
