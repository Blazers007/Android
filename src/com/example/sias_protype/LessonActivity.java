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
	//��Ŵӿα�ҳ���ȡ�ı���ʮ��������
//	private ArrayList<String>lessonsDataList = new ArrayList<String>();
	//�������7��γ̵� Hash ͼ������
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
	private int SorM = 0;//Ĭ������?
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
	//�߸� ListView ����
	private ListView lessonsList1 = null;
	private ListView lessonsList2 = null;
	private ListView lessonsList3 = null;
	private ListView lessonsList4 = null;
	private ListView lessonsList5 = null;
	private ListView lessonsList6 = null;
	private ListView lessonsList7 = null;
//	private List<ListView> lessonAll = new ArrayList<ListView>();
	
	private boolean hasLocalFile;
	
	///////��½����
	private String user = null;
	private String password = null;
	private EditText userEdit = null;
	private EditText pwdEdit = null;
	private CheckBox infoSave = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����Ϊȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.activity_lesson);
		listAllS.add(listDay01DataS);listAllM.add(listDay01DataM); 	listAll.add(listDay01Data);//���Ǹ���̬���� ÿ����������һ������ ������ HASHMAP��ֵ�� һ����һ���
		listAllS.add(listDay02DataS);listAllM.add(listDay02DataM);	listAll.add(listDay02Data);
		listAllS.add(listDay03DataS);listAllM.add(listDay03DataM);	listAll.add(listDay03Data);
		listAllS.add(listDay04DataS);listAllM.add(listDay04DataM);	listAll.add(listDay04Data);
		listAllS.add(listDay05DataS);listAllM.add(listDay05DataM);	listAll.add(listDay05Data);
		listAllS.add(listDay06DataS);listAllM.add(listDay06DataM);	listAll.add(listDay06Data);
		listAllS.add(listDay07DataS);listAllM.add(listDay07DataM);	listAll.add(listDay07Data);
		
		////////////////////////////////////////////////////////////
		
		View view1 = View.inflate(getApplicationContext(),R.layout.tabview,null);
		///01.�˴�������TAB ����װ������xml�����ļ� �ֱ�Ϊ CET 4 6 �Լ� LOL
		TabHost mTabHost = (TabHost)findViewById(R.id.tabhost); 
		mTabHost.setup();
		LayoutInflater inflater_tab1 = LayoutInflater.from(this);   
		inflater_tab1.inflate(R.layout.monday_tab1, mTabHost.getTabContentView());//7����XML�ļ�  
		inflater_tab1.inflate(R.layout.tuesday_tab2, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.wednesday_tab3, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.thursday_tab4, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.friday_tab5, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.saturday_tab6, mTabHost.getTabContentView());
		inflater_tab1.inflate(R.layout.sunday_tab7, mTabHost.getTabContentView());
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("һ").setContent(R.id.monday_layout));   
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("��").setContent(R.id.tuesday_layout));  
		mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("��").setContent(R.id.wednesday_layout));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator("��").setContent(R.id.thursday_layout));   
		mTabHost.addTab(mTabHost.newTabSpec("tab_test5").setIndicator("��").setContent(R.id.friday_layout));  
		mTabHost.addTab(mTabHost.newTabSpec("tab_test6").setIndicator("��").setContent(R.id.saturday_layout));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test7").setIndicator("��").setContent(R.id.sunday_layout));
		System.out.println(mTabHost.getChildCount());
//		mTabHost.setCurrentTab(3);
		new Thread(initActivity).start(); //���ظ��� list �Լ��ӱ��ض�ȡ�ļ���䵽��̬������ȥ
		
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(LessonActivity.this)
		.setView(waitAlertView)
		.create();

		
//////////Ȼ��Ӧ�������������Ϣ�Ļ���ֱ�Ӷ�/////////////
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
				.setTitle("��ϸ��Ϣ")
				.setNegativeButton("�ر�", null)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
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
				.setTitle("��ϸ��Ϣ")
				.setNegativeButton("�ر�", null)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
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
				.setTitle("��ϸ��Ϣ")
				.setNegativeButton("�ر�", null)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
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
				.setTitle("��ϸ��Ϣ")
				.setNegativeButton("�ر�", null)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
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
				.setTitle("��ϸ��Ϣ")
				.setNegativeButton("�ر�", null)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
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
				.setTitle("��ϸ��Ϣ")
				.setNegativeButton("�ر�", null)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
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
				.setTitle("��ϸ��Ϣ")
				.setNegativeButton("�ر�", null)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
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
	
	////����
	public boolean hasLocalLessonInfo(){
		File fileS = new File("/sdcard/SiasPro/Control/Sias_lesson.txt");//�洢APP��Ϣ��·��
		if(fileS.exists()){//������ļ� �Ͷ�ȡ�ļ���Ϣ
			try {
				BufferedReader in = new BufferedReader(new FileReader(fileS));//�������������ݵ��ļ�
				String info = in.readLine();//ȷ���ļ�����һ�У�
				if(info.equals("#")) //��������һ�����ļ� ��ֱ�ӷ��� ˵������ûѡ�񱣴��¼��Ϣ
					return false; //��ôʵ���Ͼ͸�û����һ���� ���Է��� �� ��ô�Ͳ�ȥ����Ϣ���
				else{
					readLocalLesson();//�ӱ��ض�ȡ��Ϣ���� HashMap��ȥ
					return true;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}else{				//���û�� ���ǵ�һ�ε�¼ ���߰汾���£� �ʹ����ռ䲢д#��
			try {
				FileOutputStream fouts = new FileOutputStream(fileS);
				fouts.write("#".getBytes());						 
				fouts.close();				
			} catch (FileNotFoundException e) {
				System.out.println("open file stream failed!");
				e.printStackTrace();
				return false; //�����ʧ����ֱ�ӷ���
			} catch (IOException e) {
				System.out.println("write file stream failed!");
				e.printStackTrace();
				return false; //�����ʧ����ֱ�ӷ���
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
			while((line = in.readLine()) != null){ //�ı���һ�о���һ���ڵ�ĳ�ڿ� 1 3 5 7 9
				if(line.isEmpty())
					break;//��ʱ���������� ��ô��ֱ������
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
	//Handler ���������߳����н����Ķ���
	Handler threadHandler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0://�Ǵ������ȡ�ļ�
//				listAll.clear();
//				listAll.addAll(listAllS); //��ʾ���ݵ�
				for(int index = 0 ; index <7 ; index ++)
					adapterList[index].notifyDataSetChanged();
				Toast.makeText(LessonActivity.this, "���³ɹ�!", 1000).show();
				waitAlert.dismiss();
				break;
			case 1://�ӱ��ض�ȡ�ļ�
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
	///////////////////////////�̲߳���//////////////
	Runnable initActivity = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			////////���濪ʼ���� tab �� listview
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
			//01���Ի�ȡ viewState
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
				//���ó�ʱ
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
			//02��ʼ��½
			String temp = null;
			
			List<Cookie> cookies = null; //����һ����� Cookie ���б�
			HttpClient client = new DefaultHttpClient();//����һ�� HttpClient ����ִ�з���Request
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
			//���ó�ʱ
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000); 
			try {
				// ����HTTP request
				httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				// ȡ��HTTP response
				httpResponse = client.execute(httpRequest);   //ִ��
				// ��״̬��Ϊ200 ok
				if (httpResponse.getStatusLine().getStatusCode() == 200) {   //����ֵ����
					// ��ȡ���ص�cookie
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
			//03��ʼ��ȡ�α�
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
			System.out.println("Cookie header ready!"); //��� Header
			//myRequest.setEntity(new UrlEncodedFormEntity(params_step2, HTTP.UTF_8));//����POST��ʽ��������ʵ��
			//���ó�ʱ
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
			 *   �˴����Լ���һ���жϺ��� �ж��Ƿ�ɹ����� ���Ҳ����д��ALL��ȥ 
			 *   ����϶�Ҫʵ�ֵģ������������ˣ�����
			 */
			//////////////��ʼ���� html
			if(allIsOk){
				System.out.println("starting parsing...");
				temp = temp.replaceAll("<br>", "��");
				Document doc = Jsoup.parse(temp);
				int i = 0;
				for(Element ele:doc.getElementsByClass("blacktab").get(0).select("tr")){
					for(Element  ele1:ele.select("td")){ //ÿ�ڿεľ�������
						sbs[i] += (ele1.select("td").text()+"#");
					}
					i++;
				}
				//��2~11 split ����MAP�� ʵ���Ͼ���  1 3 5 7 �⼸�ڿ���? ������ �����ϻ���������ڿΣ�
				sbs[2]=sbs[2].substring(7);
				sbs[4]=sbs[4].substring(4);
				sbs[6]=sbs[6].substring(7);
				sbs[8]=sbs[8].substring(4);
				sbs[10]=sbs[10].substring(7);
				/*
				 * ע������Ժ���Ҫ��ѯ�����༶�Ŀα�Ļ� ��ô�������Ӧ�ø������ д����߲�д�룡
				 */
				String write = sbs[2]+"\n"+sbs[4]+"\n"+sbs[6]+"\n"+sbs[8]+"\n"+sbs[10];
				System.out.println("starting write to local");
				writeToLocalFile(write);//����д�뱾���ļ���ȥ  
				System.out.println("write over��");
				//�˴�Ӧ�����һ������ո��� List�ĺ��� ������º����������
				for(int i1 = 0 ;i1 < 7; i1 ++){
					listAll.get(i1).clear();
					System.out.println("clear "+i1);
				}
				for(int index = 2; index <=10; index +=2){
					String tempSplit[] = sbs[index].split("#");
					System.out.println("split "+index);
					for(int odex = 0 ; odex < 7 ; odex ++){ //��������Ŀγ̷ֿ�
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
							
							System.out.println("����  "+parsees[0].getName());
							System.out.println("����  "+parsees[0].getTeacher());
							System.out.println("����  "+parsees[0].getTime());
							System.out.println("����  "+parsees[0].getLocation());
							
							System.out.println("˫��  "+parsees[1].getName());
							System.out.println("˫��  "+parsees[1].getTeacher());
							System.out.println("˫��  "+parsees[1].getTime());
							System.out.println("˫��  "+parsees[1].getLocation());
							
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
		private String name = "��";
		private String time = "��";
		private String teacher = "��";
		private String location = "��";
		parseLesson(String lesson){
			int length = lesson.split("��").length;
			switch(length){
			case 0:
				System.out.println("null");
			case 1:
				this.name = lesson.split("��")[0];
				break;
			case 2:
				this.name = lesson.split("��")[0];
				this.time = lesson.split("��")[1];
				break;
			case 3:
				this.name = lesson.split("��")[0];
				this.time = lesson.split("��")[1];
				this.teacher = lesson.split("��")[2];
				break;
			default:
				this.name = lesson.split("��")[0];
				this.time = lesson.split("��")[1];
				this.teacher = lesson.split("��")[2];
				this.location = lesson.split("��")[3];
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
	
	//////////////////////////�˵�����
	
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
			if(allInfo.getLogState()){ //����Ѿ���¼��ֱ�ӽ��оͺ���
				user = allInfo.getUser();
				password = allInfo.getPassword();
				new Thread(updateLessons).start();
				waitAlert.show();
			}else{ //��������Ҫ��¼һ��
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View logView=inflater.inflate(R.layout.login_window, null);
				userEdit = (EditText) logView.findViewById(R.id.log_user);
				pwdEdit = (EditText) logView.findViewById(R.id.log_pwd);
				infoSave = (CheckBox) logView.findViewById(R.id.log_save_or_not);
				infoSave.setVisibility(View.GONE);
				new AlertDialog.Builder(LessonActivity.this)
				.setTitle("��δ��¼,���ȵ�¼��")
				.setView(logView)
				.setNegativeButton("ȡ��", null)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
			if(SorM == 0){ //����˫
				for(int i = 0 ; i < 7 ; i ++){
					listAll.get(i).clear();
					listAll.get(i).addAll(listAllM.get(i));
				}
				for(int index = 0 ; index <7 ; index ++)
					adapterList[index].notifyDataSetChanged();
				Toast.makeText(LessonActivity.this, "��ʾ˫��(��չ)�γ�", 1000).show();
				SorM = 1;
			}else{
				for(int i = 0 ; i < 7 ; i ++){
					listAll.get(i).clear();
					listAll.get(i).addAll(listAllS.get(i));
				}
				for(int index = 0 ; index <7 ; index ++)
					adapterList[index].notifyDataSetChanged();
				Toast.makeText(LessonActivity.this, "��ʾ����(��չ)�γ�", 1000).show();
				SorM = 0;
			}
			break;
		case 3:
			new AlertDialog.Builder(this)
			.setTitle("���ڿγ̱�")
			.setMessage("��Ϊ�γ̱�Ľ���ʵ��������...\nʹ������Щ����ϣ������½�")
			.setPositiveButton("OK", null)
			.show();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	class SuperParse{
		private String toParse;
		private int classTypeS; //0��˫ 1���� 2˫��
		private parseLesson lessonS;
		private parseLesson lessonM;
		private int classTypeM; //0��˫ 1���� 2˫��
		private parseLesson[] lesson;
		public SuperParse(String toParse){
			this.toParse = toParse;
		}
		int parseNum(){
			String dex = "{��";
			int first = toParse.indexOf(dex);//�ҵ���һ��λ��
			String left = toParse.substring(first+2);
			if(left.indexOf(dex) == -1)//���û���� ����һ���γ�
				return 1;
			else
				return 2;
		}
		
		parseLesson[] parse(int num){
			parseLesson[] lessons = new parseLesson[2];
			if(num == 1){ //�����һ�ڿ� ��ô��ʼ���������ݻ���˫��
				if(toParse.indexOf("|����") != -1){ //�ǵ���
					lessonS = new parseLesson(toParse);
					lessonM = new parseLesson("  ��  ��  ��  ��  ��  ");
					lessons[0] = lessonS;
					lessons[1] = lessonM;
					return lessons;
				}else if(toParse.indexOf("|˫��") != -1){
					lessonS = new parseLesson("  ��  ��  ��  ��  ��  ");
					lessonM = new parseLesson(toParse);
					lessons[0] = lessonS;
					lessons[1] = lessonM;
					return lessons;
				}else{ //ÿ��
					lessonS = new parseLesson(toParse);
					lessonM = new parseLesson(toParse);
					lessons[0] = lessonS;
					lessons[1] = lessonM;
					return lessons;
				}
			}else{//����������ڿ� Ȼ��ʼ������˫��  1 �����Ȱ�������ͬ�Ŀγ̷ֿ�
				int first = 0;
				boolean findFirst = false;
				int second = 0;
				String[] sb = toParse.split("��");
				for(int i = 0 ; i < sb.length ; i ++){
					if(sb[i].indexOf("{��") != -1 && !findFirst){
						findFirst = true;
						first = i-1;
					}
					if(sb[i].indexOf("{��") != -1 && findFirst){
						second = i-1;
					}
				}
				
				String firstString = sb[first]+"��"+sb[first+1]+"��"+sb[first+2]+"��"+sb[first+3]+"��"+"  ��";
				String secondString = sb[second]+"��"+sb[second+1]+"��"+sb[second+2]+"��"+sb[second+3]+"��"+"  ��";
				lessonS = new parseLesson(firstString);
				lessonM = new parseLesson(secondString);
				lessons[0] = lessonS;
				lessons[1] = lessonM;
				return lessons;
			}
		}
	}

}
