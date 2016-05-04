package com.picture.listener.java;


import com.example.netease.R;

import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MyRbCheckChangeListener implements OnCheckedChangeListener
{
	private ViewPager viewPager;
	
	public MyRbCheckChangeListener(ViewPager viewPager)
	{
		super();
		this.viewPager = viewPager;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		int current = 0;
		switch (checkedId)
		{
		case R.id.radioButton1:
			current = 0;
			break;
		case R.id.radioButton2:
			current = 1;
			break;
		case R.id.radioButton3:
			current = 2;
			break;
		
		default:
			break;
		}
		viewPager.setCurrentItem(current);
		
	}




}
