package com.example.sias_protype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
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
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class LessonActivity extends Activity {
	
	private AlertDialog waitAlert = null;
	private String[] sbs = {"","","","","","","","","","","",""};
	private SimpleAdapter adapterList[] = new SimpleAdapter[7];
	//存放从课表页面获取的表格的十二组数据
//	private ArrayList<String>lessonsDataList = new ArrayList<String>();
	//用来存放7天课程的 Hash 图的数组
	private ArrayList<HashMap<String,String>> listDay01DataS = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay02DataS = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay03DataS = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay04DataS = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay05DataS = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay06DataS = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay07DataS = new ArrayList<HashMap<String,String>>();
	private List<ArrayList> listAllS = new ArrayList<ArrayList>();
	private ArrayList<HashMap<String,String>> listDay01DataM = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay02DataM = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay03DataM = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay04DataM = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay05DataM = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay06DataM = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay07DataM = new ArrayList<HashMap<String,String>>();
	private List<ArrayList> listAllM = new ArrayList<ArrayList>();
	private int SorM = 0;//默认儋州?
	private ArrayList<HashMap<String,String>> listDay01Data = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay02Data = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay03Data = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay04Data = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay05Data = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay06Data = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> listDay07Data = new ArrayList<HashMap<String,String>>();
	private List<ArrayList> listAll = new ArrayList<ArrayList>();
//	private List<List> listAll = new ArrayList<List>();
//	private List[] listss = new List[]{listAllM,listAllS};
	//七个 ListView 对象
	private ListView lessonsList1 = null;
	private ListView lessonsList2 = null;
	private ListView lessonsList3 = null;
	private ListView lessonsList4 = null;
	private ListView lessonsList5 = null;
	private ListView lessonsList6 = null;
	private ListView lessonsList7 = null;
//	private List<ListView> lessonAll = new ArrayList<ListView>();
	
	private boolean hasLocalFile;
	
	///////登陆部分
	private String user = null;
	private String password = null;
	private EditText userEdit = null;
	private EditText pwdEdit = null;
	private CheckBox infoSave = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置为全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.activity_lesson);
		listAllS.add(listDay01DataS);listAllM.add(listDay01DataM); 	listAll.add(listDay01Data);//这是个动态数组 每个对象又是一个数组 来保存 HASHMAP键值对 一个是一天的
		listAllS.add(listDay02DataS);listAllM.add(listDay02DataM);	listAll.add(listDay02Data);
		listAllS.add(listDay03DataS);listAllM.add(listDay03DataM);	listAll.add(listDay03Data);
		listAllS.add(listDay04DataS);listAllM.add(listDay04DataM);	listAll.add(listDay04Data);
		listAllS.add(listDay05DataS);listAllM.add(listDay05DataM);	listAll.add(listDay05Data);
		listAllS.add(listDay06DataS);listAllM.add(listDay06DataM);	listAll.add(listDay06Data);
		listAllS.add(listDay07DataS);listAllM.add(listDay07DataM);	listAll.add(listDay07Data);
		
		////////////////////////////////////////////////////////////
		
		View view1 = View.inflate(getApplicationContext(),R.layout.tabview,null);
		///01.此处是三个TAB 用来装载三个xml布局文件 分别为 CET 4 6 以及 LOL
		TabHost mTabHost = (TabHost)findViewById(R.id.tabhost); 
		mTabHost.setup();
		LayoutInflater inflater_tab1 = LayoutInflater.from(this);   
		inflater_tab1.inflate(R.layout.monday_tab1, mTabHost.getTabContentView());//7个子XML文件  
		inflater_tab1.inflate(R.layout.tuesday_tab2, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.wednesday_tab3, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.thursday_tab4, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.friday_tab5, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.saturday_tab6, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.sunday_tab7, mTabHost.getTabContentView());
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("一").setContent(R.id.monday_layout));   
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("二").setContent(R.id.tuesday_layout));  
		mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("三").setContent(R.id.wednesday_layout));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator("四").setContent(R.id.thursday_layout));   
		mTabHost.addTab(mTabHost.newTabSpec("tab_test5").setIndicator("五").setContent(R.id.friday_layout));  
		mTabHost.addTab(mTabHost.newTabSpec("tab_test6").setIndicator("六").setContent(R.id.saturday_layout));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test7").setIndicator("日").setContent(R.id.sunday_layout));
		System.out.println(mTabHost.getChildCount());
//		mTabHost.setCurrentTab(3);
		new Thread(initActivity).start(); //加载各项 list 以及从本地读取文件填充到动态数组中去
		
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(LessonActivity.this)
		.setView(waitAlertView)
		.create();

		
//////////然后应该如果本地有信息的话则直接读/////////////
	}

	
	public void setListListener(){
		lessonsList1.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.lesson_list_find_classroom, null);
				final TextView roomId = (TextView)view.findViewById(R.id.findRoomId);
				roomId.setText(listDay01Data.get(arg2).get("Location"));
				new AlertDialog.Builder(LessonActivity.this)
				.setView(view)
				.setTitle("详细信息")
				.setNegativeButton("关闭", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String id = roomId.getText().toString();
						Intent intent = new Intent(LessonActivity.this,MapActivity.class);
						intent.putExtra("FindRoomById", id);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
			
		});
		lessonsList2.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.lesson_list_find_classroom, null);
				final TextView roomId = (TextView)view.findViewById(R.id.findRoomId);
				roomId.setText(listDay02Data.get(arg2).get("Location"));
				new AlertDialog.Builder(LessonActivity.this)
				.setView(view)
				.setTitle("详细信息")
				.setNegativeButton("关闭", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String id = roomId.getText().toString();
						Intent intent = new Intent(LessonActivity.this,MapActivity.class);
						intent.putExtra("FindRoomById", id);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
			
		});
		lessonsList3.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.lesson_list_find_classroom, null);
				final TextView roomId = (TextView)view.findViewById(R.id.findRoomId);
				roomId.setText(listDay03Data.get(arg2).get("Location"));
				new AlertDialog.Builder(LessonActivity.this)
				.setView(view)
				.setTitle("详细信息")
				.setNegativeButton("关闭", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String id = roomId.getText().toString();
						Intent intent = new Intent(LessonActivity.this,MapActivity.class);
						intent.putExtra("FindRoomById", id);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
			
		});
		lessonsList4.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.lesson_list_find_classroom, null);
				final TextView roomId = (TextView)view.findViewById(R.id.findRoomId);
				roomId.setText(listDay04Data.get(arg2).get("Location"));
				new AlertDialog.Builder(LessonActivity.this)
				.setView(view)
				.setTitle("详细信息")
				.setNegativeButton("关闭", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String id = roomId.getText().toString();
						Intent intent = new Intent(LessonActivity.this,MapActivity.class);
						intent.putExtra("FindRoomById", id);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
			
		});
		lessonsList5.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.lesson_list_find_classroom, null);
				final TextView roomId = (TextView)view.findViewById(R.id.findRoomId);
				roomId.setText(listDay05Data.get(arg2).get("Location"));
				new AlertDialog.Builder(LessonActivity.this)
				.setView(view)
				.setTitle("详细信息")
				.setNegativeButton("关闭", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String id = roomId.getText().toString();
						Intent intent = new Intent(LessonActivity.this,MapActivity.class);
						intent.putExtra("FindRoomById", id);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
			
		});
		lessonsList6.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.lesson_list_find_classroom, null);
				final TextView roomId = (TextView)view.findViewById(R.id.findRoomId);
				roomId.setText(listDay06Data.get(arg2).get("Location"));
				new AlertDialog.Builder(LessonActivity.this)
				.setView(view)
				.setTitle("详细信息")
				.setNegativeButton("关闭", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String id = roomId.getText().toString();
						Intent intent = new Intent(LessonActivity.this,MapActivity.class);
						intent.putExtra("FindRoomById", id);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
			
		});
		lessonsList7.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.lesson_list_find_classroom, null);
				final TextView roomId = (TextView)view.findViewById(R.id.findRoomId);
				roomId.setText(listDay07Data.get(arg2).get("Location"));
				new AlertDialog.Builder(LessonActivity.this)
				.setView(view)
				.setTitle("详细信息")
				.setNegativeButton("关闭", null)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String id = roomId.getText().toString();
						Intent intent = new Intent(LessonActivity.this,MapActivity.class);
						intent.putExtra("FindRoomById", id);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
			
		});
		
	}
	
	////本地
	public boolean hasLocalLessonInfo(){
		File fileS = new File("/sdcard/SiasPro/Control/Sias_lesson.txt");//存储APP信息的路径
		if(fileS.exists()){//如果有文件 就读取文件信息
			try {
				BufferedReader in = new BufferedReader(new FileReader(fileS));//这里仅仅检测儋州的文件
				String info = in.readLine();//确保文件仅有一行？
				if(info.equals("#")) //若仅仅是一个空文件 则直接返回 说明主人没选择保存登录信息
					return false; //那么实质上就跟没有是一样的 所以返回 假 那么就不去给信息填表
				else{
					readLocalLesson();//从本地读取信息放入 HashMap中去
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
				FileOutputStream fouts = new FileOutputStream(fileS);
				fouts.write("#".getBytes());						 
				fouts.close();				
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
	
	public void readLocalLesson(){
		File file = new File("/sdcard/SiasPro/Control/Sias_lesson.txt");
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			int time = 1;
			while((line = in.readLine()) != null){ //文本中一行就是一星期的某节口 1 3 5 7 9
				if(line.isEmpty())
					break;//有时候会读到空行 那么就直接挑出
				String tempSplit[] = line.split("#");
				for(int odex = 0 ; odex < 7 ; odex ++){
					HashMap<String,String> mapS = new HashMap<String,String>();
					HashMap<String,String> mapM = new HashMap<String,String>();
					if(tempSplit[odex].length()>10){
						SuperParse parse = new SuperParse(tempSplit[odex]); //0danzhou 1 shuangzhou 
						parseLesson parsees[] = parse.parse(parse.parseNum());
						mapS.put("Name", parsees[0].getName());
						mapS.put("Time", parsees[0].getTime());
						mapS.put("Teacher", parsees[0].getTeacher());
						mapS.put("Location", parsees[0].getLocation());
						mapM.put("Name", parsees[1].getName());
						mapM.put("Time", parsees[1].getTime());
						mapM.put("Teacher", parsees[1].getTeacher());
						mapM.put("Location", parsees[1].getLocation());
					}else{
						mapS.put("Name", " ");
						mapS.put("Time", " ");
						mapS.put("Teacher", " ");
						mapS.put("Location", " ");
						mapM.put("Name", " ");
						mapM.put("Time", " ");
						mapM.put("Teacher", " ");
						mapM.put("Location", " ");
					}
					String classTime = Integer.toString(time)+" - "+Integer.toString(time+1);
					mapS.put("ClassTime", classTime);
					mapM.put("ClassTime", classTime);
					listAll.get(odex).add(mapS);
					listAllS.get(odex).add(mapS);
					listAllM.get(odex).add(mapM);
				}
				time += 2;
			}
		} catch (FileNotFoundException e) {
			file.delete();
		} catch (IOException e) {
			file.delete();
			e.printStackTrace();
		}
		
		Message msg = new Message();
		msg.what = 1;
		threadHandler.sendMessage(msg);
	}
	
	public void writeToLocalFile(String write){
		try {
			File file = new File("/sdcard/SiasPro/Control/Sias_lesson.txt");

			FileOutputStream fout = new FileOutputStream(file);

			fout.write(write.getBytes());
			fout.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//////////////////////////////////////////////
	//Handler 用来控制线程运行结束的动作
	Handler threadHandler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0://是从网络读取文件
//				listAll.clear();
//				listAll.addAll(listAllS); //显示儋州的
				for(int index = 0 ; index <7 ; index ++)
					adapterList[index].notifyDataSetChanged();
				Toast.makeText(LessonActivity.this, "更新成功!", 1000).show();
				waitAlert.dismiss();
				break;
			case 1://从本地读取文件
				for(int index = 0 ; index <7 ; index ++)
					adapterList[index].notifyDataSetChanged();
				break;
			case 3:
				Toast.makeText(LessonActivity.this, "failed!", 1000).show();
				break;
			}
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
		
	};
	///////////////////////////线程部分//////////////
	Runnable initActivity = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			////////下面开始配置 tab 的 listview
//			System.out.println("starting band ListView");
			lessonsList1 = (ListView)findViewById(R.id.listViewDay1);
			lessonsList2 = (ListView)findViewById(R.id.listViewDay2);
			lessonsList3 = (ListView)findViewById(R.id.listViewDay3);
			lessonsList4 = (ListView)findViewById(R.id.listViewDay4);
			lessonsList5 = (ListView)findViewById(R.id.listViewDay5);
			lessonsList6 = (ListView)findViewById(R.id.listViewDay6);
			lessonsList7 = (ListView)findViewById(R.id.listViewDay7);
			for(int index = 0 ; index < 7 ; index ++){
				adapterList[index] = new SimpleAdapter (LessonActivity.this,listAll.get(index),
						R.layout.lessons_list,
						new String[] {"Name","Time","Teacher","Location","ClassTime"},
						new int[] {R.id.classListUnit_name,R.id.classListUnit_time,R.id.classListUnit_teacher,R.id.classListUnit_location,R.id.classTimeUnit}
						);
			}
			lessonsList1.setAdapter(adapterList[0]);	
			lessonsList2.setAdapter(adapterList[1]);
			lessonsList3.setAdapter(adapterList[2]);	
			lessonsList4.setAdapter(adapterList[3]);
			lessonsList5.setAdapter(adapterList[4]);
			lessonsList6.setAdapter(adapterList[5]);
			lessonsList7.setAdapter(adapterList[6]);
			
			hasLocalFile = hasLocalLessonInfo();	
			setListListener();
		}
		
	};
	
	Runnable updateLessons = new Runnable(){

		@Override
		public void run() {
			boolean allIsOk = true;
			// TODO Auto-generated method stub
			int msgWhat = 3;
			String viewState = null;
			//01尝试获取 viewState
			try{
				HttpGet getViewState = new HttpGet("http://jwgl.sias.edu.cn");
				getViewState.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				getViewState.setHeader("Accept-Encoding	","gzip, deflate");
				getViewState.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				getViewState.setHeader("Cache-Control","max-age=0");
				getViewState.setHeader("Connection", "keep-alive");
				getViewState.setHeader("DNT","1");
				getViewState.setHeader("Host","218.198.176.91");
				getViewState.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
				HttpClient defaultHttpClient = new DefaultHttpClient();
				//设置超时
				defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
				defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000); 
				HttpResponse gotViewState = defaultHttpClient.execute(getViewState);
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
//					System.out.println(sb.toString());	
				String pattern = "<input type=\"hidden\" name=\"__VIEWSTATE\" value=\"(.*?)\" />";
				Pattern reg = Pattern.compile(pattern);
				Matcher match = reg.matcher(sb.toString());
				while(match.find())
					viewState = match.group(1);
				System.out.println(viewState);
			}catch(Exception ex){
				Toast.makeText(LessonActivity.this, "failed!", 1000).show();
				allIsOk = false;
			}
			//02开始登陆
			String temp = null;
			
			List<Cookie> cookies = null; //建立一个存放 Cookie 的列表
			HttpClient client = new DefaultHttpClient();//声明一个 HttpClient 用于执行访问Request
			HttpResponse httpResponse = null;
			String url = "http://jwgl.sias.edu.cn/default2.aspx";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Button1",""));
			params.add(new BasicNameValuePair("RadioButtonList1","%D1%A7%C9%FA"));
			params.add(new BasicNameValuePair("TextBox1",user));
			params.add(new BasicNameValuePair("TextBox2",password));
			params.add(new BasicNameValuePair("__VIEWSTATE",viewState));
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
			//设置超时
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000); 
			try {
				// 发出HTTP request
				httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				// 取得HTTP response
				httpResponse = client.execute(httpRequest);   //执行
				// 若状态码为200 ok
				if (httpResponse.getStatusLine().getStatusCode() == 200) {   //返回值正常
					// 获取返回的cookie
					cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
					
					System.out.println("Here is the cookie!!");
					
					System.out.println(cookies.get(0).getValue());
				} else {
					Toast.makeText(LessonActivity.this, "failed!", 1000).show();
					allIsOk = false;
					System.out.println("200 failed!!");
				}
			} catch (Exception e) {
				Toast.makeText(LessonActivity.this, "failed!", 1000).show();
				System.out.println("try failed!");
				e.printStackTrace();
			}
			//gb2312
			//03开始获取课表
			String url2 = "http://218.198.176.91/xskbcx.aspx?xh="+user+"&xm=%C1%F5%B2%A9%CE%C4&gnmkdm=N121603";
			HttpGet myRequest = new HttpGet(url2);
			try {
			HttpResponse myResponse = null;
			HttpClient myClient = new DefaultHttpClient();
		//	System.out.println("statring cookie get");
			myRequest.setHeader("Cookie","ASP.NET_SessionId="+cookies.get(0).getValue());
			myRequest.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			myRequest.setHeader("Accept-Encoding	","gzip, deflate");
			myRequest.setHeader("Accept-Language",	"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			myRequest.setHeader("Cache-Control",	"max-age=0");
			myRequest.setHeader("Connection", "keep-alive");
			myRequest.setHeader("DNT","1");
			myRequest.setHeader("Host","218.198.176.91");
			myRequest.setHeader("Referer",	"http://218.198.176.91/xs_main.aspx?xh="+user);
			System.out.println("Cookie header ready!"); //填充 Header
			//myRequest.setEntity(new UrlEncodedFormEntity(params_step2, HTTP.UTF_8));//若是POST方式则填充参数实体
			//设置超时
			myClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
			myClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			System.out.println("Starting execute!");
			myResponse = myClient.execute(myRequest);
			if(myResponse.getStatusLine().getStatusCode() ==200 ){
				System.out.println("state ok!");
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = myResponse.getEntity();
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
				System.out.println(temp);
				}else{
					Toast.makeText(LessonActivity.this, "failed!", 1000).show();
					System.out.println("entering the lessons failed");
					allIsOk = false;
					}
			}catch(Exception e) {  
				e.printStackTrace();  
				Toast.makeText(LessonActivity.this, "failed!", 1000).show();
				System.out.println("trying the lessons failed");
				allIsOk = false;
                }  
			/*
			 *   此处可以加入一个判断函数 判断是否成功连接 如果也可以写到ALL中去 
			 *   这个肯定要实现的！！！！别忘了！！！
			 */
			//////////////开始解析 html
			if(allIsOk){
				System.out.println("starting parsing...");
				temp = temp.replaceAll("<br>", "屌");
				Document doc = Jsoup.parse(temp);
				int i = 0;
				for(Element ele:doc.getElementsByClass("blacktab").get(0).select("tr")){
					for(Element  ele1:ele.select("td")){ //每节课的具体内容
						sbs[i] += (ele1.select("td").text()+"#");
					}
					i++;
				}
				//从2~11 split 放入MAP中 实际上就是  1 3 5 7 这几节课吗? 先试试 理论上还有晚上这节课！
				sbs[2]=sbs[2].substring(7);
				sbs[4]=sbs[4].substring(4);
				sbs[6]=sbs[6].substring(7);
				sbs[8]=sbs[8].substring(4);
				sbs[10]=sbs[10].substring(7);
				/*
				 * 注意如果以后想要查询其他班级的课表的话 那么这个空能应该给与控制 写入或者不写入！
				 */
				String write = sbs[2]+"\n"+sbs[4]+"\n"+sbs[6]+"\n"+sbs[8]+"\n"+sbs[10];
				System.out.println("starting write to local");
				writeToLocalFile(write);//将其写入本地文件中去  
				System.out.println("write over！");
				//此处应该添加一个先清空各个 List的函数 避免更新后出现两个！
				for(int i1 = 0 ;i1 < 7; i1 ++){
					listAll.get(i1).clear();
					System.out.println("clear "+i1);
				}
				for(int index = 2; index <=10; index +=2){
					String tempSplit[] = sbs[index].split("#");
					System.out.println("split "+index);
					for(int odex = 0 ; odex < 7 ; odex ++){ //按照七天的课程分开
						HashMap<String,String> mapS = new HashMap<String,String>();
						HashMap<String,String> mapM = new HashMap<String,String>();
						if(tempSplit[odex].length()>8){
							System.out.println("pass if !");
							System.out.println(tempSplit[odex]);
							SuperParse parse = new SuperParse(tempSplit[odex]); //0danzhou 1 shuangzhou 
							System.out.println(parse.parseNum());
							parseLesson parsees[] = parse.parse(parse.parseNum());
							mapS.put("Name", parsees[0].getName());
							mapS.put("Time", parsees[0].getTime());
							mapS.put("Teacher", parsees[0].getTeacher());
							mapS.put("Location", parsees[0].getLocation());
							
							System.out.println("单周  "+parsees[0].getName());
							System.out.println("单周  "+parsees[0].getTeacher());
							System.out.println("单周  "+parsees[0].getTime());
							System.out.println("单周  "+parsees[0].getLocation());
							
							System.out.println("双周  "+parsees[1].getName());
							System.out.println("双周  "+parsees[1].getTeacher());
							System.out.println("双周  "+parsees[1].getTime());
							System.out.println("双周  "+parsees[1].getLocation());
							
							mapM.put("Name", parsees[1].getName());
							mapM.put("Time", parsees[1].getTime());
							mapM.put("Teacher", parsees[1].getTeacher());
							mapM.put("Location", parsees[1].getLocation());
						}else{
							mapS.put("Name", " ");
							mapS.put("Time", " ");
							mapS.put("Teacher", " ");
							mapS.put("Location", " ");
							mapM.put("Name", " ");
							mapM.put("Time", " ");
							mapM.put("Teacher", " ");
							mapM.put("Location", " ");
						}
						String classTime = Integer.toString(index-1)+" - "+Integer.toString(index);
						mapS.put("ClassTime", classTime);
						mapM.put("ClassTime", classTime);
						listAll.get(odex).add(mapS);
						listAllS.get(odex).add(mapS);
						listAllM.get(odex).add(mapM);
					}
					System.out.println(sbs[index]);
					msgWhat = 0;
				}
			}else{
				msgWhat = 3;
			}
			Message msg = new Message();
			threadHandler.sendMessage(msg);
		}		
	};
	
	class parseLesson{
		private String name = "无";
		private String time = "无";
		private String teacher = "无";
		private String location = "无";
		parseLesson(String lesson){
			int length = lesson.split("屌").length;
			switch(length){
			case 0:
				System.out.println("null");
			case 1:
				this.name = lesson.split("屌")[0];
				break;
			case 2:
				this.name = lesson.split("屌")[0];
				this.time = lesson.split("屌")[1];
				break;
			case 3:
				this.name = lesson.split("屌")[0];
				this.time = lesson.split("屌")[1];
				this.teacher = lesson.split("屌")[2];
				break;
			default:
				this.name = lesson.split("屌")[0];
				this.time = lesson.split("屌")[1];
				this.teacher = lesson.split("屌")[2];
				this.location = lesson.split("屌")[3];
			}
		}
		public String getName() {
			return name;
		}

		public String getTime() {
			return time;
		}

		public String getTeacher() {
			return teacher;
		}

		public String getLocation() {
			return location;
		}
		
	}
	
	//////////////////////////菜单区域
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.lesson, menu);
		menu.add(0, 1, 1, R.string.update);
		menu.add(0,2,2,R.string.change);
		menu.add(0,3,3,R.string.about);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			AllInfomation allInfo = ((AllInfomation)getApplication());
			if(allInfo.getLogState()){ //如果已经登录就直接进行就好了
				user = allInfo.getUser();
				password = allInfo.getPassword();
				new Thread(updateLessons).start();
				waitAlert.show();
			}else{ //否则则需要登录一下
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View logView=inflater.inflate(R.layout.login_window, null);
				userEdit = (EditText) logView.findViewById(R.id.log_user);
				pwdEdit = (EditText) logView.findViewById(R.id.log_pwd);
				infoSave = (CheckBox) logView.findViewById(R.id.log_save_or_not);
				infoSave.setVisibility(View.GONE);
				new AlertDialog.Builder(LessonActivity.this)
				.setTitle("尚未登录,请先登录！")
				.setView(logView)
				.setNegativeButton("取消", null)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						user = userEdit.getText().toString();
						password = pwdEdit.getText().toString();
						new Thread(updateLessons).start();
						waitAlert.show();
					}
				})
				.create()
				.show();
			}
			break;
		case 2:
			if(SorM == 0){ //单边双
				for(int i = 0 ; i < 7 ; i ++){
					listAll.get(i).clear();
					listAll.get(i).addAll(listAllM.get(i));
				}
				for(int index = 0 ; index <7 ; index ++)
					adapterList[index].notifyDataSetChanged();
				Toast.makeText(LessonActivity.this, "显示双周(拓展)课程", 1000).show();
				SorM = 1;
			}else{
				for(int i = 0 ; i < 7 ; i ++){
					listAll.get(i).clear();
					listAll.get(i).addAll(listAllS.get(i));
				}
				for(int index = 0 ; index <7 ; index ++)
					adapterList[index].notifyDataSetChanged();
				Toast.makeText(LessonActivity.this, "显示单周(拓展)课程", 1000).show();
				SorM = 0;
			}
			break;
		case 3:
			new AlertDialog.Builder(this)
			.setTitle("关于课程表")
			.setMessage("因为课程表的解析实在是略难...\n使用上有些不便希望大家谅解")
			.setPositiveButton("OK", null)
			.show();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	class SuperParse{
		private String toParse;
		private int classTypeS; //0单双 1儋州 2双周
		private parseLesson lessonS;
		private parseLesson lessonM;
		private int classTypeM; //0单双 1儋州 2双周
		private parseLesson[] lesson;
		public SuperParse(String toParse){
			this.toParse = toParse;
		}
		int parseNum(){
			String dex = "{第";
			int first = toParse.indexOf(dex);//找到第一个位置
			String left = toParse.substring(first+2);
			if(left.indexOf(dex) == -1)//如果没有了 就是一个课程
				return 1;
			else
				return 2;
		}
		
		parseLesson[] parse(int num){
			parseLesson[] lessons = new parseLesson[2];
			if(num == 1){ //如果就一节课 那么开始分析是儋州还是双周
				if(toParse.indexOf("|单周") != -1){ //是单周
					lessonS = new parseLesson(toParse);
					lessonM = new parseLesson("  屌  屌  屌  屌  屌  ");
					lessons[0] = lessonS;
					lessons[1] = lessonM;
					return lessons;
				}else if(toParse.indexOf("|双周") != -1){
					lessonS = new parseLesson("  屌  屌  屌  屌  屌  ");
					lessonM = new parseLesson(toParse);
					lessons[0] = lessonS;
					lessons[1] = lessonM;
					return lessons;
				}else{ //每周
					lessonS = new parseLesson(toParse);
					lessonM = new parseLesson(toParse);
					lessons[0] = lessonS;
					lessons[1] = lessonM;
					return lessons;
				}
			}else{//否则就是两节课 然后开始分析单双周  1 首先先把两个不同的课程分开
				int first = 0;
				boolean findFirst = false;
				int second = 0;
				String[] sb = toParse.split("屌");
				for(int i = 0 ; i < sb.length ; i ++){
					if(sb[i].indexOf("{第") != -1 && !findFirst){
						findFirst = true;
						first = i-1;
					}
					if(sb[i].indexOf("{第") != -1 && findFirst){
						second = i-1;
					}
				}
				
				String firstString = sb[first]+"屌"+sb[first+1]+"屌"+sb[first+2]+"屌"+sb[first+3]+"屌"+"  屌";
				String secondString = sb[second]+"屌"+sb[second+1]+"屌"+sb[second+2]+"屌"+sb[second+3]+"屌"+"  屌";
				lessonS = new parseLesson(firstString);
				lessonM = new parseLesson(secondString);
				lessons[0] = lessonS;
				lessons[1] = lessonM;
				return lessons;
			}
		}
	}

}
