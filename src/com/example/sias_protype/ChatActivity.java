package com.example.sias_protype;

import java.util.ArrayList;
import java.util.List;

import com.example.sias_protype.WeiboActivity.WeiboAdapter;
import com.example.sias_protype.WeiboActivity.WeiboInformation;
import com.laohuai.appdemo.customui.ui.MyListView;
import com.laohuai.appdemo.customui.ui.MyListView.OnRefreshListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatActivity extends Activity {

	private WeiboAdapter adapter = null;
	private List<WeiboInformation> weiboInformation = new ArrayList<WeiboInformation>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		final MyListView listView = (MyListView) findViewById(R.id.Chat_listView);
		WeiboInformation w1 = new WeiboInformation("Blazers","Test","2013-08-19","1");
		WeiboInformation w2 = new WeiboInformation("张三","呵呵呵呵呵呵呵","2013-08-19","2");
		WeiboInformation w3 = new WeiboInformation("王五","楼上的额呵呵呵呵呵呵呵呵呵呵，呵呵呵呵呵呵呵","2013-08-19","3");
		final WeiboInformation w0 = new WeiboInformation("测试人员真苦逼","谁打呵呵俩字谁2  \n  真心2\n","2013-08-19","10");
		weiboInformation.add(w1);
		weiboInformation.add(w2);
		weiboInformation.add(w3);
		
		adapter = new WeiboAdapter(weiboInformation);
		
		listView.setAdapter(adapter);

		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						weiboInformation.add(w0);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
					}

				}.execute();
			}
		});
	}
	
	class WeiboInformation{
		private String name;
		private String msg;
		private String date;
		private String headNo;
		public WeiboInformation(String name, String msg, String date,
				String headNo) {
			super();
			this.name = name;
			this.msg = msg;
			this.date = date;
			this.headNo = headNo;
		}
		public String getName() {
			return name;
		}
		public String getDate() {
			return date;
		}
		public String getMsg() {
			return msg;
		}
		public String getHeadNo() {
			return headNo;
		}
	}
	
	
	class WeiboAdapter extends BaseAdapter{

		private List<WeiboInformation> weiboInfoList;
		private ImageView headView;
		private TextView nameView;
		private TextView msgView;
		private TextView dateView;
		
		public WeiboAdapter(List<WeiboInformation> weiboInfoList){
			this.weiboInfoList = weiboInfoList;
		}
		@Override
		public int getCount() {
			return weiboInfoList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return weiboInfoList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return weiboInfoList.get(position).hashCode();
		}
		
		public int chooseHead(int arg0){
			switch(Integer.parseInt(weiboInfoList.get(arg0).getHeadNo())){
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
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView =inflater.inflate(R.layout.weibo_item, null);
				headView = (ImageView)convertView.findViewById(R.id.Weibo_Item_Head);
				nameView = (TextView)convertView.findViewById(R.id.Weibo_Item_Name);
				msgView = (TextView)convertView.findViewById(R.id.Weibo_Item_Msg);
				dateView = (TextView)convertView.findViewById(R.id.Weibo_Item_Date);
			}
			headView.setImageResource(chooseHead(position));
			nameView.setText(weiboInfoList.get(position).getName());
			msgView.setText(weiboInfoList.get(position).getMsg());
			dateView.setText(weiboInfoList.get(position).getDate());
			return convertView;
		}
		
	}
}