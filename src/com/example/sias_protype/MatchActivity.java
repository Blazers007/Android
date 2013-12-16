package com.example.sias_protype;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MatchActivity extends Activity {
	
	private AlertDialog waitAlert = null;

//	private TabHost myTabHost = null;//这个是TabHost
	//////////////////Tab3的成员////////////
	private Spinner lol_net_spinner = null;
	private Spinner lol_realm_spinner = null;
	private int netSelectedId;
	private static final String[] nets = {"网通","电信"};
	private static final String[] realms = {"比尔吉沃特","德玛西亚","弗雷尔卓德","无畏先锋","恕瑞玛","扭曲丛林"};
	private String serverNameForUpdate = null;
	private static final String[][] realmsChange = {{"比尔吉沃特","德玛西亚","弗雷尔卓德","无畏先锋","恕瑞玛","扭曲丛林"},{"艾欧尼亚","祖安","诺克萨斯","班德尔城",
		"皮尔特沃夫","战争学院","巨神峰","雷瑟守备","裁决之地","黑色玫瑰","暗影岛","钢铁烈阳","均衡教派","水晶之痕","影流","守望之海","征服之海","卡拉曼达","皮城警备"}};//先测试网通的区
	private String[][] realmsId = {{"网通一","网通二","网通三","网通四","网通五","网通六"},{"电信一","电信二","电信三","电信四","电信五","电信六","电信七","电信八","电信九","电信十","电信十一","电信十二","电信十三","电信十四","电信十五","电信十六","电信十七","电信十八","电信十九"}};
	private ArrayAdapter<String> netAdapter;
	private ArrayAdapter<String> realmAdapter;
	
	private String trueRealm = null; //把上方两个的信息存入下面两个中
	private String  truePlayer = null;//
	//private ArrayAdapter<String> realm_adapter = null;
	private String fightPoint = null; //存储战斗力
	///////////////////TAB2 成员 查询成绩/////////////
	private String finalCetGrade = ""; //存放成绩
	private int finalExamTimes = 0;//保存一共考了几次
	
	private TextView cetGradeShow = null; //显示最后的成绩
	private EditText editXH = null;//输入学号
	private EditText editPWD = null;//输入密码
	
	private int cet4OrCet6;
//	private EditText editXM = null;//输入姓名
	private String XH = null; //把上方三个的信息存入下面三个中
	private String PWD = null;//
	private String XM = null;
	//////////////////////////////////////////////
	//!!  这是  list 三个 list 来显示三个TAB的排名列表！！
	private ListView cet4List = null;
	private MyRankAdapter myAdapter_cet4 = null;
	private List<MyRankView> myCet4Data = new ArrayList<MyRankView>();
//	private SimpleAdapter adapter_cet4 = null;
	private ArrayList<HashMap<String,String>> cet4ListAdapter = new ArrayList<HashMap<String,String>>();
	private ListView cet6List = null;
	private MyRankAdapter myAdapter_cet6 = null;
	private List<MyRankView> myCet6Data = new ArrayList<MyRankView>();
	private SimpleAdapter adapter_cet6 = null;
	private ArrayList<HashMap<String,String>> cet6ListAdapter = new ArrayList<HashMap<String,String>>();
	private ListView lolList = null;
	private MyRankAdapter myAdapter_lol = null;
	private List<MyRankView> myLolData = new ArrayList<MyRankView>();
//	private SimpleAdapter adapter_lol = null;
	private ArrayList<HashMap<String,String>> lolListAdapter = new ArrayList<HashMap<String,String>>();
	///////////////这是几个按钮或者布局控件 控制他们的可见度
	private int selectId = 0 ;
	
	private boolean threadLock = true;//县城所 用来显示是否有 查询县城正在运行
	
	/////////////////////////////////////////////
	
	private int cet4Range = 0;  //这三个是用来表示标签的位置来显示的 用来分段！
	private int cet6Range = 0;
	private int lolRange = 0;
	private String[] rangeList = {"点击查询","青                              铜","白                              银","黄                              金","白                              金","钻                              石","最强                              王者"};
	private int[] rangeListImg = {R.drawable.rank0,R.drawable.rank1,R.drawable.rank2,R.drawable.rank3,R.drawable.rank4,R.drawable.rank5,R.drawable.rank6};
	private int[] rankListImg = {R.drawable.ranksixth,R.drawable.rankfifth,R.drawable.rankforth,R.drawable.rankthird,R.drawable.ranksecond,R.drawable.rankfirst,R.drawable.rankzero};
	//一个是标题图片 一个是LIST图片
	private TextView cet4RankRange = null;
	private TextView cet6RankRange = null;
	private TextView lolRankRange = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
		
		setContentView(R.layout.activity_match);
		ifHasLocal();//创建本地存储文件
		////////////////////////////////////////////////////////////
		///01.此处是三个TAB 用来装载三个xml布局文件 分别为 CET 4 6 以及 LOL
        TabHost mTabHost = (TabHost)findViewById(R.id.tabhost); 
        mTabHost.setup();
        LayoutInflater inflater_tab1 = LayoutInflater.from(this);   
        inflater_tab1.inflate(R.layout.match_tab1, mTabHost.getTabContentView());//三个子XML文件  
        inflater_tab1.inflate(R.layout.match_tab2, mTabHost.getTabContentView());
        inflater_tab1.inflate(R.layout.match_tab3, mTabHost.getTabContentView());
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("CET-4").setContent(R.id.LinearLayout01));   
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("CET-6").setContent(R.id.LinearLayout02));  
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("LOL").setContent(R.id.LinearLayout03));
        ///*********下拉选单暂时无法在AlertDialog中使用!!!
        //Test waitAlert 最后阶段优化内存时候考虑下!!
        LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(MatchActivity.this)
		.setView(waitAlertView)
		.create();
		//初始化 rankRange
		cet4RankRange = (TextView) findViewById(R.id.Cet4RankRegion);
		cet6RankRange = (TextView) findViewById(R.id.Cet6RankRegion);
		lolRankRange = (TextView) findViewById(R.id.LolRankRegion);
		cet4RankRange.setText(rangeList[cet4Range]);
		cet6RankRange.setText(rangeList[cet6Range]);
		lolRankRange.setText(rangeList[lolRange]);
		//////////然后应该如果本地有信息的话则直接读取
		/////////////////////////////////////
		//载入三个 listview
		cet4List = (ListView)findViewById(R.id.cet4RankList);
		cet6List = (ListView)findViewById(R.id.cet6RankList);
		lolList = (ListView)findViewById(R.id.lolRankList);
		
		MyRankView v1 = new MyRankView("1","一万个兄贵","537");
		MyRankView v2 = new MyRankView("3","一万个兄贵","537");
		MyRankView v3 = new MyRankView("9","一万个兄贵","537");
		
		myCet4Data.add(v1);myCet4Data.add(v2);myCet4Data.add(v3);
		myCet6Data.add(v1);myCet6Data.add(v2);myCet6Data.add(v3);
		myLolData.add(v1);myLolData.add(v2);myLolData.add(v3);
		
		myAdapter_cet4 = new MyRankAdapter(myCet4Data);myAdapter_cet6 = new MyRankAdapter(myCet6Data);myAdapter_lol = new MyRankAdapter(myLolData);
		
		cet4List.setAdapter(myAdapter_cet4);cet6List.setAdapter(myAdapter_cet6);lolList.setAdapter(myAdapter_lol);
		
		cet4List.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final int selectId = arg2;
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View popView =inflater.inflate(R.layout.match_list_detail,null);
				TextView IDView = (TextView)popView.findViewById(R.id.MatchListDetail_ID);
				IDView.setText(cet4ListAdapter.get(arg2).get("ID"));
				new AlertDialog.Builder(MatchActivity.this)
				.setTitle("资料卡")
				.setView(popView)
				.setNegativeButton("关闭", null)
				.setPositiveButton("详细资料", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String ID = cet4ListAdapter.get(selectId).get("ID");
						Intent intent = new Intent(MatchActivity.this,ProfileActivity.class);
						intent.putExtra("ID", ID);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
		});
		cet6List.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final int selectId = arg2;
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View popView =inflater.inflate(R.layout.match_list_detail,null);
				TextView IDView = (TextView)popView.findViewById(R.id.MatchListDetail_ID);
				IDView.setText(cet4ListAdapter.get(arg2).get("ID"));
				new AlertDialog.Builder(MatchActivity.this)
				.setTitle("资料卡")
				.setView(popView)
				.setNegativeButton("关闭", null)
				.setPositiveButton("详细资料", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String ID = cet6ListAdapter.get(selectId).get("ID");
						Intent intent = new Intent(MatchActivity.this,ProfileActivity.class);
						intent.putExtra("ID", ID);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
		});
		lolList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final int selectId = arg2;
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View popView =inflater.inflate(R.layout.match_list_detail,null);
				TextView IDView = (TextView)popView.findViewById(R.id.MatchListDetail_ID);
				IDView.setText(cet4ListAdapter.get(arg2).get("ID"));
				new AlertDialog.Builder(MatchActivity.this)
				.setTitle("资料卡")
				.setView(popView)
				.setNegativeButton("关闭", null)
				.setPositiveButton("详细资料", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String ID = lolListAdapter.get(selectId).get("ID");
						Intent intent = new Intent(MatchActivity.this,ProfileActivity.class);
						intent.putExtra("ID", ID);
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
		});
		///////////////////载入几个按钮控件便于控制
		
		////////////////开启线程去装载读取本地文件
		
		
		////////////////测试 spinner///////////////////////////////////////////////////////////////////////////
		lol_net_spinner = (Spinner)findViewById(R.id.lol_net_spinner);
		netAdapter = new ArrayAdapter<String>(MatchActivity.this,android.R.layout.simple_spinner_item,nets);
		netAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		lol_net_spinner.setAdapter(netAdapter);
		lol_net_spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				netSelectedId = arg2;
				realmAdapter = new ArrayAdapter<String>(MatchActivity.this,android.R.layout.simple_spinner_item,realmsChange[arg2]);
				lol_realm_spinner.setAdapter(realmAdapter);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		lol_realm_spinner = (Spinner)findViewById(R.id.lol_realm_spinner);
		realmAdapter = new ArrayAdapter<String>(MatchActivity.this,android.R.layout.simple_spinner_item,realms);
		realmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		lol_realm_spinner.setAdapter(realmAdapter);
		lol_realm_spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				trueRealm = realmsId[netSelectedId][arg2];
				serverNameForUpdate = realmsChange[netSelectedId][arg2];
				Toast.makeText(MatchActivity.this, trueRealm,Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(MatchActivity.this, "请选择一个服务器",Toast.LENGTH_SHORT).show();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match, menu);
		return true;
	}
	
	//////////////////////////////////////////Button 响应函数/////////////////////
	///Button1 LOL战斗力查询
	public void findLol(View view){
		EditText playerName = (EditText)findViewById(R.id.playerName);
		truePlayer = playerName.getText().toString();
		//为两个空间设置对象
		if(playerName.getText().toString().isEmpty()){
			Toast.makeText(MatchActivity.this, "请输入角色名！", 1000).show();
		}else{
			new Thread(queryFightPoint).start();//开启一个查询线程
			waitAlert.show();
		}
	}
	
	//////Button2 CET 4-6查询
	public void findCet6(View view){
		cet4OrCet6 = 6;
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popView =inflater.inflate(R.layout.pop_cet_band, null);
		
		editXH = (EditText)popView.findViewById(R.id.editXH);
		editPWD = (EditText)popView.findViewById(R.id.editPWD);
		
		new AlertDialog.Builder(MatchActivity.this)
		.setTitle("查询成绩")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(popView)
		.setPositiveButton("查询", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				XH = editXH.getText().toString();
				PWD = editPWD.getText().toString();
				if(editXH.getText().toString().isEmpty()||editPWD.getText().toString().isEmpty()){
					Toast.makeText(MatchActivity.this, "请输入完整的信息！", 1000).show();
				}else{
					new Thread(queryCetGrade).start();
					waitAlert.show();
				}
			}
		})
		.create()
		.show();
	}
	
	
	public void findCet4(View view){
		cet4OrCet6 = 4;
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popView =inflater.inflate(R.layout.pop_cet_band, null);
		
		editXH = (EditText)popView.findViewById(R.id.editXH);
		editPWD = (EditText)popView.findViewById(R.id.editPWD);
		
		new AlertDialog.Builder(MatchActivity.this)
		.setTitle("查询成绩")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(popView)
		.setPositiveButton("查询", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				XH = editXH.getText().toString();
				PWD = editPWD.getText().toString();
				if(editXH.getText().toString().isEmpty()||editPWD.getText().toString().isEmpty()){
					Toast.makeText(MatchActivity.this, "请输入完整的信息！", 1000).show();
				}else{
					new Thread(queryCetGrade).start();
					waitAlert.show();
				}
			}
		})
		.create()
		.show();
	}

	////////////////////////////////////本地文件操作区域/////////////////////////
	boolean ifHasLocal(){
		boolean allRight = true;
		File file1 = new File("/sdcard/SiasPro/Control/Cet4_Rank.txt");
		File file2 = new File("/sdcard/SiasPro/Control/Cet4_My.txt");
		File file3 = new File("/sdcard/SiasPro/Control/Cet4_Choose.txt");
		File file4 = new File("/sdcard/SiasPro/Control/Cet6_Rank.txt");
		File file5 = new File("/sdcard/SiasPro/Control/Cet6_My.txt");
		File file6 = new File("/sdcard/SiasPro/Control/Cet6_Choose.txt");
		File file7 = new File("/sdcard/SiasPro/Control/Lol_Rank.txt");
		File file8 = new File("/sdcard/SiasPro/Control/Lol_choose.txt");
//		List<File> fileList = new ArrayList<File>();
		File[] fileList = {file1,file2,file3,file4,file5,file6,file7,file8};
		for(int i = 0 ; i < 8 ; i ++){
			if(!fileList[i].exists()){
				try {
					FileOutputStream fout = new FileOutputStream(fileList[i]);
					fout.write("#".getBytes());
					fout.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				allRight = false; //如果某个文件不存在 就写入 # 并且把全部OK标记为false
			}
		}
		return allRight;
	}
	
	//////////将指定的写入本地文件中去
	void writeToLocal(String write,String path){
		try {
			File file = new File(path);
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
	//////////////////////////////Handler区域/////////////////////////////////
	Handler threadHandler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 1://查询战斗力
					waitAlert.dismiss();
					TextView fightPointShow = (TextView)findViewById(R.id.fightPointShow);
					TextView fightPlayerShow = (TextView)findViewById(R.id.fightPlayerShow);
//					fightPointShow.setText("服务器: "+trueRealm+"\n角色名: "+truePlayer+"\n战斗力: "+fightPoint);
					fightPlayerShow.setText(truePlayer);
					fightPointShow.setText(fightPoint);
					LinearLayout layout1 = (LinearLayout) findViewById(R.id.myLolLayout1);
					layout1.setVisibility(View.GONE);
					LinearLayout layout = (LinearLayout) findViewById(R.id.myLolLayout);
					layout.setVisibility(View.VISIBLE);
					//自动绑定！
					AllInfomation allInfo = (AllInfomation)getApplication();
					if(allInfo.isRegState()&&!fightPoint.equals("0")){ // 如果已经登录则
						//另外还要向服务器传输绑定的信息！ String region,String ID,String Server,String Name,String Score,int msgWhat
						MyThreadUpdate update = new MyThreadUpdate("LOL",allInfo.getUser(),serverNameForUpdate,allInfo.getWelComeName(),fightPoint,10,truePlayer);
						update.start();
					}else{ //未登录！
						Toast.makeText(MatchActivity.this,"您尚未注册！或查询失败！不能进行绑定！",2000).show();
					}
					break;
				case 2://查询6级成绩  那么在此应该弹出一个框来让其选择绑定那个成绩！
					waitAlert.dismiss();
					writeToLocal(finalCetGrade,"/sdcard/SiasPro/Control/Cet6_My.txt");// 写入本地
					ListView popListBand = null;
					final ArrayList<HashMap<String,String>> popListBandAdapter = new ArrayList<HashMap<String,String>>(); //多个List来装入六级成绩
					String[] exams = finalCetGrade.split("@"); //按次分开
					for(int i = 0; i< finalExamTimes ; i ++){
						String[] oneTime = exams[i].split("#");//将一次的分开 然后装入map中去
						HashMap<String,String> map = new HashMap<String,String>();
						for(int odex = 0 ; odex < 10 ; odex ++ ){
							map.put(Integer.toString(odex),oneTime[odex]);//将各项放入其中
						}
						popListBandAdapter.add(map);
					}
					LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
					View popView =inflater.inflate(R.layout.pop_cet_list_band,null);
					popListBand = (ListView)popView.findViewById(R.id.pop_cet_list_listview);
					SimpleAdapter adapter = new SimpleAdapter(MatchActivity.this,popListBandAdapter,
							R.layout.pop_cet_listview_detail1,
							new String[]{"3","4","5","6","7","8","9"},
							new int[]{R.id.a3_id,R.id.a4_date,R.id.a5_sum,R.id.a6_listen,R.id.a7_read,
							R.id.a8_write,R.id.a9_mix});
//					cetGradeShow = (TextView)findViewById(R.id.cetGradeShow);
//					cetGradeShow.setText(finalCetGrade);
					popListBand.setAdapter(adapter);
					//设置监听！
					popListBand.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							selectId = position; //应该添加一个或者checkBox 来表示选中了那个或者变色
							Toast.makeText(MatchActivity.this, "绑定总分为:"+popListBandAdapter.get(selectId).get("5"), Toast.LENGTH_SHORT).show();
						}
						
					});
					new AlertDialog.Builder(MatchActivity.this)
					.setTitle("Band cet")
					.setView(popView)
					.setPositiveButton("绑定",new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							AllInfomation allInfo = (AllInfomation)getApplication();
							if(allInfo.isRegState()){ // 如果已经登录则
								LinearLayout myCet6Layout = (LinearLayout)findViewById(R.id.myCet6Layout);
								Button findCet6Button = (Button)findViewById(R.id.findCet6Button);
								TextView cet6Name = (TextView)findViewById(R.id.cet6_name);
								TextView cet6Grade = (TextView)findViewById(R.id.cet6_grade);
								findCet6Button.setVisibility(View.GONE);
								cet6Name.setText(XH);//设置名称！
								writeToLocal("MyRank"+"#"+"MyName"+"#"+popListBandAdapter.get(selectId).get("5"),"/sdcard/SiasPro/Control/Cet6_Choose");
								cet6Grade.setText(popListBandAdapter.get(selectId).get("5"));//吧总分输出出去
								myCet6Layout.setVisibility(View.VISIBLE);
								//另外还要向服务器传输绑定的信息！ String region,String ID,String Server,String Name,String Score,int msgWhat
								if(allInfo.getUser().equals(XH)){
									MyThreadUpdate update = new MyThreadUpdate("CET6",allInfo.getUser(),"Server",allInfo.getWelComeName(),cet6Grade.getText().toString(),10);
									update.start();
								}else{
									Toast.makeText(MatchActivity.this,"别绑定别人的成绩啊...",2000).show();
								}
							}else{ //未登录！
								Toast.makeText(MatchActivity.this,"您尚未注册！不能进行绑定！",2000).show();
							}
						}
						
					})
					.create()
					.show();
					finalCetGrade = ""; //将次数显示为0
					finalExamTimes = 0;
					cet4OrCet6 = 0;
					break;
				case 3:
					waitAlert.dismiss();
					writeToLocal(finalCetGrade,"/sdcard/SiasPro/Control/Cet4_My.txt");// 写入本地
					ListView popListBand1 = null;
					final ArrayList<HashMap<String,String>> popListBandAdapter1 = new ArrayList<HashMap<String,String>>(); //多个List来装入六级成绩
					String[] exams1 = finalCetGrade.split("@"); //按次分开
					for(int i = 0; i< finalExamTimes ; i ++){
						String[] oneTime = exams1[i].split("#");//将一次的分开 然后装入map中去
						HashMap<String,String> map = new HashMap<String,String>();
						for(int odex = 0 ; odex < 10 ; odex ++ ){
							map.put(Integer.toString(odex),oneTime[odex]);//将各项放入其中
						}
						popListBandAdapter1.add(map);
					}
					LayoutInflater inflater1=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
					View popView1 =inflater1.inflate(R.layout.pop_cet_list_band,null);
					popListBand1 = (ListView)popView1.findViewById(R.id.pop_cet_list_listview);
					SimpleAdapter adapter1 = new SimpleAdapter(MatchActivity.this,popListBandAdapter1,
							R.layout.pop_cet_listview_detail1,
							new String[]{"3","4","5","6","7","8","9"},
							new int[]{R.id.a3_id,R.id.a4_date,R.id.a5_sum,R.id.a6_listen,R.id.a7_read,
							R.id.a8_write,R.id.a9_mix});
//					cetGradeShow = (TextView)findViewById(R.id.cetGradeShow);
//					cetGradeShow.setText(finalCetGrade);
					popListBand1.setAdapter(adapter1);
					//设置监听！
					popListBand1.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							selectId = position; //应该添加一个或者checkBox 来表示选中了那个或者变色
							Toast.makeText(MatchActivity.this, "绑定总分为:"+popListBandAdapter1.get(selectId).get("5"), Toast.LENGTH_SHORT).show();
						}
						
					});
					new AlertDialog.Builder(MatchActivity.this)
					.setTitle("Band cet")
					.setView(popView1)
					.setPositiveButton("绑定",new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							AllInfomation allInfo = (AllInfomation)getApplication();
							if(allInfo.isRegState()){ // 如果已经登录则
								LinearLayout myCet4Layout = (LinearLayout)findViewById(R.id.myCet4Layout);
								Button findCet4Button = (Button)findViewById(R.id.findCet4Button);
								TextView cet4Name = (TextView)findViewById(R.id.cet4_name);
								TextView cet4Grade = (TextView)findViewById(R.id.cet4_grade);
								findCet4Button.setVisibility(View.GONE);
								cet4Name.setText(XH);//设置名称！
								writeToLocal("MyRank"+"#"+"MyName"+"#"+popListBandAdapter1.get(selectId).get("5"),"/sdcard/SiasPro/Control/Cet4_Choose");
								cet4Grade.setText(popListBandAdapter1.get(selectId).get("5"));//吧总分输出出去
								myCet4Layout.setVisibility(View.VISIBLE);
								//另外还要向服务器传输绑定的信息！
								if(allInfo.getUser().equals(XH)){
									MyThreadUpdate update = new MyThreadUpdate("CET4",allInfo.getUser(),"Server",allInfo.getWelComeName(),cet4Grade.getText().toString(),10);
									update.start();
								}else{
									Toast.makeText(MatchActivity.this,"别绑定别人的成绩啊...",2000).show();
								}
								//并将自己选的写入本地记录中
							}else{ //未登录！
								Toast.makeText(MatchActivity.this,"您尚未注册！不能进行绑定！",2000).show();
							}
						}
						
					})
					.create()
					.show();
					finalCetGrade = ""; //将次数显示为0
					finalExamTimes = 0;
					cet4OrCet6 = 0;
					break;
				case 4: //此处是对于更新cet4rank的操作
					threadLock = true;
					myAdapter_cet4.notifyDataSetChanged();
					waitAlert.dismiss();
					break;
				case 5: //此处是对于更新cet4rank的操作
					threadLock = true;
					myAdapter_cet6.notifyDataSetChanged();
					waitAlert.dismiss();
					break;
				case 6: //此处是对于更新cet4rank的操作
					threadLock = true;
					myAdapter_lol.notifyDataSetChanged();
					waitAlert.dismiss();
					break;
				case 10:
					Toast.makeText(MatchActivity.this,"Update success!",2000).show();
					break;
				case 11: //查找排名失败！！
					threadLock = true;
					waitAlert.dismiss();
					Toast.makeText(MatchActivity.this,"Query faied!!",2000).show();
					break;
				case 12:
					waitAlert.dismiss();
					Toast.makeText(MatchActivity.this,"Update Failed!",2000).show();
					break;
				case 13:
					Toast.makeText(MatchActivity.this,"failed!",1000).show();
					break;
			}
			// TODO Auto-generated method stub
			super.handleMessage(msg);//接受收到的MSG信息但是此处暂时先写对LOL的反应 故不加switch
			//其实此处缺少完全的相应方法 比如为查找到 超时等等
		}
	};
	/////////////////////////////线程区域//////////////////////////////////////////
	//线程1 queryFightPoint
	Runnable queryFightPoint = new Runnable(){
		//新的查询战斗力线程 以后比如查询四六级成绩线程也要写出来！！！
		@Override
		public void run() {
			int msgWhat = 13;
			StringBuilder sb = new StringBuilder();
			try {
				URL url = new URL("http://lolbox.duowan.com/playerDetail_baidu.php");
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				connection.setReadTimeout(25*1000);
				//发送域信息
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1");
				String realm = java.net.URLEncoder.encode(trueRealm,"utf-8");
				String player = java.net.URLEncoder.encode(truePlayer,"utf-8");
				out.write("serverName="+realm+"&playerName="+player);
				//写入post参数对象
				out.flush();
				out.close();
				//获取返回数据
				InputStream in=connection.getInputStream();
				while(true){
					byte[] buffer=new byte[102400]; //每段为 100 KB大小
					int amountRead=in.read(buffer);//输入流输入到 buffer数组中
					if(amountRead==-1){
						break;
					}	
					sb.append(new String(buffer,0,amountRead,"utf-8"));
					//分别在压入StringBuilder 进行组装
				}
				String pattern = "<em><span title='其中基础分（主要跟排位赛有关）(.*?) 胜率加成分(.*?) 胜场加成分(.*?)'>(.*?)</span>";
				//设置一个正则过滤规则
				Pattern reg = Pattern.compile(pattern);
				//一个使用上述规则的 Pattern 对象 reg
				Matcher match = reg.matcher(sb.toString());
				//将返回信息的StringBuilder转化为字符串并套用规则进行过滤 取出的放入 Matcher 对象中
				while(match.find())
						fightPoint = match.group(4);
				msgWhat = 1;
//				AllInfomation allInfo = ((AllInfomation)getApplication());
//				System.out.println(allInfo.getCookies());
				} catch (MalformedURLException e) {
				msgWhat = 13;
				e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					msgWhat = 13;
				e.printStackTrace();
				} catch (IOException e) {
					msgWhat = 13;
				e.printStackTrace();
				} 
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
			System.out.println(fightPoint);
		}
	};
	
	////线程2 queryCetGrade
	Runnable queryCetGrade = new Runnable(){

		@Override
		public void run() {

			int msgWhat = 19;
			//首先要获取__viewstate
			String viewState = null;
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
				//设置超时
				HttpClient defaultHttpClient = new DefaultHttpClient();
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
//				System.out.println(sb.toString());
				String pattern = "<input type=\"hidden\" name=\"__VIEWSTATE\" value=\"(.*?)\" />";
				Pattern reg = Pattern.compile(pattern);
				Matcher match = reg.matcher(sb.toString());
				while(match.find())
					viewState = match.group(1);
			}catch(Exception ex){
				msgWhat = 13;
			}
//				System.out.println(viewState);
			// TODO Auto-generated method stub
			String xm1 = "张良";
			try {
				xm1 = java.net.URLEncoder.encode(xm1, "GB2312");
			} catch (UnsupportedEncodingException e1) {
				msgWhat = 13;
				e1.printStackTrace();
			}
			String temp = null;
			List<Cookie> cookies = null; //建立一个存放 Cookie 的列表
			HttpClient client = new DefaultHttpClient();//声明一个 HttpClient 用于执行访问Request
			HttpResponse httpResponse = null;
			String url = "http://jwgl.sias.edu.cn/default2.aspx";
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Button1",""));
			params.add(new BasicNameValuePair("RadioButtonList1","%D1%A7%C9%FA"));
			params.add(new BasicNameValuePair("TextBox1",XH));
			params.add(new BasicNameValuePair("TextBox2",PWD));
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
				httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				// 取得HTTP response
				httpResponse = client.execute(httpRequest);   //执行
				// 若状态码为200 ok
				if (httpResponse.getStatusLine().getStatusCode() == 200) {   //返回值正常
					// 获取返回的cookie
					cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
				} else {
					msgWhat = 13;
				}
			} catch (Exception e) {
				msgWhat = 13;
			}
			//gb2312
			String url2 = "http://218.198.176.91/xsdjkscx.aspx?xh="+XH+"&xm="+xm1+"&gnmkdm=N121606";
			HttpGet myRequest = new HttpGet(url2);
			try {
			HttpResponse myResponse = null;
			HttpClient myClient = new DefaultHttpClient();
//			System.out.println("statring cookie get");
			myRequest.setHeader("Cookie","ASP.NET_SessionId="+cookies.get(0).getValue());
			myRequest.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			myRequest.setHeader("Accept-Encoding	","gzip, deflate");
			myRequest.setHeader("Accept-Language",	"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			myRequest.setHeader("Cache-Control",	"max-age=0");
			myRequest.setHeader("Connection", "keep-alive");
			myRequest.setHeader("DNT","1");
			myRequest.setHeader("Host","218.198.176.91");
			myRequest.setHeader("Referer",	"http://218.198.176.91/xs_main.aspx?xh="+XH);
			//设置超时
			myClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
			myClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000); 
			myResponse = myClient.execute(myRequest);
			if(myResponse.getStatusLine().getStatusCode() ==200 ){
//				System.out.println("state ok!");
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = myResponse.getEntity();
				InputStream is = entity.getContent();
				while(true){
					byte[] buffer = new byte[102400];
					int len = is.read(buffer);
					if(len == -1)
						break;
					sb.append(new String(buffer,0,len,"GB2312"));
//					System.out.println("downloading!!");
					}
				temp = sb.toString();
				}else{
					msgWhat =13;
					}
			}catch(Exception e) {  
				e.printStackTrace();  
				msgWhat = 13;
                }  
//			System.out.println("download over!!");
			if(msgWhat != 13){
				String pattern = "<td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td>";
				Pattern reg = Pattern.compile(pattern);
				Matcher match = reg.matcher(temp);
				System.out.println("heihei");
				if(cet4OrCet6 == 6){
					while(match.find()){
						if(match.group(3).equals("英语六级")){
							for(int i=1;i<11;i++){
								finalCetGrade += (match.group(i)+"#");
								}
								finalCetGrade += "@";
								finalExamTimes++;
//								System.out.println()
								}else{
									System.out.println("haha");
								}
						}
					finalCetGrade = finalCetGrade.replace("&nbsp;", "无");
					System.out.println(finalCetGrade);
					msgWhat = 2;
				}else{
					while(match.find()){
						if(match.group(3).equals("英语四级")){
							for(int i=1;i<11;i++){
								finalCetGrade += (match.group(i)+"#");
								}
								finalCetGrade += "@";
								finalExamTimes++;
//								System.out.println()
								}else{
									System.out.println("haha");
								}
						}
					finalCetGrade = finalCetGrade.replace("&nbsp;", "无");
					System.out.println(finalCetGrade);
					msgWhat = 3;
				}
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	};
	
	Runnable readLocalFile = new Runnable(){

		@Override
		public void run() {
			File file1 = new File("/sdcard/SiasPro/Control/Cet4_Rank.txt");
			File file2 = new File("/sdcard/SiasPro/Control/Cet4_My.txt");
			File file3 = new File("/sdcard/SiasPro/Control/Cet4_Choose.txt");
			File file4 = new File("/sdcard/SiasPro/Control/Cet6_Rank.txt");
			File file5 = new File("/sdcard/SiasPro/Control/Cet6_My.txt");
			File file6 = new File("/sdcard/SiasPro/Control/Cet6_Choose.txt");
			File file7 = new File("/sdcard/SiasPro/Control/Lol_Rank.txt");
			File file8 = new File("/sdcard/SiasPro/Control/Lol_choose.txt");
			try {
				BufferedReader in = new BufferedReader(new FileReader(file5));
				String line = null;
				while((line = in.readLine()) != null){
					if(line.isEmpty())
						break;
					
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	///////////////////////////自顶一个类 可以传参数来进行代码的优化
	class MyThreadQuery extends Thread{
		private String region;
		private int msgWhat;
		private String range;
		private ArrayList<HashMap<String,String>> arrayAdapter ;
		private List<MyRankView> myRankView;
		
		MyThreadQuery(String region,int msgWhat,int range,ArrayList<HashMap<String,String>> arrayAdapter,List<MyRankView> myRankView){
			this.region = region;
			this.msgWhat = msgWhat;
			this.range = Integer.toString(range);
			this.arrayAdapter = arrayAdapter;
			this.myRankView = myRankView;
		}
		@Override
		public void run() {
			threadLock = false;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","Query"));
			params.add(new BasicNameValuePair("region",region));
			params.add(new BasicNameValuePair("range",range));  //设置超时时间！！
			hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
			hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);

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
//					JSONObject jsonArray = new JSONObject(sb.toString());
					JSONArray jsonArray = new JSONArray(sb.toString());
					arrayAdapter.clear();
					for(int i = 0 ; i < jsonArray.length(); i ++){
						JSONObject json = jsonArray.getJSONObject(i);
						HashMap<String,String> map = new HashMap<String,String>();
						//////////////////////////
						MyRankView rankView = new MyRankView(json.getString("Rank"),json.getString("Name"),json.getString("Score"));
						myRankView.add(rankView);
						/////////////////////////
						map.put("Rank", json.getString("Rank"));
						map.put("ID", json.getString("ID"));
						map.put("Name",json.getString("Name"));
						map.put("Score", json.getString("Score"));
						arrayAdapter.add(map);
					}
				} else {
					System.out.println("connect failed!");
					msgWhat = 11;
				}
			} catch (Exception e) {
				e.printStackTrace();
				msgWhat = 11;
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	}
	
	class MyThreadUpdate extends Thread{
		private String region;
		private String ID;
		private String Server;//不一定有
		private String Name;
		private String Score;
		private int msgWhat;
		private String playerName = "";
		MyThreadUpdate(String region,String ID,String Server,String Name,String Score,int msgWhat,String playerName){
			this.region = region;
			this.ID = ID;
			this.Server =Server;
			this.Name = Name;
			this.Score = Score;
			this.msgWhat = msgWhat;
			this.playerName = playerName;
		}
		MyThreadUpdate(String region,String ID,String Server,String Name,String Score,int msgWhat){
			this.region = region;
			this.ID = ID;
			this.Server =Server;
			this.Name = Name;
			this.Score = Score;
			this.msgWhat = msgWhat;
		}
		@Override
		public void run() {
			HttpClient hc = new DefaultHttpClient();	
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","Update"));
			params.add(new BasicNameValuePair("region",region));
			params.add(new BasicNameValuePair("ID",ID));
			params.add(new BasicNameValuePair("server",Server));
			params.add(new BasicNameValuePair("name",Name));
			params.add(new BasicNameValuePair("score",Score));
			params.add(new BasicNameValuePair("player",playerName));
			hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
			hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
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
					if(!jsonArray.getJSONObject(0).getString("State").equals("success"))
						msgWhat = 12;
				} else {
					System.out.println("connect failed!");
					msgWhat = 12;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	}
	
	//分级 int 默认为 0 此时显示查找战斗力 点哪个都行 此后开始计算
	//1青铜 300~424 2白银 425~475 3 黄金476~525 4白金 526~575 5钻石 576~625 6最强王者 626~*** 
	//试着改变 窗口的边框 来彰显威力啊！！哈哈
	public void queryCet4RankLeft(View view){
		if(threadLock){
			if(cet4Range == 0){//这是还没开始查找
				cet4Range ++;
				myCet4Data.clear();myAdapter_cet4.notifyDataSetChanged();
				cet4RankRange.setBackgroundResource(rangeListImg[cet4Range]);
				cet4RankRange.setText(rangeList[cet4Range]);//暂时不考虑没有查找成功的情况！！
				MyThreadQuery queryCet4Rank = new MyThreadQuery("CET4",4,cet4Range,cet4ListAdapter,myCet4Data);
				queryCet4Rank.start();
				waitAlert.show();
			}else if(cet4Range == 1){//到达边界
				return;
			}else{
				cet4Range --;
				myCet4Data.clear();myAdapter_cet4.notifyDataSetChanged();
				MyThreadQuery queryCet4Rank = new MyThreadQuery("CET4",4,cet4Range,cet4ListAdapter,myCet4Data);
				queryCet4Rank.start();
				cet4RankRange.setBackgroundResource(rangeListImg[cet4Range]);
				cet4RankRange.setText(rangeList[cet4Range]);
				waitAlert.show();
			}
		}
	}
	public void queryCet4RankRight(View view){
		if(threadLock){
			if(cet4Range == 0){//这是还没开始查找
				cet4Range ++;
				myCet4Data.clear();myAdapter_cet4.notifyDataSetChanged();
				MyThreadQuery queryCet4Rank = new MyThreadQuery("CET4",4,cet4Range,cet4ListAdapter,myCet4Data);
				queryCet4Rank.start();
				cet4RankRange.setBackgroundResource(rangeListImg[cet4Range]);
				cet4RankRange.setText(rangeList[cet4Range]);
				waitAlert.show();
			}else if(cet4Range == 6){//到达边界
				return;
			}else{
				cet4Range ++;
				myCet4Data.clear();myAdapter_cet4.notifyDataSetChanged();
				MyThreadQuery queryCet4Rank = new MyThreadQuery("CET4",4,cet4Range,cet4ListAdapter,myCet4Data);
				queryCet4Rank.start();
				cet4RankRange.setBackgroundResource(rangeListImg[cet4Range]);
				cet4RankRange.setText(rangeList[cet4Range]);
				waitAlert.show();
			}
		}
		
	}
	public void queryCet6RankLeft(View view){
		if(threadLock){
			if(cet6Range == 0){//这是还没开始查找
				cet6Range ++;
				myCet6Data.clear();myAdapter_cet6.notifyDataSetChanged();
				MyThreadQuery queryCet6Rank = new MyThreadQuery("CET6",5,cet6Range,cet6ListAdapter,myCet6Data);
				queryCet6Rank.start();
				cet6RankRange.setBackgroundResource(rangeListImg[cet6Range]);
				cet6RankRange.setText(rangeList[cet6Range]);
				waitAlert.show();
			}else if(cet6Range == 1){//到达边界
				return;
			}else{
				cet6Range --;
				myCet6Data.clear();myAdapter_cet6.notifyDataSetChanged();
				MyThreadQuery queryCet6Rank = new MyThreadQuery("CET6",5,cet6Range,cet6ListAdapter,myCet6Data);
//				threadHandler.postDelayed(queryCet6Rank, 1000);
				queryCet6Rank.start();
				cet6RankRange.setBackgroundResource(rangeListImg[cet6Range]);
				cet6RankRange.setText(rangeList[cet6Range]);
				waitAlert.show();
			}
		}
		
	}
	public void queryCet6RankRight(View view){
		if(threadLock){
			if(cet6Range == 0){//这是还没开始查找
				cet6Range ++;
				myCet6Data.clear();myAdapter_cet6.notifyDataSetChanged();
				MyThreadQuery queryCet6Rank = new MyThreadQuery("CET6",5,cet6Range,cet6ListAdapter,myCet6Data);
				queryCet6Rank.start(); //其实这个应该弄到成功相应之后！ 或者查找失败改写表单数据!!!
				cet6RankRange.setBackgroundResource(rangeListImg[cet6Range]);
				cet6RankRange.setText(rangeList[cet6Range]);
				waitAlert.show();
			}else if(cet6Range == 6){//到达边界
				return;
			}else{
				cet6Range ++;
				myCet6Data.clear();myAdapter_cet6.notifyDataSetChanged();
				MyThreadQuery queryCet6Rank = new MyThreadQuery("CET6",5,cet6Range,cet6ListAdapter,myCet6Data);
				queryCet6Rank.start();
				cet6RankRange.setBackgroundResource(rangeListImg[cet6Range]);
				cet6RankRange.setText(rangeList[cet6Range]);
				waitAlert.show();
			}
		}
	}
	public void queryLolRankLeft(View view){
		if(threadLock){

			if(lolRange == 0){
				lolRange ++;
				myLolData.clear();myAdapter_lol.notifyDataSetChanged();
				MyThreadQuery queryLolRank = new MyThreadQuery("LOL",6,lolRange,lolListAdapter,myLolData);
				queryLolRank.start();
				lolRankRange.setBackgroundResource(rangeListImg[lolRange]);
				lolRankRange.setText(rangeList[lolRange]);
				waitAlert.show();
			}else if(lolRange == 1){
				return;
			}else{
				lolRange -- ;
				myLolData.clear();myAdapter_lol.notifyDataSetChanged();
				MyThreadQuery queryLolRank = new MyThreadQuery("LOL",6,lolRange,lolListAdapter,myLolData);
				queryLolRank.start();
				lolRankRange.setBackgroundResource(rangeListImg[lolRange]);
				lolRankRange.setText(rangeList[lolRange]);
				waitAlert.show();
			}
		}
	}
	public void queryLolRankRight(View view){
		if(threadLock){

			if(lolRange == 0){
				lolRange ++;
				myLolData.clear();myAdapter_lol.notifyDataSetChanged();
				MyThreadQuery queryLolRank = new MyThreadQuery("LOL",6,lolRange,lolListAdapter,myLolData);
				queryLolRank.start();
				lolRankRange.setBackgroundResource(rangeListImg[lolRange]);
				lolRankRange.setText(rangeList[lolRange]);
				waitAlert.show();
			}else if(lolRange == 6){
				return;
			}else{
				lolRange ++ ;
				myLolData.clear();myAdapter_lol.notifyDataSetChanged();
				MyThreadQuery queryLolRank = new MyThreadQuery("LOL",6,lolRange,lolListAdapter,myLolData);
				queryLolRank.start();
				lolRankRange.setBackgroundResource(rangeListImg[lolRange]);
				lolRankRange.setText(rangeList[lolRange]);
				waitAlert.show();
			}
		}
	}
	
	class MyRankView{
		private String rank;
		private String name;
		private String score;
		public MyRankView(String rank, String name, String score) {
			super();
			this.rank = rank;
			this.name = name;
			this.score = score;
		}
		public String getRank() {
			return rank;
		}
		public String getName() {
			return name;
		}
		public String getScore() {
			return score;
		}
		
	}
	class MyRankAdapter extends BaseAdapter{

		private List<MyRankView> rankViews;
		private ImageView rankImg;
		private TextView nameView;
		private TextView scoreView;

		public MyRankAdapter(List<MyRankView> rankViews) {
			super();
			this.rankViews = rankViews;
		}

		@Override
		public int getCount() {
			return rankViews.size();
		}

		@Override
		public Object getItem(int position) {
			return rankViews.get(position);
		}

		@Override
		public long getItemId(int position) {
			return rankViews.hashCode();
		}
		int chooseRankImg(int position){
			switch(Integer.parseInt(rankViews.get(position).getRank())){
			case 1:
				return R.drawable.ranksixth;//第一名
			case 2:
				return R.drawable.rankfifth;
			case 3:
				return R.drawable.rankforth;
			case 4:
				return R.drawable.rankthird;
			case 5:
				return R.drawable.ranksecond;
			}
			return R.drawable.rankfirst;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.match_list_basic, null);
				rankImg = (ImageView)convertView.findViewById(R.id.match_rankImg);
				nameView = (TextView)convertView.findViewById(R.id.match_name);
				scoreView = (TextView)convertView.findViewById(R.id.match_grade);
			}
			rankImg.setImageResource(chooseRankImg(position));
			nameView.setText(rankViews.get(position).getName());
			scoreView.setText(rankViews.get(position).getScore());	
			return convertView;
		}
		
	}
}
