package com.stegandroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.stegandroid.R;

public class SocialNetworkActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_network);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.social_network, menu);
		return true;
	}

}
