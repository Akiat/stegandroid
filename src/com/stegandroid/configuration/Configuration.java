package com.stegandroid.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class Configuration {

	private final String KEY_USE_AUDIO_CHANNEL = "com.stegandroid.USE_AUDIO_CHANNEL_KEY";
	private final String KEY_AUDIO_ALGORITHM = "com.stegandroid.AUDIO_ALGORITHM_KEY";

	private final String KEY_USE_VIDEO_CHANNEL = "com.stegandroid.USE_VIDEO_CHANNEL_KEY";
	private final String KEY_VIDEO_ALGORITHM = "com.stegandroid.VIDEO_ALGORITHM_KEY";
	
	private final String KEY_USE_METADATA_CHANNEL = "com.stegandroid.USE_METADATA_CHANNEL_KEY";
	private final String KEY_METADATA_ALGORITHM = "com.stegandroid.METADATA_ALGORITHM_KEY";
	
	private final String FILE_PREFERENCE = "com.stegandroid.preferences";
	
	private Activity _mainActivity;
	private boolean _useAudioChannel;
	private boolean _useVideoChannel;
	private boolean _useMetadataChannel;
	private String _audioAlgorithm;
	private String _videoAlgorithm;
	private String _metadataAlgorithm;
	// TODO: Complete the list of attributes

	private static Configuration s_configuration;
	
	public static Configuration	getInstance() {
		if (s_configuration == null) {
			s_configuration = new Configuration();
			s_configuration.init(null);
		}
		return (s_configuration);
	}
	
	public static Configuration	getInstance(Activity mainActivity) {
		if (s_configuration == null) {
			s_configuration = new Configuration();
			s_configuration.init(mainActivity);
		}
		return (s_configuration);
	}
	
	private Configuration() {
		
	}
	
	private void init(Activity activity) {
		this._useAudioChannel = false;
		this._useVideoChannel = false;
		this._useMetadataChannel = true;
		this._audioAlgorithm = "";
		this._videoAlgorithm = "";
		this._metadataAlgorithm = "";
		this._mainActivity = activity;
	}
	
	public boolean getUseAudioChannel() {
		return _useAudioChannel;
	}

	public void setUseAudioChannel(boolean useAudioChannel) {
		this._useAudioChannel = useAudioChannel;
		saveData();
	}

	public boolean getUseVideoChannel() {
		return _useVideoChannel;
	}

	public void setUseVideoChannel(boolean useVideoChannel) {
		this._useVideoChannel = useVideoChannel;
		saveData();
	}

	public boolean getUseMetadataChannel() {
		return _useMetadataChannel;
	}

	public void setUseMetadataChannel(boolean useMetadataChannel) {
		this._useMetadataChannel = useMetadataChannel;
		saveData();
	}

	public String getAudioAlgorithm() {
		return _audioAlgorithm;
	}

	public void setAudioAlgorithm(String audioAlgorithm) {
		this._audioAlgorithm = audioAlgorithm;
		saveData();
	}

	public String getVideoAlgorithm() {
		return _videoAlgorithm;
	}

	public void setVideoAlgorithm(String videoAlgorithm) {
		this._videoAlgorithm = videoAlgorithm;
		saveData();
	}

	public String getMetadataAlgorithm() {
		return _metadataAlgorithm;
	}

	public void setMetadataAlgorithm(String metadataAlgorithm) {
		this._metadataAlgorithm = metadataAlgorithm;
		saveData();
	}

	public void saveData() {
		SharedPreferences sharedPref;
		SharedPreferences.Editor editor;

		if (_mainActivity == null) {
			Log.d("DEBUG", "Configuration: Unable to save data...");
			return;
		}
		
		sharedPref = _mainActivity.getSharedPreferences(FILE_PREFERENCE, Context.MODE_PRIVATE);
		editor = sharedPref.edit();

		editor.putBoolean(KEY_USE_AUDIO_CHANNEL, _useAudioChannel);
		editor.putBoolean(KEY_USE_VIDEO_CHANNEL, _useVideoChannel);
		editor.putBoolean(KEY_USE_METADATA_CHANNEL, _useMetadataChannel);
		
		editor.putString(KEY_AUDIO_ALGORITHM, _audioAlgorithm);
		editor.putString(KEY_VIDEO_ALGORITHM, _videoAlgorithm);
		editor.putString(KEY_METADATA_ALGORITHM, _metadataAlgorithm);
		
		if (!editor.commit()) {
			Log.d("DEBUG", "Error while commiting preferences");
		}
	}
	
	public void loadData() {
		SharedPreferences sharedPref;
		
		if (_mainActivity == null) {
			Log.d("DEBUG", "Configuration: Unable to load data...");
			return;
		}
		
		sharedPref = _mainActivity.getSharedPreferences(FILE_PREFERENCE, Context.MODE_PRIVATE);

		_useAudioChannel = sharedPref.getBoolean(KEY_USE_AUDIO_CHANNEL, false);
		_useVideoChannel = sharedPref.getBoolean(KEY_USE_VIDEO_CHANNEL, false);
		_useMetadataChannel = sharedPref.getBoolean(KEY_USE_METADATA_CHANNEL, false);

		_audioAlgorithm = sharedPref.getString(KEY_AUDIO_ALGORITHM, "");
		_videoAlgorithm = sharedPref.getString(KEY_VIDEO_ALGORITHM, "");
		_metadataAlgorithm = sharedPref.getString(KEY_METADATA_ALGORITHM, "");
	}
	
}
