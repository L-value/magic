package com.example.netease;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.baidu.mapapi.common.SysOSUtil;
import com.picture.bean.PhotoData;
import com.picture.myviews.RoundedImageView;
import com.picture.myviews.RoundedTransformationBuilder;
import com.picture.utils.GalleryUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import HaoRan.ImageFilter.AutoAdjustFilter;
import HaoRan.ImageFilter.BannerFilter;
import HaoRan.ImageFilter.BigBrotherFilter;
import HaoRan.ImageFilter.BlackWhiteFilter;
import HaoRan.ImageFilter.BlindFilter;
import HaoRan.ImageFilter.BlockPrintFilter;
import HaoRan.ImageFilter.BrickFilter;
import HaoRan.ImageFilter.BrightContrastFilter;
import HaoRan.ImageFilter.CleanGlassFilter;
import HaoRan.ImageFilter.ColorQuantizeFilter;
import HaoRan.ImageFilter.ColorToneFilter;
import HaoRan.ImageFilter.ComicFilter;
import HaoRan.ImageFilter.EdgeFilter;
import HaoRan.ImageFilter.FeatherFilter;
import HaoRan.ImageFilter.FillPatternFilter;
import HaoRan.ImageFilter.FilmFilter;
import HaoRan.ImageFilter.FocusFilter;
import HaoRan.ImageFilter.GammaFilter;
import HaoRan.ImageFilter.GaussianBlurFilter;
import HaoRan.ImageFilter.Gradient;
import HaoRan.ImageFilter.HslModifyFilter;
import HaoRan.ImageFilter.IImageFilter;
import HaoRan.ImageFilter.IllusionFilter;
import HaoRan.ImageFilter.InvertFilter;
import HaoRan.ImageFilter.LensFlareFilter;
import HaoRan.ImageFilter.LightFilter;
import HaoRan.ImageFilter.LomoFilter;
import HaoRan.ImageFilter.MirrorFilter;
import HaoRan.ImageFilter.MistFilter;
import HaoRan.ImageFilter.MonitorFilter;
import HaoRan.ImageFilter.MosaicFilter;
import HaoRan.ImageFilter.NeonFilter;
import HaoRan.ImageFilter.NightVisionFilter;
import HaoRan.ImageFilter.NoiseFilter;
import HaoRan.ImageFilter.OilPaintFilter;
import HaoRan.ImageFilter.OldPhotoFilter;
import HaoRan.ImageFilter.PaintBorderFilter;
import HaoRan.ImageFilter.PixelateFilter;
import HaoRan.ImageFilter.PosterizeFilter;
import HaoRan.ImageFilter.RadialDistortionFilter;
import HaoRan.ImageFilter.RainBowFilter;
import HaoRan.ImageFilter.RaiseFrameFilter;
import HaoRan.ImageFilter.RectMatrixFilter;
import HaoRan.ImageFilter.ReflectionFilter;
import HaoRan.ImageFilter.ReliefFilter;
import HaoRan.ImageFilter.SaturationModifyFilter;
import HaoRan.ImageFilter.SceneFilter;
import HaoRan.ImageFilter.SepiaFilter;
import HaoRan.ImageFilter.SharpFilter;
import HaoRan.ImageFilter.ShiftFilter;
import HaoRan.ImageFilter.SmashColorFilter;
import HaoRan.ImageFilter.SoftGlowFilter;
import HaoRan.ImageFilter.SupernovaFilter;
import HaoRan.ImageFilter.ThreeDGridFilter;
import HaoRan.ImageFilter.ThresholdFilter;
import HaoRan.ImageFilter.TileReflectionFilter;
import HaoRan.ImageFilter.TintFilter;
import HaoRan.ImageFilter.VideoFilter;
import HaoRan.ImageFilter.VignetteFilter;
import HaoRan.ImageFilter.VintageFilter;
import HaoRan.ImageFilter.WaterWaveFilter;
import HaoRan.ImageFilter.XRadiationFilter;
import HaoRan.ImageFilter.YCBCrLinearFilter;
import HaoRan.ImageFilter.ZoomBlurFilter;
import HaoRan.ImageFilter.Distort.BulgeFilter;
import HaoRan.ImageFilter.Distort.RippleFilter;
import HaoRan.ImageFilter.Distort.TwistFilter;
import HaoRan.ImageFilter.Distort.WaveFilter;
import HaoRan.ImageFilter.Textures.CloudsTexture;
import HaoRan.ImageFilter.Textures.LabyrinthTexture;
import HaoRan.ImageFilter.Textures.MarbleTexture;
import HaoRan.ImageFilter.Textures.TextileTexture;
import HaoRan.ImageFilter.Textures.TexturerFilter;
import HaoRan.ImageFilter.Textures.WoodTexture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;

public class GetImageActivity extends Activity implements OnClickListener {

	private ImageView imageview;
	private Button check, make, add, sure;
	private EditText watertext;
	private TextView textview;
	private Button submit;
	private Button say;

	private Gallery galleryFilter;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 2) {
				Toast.makeText(GetImageActivity.this, (String)msg.obj, 800).show();
			}
			
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getimage);
		imageview = (ImageView) findViewById(R.id.img);
		check = (Button) findViewById(R.id.check);
		make = (Button) findViewById(R.id.make);
		add = (Button) findViewById(R.id.add);
		sure = (Button) findViewById(R.id.sure);
		watertext = (EditText) findViewById(R.id.watertext);
		textview = (TextView) findViewById(R.id.runtime);
		galleryFilter = (Gallery) findViewById(R.id.galleryFilter);
		submit = (Button) findViewById(R.id.submit);
		say = (Button) findViewById(R.id.say);
		LoadImageFilter();
		addListener();
	}

	private void initPost(Uri uri) {
	    String[] proj = { MediaStore.Images.Media.DATA };   
	    Cursor actualimagecursor = this.managedQuery(uri,proj,null,null,null);  
	    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   
	    actualimagecursor.moveToFirst();   
	    String img_path = actualimagecursor.getString(actual_image_column_index);  
	    System.out.println(img_path);
		File file = new File(img_path);
		System.out.println(file.getName()+"name");
		RequestBody fileBody = RequestBody.create(
				MediaType.parse("application/octet-stream"), file);

		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addPart(
						Headers.of("Content-Disposition",
								"form-data; name=\"UploadForm[userName]\""),
						RequestBody.create(null, "admin"))
				.addPart(
						Headers.of("Content-Disposition",
								"form-data; name=\"UploadForm[imageFile]\""),
						fileBody).build();
		try {
			System.out.println(fileBody.contentType()+"..."+fileBody.toString()+"..."+fileBody.contentLength());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Request request = new Request.Builder()
				.url("http://test.nsscn.org/helloYii/web/index.php?r=site/upload")
				.post(requestBody).build();

		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				System.out.println("fail"+arg0.body().toString()+"arg1"+arg1.getMessage());
				System.out.println(arg0.method());
			}

			@Override
			public void onResponse(Response arg0) throws IOException {
				System.out.println("ok"+arg0.body().string());
				System.out.println("message"+arg0.message());
				System.out.println("code"+arg0.code());
				Message message = Message.obtain();
				message.obj = arg0.body().string();
				message.what = 2;
				handler.sendMessage(message);
			}
			// ...
		});
	}

	private void addListener() {
		check.setOnClickListener(this);
		make.setOnClickListener(this);
		add.setOnClickListener(this);
		sure.setOnClickListener(this);
		submit.setOnClickListener(this);
		say.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check:
			showOptionsDialog(null, new String[] {
					getString(R.string.me_profile_camera),
					getString(R.string.me_profile_album) },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface,
								int i) {
							if (i == 0) {
								shootPhoto();
							}

							if (i == 1) {
								pickPhoto();
							}
						}
					});
			break;
		case R.id.add:
			watertext.setVisibility(View.VISIBLE);
			sure.setVisibility(View.VISIBLE);
			break;
		case R.id.sure:
			Log.i("info", imageview.getBackground().toString());
			if (null != watertext.getText().toString()
					&& !"".equals(watertext.getText().toString())
					&& null != imageview.getBackground()) {
				Toast.makeText(this, "water2" + watertext.getText().toString(),
						800).show();
				BitmapDrawable drawable = (BitmapDrawable) imageview
						.getBackground();
				Bitmap bitmap = createWaterMark(drawable.getBitmap(), watertext
						.getText().toString());
				imageview.setBackground(new BitmapDrawable(bitmap));
			}
			break;
		case R.id.make:

			break;
		case R.id.submit:
			BitmapDrawable bitmapDrawable = (BitmapDrawable) imageview.getBackground();
			Bitmap bitmap = bitmapDrawable.getBitmap();
			Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
			initPost(uri);
			break;
		case R.id.say:
			BitmapDrawable bitmapDrawable2 = (BitmapDrawable) imageview.getBackground();
			Bitmap bitmap2 = bitmapDrawable2.getBitmap();
			Intent intent = new Intent(GetImageActivity.this,StateActivity.class);
			intent.putExtra("image",bitmap2);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	private Bitmap createWaterMark(Bitmap bitmap, String text) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap2);
		Paint p = new Paint();
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		p.setColor(Color.RED);
		p.setTypeface(font);
		p.setTextSize(100);
		p.setDither(true); // 获取跟清晰的图像采样
		p.setFilterBitmap(true);// 过滤一些
		canvas.drawBitmap(bitmap, 0, 0, p);
		canvas.drawText(text, 3, 50, p);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return bitmap2;
	}

	//
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

	public static Uri mCapturedImageUri;
	PhotoData photoData;

	private void pickPhoto() {

		Intent toGallery = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		toGallery.setAction(Intent.ACTION_GET_CONTENT);
		toGallery.setType("image/*");
		startActivityForResult(toGallery,
				MainActivity.REQUEST_CHOOSE_FROM_GALLERY);
	}

	private void shootPhoto() {

		String fileName = GalleryUtils.generateImageName();
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.TITLE, fileName);
		this.mCapturedImageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageUri);

		startActivityForResult(cameraIntent, MainActivity.REQUEST_TAKE_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode) {
			return;
		}

		if (MainActivity.REQUEST_CHOOSE_FROM_GALLERY == requestCode) {
			this.mCapturedImageUri = data.getData();
		}

		if (MainActivity.REQUEST_CHOOSE_FROM_GALLERY == requestCode
				|| MainActivity.REQUEST_TAKE_PHOTO == requestCode) {

			photoData = new PhotoData(mCapturedImageUri, 0, null);
			System.out.println(mCapturedImageUri.getPath());
			try {
				imageview.setBackground(new BitmapDrawable(
						MediaStore.Images.Media.getBitmap(getContentResolver(),
								mCapturedImageUri)));
				

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private OkHttpClient okHttpClient = new OkHttpClient();

	/**
	 * 加载图片filter
	 */
	private void LoadImageFilter() {
		Gallery gallery = (Gallery) findViewById(R.id.galleryFilter);
		final ImageFilterAdapter filterAdapter = new ImageFilterAdapter(
				GetImageActivity.this);
		gallery.setAdapter(new ImageFilterAdapter(GetImageActivity.this));
		gallery.setSelection(2);
		gallery.setAnimationDuration(3000);
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				IImageFilter filter = (IImageFilter) filterAdapter
						.getItem(position);
				new processImageTask(GetImageActivity.this, filter).execute();
			}
		});
	}

	public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
		private IImageFilter filter;
		private Activity activity = null;

		public processImageTask(Activity activity, IImageFilter imageFilter) {
			this.filter = imageFilter;
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			textview.setVisibility(View.VISIBLE);
		}

		public Bitmap doInBackground(Void... params) {
			HaoRan.ImageFilter.Image img = null;
			try {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) imageview
						.getBackground();
				Bitmap bitmap = bitmapDrawable.getBitmap();
				img = new HaoRan.ImageFilter.Image(bitmap);
				if (filter != null) {
					img = filter.process(img);
					img.copyPixelsFromBuffer();
				}
				return img.getImage();
			} catch (Exception e) {
				if (img != null && img.destImage.isRecycled()) {
					img.destImage.recycle();
					img.destImage = null;
					System.gc(); // 提醒系统及时回收
				}
			} finally {
				if (img != null && img.image.isRecycled()) {
					img.image.recycle();
					img.image = null;
					System.gc(); // 提醒系统及时回收
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				super.onPostExecute(result);
				imageview.setBackground(new BitmapDrawable(result));
				textview.setVisibility(View.GONE);
			}
		}
	}

	class ImageFilterAdapter extends BaseAdapter {
		private class FilterInfo {
			public int filterID;
			public IImageFilter filter;

			public FilterInfo(int filterID, IImageFilter filter) {
				this.filterID = filterID;
				this.filter = filter;
			}
		}

		private Context mContext;
		private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();

		public ImageFilterAdapter(Context c) {
			mContext = c;

			// 99种效果

			// v0.4
			filterArray.add(new FilterInfo(R.drawable.video_filter1,
					new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_STAGGERED)));
			filterArray.add(new FilterInfo(R.drawable.video_filter2,
					new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_TRIPED)));
			filterArray.add(new FilterInfo(R.drawable.video_filter3,
					new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_3X3)));
			filterArray.add(new FilterInfo(R.drawable.video_filter4,
					new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_DOTS)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter1,
					new TileReflectionFilter(20, 8, 45, (byte) 1)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter2,
					new TileReflectionFilter(20, 8, 45, (byte) 2)));
			filterArray.add(new FilterInfo(R.drawable.fillpattern_filter,
					new FillPatternFilter(GetImageActivity.this,
							R.drawable.texture1)));
			filterArray.add(new FilterInfo(R.drawable.fillpattern_filter1,
					new FillPatternFilter(GetImageActivity.this,
							R.drawable.texture2)));
			filterArray.add(new FilterInfo(R.drawable.mirror_filter1,
					new MirrorFilter(true)));
			filterArray.add(new FilterInfo(R.drawable.mirror_filter2,
					new MirrorFilter(false)));
			filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter,
					new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.3f,
							0.3f))));
			filterArray
					.add(new FilterInfo(R.drawable.ycb_crlinear_filter2,
							new YCBCrLinearFilter(new YCBCrLinearFilter.Range(
									-0.276f, 0.163f),
									new YCBCrLinearFilter.Range(-0.202f, 0.5f))));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter,
					new TexturerFilter(new CloudsTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter1,
					new TexturerFilter(new LabyrinthTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter2,
					new TexturerFilter(new MarbleTexture(), 1.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter3,
					new TexturerFilter(new WoodTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter4,
					new TexturerFilter(new TextileTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter,
					new HslModifyFilter(20f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter0,
					new HslModifyFilter(40f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter1,
					new HslModifyFilter(60f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter2,
					new HslModifyFilter(80f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter3,
					new HslModifyFilter(100f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter4,
					new HslModifyFilter(150f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter5,
					new HslModifyFilter(200f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter6,
					new HslModifyFilter(250f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter7,
					new HslModifyFilter(300f)));

			// v0.3
			filterArray.add(new FilterInfo(R.drawable.zoomblur_filter,
					new ZoomBlurFilter(30)));
			filterArray.add(new FilterInfo(R.drawable.threedgrid_filter,
					new ThreeDGridFilter(16, 100)));
			filterArray.add(new FilterInfo(R.drawable.colortone_filter,
					new ColorToneFilter(Color.rgb(33, 168, 254), 192)));
			filterArray.add(new FilterInfo(R.drawable.colortone_filter2,
					new ColorToneFilter(0x00FF00, 192)));// green
			filterArray.add(new FilterInfo(R.drawable.colortone_filter3,
					new ColorToneFilter(0xFF0000, 192)));// blue
			filterArray.add(new FilterInfo(R.drawable.colortone_filter4,
					new ColorToneFilter(0x00FFFF, 192)));// yellow
			filterArray.add(new FilterInfo(R.drawable.softglow_filter,
					new SoftGlowFilter(10, 0.1f, 0.1f)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter,
					new TileReflectionFilter(20, 8)));
			filterArray.add(new FilterInfo(R.drawable.blind_filter1,
					new BlindFilter(true, 96, 100, 0xffffff)));
			filterArray.add(new FilterInfo(R.drawable.blind_filter2,
					new BlindFilter(false, 96, 100, 0x000000)));
			filterArray.add(new FilterInfo(R.drawable.raiseframe_filter,
					new RaiseFrameFilter(20)));
			filterArray.add(new FilterInfo(R.drawable.shift_filter,
					new ShiftFilter(10)));
			filterArray.add(new FilterInfo(R.drawable.wave_filter,
					new WaveFilter(25, 10)));
			filterArray.add(new FilterInfo(R.drawable.bulge_filter,
					new BulgeFilter(-97)));
			filterArray.add(new FilterInfo(R.drawable.twist_filter,
					new TwistFilter(27, 106)));
			filterArray.add(new FilterInfo(R.drawable.ripple_filter,
					new RippleFilter(38, 15, true)));
			filterArray.add(new FilterInfo(R.drawable.illusion_filter,
					new IllusionFilter(3)));
			filterArray.add(new FilterInfo(R.drawable.supernova_filter,
					new SupernovaFilter(0x00FFFF, 20, 100)));
			filterArray.add(new FilterInfo(R.drawable.lensflare_filter,
					new LensFlareFilter()));
			filterArray.add(new FilterInfo(R.drawable.posterize_filter,
					new PosterizeFilter(2)));
			filterArray.add(new FilterInfo(R.drawable.gamma_filter,
					new GammaFilter(50)));
			filterArray.add(new FilterInfo(R.drawable.sharp_filter,
					new SharpFilter()));

			// v0.2
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new ComicFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new SceneFilter(5f, Gradient.Scene())));// green
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new SceneFilter(5f, Gradient.Scene1())));// purple
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new SceneFilter(5f, Gradient.Scene2())));// blue
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new SceneFilter(5f, Gradient.Scene3())));
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new FilmFilter(80f)));
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new FocusFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new CleanGlassFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new PaintBorderFilter(0x00FF00)));// green
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new PaintBorderFilter(0x00FFFF)));// yellow
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new PaintBorderFilter(0xFF0000)));// blue
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new LomoFilter()));

			// v0.1
			filterArray.add(new FilterInfo(R.drawable.invert_filter,
					new InvertFilter()));
			filterArray.add(new FilterInfo(R.drawable.blackwhite_filter,
					new BlackWhiteFilter()));
			filterArray.add(new FilterInfo(R.drawable.edge_filter,
					new EdgeFilter()));
			filterArray.add(new FilterInfo(R.drawable.pixelate_filter,
					new PixelateFilter()));
			filterArray.add(new FilterInfo(R.drawable.neon_filter,
					new NeonFilter()));
			filterArray.add(new FilterInfo(R.drawable.bigbrother_filter,
					new BigBrotherFilter()));
			filterArray.add(new FilterInfo(R.drawable.monitor_filter,
					new MonitorFilter()));
			filterArray.add(new FilterInfo(R.drawable.relief_filter,
					new ReliefFilter()));
			filterArray.add(new FilterInfo(R.drawable.brightcontrast_filter,
					new BrightContrastFilter()));
			filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,
					new SaturationModifyFilter()));
			filterArray.add(new FilterInfo(R.drawable.threshold_filter,
					new ThresholdFilter()));
			filterArray.add(new FilterInfo(R.drawable.noisefilter,
					new NoiseFilter()));
			filterArray.add(new FilterInfo(R.drawable.banner_filter1,
					new BannerFilter(10, true)));
			filterArray.add(new FilterInfo(R.drawable.banner_filter2,
					new BannerFilter(10, false)));
			filterArray.add(new FilterInfo(R.drawable.rectmatrix_filter,
					new RectMatrixFilter()));
			filterArray.add(new FilterInfo(R.drawable.blockprint_filter,
					new BlockPrintFilter()));
			filterArray.add(new FilterInfo(R.drawable.brick_filter,
					new BrickFilter()));
			filterArray.add(new FilterInfo(R.drawable.gaussianblur_filter,
					new GaussianBlurFilter()));
			filterArray.add(new FilterInfo(R.drawable.light_filter,
					new LightFilter()));
			filterArray.add(new FilterInfo(R.drawable.mosaic_filter,
					new MistFilter()));
			filterArray.add(new FilterInfo(R.drawable.mosaic_filter,
					new MosaicFilter()));
			filterArray.add(new FilterInfo(R.drawable.oilpaint_filter,
					new OilPaintFilter()));
			filterArray.add(new FilterInfo(R.drawable.radialdistortion_filter,
					new RadialDistortionFilter()));
			filterArray.add(new FilterInfo(R.drawable.reflection1_filter,
					new ReflectionFilter(true)));
			filterArray.add(new FilterInfo(R.drawable.reflection2_filter,
					new ReflectionFilter(false)));
			filterArray.add(new FilterInfo(R.drawable.saturationmodify_filter,
					new SaturationModifyFilter()));
			filterArray.add(new FilterInfo(R.drawable.smashcolor_filter,
					new SmashColorFilter()));
			filterArray.add(new FilterInfo(R.drawable.tint_filter,
					new TintFilter()));
			filterArray.add(new FilterInfo(R.drawable.vignette_filter,
					new VignetteFilter()));
			filterArray.add(new FilterInfo(R.drawable.autoadjust_filter,
					new AutoAdjustFilter()));
			filterArray.add(new FilterInfo(R.drawable.colorquantize_filter,
					new ColorQuantizeFilter()));
			filterArray.add(new FilterInfo(R.drawable.waterwave_filter,
					new WaterWaveFilter()));
			filterArray.add(new FilterInfo(R.drawable.vintage_filter,
					new VintageFilter()));
			filterArray.add(new FilterInfo(R.drawable.oldphoto_filter,
					new OldPhotoFilter()));
			filterArray.add(new FilterInfo(R.drawable.sepia_filter,
					new SepiaFilter()));
			filterArray.add(new FilterInfo(R.drawable.rainbow_filter,
					new RainBowFilter()));
			filterArray.add(new FilterInfo(R.drawable.feather_filter,
					new FeatherFilter()));
			filterArray.add(new FilterInfo(R.drawable.xradiation_filter,
					new XRadiationFilter()));
			filterArray.add(new FilterInfo(R.drawable.nightvision_filter,
					new NightVisionFilter()));

			filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,
					null/* 此处会生成原图效果 */));
		}

		public int getCount() {
			return filterArray.size();
		}

		public Object getItem(int position) {
			return position < filterArray.size() ? filterArray.get(position).filter
					: null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Bitmap bmImg = BitmapFactory
					.decodeResource(mContext.getResources(),
							filterArray.get(position).filterID);
			int width = 100;// bmImg.getWidth();
			int height = 100;// bmImg.getHeight();
			bmImg.recycle();
			ImageView imageview = new ImageView(mContext);
			imageview.setImageResource(filterArray.get(position).filterID);
			imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
			imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置显示比例类型
			return imageview;
		}
	};

}
