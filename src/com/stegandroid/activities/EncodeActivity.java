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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stegandroid.R;
import com.stegandroid.directorydialog.ChoosenDirectoryListener;
import com.stegandroid.directorydialog.DirectoryDialog;
import com.stegandroid.parameters.EncodeParameters;
import com.stegandroid.tools.Utils;

public class EncodeActivity extends Activity{

	private final int CHOOSE_FILE_CONTENT = 0;
	private final int CHOOSE_VIDEO_CONTAINER = 1;

	// Graphical components
	private ImageButton _btnBack;
	private ImageButton _btnSettings;
	private Button _btnSelectSourceVideo;
	private Button _btnSelectVideoDestination;
	private Button _btnSelectFileToHide;
	private Button _btnEncode;
	private CheckBox _checkBoxFileToHide;
	private CheckBox _checkBoxTextToHide;
	private EditText _editTextContentToHide;
	
	// Private attributes
	private EncodeParameters _encodeParameters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
        
		_btnBack = (ImageButton) findViewById(R.id.btn_back);
		_btnSettings = (ImageButton) findViewById(R.id.btn_settings);
		_btnSelectSourceVideo = (Button) findViewById(R.id.btn_select_video_source);
		_btnSelectVideoDestination = (Button) findViewById(R.id.btn_select_video_destination);
		_btnSelectFileToHide = (Button) findViewById(R.id.btn_select_file_to_hide);
		_btnEncode = (Button) findViewById(R.id.btn_encode);
		_checkBoxFileToHide = (CheckBox) findViewById(R.id.chk_box_file_to_hide);
		_checkBoxTextToHide = (CheckBox) findViewById(R.id.chk_box_text_to_hide);
		_editTextContentToHide = (EditText) findViewById(R.id.edit_text_content_to_hide);
		
		_btnBack.setOnClickListener(onClickListener);
		_btnSettings.setOnClickListener(onClickListener);
		_btnSelectSourceVideo.setOnClickListener(onClickListener);
		_btnSelectVideoDestination.setOnClickListener(onClickListener);
		_btnSelectFileToHide.setOnClickListener(onClickListener);
		_btnEncode.setOnClickListener(onClickListener);
		_checkBoxFileToHide.setChecked(false);
		_checkBoxTextToHide.setChecked(true);
		_checkBoxFileToHide.setOnCheckedChangeListener(onCheckedChangeListener);
		_checkBoxTextToHide.setOnCheckedChangeListener(onCheckedChangeListener);
		_encodeParameters = new EncodeParameters();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void updateImageViews() {
		if (_encodeParameters.getSrcVideoPath() != null && !_encodeParameters.getSrcVideoPath().isEmpty()) {
			((ImageView) findViewById(R.id.img_view_valid_video_source)).setImageResource(R.drawable.btn_check_buttonless_on);
		}

		if (_encodeParameters.getDestVideoPath() != null && !_encodeParameters.getDestVideoPath().isEmpty()) {
			((ImageView) findViewById(R.id.img_view_valid_video_destination)).setImageResource(R.drawable.btn_check_buttonless_on);
		}

		if (_encodeParameters.getFileToHidePath() != null && !_encodeParameters.getFileToHidePath().isEmpty()) {
			((ImageView) findViewById(R.id.img_view_valid_file_to_hide)).setImageResource(R.drawable.btn_check_buttonless_on);
		}
		
	}
	
	private void showDirectoryChooser() {
		DirectoryDialog dialog = new DirectoryDialog(this);
		dialog.setChoosenDirectoryListener(onChoosenDirectoryListener);
		dialog.show();
	}
	
	private void showFileChooser(int code) {
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
	    intent.addCategory(Intent.CATEGORY_OPENABLE);

	    if (code == CHOOSE_VIDEO_CONTAINER) {
	    	intent.setType("video/*"); 
	    } else {
	    	intent.setType("*/*"); 
	    }

	    try {
	        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.destination_string)), code);
	    } catch (android.content.ActivityNotFoundException ex) {
	        Toast.makeText(this, getResources().getString(R.string.file_manager_error_string), Toast.LENGTH_SHORT).show();
	    }
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case CHOOSE_VIDEO_CONTAINER:
				callbackFileChooserVideoContainer(requestCode, resultCode, data);
				break;
			case CHOOSE_FILE_CONTENT:
				callbackFileChooserFileToHide(requestCode, resultCode, data);
				break;
			default:
				Log.d("DEBUG", "There is a big problem there!");
		}
	}

	private void callbackFileChooserVideoContainer(int requestCode, int resultCode, Intent data) {
		Uri selectedVideoLocation;
		
		if (resultCode == Activity.RESULT_OK) {
			selectedVideoLocation = data.getData();
			
			_encodeParameters.setSrcVideoPath(Utils.getRealPathFromUri(this, selectedVideoLocation));
			if (_encodeParameters.getDestVideoPath() == null || _encodeParameters.getDestVideoPath().isEmpty()) {
				_encodeParameters.setDestVideoPath(Utils.getBasenameFromPath(_encodeParameters.getSrcVideoPath()));
			}
			updateImageViews();
		}
	}
	
	private void callbackFileChooserFileToHide(int requestCode, int resultCode, Intent data) {
		Uri selectedFileLocation;
		
		if (resultCode == Activity.RESULT_OK) {
			selectedFileLocation = data.getData();
			_encodeParameters.setFileToHidePath(Utils.getRealPathFromUri(this, selectedFileLocation));
			updateImageViews();
		}
	}
	
	
	private ChoosenDirectoryListener onChoosenDirectoryListener = new ChoosenDirectoryListener() {

		@Override
		public void onChoosenDir(String directory) {
			_encodeParameters.setDestVideoPath(directory);
			updateImageViews();
		}
	};

	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg0.getId() == R.id.chk_box_file_to_hide && arg1) {
				_editTextContentToHide.setVisibility(View.GONE);
				_checkBoxTextToHide.setChecked(false);
				_encodeParameters.setHideText(false);
			} else if (arg0.getId() == R.id.chk_box_file_to_hide) {
				_editTextContentToHide.setVisibility(View.VISIBLE);
				_checkBoxTextToHide.setChecked(true);
				_encodeParameters.setHideText(true);
			} else if (arg0.getId() == R.id.chk_box_text_to_hide && arg1) {
				((RelativeLayout) findViewById(R.id.relative_layout_content_hide_file)).setVisibility(View.GONE);
				_checkBoxFileToHide.setChecked(false);
				_encodeParameters.setHideText(false);
			} else {
				((RelativeLayout) findViewById(R.id.relative_layout_content_hide_file)).setVisibility(View.VISIBLE);
				_checkBoxFileToHide.setChecked(true);
				_encodeParameters.setHideText(true);
			}
		}
	};
	
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
				case R.id.btn_select_video_source:
					showFileChooser(CHOOSE_VIDEO_CONTAINER);
					break;
				case R.id.btn_select_video_destination:
					showDirectoryChooser();
					break;
				case R.id.btn_select_file_to_hide:
					showFileChooser(CHOOSE_FILE_CONTENT);
					break;
				default:
					Log.d("DEBUG", "There is a big problem there!");
			}
		}
	};
	
}
