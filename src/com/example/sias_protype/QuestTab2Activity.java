package com.example.sias_protype;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuestTab2Activity extends Activity {
	
	private AlertDialog waitAlert = null;
	private String ID = "";
	private String groupNames = "";
	private String groupNums = "";
	private String groupLevels = "";
	private int myGroupNum = 0;
	
	private TextView gName1 = null;private TextView gName2 = null;private TextView gName3 = null;
	private TextView gNum1 = null;private TextView gNum2 = null;private TextView gNum3 = null;
	private TextView gLevel1 = null;private TextView gLevel2 = null;private TextView gLevel3 = null;
//	private static int[][] frameListRes = {{R.id.Quest2_Frame1,R.id.Quest2_Frame2,R.id.Quest2_Frame3},
//			{R.id.Quest2_Frame4,R.id.Quest2_Frame5,R.id.Quest2_Frame6},
//			{R.id.Quest2_Frame7,R.id.Quest2_Frame8,R.id.Quest2_Frame9},
//			{R.id.Quest2_Frame10,R.id.Quest2_Frame11,R.id.Quest2_Frame12}};
	private FrameLayout frame1 = null;private FrameLayout frame2 = null;private FrameLayout frame3 = null;private FrameLayout frame4 = null;private FrameLayout frame5 = null;private FrameLayout frame6 = null;
	private FrameLayout frame7 = null;private FrameLayout frame8 = null;private FrameLayout frame9 = null;private FrameLayout frame10 = null;private FrameLayout frame11 = null;private FrameLayout frame12 = null;
	private FrameLayout [][] layoutList = new FrameLayout[4][3];
	private int [][]  groupLevel = new int[4][3];
	private int [][] groupNum = new int[4][3];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quest_tab2);
		
		gName1 = (TextView)findViewById(R.id.Quest2_SpanName1);gName2 = (TextView)findViewById(R.id.Quest2_SpanName2);gName3 = (TextView)findViewById(R.id.Quest2_SpanName3);
		gNum1 = (TextView)findViewById(R.id.Quest2_SpanNum1);gNum2 = (TextView)findViewById(R.id.Quest2_SpanNum2);gNum3 = (TextView)findViewById(R.id.Quest2_SpanNum3);
		gLevel1 = (TextView)findViewById(R.id.Quest2_SpanLevel1);gLevel2 = (TextView)findViewById(R.id.Quest2_SpanLevel2);gLevel3 = (TextView)findViewById(R.id.Quest2_SpanLevel3);
		
		frame1 = (FrameLayout)findViewById(R.id.Quest2_Frame1);frame2 = (FrameLayout)findViewById(R.id.Quest2_Frame2);
		frame3 = (FrameLayout)findViewById(R.id.Quest2_Frame3);frame4 = (FrameLayout)findViewById(R.id.Quest2_Frame4);
		frame5 = (FrameLayout)findViewById(R.id.Quest2_Frame5);frame6 = (FrameLayout)findViewById(R.id.Quest2_Frame6);
		frame7 = (FrameLayout)findViewById(R.id.Quest2_Frame7);frame8 = (FrameLayout)findViewById(R.id.Quest2_Frame8);
		frame9 = (FrameLayout)findViewById(R.id.Quest2_Frame9);frame10 = (FrameLayout)findViewById(R.id.Quest2_Frame10);
		frame11 = (FrameLayout)findViewById(R.id.Quest2_Frame11);frame12 = (FrameLayout)findViewById(R.id.Quest2_Frame12);
		layoutList[0][0] = frame1;layoutList[0][1] = frame2;layoutList[0][2] = frame3;layoutList[1][0] = frame4;layoutList[1][1] = frame5;layoutList[1][2] = frame6;
		layoutList[2][0] = frame7;layoutList[2][1] = frame8;layoutList[2][2] = frame9;layoutList[3][0] = frame10;layoutList[3][1] = frame11;layoutList[3][2] = frame12;

		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(QuestTab2Activity.this)
		.setView(waitAlertView)
		.create();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.lesson, menu);
		menu.add(0, 1, 1, "说明");
		menu.add(0,2,2,"返回");
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			new AlertDialog.Builder(QuestTab2Activity.this)
			.setTitle("帮助说明")
			.setMessage("地盘是一个你的标签\n 你一共可以加入三个地盘\n 每天你可以为一个地盘签到增加EXP \n 地盘会随着人数以及等级发生变化！")
			.setNegativeButton("确定", null)
			.create()
			.show();
			break;
		case 2:
			Intent intent = new Intent(QuestTab2Activity.this,MainActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void group(View view){
		Intent intent = new Intent(QuestTab2Activity.this,GroupActivity.class);
		switch(view.getId()){
		case R.id.Quest2_Gruop1:
			intent.putExtra("GroupID", 1);
			startActivity(intent);
			break;
		case R.id.Quest2_Group2:
			intent.putExtra("GroupID", 2);
			startActivity(intent);
			break;
		case R.id.Quest2_Group3:
			intent.putExtra("GroupID", 3);
			startActivity(intent);
			break;
		case R.id.Quest2_Gruop4:
			intent.putExtra("GroupID", 4);
			startActivity(intent);
			break;
		case R.id.Quest2_Group5:
			intent.putExtra("GroupID", 5);
			startActivity(intent);
			break;
		case R.id.Quest2_Group6:
			intent.putExtra("GroupID", 6);
			startActivity(intent);
			break;
		case R.id.Quest2_Group7:
			intent.putExtra("GroupID", 7);
			startActivity(intent);
			break;
		case R.id.Quest2_Group8:
			intent.putExtra("GroupID", 8);
			startActivity(intent);
			break;
		case R.id.Quest2_Group9:
			intent.putExtra("GroupID", 9);
			startActivity(intent);
			break;
		case R.id.Quest2_Group10:
			intent.putExtra("GroupID", 10);
			startActivity(intent);
			break;
		case R.id.Quest2_Group11:
			intent.putExtra("GroupID", 11);
			startActivity(intent);
			break;
		case R.id.Quest2_Group12:
			intent.putExtra("GroupID", 12);
			startActivity(intent);
			break;
		}
	}
	public void search(View view){
		waitAlert.show();
		new Thread(queryGroupInfo).start();
		AllInfomation allInfo = (AllInfomation)getApplication();
		if(allInfo.isRegState()){
			ID = allInfo.getUser();
			new Thread(queryMyGroup).start();
		}else{
			Toast.makeText(QuestTab2Activity.this, "尚未登录不能查询我的地盘", 1000).show();
		}
	}
	
	////////////////////////////////////////////////////////
	Handler threadHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1: //good
				Toast.makeText(QuestTab2Activity.this, "InitSuccess", 500).show();
				for(int i = 0 ; i < 4 ; i++)
					setSize(layoutList[i][0],groupLevel[i][0],groupNum[i][0],
							layoutList[i][1],groupLevel[i][1],groupNum[i][1],
							layoutList[i][2],groupLevel[i][2],groupNum[i][2]);
				
				waitAlert.dismiss();
				break;
			case 2: //bad
				Toast.makeText(QuestTab2Activity.this, "Failed", 1000).show();
				waitAlert.dismiss();
				break;
			case 3:
				String[] names = {"","",""} ;
				String[] nums = {"","",""} ;
				String[] levels = {"","",""} ;
				for(int i = 0 ; i <  myGroupNum ; i ++){
					names[i] = groupNames.split("#")[i];
					nums[i] = groupNums.split("#")[i];
					levels[i] = groupLevels.split("#")[i];
				}
				gName1.setText(names[0]);gName2.setText(names[1]);gName3.setText(names[2]);
				gNum1.setText(nums[0]);gNum2.setText(nums[1]);gNum3.setText(nums[2]);
				gLevel1.setText(levels[0]);gLevel2.setText(levels[1]);gLevel3.setText(levels[2]);
				groupNames = "";
				groupNums = ""; 
				groupLevels = "";
				myGroupNum = 0;
				waitAlert.dismiss();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	Runnable queryMyGroup = new Runnable(){ //3good 4 bad!
		@Override
		public void run() {
			int msgWhat = 3;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryUserGroups"));
			params.add(new BasicNameValuePair("ID",ID));
//			hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
//			hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
			hp.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//很重要！！！
			HttpResponse hr = null;
			try {
				// 发出HTTP request
				hp.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				// 取得HTTP response
				hr = hc.execute(hp);   //执行
				// 若状态码为200 ok
				if (hr.getStatusLine().getStatusCode() == 200) {   //返回值正常
					HttpEntity entity = hr.getEntity();
					InputStream is = entity.getContent();
					StringBuffer sb = new StringBuffer();
					while(true){
						byte[] buffer = new byte[10240];
						int len = is.read(buffer);
						if(len == -1)
							break;
						sb.append(new String(buffer,0,len,"utf-8"));
					}
					//////////////查找结果为空！怎么办
					
					///////////////
					JSONArray jsonArray = new JSONArray(sb.toString());
					if(jsonArray.getJSONObject(0).has("State")){//error happen
						msgWhat = 2;//没有查询到信息
					}else{ //no state says that goes well
						for(int i = 0 ; i <jsonArray.length(); i ++){
							myGroupNum ++;
							groupNames += (jsonArray.getJSONObject(i).getString("Name")+"#");
							groupNums += (jsonArray.getJSONObject(i).getString("Num")+"#");
							groupLevels += (jsonArray.getJSONObject(i).getString("Level")+"#");
						}
						System.out.println(groupNames);
						msgWhat = 3;
					}
				} else {
					msgWhat = 2;
					System.out.println("connect failed!");
				}
			} catch (Exception e) {
				msgWhat = 2;
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	};
	
	
	Runnable queryGroupInfo = new Runnable(){ //1 GOOD 2 BAD
		@Override
		public void run() {
			int msgWhat = 1;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","InitGroup"));
//			hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
//			hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
			hp.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//很重要！！！
			HttpResponse hr = null;
			try {
				// 发出HTTP request
				hp.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				// 取得HTTP response
				hr = hc.execute(hp);   //执行
				// 若状态码为200 ok
				if (hr.getStatusLine().getStatusCode() == 200) {   //返回值正常
					HttpEntity entity = hr.getEntity();
					InputStream is = entity.getContent();
					StringBuffer sb = new StringBuffer();
					while(true){
						byte[] buffer = new byte[10240];
						int len = is.read(buffer);
						if(len == -1)
							break;
						sb.append(new String(buffer,0,len,"utf-8"));
					}
					//////////////查找结果为空！怎么办
					
					///////////////
					JSONArray jsonArray = new JSONArray(sb.toString());
					if(jsonArray.getJSONObject(0).has("State")){//error happen
						msgWhat = 2;//没有查询到信息
					}else{ //no state says that goes well
						int queue = 0;
						for(int i = 0 ; i < 4 ; i ++){
							for(int j = 0; j < 3 ; j++){
								groupLevel[i][j] =Integer.parseInt(jsonArray.getJSONObject(queue).getString("Level"));
								groupNum[i][j] =Integer.parseInt(jsonArray.getJSONObject(queue).getString("Num"));
								queue ++;
								System.out.println(groupLevel[i][j]+"  "+groupNum[i][j]);
							}
						}
					}
				} else {
					msgWhat = 2;
					System.out.println("connect failed!");
				}
			} catch (Exception e) {
				msgWhat = 2;
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	};
	//设置一行的大小比例
	public void setSize(FrameLayout left,int leftLevel,int leftNum,
						 FrameLayout mid,int midLevel,int midNum,
						 FrameLayout right,int rightLevel,int rightNum){
		DisplayMetrics dm = new DisplayMetrics();// 声明一个手机显示参数对象
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    int width = dm.widthPixels-100;//获取宽度
//	    System.out.println(width);
	    //获得手机的宽带和高度像素单位为px
	    int leftW = (int)(width*((double)leftNum/(double)(leftNum+midNum+rightNum)));
	    int midW = (int)(width*((double)midNum/(double)(leftNum+midNum+rightNum)));
	    int rightW = (int)(width*((double)rightNum/(double)(leftNum+midNum+rightNum)));
	    
	    System.out.println(leftW);
	    System.out.println(midW);
	    System.out.println(rightW);
//	    System.out.println((double)width*(((double)leftNum/(double)(leftNum+midNum+rightNum))));
//	    System.out.println((double)width*(double)((midNum/(leftNum+midNum+rightNum))));
//	    System.out.println(width*(double)((rightNum/(leftNum+midNum+rightNum))));
//	    
//	    int leftW = leftNum * 80;
//	    int midW = midNum * 80;
//	    int rightW = rightNum * 80;
	    
	    int height = width/3;
	    int leftH = leftLevel*height;
	    int midH = midLevel*height;
	    int rightH = rightLevel*height;
	    
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(leftW,leftH);
	    left.setLayoutParams(params);
	    params = new LinearLayout.LayoutParams(midW,midH);
	    mid.setLayoutParams(params);
	    params = new LinearLayout.LayoutParams(rightW,rightH);
	    right.setLayoutParams(params);
	}
}
