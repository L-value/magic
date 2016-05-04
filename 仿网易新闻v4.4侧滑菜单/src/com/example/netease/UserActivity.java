package com.example.netease;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;











import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserActivity extends Activity{
	private ImageView icon;
	private TextView name;
	private Button attention,sixin;
	private TextView photonum,hotnum,atnum,fansnum;
	private ImageView []imgs = new ImageView[10];
	
	
	private String url;
	private String names;
	private Intent intent;
	
	private Handler handler = new Handler(){
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			
			if (msg.what == 100) {
				icon.setBackground(new BitmapDrawable((Bitmap)msg.obj));
			}else {
				imgs[msg.what].setBackground(new BitmapDrawable((Bitmap)msg.obj));
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		intent = getIntent();
		names = intent.getStringExtra("userName");
		url = intent.getStringExtra("imgurl");
		icon = (ImageView) findViewById(R.id.icon);
		Thread thread = new Thread(){
			@Override
			public void run() {
				URL url1 = null;
				try {
					url1 = new URL(url);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					URLConnection connection = url1.openConnection();
					Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
					Message message = Message.obtain();
					message.obj = bitmap;
					message.what = 100;
					handler.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thread.start();
		name = (TextView) findViewById(R.id.name);
		name.setText(names);
		
		attention = (Button) findViewById(R.id.attention);
		attention.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				attention.setText("已关注");
				fansnum.setText(Integer.valueOf(fansnum.getText().toString())+1+"");
			}
		});
		sixin = (Button) findViewById(R.id.sixin);
		photonum = (TextView) findViewById(R.id.photonum);
		hotnum = (TextView) findViewById(R.id.hotnum);
		atnum = (TextView) findViewById(R.id.atnum);
		fansnum = (TextView) findViewById(R.id.fansnum);
		imgs[0] = (ImageView) findViewById(R.id.img1);
		imgs[1] = (ImageView) findViewById(R.id.img2);
		imgs[2] = (ImageView) findViewById(R.id.img3);
		imgs[3] = (ImageView) findViewById(R.id.img4);
		imgs[4] = (ImageView) findViewById(R.id.img5);
		imgs[5] = (ImageView) findViewById(R.id.img6);
		imgs[6] = (ImageView) findViewById(R.id.img7);
		imgs[7] = (ImageView) findViewById(R.id.img8);
		imgs[8] = (ImageView) findViewById(R.id.img9);
		//创建okHttpClient对象
		OkHttpClient mOkHttpClient = new OkHttpClient();
		//创建一个Request
		final Request request = new Request.Builder()
		                .url("http://test.nsscn.org/helloYii/web/index.php?r=site/go-zone-by-username&username="+names)
		                .build();
		//new call
		Call call = mOkHttpClient.newCall(request); 
		//请求加入调度
		call.enqueue(new Callback()
		        {
		            @Override
		            public void onFailure(Request request, IOException e)
		            {
		            	
		            }

		            @Override
		            public void onResponse(final Response response) throws IOException
		            {
		                    
		            	synchronized (UserActivity.class) {
		            		String htmlStr =  response.body().string();
			            	JSONArray sArray = null;
			            	try {
								sArray = new JSONArray(htmlStr);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	for (int i = 0; i < sArray.length(); i++) {
								try {
									URL url = new URL(sArray.get(i).toString());
									URLConnection connection = url.openConnection();
									Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
									Message message = Message.obtain();
									message.what = i;
									message.obj = bitmap;
									handler.sendMessage(message);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
			            	
						}
		            	
		            }
		        });
	}

}
