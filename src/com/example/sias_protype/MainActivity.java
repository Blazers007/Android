package com.example.sias_protype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int reSendACKTimes = 0;
	private AlertDialog waitAlert = null;
	private boolean log_state = false;//保存登陆状态 应该在 onCreate 里面读取参数文件来初始化
	private boolean has_local_log;
	private Button log_button = null;//用来改变Button字
	private TextView info_bar = null;//用来展示信息条
	private String date = null;//存储日期
	//登录相关模块
	private String user = null;
	private String password = null;
	private EditText userEdit = null;
	private EditText pwdEdit = null;
	private CheckBox infoSave = null;
	////////////测试登陆信息模块
	private boolean hasRegistered = false;
	private String info_username = "";
	///test gallery
	private Gallery my_gallery = null;
	private Integer [] mps = {R.raw.p1,R.raw.p2,R.raw.p3,R.raw.p4,R.raw.p5,};//gallery 的图片参数
	private int msgNumbers = 0;
	//读取参数的时候应该设置内容
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置为全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
				
		setContentView(R.layout.activity_main);
		//测试用 
		info_bar = (TextView)findViewById(R.id.infoBar);
		log_button = (Button)findViewById(R.id.loginOrOutButton);
		my_gallery = (Gallery)findViewById(R.id.mygallery);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
	    date=sdf.format(new java.util.Date());//获取日期
	    //检测是否存在本地文件 有的话就存入String中
	    has_local_log = hasLocalLogInfo();
		/////////test gallery
		my_gallery.setAdapter(new ImageAdapter(this));
		my_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,int position, long id) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			//这里不做响应
			}
		});
		//设置一下等待窗口
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(MainActivity.this)
		.setView(waitAlertView)
		.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	//00.这个是程序启动时候应该处理的一系列函数 比如读取参数 检测更新 检测网络等等等
	//	
	//	
	//	
	//	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////////////////////////////////////////////////////////
	//01.这个是登陆或者登出按钮的响应函数
	//		此函数先判断登陆状态然后响应不同的方法
	//		
	///////////////////////////////////////////////////////////////////////////////////////
	
	public void loginOrLogout(View view){
		//0.获取几个按钮的对象
		
		//1.先进行登陆状态的判断
		//1.5.无论登陆还是登出 完毕后都应该改变 log_state 值 以及更新 info_Bar
		if(log_state == true){
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("确定要登出吗?")
			.setIcon(R.drawable.ic_launcher)
			.setNegativeButton("算了", null)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//1检测输入框是否正确
					//2发送登陆消息
					//3返回登陆成功以及信息
					  //-3否则提示登陆失败 然后弹框并退出登陆框
					//保存 viewstate ？以及 cookie 并且设置一个全局变量来保存登陆状态
					//其他的Activity也可以看到登陆状态
					//但是如果在其他ACT中使用COOKIES登陆失败则重新再其他的ACT中登陆并改变全局的COOKIE
					log_state = false;//退出！ 询问是否保存退出信息？改变本地参数文件?
					log_button.setText("登陆");
					//!!当然这里要设置一个等待线程以及与服务器用户密码核对的函数!!
					//info_bar.setText("Today is: "+date+"You have logged out!");
					info_bar.setClickable(false);
					info_bar.setText("尚未登录，请点击登陆!");
					Toast.makeText(MainActivity.this, "You have logged out!", Toast.LENGTH_SHORT).show();
				}
			})
			.create()
			.show();
		}else{
			LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			View logView=inflater.inflate(R.layout.login_window, null);
			userEdit = (EditText) logView.findViewById(R.id.log_user);
			pwdEdit = (EditText) logView.findViewById(R.id.log_pwd);
			infoSave = (CheckBox) logView.findViewById(R.id.log_save_or_not);
			//2.实例化一个view 对象用来载入 xml 文件
			if(has_local_log){//如果本地存在信息 那么填入列表中
				userEdit.setText(user);
				pwdEdit.setText(password);
			}
			new AlertDialog.Builder(MainActivity.this)  
			.setTitle("Welcome to log in my app!")  
			//.setIcon(R.drawable.ic_launcher)  
			.setView(logView)
			.setNegativeButton("取消", null)
			.setPositiveButton("登陆", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					user = userEdit.getText().toString();
					password = pwdEdit.getText().toString();
					new Thread(mainLogInThread).start(); //开启登陆线程
					waitAlert.show();
				}
				
			})
			.create()
			.show();
		}
	}
	////////////////////////////////////////////////////////////////////////////
	//1.5. 这是读取本地登录信息参数的函数 
	////////////////////////////////////////////////////////////////////////////
	boolean hasLocalLogInfo(){
		File file = new File("/sdcard/Sias_log.txt");//存储APP信息的路径
		if(file.exists()){//如果有文件 就读取文件信息
			try {
				BufferedReader in = new BufferedReader(new FileReader(file));
				String info = in.readLine();//确保文件仅有一行？
				if(info.equals("#")) //若仅仅是一个空文件 则直接返回 说明主人没选择保存登录信息
					return false; //那么实质上就跟没有是一样的 所以返回 假 那么就不去给信息填表
				else{
					String[] infos = new String[3];//用户密码昵称？ user#pwd#name
					infos = info.split("#");
					user = infos[0];
					password = infos[1];
					return true;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}else{				//如果没有 则是第一次登录 或者版本更新？ 就创建空间并写#？
			try {
				FileOutputStream fout = new FileOutputStream(file);
				fout.write("#".getBytes());
				fout.close();
			} catch (FileNotFoundException e) {
				System.out.println("open file stream failed!");
				e.printStackTrace();
				return false; //如果打开失败则直接返回
			} catch (IOException e) {
				System.out.println("write file stream failed!");
				e.printStackTrace();
				return false; //如果打开失败则直接返回
			}
			return false;
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////
	//02.这是第二部 按钮的转入部分 分别打开四个其他的ACTIVITY！！！
	//   有四个函数分别绑定四个ImageButton 用来开启 Activity
	//   分别为 startMapActivity
	//		   startLessonActivity
	//		   startMatchActivity
	//		   startQuestActivity
	//////////////////////////////////////////////////////////////////////////////////////////
		public void startMapActivity(View view){
			Intent intent = new Intent(this,MapActivity.class);
//			Intent intent = new Intent(this,FriendActivity.class);
//			Intent intent = new Intent(this,RegisterActivity.class);
			//在此加入需要传入的参数 如上次查询的地图信息 等等
			//1 开启MAP act 的时候从本地读取上次的查询信息 然后传出 startIntent
			startActivity(intent); 
		}
		
		public void startLessonActivity(View view){
			Intent intent = new Intent(this,LessonActivity.class);
			//在此加入需要传入的参数 比如 上次查询的页面 周数 等等
			startActivity(intent);
		}

		public void startMatchActivity(View view){
			Intent intent = new Intent(this,MatchActivity.class);
			//在此加入需要传入的参数 如比一比的缓存信息  等等
			startActivity(intent); 
		}
		
		public void startQuestActivity(View view){
			Intent intent = new Intent(this,QuestActivity.class);
			//在此加入需要传入的参数 比如 上次查讯的成就页面 成就点数更新？ 周数 等等
			startActivity(intent);
		}
		
		public void profileActivity(View view){
			if(hasRegistered){//注册过
				Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
				intent.putExtra("ID", user);
				startActivity(intent);
			}else{//未注册过
				Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
				intent.putExtra("ID", user);
				startActivityForResult(intent,0);//打开 注册页面并申请返回注册的结果
			}
		}
		
		public void msgActivity(View view){
			Intent intent = new Intent(MainActivity.this,MessageActivity.class);
			if(msgNumbers != 0){//若有消息
				intent.putExtra("Read","true");
				startActivity(intent);
			}else{
				intent.putExtra("Read","false");
				startActivity(intent);
			}
		}
		
		@Override  
		protected void onActivityResult(int requestCode, int resultCode, Intent back){  //检测返回信息
		 //可以根据多个请求代码来作相应的操作
			switch(resultCode){
			case 1:
				if(back.getStringExtra("Result").equals("Success")){ //20代表从注册返回de intent
					info_bar.setClickable(true);
					info_bar.setText("欢迎使用!!");
				}
				break;
			}
			super.onActivityResult(requestCode, resultCode, back);  
		}  


		/////////////test gallery
		public class ImageAdapter extends BaseAdapter {
			private Context mContext;
				public ImageAdapter(Context context) {
				mContext = context;
			}

			public int getCount() { 
				return mps.length;
			}

			public Object getItem(int position) {
				return position;
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView image = new ImageView(mContext);
				image.setImageResource(mps[position]);
				image.setAdjustViewBounds(true);
				image.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				return image;
			}
		}
		/////////////////////////////////////////////////////
		//04. handler 函数控制线程结束后的相应
		////////////////////////////////////////////////////
		Handler threadHandler = new Handler(){

			/* (non-Javadoc)
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0://说明登录失败！
					info_bar.setText("登录失败,请重新登录或检查网络!");//设置信息条文字
					waitAlert.dismiss();
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("登录失败！")
					.setIcon(R.drawable.ic_launcher)
					.setMessage("登录失败，请检查网络以及学号或密码是否错误！")
					.setNegativeButton("确定", null)
					.create()
					.show();
					break;
				case 1://说明登陆成功
					info_bar.setText("登陆成功！正在查询服务器账号信息...");//改变文字 然后开启新线程查询服务器
					new Thread(queryHasReg).start();
					waitAlert.dismiss();
					log_button.setText("退出");
					log_state = true;
					if(infoSave.isChecked()){//如果保存信息被选中 则写入信息中
						try {
							File file = new File("/sdcard/Sias_log.txt");
							FileOutputStream fout = new FileOutputStream(file);
							String aboutToWrite = user+"#"+password+"#"+"Blazers";
							fout.write(aboutToWrite.getBytes());
							fout.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}//没有则不写入
					Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
					break;
				case 2: //未注册用户！！
					info_bar.setText("您尚未注册，请点击此处进行注册.");
					info_bar.setClickable(true);
					hasRegistered = false;
					break;
				case 3://注册过  则查询邮件信息
					new Thread(queryMsg).start();
					AllInfomation allInfo = (AllInfomation)getApplication();
					allInfo.setRegState(true);
					allInfo.setWelComeName(info_username);
					info_bar.setText("欢迎 "+info_username+"正在检测邮箱...");
//					info_bar.setClickable(true);
					hasRegistered = true;
					break;
					///处理信息线程
				case 13://没有信息
					info_bar.setText("欢迎 "+info_username+"  没有新的邮件...");
					info_bar.setClickable(true);
					Toast.makeText(MainActivity.this, "您没有新的信息！注定一辈子Diao si...", 2000).show();
					TextView msgView = (TextView)findViewById(R.id.Main_MsgView);
					msgView.setText(Integer.toString(msgNumbers));
					msgView.setClickable(true);
					break;
				case 14://failed
					info_bar.setText("欢迎 "+info_username+" 连接邮件服务器失败..");
					info_bar.setClickable(true);
					Toast.makeText(MainActivity.this, "连接服务器失败！真相就是！不是你网有问题就是我们服务器挂了...", 2000).show();
					break;
				case 15://query Success!
					info_bar.setText("欢迎 "+info_username+" 您有邮件:");
					info_bar.setClickable(true);
					Toast.makeText(MainActivity.this, "信息列表更新成功！", 2000).show();
					TextView msgView1 = (TextView)findViewById(R.id.Main_MsgView);
					msgView1.setText(Integer.toString(msgNumbers));
					msgView1.setClickable(true);
					new Thread(sendACK).start();
					break;
					
				case 17://ack failed!
					reSendACKTimes ++;
					if(reSendACKTimes > 10){
						Toast.makeText(MainActivity.this, "发送ACK失败超过10次", 2000);
						reSendACKTimes = 0;
					}
					else
						new Thread(sendACK).start();//重复发送..
					break;
				case 18 ://ack success!
					Toast.makeText(MainActivity.this, "Send ACK Success!", 2000).show();
					break;
				}

				super.handleMessage(msg);
			}
			
		};
		///////////////上面是Gallery 的测试内容 回头要添加函数
		///05下面是登陆的线程
		Runnable mainLogInThread = new Runnable(){

			@Override
			public void run() {
				if(user.equals("123456")&&password.equals("admin")){
					int msgWhat = 1;
					Message msg = new Message();
					msg.what = msgWhat; //然后存储cookie VIEWSTATE仅仅用来获取 cookie 如果cookie获取则以后不用再获取viewstate
					AllInfomation allInfo = ((AllInfomation)getApplication());
					allInfo.setUser(user);
					allInfo.setPassword(password);
					allInfo.setLogState(true); //设置全局变量！
					threadHandler.sendMessage(msg);
				}else{
					//首先要获取__viewstate
					System.out.println("Start log");
					String viewState = null;
					boolean logSuccess = false;
					int msgWhat = 1;
					try{
						System.out.println("start try");
						HttpGet getViewState = new HttpGet("http://jwgl.sias.edu.cn");
						getViewState.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
						getViewState.setHeader("Accept-Encoding	","gzip, deflate");
						getViewState.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
						getViewState.setHeader("Cache-Control","max-age=0");
						getViewState.setHeader("Connection", "keep-alive");
						getViewState.setHeader("DNT","1");
						getViewState.setHeader("Host","218.198.176.91");
						getViewState.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
						HttpResponse gotViewState = new DefaultHttpClient().execute(getViewState);
						HttpEntity viewEntity = gotViewState.getEntity();
						InputStream viewIs = viewEntity.getContent();
						StringBuilder sb = new StringBuilder();
						while(true){
							byte[] buffer = new byte[102400];
							int len = viewIs.read(buffer);
							if(len == -1)
								break;
							sb.append((new String(buffer,0,len,"GB2312")));
						}
//						System.out.println(sb.toString());
						String pattern = "<input type=\"hidden\" name=\"__VIEWSTATE\" value=\"(.*?)\" />";
						Pattern reg = Pattern.compile(pattern);
						Matcher match = reg.matcher(sb.toString());
						while(match.find())
							viewState = match.group(1);
						System.out.println(viewState);
					}catch(Exception ex){
						msgWhat = 0;
						// add throw out alert!
					}
//						System.out.println(viewState);
					String temp = null;//用来保存返回的信息！！！
					List<Cookie> cookies = null; //建立一个存放 Cookie 的列表
					HttpClient client = new DefaultHttpClient();//声明一个 HttpClient 用于执行访问Request
					HttpResponse httpResponse = null;
					String url = "http://jwgl.sias.edu.cn/default2.aspx";
					HttpPost httpRequest = new HttpPost(url);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("Button1",""));
					params.add(new BasicNameValuePair("RadioButtonList1","%D1%A7%C9%FA"));
					params.add(new BasicNameValuePair("TextBox1",user));//设置账户
					params.add(new BasicNameValuePair("TextBox2",password));//设置密码
					params.add(new BasicNameValuePair("__VIEWSTATE",viewState));//填入获取的VIEWSTATE
					params.add(new BasicNameValuePair("lbLanguage",""));
					httpRequest.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
					httpRequest.setHeader("Accept-Encoding	","gzip, deflate");
					httpRequest.setHeader("Accept-Language",	"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
					httpRequest.setHeader("Cache-Control",	"max-age=0");
					httpRequest.setHeader("Connection", "keep-alive");
					httpRequest.setHeader("DNT","1");
					httpRequest.setHeader("Host","218.198.176.91");
					httpRequest.setHeader("Referer","http://218.198.176.91/");
					httpRequest.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
					try {
						// 发出HTTP request
						httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						// 取得HTTP response
						httpResponse = client.execute(httpRequest);   //执行
						// 若状态码为200 ok
						if (httpResponse.getStatusLine().getStatusCode() == 200) {   //返回值正常 不能判断是否成功登陆！
							// 获取返回的cookie
							cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
							//获取返回的信息 并进行简单解析
							StringBuffer sb = new StringBuffer();
							HttpEntity entity = httpResponse.getEntity();
							InputStream is = entity.getContent();
							while(true){
								byte[] buffer = new byte[102400];
								int len = is.read(buffer);
								if(len == -1)
									break;
								sb.append(new String(buffer,0,len,"GB2312"));
								System.out.println("downloading!!");
								}
							temp = sb.toString();
//							System.out.println(temp);
							//下面来解析temp看是否登陆成功！
							String pattern = "<span id=\"Label3\">(.*?)</span>";
							Pattern reg = Pattern.compile(pattern);
							Matcher match = reg.matcher(temp);
							while(match.find()){
								logSuccess = true;
								System.out.println(match.group(1));
							}
							
						} else {//若状态不是200 则访问失败
							//访问失败
							msgWhat = 0;
						}
					} catch (Exception e) {
						e.printStackTrace();
						msgWhat = 0;
					}
					//判断是否获取到httpResponse 的结果来判断是否登陆成功！！！
					if(!logSuccess){
						Message msg = new Message();
						msg.what = 0;//0表示没有成功登陆 但是也有cookie!!!
						threadHandler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what = msgWhat; //然后存储cookie VIEWSTATE仅仅用来获取 cookie 如果cookie获取则以后不用再获取viewstate
						AllInfomation allInfo = ((AllInfomation)getApplication());
						allInfo.setUser(user);
						allInfo.setPassword(password);
						allInfo.setCookies(cookies);
						allInfo.setLogState(true); //设置全局变量！
						threadHandler.sendMessage(msg);
					}
				}
			}
		};
		
		/////////////////////////////////////////////////////
		//06查询是否存在账户信息
		Runnable queryHasReg = new Runnable(){
			@Override
			public void run() {
				int msgWhat = 3;//3表示查询成功已经注册
				HttpClient hc = new DefaultHttpClient();
				HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("type","QueryUserInfo"));
				params.add(new BasicNameValuePair("ID",user));
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
						if(json.getString("Name").equals("sb"))
							msgWhat = 2;//表示查询失败 未注册
						else
							info_username = json.getString("Name");
					} else {
						System.out.println("connect failed!");
						msgWhat = 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgWhat = 0;
				}
				Message msg = new Message();
				msg.what = msgWhat;
				threadHandler.sendMessage(msg);
			}	
		};
		
		//查询邮箱
		Runnable queryMsg = new Runnable(){  //15 failed 14

			@Override
			public void run() {
				int msgWhat = 15;
				HttpClient hc = new DefaultHttpClient();
				HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("type","QueryMsg")); //类型 注册信息
				params.add(new BasicNameValuePair("ID",user));//送入信息 有没有最大限制？？？？
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
						ArrayList<HashMap<String,String>> message = new ArrayList<HashMap<String,String>>();
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
								msgNumbers ++;// 消息数量增加
								HashMap<String,String> map = new HashMap<String,String>();
								System.out.println(jsonArray.getJSONObject(i).getString("Msg"));
								map.put("Date", jsonArray.getJSONObject(i).getString("Date"));
								map.put("From", jsonArray.getJSONObject(i).getString("From"));
								map.put("Msg", jsonArray.getJSONObject(i).getString("Msg"));
//								map.put("Date", jsonArray.getJSONObject(i).getString("Date"));
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
		
		//发回确认信息 表示收到 服务器删除信息
				Runnable sendACK = new Runnable(){  //17 失败 18

					@Override
					public void run() {
						int msgWhat = 18;
						HttpClient hc = new DefaultHttpClient();
						HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("type","ACKMsg")); //类型 注册信息
						params.add(new BasicNameValuePair("ID",user));//送入信息 有没有最大限制？？？？
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
}
