package com.stegandroid.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.stegandroid.R;
import com.stegandroid.activities.DecodeActivity;
import com.stegandroid.activities.EncodeActivity;
import com.stegandroid.activities.SettingsActivity;
import com.stegandroid.configuration.Configuration;

/* To do (or not): 
 * Find a way to create tab without using TabActivity
 * since it is deprecated
 */
//@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private Button _btnEncode;
	private Button _btnDecode;
	private Button _btnSettings;
	private Button _btnAbout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration.getInstance(this).loadData();
        
        _btnEncode = (Button) findViewById(R.id.btn_main_choose_encode);
        _btnDecode = (Button) findViewById(R.id.btn_main_choose_decode);
        _btnSettings = (Button) findViewById(R.id.btn_main_choose_settings);
        _btnAbout = (Button) findViewById(R.id.btn_main_choose_about);
        
        _btnEncode.setOnClickListener(onClickListener);
        _btnDecode.setOnClickListener(onClickListener);
        _btnSettings.setOnClickListener(onClickListener);
        _btnAbout.setOnClickListener(onClickListener);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void createActivities(Class<?> claz) {
		Intent intent = new Intent(getApplicationContext(), claz);
		startActivity(intent);
	}
	
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.btn_main_choose_encode:
					createActivities(EncodeActivity.class);
					break;
				case R.id.btn_main_choose_decode:
					createActivities(DecodeActivity.class);
					break;
				case R.id.btn_main_choose_settings:
					createActivities(SettingsActivity.class);
					break;
				case R.id.btn_main_choose_about:
					break;
				default:
					Log.d("DEBUG", "There is a big problem there!");
			}
		}
	};

}
