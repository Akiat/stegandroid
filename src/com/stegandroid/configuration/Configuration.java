package com.stegandroid.configuration;

public class Configuration {

	private boolean _useAudioChannel;
	private boolean _useVideoChannel;
	private boolean _useMetadataChannel;
	// TO DO: Complete the list of attributes

	private static Configuration s_configuration;
	
	public static Configuration	getInstance() {
		if (s_configuration == null) {
			s_configuration = new Configuration();
			s_configuration.init();
		}
		return (s_configuration);
	}
	
	private Configuration() {	
	}
	
	private void init() {
		this._useAudioChannel = false;
		this._useVideoChannel = false;
		this._useMetadataChannel = true;
	}
	
	public boolean getUseAudioChannel() {
		return _useAudioChannel;
	}

	public void setUseAudioChannel(boolean _useAudioChannel) {
		this._useAudioChannel = _useAudioChannel;
	}

	public boolean getUseVideoChannel() {
		return _useVideoChannel;
	}

	public void setUseVideoChannel(boolean _useVideoChannel) {
		this._useVideoChannel = _useVideoChannel;
	}

	public boolean getUseMetadataChannel() {
		return _useMetadataChannel;
	}

	public void setUseMetadataChannel(boolean _useMetadataChannel) {
		this._useMetadataChannel = _useMetadataChannel;
	}

}
