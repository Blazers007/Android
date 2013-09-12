package com.example.sias_protype;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.quest.sias_prototype.ScrollLayout;
import com.quest.sias_prototype.ScrollLayout.OnScreenChanageCallBack;

public class QuestActivity extends ActivityGroup implements OnClickListener {
	private Button tab1, tab2, tab3, tab4;
	private ScrollLayout sl;
	private View view1, view2, view3, view4,view01, view02, view03, view04;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置为全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.activity_quest);
		tab1 = (Button) findViewById(R.id.tab1);
		tab2 = (Button) findViewById(R.id.tab2);
		tab3 = (Button) findViewById(R.id.tab3);
		tab4 = (Button) findViewById(R.id.tab4);
		view01 = (View)findViewById(R.id.view01);
		view02 = (View)findViewById(R.id.view02);
		view03 = (View)findViewById(R.id.view03);
		view04 = (View)findViewById(R.id.view04);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		sl = (ScrollLayout) this.findViewById(R.id.container);
		/* 添加空白页 ,为了站位*/
		LayoutInflater inflager = LayoutInflater.from(this);
		View viewa = inflager.inflate(R.layout.whitebg, null);
		View viewb = inflager.inflate(R.layout.whitebg, null);
		View viewc = inflager.inflate(R.layout.whitebg, null);
		View viewd = inflager.inflate(R.layout.whitebg, null);
		sl.mAddView(viewa);
		sl.mAddView(viewb);
		sl.mAddView(viewc);
		sl.mAddView(viewd);
		//设置回调
		sl.setOnScreenChangeCallBack(new OnScreenChanageCallBack() {

			/**
			 * 当页面改变的时候获得 页面号page
			 * 这里为了修改tab的按下的状态
			 */
			@Override
			public void onScreenChanage(int page) {
				tab1.setBackgroundResource(R.drawable.public_twowordsbutton_background_selector);
				tab2.setBackgroundResource(R.drawable.public_twowordsbutton_background_selector);
				tab3.setBackgroundResource(R.drawable.public_twowordsbutton_background_selector);
				tab4.setBackgroundResource(R.drawable.public_twowordsbutton_background_selector);
				view01.setBackgroundResource(R.drawable.public_twowordsbutton_background_view);
				view02.setBackgroundResource(R.drawable.public_twowordsbutton_background_view);
				view03.setBackgroundResource(R.drawable.public_twowordsbutton_background_view);
				view04.setBackgroundResource(R.drawable.public_twowordsbutton_background_view);
				switch (page) {
				case 0:
					tab1.setBackgroundResource(R.drawable.public_twowordsbutton_background_dn);
					view01.setBackgroundResource(R.color.myfavor);
					break;
				case 1:
					tab2.setBackgroundResource(R.drawable.public_twowordsbutton_background_dn);
					view02.setBackgroundResource(R.color.myfavor);
					break;
				case 2:
					tab3.setBackgroundResource(R.drawable.public_twowordsbutton_background_dn);
					view03.setBackgroundResource(R.color.myfavor);
					break;
				case 3:
					tab4.setBackgroundResource(R.drawable.public_twowordsbutton_background_dn);
					view04.setBackgroundResource(R.color.myfavor);
					break;
				}
			}

			/**
			 * 滑动到下一页面，可以获得下一页面的页号page
			 * 这里直接模拟按下对应tab
			 */
			@Override
			public void glideNext(int page) {
				switch (page) {
				case 0:
					onClick(tab1);
					break;
				case 1:
					onClick(tab2);
					break;
				case 2:
					onClick(tab3);
					break;
				case 3:
					onClick(tab4);
					break;
				}
			}
		});
		// 默认按下第一个tab
		onClick(tab1);
	}

	@Override
	public void onClick(View v) {
		tab1.setBackgroundResource(R.drawable.public_twowordsbutton_background_selector);
		tab2.setBackgroundResource(R.drawable.public_twowordsbutton_background_selector);
		tab3.setBackgroundResource(R.drawable.public_twowordsbutton_background_selector);
		tab4.setBackgroundResource(R.drawable.public_twowordsbutton_background_selector);
		view01.setBackgroundResource(R.drawable.public_twowordsbutton_background_view);
    view02.setBackgroundResource(R.drawable.public_twowordsbutton_background_view);
    view03.setBackgroundResource(R.drawable.public_twowordsbutton_background_view);
    view04.setBackgroundResource(R.drawable.public_twowordsbutton_background_view);
		switch (v.getId()) {
		case R.id.tab1:
			tab1.setBackgroundResource(R.drawable.public_twowordsbutton_background_dn);
			view01.setBackgroundResource(R.color.myfavor);
			//如果没有初始化
			if (view1 == null)
			{
				//删除空白站位页
				sl.removeViewAt(0);
				//获得activity 的view
				view1 = getLocalActivityManager().startActivity("tab1",
						new Intent(this, QuestTab1Activity.class)).getDecorView();
				//添加到ScrollLayout
				sl.mAddView(view1, 0);
			}
			//滑动到0页
			sl.moveToView(0);
			
			break;
		case R.id.tab2:
			tab2.setBackgroundResource(R.drawable.public_twowordsbutton_background_dn);
			view02.setBackgroundResource(R.color.myfavor);
			if (view2 == null)
			{
				sl.removeViewAt(1);
				view2 = getLocalActivityManager().startActivity("tab2",
						new Intent(this, QuestTab2Activity.class)).getDecorView();
				sl.mAddView(view2, 1);
			}
			sl.moveToView(1);
			
			break;
		case R.id.tab3:
			tab3.setBackgroundResource(R.drawable.public_twowordsbutton_background_dn);
			view03.setBackgroundResource(R.color.myfavor);
			if (view3 == null)
			{
				sl.removeViewAt(2);
				view3 = getLocalActivityManager().startActivity("tab3",
						new Intent(this, QuestTab3Activity.class)).getDecorView();
				sl.mAddView(view3, 2);
			}
			sl.moveToView(2);
			
			break;
		case R.id.tab4:
			tab4.setBackgroundResource(R.drawable.public_twowordsbutton_background_dn);
			view04.setBackgroundResource(R.color.myfavor);
			if (view4 == null)
			{
				sl.removeViewAt(3);
				view4 = getLocalActivityManager().startActivity("tab4",
						new Intent(this, QuestTab4Activity.class)).getDecorView();
				sl.mAddView(view4, 3);
			}
			sl.moveToView(3);
			
			break;
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}
}