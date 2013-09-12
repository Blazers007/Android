package com.example.sias_protype;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class QuestTab1Activity extends Activity {

	private ListView myListView = null;
	private MyGroupAdapter groupAdapter = null;
	
	private List<GroupView> DY_Data = new ArrayList<GroupView>();//电院
	private String[] DY_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] DY_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] DY_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<GroupView> SXY_Data = new ArrayList<GroupView>();//商学
	private String[] SXY_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] SXY_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] SXY_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<GroupView> GJY_Data = new ArrayList<GroupView>();//国窖
	private String[] GJY_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] GJY_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] GJY_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<GroupView> FXY_Data = new ArrayList<GroupView>();//法学
	private String[] FXY_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] FXY_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] FXY_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<GroupView> JJY_Data = new ArrayList<GroupView>();//基础
	private String[] JJY_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] JJY_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] JJY_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<GroupView> TY_Data = new ArrayList<GroupView>();//体育
	private String[] TY_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] TY_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] TY_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<GroupView> XC_Data = new ArrayList<GroupView>();//新船
	private String[] XC_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] XC_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] XC_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<GroupView> WY_Data = new ArrayList<GroupView>();//外语
	private String[] WY_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] WY_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] WY_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<GroupView> GJX_Data = new ArrayList<GroupView>();//歌剧系
	private String[] GJX_Name = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] GJX_Charger = {"中文辩论队","通讯部","组织部","记者团","学习部","纪律部","呵呵"};//7ge 
	private String[] GJX_Conn = {"123123123","123123123","123123123","123123123","123123","123123123","123123123"};//7ge 
	
	private List<List<GroupView>> listAll = new ArrayList<List<GroupView>>();
	/////////////////////////////////////////////
	private Spinner collegesSpinner = null;
	private ArrayAdapter<String> collegeAdapter;//9e xi
	private static String[] colleges = {"电子信息工程学院","商学院","国际教育学院","法学院","基础教育学院","体育学院","新闻与传播学院","外语学院","歌剧系"};
	private LinearLayout[] e_list = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_quest_tab1);
		
		myListView = (ListView)findViewById(R.id.Quest1_ListView);
		///////////////////01.填充数据组
		//String groupCollege, String groupName,String groupCharer, String groupConn, String groupState
		listAll.add(DY_Data);	listAll.add(SXY_Data);	listAll.add(GJY_Data);	listAll.add(FXY_Data);	listAll.add(JJY_Data);	listAll.add(TY_Data);
		listAll.add(XC_Data);	listAll.add(WY_Data);	listAll.add(GJX_Data);
		for(int j = 0 ; j < 9 ; j ++ ){
			for(int i = 0 ; i < 7 ; i ++){
				GroupView v = new GroupView(colleges[j],DY_Name[i],DY_Charger[i],DY_Conn[i],"招新中");
				listAll.get(j).add(v);
			}
		}
		
		
		collegesSpinner = (Spinner)findViewById(R.id.Quest1_Spinner);
		collegeAdapter = new ArrayAdapter<String>(QuestTab1Activity.this,android.R.layout.simple_spinner_item,colleges);
		collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		collegesSpinner.setAdapter(collegeAdapter);
		
		
		collegesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				groupAdapter = new MyGroupAdapter(listAll.get(arg2));
				myListView.setAdapter(groupAdapter);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quest_tab1, menu);
		return true;
	}
	
	class GroupView{
		private String groupCollege;
		private String groupName;
		private String groupCharger;
		private String groupConn;
		private String groupState;
		private String groupAbout;
		public GroupView(String groupCollege, String groupName,
				String groupCharer, String groupConn, String groupState) {
			this.groupCollege = groupCollege;
			this.groupName = groupName;
			this.groupCharger = groupCharer;
			this.groupConn = groupConn;
			this.groupState = groupState;
		}
		public String getGroupCollege() {
			return groupCollege;
		}
		public String getGroupName() {
			return groupName;
		}
		public String getGroupCharger() {
			return groupCharger;
		}
		public String getGroupConn() {
			return groupConn;
		}
		public String getGroupState() {
			return groupState;
		}
		public String getGroupAbout() {
			return groupAbout;
		}	
		
		public int chooseImg(){
			return R.drawable.group_button11;
		}
	}
	
	class MyGroupAdapter extends BaseAdapter{

		private List<GroupView> groupViews;
		
		private ImageView groupImg;
		private TextView collegeView;
		private TextView nameView;
		private TextView chargerView;
		private TextView connView;
		private TextView stateView;
		
		public MyGroupAdapter(List<GroupView> groupViews){
			this.groupViews = groupViews;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return groupViews.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return groupViews.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return groupViews.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView =inflater.inflate(R.layout.quest1_list_layout, null);
				groupImg = (ImageView)convertView.findViewById(R.id.Quest1_ListImg);
				collegeView = (TextView)convertView.findViewById(R.id.Quest1_ListCollege);
				nameView = (TextView)convertView.findViewById(R.id.Quest1_ListName);
				chargerView = (TextView)convertView.findViewById(R.id.Quest1_ListCharger);
				connView = (TextView)convertView.findViewById(R.id.Quest1_ListConn);
				stateView = (TextView)convertView.findViewById(R.id.Quest1_ListState);
			}
			groupImg.setImageResource(groupViews.get(position).chooseImg());
			collegeView.setText(groupViews.get(position).getGroupCollege());
			nameView.setText(groupViews.get(position).getGroupName());
			chargerView.setText(groupViews.get(position).getGroupCharger());
			connView.setText(groupViews.get(position).getGroupConn());
			stateView.setText(groupViews.get(position).getGroupState());
			return convertView;
		}
		
	}

}
