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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {

	private AlertDialog waitAlert = null;
	int reSendACKTimes = 0;
	private String toID = "";
	private String ID = "";
	private String replayMsg = "";
	private ListView messageListView = null;
	private ArrayList<HashMap<String,String>> message = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.activity_message);
		
		Intent intent = getIntent();//获取启动参数 并选择是否从本地读取
		AllInfomation allInfo = ((AllInfomation)getApplication());
		ID = allInfo.getUser();//从本地获取ID号
		message = allInfo.getMessage();
		
		
		messageListView = (ListView)findViewById(R.id.Message_ListView);
		adapter = new SimpleAdapter(MessageActivity.this,message,
				R.layout.message_list_layout,
				new String[]{"Date","From","Msg"},//将投向位置插入
				new int[]{R.id.Message_Date,R.id.Message_From,R.id.Message_Msg});
		messageListView.setAdapter(adapter);
		messageListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int selectId = arg2;
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View popView =inflater.inflate(R.layout.message_detail_layout,null);
				TextView msgView = (TextView)popView.findViewById(R.id.MessageDetail_Msg);
				final EditText reView = (EditText)popView.findViewById(R.id.MessageDetail_Replay);
				
				msgView.setText(message.get(selectId).get("Msg"));
				toID = message.get(selectId).get("From");
				new AlertDialog.Builder(MessageActivity.this)
				.setTitle("资料卡")
				.setView(popView)
				.setNegativeButton("关闭", null)
				.setPositiveButton("回复", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						replayMsg = reView.getText().toString();
						new Thread(sendMsg).start();
						waitAlert.dismiss();
					}
				})
				.create()
				.show();
			}
		});
		
		String readFromLocal = intent.getStringExtra("Read");
		if(readFromLocal.equals("true")){
			Toast.makeText(MessageActivity.this, "从本地读取！", 2000).show();
//			message.clear();
			adapter.notifyDataSetChanged();
		}
//		else{
//			new Thread(queryMsg).start();
//		}
		
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(MessageActivity.this)
		.setView(waitAlertView)
		.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}
	
	///////点击事件
	public void updateMsg(View view){
		new Thread(queryMsg).start();
		waitAlert.show();
	}
	
	public void clearMsg(View view){
		message.clear();
		adapter.notifyDataSetChanged();
	}
	
	///////////////////////////////////////查询信息线程 
	//查询邮箱
	Runnable queryMsg = new Runnable(){  //15

		@Override
		public void run() {
			int msgWhat = 15;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryMsg")); //类型 注册信息
			params.add(new BasicNameValuePair("ID",ID));//送入信息 有没有最大限制？？？？
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
					//判断返回结果
					message.clear();//清空！
					JSONArray jsonArray = new JSONArray(sb.toString());
					if(jsonArray.getJSONObject(0).has("State")){
						if(jsonArray.getJSONObject(0).getString("State").equals("Nothing"))
							msgWhat = 13;//表示没有信息
						else
							msgWhat = 14;
						System.out.println("no msg");
						}
					else{
						for(int i = 0 ; i < jsonArray.length() ; i ++){
							HashMap<String,String> map = new HashMap<String,String>();
							map.put("Date", jsonArray.getJSONObject(i).getString("Date"));
							map.put("From", jsonArray.getJSONObject(i).getString("From"));
							map.put("Msg", jsonArray.getJSONObject(i).getString("Msg"));
//							map.put("Date", jsonArray.getJSONObject(i).getString("Date"));
							message.add(map);
						}
						AllInfomation allInfo = (AllInfomation)getApplication();
						allInfo.setMessage(message);
						System.out.println(allInfo.getMessage().get(0).get("Date"));
					}
				}else{
					System.out.println("connect failed!");
					msgWhat = 14;
				}
			}catch(Exception e){
				System.out.println("connect bug!");
				msgWhat = 14;
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
			
		}
	};
	//回复邮件
	Runnable sendMsg = new Runnable(){ //16

		@Override
		public void run() {
			int msgWhat = 16;
			JSONObject json = new JSONObject();
			try {
				json.put("From", ID);
				json.put("To", toID);
				json.put("Type", "Chat");
				json.put("Msg", replayMsg); //获取弹出框中的数据 并保存
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sendMsg = json.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","SendMsg")); //类型 注册信息
			params.add(new BasicNameValuePair("Info",sendMsg));//送入信息 有没有最大限制？？？？
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
					//判断返回结果
					JSONArray jsonArray = new JSONArray(sb.toString());
					if(!jsonArray.getJSONObject(0).getString("State").equals("Success"))
						msgWhat = 14;
				}else{
					System.out.println("connect failed!");
					msgWhat = 14;
				}
			}catch(Exception e){
				System.out.println("connect bug!");
				msgWhat = 14;
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
			
		}
		
	};
	//发回确认信息 表示收到 服务器删除信息
		Runnable sendACK = new Runnable(){  //17 失败 18

			@Override
			public void run() {
				int msgWhat = 18;
				HttpClient hc = new DefaultHttpClient();
				HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("type","ACKMsg")); //类型 注册信息
				params.add(new BasicNameValuePair("ID",ID));//送入信息 有没有最大限制？？？？
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
						//判断返回结果
						JSONArray jsonArray = new JSONArray(sb.toString());
						if(!jsonArray.getJSONObject(0).getString("State").equals("Success"))
							msgWhat = 17;
					}else{
						System.out.println("connect failed!");
						msgWhat = 17;
					}
				}catch(Exception e){
					System.out.println("connect bug!");
					msgWhat = 17;
				}
				Message msg = new Message();
				msg.what = msgWhat;
				threadHandler.sendMessage(msg);
			}
			
		};
	////////////////////////handler 区域
	Handler threadHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 13://没有信息
				waitAlert.dismiss();
				Toast.makeText(MessageActivity.this, "您没有新的信息！注定一辈子Diao si...", 2000).show();
				break;
			case 14://failed
				replayMsg = "";
				toID = "";
				waitAlert.dismiss();
				Toast.makeText(MessageActivity.this, "连接服务器失败！真相就是！不是你网有问题就是我们服务器挂了...", 2000).show();
				break;
			case 15://query Success!
				adapter.notifyDataSetChanged();
				waitAlert.dismiss();
				Toast.makeText(MessageActivity.this, "信息列表更新成功！", 2000).show();
				new Thread(sendACK).start();
				break;
			case 16://send Success!
				replayMsg = "";
				toID = "";
				Toast.makeText(MessageActivity.this, "发送信息成功！", 2000).show();
				break;
			case 17://ack failed!
				reSendACKTimes ++;
				if(reSendACKTimes > 10){
					Toast.makeText(MessageActivity.this, "发送ACK失败超过10次", 2000);
					reSendACKTimes = 0;
				}
				else
					new Thread(sendACK).start();//重复发送..
				break;
			case 18 ://ack success!
				Toast.makeText(MessageActivity.this, "Send ACK Success!", 2000).show();
			}
			super.handleMessage(msg);
		}
		
	};

}
