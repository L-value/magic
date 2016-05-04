package com.picture.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
	private  OkHttpClient client = new OkHttpClient();
	
	
	
	public OkHttpUtils(Map<String, String> maps) {
		super();
		this.maps = maps;
	}
	private Map<String, String> maps = new HashMap<>();
	/*
	 * 上传文件
	 */
	public  void upFile(File fileName){
		 
	}
	/*
	 * POST请求
	 */
	public  void  post(String url){
		client = new OkHttpClient();
		FormBody.Builder builder = new FormBody.Builder();
		for (Map.Entry<String, String> entry : maps.entrySet()) {
			builder.add(entry.getKey(), entry.getValue());
		}
		RequestBody body = builder.build();
		Request request = new Request.Builder().url(url).post(body).build();
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * GET请求
	 */
	public void get(String url){
		client = new OkHttpClient();
		Request request = new Request.Builder().url(url).get().build();
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
}
