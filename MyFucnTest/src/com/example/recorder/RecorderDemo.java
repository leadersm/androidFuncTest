package com.example.recorder;

import android.app.Activity;
import android.os.Bundle;

public class RecorderDemo extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
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
	}
}
