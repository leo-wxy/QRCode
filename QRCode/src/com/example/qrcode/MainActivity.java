/**
 * @Title: MainActivity.java
 * @Package com.example.qrcode
 * @Description: TODO
 * @author WangXY xywang@3ti.us
 * @date 2014-7-8 下午7:31:36
 * @version  V1.0
 */
package com.example.qrcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * @ClassName: MainActivity
 * @Description:TODO
 * @author WangXY xywang@3ti.us
 * @date 2014-7-8 下午7:31:36
 */
public class MainActivity extends Activity implements OnClickListener {
	Button saoButton, selButton;
	private Vector<BarcodeFormat> decodeFormats;
	TextView textView;
	private static final int IMAGE_REQUEST_CODE = 0;
	Bitmap scanBitmap;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
		saoButton = (Button) findViewById(R.id.sao);
		selButton = (Button) findViewById(R.id.sel);
		saoButton.setOnClickListener(this);
		selButton.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.show);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sao:
			startActivity(new Intent(MainActivity.this, CaptureActivity.class));
			break;
		case R.id.sel:
			Intent intentFromPhoto = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intentFromPhoto, IMAGE_REQUEST_CODE);
			break;
		default:
			break;

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case IMAGE_REQUEST_CODE:
			Result result = scanningImage(getPath(data));
			if (result == null) {
				Toast.makeText(getApplicationContext(), "图片格式有误", 0).show();
			} else {
				Log.i("123result", result.toString());
				// Log.i("123result", result.getText());
				// 数据返回
				textView.setText(result.toString());
			}
			break;
		}
	}

	protected Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		BitmapLuminanceSource source = new BitmapLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);
		} catch (NotFoundException e) {

			e.printStackTrace();

		} catch (ChecksumException e) {

			e.printStackTrace();

		} catch (FormatException e) {

			e.printStackTrace();

		}

		return null;

	}

	public String getPath(Intent data) {
		Bitmap bm = null;
		ContentResolver resolver = getContentResolver();
		// 获得图片的uri
		try {
			Uri originalUri = data.getData();
			bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
			String[] proj = { MediaStore.Images.Media.DATA };

			// 好像是android多媒体数据库的封装接口，具体的看Android文档

			Cursor cursor = managedQuery(originalUri, proj, null, null, null);

			// 按我个人理解 这个是获得用户选择的图片的索引值

			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			// 将光标移至开头 ，这个很重要，不小心很容易引起越界

			cursor.moveToFirst();

			// 最后根据索引值获取图片路径

			String path = cursor.getString(column_index);
			return path;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}