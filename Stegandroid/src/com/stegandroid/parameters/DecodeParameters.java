package com.stegandroid.parameters;

public class DecodeParameters {

	// Private attributes
	private String _srcVideoPath;
	private String _destFilePath;
	private String _cryptographyKey;
	private Boolean _displayText;
	private String _fileExtension;
	
	public DecodeParameters() {
		
	}

	public String getSrcVideoPath() {
		return _srcVideoPath;
	}

	public void setSrcVideoPath(String _srcVideoPath) {
		this._srcVideoPath = _srcVideoPath;
	}

	public String getDestFilePath() {
		return _destFilePath;
	}

	public void setDestFilePath(String _destFilePath) {
		this._destFilePath = _destFilePath;
	}
	
	public String getCryptographyKey() {
		return _cryptographyKey;
	}

	public void setCryptographyKey(String _cryptographyKey) {
		this._cryptographyKey = _cryptographyKey;
	}

	public Boolean isDisplayText() {
		return _displayText;
	}

	public void setDisplayText(Boolean _displayText) {
		this._displayText = _displayText;
	}

	public String getFileExtension() {
		return _fileExtension;
	}

	public void setFileExtension(String _fileExtension) {
		this._fileExtension = _fileExtension;
	}

}
