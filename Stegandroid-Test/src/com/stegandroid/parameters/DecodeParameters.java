package com.stegandroid.parameters;

public class DecodeParameters {

	private String _videoPath;
	private String _destinationVideoDirectory;
	private String _cryptographyKey;
	private String _displayText;
	private Boolean _display;
	private Boolean _useAudioChannel;
	private Boolean _useVideoChannel;
	private String _videoAlgorithm;
	private String _audioAlgorithm;
	private String _outputName;
	
	public DecodeParameters() {
		_videoPath = "";
		_destinationVideoDirectory = "";
		_display = true;
		_useAudioChannel = false;
		_audioAlgorithm = "com.stegandroid.algorithms.steganography.audio.AACSteganographyContainerLsb1Bit";
		_useVideoChannel = false;
		_videoAlgorithm = "com.stegandroid.algorithms.steganography.video.H264SteganographyContainerLsb";
		_outputName = "default_name.txt";
	}
	
	public Boolean getUseAudioChannel() {
		return _useAudioChannel;
	}

	public void setUseAudioChannel(Boolean audioChannel) {
		this._useAudioChannel = audioChannel;
	}

	public Boolean getUseVideoChannel() {
		return _useVideoChannel;
	}

	public void setUseVideoChannel(Boolean videoChannel) {
		this._useVideoChannel = videoChannel;
	}

	public String getVideoPath() {
		return _videoPath;
	}

	public void setVideoPath(String videoPath) {
		this._videoPath = videoPath;
	}

	public String getDestinationVideoDirectory() {
		return _destinationVideoDirectory;
	}

	public void setDestinationVideoDirectory(String destinationVideoDirectory) {
		this._destinationVideoDirectory = destinationVideoDirectory;
	}

	public String getCryptographyKey() {
		return _cryptographyKey;
	}

	public void setCryptographyKey(String cryptographyKey) {
		this._cryptographyKey = cryptographyKey;
	}

	public Boolean getDisplay() {
		return _display;
	}

	public void setDisplay(Boolean display) {
		this._display = display;
	}
	
	public String getDisplayText() {
		return _displayText;
	}

	public void setDisplayText(String displayText) {
		this._displayText = displayText;
	}

	public String getVideoAlgorithm() {
		return _videoAlgorithm;
	}

	public void setVideoAlgorithm(String videoAlgorithm) {
		this._videoAlgorithm = videoAlgorithm;
	}

	public String getAudioAlgorithm() {
		return _audioAlgorithm;
	}

	public void setAudioAlgorithm(String audioAlgorithm) {
		this._audioAlgorithm = audioAlgorithm;
	}

	public String getOutputName() {
		return _outputName;
	}

	public void setOutputName(String outputName) {
		this._outputName = outputName;
	}
}

