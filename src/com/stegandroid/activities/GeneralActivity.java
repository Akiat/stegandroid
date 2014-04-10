package com.stegandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stegandroid.R;
import com.stegandroid.process.EncodeProcess;

public class GeneralActivity extends Activity {

	final int CHOOSE_VIDEO_CONTAINER = 0;
	final int CHOOSE_VIDEO_DEST = 1;

	private String _pathToVideoContainer;
	private String _pathToVideoDestination;
	
	private Button _btnChooseVideoContainer;
	private Button _btnChooseVideoDest;
	private Button _btnProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_general);

		_btnChooseVideoContainer = (Button) findViewById(R.id.btn_choose_video_container);
		_btnChooseVideoDest = (Button) findViewById(R.id.btn_choose_video_dest);
		_btnProcess = (Button) findViewById(R.id.btn_process);
		
		_btnChooseVideoContainer.setOnClickListener(onClickListener);
		_btnChooseVideoDest.setOnClickListener(onClickListener);
		_btnProcess.setOnClickListener(onClickListener);
	}

	
	private void showFileChooser() {
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
	    intent.addCategory(Intent.CATEGORY_OPENABLE);
	    intent.setType("video/*"); 

	    try {
	        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.lbl_select_video_container)), CHOOSE_VIDEO_CONTAINER);
	    } catch (android.content.ActivityNotFoundException ex) {
	        Toast.makeText(this, getResources().getString(R.string.err_file_manager), Toast.LENGTH_SHORT).show();
	    }
	}
	
	private void callbackFileChooser(int requestCode, int resultCode, Intent data) {
		Uri selectedVideoLocation;
		
		if (resultCode == Activity.RESULT_OK) {
			selectedVideoLocation = data.getData();
			_pathToVideoContainer = selectedVideoLocation.getPath();
			((TextView) findViewById(R.id.label_path_selected_video_container)).setText(_pathToVideoContainer);
		}
	}
	
	private void showDirectoryChooser() {
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
//	    intent.addCategory(Intent.CATEGORY_APP_GALLERY);
//	    intent.setType("video/*"); 
//
//	    try {
//	        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.lbl_select_video_container)), CHOOSE_VIDEO_DEST);
//	    } catch (android.content.ActivityNotFoundException ex) {
//	        Toast.makeText(this, getResources().getString(R.string.err_file_manager), Toast.LENGTH_SHORT).show();
//	    }		
	}

	private void callbackDirectoryChooser(int requestCode, int resultCode, Intent data) {
		Uri selectedVideoDest;
		
		if (resultCode == Activity.RESULT_OK) {
			selectedVideoDest = data.getData();
			_pathToVideoDestination = selectedVideoDest.getPath();
			((TextView) findViewById(R.id.label_path_selected_video_dest)).setText(_pathToVideoDestination);
		}
	}

	private void process() {
		EncodeProcess encodeProcess = new EncodeProcess();

		encodeProcess.encode(_pathToVideoContainer, _pathToVideoDestination);
		
        Toast.makeText(this, "Processing ...", Toast.LENGTH_SHORT).show();
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.general, menu);
		return true;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.btn_choose_video_container:
					showFileChooser();
					break;
				case R.id.btn_choose_video_dest:
					showDirectoryChooser();
					break;
				case R.id.btn_process:
					process();
					break;
				default:
					Log.d("DEBUG", "There is a big problem there!");
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case CHOOSE_VIDEO_CONTAINER:
				callbackFileChooser(requestCode, resultCode, data);
				break;
			case CHOOSE_VIDEO_DEST:
				callbackDirectoryChooser(requestCode, resultCode, data);
				break;
			default:
				Log.d("DEBUG", "There is a big problem there!");
		}
	}
}
