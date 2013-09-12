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
	private boolean log_state = false;//�����½״̬ Ӧ���� onCreate �����ȡ�����ļ�����ʼ��
	private boolean has_local_log;
	private Button log_button = null;//�����ı�Button��
	private TextView info_bar = null;//����չʾ��Ϣ��
	private String date = null;//�洢����
	//��¼���ģ��
	private String user = null;
	private String password = null;
	private EditText userEdit = null;
	private EditText pwdEdit = null;
	private CheckBox infoSave = null;
	////////////���Ե�½��Ϣģ��
	private boolean hasRegistered = false;
	private String info_username = "";
	///test gallery
	private Gallery my_gallery = null;
	private Integer [] mps = {R.raw.p1,R.raw.p2,R.raw.p3,R.raw.p4,R.raw.p5,};//gallery ��ͼƬ����
	private int msgNumbers = 0;
	//��ȡ������ʱ��Ӧ����������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����Ϊȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
				
		setContentView(R.layout.activity_main);
		//������ 
		info_bar = (TextView)findViewById(R.id.infoBar);
		log_button = (Button)findViewById(R.id.loginOrOutButton);
		my_gallery = (Gallery)findViewById(R.id.mygallery);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
	    date=sdf.format(new java.util.Date());//��ȡ����
	    //����Ƿ���ڱ����ļ� �еĻ��ʹ���String��
	    has_local_log = hasLocalLogInfo();
		/////////test gallery
		my_gallery.setAdapter(new ImageAdapter(this));
		my_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,int position, long id) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			//���ﲻ����Ӧ
			}
		});
		//����һ�µȴ�����
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
	//00.����ǳ�������ʱ��Ӧ�ô����һϵ�к��� �����ȡ���� ������ �������ȵȵ�
	//	
	//	
	//	
	//	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////////////////////////////////////////////////////////
	//01.����ǵ�½���ߵǳ���ť����Ӧ����
	//		�˺������жϵ�½״̬Ȼ����Ӧ��ͬ�ķ���
	//		
	///////////////////////////////////////////////////////////////////////////////////////
	
	public void loginOrLogout(View view){
		//0.��ȡ������ť�Ķ���
		
		//1.�Ƚ��е�½״̬���ж�
		//1.5.���۵�½���ǵǳ� ��Ϻ�Ӧ�øı� log_state ֵ �Լ����� info_Bar
		if(log_state == true){
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("ȷ��Ҫ�ǳ���?")
			.setIcon(R.drawable.ic_launcher)
			.setNegativeButton("����", null)
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//1���������Ƿ���ȷ
					//2���͵�½��Ϣ
					//3���ص�½�ɹ��Լ���Ϣ
					  //-3������ʾ��½ʧ�� Ȼ�󵯿��˳���½��
					//���� viewstate ���Լ� cookie ��������һ��ȫ�ֱ����������½״̬
					//������ActivityҲ���Կ�����½״̬
					//�������������ACT��ʹ��COOKIES��½ʧ����������������ACT�е�½���ı�ȫ�ֵ�COOKIE
					log_state = false;//�˳��� ѯ���Ƿ񱣴��˳���Ϣ���ı䱾�ز����ļ�?
					log_button.setText("��½");
					//!!��Ȼ����Ҫ����һ���ȴ��߳��Լ���������û�����˶Եĺ���!!
					//info_bar.setText("Today is: "+date+"You have logged out!");
					info_bar.setClickable(false);
					info_bar.setText("��δ��¼��������½!");
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
			//2.ʵ����һ��view ������������ xml �ļ�
			if(has_local_log){//������ش�����Ϣ ��ô�����б���
				userEdit.setText(user);
				pwdEdit.setText(password);
			}
			new AlertDialog.Builder(MainActivity.this)  
			.setTitle("Welcome to log in my app!")  
			//.setIcon(R.drawable.ic_launcher)  
			.setView(logView)
			.setNegativeButton("ȡ��", null)
			.setPositiveButton("��½", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					user = userEdit.getText().toString();
					password = pwdEdit.getText().toString();
					new Thread(mainLogInThread).start(); //������½�߳�
					waitAlert.show();
				}
				
			})
			.create()
			.show();
		}
	}
	////////////////////////////////////////////////////////////////////////////
	//1.5. ���Ƕ�ȡ���ص�¼��Ϣ�����ĺ��� 
	////////////////////////////////////////////////////////////////////////////
	boolean hasLocalLogInfo(){
		File file = new File("/sdcard/Sias_log.txt");//�洢APP��Ϣ��·��
		if(file.exists()){//������ļ� �Ͷ�ȡ�ļ���Ϣ
			try {
				BufferedReader in = new BufferedReader(new FileReader(file));
				String info = in.readLine();//ȷ���ļ�����һ�У�
				if(info.equals("#")) //��������һ�����ļ� ��ֱ�ӷ��� ˵������ûѡ�񱣴��¼��Ϣ
					return false; //��ôʵ���Ͼ͸�û����һ���� ���Է��� �� ��ô�Ͳ�ȥ����Ϣ���
				else{
					String[] infos = new String[3];//�û������ǳƣ� user#pwd#name
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
		}else{				//���û�� ���ǵ�һ�ε�¼ ���߰汾���£� �ʹ����ռ䲢д#��
			try {
				FileOutputStream fout = new FileOutputStream(file);
				fout.write("#".getBytes());
				fout.close();
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
	///////////////////////////////////////////////////////////////////////////////////////////
	//02.���ǵڶ��� ��ť��ת�벿�� �ֱ���ĸ�������ACTIVITY������
	//   ���ĸ������ֱ���ĸ�ImageButton �������� Activity
	//   �ֱ�Ϊ startMapActivity
	//		   startLessonActivity
	//		   startMatchActivity
	//		   startQuestActivity
	//////////////////////////////////////////////////////////////////////////////////////////
		public void startMapActivity(View view){
			Intent intent = new Intent(this,MapActivity.class);
//			Intent intent = new Intent(this,FriendActivity.class);
//			Intent intent = new Intent(this,RegisterActivity.class);
			//�ڴ˼�����Ҫ����Ĳ��� ���ϴβ�ѯ�ĵ�ͼ��Ϣ �ȵ�
			//1 ����MAP act ��ʱ��ӱ��ض�ȡ�ϴεĲ�ѯ��Ϣ Ȼ�󴫳� startIntent
			startActivity(intent); 
		}
		
		public void startLessonActivity(View view){
			Intent intent = new Intent(this,LessonActivity.class);
			//�ڴ˼�����Ҫ����Ĳ��� ���� �ϴβ�ѯ��ҳ�� ���� �ȵ�
			startActivity(intent);
		}

		public void startMatchActivity(View view){
			Intent intent = new Intent(this,MatchActivity.class);
			//�ڴ˼�����Ҫ����Ĳ��� ���һ�ȵĻ�����Ϣ  �ȵ�
			startActivity(intent); 
		}
		
		public void startQuestActivity(View view){
			Intent intent = new Intent(this,QuestActivity.class);
			//�ڴ˼�����Ҫ����Ĳ��� ���� �ϴβ�Ѷ�ĳɾ�ҳ�� �ɾ͵������£� ���� �ȵ�
			startActivity(intent);
		}
		
		public void profileActivity(View view){
			if(hasRegistered){//ע���
				Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
				intent.putExtra("ID", user);
				startActivity(intent);
			}else{//δע���
				Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
				intent.putExtra("ID", user);
				startActivityForResult(intent,0);//�� ע��ҳ�沢���뷵��ע��Ľ��
			}
		}
		
		public void msgActivity(View view){
			Intent intent = new Intent(MainActivity.this,MessageActivity.class);
			if(msgNumbers != 0){//������Ϣ
				intent.putExtra("Read","true");
				startActivity(intent);
			}else{
				intent.putExtra("Read","false");
				startActivity(intent);
			}
		}
		
		@Override  
		protected void onActivityResult(int requestCode, int resultCode, Intent back){  //��ⷵ����Ϣ
		 //���Ը��ݶ���������������Ӧ�Ĳ���
			switch(resultCode){
			case 1:
				if(back.getStringExtra("Result").equals("Success")){ //20�����ע�᷵��de intent
					info_bar.setClickable(true);
					info_bar.setText("��ӭʹ��!!");
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
		//04. handler ���������߳̽��������Ӧ
		////////////////////////////////////////////////////
		Handler threadHandler = new Handler(){

			/* (non-Javadoc)
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0://˵����¼ʧ�ܣ�
					info_bar.setText("��¼ʧ��,�����µ�¼��������!");//������Ϣ������
					waitAlert.dismiss();
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("��¼ʧ�ܣ�")
					.setIcon(R.drawable.ic_launcher)
					.setMessage("��¼ʧ�ܣ����������Լ�ѧ�Ż������Ƿ����")
					.setNegativeButton("ȷ��", null)
					.create()
					.show();
					break;
				case 1://˵����½�ɹ�
					info_bar.setText("��½�ɹ������ڲ�ѯ�������˺���Ϣ...");//�ı����� Ȼ�������̲߳�ѯ������
					new Thread(queryHasReg).start();
					waitAlert.dismiss();
					log_button.setText("�˳�");
					log_state = true;
					if(infoSave.isChecked()){//���������Ϣ��ѡ�� ��д����Ϣ��
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
					}//û����д��
					Toast.makeText(MainActivity.this, "��½�ɹ���", Toast.LENGTH_SHORT).show();
					break;
				case 2: //δע���û�����
					info_bar.setText("����δע�ᣬ�����˴�����ע��.");
					info_bar.setClickable(true);
					hasRegistered = false;
					break;
				case 3://ע���  ���ѯ�ʼ���Ϣ
					new Thread(queryMsg).start();
					AllInfomation allInfo = (AllInfomation)getApplication();
					allInfo.setRegState(true);
					allInfo.setWelComeName(info_username);
					info_bar.setText("��ӭ "+info_username+"���ڼ������...");
//					info_bar.setClickable(true);
					hasRegistered = true;
					break;
					///������Ϣ�߳�
				case 13://û����Ϣ
					info_bar.setText("��ӭ "+info_username+"  û���µ��ʼ�...");
					info_bar.setClickable(true);
					Toast.makeText(MainActivity.this, "��û���µ���Ϣ��ע��һ����Diao si...", 2000).show();
					TextView msgView = (TextView)findViewById(R.id.Main_MsgView);
					msgView.setText(Integer.toString(msgNumbers));
					msgView.setClickable(true);
					break;
				case 14://failed
					info_bar.setText("��ӭ "+info_username+" �����ʼ�������ʧ��..");
					info_bar.setClickable(true);
					Toast.makeText(MainActivity.this, "���ӷ�����ʧ�ܣ�������ǣ���������������������Ƿ���������...", 2000).show();
					break;
				case 15://query Success!
					info_bar.setText("��ӭ "+info_username+" �����ʼ�:");
					info_bar.setClickable(true);
					Toast.makeText(MainActivity.this, "��Ϣ�б���³ɹ���", 2000).show();
					TextView msgView1 = (TextView)findViewById(R.id.Main_MsgView);
					msgView1.setText(Integer.toString(msgNumbers));
					msgView1.setClickable(true);
					new Thread(sendACK).start();
					break;
					
				case 17://ack failed!
					reSendACKTimes ++;
					if(reSendACKTimes > 10){
						Toast.makeText(MainActivity.this, "����ACKʧ�ܳ���10��", 2000);
						reSendACKTimes = 0;
					}
					else
						new Thread(sendACK).start();//�ظ�����..
					break;
				case 18 ://ack success!
					Toast.makeText(MainActivity.this, "Send ACK Success!", 2000).show();
					break;
				}

				super.handleMessage(msg);
			}
			
		};
		///////////////������Gallery �Ĳ������� ��ͷҪ��Ӻ���
		///05�����ǵ�½���߳�
		Runnable mainLogInThread = new Runnable(){

			@Override
			public void run() {
				if(user.equals("123456")&&password.equals("admin")){
					int msgWhat = 1;
					Message msg = new Message();
					msg.what = msgWhat; //Ȼ��洢cookie VIEWSTATE����������ȡ cookie ���cookie��ȡ���Ժ����ٻ�ȡviewstate
					AllInfomation allInfo = ((AllInfomation)getApplication());
					allInfo.setUser(user);
					allInfo.setPassword(password);
					allInfo.setLogState(true); //����ȫ�ֱ�����
					threadHandler.sendMessage(msg);
				}else{
					//����Ҫ��ȡ__viewstate
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
					String temp = null;//�������淵�ص���Ϣ������
					List<Cookie> cookies = null; //����һ����� Cookie ���б�
					HttpClient client = new DefaultHttpClient();//����һ�� HttpClient ����ִ�з���Request
					HttpResponse httpResponse = null;
					String url = "http://jwgl.sias.edu.cn/default2.aspx";
					HttpPost httpRequest = new HttpPost(url);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("Button1",""));
					params.add(new BasicNameValuePair("RadioButtonList1","%D1%A7%C9%FA"));
					params.add(new BasicNameValuePair("TextBox1",user));//�����˻�
					params.add(new BasicNameValuePair("TextBox2",password));//��������
					params.add(new BasicNameValuePair("__VIEWSTATE",viewState));//�����ȡ��VIEWSTATE
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
						// ����HTTP request
						httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						// ȡ��HTTP response
						httpResponse = client.execute(httpRequest);   //ִ��
						// ��״̬��Ϊ200 ok
						if (httpResponse.getStatusLine().getStatusCode() == 200) {   //����ֵ���� �����ж��Ƿ�ɹ���½��
							// ��ȡ���ص�cookie
							cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
							//��ȡ���ص���Ϣ �����м򵥽���
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
							//����������temp���Ƿ��½�ɹ���
							String pattern = "<span id=\"Label3\">(.*?)</span>";
							Pattern reg = Pattern.compile(pattern);
							Matcher match = reg.matcher(temp);
							while(match.find()){
								logSuccess = true;
								System.out.println(match.group(1));
							}
							
						} else {//��״̬����200 �����ʧ��
							//����ʧ��
							msgWhat = 0;
						}
					} catch (Exception e) {
						e.printStackTrace();
						msgWhat = 0;
					}
					//�ж��Ƿ��ȡ��httpResponse �Ľ�����ж��Ƿ��½�ɹ�������
					if(!logSuccess){
						Message msg = new Message();
						msg.what = 0;//0��ʾû�гɹ���½ ����Ҳ��cookie!!!
						threadHandler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what = msgWhat; //Ȼ��洢cookie VIEWSTATE����������ȡ cookie ���cookie��ȡ���Ժ����ٻ�ȡviewstate
						AllInfomation allInfo = ((AllInfomation)getApplication());
						allInfo.setUser(user);
						allInfo.setPassword(password);
						allInfo.setCookies(cookies);
						allInfo.setLogState(true); //����ȫ�ֱ�����
						threadHandler.sendMessage(msg);
					}
				}
			}
		};
		
		/////////////////////////////////////////////////////
		//06��ѯ�Ƿ�����˻���Ϣ
		Runnable queryHasReg = new Runnable(){
			@Override
			public void run() {
				int msgWhat = 3;//3��ʾ��ѯ�ɹ��Ѿ�ע��
				HttpClient hc = new DefaultHttpClient();
				HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("type","QueryUserInfo"));
				params.add(new BasicNameValuePair("ID",user));
				HttpResponse hr = null;
				try {
					// ����HTTP request
					hp.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					// ȡ��HTTP response
					hr = hc.execute(hp);   //ִ��
					// ��״̬��Ϊ200 ok
					if (hr.getStatusLine().getStatusCode() == 200) {   //����ֵ����
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
						JSONObject json = jsonArray.getJSONObject(0);//��ȡJSON
						if(json.getString("Name").equals("sb"))
							msgWhat = 2;//��ʾ��ѯʧ�� δע��
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
		
		//��ѯ����
		Runnable queryMsg = new Runnable(){  //15 failed 14

			@Override
			public void run() {
				int msgWhat = 15;
				HttpClient hc = new DefaultHttpClient();
				HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("type","QueryMsg")); //���� ע����Ϣ
				params.add(new BasicNameValuePair("ID",user));//������Ϣ ��û��������ƣ�������
				hp.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//����Ҫ������
				HttpResponse hr = null;
				try {
					// ����HTTP request
					hp.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					// ȡ��HTTP response
					hr = hc.execute(hp);   //ִ��
					// ��״̬��Ϊ200 ok
					if (hr.getStatusLine().getStatusCode() == 200) {   //����ֵ����
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
						//�жϷ��ؽ��
						ArrayList<HashMap<String,String>> message = new ArrayList<HashMap<String,String>>();
						JSONArray jsonArray = new JSONArray(sb.toString());
						if(jsonArray.getJSONObject(0).has("State")){
							if(jsonArray.getJSONObject(0).getString("State").equals("Nothing"))
								msgWhat = 13;//��ʾû����Ϣ
							else
								msgWhat = 14;
							System.out.println("no msg");
						}
						else{
							for(int i = 0 ; i < jsonArray.length() ; i ++){
								msgNumbers ++;// ��Ϣ��������
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
		
		//����ȷ����Ϣ ��ʾ�յ� ������ɾ����Ϣ
				Runnable sendACK = new Runnable(){  //17 ʧ�� 18

					@Override
					public void run() {
						int msgWhat = 18;
						HttpClient hc = new DefaultHttpClient();
						HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("type","ACKMsg")); //���� ע����Ϣ
						params.add(new BasicNameValuePair("ID",user));//������Ϣ ��û��������ƣ�������
						hp.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//����Ҫ������
						HttpResponse hr = null;
						try {
							// ����HTTP request
							hp.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
							// ȡ��HTTP response
							hr = hc.execute(hp);   //ִ��
							// ��״̬��Ϊ200 ok
							if (hr.getStatusLine().getStatusCode() == 200) {   //����ֵ����
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
								//�жϷ��ؽ��
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
