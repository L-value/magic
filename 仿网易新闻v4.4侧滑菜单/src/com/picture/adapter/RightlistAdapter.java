package com.picture.adapter;

import com.example.netease.R;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RightlistAdapter extends BaseAdapter{
		
	private Context context;
	private String[] strings;
	
	
	public RightlistAdapter(Context context, String[] strings) {
		super();
		this.context = context;
		this.strings = strings;
	}

	@Override
	public int getCount() {

		return 2;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewholder;
		if(convertView == null){
			view = LayoutInflater.from(context).inflate(R.layout.itme_right, null);
			viewholder = new ViewHolder();
			viewholder.setIcon((ImageView)view.findViewById(R.id.icon));
			viewholder.setText((TextView)view.findViewById(R.id.text));
			view.setTag(viewholder);
		}else {
			view = convertView;
			viewholder = (ViewHolder) view.getTag();
		}
		switch (position) {
		case 0:
			viewholder.getIcon().setBackgroundResource(R.drawable.issueaa);
			viewholder.getText().setText("发布");
			break;
		case 1:
			viewholder.getIcon().setBackgroundResource(R.drawable.around);
			viewholder.getText().setText("周边雷达");
			break;
		default:
			break;
		}
		return view;
	}
	private class ViewHolder{
		private ImageView icon;
		private TextView text;
		public ImageView getIcon() {
			return icon;
		}
		public void setIcon(ImageView icon) {
			this.icon = icon;
		}
		public TextView getText() {
			return text;
		}
		public void setText(TextView text) {
			this.text = text;
		}
		
	}
}
