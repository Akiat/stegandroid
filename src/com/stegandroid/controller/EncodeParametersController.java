package com.stegandroid.controller;

import android.content.Context;

import com.stegandroid.R;
import com.stegandroid.error.ErrorManager;
import com.stegandroid.parameters.EncodeParameters;

public class EncodeParametersController {

	// TODO: Complete the control of data if necessary [ IT IS NECESSARY ;) ]
	public EncodeParametersController(Context context) {
	}

	public Boolean controlAllData(EncodeParameters parameters) {
		boolean ret = true;
		
		ret &= controlSrcVideoPath(parameters);
		ret &= controlDestVideoPath(parameters);
		ret &= controlTextToHide(parameters);
		ret &= controlFileToHide(parameters);
		return ret;
	}

	private Boolean controlSrcVideoPath(EncodeParameters parameters) {
		if (parameters.getSrcVideoPath() == null || parameters.getSrcVideoPath().isEmpty()) {
			ErrorManager.getInstance().addErrorMessage(R.string.error_src_video_path_empty_string);
			return false;
		}
		return true;
	}
	
	private Boolean controlDestVideoPath(EncodeParameters parameters) {
		if (parameters.getDestVideoPath() == null || parameters.getDestVideoPath().isEmpty()) {
			ErrorManager.getInstance().addErrorMessage(R.string.error_dest_video_empty_string);
			return false;
		}
		return true;
	}
	
	private Boolean controlTextToHide(EncodeParameters parameters) {
		if (parameters.isUsingHideText()) {
			if (parameters.getTextToHide() == null || parameters.getTextToHide().isEmpty()) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_text_to_hide_empty_string);
				return false;
			}
		}
		return true;
	}
	
	private Boolean controlFileToHide(EncodeParameters parameters) {
		if (!parameters.isUsingHideText()) {
			if (parameters.getFileToHidePath() == null || parameters.getFileToHidePath().isEmpty()) {
				ErrorManager.getInstance().addErrorMessage(R.string.error_file_to_hide_empty_string);
				return false;
			}
		}
		return true;
	}
}
