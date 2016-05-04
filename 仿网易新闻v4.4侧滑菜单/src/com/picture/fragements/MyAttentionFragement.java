package com.picture.fragements;

import java.util.ArrayList;

import com.example.imageloader.AttentionAdapter;
import com.example.netease.R;






import com.picture.bean.Attention;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MyAttentionFragement extends Fragment{

	private ListView attention;
	private AttentionAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragement_attention, null);
		attention = (ListView) view.findViewById(R.id.attention);
		ArrayList<Attention> arrayList = new ArrayList<>();
		arrayList.add(new Attention("test","http://test.nsscn.org/helloYii/web/uploads/test/tongxiang.png"));
		adapter = new AttentionAdapter(getActivity(), attention, arrayList);
			attention.setAdapter(adapter);
		return view;
	}
}
