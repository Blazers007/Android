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
import org.json.JSONException;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {

	private AlertDialog waitAlert = null;
	int reSendACKTimes = 0;
	private String toID = "";
	private String ID = "";
	private String replayMsg = "";
	private ListView messageListView = null;
	private ArrayList<HashMap<String,String>> message = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.activity_message);
		
		Intent intent = getIntent();//��ȡ�������� ��ѡ���Ƿ�ӱ��ض�ȡ
		AllInfomation allInfo = ((AllInfomation)getApplication());
		ID = allInfo.getUser();//�ӱ��ػ�ȡID��
		message = allInfo.getMessage();
		
		
		messageListView = (ListView)findViewById(R.id.Message_ListView);
		adapter = new SimpleAdapter(MessageActivity.this,message,
				R.layout.message_list_layout,
				new String[]{"Date","From","Msg"},//��Ͷ��λ�ò���
				new int[]{R.id.Message_Date,R.id.Message_From,R.id.Message_Msg});
		messageListView.setAdapter(adapter);
		messageListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int selectId = arg2;
				LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View popView =inflater.inflate(R.layout.message_detail_layout,null);
				TextView msgView = (TextView)popView.findViewById(R.id.MessageDetail_Msg);
				final EditText reView = (EditText)popView.findViewById(R.id.MessageDetail_Replay);
				
				msgView.setText(message.get(selectId).get("Msg"));
				toID = message.get(selectId).get("From");
				new AlertDialog.Builder(MessageActivity.this)
				.setTitle("���Ͽ�")
				.setView(popView)
				.setNegativeButton("�ر�", null)
				.setPositiveButton("�ظ�", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						replayMsg = reView.getText().toString();
						new Thread(sendMsg).start();
						waitAlert.dismiss();
					}
				})
				.create()
				.show();
			}
		});
		
		String readFromLocal = intent.getStringExtra("Read");
		if(readFromLocal.equals("true")){
			Toast.makeText(MessageActivity.this, "�ӱ��ض�ȡ��", 2000).show();
//			message.clear();
			adapter.notifyDataSetChanged();
		}
//		else{
//			new Thread(queryMsg).start();
//		}
		
		LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View waitAlertView =inflater.inflate(R.layout.wait_window, null);
		waitAlert = new AlertDialog.Builder(MessageActivity.this)
		.setView(waitAlertView)
		.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}
	
	///////����¼�
	public void updateMsg(View view){
		new Thread(queryMsg).start();
		waitAlert.show();
	}
	
	public void clearMsg(View view){
		message.clear();
		adapter.notifyDataSetChanged();
	}
	
	///////////////////////////////////////��ѯ��Ϣ�߳� 
	//��ѯ����
	Runnable queryMsg = new Runnable(){  //15

		@Override
		public void run() {
			int msgWhat = 15;
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","QueryMsg")); //���� ע����Ϣ
			params.add(new BasicNameValuePair("ID",ID));//������Ϣ ��û��������ƣ�������
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
					message.clear();//��գ�
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
							HashMap<String,String> map = new HashMap<String,String>();
							map.put("Date", jsonArray.getJSONObject(i).getString("Date"));
							map.put("From", jsonArray.getJSONObject(i).getString("From"));
							map.put("Msg", jsonArray.getJSONObject(i).getString("Msg"));
//							map.put("Date", jsonArray.getJSONObject(i).getString("Date"));
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
	//�ظ��ʼ�
	Runnable sendMsg = new Runnable(){ //16

		@Override
		public void run() {
			int msgWhat = 16;
			JSONObject json = new JSONObject();
			try {
				json.put("From", ID);
				json.put("To", toID);
				json.put("Type", "Chat");
				json.put("Msg", replayMsg); //��ȡ�������е����� ������
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sendMsg = json.toString();
			
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost("http://106.3.44.26:8080/ServerForSias/search121736");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type","SendMsg")); //���� ע����Ϣ
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
						msgWhat = 14;
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
				params.add(new BasicNameValuePair("ID",ID));//������Ϣ ��û��������ƣ�������
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
	////////////////////////handler ����
	Handler threadHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 13://û����Ϣ
				waitAlert.dismiss();
				Toast.makeText(MessageActivity.this, "��û���µ���Ϣ��ע��һ����Diao si...", 2000).show();
				break;
			case 14://failed
				replayMsg = "";
				toID = "";
				waitAlert.dismiss();
				Toast.makeText(MessageActivity.this, "���ӷ�����ʧ�ܣ�������ǣ���������������������Ƿ���������...", 2000).show();
				break;
			case 15://query Success!
				adapter.notifyDataSetChanged();
				waitAlert.dismiss();
				Toast.makeText(MessageActivity.this, "��Ϣ�б���³ɹ���", 2000).show();
				new Thread(sendACK).start();
				break;
			case 16://send Success!
				replayMsg = "";
				toID = "";
				Toast.makeText(MessageActivity.this, "������Ϣ�ɹ���", 2000).show();
				break;
			case 17://ack failed!
				reSendACKTimes ++;
				if(reSendACKTimes > 10){
					Toast.makeText(MessageActivity.this, "����ACKʧ�ܳ���10��", 2000);
					reSendACKTimes = 0;
				}
				else
					new Thread(sendACK).start();//�ظ�����..
				break;
			case 18 ://ack success!
				Toast.makeText(MessageActivity.this, "Send ACK Success!", 2000).show();
			}
			super.handleMessage(msg);
		}
		
	};

}
