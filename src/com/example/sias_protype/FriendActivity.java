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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FriendActivity extends Activity {

	private String ID = ""; //保存用户名
	private int[] imgSrc = {R.raw.hp1,R.raw.hp2,R.raw.hp3,R.raw.hp4,R.raw.hp5,R.raw.hp6,R.raw.hp7,R.raw.hp8,
			R.raw.hp9,R.raw.hp10,R.raw.hp11,R.raw.hp12,R.raw.hp13,R.raw.hp14,R.raw.hp15,R.raw.hp16,R.raw.hp17,R.raw.hp18,R.raw.hp19,R.raw.hp20};
	private FriendListAdapter myAdapter = null;
	private List<FriendView> myListData = new ArrayList<FriendView>();
	//////////////////////////////////////////////
	private AlertDialog waitAlert = null;
	private ArrayList<HashMap<String,String>> friendListMapAdapter = new ArrayList<HashMap<String,String>>();
	private ListView friendListView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
		setContentView(R.layout.activity_friend);
		
		//获取intent
		Intent intent = getIntent();
		boolean autoRefresh = intent.getBooleanExtra("Auto", true);
		ID = intent.getStringExtra("ID");
		
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(FriendActivity.this)
		.setView(waitAlertView)
		.create();
		
		new Thread(queryFriend).start();
		
		friendListView = (ListView)findViewById(R.id.Friend_FriendList);

		myAdapter = new FriendListAdapter(myListData);
		friendListView.setAdapter(myAdapter);

		friendListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View popView =inflater.inflate(R.layout.friendlist_detail_layout,null);
				ImageView headView = (ImageView)popView.findViewById(R.id.FriendDetail_Head);
				TextView titleView = (TextView)popView.findViewById(R.id.FriendDetail_Title);
				TextView levelView = (TextView)popView.findViewById(R.id.FriendDetail_Level);
				TextView signView = (TextView)popView.findViewById(R.id.FriendDetail_Sign);
				headView.setImageResource(imgSrc[Integer.parseInt(friendListMapAdapter.get(arg2).get("Head"))-1]);
				titleView.setText(friendListMapAdapter.get(arg2).get("Title"));
				levelView.setText(friendListMapAdapter.get(arg2).get("Level"));
				signView.setText(friendListMapAdapter.get(arg2).get("Sign"));
				final int selectId = arg2;
				new AlertDialog.Builder(FriendActivity.this)
				.setTitle("用户卡")
				.setView(popView)
				.setPositiveButton("详细信息", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(FriendActivity.this,ProfileActivity.class);
						intent.putExtra("AutoFind", false);
						intent.putExtra("Head", friendListMapAdapter.get(selectId).get("Head"));
						intent.putExtra("ID", friendListMapAdapter.get(selectId).get("ID"));
						System.out.println(friendListMapAdapter.get(selectId).get("ID"));
						intent.putExtra("Name",friendListMapAdapter.get(selectId).get("Name"));
						intent.putExtra("Level",friendListMapAdapter.get(selectId).get("Level"));
						intent.putExtra("Title",friendListMapAdapter.get(selectId).get("Title"));
						intent.putExtra("Exp",friendListMapAdapter.get(selectId).get("Exp"));
						intent.putExtra("Sign",friendListMapAdapter.get(selectId).get("Sign"));
						intent.putExtra("height",friendListMapAdapter.get(selectId).get("Height"));
						intent.putExtra("weight",friendListMapAdapter.get(selectId).get("Weight"));
						intent.putExtra("SkinRating",friendListMapAdapter.get(selectId).get("SkinRating"));
						intent.putExtra("FaceRating",friendListMapAdapter.get(selectId).get("FaceRating"));
						startActivity(intent);
					}
				})
				.setNegativeButton("关闭", null)
				.create()
				.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend, menu);
		return true;
	}

	Handler threadHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				waitAlert.dismiss();
				myAdapter.notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(FriendActivity.this, "failed!", 1000).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	Runnable queryFriend = new Runnable(){
		@Override
		public void run() {
			int msgWhat = 0;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryFriendList"));
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
//					JSONObject jsonArray = new JSONObject(sb.toString());
					JSONArray jsonArray = new JSONArray(sb.toString());
					friendListMapAdapter.clear();//清空两者！
					myListData.clear();
					for(int i = 0 ; i < jsonArray.length(); i ++){
						JSONObject json = jsonArray.getJSONObject(i);
						HashMap<String,String> map = new HashMap<String,String>();
						/////////////////// 构造累 进图片的显示
						FriendView friendView = new FriendView(json.getString("Head"),json.getString("Name"),
								json.getString("ID"),json.getString("Sex"));
						myListData.add(friendView);
						
						System.out.println("一个好友"+json.getString("Head")+json.getString("Name")+
								json.getString("ID")+json.getString("Sex"));
						//////////////////
//						map.put("Rand", json.getString("Rank"));
						map.put("Head", json.getString("Head"));
						map.put("ID", json.getString("ID"));
						map.put("Title", json.getString("Title"));
						map.put("Name",json.getString("Name"));
						map.put("Sign", json.getString("Sign"));
						map.put("Height", json.getString("Height"));
						map.put("Weight", json.getString("Weight"));
						map.put("Level", json.getString("Level"));
						map.put("Exp", json.getString("Exp"));
						map.put("SkinRating", json.getString("SkinRating"));
						map.put("FaceRating", json.getString("FaceRating"));
						friendListMapAdapter.add(map);
					}
				} else {
					System.out.println("connect failed!");
					msgWhat = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
				msgWhat = 1;
			}
			Message msg = new Message();
			msg.what = msgWhat;
			threadHandler.sendMessage(msg);
		}
	};
	
	public void showList(View view){
		new Thread(queryFriend).start();
		waitAlert.show();
	}
	
	public void finishActivity(View view){
		this.finish();
	}
	class FriendView{
		private String headImg;
		private String name;
		private String ID;
		private String sexImg;
		public FriendView(String headImg, String name, String iD, String sexImg) {
			super();
			this.headImg = headImg;
			this.name = name;
			ID = iD;
			this.sexImg = sexImg;
		}
		public String getHeadImg() {
			return headImg;
		}
		public String getName() {
			return name;
		}
		public String getID() {
			return ID;
		}
		public String getSexImg() {
			return sexImg;
		}
		
	}
	class FriendListAdapter extends BaseAdapter{
		
		private List<FriendView> friendViews;	
		private ImageView headView;
		private TextView nameView;
		private TextView IDView;
		private ImageView sexView;
		
		public FriendListAdapter(List<FriendView> friendViews){
			this.friendViews = friendViews;
			System.out.println("构造函数运行中");
		}
		@Override
		public int getCount() {
			return friendViews.size();
		}

		@Override
		public Object getItem(int arg0) {
			return friendViews.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return friendViews.get(arg0).hashCode();
		}
		
		public int chooseHead(int arg0){
			switch(Integer.parseInt(friendViews.get(arg0).getHeadImg())){
			case 1:
				return R.raw.hp1;
			case 2:
				return R.raw.hp2;
			case 3:
				return R.raw.hp3;
			case 4:
				return R.raw.hp4;
			case 5:
				return R.raw.hp5;
			case 6:
				return R.raw.hp6;
			case 7:
				return R.raw.hp7;
			case 8:
				return R.raw.hp8;
			case 9:
				return R.raw.hp9;
			case 10:
				return R.raw.hp10;
			case 11:
				return R.raw.hp11;
			case 12:
				return R.raw.hp12;
			case 13:
				return R.raw.hp13;
			case 14:
				return R.raw.hp14;
			case 15:
				return R.raw.hp15;
			case 16:
				return R.raw.hp16;
			case 17:
				return R.raw.hp17;
			case 18:
				return R.raw.hp18;
			case 19:
				return R.raw.hp19;
			case 20:
				return R.raw.hp20;
			}
			return 0;
		}
		
		public int chooseSex(int arg0){
			switch(Integer.parseInt(friendViews.get(arg0).getSexImg())){
			case 0:
				return R.drawable.gender_female;
			case 1:
				return R.drawable.gender_male;
			}
			return 0;
		}
		@Override
		public View getView(int arg0, View listView, ViewGroup arg2) {
			System.out.println("开始 getView");
			if(listView == null){
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				listView =inflater.inflate(R.layout.friendlist_layout, null);
				headView = (ImageView)listView.findViewById(R.id.FriendListLayout_Head);
				nameView = (TextView)listView.findViewById(R.id.FriendListLayout_Name);
				IDView = (TextView)listView.findViewById(R.id.FriendListLayout_ID);
				sexView = (ImageView)listView.findViewById(R.id.FriendListLayout_Sex);
				System.out.println("init listView");
			}
			System.out.println(friendViews.get(arg0).getName());
			headView.setImageResource(chooseHead(arg0));
			nameView.setText(friendViews.get(arg0).getName());
			IDView.setText(friendViews.get(arg0).getID());
			sexView.setImageResource(chooseSex(arg0));
			return listView;
		}
		
	}
}
