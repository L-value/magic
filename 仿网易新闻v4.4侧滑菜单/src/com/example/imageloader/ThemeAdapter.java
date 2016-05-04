package com.example.imageloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import lib.MultiColumnListView;
import lib.internal.PLA_AbsListView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imageloader.ImageDownLoader.onImageLoaderListener;
import com.example.netease.R;
import com.picture.bean.Community;
import com.picture.bean.Quality;
import com.picture.bean.Theme;
import com.picture.myviews.HorizontialListView;
import com.picture.myviews.RoundedImageView;
import com.squareup.picasso.OkHttpDownloader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.utils.ImageUtils;


public class ThemeAdapter  extends BaseAdapter{

	/** 
     * 上下文对象的引用 
     */  
    private Context context;  
    private ImageDownLoader mImageDownLoader;
    /** 
     * Image Url的数组 
     */  
   private List<Theme> themes;


      
      
    public ThemeAdapter(Context context,  List<Theme> themes){  
        this.context = context;  
        this.themes = themes;  
        mImageDownLoader = new ImageDownLoader(context);
    }  
      
 
      
  
    @Override  
    public int getCount() {  
        return themes.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return themes.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
    ViewHolder holder;
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_theme, null);
			holder = new ViewHolder();
			holder.setImageview((ImageView)convertView.findViewById(R.id.img));
			holder.setTextview((TextView)convertView.findViewById(R.id.name));
			convertView.setTag(holder);
    	}else {
    		holder = (ViewHolder) convertView.getTag();
		}
          
         holder.getTextview().setText(themes.get(position).getName());
       
		/*******************************去掉下面这几行试试是什么效果****************************/  
       
        showImage(position, holder.getImageview());
        /**********************************************************************************/  
          
          
    	return convertView;
    }  
    
   
    
    private class ViewHolder{
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
   
    private void showImage(int position,final ImageView mImageView){  
    		Bitmap bitmap = null;  
        
            String mImageUrl = themes.get(position).getImg();  
            System.out.println("MiMageURL"+mImageUrl);
            bitmap = mImageDownLoader.downloadImage(mImageUrl, new onImageLoaderListener() {  
                  
                @Override  
                public void onImageLoader(Bitmap bitmap, String url) {  
                    if(mImageView != null && bitmap != null){  
                    	mImageView.setBackground(new BitmapDrawable(bitmap));  
                    	System.out.println(bitmap);
                    }  
                      
                }  
            });  
            if(bitmap != null){  
              mImageView.setBackground(new BitmapDrawable(bitmap));
            }else{  
              mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.cc));  
            }  
        
    }  
}  

