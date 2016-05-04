package com.picture.adapter;

import com.example.netease.MainActivity;
import com.example.netease.R;
import com.picture.listener.java.HeadListener;
import com.picture.myviews.RoundedImageView;
import com.picture.utils.GalleryUtils;
import com.picture.utils.PrefUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LeftlistAdapter extends BaseAdapter {
	private Context context;
	private HeadListener headListener;
	private RoundedImageView portiait;
	
	public LeftlistAdapter(Context context,HeadListener headListener) {
		super();
		this.context = context;
		this.headListener = headListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public static boolean click = false;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (position == 0) {
			view = LayoutInflater.from(context).inflate(R.layout.item_left_1,
					null);
			portiait = (RoundedImageView) view.findViewById(R.id.portiait);
			portiait.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					headListener.photoClick(portiait);
				}
			});
			if (click) {
				MainActivity.loadRoundImage(context, MainActivity.mCapturedImageUri.toString(), portiait);
			}
		//	Toast.makeText(context, portiait.getBackground().toString(), 800).show();
			TextView name = (TextView) view.findViewById(R.id.name);
			TextView address = (TextView) view.findViewById(R.id.address);
			PrefUtils prefUtils = new PrefUtils(context);
			String string2 = prefUtils.getString("username");
			if (string2 != null) {
				name.setText(string2);
			}
		} else {
			ViewHolder viewHolder;
			if (convertView == null) {
				view = LayoutInflater.from(context).inflate(
						R.layout.item_left_2, null);
				viewHolder = new ViewHolder();
				viewHolder.setTextview((TextView) view.findViewById(R.id.text));
				viewHolder.setImageview((ImageView) view
						.findViewById(R.id.icon));
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			switch (position) {
			case 1:
				viewHolder.getImageview().setBackgroundResource(R.drawable.homepage);
				viewHolder.getTextview().setText("首页");
				break;
			case 2:
				viewHolder.getImageview().setBackgroundResource(R.drawable.myattention);
				viewHolder.getTextview().setText("我的关注");
				break;
			case 3:
				viewHolder.getImageview().setBackgroundResource(R.drawable.myshoucang);
				viewHolder.getTextview().setText("我的收藏");
				break;
			case 4:
				viewHolder.getImageview().setBackgroundResource(R.drawable.myissue);
				viewHolder.getTextview().setText("我的发布");
				break;
			case 5:
				viewHolder.getImageview().setBackgroundResource(R.drawable.night);
				viewHolder.getTextview().setText("夜间模式");
				break;
			case 6:
				viewHolder.getImageview().setBackgroundResource(R.drawable.setting);
				viewHolder.getTextview().setText("设置");
				break;
			default:
				break;
			}
		}

		return view;
	}

	private class ViewHolder {
		private ImageView imageview;
		private TextView textview;

		public ImageView getImageview() {
			return imageview;
		}

		public void setImageview(ImageView imageview) {
			this.imageview = imageview;
		}

		public TextView getTextview() {
			return textview;
		}

		public void setTextview(TextView textview) {
			this.textview = textview;
		}

	}
   

}
