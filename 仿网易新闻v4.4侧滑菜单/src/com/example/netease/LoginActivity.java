package com.example.netease;

import java.io.File;
import java.io.IOException;

import com.picture.utils.PrefUtils;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private TextView register, login, tvForgot;
	private EditText etEmail, etPassword;
	private OkHttpClient mOkHttpClient;
	private PrefUtils prefUtils;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			System.out.println(msg.obj);
			if (msg.what == 1) {
					prefUtils.putBoolean("login", true);
					prefUtils.putString("username", etEmail.getText().toString());
					prefUtils.putString("password", etPassword.getText().toString());
					prefUtils.putString("id", (String) msg.obj);
					prefUtils.commit();
					finish();
			}
		};
	};


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		prefUtils = new PrefUtils(this);
		register = (TextView) findViewById(R.id.register);
		login = (TextView) findViewById(R.id.login);
		tvForgot = (TextView) findViewById(R.id.tvForgot);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		mOkHttpClient = new OkHttpClient();
	    login.setOnClickListener(this);
	
	}

	@Override
	public void onClick(View v) {
		
		 RequestBody requestBody = new FormBody.Builder()
         .add("login-form[login]", etEmail.getText().toString())
         .add("login-form[password]", etPassword.getText().toString())
         .build();
	     String url = "http://test.nsscn.org/helloYii/web/index.php?r=user/security/login-app";
	     Request request = new Request.Builder().url(url).post(requestBody).build();
	     mOkHttpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
				Message message = Message.obtain();
				message.what = 1;
				Log.i("info", "message" + arg0.message());
			
				message.obj = arg0.body().string();
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				
			}
		});
	}

}
