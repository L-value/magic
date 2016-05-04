package com.picture.fragements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import lib.MultiColumnListView;
import lib.internal.PLA_AdapterView;





import com.example.imageloader.CommuntiyAdapter;
import com.example.imageloader.ImageAdapter;
import com.example.imageloader.ThemeAdapter;
import com.example.imageloader.TitleAdapter;
import com.example.netease.GetImageActivity;
import com.example.netease.R;
import com.example.netease.Tag;
import com.example.netease.UserActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.picture.bean.Quality;
import com.picture.bean.Theme;
import com.picture.myviews.HorizontialListView;
import com.picture.myviews.RoundedImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragement extends Fragment {
	private MultiColumnListView multicolumnlist;
	private ImageAdapter multAdapter;
	private HorizontialListView hor01;
	private ThemeAdapter themeAdapter1;
	private TitleAdapter titleAdapter;
	private HorizontialListView hor02;
	private Gson gson;
	private List<Quality> qualities;
	private List<Theme> themes;
	private List<Tag> tags;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		
			if (msg.what == 1) {
				qualities = gson.fromJson((String) msg.obj,
						new TypeToken<List<Quality>>() {
						}.getType());
				multAdapter = new ImageAdapter(getActivity(), multicolumnlist,
						qualities);
				multicolumnlist.setAdapter(multAdapter);
			}
			if (msg.what == 2) {
				themes = gson.fromJson((String) msg.obj,
						new TypeToken<List<Theme>>() {
						}.getType());
				themeAdapter1 = new ThemeAdapter(getActivity(), themes);
				hor02.setAdapter(themeAdapter1);
			}
			if(msg.what == 3){
				tags = gson.fromJson((String)msg.obj, new TypeToken<List<Tag>>() {
				}.getType());
				for(Tag tag : tags){
					System.out.println(tag.getHeadportrait()+tag.getId()+tag.getUsername());
				}
				titleAdapter = new TitleAdapter(getActivity(), tags);
				hor01.setAdapter(titleAdapter);
				hor01.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(getActivity(),UserActivity.class);
						intent.putExtra("userName", tags.get(position).getUsername());
						intent.putExtra("imgurl", tags.get(position).getHeadportrait());
						getActivity().startActivity(intent);
					}
				});
				
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragement_home, null);
		gson = new Gson();
		multicolumnlist = (MultiColumnListView) view.findViewById(R.id.list);
		// multAdapter = new ImageAdapter(getActivity(), multicolumnlist,
		// qualities);
		// multicolumnlist.setAdapter(multAdapter);
		hor01 = (HorizontialListView) view.findViewById(R.id.hor01);
		hor02 = (HorizontialListView) view.findViewById(R.id.hor02);
		// 创建okHttpClient对象
		OkHttpClient mOkHttpClientt = new OkHttpClient();
		// 创建一个Request
		final Request requestt = new Request.Builder()
				.url("http://test.nsscn.org/helloYii/web/index.php?r=site/get-attention-list-by-tag&tag=文艺派")
				.build();
		// new call
		Call calll = mOkHttpClientt.newCall(requestt);
		// 请求加入调度
		calll.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(final Response response) throws IOException {
				// String htmlStr = response.body().string();
				Message message = Message.obtain();
				message.what = 3;
				message.obj = response.body().string();
				handler.sendMessage(message);
			}
		});
		// 创建okHttpClient对象
		OkHttpClient mOkHttpClient = new OkHttpClient();
		// 创建一个Request
		final Request request = new Request.Builder()
				.url("http://test.nsscn.org/helloYii/web/index.php?r=boutique/get-boutique")
				.build();
		// new call
		Call call = mOkHttpClient.newCall(request);
		// 请求加入调度
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(final Response response) throws IOException {
				// String htmlStr = response.body().string();
				Message message = Message.obtain();
				message.what = 1;
				message.obj = response.body().string();
				handler.sendMessage(message);
			}
		});
		OkHttpClient mOkHttpClient1 = new OkHttpClient();
		final Request request2 = new Request.Builder()
				.url("http://test.nsscn.org/helloYii/web/index.php?r=recommended/get-recommended")
				.build();
		Call call1 = mOkHttpClient1.newCall(request2);
		// 请求加入调度
		call1.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(final Response response) throws IOException {
				// String htmlStr = response.body().string();
				Message message = Message.obtain();
				message.what = 2;
				message.obj = response.body().string();
				handler.sendMessage(message);
			}
		});
		return view;
	}
	// http://localhost/helloYii/web/index.php?r=site/get-attention-list-by-tag&tag=文艺派
	// http://test.nsscn.org/helloYii/web/index.php?r=boutique/get-boutique


}



