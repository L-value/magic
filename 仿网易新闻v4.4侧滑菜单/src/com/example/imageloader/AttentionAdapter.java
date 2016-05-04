package com.example.imageloader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.imageloader.ImageDownLoader.onImageLoaderListener;
import com.example.netease.R;
import com.picture.bean.Attention;

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
public class AttentionAdapter  extends BaseAdapter implements OnScrollListener{

	/** 
     * 上下文对象的引用 
     */  
    private Context context;  
      
    /** 
     * Image Url的数组 
     */  
      
    /** 
     * GridView对象的应用 
     */  
    private ListView mListView;  
      
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
    private List<Attention> Attentions;
      
      
    public AttentionAdapter(Context context, ListView mGridView,List<Attention> Attentions){  
        this.context = context;  
        this.mListView = mGridView;  
        this.Attentions = Attentions;
        mImageDownLoader = new ImageDownLoader(context);  
       mListView.setOnScrollListener(this);  
    }  
      
    @Override  
    public void onScrollStateChanged(AbsListView view, int scrollState) {  
        //仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务    
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){  
    //        showImage(mFirstVisibleItem, mVisibleItemCount);  
        }else{  
            cancelTask();  
        }  
          
    }  
  
  
    /** 
     * GridView滚动的时候调用的方法，刚开始显示GridView也会调用此方法 
     */  
    @Override  
    public void onScroll(AbsListView view, int firstVisibleItem,  
            int visibleItemCount, int totalItemCount) {  
        mFirstVisibleItem = firstVisibleItem;  
        mVisibleItemCount = visibleItemCount;  
        // 因此在这里为首次进入程序开启下载任务。   
        if(isFirstEnter && visibleItemCount > 0){  
         //   showImage(mFirstVisibleItem, mVisibleItemCount);  
            isFirstEnter = false;  
        }  
    }  
      
  
    @Override  
    public int getCount() {  
        return Attentions.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return Attentions.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
    //	AndroidTools.showToastShort(context, "position"+Attentions.get(position).getDetail());
        TextView name;
        if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_attention, null);
		}
        name = (TextView) convertView.findViewById(R.id.name);
        ImageView mImageView = (ImageView) convertView.findViewById(R.id.att);
        String mImageUrl = Attentions.get(position).getImg();
		/*******************************去掉下面这几行试试是什么效果****************************/  
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl .replaceAll("[^\\w]", ""));  
        if(bitmap != null){  
            mImageView.setImageBitmap(bitmap);  
        }else{  
            mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));  
        }  
        /**********************************************************************************/  
          
          
        return convertView;  
    }  
      
    /** 
     * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载 
     * @param firstVisibleItem 
     * @param visibleItemCount 
     */  
  
  
    /** 
     * 取消下载任务 
     */  
    public void cancelTask(){  
        mImageDownLoader.cancelTask();  
    }  
    public void addMoreData(List<Attention> collection){
    	if (Attentions != null)
		{
			Attentions.addAll(collection);
		}
    }
  
}  

