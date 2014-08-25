package com.stegandroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.stegandroid.R;
import com.stegandroid.error.ErrorManager;

public class SocialNetworkActivity extends Activity {

	private ImageButton _btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_network);
		
		_btnBack = (ImageButton) findViewById(R.id.btn_back_social_network);
		_btnBack.setOnClickListener(onClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.social_network, menu);
		return true;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.btn_back_social_network:
					finish();
					break;
				default:
					ErrorManager.getInstance().addErrorMessage("[Social Activity] Click event not known");
					ErrorManager.getInstance().displayErrorMessages(arg0.getContext());
					break;
			}
		}
	};
	
}
