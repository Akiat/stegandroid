package com.stegandroid.parameters;

public class EncodeParameters {

	private String _srcVideoPath;
	private String _destVideoPath;
	private String _textToHide;
	private String _fileToHidePath;
	private Boolean _hideText;
	
	public EncodeParameters() {
		_srcVideoPath = "";
		_destVideoPath = "";
		_textToHide = "";
		_fileToHidePath = "";
		_hideText = false;
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
	
	public boolean isHideText() {
		return _hideText;
	}
	
	public void setHideText(Boolean hideText) {
		_hideText = hideText;
	}
}
