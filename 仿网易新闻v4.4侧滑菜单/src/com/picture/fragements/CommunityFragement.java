package com.picture.fragements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import cn.jpush.android.api.JPushInterface;

import com.example.imageloader.CommuntiyAdapter;
import com.example.netease.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.picture.bean.Community;
import com.picture.myviews.PullToRefreshView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityFragement extends Fragment {
	public static final int REFRESH_DELAY = 3000;
	private PullToRefreshView mPullToRefreshView;
	private ListView listView;
	private CommuntiyAdapter communtiyAdapter;
	private List<Community> communities;
	private Gson gson;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				System.out.println(msg.obj);
				communities = gson.fromJson((String) msg.obj, new TypeToken<List<Community>>(){}.getType()
				);
				if (bundle != null) {
					Community community = new Community();
					community.setDate("0000-00-00");
					community.setId("8");
					community.setImg("----");
					community.setTitle(bundle.getString(JPushInterface.EXTRA_ALERT));
					communities.add(community);
				}
				communtiyAdapter = new CommuntiyAdapter(getActivity(), listView, communities);
				listView.setAdapter(communtiyAdapter);
			}
			if(msg.what == 2){
				System.out.println(msg.obj);
				communities = gson.fromJson((String) msg.obj, new TypeToken<List<Community>>(){}.getType()
				);
				communtiyAdapter = new CommuntiyAdapter(getActivity(), listView, communities);
				listView.setAdapter(communtiyAdapter);
				mPullToRefreshView.setRefreshing(false);
				Toast.makeText(getActivity(), "刷新成功", 800).show();
			}
			
		};
	};

	
	private Bundle bundle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragement_community, null);
		bundle = getArguments();
		System.out.println("bundle is" + bundle);
		gson = new Gson();
		//创建okHttpClient对象
		OkHttpClient mOkHttpClient = new OkHttpClient();
		//创建一个Request
		final Request request = new Request.Builder()
		                .url("http://test.nsscn.org/helloYii/web/index.php?r=community/get-community")
		                .build();
		//new call
		Call call = mOkHttpClient.newCall(request); 
		//请求加入调度
		call.enqueue(new Callback()
		        {
		            @Override
		            public void onFailure(Request request, IOException e)
		            {
		            	System.out.println(request+"fail");
		            }

		            @Override
		            public void onResponse(final Response response) throws IOException
		            {
		            	Message message = Message.obtain();
		            	message.what = 1;
		            	message.obj = response.body().string();
		            	handler.sendMessage(message);
		            }
		        });
		listView = (ListView) view.findViewById(R.id.list_view);
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pull_to_refresh);
		mPullToRefreshView
				.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
					@Override
					public void onRefresh() {
						//创建okHttpClient对象
						OkHttpClient mOkHttpClient = new OkHttpClient();
						//创建一个Request
						final Request request = new Request.Builder()
						                .url("http://test.nsscn.org/helloYii/web/index.php?r=community/get-community")
						                .build();
						//new call
						Call call = mOkHttpClient.newCall(request); 
						//请求加入调度
						call.enqueue(new Callback()
						        {
						            @Override
						            public void onFailure(Request request, IOException e)
						            {
						            	System.out.println(request+"fail");
						            }

						            @Override
						            public void onResponse(final Response response) throws IOException
						            {
						            	Message message = Message.obtain();
						            	message.what = 2;
						            	message.obj = response.body().string();
						            	handler.sendMessage(message);
						            }
						        });
					}
				});
		return view;
	}

	
}
