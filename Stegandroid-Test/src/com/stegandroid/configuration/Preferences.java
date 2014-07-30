package com.stegandroid.configuration;

public class Preferences {
	
	private boolean _useAudioChannel;
	private boolean _useVideoChannel;
	private boolean _useMetadataChannel;
	private boolean _useCryptography;
	private String _audioAlgorithm;
	private String _videoAlgorithm;
	private String _metadataAlgorithm;
	private String _cryptographyAlgorithm;
	// TODO: Complete the list of attributes

	private static Preferences s_configuration;
	
	public static Preferences getInstance() {
		if (s_configuration == null) {
			s_configuration = new Preferences();
			s_configuration.init();
		}
		return (s_configuration);
	}
	
	private Preferences() {
		
	}
	
	private void init() {
		this._useAudioChannel = false;
		this._useVideoChannel = false;
		this._useMetadataChannel = false;
		this._useCryptography = false;
		this._audioAlgorithm = "";
		this._videoAlgorithm = "";
		this._metadataAlgorithm = "";
		this._cryptographyAlgorithm = "";
	}
	
	public boolean getUseAudioChannel() {
		return _useAudioChannel;
	}

	public void setUseAudioChannel(boolean useAudioChannel) {
		this._useAudioChannel = useAudioChannel;
	}

	public boolean getUseVideoChannel() {
		return _useVideoChannel;
	}

	public void setUseVideoChannel(boolean useVideoChannel) {
		this._useVideoChannel = useVideoChannel;
	}

	public boolean getUseMetadataChannel() {
		return _useMetadataChannel;
	}

	public void setUseMetadataChannel(boolean useMetadataChannel) {
		this._useMetadataChannel = useMetadataChannel;
	}
	
	public boolean getUseCryptography() {
		return _useCryptography;
	}

	public void setUseCryptography(boolean useCryptography) {
		this._useCryptography = useCryptography;
	}

	public String getAudioAlgorithm() {
		return _audioAlgorithm;
	}

	public void setAudioAlgorithm(String audioAlgorithm) {
		this._audioAlgorithm = audioAlgorithm;
	}

	public String getVideoAlgorithm() {
		return _videoAlgorithm;
	}

	public void setVideoAlgorithm(String videoAlgorithm) {
		this._videoAlgorithm = videoAlgorithm;
	}

	public String getMetadataAlgorithm() {
		return _metadataAlgorithm;
	}

	public void setMetadataAlgorithm(String metadataAlgorithm) {
		this._metadataAlgorithm = metadataAlgorithm;
	}

	public String getCryptographyAlgorithm() {
		return _cryptographyAlgorithm;
	}

	public void setCryptographyAlgorithm(String cryptographyAlgorithm) {
		this._cryptographyAlgorithm = cryptographyAlgorithm;
	}
	
}
