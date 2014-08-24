package com.stegandroid.controller;

import com.stegandroid.R;
import com.stegandroid.algorithms.data.CryptographyAlgorithmData;
import com.stegandroid.configuration.Configuration;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.error.ErrorManager;
import com.stegandroid.parameters.EncodeParameters;

public class EncodeParametersController {

	private boolean _quite;
	
	// TODO: Complete the control of data if necessary [ IT IS NECESSARY ;) ]
	public EncodeParametersController(boolean quite) {
		_quite = quite;
	}

	public Boolean controlAllData(EncodeParameters parameters) {
		boolean ret = true;
		
		ret &= controlSrcVideoPath(parameters);
		ret &= controlDestVideoPath(parameters);
		ret &= controlTextToHide(parameters);
		ret &= controlFileToHide(parameters);
		return ret;
	}

	public Boolean controlSrcVideoPath(EncodeParameters parameters) {
		if (parameters == null) {
			return false;
		}
		if (parameters.getSourceVideoPath() == null || parameters.getSourceVideoPath().isEmpty()) {
			if (!_quite) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_src_video_path_empty_string);
			}
			return false;
		}
		return true;
	}
	
	public Boolean controlDestVideoPath(EncodeParameters parameters) {
		if (parameters == null) {
			return false;
		}
		if (parameters.getDestinationVideoDirectory() == null || parameters.getDestinationVideoDirectory().isEmpty()) {
			if (!_quite) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_dest_video_empty_string);
			}
			return false;
		}
		return true;
	}
		
	public boolean controlContentToHide(EncodeParameters parameters) {
		if (parameters == null) {
			return false;
		}
		if (parameters.isHidingText()) {
			return controlTextToHide(parameters);
		}
		return controlFileToHide(parameters);
	}
	
	private Boolean controlTextToHide(EncodeParameters parameters) {
		if (parameters.isHidingText() && (parameters.getTextToHide() == null || parameters.getTextToHide().isEmpty())) {
			if (!_quite) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_text_to_hide_empty_string);
			}
			return false;
		}
		return true;
	}
	
	private Boolean controlFileToHide(EncodeParameters parameters) {
		if (!parameters.isHidingText() && (parameters.getFileToHidePath() == null || parameters.getFileToHidePath().isEmpty())) {
			if (!_quite) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_file_to_hide_empty_string);
			}
			return false;
		}
		return true;
	}
	
	public boolean controlCryptographyKey(EncodeParameters parameters) {
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
