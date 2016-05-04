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
public class AttentionAdapter  extends BaseAdapter implements OnScrollListener{

	/** 
     * �����Ķ�������� 
     */  
    private Context context;  
      
    /** 
     * Image Url������ 
     */  
      
    /** 
     * GridView�����Ӧ�� 
     */  
    private ListView mListView;  
      
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
        //����GridView��ֹʱ��ȥ����ͼƬ��GridView����ʱȡ�������������ص�����    
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){  
    //        showImage(mFirstVisibleItem, mVisibleItemCount);  
        }else{  
            cancelTask();  
        }  
          
    }  
  
  
    /** 
     * GridView������ʱ����õķ������տ�ʼ��ʾGridViewҲ����ô˷��� 
     */  
    @Override  
    public void onScroll(AbsListView view, int firstVisibleItem,  
            int visibleItemCount, int totalItemCount) {  
        mFirstVisibleItem = firstVisibleItem;  
        mVisibleItemCount = visibleItemCount;  
        // ���������Ϊ�״ν����������������   
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
		/*******************************ȥ�������⼸��������ʲôЧ��****************************/  
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
     * ��ʾ��ǰ��Ļ��ͼƬ���Ȼ�ȥ����LruCache��LruCacheû�о�ȥsd�������ֻ�Ŀ¼���ң���û�оͿ����߳�ȥ���� 
     * @param firstVisibleItem 
     * @param visibleItemCount 
     */  
  
  
    /** 
     * ȡ���������� 
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

