package com.example.netease;

import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuIcon;
import com.balysv.materialmenu.MaterialMenuDrawable.Stroke;
import com.picture.adapter.LeftlistAdapter;
import com.picture.adapter.RightlistAdapter;
import com.picture.bean.PhotoData;
import com.picture.fragements.MyAttentionFragement;
import com.picture.fragements.MyCollectFragement;
import com.picture.fragements.MyIssueFragement;
import com.picture.fragements.SettingFragement;
import com.picture.listener.java.HeadListener;
import com.picture.myviews.RoundedImageView;
import com.picture.myviews.RoundedTransformationBuilder;
import com.picture.utils.GalleryUtils;
import com.picture.utils.PrefUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	/** 左边栏菜单 */
	private ListView mMenuListView;
	/** 右边栏 */
	private RelativeLayout right_drawer;
	/** 菜单列表 */
	private String[] mMenuTitles;
	/** Material Design风格 */
	private MaterialMenuIcon mMaterialMenuIcon;
	/** 菜单打开/关闭状态 */
	private boolean isDirection_left = false;
	/** 右边栏打开/关闭状态 */
	private boolean isDirection_right = false;
	private View showView;
	private PrefUtils prefUtils;
	private LeftlistAdapter leftlistAdapter = new LeftlistAdapter(this, new HeadListener() {

		@Override
		public void photoClick(RoundedImageView rouImageView) {
			roundheadimg = rouImageView;
			if (prefUtils.getBoolean("login")) {
			showOptionsDialog(null, new String[] {
					getString(R.string.me_profile_camera),
					getString(R.string.me_profile_album) },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialogInterface, int i) {
								if (i == 0) {
									shootPhoto();
									LeftlistAdapter.click = true;
								}

								if (i == 1) {
									pickPhoto();
									LeftlistAdapter.click = true;
								}
						}
					});
					}else {
						startActivity(new Intent(MainActivity.this,LoginActivity.class));
					}
		}
	});
	private ListView list_right;
	
	private int page = 0;
	private Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SDKInitializer.initialize(getApplicationContext());  
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		page = intent.getIntExtra("page", 0);
		bundle = intent.getExtras();
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		 JPushInterface.init(getApplicationContext());
		RadarSearchManager mManager = RadarSearchManager.getInstance();
		setContentView(R.layout.activity_main);
		list_right = (ListView) findViewById(R.id.list_right);
		list_right.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(MainActivity.this,GetImageActivity.class));
					break;
				case 1:
					startActivity(new Intent(MainActivity.this,AroundActivityt.class));
					break;
				default:
					break;
				}
				
			}
		});
		prefUtils = new PrefUtils(this);
		String [] s = null;
		list_right.setAdapter(new RightlistAdapter(this, s));
		
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenuListView = (ListView) findViewById(R.id.left_drawer);
		right_drawer = (RelativeLayout) findViewById(R.id.right_drawer);
		this.showView = mMenuListView;

		// 初始化菜单列表
		mMenuTitles = getResources().getStringArray(R.array.menu_array);
		mMenuListView.setAdapter(leftlistAdapter);
		// new ArrayAdapter<String>(this,R.layout.drawer_list_item, mMenuTitles)
		mMenuListView.setOnItemClickListener(new DrawerItemClickListener());

		// 设置抽屉打开时，主要内容区被自定义阴影覆盖
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// 设置ActionBar可见，并且切换菜单和内容视图
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mMaterialMenuIcon = new MaterialMenuIcon(this, Color.WHITE, Stroke.THIN);
		mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());

		if (savedInstanceState == null) {
			selectItem(0);
		}

	}

	/**
	 * ListView上的Item点击事件
	 * 
	 */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/**
	 * DrawerLayout状态变化监听
	 */
	private class DrawerLayoutStateListener extends
			DrawerLayout.SimpleDrawerListener {
		/**
		 * 当导航菜单滑动的时候被执行
		 */
		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			showView = drawerView;
			if (drawerView == mMenuListView) {// 根据isDirection_left决定执行动画
				mMaterialMenuIcon.setTransformationOffset(
						MaterialMenuDrawable.AnimationState.BURGER_ARROW,
						isDirection_left ? 2 - slideOffset : slideOffset);
			} else if (drawerView == right_drawer) {// 根据isDirection_right决定执行动画
				mMaterialMenuIcon.setTransformationOffset(
						MaterialMenuDrawable.AnimationState.BURGER_ARROW,
						isDirection_right ? 2 - slideOffset : slideOffset);
			}
		}

		/**
		 * 当导航菜单打开时执行
		 */
		@Override
		public void onDrawerOpened(android.view.View drawerView) {
			if (drawerView == mMenuListView) {
				isDirection_left = true;
			} else if (drawerView == right_drawer) {
				isDirection_right = true;
			}
		}

		/**
		 * 当导航菜单关闭时执行
		 */
		@Override
		public void onDrawerClosed(android.view.View drawerView) {
			if (drawerView == mMenuListView) {
				isDirection_left = false;
			} else if (drawerView == right_drawer) {
				isDirection_right = false;
				showView = mMenuListView;
			}
		}
	}

	/**
	 * 切换主视图区域的Fragment
	 * 
	 * @param position
	 */
	private void selectItem(int position) {
		
		if (position == 0) {
			Bundle args = new Bundle();
			FragmentManager fragmentManager = getSupportFragmentManager();
			Fragment fragment = new ContentFragment();
			args.putString("key", mMenuTitles[position]);
			args.putInt("page", page);
			if (bundle != null) {
				args.putBundle("bundle", bundle);
			}
			fragment.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		}else {
			Bundle args = new Bundle();
			FragmentManager fragmentManager = getSupportFragmentManager();
			switch (position) {
			case 1:
				Fragment fragment1 = new ContentFragment();
				args.putString("key", mMenuTitles[position - 1]);
				fragment1.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment1).commit();
				break;
			case 2:
				Fragment fragment2 = new MyAttentionFragement();
				args.putString("key", mMenuTitles[position - 1]);
				fragment2.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment2).commit();
				break;
			case 3:
				Fragment fragment3 = new MyCollectFragement();
				args.putString("key", mMenuTitles[position - 1]);
				fragment3.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment3).commit();
				break;
			case 4:
				Fragment fragment4 = new MyIssueFragement();
				args.putString("key", mMenuTitles[position - 1]);
				fragment4.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment4).commit();
				break;
			case 5:
				Toast.makeText(this, "夜间模式", Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Fragment fragment6 = new SettingFragement();
				args.putString("key", mMenuTitles[position - 1]);
				fragment6.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment6).commit();
				break;
			default:
				break;
		}
		}
	

		// 更新选择后的item和title，然后关闭菜单
		mMenuListView.setItemChecked(position, true);
		if (position != 0) {
			setTitle(mMenuTitles[position - 1]);
		}
		mDrawerLayout.closeDrawer(mMenuListView);
	}

	/**
	 * 点击ActionBar上菜单
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			if (showView == mMenuListView) {
				if (!isDirection_left) { // 左边栏菜单关闭时，打开
					mDrawerLayout.openDrawer(mMenuListView);
				} else {// 左边栏菜单打开时，关闭
					mDrawerLayout.closeDrawer(mMenuListView);
				}
			} else if (showView == right_drawer) {
				if (!isDirection_right) {// 右边栏关闭时，打开
					mDrawerLayout.openDrawer(right_drawer);
				} else {// 右边栏打开时，关闭
					mDrawerLayout.closeDrawer(right_drawer);
				}
			}
			break;
		case R.id.action_personal:
			if (!isDirection_right) {// 右边栏关闭时，打开
				if (showView == mMenuListView) {
					mDrawerLayout.closeDrawer(mMenuListView);
				}
				mDrawerLayout.openDrawer(right_drawer);
			} else {// 右边栏打开时，关闭
				mDrawerLayout.closeDrawer(right_drawer);
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 根据onPostCreate回调的状态，还原对应的icon state
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mMaterialMenuIcon.syncState(savedInstanceState);
	}

	/**
	 * 根据onSaveInstanceState回调的状态，保存当前icon state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mMaterialMenuIcon.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 加载菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static final int REQUEST_CHOOSE_FROM_GALLERY = 0x300;
	public static final int REQUEST_TAKE_PHOTO = 0x301;

	public static Uri mCapturedImageUri;
	RoundedImageView roundheadimg;
	PhotoData photoData;

	private void pickPhoto() {

		Intent toGallery = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		toGallery.setAction(Intent.ACTION_GET_CONTENT);
		toGallery.setType("image/*");
		startActivityForResult(toGallery, REQUEST_CHOOSE_FROM_GALLERY);
	}

	private void shootPhoto() {

		String fileName = GalleryUtils.generateImageName();
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.TITLE, fileName);
		this.mCapturedImageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageUri);

		startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode) {
			return;
		}

		if (REQUEST_CHOOSE_FROM_GALLERY == requestCode) {
			this.mCapturedImageUri = data.getData();
		}

		if (REQUEST_CHOOSE_FROM_GALLERY == requestCode
				|| REQUEST_TAKE_PHOTO == requestCode) {

			photoData = new PhotoData(mCapturedImageUri, 0, null);
			loadRoundImage(getApplicationContext(),
					mCapturedImageUri.toString(), roundheadimg);

		}

	}

	public static void loadRoundImage(Context context, String url,
			ImageView imageView) {

		if (imageView == null || TextUtils.isEmpty(url)) {
			return;
		}
		Transformation transformation = new RoundedTransformationBuilder()
				.borderColor(Color.WHITE).borderWidth(2).cornerRadiusDp(40)
				.oval(false).build();
		int profileAvatarSize = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
						.getDisplayMetrics()));

		Picasso.with(context).load(url)
				.resize(profileAvatarSize, profileAvatarSize).centerCrop()
				.placeholder(R.drawable.ic_tab_button_me)
				.error(R.drawable.ic_tab_button_me).transform(transformation)
				.into(imageView);
	}
	
    private Stack<Object> mDialogs = new java.util.Stack<Object>();
	public void showOptionsDialog(String title, String[] options,
			DialogInterface.OnClickListener listener) {

		// No need to show
		if (isFinishing()) {
			return;
		}

		// Already has one shown, no need to show
		if (!mDialogs.isEmpty()) {
			return;
		}

		AlertDialog.Builder alert = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		alert.setItems(options, listener);
		alert.setTitle(title);

		mDialogs.add(new Object());
		AlertDialog dialog = alert.create();

		// Reset the dialog counts.
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {

				mDialogs.clear();
			}
		});
		
		dialog.show();
	
	}
	// 设置初始时间
	private long firstTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000)
			{
				final Toast toast = Toast.makeText(this, "再按一次退出程序",
						Toast.LENGTH_SHORT);
				toast.show();
				firstTime = secondTime;
			}else {
				prefUtils.clear();
				finish();
			}
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

}
