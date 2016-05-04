package com.example.imageloader;

import java.util.List;

import lib.MultiColumnListView;
import lib.internal.PLA_AbsListView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.imageloader.ImageDownLoader.onImageLoaderListener;
import com.example.netease.R;
import com.picture.bean.Community;
import com.picture.bean.Quality;
import com.picture.myviews.RoundedImageView;

/**   
*    
* 项目名称：ImageLoader   
* 类名称：ImageAdapter   
* 类描述： GridView的适配器类，主要是GridView滑动的时候取消下载任务，
* 静止的时候去下载当前显示的item的图片   
* 创建人：fuli   
* 创建时间：2015年7月26日 上午9:33:31   
* 修改人：fuli   
* 修改时间：2015年7月26日 上午9:33:31   
* 修改备注：   
* @version    
*    
*/
public class CommuntiyAdapter  extends BaseAdapter implements  OnScrollListener{

	/** 
     * 上下文对象的引用 
     */  
    private Context context;  
      
    /** 
     * Image Url的数组 
     */  
   private List<Community> communities;
      
    /** 
     * GridView对象的应用 
     */  
    private ListView mGridView;  
      
    /** 
     * Image 下载器 
     */  
    private ImageDownLoader mImageDownLoader;  
      
    /** 
     * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。 
     * 参考http://blog.csdn.net/guolin_blog/article/details/9526203#comments 
     */  
    private boolean isFirstEnter = true;  
      
    /** 
     * 一屏中第一个item的位置 
     */  
    private int mFirstVisibleItem;  
      
    /** 
     * 一屏中所有item的个数 
     */  
    private int mVisibleItemCount;  
      
      
    public CommuntiyAdapter(Context context, ListView listView, List<Community> communities){  
        this.context = context;  
        this.mGridView = listView;  
        this.communities = communities;  
        mImageDownLoader = new ImageDownLoader(context);  
        listView.setOnScrollListener(this);  
    }  
      
 
      
  
    @Override  
    public int getCount() {  
        return communities.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return communities.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder;
    	if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_community, null);
			holder = new ViewHolder();
			holder.setImageview((ImageView)convertView.findViewById(R.id.comimage));
			holder.setTextview((TextView)convertView.findViewById(R.id.name));
			holder.setContent((TextView)convertView.findViewById(R.id.content));
			convertView.setTag(holder);
    	}else {
    		holder = (ViewHolder) convertView.getTag();
		}
          
         holder.getContent().setText(communities.get(position).getTitle());
        /*******************************去掉下面这几行试试是什么效果****************************/  
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(communities.get(position).getImg().replaceAll("[^\\w]", ""));  
        if(bitmap != null){  
            holder.getImageview().setBackground(new BitmapDrawable(bitmap));
        }else{  
        	holder.getImageview().setBackground(context.getResources().getDrawable(R.drawable.cc));
        }  
        /**********************************************************************************/  
          
          
    	return convertView;
    }  
    
    private class ViewHolder{
    	private ImageView imageview;
    	private TextView textview;
    	private TextView content;
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
		public TextView getContent() {
			return content;
		}
		public void setContent(TextView content) {
			this.content = content;
		}
    	
    }
      
    /** 
     * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载 
     * @param firstVisibleItem 
     * @param visibleItemCount 
     */  
    private void showImage(int firstVisibleItem, int visibleItemCount){  
        Bitmap bitmap = null;  
        for(int i=firstVisibleItem; i<firstVisibleItem + visibleItemCount; i++){  
            String mImageUrl = communities.get(i).getImg();  
            final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);  
            bitmap = mImageDownLoader.downloadImage(mImageUrl, new onImageLoaderListener() {  
                  
                @Override  
                public void onImageLoader(Bitmap bitmap, String url) {  
                    if(mImageView != null && bitmap != null){  
                        mImageView.setImageBitmap(bitmap);  
                    }  
                      
                }  
            });  
              
            //if(bitmap != null){  
            //  mImageView.setImageBitmap(bitmap);  
            //}else{  
            //  mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_empty));  
            //}  
        }  
    }  
  
    /** 
     * 取消下载任务 
     */  
    public void cancelTask(){  
        mImageDownLoader.cancelTask();  
    }




	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		  if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){  
	            showImage(mFirstVisibleItem, mVisibleItemCount);  
	        }else{  
	            cancelTask();  
	        }  
					
	}




	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		   mFirstVisibleItem = firstVisibleItem;  
	        mVisibleItemCount = visibleItemCount;  
	        // 因此在这里为首次进入程序开启下载任务。   
	        if(isFirstEnter && visibleItemCount > 0){  
	            showImage(mFirstVisibleItem, mVisibleItemCount);  
	            isFirstEnter = false;  
	        }  		
	}

	
  
  
}  

