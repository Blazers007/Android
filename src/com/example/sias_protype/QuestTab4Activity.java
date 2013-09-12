package com.example.sias_protype;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class QuestTab4Activity extends Activity {

	private CheckBox showReturn = null;
	
	private Spinner dstSpinner = null;
	private ArrayAdapter<String> dstAdapter;
	private static String[] dst = {"֣��","����","����","����"};
	private ListView waysListView = null;
	
	private int selectId = 0;
//	private List<ArrayList> wayListSum = new ArrayList<ArrayList>();
	private ArrayList<HashMap<String,String>> waysListMapArrayList = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter listAdapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quest_tab4);
		//spinner����
		dstSpinner = (Spinner)findViewById(R.id.Quest4_Spinner);
		dstAdapter = new ArrayAdapter<String>(QuestTab4Activity.this,android.R.layout.simple_spinner_item,dst);
		dstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dstSpinner.setAdapter(dstAdapter);
		
		//����checkbox
		showReturn = (CheckBox)findViewById(R.id.Quest4_ShowReturn);
		//����LISTVIEW
		waysListView = (ListView)findViewById(R.id.Quest4_ListView);
		listAdapter = new SimpleAdapter(QuestTab4Activity.this,waysListMapArrayList,R.layout.quest_tab4_listlayout,
				new String[]{"Time","Tel","Location","Cost"},
				new int[]{R.id.Quest4_Time,R.id.Quest4_Tel,R.id.Quest4_Location,R.id.Quest4_Cost});
		waysListView.setAdapter(listAdapter);
		///���ü���
		dstSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
//				waysListMapArrayList.clear();//���Ȼ��ֵ
				if(!showReturn.isChecked()){
					Toast.makeText(QuestTab4Activity.this, "��ʾ�س� "+dst[arg2], 2000).show();
					selectId = arg2;
					putWaysGoMap(arg2);//�ı����
				}
				else{
					Toast.makeText(QuestTab4Activity.this, "��ʾ���� "+dst[arg2], 2000).show();
					selectId = arg2;
					putWaysBackMap(arg2);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		showReturn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					Toast.makeText(QuestTab4Activity.this, "��ʾ�س� "+dst[selectId], 2000).show();
					putWaysGoMap(selectId);//�ı����
				}
				else{
					Toast.makeText(QuestTab4Activity.this, "��ʾ���� "+dst[selectId], 2000).show();
					putWaysBackMap(selectId);
				}
			}
		});
	}

	void putWaysGoMap(int i){//����Ϣװ���ڴ�
		switch(i){
		case 0: //A֣��
			break;
		case 1:  //B����
			break;
		case 2: //C����
			waysListMapArrayList.clear();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("Time", "05:10");map.put("Tel", "13838208583");map.put("Location", "����˹����");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "06:20");map.put("Tel", "13608677691");map.put("Location", "����˹����");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "07:30");map.put("Tel", "13838384053");map.put("Location", "����˹����");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "10:00");map.put("Tel", "13937126281");map.put("Location", "����˹����");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "13:40");map.put("Tel", "15036182599");map.put("Location", "����˹����");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "16:10");map.put("Tel", "15036182566");map.put("Location", "����˹����");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
	
			listAdapter.notifyDataSetChanged();
			break;
		case 3:// xinxiang ?
			break;
		}
	}
	
	void putWaysBackMap(int i){//����Ϣװ���ڴ�
		switch(i){
		case 0: //A֣��
			break;
		case 1:  //B����
			break;
		case 2: //C����
			waysListMapArrayList.clear();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("Time", "07:20");map.put("Tel", "15036182566");map.put("Location", "��������վ");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "09:00");map.put("Tel", "13838208583");map.put("Location", "��������վ");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "11:30");map.put("Tel", "13608677691");map.put("Location", "��������վ");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "13:00");map.put("Tel", "13838384053");map.put("Location", "��������վ");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "15:00");map.put("Tel", "13937126281");map.put("Location", "��������վ");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			map = new HashMap<String,String>();
			map.put("Time", "18:00");map.put("Tel", "15036182599");map.put("Location", "��������վ");map.put("Cost", "50~60");
			waysListMapArrayList.add(map);
			listAdapter.notifyDataSetChanged();
			break;
		case 3:// xinxiang ?
			break;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quest_tab4, menu);
		return true;
	}
	
	public void returnMain(View view){
		Intent intent = new Intent(QuestTab4Activity.this,MainActivity.class);
		startActivity(intent);
	}
	
}
