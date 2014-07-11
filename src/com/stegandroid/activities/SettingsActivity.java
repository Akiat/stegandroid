package com.stegandroid.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

import com.stegandroid.R;
import com.stegandroid.configuration.Configuration;
import com.stegandroid.tools.Utils;

public class SettingsActivity extends Activity {
	
	private final String AUDIO_PACKAGE_NAME = "com.stegandroid.algorithms.steganography.audio";
	private final String VIDEO_PACKAGE_NAME = "com.stegandroid.algorithms.steganography.video";
	private final String METADATA_PACKAGE_NAME = "com.stegandroid.algorithms.steganography.metadata";
	private final String CRYPTOGRAPHY_PACKAGE_NAME = "com.stegandroid.algorithms.cryptography";
	
	// Graphical components
	private Spinner		_spinAudioAlrogithm;
	private Spinner		_spinVideoAlrogithm;
	private Spinner		_spinMetadataAlrogithm;
	private Spinner		_spinCryptographyAlgorithm;
	private CheckBox	_chkboxAudioChannel;
	private CheckBox	_chkboxVideoChannel;
	private CheckBox	_chkboxMetadataChannel;
	private CheckBox	_chkboxCryptography;
	private ImageButton _btnBack;
	
	// Private attributes
	private Map<String, String>	_mapClasses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	
		_chkboxAudioChannel = (CheckBox) findViewById(R.id.chk_box_audio_channel);
		_chkboxVideoChannel = (CheckBox) findViewById(R.id.chk_box_video_channel);
		_chkboxMetadataChannel = (CheckBox) findViewById(R.id.chk_box_metadata_channel);
		_chkboxCryptography = (CheckBox) findViewById(R.id.chk_box_cryptography);
		
		_spinAudioAlrogithm = (Spinner) findViewById(R.id.spinner_audio_algorithm);
		_spinVideoAlrogithm = (Spinner) findViewById(R.id.spinner_video_algorithm);
		_spinMetadataAlrogithm = (Spinner) findViewById(R.id.spinner_metadata_algorithm);
		_spinCryptographyAlgorithm = (Spinner) findViewById(R.id.spinner_cryptography_algorithm);
		
		_btnBack = (ImageButton) findViewById(R.id.btn_back);
		_btnBack.setOnClickListener(onClickListener);
		
		_mapClasses = new HashMap<String, String>();
		
		this.initCheckboxes();
		this.initSpinnerContentFromPackageName(this._spinAudioAlrogithm, AUDIO_PACKAGE_NAME, Configuration.getInstance().getAudioAlgorithm());
		this.initSpinnerContentFromPackageName(this._spinVideoAlrogithm, VIDEO_PACKAGE_NAME, Configuration.getInstance().getVideoAlgorithm());
		this.initSpinnerContentFromPackageName(this._spinMetadataAlrogithm, METADATA_PACKAGE_NAME, Configuration.getInstance().getMetadataAlgorithm());
		this.initSpinnerContentFromPackageName(this._spinCryptographyAlgorithm, CRYPTOGRAPHY_PACKAGE_NAME, Configuration.getInstance().getCryptographyAlgorithm());
		this.actualizeSpinners();
	}
	
	private void initCheckboxes() {
		_chkboxAudioChannel.setChecked(Configuration.getInstance().getUseAudioChannel());
		_chkboxAudioChannel.setOnCheckedChangeListener(onCheckedChangeListener);
		
		_chkboxVideoChannel.setChecked(Configuration.getInstance().getUseVideoChannel());
		_chkboxVideoChannel.setOnCheckedChangeListener(onCheckedChangeListener);
		
		_chkboxMetadataChannel.setChecked(Configuration.getInstance().getUseMetadataChannel());
		_chkboxMetadataChannel.setOnCheckedChangeListener(onCheckedChangeListener);		

		_chkboxCryptography.setChecked(Configuration.getInstance().getUseCryptography());
		_chkboxCryptography.setOnCheckedChangeListener(onCheckedChangeListener);		
	}
	
	private void initSpinnerContentFromPackageName(Spinner spinner, String packageName, String defaultValue) {
		List<String> classes;
		ArrayAdapter<String> adaptater;
		String readable;
		String fullpath;
		int idx = 0;
		
		if (_mapClasses == null)
			_mapClasses = new HashMap<String, String>();
		
		adaptater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
		adaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		classes = Utils.getClassesPathFromPackage(this, packageName);
		for (int i = 0; i < classes.size(); ++i) 
		{		
			fullpath = classes.get(i);
			readable = Utils.convertClassNameToReadableName(fullpath);
			_mapClasses.put(readable, fullpath);
			adaptater.add(readable);
			if (fullpath.equals(defaultValue)) {
				idx = i;
			}
		}
		spinner.setAdapter(adaptater);
		spinner.setSelection(idx);
		spinner.setOnItemSelectedListener(onItemSelectedListener);
	}
		
	private void actualizeSpinners() {
		if (!_chkboxAudioChannel.isChecked() && _spinAudioAlrogithm.isEnabled()) {
			_spinAudioAlrogithm.setEnabled(false);
			_spinAudioAlrogithm.setVisibility(View.GONE);
		} else if (_chkboxAudioChannel.isChecked() && !_spinAudioAlrogithm.isEnabled()) {
			_spinAudioAlrogithm.setEnabled(true);
			_spinAudioAlrogithm.setVisibility(View.VISIBLE);
		}
		
		if (!_chkboxVideoChannel.isChecked() && _spinVideoAlrogithm.isEnabled()) {
			_spinVideoAlrogithm.setEnabled(false);
			_spinVideoAlrogithm.setVisibility(View.GONE);
			
		} else if (_chkboxVideoChannel.isChecked() && !_spinVideoAlrogithm.isEnabled()) {
			_spinVideoAlrogithm.setEnabled(true);
			_spinVideoAlrogithm.setVisibility(View.VISIBLE);
		}
		
		if (!_chkboxMetadataChannel.isChecked() && _spinMetadataAlrogithm.isEnabled()) {
			_spinMetadataAlrogithm.setEnabled(false);
			_spinMetadataAlrogithm.setVisibility(View.GONE);
		} else if (_chkboxMetadataChannel.isChecked() && !_spinMetadataAlrogithm.isEnabled()) {
			_spinMetadataAlrogithm.setEnabled(true);
			_spinMetadataAlrogithm.setVisibility(View.VISIBLE);
		}
		
		if (!_chkboxCryptography.isChecked() && _spinCryptographyAlgorithm.isEnabled()) {
			_spinCryptographyAlgorithm.setEnabled(false);
			_spinCryptographyAlgorithm.setVisibility(View.GONE);
		} else if (_chkboxCryptography.isChecked() && !_spinCryptographyAlgorithm.isEnabled()) {
			_spinCryptographyAlgorithm.setEnabled(true);
			_spinCryptographyAlgorithm.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			switch (arg0.getId()) {
				case R.id.chk_box_audio_channel:
					Configuration.getInstance().setUseAudioChannel(arg1);
					break;
				case R.id.chk_box_video_channel:
					Configuration.getInstance().setUseVideoChannel(arg1);
					break;
				case R.id.chk_box_metadata_channel:
					Configuration.getInstance().setUseMetadataChannel(arg1);
					break;
				case R.id.chk_box_cryptography:
					Configuration.getInstance().setUseCryptography(arg1);
					break;
				default:
					Log.d("DEBUG", "There is a big problem there!");
			}
			actualizeSpinners();
		}
	};
	
	private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			String key;
			
			switch (arg0.getId()) {
				case R.id.spinner_audio_algorithm:
					key = (String) _spinAudioAlrogithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Configuration.getInstance().setAudioAlgorithm(_mapClasses.get(key));
					else
						Configuration.getInstance().setAudioAlgorithm(null);
					break;
				case R.id.spinner_video_algorithm:
					key = (String) _spinVideoAlrogithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Configuration.getInstance().setVideoAlgorithm(_mapClasses.get(key));
					else
						Configuration.getInstance().setVideoAlgorithm(null);
					break;
				case R.id.spinner_metadata_algorithm:
					key = (String) _spinMetadataAlrogithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Configuration.getInstance().setMetadataAlgorithm(_mapClasses.get(key));
					else
						Configuration.getInstance().setMetadataAlgorithm(null);
					break;
				case R.id.spinner_cryptography_algorithm:
					key = (String) _spinCryptographyAlgorithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Configuration.getInstance().setCryptographyAlgorithm(_mapClasses.get(key));
					else
						Configuration.getInstance().setCryptographyAlgorithm(null);
					break;
				default:
					Log.d("DEBUG", "There is a big problem there!");
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			return;
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.btn_back:
					finish();
					break;
				default:
					Log.d("DEBUG", "There is a big problem there!");
			}
		}
	};

}
