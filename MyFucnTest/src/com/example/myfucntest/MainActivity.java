package com.example.myfucntest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.example.myfucntest.jword.JWordTest;

public class MainActivity extends Activity {

//	AQuery aq;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		aq = new AQuery(this);
//		
//		aq.id(R.id.start).clicked(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				AudioHelper.getInstance(MainActivity.this).startRecoding("test",new AudioHelper.OnRecorderListener() {
//					
//					@Override
//					public void onRecording(int maxAmplitude) {
//						final int res = maxAmplitude/2000>(images.length-1)?6:maxAmplitude/2000;
//						h.post(new Runnable() {
//							
//							@Override
//							public void run() {
//								aq.id(R.id.imageView1).image(images[res]);
//							}
//						});
//					}
//				});
//				h.sendEmptyMessageDelayed(0, 50);		
//			}
//		});
//		
//		aq.id(R.id.stop).clicked(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				AudioHelper.getInstance(MainActivity.this).stop();
//			}
//		});
		
		startService(new Intent(this,JWordTest.class));
	}
	
	int [] images = {R.drawable.amp1,R.drawable.amp2,R.drawable.amp3
			,R.drawable.amp4,R.drawable.amp5,R.drawable.amp6,R.drawable.amp7};
	
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
