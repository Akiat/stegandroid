package com.stegandroid.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

import com.stegandroid.R;
import com.stegandroid.configuration.Configuration;
import com.stegandroid.tools.Utils;

public class SettingsActivity extends Activity {
	
	private Spinner		_spinAudioAlrogithm;
	private Spinner		_spinVideoAlrogithm;
	private Spinner		_spinMetadataAlrogithm;
	private CheckBox	_chkboxAudioChannel;
	private CheckBox	_chkboxVideoChannel;
	private CheckBox	_chkboxMetadataChannel;
	
	
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
		
		this.initCheckboxes();
		this.initSpinnersAlgorithm();
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
	
	private void initSpinnersAlgorithm() {
		final String audioPackageName = "com.stegandroid.algorithms.audio";
		final String videoPackageName = "com.stegandroid.algorithms.video";
		final String metadataPackageName = "com.stegandroid.algorithms.metadata";

		List<String> audioClassName;
		List<String> videoClassName;
		List<String> metadataClassName;
		ArrayAdapter<String> audioArrayAdaptater;
		ArrayAdapter<String> videoArrayAdaptater;
		ArrayAdapter<String> metadataArrayAdaptater;
		
		audioClassName = Utils.getClassesNameFromPackage(this.getApplicationContext(), audioPackageName);
		videoClassName = Utils.getClassesNameFromPackage(this.getApplicationContext(), videoPackageName);
		metadataClassName = Utils.getClassesNameFromPackage(this.getApplicationContext(), metadataPackageName);
	
		audioArrayAdaptater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, audioClassName);
		audioArrayAdaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_spinAudioAlrogithm.setAdapter(audioArrayAdaptater);
		
		videoArrayAdaptater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, videoClassName);
		videoArrayAdaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_spinVideoAlrogithm.setAdapter(videoArrayAdaptater);
		
		metadataArrayAdaptater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, metadataClassName);
		metadataArrayAdaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_spinMetadataAlrogithm.setAdapter(metadataArrayAdaptater);
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
		getMenuInflater().inflate(R.menu.option, menu);
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
	

}
