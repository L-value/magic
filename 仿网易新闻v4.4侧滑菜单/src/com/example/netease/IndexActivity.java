package com.example.netease;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IndexActivity extends Activity{
	
	private ImageView img;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Intent intent = new Intent();
			intent.setClass(IndexActivity.this, MainActivity.class);
			IndexActivity.this.startActivity(intent);
			finish();
		}
	};
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_index);
			img = (ImageView) findViewById(R.id.indexImg);
			Animation animation = AnimationUtils.loadAnimation(this,R.anim.launch_from);
			img.setAnimation(animation);
			Thread thread = new Thread(){
				public void run() {
					try
					{
						Thread.sleep(1500);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					handler.sendMessage(Message.obtain());
				};
			};
			thread.start();
	}
}
