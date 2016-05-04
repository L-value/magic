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

	// 定位相关
	LocationClient mLocClient;
	private int pageIndex = 0;
	private int curPage = 0;
	private int totalPage = 0;
	private LatLng pt = null;

	// 周边雷达相关
	RadarNearbyResult listResult = null;
	ListView mResultListView = null;
	private String userID = "";
	private String userComment = "";
	private boolean uploadAuto = false;

	// 地图相关
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private TextView popupText = null;// 泡泡view
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
		// 周边雷达设置监听
		RadarSearchManager.getInstance().addNearbyInfoListener(this);
		// 周边雷达设置用户，id为空默认是设备标识
		RadarSearchManager.getInstance().setUserID(userID);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(false);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}
	
	  /**
     * 设置中心点
     */
    private void setUserMapCenter() {
        Log.v("pcw","setUserMapCenter : lat : "+ lat+" lon : " + lon);
        LatLng cenpt = new LatLng(lat,lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

    }


	/**
	 * 上传一次位置
	 * 
	 * @param v
	 */
	public void uploadOnceClick(View v) {
		if (pt == null) {
			Toast.makeText(AroundActivityt.this, "未获取到位置", Toast.LENGTH_LONG)
					.show();
			return;
		}
		RadarUploadInfo info = new RadarUploadInfo();
		info.comments = userComment;
		info.pt = pt;
		RadarSearchManager.getInstance().uploadInfoRequest(info);
	}

	/**
	 * 开始自动上传
	 * 
	 * @param v
	 */
	public void uploadContinueClick() {
		if (pt == null) {
			Toast.makeText(AroundActivityt.this, "未获取到位置", Toast.LENGTH_LONG)
					.show();
			return;
		}
		uploadAuto = true;
		RadarSearchManager.getInstance().startUploadAuto(this, 5000);
	}

	/**
	 * 停止自动上传
	 * 
	 * @param v
	 */
	public void stopUploadClick() {
		uploadAuto = false;
		RadarSearchManager.getInstance().stopUploadAuto();
	}

	/**
	 * 清除自己当前的信息
	 * 
	 * @param v
	 */
	public void clearInfoClick() {
		RadarSearchManager.getInstance().clearUserInfo();
	}

	/**
	 * 查找周边的人
	 * 
	 * @param v
	 */
	public void searchNearby() {
		if (pt == null) {
			Toast.makeText(AroundActivityt.this, "未获取到位置", Toast.LENGTH_LONG)
					.show();
			return;
		}
		pageIndex = 0;
		searchRequest(pageIndex);
	}

	/**
	 * 上一页
	 * 
	 * @param v
	 */
	public void preClick() {
		if (pageIndex < 1) {
			return;
		}
		// 上一页
		pageIndex--;
		searchRequest(pageIndex);
	}

	/**
	 * 下一页
	 * 
	 * @param v
	 */
	public void nextClick() {
		if (pageIndex >= totalPage - 1) {
			return;
		}
		// 下一页
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
	 * 清除查找结果
	 * 
	 * @param v
	 */
	public void clearResult() {
		parseResultToMap(null);
		mBaiduMap.hideInfoWindow();
	}

	/**
	 * 更新结果地图
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
					des.putString("des", "没有备注");
				} else {
					des.putString("des", res.infoList.get(i).comments);
				}

				option.extraInfo(des);
				mBaiduMap.addOverlay(option);
			}
		}

	}

	/**
	 * 实现上传callback，自动上传
	 */

	@Override
	public void onGetNearbyInfoList(RadarNearbyResult result,
			RadarSearchError error) {
		// TODO Auto-generated method stub
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			Toast.makeText(AroundActivityt.this, "查询周边成功", Toast.LENGTH_LONG)
					.show();
			// 获取成功
			listResult = result;
			curPage = result.pageIndex;
			totalPage = result.pageNum;
			// 处理数据
			parseResultToMap(listResult);
		} else {
			// 获取失败
			curPage = 0;
			totalPage = 0;
			Toast.makeText(AroundActivityt.this, "查询周边失败", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onGetUploadState(RadarSearchError error) {
		// TODO Auto-generated method stub
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// 上传成功
			if (!uploadAuto) {
				Toast.makeText(AroundActivityt.this, "单次上传位置成功",
						Toast.LENGTH_LONG).show();
			}
		} else {
			// 上传失败
			if (!uploadAuto) {
				Toast.makeText(AroundActivityt.this, "单次上传位置失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onGetClearInfoState(RadarSearchError error) {
		// TODO Auto-generated method stub
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// 清除成功
			Toast.makeText(AroundActivityt.this, "清除位置成功", Toast.LENGTH_LONG)
					.show();
		} else {
			// 清除失败
			Toast.makeText(AroundActivityt.this, "清除位置失败", Toast.LENGTH_LONG)
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
		// 退出时销毁定位
		mLocClient.stop();
		// 释放周边雷达相关
		RadarSearchManager.getInstance().removeNearbyInfoListener(this);
		RadarSearchManager.getInstance().clearUserInfo();
		RadarSearchManager.getInstance().destroy();
		// 释放地图
		ff3.recycle();
		mMapView.onDestroy();
		mBaiduMap = null;
		super.onDestroy();
	}

	// 定位SDK回调
	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub
		if (arg0 == null || mBaiduMap == null)
			return;
		pt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(arg0.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(arg0.getLatitude())
				.longitude(arg0.getLongitude()).build();
		if (mBaiduMap != null) {
			mBaiduMap.setMyLocationData(locData);
		}
		lat = arg0.getLatitude();
		lon = arg0.getLongitude();
		 //这个判断是为了防止每次定位都重新设置中心点和marker  
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
