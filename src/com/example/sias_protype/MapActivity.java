package com.example.sias_protype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MapActivity extends Activity {
	private ImageView bgView = null;
	private ImageView mkView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//设置为全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		////////////////////////////////////////
		bgView = (ImageView)findViewById(R.id.mapBackgroundView);
		mkView = (ImageView)findViewById(R.id.mapMarkView);
		
		Intent intent = getIntent();
		String roomID = intent.getStringExtra("FindRoomById");
		Toast.makeText(this, roomID, 2000).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.map, menu);
		menu.add(0, 1, 1, R.string.findClassRoom);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			findClassRoom(50,100);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void findClassRoom(int x,int y){
		//如果查找成功
		Toast.makeText(MapActivity.this, "Find the dst!", Toast.LENGTH_SHORT).show();
		bgView.setImageResource(R.raw.maptest2);
		mkView.setPadding( mkView.getPaddingLeft(),  mkView.getPaddingTop()+200,  
				mkView.getPaddingRight(),  mkView.getPaddingBottom());
		mkView.setVisibility(View.VISIBLE);
	}
	
	public void clickMarkView(View view){
		mkView.setVisibility(View.GONE);
		mkView.setPadding(0, 0, 0, 0);
		bgView.setImageResource(R.raw.maptest1);
	}
}
