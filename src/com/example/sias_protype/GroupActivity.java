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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GroupActivity extends Activity {

	//HeadImg
	private int[] imgSrc = {R.raw.group1,R.raw.group2,R.raw.group3,R.raw.group4,R.raw.group5,R.raw.group6,
			R.raw.group7,R.raw.group8,R.raw.group9,R.raw.group10,R.raw.group11,R.raw.group12,};
	
	private ImageView headView = null;
	///////////////////
	private String gName,gAbout,gLevel,gNum,gExp;
	private TextView gNameView = null;
	private TextView gAboutView = null;
	private TextView gLevelView = null;
	private TextView gNumView = null;
	private ProgressBar gExpBar = null;
	
	private Button checkInButton = null;
	private Button signDailyButton = null;
	private Button checkOutButton = null;
	
	private int groupId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.activity_group);
		
		Intent intent = getIntent();
		groupId = intent.getIntExtra("GroupID", 0);
		
		headView = (ImageView)findViewById(R.id.Group_HeadView);
		headView.setImageResource(imgSrc[groupId - 1]);
		
		gNameView = (TextView)findViewById(R.id.Group_NameView);
		gAboutView = (TextView)findViewById(R.id.Group_AboutView);
		gLevelView = (TextView)findViewById(R.id.Group_LevelView);
		gNumView = (TextView)findViewById(R.id.Group_NumView);
		gExpBar = (ProgressBar)findViewById(R.id.Group_ExpProgressView);
		
		checkInButton = (Button)findViewById(R.id.Grout_CheckIn);
		signDailyButton = (Button)findViewById(R.id.Group_SignDaily);
		checkOutButton = (Button)findViewById(R.id.Group_CheckOut);
		
//		Toast.makeText(this, Integer.toString(groupId), 200).show();
		
		queryGroup thread = new queryGroup(Integer.toString(groupId),"test");
		thread.start();
		
		AllInfomation allInfo = (AllInfomation)getApplication();
		if(allInfo.isRegState()){ //check if can check in
//			Toast.makeText(GroupActivity.this, "Connecting to Server ...", 200).show();
			queryIfChecked queryIfThread = new queryIfChecked(allInfo.getUser(),Integer.toString(groupId));
			queryIfThread.start();
		}else{
			Toast.makeText(GroupActivity.this, "You have not logged in Cant do further options", 1000).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
		return true;
	}
	
	public void checkIn(View view){
		AllInfomation allInfo = (AllInfomation)getApplication();
		//String ID,String gID,String Name
		CheckIn thread = new CheckIn(allInfo.getUser(),Integer.toString(groupId),allInfo.getWelComeName());
		thread.start();
//		waitAlert.show();
	}
	
	public void signDaily(View view){
		AllInfomation allInfo = (AllInfomation)getApplication();
//		String ID,String gID,String Name
		SignDayly thread = new SignDayly(allInfo.getUser(),Integer.toString(groupId),allInfo.getWelComeName());
		thread.start();
//		waitAlert.show();
	}
	
	public void checkOut(View view){
		AllInfomation allInfo = (AllInfomation)getApplication();
//		String ID,String gID
		CheckOut thread = new CheckOut(allInfo.getUser(),Integer.toString(groupId));
		thread.start();
//		waitAlert.show();
	}
	
	Handler threadHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				Toast.makeText(GroupActivity.this, "failed", 1000).show();
				break;
			case 1: //ok
				gNameView.setText(gName);
				gAboutView.setText(gAbout);
				gLevelView.setText(gLevel);
				gNumView.setText(gNum);
				gExpBar.setProgress(Integer.parseInt(gExp));
				gExpBar.setMax(Integer.parseInt(gLevel)*100);
				break;
			case 2: //bad
				Toast.makeText(GroupActivity.this, "Connect failed!", 200).show();
				break;
			case 3://  for that you have checked in
//				checkInButton.setVisibility(View.GONE);
				Toast.makeText(GroupActivity.this, "You have checked in this group", 1000).show();
				checkOutButton.setVisibility(View.VISIBLE);//you can click check out
				signDailyButton.setVisibility(View.VISIBLE);//you can click sign daily
				break;
			case 4: // for that you have not checked in
				Toast.makeText(GroupActivity.this, "You have checked not in this group you can check in", 200).show();
				checkInButton.setVisibility(View.VISIBLE); //you can click check in
				break;
			case 5://check in successful
				checkInButton.setVisibility(View.GONE);
				checkOutButton.setVisibility(View.VISIBLE);//you can click check out
				signDailyButton.setVisibility(View.VISIBLE);//you can click sign daily
				Toast.makeText(GroupActivity.this, "You hace successfully check in", 1000).show();
				break;
			case 6: //full
				Toast.makeText(GroupActivity.this, "You hace alread checkin 3 spans you must leave one first!", 1000).show();
				break;
			case 7: //success
				Toast.makeText(GroupActivity.this, "You hace sign successfully and Add 10Exp to you group!", 1000).show();
				break;
			case 8://already
				Toast.makeText(GroupActivity.this, "You hace allready signed come back tomorrow!", 1000).show();
				break;
			case 9 :
				Toast.makeText(GroupActivity.this, "Check out successfully!", 1000).show();
				checkInButton.setVisibility(View.VISIBLE);
				checkOutButton.setVisibility(View.GONE);//you can click check out
				signDailyButton.setVisibility(View.GONE);//you can click sign daily
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	/////////////////////////////////xianchengquyu
	class queryGroup extends Thread{
		
		private String gID;
		private String Object;
		
		public queryGroup(String Type,String Object){
			this.gID = Type;
			this.Object = Object;
		}
		@Override
		public void run() { //1 ok 2 bad
			int msgWhat = 1;
			
			JSONObject jsonSend = new JSONObject();
			try {
				jsonSend.put("gID", gID);
				jsonSend.put("Object", Object);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String info = jsonSend.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryGroup"));
			params.add(new BasicNameValuePair("Info",info));
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
					if(jsonArray.getJSONObject(0).has("State")){
						if(jsonArray.getJSONObject(0).getString("State").equals("Nothing"))
							msgWhat = 0;//没有查询到信息
					}else{
						for(int i = 0 ; i < jsonArray.length(); i ++){
							JSONObject json = jsonArray.getJSONObject(i);

							gName = json.getString("Name");
							gAbout = json.getString("About");
							gNum = json.getString("Num");
							gLevel = json.getString("Level");
							gExp = json.getString("Exp");
							
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
	}
	
	class queryIfChecked extends Thread{ //3 YES 4 NO 2bad
		String ID;
		String gID;
		public queryIfChecked(String ID,String gID){
			this.ID = ID;
			this.gID = gID;
		}
		@Override
		public void run() {
			int msgWhat = 4;// for that you have not checked in
			JSONObject jsonSend = new JSONObject();
			try {
				jsonSend.put("memID", ID);
				jsonSend.put("gID", gID);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String info = jsonSend.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryIfCheckedIn"));
			params.add(new BasicNameValuePair("Info",info));
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
					if(jsonArray.getJSONObject(0).has("State")){//查询成功
						if(jsonArray.getJSONObject(0).getString("State").equals("Can")) //查询成功切 无结果 说明为注册这个地盘
							msgWhat = 4;// for that you have checked in
						else
							msgWhat = 3;
					}else{
						msgWhat = 2;//说明服务端出现问题
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
	}
	
	class CheckIn extends Thread{ // 5 ok 6 fill 2 bad
		private String ID;
		private String gID;
		private String Name;
		public CheckIn(String ID,String gID,String Name){
			this.ID = ID;
			this.gID = gID;
			this.Name = Name;
		}
		@Override
		public void run() {
			int msgWhat = 5;//
			JSONObject jsonSend = new JSONObject();
			try {
				jsonSend.put("memID", ID);
				jsonSend.put("gID", gID);
				jsonSend.put("memName", Name);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String info = jsonSend.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","CheckIn"));
			params.add(new BasicNameValuePair("Info",info));
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
					if(jsonArray.getJSONObject(0).has("State")){//查询成功
						if(jsonArray.getJSONObject(0).getString("State").equals("Full"))
							msgWhat = 6;
						else if(jsonArray.getJSONObject(0).getString("State").equals("Success"))
							msgWhat = 5;
						else
							msgWhat = 2;
					}else{
						msgWhat = 2;//说明服务端出现问题
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
	}
	
	class SignDayly extends Thread{ //7 ok 8 already  2 bad
		private String ID;
		private String gID;
		private String Name;
		public SignDayly(String ID,String gID,String Name){
			this.ID = ID;
			this.gID = gID;
			this.Name = Name;
		}
		@Override
		public void run() {
			int msgWhat = 6;//
			JSONObject jsonSend = new JSONObject();
			try {
				jsonSend.put("memID", ID);
				jsonSend.put("gID", gID);
				jsonSend.put("memName", Name);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String info = jsonSend.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","SignDaily"));
			params.add(new BasicNameValuePair("Info",info));
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
					if(jsonArray.getJSONObject(0).has("State")){//查询成功
						if(jsonArray.getJSONObject(0).getString("State").equals("Success")) //query success but sql failed
							msgWhat = 7;// 7 is ok
						else if(jsonArray.getJSONObject(0).getString("State").equals("Already"))
							msgWhat = 8;
						else
							msgWhat = 2;
					}else{
						msgWhat = 2;//说明服务端出现问题
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
	}
	
	class CheckOut extends Thread{ //9 ok 10 or 2 bad
		String ID;
		String gID;
		public CheckOut(String ID,String gID){
			this.ID = ID;
			this.gID = gID;
		}
		@Override
		public void run() {
			int msgWhat = 9;//ok
			JSONObject jsonSend = new JSONObject();
			try {
				jsonSend.put("memID", ID);
				jsonSend.put("gID", gID);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String info = jsonSend.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","CheckOut"));
			params.add(new BasicNameValuePair("Info",info));
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
					JSONArray jsonArray = new JSONArray(sb.toString());
					if(jsonArray.getJSONObject(0).has("State")){//查询成功
						if(jsonArray.getJSONObject(0).getString("State").equals("Success")) //查询成功切 无结果 说明为注册这个地盘
							msgWhat = 9;//没有查询到信息
						else
							msgWhat = 2;
					}else{
						msgWhat = 2;//说明服务端出现问题
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
	}

}
