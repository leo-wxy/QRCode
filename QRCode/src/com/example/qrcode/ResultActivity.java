package com.example.qrcode;



import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {
	ImageView imageView;
	TextView textView;
	Button button;

	String result;
	Bitmap barcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.decode_result);
		imageView = (ImageView) findViewById(R.id.decode_result_bitmap);
		textView = (TextView) findViewById(R.id.decode_result_str);
		ini();
	}

	void ini() {
		imageView.setImageBitmap(Comm.barcode);
		
		textView.setText(Comm.result);
		System.err.println(Comm.result.toString());
	}

}
