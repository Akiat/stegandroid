package com.stegandroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.stegandroid.R;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.controller.EncodeParametersController;
import com.stegandroid.directorydialog.ChoosenDirectoryListener;
import com.stegandroid.directorydialog.DirectoryDialog;
import com.stegandroid.error.ErrorManager;
import com.stegandroid.parameters.EncodeParameters;
import com.stegandroid.process.EncodeProcess;
import com.stegandroid.tools.Utils;

public class EncodeActivity extends Activity{

	private final int CHOOSE_FILE_CONTENT = 0;
	private final int CHOOSE_VIDEO_CONTAINER = 1;
	private final int RECORD_VIDEO = 2;
	private final int SETTINGS_ACCESS = 3;

	// Graphical components
	private ImageButton _btnBack;
	private ImageButton _btnCamera;
	private ImageButton _btnSettings;
	private Button _btnSelectSourceVideo;
	private Button _btnSelectVideoDestination;
	private Button _btnSelectFileToHide;
	private Button _btnEncode;
	private CheckBox _checkBoxFileToHide;
	private CheckBox _checkBoxTextToHide;
	private EditText _editTextContentToHide;
	private EditText _editTextCryptographyKeyEncode;
	private LinearLayout _linearLayoutCryptographyKeyEncode;
	
	// Private attributes
	private EncodeParameters _encodeParameters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
        
		_btnBack = (ImageButton) findViewById(R.id.btn_back_encode);
		_btnCamera = (ImageButton) findViewById(R.id.btn_camera);
		_btnSettings = (ImageButton) findViewById(R.id.btn_settings_encode);
		_btnSelectSourceVideo = (Button) findViewById(R.id.btn_select_video_source_encode);
		_btnSelectVideoDestination = (Button) findViewById(R.id.btn_select_video_destination_encode);
		_btnSelectFileToHide = (Button) findViewById(R.id.btn_select_file_to_hide);
		_btnEncode = (Button) findViewById(R.id.btn_encode);
		_checkBoxFileToHide = (CheckBox) findViewById(R.id.chk_box_file_to_hide);
		_checkBoxTextToHide = (CheckBox) findViewById(R.id.chk_box_text_to_hide);
		_editTextContentToHide = (EditText) findViewById(R.id.edit_text_content_to_hide);
		_editTextCryptographyKeyEncode = (EditText) findViewById(R.id.edit_text_cryptography_key_encode);
		_linearLayoutCryptographyKeyEncode = (LinearLayout) findViewById(R.id.linear_layout_cryptography_key_encode);
		
		_btnBack.setOnClickListener(onClickListener);
		_btnCamera.setOnClickListener(onClickListener);
		_btnSettings.setOnClickListener(onClickListener);
		_btnSelectSourceVideo.setOnClickListener(onClickListener);
		_btnSelectVideoDestination.setOnClickListener(onClickListener);
		_btnSelectFileToHide.setOnClickListener(onClickListener);
		_btnEncode.setOnClickListener(onClickListener);
		_checkBoxFileToHide.setChecked(false);
		_checkBoxTextToHide.setChecked(true);
		_checkBoxFileToHide.setOnCheckedChangeListener(onCheckedChangeListener);
		_checkBoxTextToHide.setOnCheckedChangeListener(onCheckedChangeListener);
		_editTextCryptographyKeyEncode.addTextChangedListener(onTextChangedListenerCryptographyKey);
		_editTextContentToHide.addTextChangedListener(onTextChangedListenerTextToHide);
		
		_encodeParameters = new EncodeParameters();
		
		updateImageViews();
		updateLinearLayoutCryptographyVisibility();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.encode, menu);
		return true;
	}
		
	private void updateLinearLayoutCryptographyVisibility() {
		if (Preferences.getInstance() != null) {
			if (Preferences.getInstance().getUseCryptography()) {
				_linearLayoutCryptographyKeyEncode.setVisibility(View.VISIBLE);
			} else {
				_linearLayoutCryptographyKeyEncode.setVisibility(View.GONE);
			}
		}
	}
	
	private void updateImageViews() {
		EncodeParametersController controller = new EncodeParametersController(true);
		
		if (controller.controlSrcVideoPath(_encodeParameters)) {
			((ImageView) findViewById(R.id.img_view_valid_video_source_encode)).setImageResource(R.drawable.btn_check_buttonless_on);
		} else {
			((ImageView) findViewById(R.id.img_view_valid_video_source_encode)).setImageResource(R.drawable.ic_delete);
		}

		if (controller.controlDestVideoPath(_encodeParameters)) {
			((ImageView) findViewById(R.id.img_view_valid_video_destination_encode)).setImageResource(R.drawable.btn_check_buttonless_on);
		} else {
			((ImageView) findViewById(R.id.img_view_valid_video_destination_encode)).setImageResource(R.drawable.ic_delete);
		}

		if (controller.controlContentToHide(_encodeParameters)) {
			((ImageView) findViewById(R.id.img_view_valid_content_to_hide_encode)).setImageResource(R.drawable.btn_check_buttonless_on);
		} else {
			((ImageView) findViewById(R.id.img_view_valid_content_to_hide_encode)).setImageResource(R.drawable.ic_delete);
		}
		
		if (controller.controlCryptographyKey(_encodeParameters)) {
			((ImageView) findViewById(R.id.img_view_valid_key_length_encode)).setImageResource(R.drawable.btn_check_buttonless_on);
		} else {
			((ImageView) findViewById(R.id.img_view_valid_key_length_encode)).setImageResource(R.drawable.ic_delete);
		}
	}
	
	private void recordVideo() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		
	    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takeVideoIntent, RECORD_VIDEO); 
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
	        Toast.makeText(this, getResources().getString(R.string.error_file_manager_string), Toast.LENGTH_SHORT).show();
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
			case SETTINGS_ACCESS:
				updateLinearLayoutCryptographyVisibility();
				updateImageViews();
				break;
			case RECORD_VIDEO:
				if (resultCode != 0)
					Toast.makeText(this, "Video saved", Toast.LENGTH_LONG).show();
				break;
			default:
				ErrorManager.getInstance().addErrorMessage("[Encode Activity] Activity result not known");
				ErrorManager.getInstance().displayErrorMessages(this);
				break;
		}
	}

	private void callbackFileChooserVideoContainer(int requestCode, int resultCode, Intent data) {
		Uri selectedVideoLocation;
		
		if (resultCode == Activity.RESULT_OK) {
			selectedVideoLocation = data.getData();
			
			if (selectedVideoLocation != null) {
				_encodeParameters.setSourceVideoPath(Utils.getRealPathFromUri(this, selectedVideoLocation));
				if (_encodeParameters.getDestinationVideoDirectory() == null || _encodeParameters.getDestinationVideoDirectory().isEmpty()) {
					_encodeParameters.setDestinationVideoDirectory(Utils.getBasenameFromPath(_encodeParameters.getSourceVideoPath()));
				}
			} else {
				_encodeParameters.setSourceVideoPath("");
			}
			updateImageViews();
		}
	}
	
	private void callbackFileChooserFileToHide(int requestCode, int resultCode, Intent data) {
		Uri selectedFileLocation;
		
		if (resultCode == Activity.RESULT_OK) {
			selectedFileLocation = data.getData();
			if (selectedFileLocation != null) {
				_encodeParameters.setFileToHidePath(Utils.getRealPathFromUri(this, selectedFileLocation));
			} else {
				_encodeParameters.setFileToHidePath("");
			}
			updateImageViews();
		}
	}
	
	private void process() {
		EncodeParametersController controller;
		boolean displayError = false;
		
		ProgressDialog loading = ProgressDialog.show(this, "Loading", "Stegandroid hiding your data...");
		_encodeParameters.setTextToHide(_editTextContentToHide.getText().toString());
		controller = new EncodeParametersController(false);
		if (controller.controlAllData(_encodeParameters)){
			EncodeProcess process = new EncodeProcess();
			if (!process.encode(_encodeParameters)) {
				displayError = true;
			}
		} else {
			displayError = true;
		}
		if (displayError) {
			ErrorManager.getInstance().displayErrorMessages(this);
		}
		loading.dismiss();
	}
		
	private ChoosenDirectoryListener onChoosenDirectoryListener = new ChoosenDirectoryListener() {

		@Override
		public void onChoosenDir(String directory) {
			_encodeParameters.setDestinationVideoDirectory(directory);
			updateImageViews();
		}
	};

	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg0.getId() == R.id.chk_box_file_to_hide && arg1) {
				_editTextContentToHide.setVisibility(View.GONE);
				_btnSelectFileToHide.setVisibility(View.VISIBLE);
				_checkBoxTextToHide.setChecked(false);
				_encodeParameters.setHidingText(false);
			} else if (arg0.getId() == R.id.chk_box_file_to_hide) {
				_editTextContentToHide.setVisibility(View.VISIBLE);
				_btnSelectFileToHide.setVisibility(View.GONE);
				_checkBoxTextToHide.setChecked(true);
				_encodeParameters.setHidingText(true);
			} else if (arg0.getId() == R.id.chk_box_text_to_hide && arg1) {
				_btnSelectFileToHide.setVisibility(View.GONE);
				_editTextContentToHide.setVisibility(View.VISIBLE);
				_checkBoxFileToHide.setChecked(false);
				_encodeParameters.setHidingText(true);
			} else {
				_btnSelectFileToHide.setVisibility(View.VISIBLE);
				_editTextContentToHide.setVisibility(View.GONE);
				_checkBoxFileToHide.setChecked(true);
				_encodeParameters.setHidingText(false);
			}
			updateImageViews();
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.btn_back_encode:
					finish();
					break;
				case R.id.btn_camera:
					recordVideo();
					break;
				case R.id.btn_settings_encode:
					Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
					startActivityForResult(intent, SETTINGS_ACCESS);
					break;					
				case R.id.btn_select_video_source_encode:
					showFileChooser(CHOOSE_VIDEO_CONTAINER);
					break;
				case R.id.btn_select_video_destination_encode:
					showDirectoryChooser();
					break;
				case R.id.btn_select_file_to_hide:
					showFileChooser(CHOOSE_FILE_CONTENT);
					break;
				case R.id.btn_encode:	
					process();
					break;
				default:
					ErrorManager.getInstance().addErrorMessage("[Encode Activity] Click event not known");
					ErrorManager.getInstance().displayErrorMessages(arg0.getContext());
					break;
			}
		}
	};
	
	private TextWatcher onTextChangedListenerCryptographyKey = new TextWatcher() {
	
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (s != null) {
				_encodeParameters.setCryptographyKey(s.toString());
			} 
			updateImageViews();
		}
	};

	private TextWatcher onTextChangedListenerTextToHide = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (s != null) {
				_encodeParameters.setTextToHide(s.toString());
			}
			updateImageViews();
		}
	};
	
}
