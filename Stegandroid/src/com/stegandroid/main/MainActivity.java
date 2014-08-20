package com.stegandroid.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.stegandroid.R;
import com.stegandroid.activities.AboutActivity;
import com.stegandroid.activities.DecodeActivity;
import com.stegandroid.activities.EncodeActivity;
import com.stegandroid.activities.SettingsActivity;
import com.stegandroid.activities.SocialNetworkActivity;
import com.stegandroid.configuration.Configuration;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.error.ErrorManager;

public class MainActivity extends Activity {

	private Button _btnEncode;
	private Button _btnDecode;
	private Button _btnSettings;
	private Button _btnSocialNetwork;
	private Button _btnAbout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration.getInstance().loadData(this);
        Preferences.getInstance(this).loadData();
        
        _btnEncode = (Button) findViewById(R.id.btn_main_choose_encode);
        _btnDecode = (Button) findViewById(R.id.btn_main_choose_decode);
        _btnSettings = (Button) findViewById(R.id.btn_main_choose_settings);
        _btnSocialNetwork = (Button) findViewById(R.id.btn_main_choose_social_network);
        _btnAbout = (Button) findViewById(R.id.btn_main_choose_about);
        
        _btnEncode.setOnClickListener(onClickListener);
        _btnDecode.setOnClickListener(onClickListener);
        _btnSettings.setOnClickListener(onClickListener);
        _btnSocialNetwork.setOnClickListener(onClickListener);
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
				case R.id.btn_main_choose_social_network:
					createActivities(SocialNetworkActivity.class);
					break;
				case R.id.btn_main_choose_about:
					createActivities(AboutActivity.class);
					break;
				default:
					ErrorManager.getInstance().addErrorMessage("[Main Activity] Requested activity not known");
					ErrorManager.getInstance().displayErrorMessages(arg0.getContext());
					break;
			}
		}
	};

}
