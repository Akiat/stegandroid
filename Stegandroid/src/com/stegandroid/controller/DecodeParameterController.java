package com.stegandroid.controller;

import com.stegandroid.R;
import com.stegandroid.algorithms.data.CryptographyAlgorithmData;
import com.stegandroid.configuration.Configuration;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.error.ErrorManager;
import com.stegandroid.parameters.DecodeParameters;

public class DecodeParameterController {

	private boolean _quite;
	
	// TODO: Complete the control of data if necessary [ IT IS NECESSARY ;) ]
	public DecodeParameterController(boolean quite) {
		_quite = quite;
	}
	
	public Boolean controlAllData(DecodeParameters parameters) {
		boolean ret = true;
		
		ret &= controlSrcVideoPath(parameters);
		ret &= controlDestFilePath(parameters);
		ret &= controlCryptographyKey(parameters);
		return ret;
	}

	public Boolean controlSrcVideoPath(DecodeParameters parameters) {
		if (parameters == null) {
			return false;
		}
		if (parameters.getSrcVideoPath() == null || parameters.getSrcVideoPath().isEmpty()) {
			if (!_quite) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_src_video_path_empty_string);
			}
			return false;
		}
		return true;
	}
	
	public Boolean controlDestFilePath(DecodeParameters parameters) {
		if (parameters == null) {
			return false;
		}
		if (parameters.getDestFilePath() == null || parameters.getDestFilePath().isEmpty()) {
			if (!_quite) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_dest_video_empty_string);
			}
			return false;
		}
		return true;
	}

	public Boolean controlFileExtension(DecodeParameters parameters) {
		if (parameters == null) {
			return false;
		}
		if (parameters.getFileExtension() == null || parameters.getFileExtension().isEmpty()) {
			if (!_quite) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_file_extension_empty_string);
			}
			return false;
		}
		return true;
	}

	
	public boolean controlCryptographyKey(DecodeParameters parameters) {
		String path;
		CryptographyAlgorithmData data;
		
		if (parameters == null) {
			return false;
		}
		path = Preferences.getInstance().getCryptographyAlgorithm();
		if (Preferences.getInstance().getUseCryptography() && (path == null || path.isEmpty())) {
			return false;
		}
		
		data = Configuration.getInstance().getCryptographyAlgorithmDataByPath(path);
		if (parameters.getCryptographyKey() != null && data != null
				&& data.getKeyLength() == parameters.getCryptographyKey().length()) { 
			return true;
		}
		return false;
	}
}
