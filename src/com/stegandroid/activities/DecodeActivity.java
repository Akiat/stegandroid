package com.stegandroid.activities;

import com.stegandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class DecodeActivity extends Activity {

	private ImageButton _btnBack;
	private ImageButton _btnSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        
		_btnBack = (ImageButton) findViewById(R.id.btn_back);
		_btnSettings = (ImageButton) findViewById(R.id.btn_settings);
		
		_btnBack.setOnClickListener(onClickListener);
		_btnSettings.setOnClickListener(onClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.decode, menu);
		return true;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.btn_back:
					finish();
					break;
				case R.id.btn_settings:
					Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
					startActivity(intent);
					break;
				default:
					Log.d("DEBUG", "There is a big problem there!");
			}
		}
	};
}
