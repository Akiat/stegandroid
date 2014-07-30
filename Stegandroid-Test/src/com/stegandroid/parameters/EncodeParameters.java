package com.stegandroid.parameters;

public class EncodeParameters {

	private String _sourceVideoPath;
	private String _destinationVideoDirectory;
	private String _textToHide;
	private String _fileToHidePath;
	private String _cryptographyKey;
	private Boolean _hidingText;
	
	public EncodeParameters() {
		_sourceVideoPath = "";
		_destinationVideoDirectory = "";
		_textToHide = "";
		_fileToHidePath = "";
		_hidingText = true;
	}
	
	public String getSourceVideoPath() {
		return _sourceVideoPath;
	}
	
	public void setSourceVideoPath(String path) {
		_sourceVideoPath = path;
	}
	
	public String getDestinationVideoDirectory() {
		return _destinationVideoDirectory;
	}
	
	public void setDestinationVideoDirectory(String path) {
		_destinationVideoDirectory = path;
	}
	
	public String getTextToHide() {
		return _textToHide;
	}
	
	public void setTextToHide(String text) {
		_textToHide = text;
	}
	
	public String getFileToHidePath() {
		return _fileToHidePath;
	}
	
	public void setFileToHidePath(String path) {
		_fileToHidePath = path;
	}

	public String getCryptographyKey() {
		return _cryptographyKey;
	}
	
	public void setCryptographyKey(String cryptographyKey) {
		_cryptographyKey = cryptographyKey;
	}
	
	public boolean isHidingText() {
		return _hidingText;
	}
	
	public void setHidingText(Boolean useHideText) {
		_hidingText = useHideText;
	}
}
