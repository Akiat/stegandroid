package com.stegandroid.parameters;

public class EncodeParameters {

	private String _srcVideoPath;
	private String _destVideoPath;
	private String _textToHide;
	private String _fileToHidePath;
	private String _cryptographyKey;
	private Boolean _useHideText;
	
	public EncodeParameters() {
		_srcVideoPath = "";
		_destVideoPath = "";
		_textToHide = "";
		_fileToHidePath = "";
		_useHideText = true;
	}
	
	public String getSrcVideoPath() {
		return _srcVideoPath;
	}
	
	public void setSrcVideoPath(String path) {
		_srcVideoPath = path;
	}
	
	public String getDestVideoPath() {
		return _destVideoPath;
	}
	
	public void setDestVideoPath(String path) {
		_destVideoPath = path;
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
	
	public boolean isUsingHideText() {
		return _useHideText;
	}
	
	public void setUseHideText(Boolean useHideText) {
		_useHideText = useHideText;
	}
}
