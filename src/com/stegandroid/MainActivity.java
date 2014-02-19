package com.stegandroid;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final int REQUEST_VIDEO_RECORD = 1;
	static final int REQUEST_VIDEO_PICK = 2;

	Button btChooseVideo;
	Button btRecordVideo;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btChooseVideo = (Button) findViewById(R.id.bt_choose_video);
		btRecordVideo = (Button) findViewById(R.id.bt_record_video);
		btChooseVideo.setOnClickListener(onClickListener);
		btRecordVideo.setOnClickListener(onClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void recordVideoIntent() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takeVideoIntent, REQUEST_VIDEO_RECORD);
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.bt_choose_video) {
				Intent pickMedia = new Intent(Intent.ACTION_GET_CONTENT);
				pickMedia.setType("video/*");
				startActivityForResult(pickMedia,REQUEST_VIDEO_PICK);

			} else if (v.getId() == R.id.bt_record_video) {
				recordVideoIntent();
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_VIDEO_PICK) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedVideoLocation = data.getData();
				Log.d("DEBUG", selectedVideoLocation.toString());

				Toast.makeText(this, R.string.toast_video_selected, Toast.LENGTH_LONG).show();
			} else
				Log.d("DEBUG", "No video picked");
		} else if (requestCode == REQUEST_VIDEO_RECORD) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedVideoLocation = data.getData();
				Log.d("DEBUG", selectedVideoLocation.toString());

				Toast.makeText(this, R.string.toast_video_recorded, Toast.LENGTH_LONG).show();
			} else
				Log.d("DEBUG", "No video recorded");
		}

	}
}
