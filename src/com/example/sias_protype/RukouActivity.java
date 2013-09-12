package com.example.sias_protype;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class RukouActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置为全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_welcome_scroll_layout);
		handler.postDelayed(runnable, 2000);	
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable(){
		@Override
		public void run() {
			File file = new File("/sdcard/SiasPro/Control/");
			if(file.exists()){
				Intent intent = new Intent(RukouActivity.this,MainActivity.class);
				startActivity(intent);
			}else{
				File file0 = new File("/sdcard/SiasPro/");
				file0.mkdir();
				file.mkdir();
				Intent intent = new Intent(RukouActivity.this,WelcomeActivity.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_scroll_layout, menu);
		return true;
	}

	@Override
	protected void onRestart() {
		finish();
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
}
