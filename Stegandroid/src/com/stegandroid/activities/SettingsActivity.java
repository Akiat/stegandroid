package com.stegandroid.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.stegandroid.R;
import com.stegandroid.algorithms.IDataAlgorithm;
import com.stegandroid.algorithms.data.SteganographyAlgorithmData.SteganographyChannelType;
import com.stegandroid.configuration.Configuration;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.error.ErrorManager;

public class SettingsActivity extends Activity {
		
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
		
		_btnBack = (ImageButton) findViewById(R.id.btn_back_settings);
		_btnBack.setOnClickListener(onClickListener);
		
		_mapClasses = new HashMap<String, String>();
		
		this.initCheckboxes();
		this.initSpinnerContentFromList(this._spinAudioAlrogithm, 
				Configuration.getInstance().getSteganographyAlgorithmByType(SteganographyChannelType.AUDIO), 
				Preferences.getInstance().getAudioAlgorithm());
		this.initSpinnerContentFromList(this._spinVideoAlrogithm, 
				Configuration.getInstance().getSteganographyAlgorithmByType(SteganographyChannelType.VIDEO),
				Preferences.getInstance().getVideoAlgorithm());
		this.initSpinnerContentFromList(this._spinMetadataAlrogithm,
				Configuration.getInstance().getSteganographyAlgorithmByType(SteganographyChannelType.METADATA), 
				Preferences.getInstance().getMetadataAlgorithm());
		this.initSpinnerContentFromList(this._spinCryptographyAlgorithm, 
				Configuration.getInstance().getCryptographyAlgorithms(), 
				Preferences.getInstance().getCryptographyAlgorithm());
		this.actualizeSpinners();
		
		// ------------------
		
		Target viewTarget = new ViewTarget(R.id.chk_box_audio_channel, this);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
        new ShowcaseView.Builder(this, true)
                .setTarget(viewTarget)
                .setContentTitle("Select the channels to use")
                .setContentText("You can select both channels and a cryptography algorithm !")
                .singleShot(42)
                .hasManualPosition(true)
                .setTextXPostion((int) (size.x * 0.05))
                .setTextYPostion((int) (size.y * 0.6))
                .hasManualWidth(true)
                .setTextWidth((int) (size.x * 0.90))
                .build();
	}
	
	private void initCheckboxes() {
		_chkboxAudioChannel.setChecked(Preferences.getInstance().getUseAudioChannel());
		_chkboxAudioChannel.setOnCheckedChangeListener(onCheckedChangeListener);
		
		_chkboxVideoChannel.setChecked(Preferences.getInstance().getUseVideoChannel());
		_chkboxVideoChannel.setOnCheckedChangeListener(onCheckedChangeListener);
		
		_chkboxMetadataChannel.setChecked(Preferences.getInstance().getUseMetadataChannel());
		_chkboxMetadataChannel.setOnCheckedChangeListener(onCheckedChangeListener);		

		_chkboxCryptography.setChecked(Preferences.getInstance().getUseCryptography());
		_chkboxCryptography.setOnCheckedChangeListener(onCheckedChangeListener);		
	}
	
	private void initSpinnerContentFromList(Spinner spinner, List<IDataAlgorithm> list, String defaultValue) {
		ArrayAdapter<String> adaptater;
		int idx = 0;
		
		if (_mapClasses == null)
			_mapClasses = new HashMap<String, String>();
		
		adaptater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
		adaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i < list.size(); ++i) {
			IDataAlgorithm algorithm = list.get(i);
			_mapClasses.put(algorithm.getDisplayName(), algorithm.getPath());
			adaptater.add(algorithm.getDisplayName());
			if (algorithm.getPath().equals(defaultValue)) {
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
					Preferences.getInstance().setUseAudioChannel(arg1);
					break;
				case R.id.chk_box_video_channel:
					Preferences.getInstance().setUseVideoChannel(arg1);
					break;
				case R.id.chk_box_metadata_channel:
					Preferences.getInstance().setUseMetadataChannel(arg1);
					break;
				case R.id.chk_box_cryptography:
					Preferences.getInstance().setUseCryptography(arg1);
					break;
				default:
					ErrorManager.getInstance().addErrorMessage("[About Activity] Checked change event not known");
					ErrorManager.getInstance().displayErrorMessages(arg0.getContext());
					break;
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
						Preferences.getInstance().setAudioAlgorithm(_mapClasses.get(key));
					else
						Preferences.getInstance().setAudioAlgorithm(null);
					break;
				case R.id.spinner_video_algorithm:
					key = (String) _spinVideoAlrogithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Preferences.getInstance().setVideoAlgorithm(_mapClasses.get(key));
					else
						Preferences.getInstance().setVideoAlgorithm(null);
					break;
				case R.id.spinner_metadata_algorithm:
					key = (String) _spinMetadataAlrogithm.getSelectedItem();
					if (_mapClasses.containsKey(key))
						Preferences.getInstance().setMetadataAlgorithm(_mapClasses.get(key));
					else
						Preferences.getInstance().setMetadataAlgorithm(null);
					break;
				case R.id.spinner_cryptography_algorithm:
					key = (String) _spinCryptographyAlgorithm.getSelectedItem();
					if (_mapClasses.containsKey(key)) {
						System.out.println("Save de crypto: " + _mapClasses.get(key));
						Preferences.getInstance().setCryptographyAlgorithm(_mapClasses.get(key));
					}
					else
						Preferences.getInstance().setCryptographyAlgorithm(null);
					break;
				default:
					ErrorManager.getInstance().addErrorMessage("[About Activity] Spinner event not known");
					ErrorManager.getInstance().displayErrorMessages(arg0.getContext());
					break;
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
				case R.id.btn_back_settings:
					finish();
					break;
				default:
					ErrorManager.getInstance().addErrorMessage("[Settings Activity] Click event not known");
					ErrorManager.getInstance().displayErrorMessages(arg0.getContext());
					break;
			}
		}
	};

}
