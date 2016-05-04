package com.picture.listener.java;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
public class MyPageChangeListener implements OnPageChangeListener
{
	private RadioButton radioButton01;
	private HorizontalScrollView horizontalScrollView;
	private ViewPager viewPager;
	private float fromX;
	private RadioGroup radioGroup;
	private View view;
	public MyPageChangeListener(RadioButton radioButton01,
								HorizontalScrollView horizontalScrollView,
								ViewPager viewPager,RadioGroup radioGroup,
								View view)
	{
		super();
		this.radioButton01 = radioButton01;
		this.horizontalScrollView = horizontalScrollView;
		this.viewPager = viewPager;
		this.radioGroup = radioGroup;
		this.view = view;
	}
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		//需要监听的主要事件
		//1计算对应选项居中时，hsv滑动的位置
		int total = (int) ((position+positionOffset)*radioButton01.getWidth());System.out.println("total"
				+ "          "+total);
		Log.i("INFO", "rb_position:"+position);
		int green = (viewPager.getWidth()-radioButton01.getWidth())/2;System.out.println(green);
		int dx = total - green;//计算出要滑出去的距离
		System.out.println("dx               "+dx);
		horizontalScrollView.scrollTo(dx, 0);
		lineScroll(position, positionOffset);
		
	}
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		
	}
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
	public void lineScroll(int position,float positionOffSet){
		//拿到被选中按钮在屏幕中的位置
//		Toast.makeText(this, "position:"+position, 1000).show();
		Log.i("INFO", "line_position:"+position);
		RadioButton button = (RadioButton) radioGroup.getChildAt(position);
		int [] location = new int[2];
		button.getLocationInWindow(location);
		//开始做位移滑动
		TranslateAnimation animation = new TranslateAnimation(
				fromX,
				location[0]+positionOffSet*radioButton01.getWidth(), 
				0, 
				0);
		animation.setDuration(50);//动画持续事件
		animation.setFillAfter(true);
		fromX = (int) (location[0]+positionOffSet*radioButton01.getWidth());
		view.startAnimation(animation);//线开始动画
	
	}
}
