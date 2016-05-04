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
import com.picture.bean.Find;
import com.picture.bean.Quality;
import com.picture.myviews.RoundedImageView;

/**   
*    
* ��Ŀ���ƣ�ImageLoader   
* �����ƣ�ImageAdapter   
* �������� GridView���������࣬��Ҫ��GridView������ʱ��ȡ����������
* ��ֹ��ʱ��ȥ���ص�ǰ��ʾ��item��ͼƬ   
* �����ˣ�fuli   
* ����ʱ�䣺2015��7��26�� ����9:33:31   
* �޸��ˣ�fuli   
* �޸�ʱ�䣺2015��7��26�� ����9:33:31   
* �޸ı�ע��   
* @version    
*    
*/
public class FindAdapter  extends BaseAdapter implements  OnScrollListener{

	/** 
     * �����Ķ�������� 
     */  
    private Context context;  
      
    /** 
     * Image Url������ 
     */  
   private List<Find> finds;
      
    /** 
     * GridView�����Ӧ�� 
     */  
    private ListView mGridView;  
      
    /** 
     * Image ������ 
     */  
    private ImageDownLoader mImageDownLoader;  
      
    /** 
     * ��¼�Ƿ�մ򿪳������ڽ��������򲻹�����Ļ����������ͼƬ�����⡣ 
     * �ο�http://blog.csdn.net/guolin_blog/article/details/9526203#comments 
     */  
    private boolean isFirstEnter = true;  
      
    /** 
     * һ���е�һ��item��λ�� 
     */  
    private int mFirstVisibleItem;  
      
    /** 
     * һ��������item�ĸ��� 
     */  
    private int mVisibleItemCount;  
      
      
    public FindAdapter(Context context, ListView listView, List<Find> finds){  
        this.context = context;  
        this.mGridView = listView;  
        this.finds = finds;  
        mImageDownLoader = new ImageDownLoader(context);  
        listView.setOnScrollListener(this);  
    }  
      
 
      
  
    @Override  
    public int getCount() {  
        return finds.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return finds.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder;
    	if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_find, null);
			holder = new ViewHolder();
			holder.setImageview((ImageView)convertView.findViewById(R.id.img));
			holder.setTextview((TextView)convertView.findViewById(R.id.saywhat));
			holder.setUsername((TextView)convertView.findViewById(R.id.name));
			convertView.setTag(holder);
    	}else {
    		holder = (ViewHolder) convertView.getTag();
		}
          
    	holder.getTextview().setText(finds.get(position).getContext());
    	holder.getUsername().setText(finds.get(position).getName());
    	System.out.println("url"+finds.get(position).getImgs());
    	/*******************************ȥ�������⼸��������ʲôЧ��****************************/  
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(finds.get(position).getImgs().replaceAll("[^\\w]", ""));  
        System.out.println(bitmap+"bitmap");
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
    	private TextView username;
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
		public TextView getUsername() {
			return username;
		}
		public void setUsername(TextView username) {
			this.username = username;
		}
    	
    	
    }
      
    /** 
     * ��ʾ��ǰ��Ļ��ͼƬ���Ȼ�ȥ����LruCache��LruCacheû�о�ȥsd�������ֻ�Ŀ¼���ң���û�оͿ����߳�ȥ���� 
     * @param firstVisibleItem 
     * @param visibleItemCount 
     */  
    private void showImage(int firstVisibleItem, int visibleItemCount){  
        Bitmap bitmap = null;  
        for(int i=firstVisibleItem; i<firstVisibleItem + visibleItemCount; i++){  
            String mImageUrl = finds.get(i).getImgs();  
            final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);  
            bitmap = mImageDownLoader.downloadImage(mImageUrl, new onImageLoaderListener() {  
                  
                @Override  
                public void onImageLoader(Bitmap bitmap, String url) {  
                    if(mImageView != null && bitmap != null){  
                        mImageView.setBackground(new BitmapDrawable(bitmap));
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
     * ȡ���������� 
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
	        // ���������Ϊ�״ν����������������   
	        if(isFirstEnter && visibleItemCount > 0){  
	            showImage(mFirstVisibleItem, mVisibleItemCount);  
	            isFirstEnter = false;  
	        }  		
	}

	
  
  
}  

