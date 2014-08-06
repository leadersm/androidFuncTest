package com.example;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.example.myfucntest.R;
import com.example.pdf.PDFWriterDemo;

public class MainActivity extends Activity {

	AQuery aq;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		testSysInfo();
		
		aq = new AQuery(this);
		
		aq.id(R.id.demo_jword).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});

		aq.id(R.id.demo_pdf).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goDemo(PDFWriterDemo.class);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
