package com.example.imageloader;

import java.util.List;

import lib.MultiColumnListView;
import lib.internal.PLA_AbsListView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imageloader.ImageDownLoader.onImageLoaderListener;
import com.example.netease.R;
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
public class ImageAdapter  extends BaseAdapter implements lib.internal.PLA_AbsListView.OnScrollListener{

	/** 
     * �����Ķ�������� 
     */  
    private Context context;  
      
    /** 
     * Image Url������ 
     */  
   private List<Quality> qualities;
      
    /** 
     * GridView�����Ӧ�� 
     */  
    private MultiColumnListView mGridView;  
      
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
      
      
    public ImageAdapter(Context context, MultiColumnListView mGridView, List<Quality> qualities){  
        this.context = context;  
        this.mGridView = mGridView;  
        this.qualities = qualities;  
        mImageDownLoader = new ImageDownLoader(context);  
        mGridView.setOnScrollListener(this);  
    }  
      
 
      
  
    @Override  
    public int getCount() {  
        return qualities.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return qualities.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	final ViewHolder holder;
    	if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_home, null);
			holder = new ViewHolder();
			holder.setImageview((ImageView)convertView.findViewById(R.id.imagecoll));
			holder.setLove((ImageView)convertView.findViewById(R.id.love));
			holder.setTextview((TextView)convertView.findViewById(R.id.title));
			convertView.setTag(holder);
    	}else {
    		holder = (ViewHolder) convertView.getTag();
		}
          
    	holder.getLove().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				holder.getLove().setBackgroundResource(R.drawable.zan_after);
				
			}
		});
    	holder.getTextview().setText(qualities.get(position).getTitle());
    	/*******************************ȥ�������⼸��������ʲôЧ��****************************/  
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(qualities.get(position).getImg().replaceAll("[^\\w]", ""));  
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
    	private ImageView love;
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
		public ImageView getLove() {
			return love;
		}
		public void setLove(ImageView love) {
			this.love = love;
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
            String mImageUrl = qualities.get(i).getImg();  
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
     * ȡ���������� 
     */  
    public void cancelTask(){  
        mImageDownLoader.cancelTask();  
    }

	@Override
	public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
		  //����GridView��ֹʱ��ȥ����ͼƬ��GridView����ʱȡ�������������ص�����    
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){  
            showImage(mFirstVisibleItem, mVisibleItemCount);  
        }else{  
            cancelTask();  
        }  
		
	}

	@Override
	public void onScroll(PLA_AbsListView view, int firstVisibleItem,
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

