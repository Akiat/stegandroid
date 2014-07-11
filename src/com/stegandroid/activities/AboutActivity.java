package com.stegandroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.stegandroid.R;

public class AboutActivity extends Activity {

	private ImageButton _btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
		_btnBack = (ImageButton) findViewById(R.id.btn_back);
		_btnBack.setOnClickListener(onClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.btn_back:
					finish();
					break;
				default:
					Log.d("DEBUG", "There is a big problem there!");
			}
		}
	};

	
}
