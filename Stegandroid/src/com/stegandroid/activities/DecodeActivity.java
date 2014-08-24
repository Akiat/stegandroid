package com.stegandroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.stegandroid.controller.DecodeParametersController;
import com.stegandroid.directorydialog.ChoosenDirectoryListener;
import com.stegandroid.directorydialog.DirectoryDialog;
import com.stegandroid.error.ErrorManager;
import com.stegandroid.parameters.DecodeParameters;
import com.stegandroid.process.DecodeProcess;
import com.stegandroid.tools.Utils;

public class DecodeActivity extends Activity {

	private final int CHOOSE_VIDEO_CONTAINER = 0;
	private final int SETTINGS_ACCESS = 1;
	
	// Graphical components
	private ImageButton _btnBack;
	private ImageButton _btnSettings;
	private Button _btnSelectSourceVideo;
	private Button _btnDecode;
	private Button _btnSelectFileDestination;
	private CheckBox _checkBoxDisplayContent;
	private CheckBox _checkBoxSaveIntoFile;
	private EditText _editTextCryptographyKeyDecode;
	private EditText _editTextFileExtension;
	private LinearLayout _linearLayoutCryptographyKeyDecode;
	private LinearLayout _linearLayoutSaveIntoFile;
	
	// Private attributes
	private DecodeParameters _decodeParameters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        
		_btnBack = (ImageButton) findViewById(R.id.btn_back);
		_btnSettings = (ImageButton) findViewById(R.id.btn_settings);
		_btnSelectSourceVideo = (Button) findViewById(R.id.btn_select_video_source_decode);
		_btnDecode = (Button) findViewById(R.id.btn_decode);
		_btnSelectFileDestination = (Button) findViewById(R.id.btn_select_file_destination_decode);
		_checkBoxDisplayContent = (CheckBox) findViewById(R.id.chk_box_display_content);
		_checkBoxSaveIntoFile = (CheckBox) findViewById(R.id.chk_box_save_into_file);
		_editTextCryptographyKeyDecode = (EditText) findViewById(R.id.edit_text_cryptography_key_decode);
		_editTextFileExtension = (EditText) findViewById(R.id.edit_text_file_extension);
		_linearLayoutCryptographyKeyDecode = (LinearLayout) findViewById(R.id.linear_layout_cryptography_key_decode);
		_linearLayoutSaveIntoFile = (LinearLayout) findViewById(R.id.linear_layout_save_into_file);
		
		_btnBack.setOnClickListener(onClickListener);
		_btnSettings.setOnClickListener(onClickListener);
		_btnSelectSourceVideo.setOnClickListener(onClickListener);
		_btnDecode.setOnClickListener(onClickListener);
		_btnSelectFileDestination.setOnClickListener(onClickListener);
		_checkBoxDisplayContent.setChecked(true);
		_checkBoxSaveIntoFile.setChecked(false);
		_checkBoxDisplayContent.setOnCheckedChangeListener(onCheckedChangeListener);
		_checkBoxSaveIntoFile.setOnCheckedChangeListener(onCheckedChangeListener);
		_editTextCryptographyKeyDecode.addTextChangedListener(onTextChangedListenerCryptographyKey);
		_editTextFileExtension.addTextChangedListener(onTextChangedListenerFileExtension);
		_linearLayoutSaveIntoFile.setVisibility(View.GONE);

		_decodeParameters = new DecodeParameters();
		
		updateImageViews();
		updateLinearLayoutCryptographyVisibility();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.decode, menu);
		return true;
	}
	
	private void updateLinearLayoutCryptographyVisibility() {
		if (Preferences.getInstance().getUseCryptography()) {
			_linearLayoutCryptographyKeyDecode.setVisibility(View.VISIBLE);
		} else {
			_linearLayoutCryptographyKeyDecode.setVisibility(View.GONE);
		}
	}
	
	private void updateImageViews() {
		DecodeParametersController controller = new DecodeParametersController(true);
		
		if (controller.controlSrcVideoPath(_decodeParameters)) {
			((ImageView) findViewById(R.id.img_view_valid_video_source)).setImageResource(R.drawable.btn_check_buttonless_on);
		} else {
			((ImageView) findViewById(R.id.img_view_valid_video_source)).setImageResource(R.drawable.ic_delete);
		}
		
		if (controller.controlDestFilePath(_decodeParameters)) {
			((ImageView) findViewById(R.id.img_view_valid_video_destination_decode)).setImageResource(R.drawable.btn_check_buttonless_on);
		} else {
			((ImageView) findViewById(R.id.img_view_valid_video_destination_decode)).setImageResource(R.drawable.ic_delete);
		}

		
		if (controller.controlCryptographyKey(_decodeParameters)) {
			((ImageView) findViewById(R.id.img_view_valid_key_length_decode)).setImageResource(R.drawable.btn_check_buttonless_on);
		} else {
			((ImageView) findViewById(R.id.img_view_valid_key_length_decode)).setImageResource(R.drawable.ic_delete);
		}
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
			case SETTINGS_ACCESS:
				updateLinearLayoutCryptographyVisibility();
				updateImageViews();
				break;
			default:
				ErrorManager.getInstance().addErrorMessage("[Decode Activity] Activity result not known");
				ErrorManager.getInstance().displayErrorMessages(this);
				break;
		}
	}
	
	private void callbackFileChooserVideoContainer(int requestCode, int resultCode, Intent data) {
		Uri selectedVideoLocation;
		
		if (resultCode == Activity.RESULT_OK) {
			selectedVideoLocation = data.getData();
			
			_decodeParameters.setVideoPath(Utils.getRealPathFromUri(this, selectedVideoLocation));
			if (_decodeParameters.getDestinationVideoDirectory() == null || _decodeParameters.getDestinationVideoDirectory().isEmpty()) {
				_decodeParameters.setDestinationVideoDirectory(Utils.getBasenameFromPath(_decodeParameters.getVideoPath()));
			}
			updateImageViews();
		}
	}
	
	private void showDirectoryChooser() {
		DirectoryDialog dialog = new DirectoryDialog(this);
		dialog.setChoosenDirectoryListener(onChoosenDirectoryListener);
		dialog.show();
	}
	
	private void process() {
		DecodeParametersController controller;
		
		ProgressDialog loading = ProgressDialog.show(this, "Loading", "Stegandroid extract your data...");
		_decodeParameters.setUseAudioChannel(Preferences.getInstance().getUseAudioChannel());
		_decodeParameters.setUseVideoChannel(Preferences.getInstance().getUseVideoChannel());
		controller = new DecodeParametersController(false);
		if (controller.controlAllData(_decodeParameters)){
			DecodeProcess process = new DecodeProcess();
			if (!process.decode(_decodeParameters)) {
				ErrorManager.getInstance().addErrorMessage("[Decode Activity] Impossible to find somthing in this file");
				ErrorManager.getInstance().displayErrorMessages(this);
			}
		} else {
			ErrorManager.getInstance().displayErrorMessages(this);
		}
		loading.cancel();
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
					startActivityForResult(intent, SETTINGS_ACCESS);
					break;
				case R.id.btn_select_video_source_decode:
					showFileChooser(CHOOSE_VIDEO_CONTAINER);
					break;
				case R.id.btn_select_file_destination_decode:
					showDirectoryChooser();
					break;
				case R.id.btn_decode:
					process();
					break;
				default:
					ErrorManager.getInstance().addErrorMessage("[Decode Activity] Click event not known");
					ErrorManager.getInstance().displayErrorMessages(arg0.getContext());
					break;
			}
		}
	};
	
	private ChoosenDirectoryListener onChoosenDirectoryListener = new ChoosenDirectoryListener() {

		@Override
		public void onChoosenDir(String directory) {
			_decodeParameters.setDestinationVideoDirectory(directory);
			updateImageViews();
		}
	};

	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg0.getId() == R.id.chk_box_display_content && arg1) {
				_linearLayoutSaveIntoFile.setVisibility(View.GONE);
				_checkBoxSaveIntoFile.setChecked(false);
				_decodeParameters.setDisplay(true);
			} else if (arg0.getId() == R.id.chk_box_display_content) {
				_linearLayoutSaveIntoFile.setVisibility(View.VISIBLE);
				_checkBoxSaveIntoFile.setChecked(true);
				_decodeParameters.setDisplay(false);
			} else if (arg0.getId() == R.id.chk_box_save_into_file && arg1) {
				_linearLayoutSaveIntoFile.setVisibility(View.VISIBLE);
				_checkBoxDisplayContent.setChecked(false);
				_decodeParameters.setDisplay(false);
			} else {
				_linearLayoutSaveIntoFile.setVisibility(View.GONE);
				_checkBoxDisplayContent.setChecked(true);
				_decodeParameters.setDisplay(true);
			}
			updateImageViews();
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
			_decodeParameters.setCryptographyKey(s.toString());
			updateImageViews();
		}
	};
	
	private TextWatcher onTextChangedListenerFileExtension = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			//_decodeParameters.setFileExtension(s.toString());
			updateImageViews();
		}
	};
}
