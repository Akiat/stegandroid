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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

import com.stegandroid.R;
import com.stegandroid.configuration.Configuration;
import com.stegandroid.tools.Utils;

public class SettingsActivity extends Activity {
	
	final String AUDIO_PACKAGE_NAME = "com.stegandroid.algorithms.audio";
	final String VIDEO_PACKAGE_NAME = "com.stegandroid.algorithms.video";
	final String METADATA_PACKAGE_NAME = "com.stegandroid.algorithms.metadata";
	
	private Spinner		_spinAudioAlrogithm;
	private Spinner		_spinVideoAlrogithm;
	private Spinner		_spinMetadataAlrogithm;
	private CheckBox	_chkboxAudioChannel;
	private CheckBox	_chkboxVideoChannel;
	private CheckBox	_chkboxMetadataChannel;
	private Map<String, String>	_mapClasses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	
		_chkboxAudioChannel = (CheckBox) findViewById(R.id.audioChannelCheckBox);
		_chkboxVideoChannel = (CheckBox) findViewById(R.id.vidoChannelCheckBox);
		_chkboxMetadataChannel = (CheckBox) findViewById(R.id.metadataChannelCheckBox);

		_spinAudioAlrogithm = (Spinner) findViewById(R.id.audioAlgorithmSpinner);
		_spinVideoAlrogithm = (Spinner) findViewById(R.id.videoAlgorithmSpinner);
		_spinMetadataAlrogithm = (Spinner) findViewById(R.id.metadataAlgorithmSpinner);
		
		_mapClasses = new HashMap<String, String>();
		
		this.initCheckboxes();
		this.initSpinnerContentFromPackageName(this._spinAudioAlrogithm, AUDIO_PACKAGE_NAME, Configuration.getInstance().getAudioAlgorithm());
		this.initSpinnerContentFromPackageName(this._spinVideoAlrogithm, VIDEO_PACKAGE_NAME, Configuration.getInstance().getVideoAlgorithm());
		this.initSpinnerContentFromPackageName(this._spinMetadataAlrogithm, METADATA_PACKAGE_NAME, Configuration.getInstance().getMetadataAlgorithm());
		this.actualizeSpinners();
	}
	
	private void initCheckboxes() {
		_chkboxAudioChannel.setChecked(Configuration.getInstance().getUseAudioChannel());
		_chkboxAudioChannel.setOnCheckedChangeListener(onCheckedChangeListener);
		
		_chkboxVideoChannel.setChecked(Configuration.getInstance().getUseVideoChannel());
		_chkboxVideoChannel.setOnCheckedChangeListener(onCheckedChangeListener);
		
		_chkboxMetadataChannel.setChecked(Configuration.getInstance().getUseMetadataChannel());
		_chkboxMetadataChannel.setOnCheckedChangeListener(onCheckedChangeListener);		
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
		} else if (_chkboxAudioChannel.isChecked() && !_spinAudioAlrogithm.isEnabled()) {
			_spinAudioAlrogithm.setEnabled(true);
		}
		
		if (!_chkboxVideoChannel.isChecked() && _spinVideoAlrogithm.isEnabled()) {
			_spinVideoAlrogithm.setEnabled(false);
		} else if (_chkboxVideoChannel.isChecked() && !_spinVideoAlrogithm.isEnabled()) {
			_spinVideoAlrogithm.setEnabled(true);
		}
		
		if (!_chkboxMetadataChannel.isChecked() && _spinMetadataAlrogithm.isEnabled()) {
			_spinMetadataAlrogithm.setEnabled(false);
		} else if (_chkboxMetadataChannel.isChecked() && !_spinMetadataAlrogithm.isEnabled()) {
			_spinMetadataAlrogithm.setEnabled(true);
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
				case R.id.audioChannelCheckBox:
					Configuration.getInstance().setUseAudioChannel(arg1);
					break;
				case R.id.vidoChannelCheckBox:
					Configuration.getInstance().setUseVideoChannel(arg1);
					break;
				case R.id.metadataChannelCheckBox:
					Configuration.getInstance().setUseMetadataChannel(arg1);
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
				case R.id.audioAlgorithmSpinner:
					key = (String) _spinAudioAlrogithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Configuration.getInstance().setAudioAlgorithm(_mapClasses.get(key));
					else
						Configuration.getInstance().setAudioAlgorithm(null);
					break;
				case R.id.videoAlgorithmSpinner:
					key = (String) _spinVideoAlrogithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Configuration.getInstance().setVideoAlgorithm(_mapClasses.get(key));
					else
						Configuration.getInstance().setVideoAlgorithm(null);
					break;
				case R.id.metadataAlgorithmSpinner:
					key = (String) _spinMetadataAlrogithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Configuration.getInstance().setMetadataAlgorithm(_mapClasses.get(key));
					else
						Configuration.getInstance().setMetadataAlgorithm(null);
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

}
