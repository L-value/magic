package com.example.imageloader;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imageloader.ImageDownLoader.onImageLoaderListener;
import com.example.netease.R;
import com.example.netease.Tag;
import com.picture.bean.Theme;

public class TitleAdapter  extends BaseAdapter{

	/** 
     * 上下文对象的引用 
     */  
    private Context context;  
    private ImageDownLoader mImageDownLoader;
    /** 
     * Image Url的数组 
     */  
   private List<Tag> tags;


      
      
    public TitleAdapter(Context context,  List<Tag> tags){  
        this.context = context;  
        this.tags = tags;  
        mImageDownLoader = new ImageDownLoader(context);
    }  
      
 
      
  
    @Override  
    public int getCount() {  
        return tags.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return tags.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
    ViewHolder holder;
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_tag, null);
			holder = new ViewHolder();
			holder.setImageview((ImageView)convertView.findViewById(R.id.tag));
			holder.setTextview((TextView)convertView.findViewById(R.id.name));
			convertView.setTag(holder);
    	}else {
    		holder = (ViewHolder) convertView.getTag();
		}
          
         holder.getTextview().setText(tags.get(position).getUsername());
        
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
        
            String mImageUrl = tags.get(position).getHeadportrait();  
            System.out.println("titleImage"+mImageUrl+"position"+position);
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
              mImageView.setImageBitmap(bitmap);  
            }else{  
              mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.cc));  
            }  
        
    }  
}  