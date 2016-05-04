package com.example.netease;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.radar.RadarUploadInfoCallback;
import com.example.netease.R;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AroundActivityt extends Activity implements
		RadarUploadInfoCallback, RadarSearchListener, BDLocationListener,
		OnMarkerClickListener, OnMapClickListener {

	// ��λ���
	LocationClient mLocClient;
	private int pageIndex = 0;
	private int curPage = 0;
	private int totalPage = 0;
	private LatLng pt = null;

	// �ܱ��״����
	RadarNearbyResult listResult = null;
	ListView mResultListView = null;
	private String userID = "";
	private String userComment = "";
	private boolean uploadAuto = false;

	// ��ͼ���
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private TextView popupText = null;// ����view
	BitmapDescriptor ff3 = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	private double lat;
	private double lon;
	private boolean isFirstLocation = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.around_fragement);
		RadarSearchManager mManager = RadarSearchManager.getInstance();
		mMapView = (MapView) findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMarkerClickListener(this);
		mBaiduMap.setOnMapClickListener(this);
		mBaiduMap.setMyLocationEnabled(true);
		// �ܱ��״����ü���
		RadarSearchManager.getInstance().addNearbyInfoListener(this);
		// �ܱ��״������û���idΪ��Ĭ�����豸��ʶ
		RadarSearchManager.getInstance().setUserID(userID);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(false);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}
	
	  /**
     * �������ĵ�
     */
    private void setUserMapCenter() {
        Log.v("pcw","setUserMapCenter : lat : "+ lat+" lon : " + lon);
        LatLng cenpt = new LatLng(lat,lon);
        //�����ͼ״̬
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //�ı��ͼ״̬
        mBaiduMap.setMapStatus(mMapStatusUpdate);

    }


	/**
	 * �ϴ�һ��λ��
	 * 
	 * @param v
	 */
	public void uploadOnceClick(View v) {
		if (pt == null) {
			Toast.makeText(AroundActivityt.this, "δ��ȡ��λ��", Toast.LENGTH_LONG)
					.show();
			return;
		}
		RadarUploadInfo info = new RadarUploadInfo();
		info.comments = userComment;
		info.pt = pt;
		RadarSearchManager.getInstance().uploadInfoRequest(info);
	}

	/**
	 * ��ʼ�Զ��ϴ�
	 * 
	 * @param v
	 */
	public void uploadContinueClick() {
		if (pt == null) {
			Toast.makeText(AroundActivityt.this, "δ��ȡ��λ��", Toast.LENGTH_LONG)
					.show();
			return;
		}
		uploadAuto = true;
		RadarSearchManager.getInstance().startUploadAuto(this, 5000);
	}

	/**
	 * ֹͣ�Զ��ϴ�
	 * 
	 * @param v
	 */
	public void stopUploadClick() {
		uploadAuto = false;
		RadarSearchManager.getInstance().stopUploadAuto();
	}

	/**
	 * ����Լ���ǰ����Ϣ
	 * 
	 * @param v
	 */
	public void clearInfoClick() {
		RadarSearchManager.getInstance().clearUserInfo();
	}

	/**
	 * �����ܱߵ���
	 * 
	 * @param v
	 */
	public void searchNearby() {
		if (pt == null) {
			Toast.makeText(AroundActivityt.this, "δ��ȡ��λ��", Toast.LENGTH_LONG)
					.show();
			return;
		}
		pageIndex = 0;
		searchRequest(pageIndex);
	}

	/**
	 * ��һҳ
	 * 
	 * @param v
	 */
	public void preClick() {
		if (pageIndex < 1) {
			return;
		}
		// ��һҳ
		pageIndex--;
		searchRequest(pageIndex);
	}

	/**
	 * ��һҳ
	 * 
	 * @param v
	 */
	public void nextClick() {
		if (pageIndex >= totalPage - 1) {
			return;
		}
		// ��һҳ
		pageIndex++;
		searchRequest(pageIndex);
	}

	private void searchRequest(int index) {
		curPage = 0;
		totalPage = 0;

		RadarNearbySearchOption option = new RadarNearbySearchOption()
				.centerPt(pt).pageNum(pageIndex).radius(2000);
		RadarSearchManager.getInstance().nearbyInfoRequest(option);

		mBaiduMap.hideInfoWindow();
	}

	/**
	 * ������ҽ��
	 * 
	 * @param v
	 */
	public void clearResult() {
		parseResultToMap(null);
		mBaiduMap.hideInfoWindow();
	}

	/**
	 * ���½����ͼ
	 * 
	 * @param res
	 */
	public void parseResultToMap(RadarNearbyResult res) {
		mBaiduMap.clear();
		if (res != null && res.infoList != null && res.infoList.size() > 0) {
			for (int i = 0; i < res.infoList.size(); i++) {
				MarkerOptions option = new MarkerOptions().icon(ff3).position(
						res.infoList.get(i).pt);
				Bundle des = new Bundle();
				if (res.infoList.get(i).comments == null
						|| res.infoList.get(i).comments.equals("")) {
					des.putString("des", "û�б�ע");
				} else {
					des.putString("des", res.infoList.get(i).comments);
				}

				option.extraInfo(des);
				mBaiduMap.addOverlay(option);
			}
		}

	}

	/**
	 * ʵ���ϴ�callback���Զ��ϴ�
	 */

	@Override
	public void onGetNearbyInfoList(RadarNearbyResult result,
			RadarSearchError error) {
		// TODO Auto-generated method stub
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			Toast.makeText(AroundActivityt.this, "��ѯ�ܱ߳ɹ�", Toast.LENGTH_LONG)
					.show();
			// ��ȡ�ɹ�
			listResult = result;
			curPage = result.pageIndex;
			totalPage = result.pageNum;
			// ��������
			parseResultToMap(listResult);
		} else {
			// ��ȡʧ��
			curPage = 0;
			totalPage = 0;
			Toast.makeText(AroundActivityt.this, "��ѯ�ܱ�ʧ��", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onGetUploadState(RadarSearchError error) {
		// TODO Auto-generated method stub
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// �ϴ��ɹ�
			if (!uploadAuto) {
				Toast.makeText(AroundActivityt.this, "�����ϴ�λ�óɹ�",
						Toast.LENGTH_LONG).show();
			}
		} else {
			// �ϴ�ʧ��
			if (!uploadAuto) {
				Toast.makeText(AroundActivityt.this, "�����ϴ�λ��ʧ��",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onGetClearInfoState(RadarSearchError error) {
		// TODO Auto-generated method stub
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// ����ɹ�
			Toast.makeText(AroundActivityt.this, "���λ�óɹ�", Toast.LENGTH_LONG)
					.show();
		} else {
			// ���ʧ��
			Toast.makeText(AroundActivityt.this, "���λ��ʧ��", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		mBaiduMap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		mBaiduMap.hideInfoWindow();
		if (marker != null) {
			popupText = new TextView(AroundActivityt.this);
			// popupText.setBackgroundResource(R.drawable.popup);
			popupText.setTextColor(0xFF000000);
			popupText.setText(marker.getExtraInfo().getString("des"));
			mBaiduMap.showInfoWindow(new InfoWindow(popupText, marker
					.getPosition(), -47));
			MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker
					.getPosition());
			mBaiduMap.setMapStatus(update);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �ͷ��ܱ��״����
		RadarSearchManager.getInstance().removeNearbyInfoListener(this);
		RadarSearchManager.getInstance().clearUserInfo();
		RadarSearchManager.getInstance().destroy();
		// �ͷŵ�ͼ
		ff3.recycle();
		mMapView.onDestroy();
		mBaiduMap = null;
		super.onDestroy();
	}

	// ��λSDK�ص�
	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub
		if (arg0 == null || mBaiduMap == null)
			return;
		pt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(arg0.getRadius())
				// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
				.direction(100).latitude(arg0.getLatitude())
				.longitude(arg0.getLongitude()).build();
		if (mBaiduMap != null) {
			mBaiduMap.setMyLocationData(locData);
		}
		lat = arg0.getLatitude();
		lon = arg0.getLongitude();
		 //����ж���Ϊ�˷�ֹÿ�ζ�λ�������������ĵ��marker  
        if(isFirstLocation){  
            isFirstLocation  = false;  
            setUserMapCenter();  
        }  
		uploadContinueClick();
		searchNearby();
	}

	@Override
	public RadarUploadInfo onUploadInfoCallback() {
		// TODO Auto-generated method stub
		RadarUploadInfo info = new RadarUploadInfo();
		info.comments = userComment;
		info.pt = pt;
		Log.e("hjtest", "OnUploadInfoCallback");
		return info;
	}

}
