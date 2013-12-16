package com.example.sias_protype;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {

	private AlertDialog waitAlert = null;
	///////////string lei~~
	private String name = "";private String title = "";private String level = "";private String sex = "";private String exp = "";private String sign = "";
	private String height = "";private String weight = "";private String skinRating = "";private String faceRating = "";private int headImg;
	
	private int[] imgSrc = {R.raw.hp1,R.raw.hp2,R.raw.hp3,R.raw.hp4,R.raw.hp5,R.raw.hp6,R.raw.hp7,R.raw.hp8,
			R.raw.hp9,R.raw.hp10,R.raw.hp11,R.raw.hp12,R.raw.hp13,R.raw.hp14,R.raw.hp15,R.raw.hp16,R.raw.hp17,R.raw.hp18,R.raw.hp19,R.raw.hp20};
	private ImageView headPhoto = null;
	
	private ProgressBar expBar = null;
	
	private String groupNames = "";
	private TextView locationView = null;
	private TextView nameView = null;
	private TextView levelView = null;
	private TextView titleView = null;
	private TextView sexView = null;
	private TextView expView = null;
	private TextView signView = null;
	private TextView heightView = null;
	private TextView weightView = null;
	
	private RatingBar skinRate = null;
	private RatingBar faceRate = null;
	////////////////////////////////控制发送信息 跟加为好友
	private ImageButton addFriendView = null;
	private boolean ifHasAdd;
	private ImageButton sendMessageView = null;
	private ImageButton uploadImgView = null;
	private ImageButton showListView = null;
	private String messageToSend= null;//获取信息
	private String myID = null;
	private String ID = "";
	private String groupID = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
		setContentView(R.layout.activity_profile);
		
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(ProfileActivity.this)
		.setView(waitAlertView)
		.create();
		
		headPhoto = (ImageView)findViewById(R.id.Info_HeadPhoto);
		
		expBar = (ProgressBar)findViewById(R.id.Info_ExpBar);
		
		locationView = (TextView)findViewById(R.id.Info_Location);
		nameView = (TextView)findViewById(R.id.Info_Name);
		levelView = (TextView)findViewById(R.id.Info_Level);
		titleView = (TextView)findViewById(R.id.Info_Title);
		sexView = (TextView)findViewById(R.id.Info_Sex);
		expView = (TextView)findViewById(R.id.Info_Exp);
		signView = (TextView)findViewById(R.id.Info_Sign);
		heightView = (TextView)findViewById(R.id.Info_Height);
		weightView = (TextView)findViewById(R.id.Info_Weight);
		
		skinRate = (RatingBar)findViewById(R.id.Info_skinRate);
		faceRate = (RatingBar)findViewById(R.id.Info_faceRate);
		skinRate.setRating(2);
		faceRate.setRating(5);
		
		addFriendView = (ImageButton)findViewById(R.id.Profile_Add);
		sendMessageView = (ImageButton)findViewById(R.id.Profile_Send);
		uploadImgView = (ImageButton)findViewById(R.id.Profile_UploadImg);
		showListView = (ImageButton)findViewById(R.id.Profile_ShowList);
		
		
		Intent intent = this.getIntent();//获取intent 或者从 all中读取
		boolean autoFind = intent.getBooleanExtra("AutoFind",true);//若未指定则默认为真
		ID = intent.getStringExtra("ID");//因为这个是上级传来的 并不一定是自己的！！这个是目标ID
		Toast.makeText(ProfileActivity.this, ID, 2000).show();
		//与本地用户对比 显示或隐藏两个按钮
		AllInfomation allInfo = ((AllInfomation)getApplication());
		if(allInfo.getLogState()){ //如果已经登录
			myID = allInfo.getUser();
			if(myID.equals(ID)){
				addFriendView.setVisibility(View.GONE);
				sendMessageView.setVisibility(View.GONE);//若是自己的也面 则设置为隐藏
			}
			if(autoFind){
				new Thread(queryUserInfo).start();
				groupID = myID;
				new Thread(queryMyGroup).start();
				waitAlert.show();
			}else{
				waitAlert.show();
				headImg = Integer.parseInt(intent.getStringExtra("Head"));
				ID = intent.getStringExtra("ID");
				groupID = ID;
				new Thread(queryMyGroup).start();
				
				name = intent.getStringExtra("Name");
				level = intent.getStringExtra("Level");
				title = intent.getStringExtra("Title");
				exp = intent.getStringExtra("Exp");
				sign = intent.getStringExtra("Sign");
				height = intent.getStringExtra("height");
				weight = intent.getStringExtra("weight");
				skinRating = intent.getStringExtra("SkinRating");
				faceRating = intent.getStringExtra("FaceRating");
				
				setHeadImg(headImg);
				nameView.setText(name);
				levelView.setText(level);
				titleView.setText(title);
				expView.setText(exp+"/"+Integer.parseInt(level)*100);
				signView.setText(sign);
				heightView.setText(height);
				weightView.setText(weight);
				skinRate.setRating(Float.parseFloat(skinRating));
				faceRate.setRating(Float.parseFloat(faceRating));
				expBar.setMax(Integer.parseInt(level)*100);//当前等级的经验上限
				expBar.setProgress(Integer.parseInt(exp));
				waitAlert.dismiss();
			}
			new Thread(queryIfHasAdd).start();
		}
		else {//尚未登录
			addFriendView.setVisibility(View.GONE);
			sendMessageView.setVisibility(View.GONE);
			uploadImgView.setVisibility(View.GONE);
			showListView.setVisibility(View.GONE);
			new Thread(queryUserInfo).start();
			Toast.makeText(ProfileActivity.this, "您尚未登录，无法使用某些功能!", 2000).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
//////////////////////////////////////////////////////////////////Handler 区域
	Handler threadHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				nameView.setText(name);
				levelView.setText(level);
				titleView.setText(title);
				sexView.setText(sex);
				expView.setText(exp+"/"+Integer.parseInt(level)*100);
				signView.setText(sign);
				heightView.setText(height);
				weightView.setText(weight);
				skinRate.setRating(Float.parseFloat(skinRating)/2);
				faceRate.setRating(Float.parseFloat(faceRating)/2);
				expBar.setMax(Integer.parseInt(level)*100);//当前等级的经验上限
				expBar.setProgress(Integer.parseInt(exp));
				setHeadImg(headImg);//设置头像
				waitAlert.dismiss();
				break;
			case 1:
				Toast.makeText(ProfileActivity.this, "该用户尚未注册！", 2000).show();
				addFriendView.setVisibility(View.GONE);
				sendMessageView.setVisibility(View.GONE);
				uploadImgView.setVisibility(View.GONE);
				showListView.setVisibility(View.GONE);
				break;
			case 2:
				System.out.println("Connect failed");
				break;
			case 3:
//				addFriendView.setText("取消关注");
				addFriendView.setImageResource(R.drawable.user_delete);
				ifHasAdd = true;
				break;
			case 15:
				System.out.println("Has not add friend!");
				break;
			case 16://已经添加好友
//				addFriendView.setText("取消关注");
				addFriendView.setImageResource(R.drawable.user_delete);
				ifHasAdd = true;
				break;
			case 17:
				Toast.makeText(ProfileActivity.this, "连接失败！！", 2000).show();
				break;
				
			case 19:
				Toast.makeText(ProfileActivity.this, "取消关注失败！", 2000).show();
				break;
				
			case 20:
				Toast.makeText(ProfileActivity.this, "取消关注成功！", 2000).show();
//				addFriendView.setText("加为关注");
				addFriendView.setImageResource(R.drawable.user_add);
				ifHasAdd = false;
				break;	
				
			case 21:
				locationView.setText(groupNames);
				break;
			case 22:
				Toast.makeText(ProfileActivity.this, "发送成功", 2000).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	/////////////////////////////////////////////设置头像
	void setHeadImg(int i){
		headPhoto.setImageResource(imgSrc[i-1]);
	}
	//////////////////////////////////////////////////////////////////线程区域
	Runnable queryUserInfo = new Runnable(){ //1没有 2失败 0成功
		@Override
		public void run() {
			int msgWhat = 0;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryUserInfo"));
			params.add(new BasicNameValuePair("ID",ID));
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
					JSONObject json = jsonArray.getJSONObject(0);//获取JSON
//					Info_String[0] = json.getString("ID");
					if(json.getString("Name").equals("sb"))
						msgWhat = 1;
					else{
						title = json.getString("Title");
						if(json.getString("Sex").equals("0"))
							sex = "Female";
						else
							sex = "Male";
						sign = json.getString("Sign");
						name = json.getString("Name");
						height = json.getString("Height");
						weight = json.getString("Weight");
						level = json.getString("Level");
						exp = json.getString("Exp");
						skinRating = json.getString("SkinRating");
						faceRating = json.getString("FaceRating");
						headImg = Integer.parseInt(json.getString("HeadImg"));
					}
				} else {
					System.out.println("connect failed!");
					msgWhat = 2;
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
	
	Runnable addFriend = new Runnable(){ //3成功 2失败
		@Override
		public void run() {
			int msgWhat = 3;
			JSONObject json = new JSONObject();
			try {
				json.put("From", myID);//myID 要去关注 ID
				json.put("To", ID);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sendMsg = json.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","AddFriend")); //类型 注册信息
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
						msgWhat = 2;
				}else{
					System.out.println("connect failed!");
					msgWhat = 2;
				}
			}catch(Exception e){
				System.out.println("connect bug!");
				msgWhat = 2;
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	};
	
	Runnable sendMsg = new Runnable(){
		@Override
		public void run() {
			int msgWhat = 1;
			JSONObject json = new JSONObject();
			try {
				json.put("From", myID);
				json.put("To", ID);
				json.put("Type", "Chat");
				json.put("Msg", messageToSend); //获取弹出框中的数据 并保存
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sendMsg = json.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
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
						msgWhat = 2;
				}else{
					System.out.println("connect failed!");
					msgWhat = 2;
				}
			}catch(Exception e){
				System.out.println("connect bug!");
				msgWhat = 2;
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	};
	
	 
	 Runnable queryIfHasAdd = new Runnable(){ //15 未添加 16 添加 17失败 18 成功
		@Override
		public void run() {
			int msgWhat = 17;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryIfHasAdd")); //类型 注册信息
			params.add(new BasicNameValuePair("ID",myID));//送入信息 有没有最大限制？？？？
			params.add(new BasicNameValuePair("name",ID));//送入信息 有没有最大限制？？？？
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
					if(jsonArray.getJSONObject(0).getString("State").equals("Yes"))
						msgWhat = 16;
					else if(!jsonArray.getJSONObject(0).getString("State").equals("No"))
						msgWhat = 15;
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
	 
	 Runnable minFriend = new Runnable(){ // 19失败 20成功
		@Override
		public void run() {
			int msgWhat = 20;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","MinFriend")); //类型 注册信息
			params.add(new BasicNameValuePair("ID",myID));//送入信息 有没有最大限制？？？？
			params.add(new BasicNameValuePair("name",ID));//送入信息 有没有最大限制？？？？
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
						msgWhat = 19;
				}else{
					System.out.println("connect failed!");
					msgWhat = 19;
				}
			}catch(Exception e){
				System.out.println("connect bug!");
				msgWhat = 19;
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	 };
	///////////////////////////////////////////////////////////////////按钮相应区域
	public void uploadImg(View view){ //点击按钮上传图像
		new Thread(uploadImg).start();
		waitAlert.show();
	}
	
	public void haveAFight(View viwe){
		Toast.makeText(ProfileActivity.this, "开发中...", 1000).show();
	}
	
	public void friendlist(View view){
		Intent intent = new Intent(ProfileActivity.this,FriendActivity.class);
		intent.putExtra("ID", ID);
		startActivity(intent);
	}
	
	public void addfriend(View view){
		if(ifHasAdd){
			Toast.makeText(ProfileActivity.this, "取消关注ing", 1000).show();;
			new Thread(minFriend).start();
		}else{
			Toast.makeText(ProfileActivity.this, "添加关注ing", 1000).show();;
			new Thread(addFriend).start();
		}
	}
	
	public void sendmessage(View view){
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popView=inflater.inflate(R.layout.send_message_layout, null);
		final EditText editMsg = (EditText) popView.findViewById(R.id.Profile_SendMsg);
		new AlertDialog.Builder(ProfileActivity.this)
		.setTitle("发送信息")
		.setView(popView)
		.setNegativeButton("取消", null)
		.setPositiveButton("发送", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				messageToSend = editMsg.getText().toString();
				new Thread(sendMsg).start();
			}
		})
		.create()
		.show();
//		new Thread(sendMsg).start();
	}
	///////////////////////////////////////////////////////////////////////线程区域
	
	Runnable queryMyGroup = new Runnable(){ //3good 4 bad!
		@Override
		public void run() {
			int msgWhat = 21;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryUserGroups"));
			params.add(new BasicNameValuePair("ID",groupID));
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
							groupNames += (jsonArray.getJSONObject(i).getString("Name")+" ");
						}
						System.out.println(groupNames);
						msgWhat = 21;
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
	
	Runnable uploadImg = new Runnable(){

		@Override
		public void run() {
			upload();
		}
	};
	 public void upload() {
		 
		 String ID = "20101111111",region = "middle";
//	        Log.d(TAG, "upload begin");
	        HttpURLConnection connection = null;
	        DataOutputStream dos = null;
	        FileInputStream fin = null;
	        
	        String boundary = "---------------------------265001916915724";
	        // 真机调试的话，这里url需要改成电脑ip
	        // 模拟机用10.0.0.2,127.0.0.1被tomcat占用了
	        String urlServer = "http://192.168.163.1:8080/ServerForSias/upload";
	        String lineEnd = "\r\n";
	        String pathOfPicture = "/sdcard/bosschen.jpg";
	        int bytesAvailable, bufferSize, bytesRead;
	        int maxBufferSize = 1*1024*512;
	        byte[] buffer = null;
	        
	        try {
//	            Log.d(TAG, "try");
	            URL url = new URL(urlServer);
	            connection = (HttpURLConnection) url.openConnection();     
	            // 允许向url流中读写数据
	            connection.setDoInput(true);
	            connection.setDoOutput(true);
	            connection.setUseCaches(true);
	            // 启动post方法
	            connection.setRequestMethod("POST");
	            
	            // 设置请求头内容
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("Content-Type", "text/plain");
	            // 伪造请求头
	            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	            
	            // 开始伪造POST Data里面的数据
	            dos = new DataOutputStream(connection.getOutputStream());
	            fin = new FileInputStream(pathOfPicture);
	            
//	            Log.d(TAG, "开始上传images");
	            //--------------------开始伪造上传images.jpg的信息-----------------------------------
	            String fileMeta ="--" + boundary + lineEnd +
	            				"Content-Disposition: form-data; name=\"ID\"" + lineEnd + lineEnd+ ID + lineEnd +
	            				"--" + boundary + lineEnd +
	            				"Content-Disposition: form-data; name=\"region\"" + lineEnd + lineEnd + region + lineEnd +
	            				"--" + boundary + lineEnd +         //此处是file web控件的名字       //文件名字去除吊路径！
	                            "Content-Disposition: form-data; name=\"file\"; filename=\"" + pathOfPicture + "\"" + lineEnd +
	                            "Content-Type: image/jpeg" + lineEnd + lineEnd;
	            // 向流中写入fileMeta
	            dos.write(fileMeta.getBytes());
	            
	            // 取得本地图片的字节流，向url流中写入图片字节流
	            bytesAvailable = fin.available();
	            bufferSize = Math.min(bytesAvailable, maxBufferSize);
	            buffer = new byte[bufferSize];
	            
	            bytesRead = fin.read(buffer, 0, bufferSize);
	            while(bytesRead > 0) {
	                dos.write(buffer, 0, bufferSize);
	                bytesAvailable = fin.available();
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                bytesRead = fin.read(buffer, 0, bufferSize);
	            }
	            dos.writeBytes(lineEnd+lineEnd);
	            //--------------------伪造images.jpg信息结束-----------------------------------
//	            Log.d(TAG, "结束上传");
	            
	            // POST Data结束
	            dos.writeBytes("--" + boundary + "--");
	            
	            // Server端返回的信息
	            System.out.println("" + connection.getResponseCode());
	            System.out.println("" + connection.getResponseMessage());
	            
	            if (dos != null) {
	                dos.flush();
	                dos.close();
	            }
//	            Log.d(TAG, "upload success-----------------------------------------");
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
	    }
}
