package com.example.netease;

import java.io.File;
import java.io.IOException;

import com.picture.bean.Community;
import com.picture.fragements.CommunityFragement;
import com.picture.fragements.FindFragement;
import com.picture.utils.PrefUtils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class StateActivity extends Activity implements OnClickListener {

	private Button submit;
	private EditText message;
	private ImageView img;
	private Bitmap bitmap;
	private PrefUtils prefUtils;
	private OkHttpClient mOkHttpClient;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 || msg.what == 2) {
				Toast.makeText(StateActivity.this, "上传成功", 800).show();
				FindFragement.FirstCome = true;
			} else {
				Toast.makeText(StateActivity.this, "上传失败", 800).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_state);
		img = (ImageView) findViewById(R.id.showImage);
		message = (EditText) findViewById(R.id.message);
		submit = (Button) findViewById(R.id.submit);
		bitmap = getIntent().getParcelableExtra("image");
		img.setBackground(new BitmapDrawable(bitmap));
		submit.setOnClickListener(this);
		prefUtils = new PrefUtils(this);
		mOkHttpClient = new OkHttpClient();
	}

	// 发状态用http://test.nsscn.org:9000/helloYii/web/index.php?r=reply/publish
	//
	// null 2016/3/24 12:09:53
	// 传user_id cotext title imags space
	String name;
	@Override
	public void onClick(View v) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getBackground();
		Bitmap bitmap = bitmapDrawable.getBitmap();
		Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
				getContentResolver(), bitmap, null, null));
		initPost(uri);
		RequestBody requestBody = new FormBody.Builder()
				.add("user_id", prefUtils.getString("id"))
				.add("cotext",
						message.getText() == null ? "" : message.getText()
								.toString()).add("imgs", name)
								.add("title", System.currentTimeMillis()+"")
				.add("space", "甘井子区").build();
		String url = "http://test.nsscn.org/helloYii/web/index.php?r=reply/publish";
		Request request = new Request.Builder().url(url).post(requestBody)
				.build();
		mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				Message message = Message.obtain();
				message.what = 1;
				Log.i("info", "message" + arg0.message()+""+arg0.body().string());
				message.obj = arg0.body().string();
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {

			}
		});
	}

	OkHttpClient okHttpClient = new OkHttpClient();

	private void initPost(Uri uri) {

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = this.managedQuery(uri, proj, null, null,
				null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor
				.getString(actual_image_column_index);
		System.out.println(img_path);
		File file = new File(img_path);
		name = file.getName();
		System.out.println(file.toString());
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
						fileBody)
				.addPart(Headers.of("Content-Disposition",
								"form-data; name=\"UploadForm[imageName]\""),
								RequestBody.create(null, name)).build();
		try {
			System.out.println(fileBody.contentType() + "..."
					+ fileBody.toString() + "..." + fileBody.contentLength());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Request request = new Request.Builder()
				.url("http://test.nsscn.org/helloYii/web/index.php?r=site/upload")
				.post(requestBody).build();

		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				System.out.println("fail" + arg0.body().toString() + "arg1"
						+ arg1.getMessage());
				System.out.println(arg0.method());

			}

			@Override
			public void onResponse(Response arg0) throws IOException {
				System.out.println("ok" + arg0.body().string());
				System.out.println("message" + arg0.message());
				System.out.println("code" + arg0.code());
				Message message = Message.obtain();
				message.obj = arg0.body().string();
				message.what = 2;
				handler.sendMessage(message);
			}
			// ...
		});
	}
}
