package com.example.netease;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

import com.picture.adapter.MyViewPagerAdapter;
import com.picture.fragements.CommunityFragement;
import com.picture.fragements.FindFragement;
import com.picture.fragements.HomeFragement;
import com.picture.listener.java.MyPageChangeListener;
import com.picture.listener.java.MyRbCheckChangeListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ContentFragment extends Fragment {

	private ViewPager viewPager;
	private RadioButton radioButton1,radioButton2,radioButton3;
	private RadioGroup radioGroup;
	private View view1;
	private HorizontalScrollView horizontalScrollView;
	private ArrayList<Fragment> views;
	private MyRbCheckChangeListener changeListener;
	private MyViewPagerAdapter myViewPagerAdapter;
	private MyPageChangeListener myPageChangeListener;
	private HomeFragement homeFragement;
	private CommunityFragement communityFragement;
	private FindFragement findFragement;
	private Bundle bundle;
	private int page;
	private Bundle important;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_layout_first, null);
		bundle = getArguments();
		page = bundle.getInt("page",0);
		important = bundle.getBundle("bundle");
		viewPager = (ViewPager) view.findViewById(R.id.vp);
		horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.hs);
		radioButton1 = (RadioButton) view.findViewById(R.id.radioButton1);
		radioButton2 = (RadioButton) view.findViewById(R.id.radioButton2);
		radioButton3 = (RadioButton) view.findViewById(R.id.radioButton3);
		radioGroup = (RadioGroup) view.findViewById(R.id.rg);
		view1 = view.findViewById(R.id.view);
		views = new ArrayList<Fragment>();
		communityFragement = new CommunityFragement();
		findFragement = new FindFragement();
		homeFragement = new HomeFragement();
		if (important != null) {
			System.out.println("important != null ");
			communityFragement.setArguments(important);
		}
		views.add(homeFragement);
		views.add(communityFragement);
		views.add(findFragement);
		myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(),getActivity(),views);
		
		changeListener = new MyRbCheckChangeListener(viewPager);
		myPageChangeListener = new MyPageChangeListener(radioButton1, horizontalScrollView, viewPager, radioGroup, view1);
		radioGroup.setOnCheckedChangeListener(changeListener);
		viewPager.setAdapter(myViewPagerAdapter);
		viewPager.setOnPageChangeListener(myPageChangeListener);
		viewPager.setCurrentItem(page);
		return view;
	}

}
