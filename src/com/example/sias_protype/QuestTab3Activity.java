package com.example.sias_protype;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class QuestTab3Activity extends Activity {
	
	private AlertDialog waitAlert = null;
	private ListView listView = null;
	private ArrayList<HashMap<String,String>> ListMapArrayList = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter listAdapter = null;
	
	private Button deleteButton = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quest_tab3);
		
		//
		deleteButton = (Button)findViewById(R.id.Quest3_DeleteButton);
		listView = (ListView)findViewById(R.id.Quest3_ListView);
		listAdapter = new SimpleAdapter(QuestTab3Activity.this,ListMapArrayList,R.layout.quest_tab3_listlayout,
				new String[]{"Type","Object","Cost","Conn"},
				new int[] {R.id.Quest3_Type,R.id.Quest3_Object,R.id.Quest3_Cost,R.id.Quest3_Conn,});
		listView.setAdapter(listAdapter);
		
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(QuestTab3Activity.this)
		.setView(waitAlertView)
		.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quest_tab3, menu);
		return true;
	}

	public void queryButton(View view){
		LayoutInflater inflater1=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popView =inflater1.inflate(R.layout.quest3_pop_query_layout,null);
		final RadioButton bookRadio = (RadioButton)popView.findViewById(R.id.Quest3_BookButton);
		final RadioButton dailyRadio = (RadioButton)popView.findViewById(R.id.Quest3_DailyButton);
		final RadioButton gameRadio = (RadioButton)popView.findViewById(R.id.Quest3_GameButton);
		final RadioButton ticketRadio = (RadioButton)popView.findViewById(R.id.Quest3_TicketButton);
		final EditText keyWords = (EditText)popView.findViewById(R.id.Quest3_KeyWords);
		final CheckBox showAll = (CheckBox)popView.findViewById(R.id.Quest3_ShowAll);
		showAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					keyWords.setVisibility(View.GONE);
					keyWords.setText("###");
				}else{
					keyWords.setVisibility(View.VISIBLE);
				}
			}
		});
		new AlertDialog.Builder(QuestTab3Activity.this)
		.setTitle("淘淘~")
		.setView(popView)
		.setNegativeButton("取消", null)
		.setPositiveButton("查找", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(keyWords.getText().toString().isEmpty()){
					Toast.makeText(QuestTab3Activity.this, "请输入关键字！", 1000).show();
				}else{
					String key = keyWords.getText().toString();
					if(bookRadio.isChecked()){//若选中书籍
						queryTwoHand thread = new queryTwoHand("书籍",key);
						System.out.println(key);
						waitAlert.show();
						thread.start();
					}else if(dailyRadio.isChecked()){//若选中书籍
						queryTwoHand thread = new queryTwoHand("日常用品",key);
						waitAlert.show();
						thread.start();
					}else if(gameRadio.isChecked()){//若选中书籍
						queryTwoHand thread = new queryTwoHand("游戏物品",key);
						waitAlert.show();
						thread.start();
					}else if(ticketRadio.isChecked()){//若选中书籍
						queryTwoHand thread = new queryTwoHand("各种票",key);
						waitAlert.show();
						thread.start();
					}
				}
			}
		})
		.create()
		.show();
	}
	
	public void updateButton(View view){
		LayoutInflater inflater1=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popView =inflater1.inflate(R.layout.quest3_pop_update_layout,null);
		final RadioButton bookRadio = (RadioButton)popView.findViewById(R.id.Quest3_BookButtonUpdate);
		final RadioButton dailyRadio = (RadioButton)popView.findViewById(R.id.Quest3_DailyButtonUpdate);
		final RadioButton gameRadio = (RadioButton)popView.findViewById(R.id.Quest3_GameButtonUpdate);
		final RadioButton ticketRadio = (RadioButton)popView.findViewById(R.id.Quest3_TicketButtonUpdate);
		final EditText object = (EditText)popView.findViewById(R.id.Quest3_ObjectUpdate);
		final EditText cost = (EditText)popView.findViewById(R.id.Quest3_CostUpdate);
		final EditText conn = (EditText)popView.findViewById(R.id.Quest3_ConnUpdate);
		new AlertDialog.Builder(QuestTab3Activity.this)
		.setTitle("发布~")
		.setView(popView)
		.setNegativeButton("取消", null)
		.setPositiveButton("发布", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AllInfomation allInfo = (AllInfomation)getApplication();
				if(!allInfo.isRegState()){//如果未注册
					Toast.makeText(QuestTab3Activity.this, "您尚未注册或者登陆请登录并注册！", 1000).show();
				}else{
					if(object.getText().toString().isEmpty()||cost.getText().toString().isEmpty()||conn.getText().toString().isEmpty()){
						Toast.makeText(QuestTab3Activity.this, "请将信息填写完毕！", 1000).show();
					}else{
						String ID = allInfo.getUser();
						String Name = allInfo.getWelComeName();
						String Object = object.getText().toString();
						String Cost = cost.getText().toString();
						String Conn = conn.getText().toString();
						if(bookRadio.isChecked()){//若选中书籍
							updateTwoHand thread = new updateTwoHand(ID,Name,"书籍",Object,Cost,Conn);
							waitAlert.show();
							thread.start();
						}else if(dailyRadio.isChecked()){//若选中书籍
							updateTwoHand thread = new updateTwoHand(ID,Name,"日常用品",Object,Cost,Conn);
							waitAlert.show();
							thread.start();
						}else if(gameRadio.isChecked()){//若选中书籍
							updateTwoHand thread = new updateTwoHand(ID,Name,"游戏物品",Object,Cost,Conn);
							waitAlert.show();
							thread.start();
						}else if(ticketRadio.isChecked()){//若选中书籍
							updateTwoHand thread = new updateTwoHand(ID,Name,"各种票",Object,Cost,Conn);
							waitAlert.show();
							thread.start();
						}
					}
				}
			}
		})
		.create()
		.show();
	}
	
	public void controlButton(View view){
		AllInfomation allInfo = (AllInfomation)getApplication();
		if(allInfo.isRegState()){  //controlTwoHand(String ID,String info)
			controlTwoHand thread = new controlTwoHand(allInfo.getUser(),"Show");
			waitAlert.show();
			thread.start();
		}else{
			Toast.makeText(QuestTab3Activity.this, "尚未登录不能操作", 500).show();
		}
	}
	
	public void deleteButton(View view){
		AllInfomation allInfo = (AllInfomation)getApplication();
		if(allInfo.isRegState()){  //controlTwoHand(String ID,String info)
			controlTwoHand thread = new controlTwoHand(allInfo.getUser(),"Delete");
			waitAlert.show();
			thread.start();
		}else{
			Toast.makeText(QuestTab3Activity.this, "尚未登录不能操作", 500).show();
		}
	}
	//////////////////////////////////////////handler
	Handler threadHandler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0://没有结果
				waitAlert.dismiss();
				Toast.makeText(QuestTab3Activity.this, "Sorry 没有找到你要的信息~", 1000).show();
				break;
			case 1://查询成功！
				listAdapter.notifyDataSetChanged();
				waitAlert.dismiss();
				Toast.makeText(QuestTab3Activity.this, "查询成功", 1000).show();
				break;
			case 2: //查询失败
				waitAlert.dismiss();
				Toast.makeText(QuestTab3Activity.this, "查询失败", 1000).show();
				break;
			case 3://提交成功
				waitAlert.dismiss();
				Toast.makeText(QuestTab3Activity.this, "提交成功", 1000).show();
				break;
			case 4://提交失败
				waitAlert.dismiss();
				Toast.makeText(QuestTab3Activity.this, "提交失败", 1000).show();
				break;
			case 6://show my self success!
				listAdapter.notifyDataSetChanged();
				waitAlert.dismiss();
				deleteButton.setVisibility(View.VISIBLE);
				break;
			case 7://DELETE success
				listAdapter.notifyDataSetChanged();
				waitAlert.dismiss();
				deleteButton.setVisibility(View.GONE);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	/////////////////////////////////////////////thread
	class queryTwoHand extends Thread{
		
		private String Type;
		private String Object;
		
		public queryTwoHand(String Type,String Object){
			this.Type = Type;
			this.Object = Object;
		}
		@Override
		public void run() { //1 ok 2 bad
			int msgWhat = 1;
			
			JSONObject jsonSend = new JSONObject();
			try {
				jsonSend.put("Type", Type);
				jsonSend.put("Object", Object);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String info = jsonSend.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryTwoHand"));
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
						ListMapArrayList.clear();
						for(int i = 0 ; i < jsonArray.length(); i ++){
							JSONObject json = jsonArray.getJSONObject(i);
							HashMap<String,String> map = new HashMap<String,String>();
//							map.put("Rand", json.getString("Rank"));
							map.put("ID", json.getString("ID"));
							map.put("Name", json.getString("Name"));
							map.put("Type", json.getString("Type"));
							map.put("Object", json.getString("Object"));
							map.put("Cost",json.getString("Cost"));
							map.put("Conn", json.getString("Conn"));
							ListMapArrayList.add(map);
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
	
	/////提交线程
	class updateTwoHand extends Thread{

		private String ID;
		private String Name;
		private String Type;
		private String Object;
		private String Cost;
		private String Conn;
		
		public updateTwoHand(String iD, String name, String type,
				String object, String cost, String conn) {
			super();
			ID = iD;
			Name = name;
			Type = type;
			Object = object;
			Cost = cost;
			Conn = conn;
		}

		@Override
		public void run() { //3ok 4 bad
			int msgWhat = 3;
			JSONObject json = new JSONObject();
			try {
				json.put("ID", ID);
				json.put("Name", Name);
				json.put("Type", Type);
				json.put("Object", Object);
				json.put("Cost", Cost);
				json.put("Conn", Conn);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String info = json.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","UpdateTwoHand"));
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
					if(!jsonArray.getJSONObject(0).getString("State").equals("Success"))
						msgWhat = 4;
				} else {
					System.out.println("connect failed!");
					msgWhat = 4;
				}
			} catch (Exception e) {
				msgWhat = 4;
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	}
	
class controlTwoHand extends Thread{
		
		private String ID;
		private String info;
		
		public controlTwoHand(String ID,String info){
			this.ID = ID;
			this.info = info;
		}
		@Override
		public void run() { //1 ok 2 bad
			int msgWhat = 2;
			
//			JSONObject jsonSend = new JSONObject();
//			try {
//				jsonSend.put("Type", Type);
//				jsonSend.put("Object", Object);
//				
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			String info = jsonSend.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","ControlSecond"));
			params.add(new BasicNameValuePair("ID",ID));
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
						else if(jsonArray.getJSONObject(0).getString("State").equals("Success")){//delete success!!{
							ListMapArrayList.clear();
							msgWhat = 7;
						}
					}else{
						ListMapArrayList.clear();
						for(int i = 0 ; i < jsonArray.length(); i ++){
							JSONObject json = jsonArray.getJSONObject(i);
							HashMap<String,String> map = new HashMap<String,String>();
//							map.put("Rand", json.getString("Rank"));
							map.put("ID", json.getString("ID"));
							map.put("Name", json.getString("Name"));
							map.put("Type", json.getString("Type"));
							map.put("Object", json.getString("Object"));
							map.put("Cost",json.getString("Cost"));
							map.put("Conn", json.getString("Conn"));
							ListMapArrayList.add(map);
						}
						msgWhat = 6;
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
