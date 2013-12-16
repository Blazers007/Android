package com.example.sias_protype;

import java.io.IOException;
import java.io.InputStream;
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
import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private int chooseHeadNum = 1;
	private Spinner headSpinner = null;
	private MyHeadAdapter headAdapter = null;
	private List<MyHeadView> headData = new ArrayList<MyHeadView>();
	/////////////////////////////////
	private String result = "Failed"; //���ڷ����ϲ�act ���
	private String ID = "";
	private EditText nameView = null; //�ǳ� ǩ�� ��� ���� ����һ������������ �Լ�������
	private EditText signView = null;
	private EditText heightView = null;
	private EditText weightView = null;
	private RatingBar skinRatingBar = null;
	private TextView skinRankView = null;
	private String[][] skinRankList = {{"�Ҳ��̶�","��ǿ����","���п�ȡ","�е�ƫ��","С���ɫ","�Ҳ��̶�","��ǿ����","���п�ȡ","�е�ƫ��","С���ɫ","С���ɫ"},
			{"�Ҳ��̶�","��ǿ����","���п�ȡ","�е�ƫ��","С���ɫ","�Ҳ��̶�","��ǿ����","���п�ȡ","�е�ƫ��","С���ɫ","С���ɫ"}};
	private RatingBar faceRatingBar = null;
	private TextView faceRankView = null;
	private String[][] faceRankList = {{"0��?�Ҳ�!˵���ӵĳ���?����������Ѱ��·!","1���Ќ�","2��2B","3�ֲ���","4�ֻ���","5���е�","6���Ԍ�","����Լ�7�ֻ����...","8�ֲ���","9�ֺ���","ʮ�ֺǺ�"},
			{"0�ֲ�л","1�֌�˿","2��2B","3�ֲ���","4�ֻ���","5���е�","6���Ԍ�","7�ֲ���","8�ֲ���","9�ֺ�X","10����ߣ"}};
	
	private RadioGroup sexGroup = null;
	private RadioButton maleButton = null;
	private RadioButton femaleButton = null;
	private Switch extendProfile = null;
	private MediaPlayer mp = new MediaPlayer();//��������ȫ�ֵĲ������� ������򲥷���ֹͣ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����Ϊȫ��
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		setContentView(R.layout.activity_register);
		
		Intent intent = getIntent();
		ID = intent.getStringExtra("ID");
//		ID = "20091814114";
//		System.out.println(ID);
		for(int i = 1 ; i<= 20 ; i ++){
			MyHeadView v1 = new MyHeadView(i);
			System.out.print("adding");
			headData.add(v1);
		}
		headSpinner = (Spinner)findViewById(R.id.Register_HeadSpinner);
		headAdapter = new MyHeadAdapter(headData);
		headSpinner.setAdapter(headAdapter);
		headSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				chooseHeadNum = arg2 + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		///////////////////////////////
		nameView = (EditText) findViewById(R.id.Register_Name);
		signView =(EditText) findViewById(R.id.Register_Sign);
		heightView = (EditText)findViewById(R.id.Register_Height);
		weightView = (EditText)findViewById(R.id.Register_Weight);
		
		sexGroup = (RadioGroup)findViewById(R.id.ProfileSexRadioGroup);//�������ü���
		maleButton = (RadioButton)findViewById(R.id.ProfileMaleRadio);
		femaleButton = (RadioButton)findViewById(R.id.ProfileFemaleRadio);
		
		skinRatingBar = (RatingBar)findViewById(R.id.Register_SkinRating);
		faceRatingBar = (RatingBar)findViewById(R.id.Register_FaceRatingr);
		skinRankView = (TextView)findViewById(R.id.ProfileSkinRank);
		faceRankView = (TextView)findViewById(R.id.ProfileFaceRank);
		
		skinRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){
			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				int rating1 = (int)(skinRatingBar.getRating()*2);
				playFaceSound(rating1,mp);
				skinRankView.setText(skinRankList[0][rating1]);
			}
			
		});
		
		faceRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				int rating2 = (int)(faceRatingBar.getRating()*2);
				playFaceSound(rating2,mp);
				faceRankView.setText(faceRankList[0][rating2]);
			}
		});
		
		extendProfile = (Switch)findViewById(R.id.ProfileSwitch);
		extendProfile.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				MediaPlayer mp = null;
				LinearLayout layout = (LinearLayout)findViewById(R.id.ProfileExtend);
				if(isChecked){
					mp = MediaPlayer.create(RegisterActivity.this, R.raw.profile0extend);
					try {
						mp.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mp.start();
					layout.setVisibility(View.VISIBLE);
				}else{
					layout.setVisibility(View.GONE);
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	@Override
	public void finish() {
		Intent back = new Intent();
		back.putExtra("Result", result);
		setResult(1,back);//1��ʾ��REG����
		super.finish();
	}

	//////////////���Ʋ������� 
	void playFaceSound(int n,MediaPlayer mp){
		switch(n){
		case 0:
//			if(mp.isPlaying())
//				mp.stop();
			mp = MediaPlayer.create(RegisterActivity.this	,R.raw.profile0face00);
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.start();
			break;
		case 7:
//			if(mp.isPlaying())
//				mp.stop();
			mp = MediaPlayer.create(RegisterActivity.this	,R.raw.profile0face07);
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.start();
			break;
		case 10:
//			if(mp.isPlaying())
//				mp.stop();
			mp = MediaPlayer.create(RegisterActivity.this	,R.raw.profile0face10);
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.start();
			break;
			default :
				return;
		}
	}
	
	//������е������Ƿ�Ϸ� �ǻ��������
	
	Handler threadHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				AllInfomation allInfo = (AllInfomation)getApplication();
				allInfo.setRegState(true);
				allInfo.setWelComeName(nameView.getText().toString());
				Toast.makeText(RegisterActivity.this, "ע��ɹ���", 2000).show();
				result = "Success";
				Intent intent = new Intent(RegisterActivity.this,ProfileActivity.class);
				intent.putExtra("ID", ID);
				startActivity(intent);
				break;
			case 2:
				Toast.makeText(RegisterActivity.this, "ע��ʧ��������", 2000).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	//��������ϴ�ע����Ϣ  �����ݷ������ķ������� msg ����handler ����
	public void submit(View view){
		if(nameView.getText().toString().isEmpty()||signView.getText().toString().isEmpty()||heightView.getText().toString().isEmpty()||weightView.getText().toString().isEmpty()){
			Toast.makeText(RegisterActivity.this, "��������������Ϣ!",1000).show();
		}else{
			String msg ="ͷ��: "+chooseHeadNum+"\n"+
						"�ǳ�: "+nameView.getText().toString()+"\n"+
						"ǩ��: "+signView.getText().toString()+"\n"+
						"���: "+heightView.getText().toString()+"\n"+
						"����: "+weightView.getText().toString();
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("ȷ����Ϣ")
			.setMessage(msg)
			.setNegativeButton("ȡ��", null)
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new Thread(registerOnServer).start();
				}
			})
			.create()
			.show();
		}
	}
	
	Runnable registerOnServer = new Runnable(){ 
		@Override
		public void run() {
			int msgWhat = 1; //���󷵻�0
			//01.��װjson����
			String headImg = "0";//Ĭ��Ϊ0
			String sex = null;
			if(maleButton.isChecked())
				sex = "1";
			else
				sex = "0";
			JSONObject json = new JSONObject();
			try {
				json.put("Head", chooseHeadNum);
				json.put("Title","����");
				json.put("Name", nameView.getText().toString());//�����ݷ�װ�� JSON ��ʽת��Ϊ string ���������� ����˽��� json ����
				json.put("Sign",signView.getText().toString());
				json.put("Height", heightView.getText().toString());  //������Ӧ���������ͣ� Ӧ���ǰ�
				json.put("Weight", weightView.getText().toString());
				json.put("SkinRating", (int)(skinRatingBar.getRating()*2));
				json.put("FaceRating", (int)(faceRatingBar.getRating()*2));    //�������ƽ�����ķ��� (sum += submit) / (number ++)
				json.put("ID",ID);
				json.put("Sex", sex);
				json.put("HeadImg", headImg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block  �κλ��ڳ���Ҫֱ�ӷ��أ�����
				e.printStackTrace();
			}
			String sendMsg = json.toString(); //��Ҫ���͵��ַ������� ���ַ��� �� �ֽ���������
			
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://192.168.163.1:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","Register")); //���� ע����Ϣ
			params.add(new BasicNameValuePair("Info",sendMsg));//������Ϣ ��û��������ƣ�������
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
	
	public void clickReturn(View view){
		finish();
	}
	
	class MyHeadView{
		private int headImg;
		public MyHeadView(int headImg){
			this.headImg = headImg;
		}
		
		public int getHeadImg(){
				switch(headImg){
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
				return R.raw.hp1;
			}
	}
	
	class MyHeadAdapter extends BaseAdapter{

		private ImageView imageView;
		private List<MyHeadView> headViews;
		public MyHeadAdapter(List<MyHeadView> headViews){
			this.headViews = headViews;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return headViews.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return headViews.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return headViews.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView =inflater.inflate(R.layout.register_head_spinner, null);
				imageView = (ImageView)convertView.findViewById(R.id.Register_Spinner_HeadPhoto);
			}
			imageView.setImageResource(headViews.get(position).getHeadImg());
			return convertView;
		}
		
	}

}
